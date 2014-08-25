package com.sitecake.publicmanager.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.sitecake.commons.client.config.Globals;
import com.sitecake.commons.client.util.DomSelector;
import com.sitecake.commons.client.util.Sha1;
import com.sitecake.commons.client.util.UrlBuilder;
import com.sitecake.commons.client.widget.ClickableElementWidget;
import com.sitecake.publicmanager.client.LoginDialog.LoginHandler;
import com.sitecake.publicmanager.client.resources.LocaleProxy;
import com.sitecake.publicmanager.client.resources.Messages;

public class LoginManagerImpl implements LoginManager {

	private Messages messages;
	
	private LoginDialog loginDialog;
	
	private String newCredential;
	
	private boolean loginAfterChange;
	
	@Inject
	protected LoginManagerImpl(TopContainer topContainer, LocaleProxy localeProxy) {
		this.messages = localeProxy.messages();
		
		loginDialog = new LoginDialog(new LoginHandler() {
			public void onLogin(String credential) {
				onLoginAttempt(credential, false);
			}

			@Override
			public void onChange(String credential, String newCredential, String newCredential2) {
				if ( !newCredential2.equals(newCredential) ) {
					loginDialog.setMessage(LoginManagerImpl.this.messages.newCredentialDoesNotMatch());
					loginDialog.setNewCredentialErrorStyle(true);
				} else if ( "".equals(newCredential) || newCredential.length() < 3 ) {
					loginDialog.setMessage(LoginManagerImpl.this.messages.newCredentialTooShort());
					loginDialog.setNewCredentialErrorStyle(true);
				} else {
					loginDialog.setMessage(LoginManagerImpl.this.messages.chaningCredential());
					loginDialog.setNewCredentialErrorStyle(false);
					
					onChangeAttempt(credential, newCredential);
				}
			}

			@Override
			public void onSwitchToChangeBox() {
				loginDialog.setMode(false);
				loginDialog.resetCredential();
				loginDialog.setMessage("");
			}

			@Override
			public void onSwitchToLoginBox() {
				loginDialog.setMode(true);
				loginDialog.resetCredential();
				loginDialog.setMessage("");
			}
		});
		
		topContainer.add(loginDialog);
		
		DomSelector domSelector = GinInjector.instance.getDomSelector();
		JsArray<Element> loginButtonElement = domSelector.select(".sc-login");
		
		if ( loginButtonElement.length() > 0 ) {
			for ( int i = 0; i < loginButtonElement.length(); i++ ) {
				ClickableElementWidget widget = new ClickableElementWidget(loginButtonElement.get(i));
				//RootPanel.get().add(widget);
				widget.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						loginDialog.setMessage("");
						loginDialog.setMode(true);
						loginDialog.show();
						event.preventDefault();
					}
				});
			}
		}
		
		if ( Globals.get().getForceLoginDialog() ) {
			loginDialog.setMode(true);
			loginDialog.show();
		}
	}
	
	private void onLoginAttempt(String credential, boolean isAfterChange) {
		this.loginAfterChange = isAfterChange;
		
		loginDialog.setProgress(true);
		loginDialog.setMessage("");

		UrlBuilder urlBuilder = new UrlBuilder(Globals.get().getSessionServiceUrl());
		String credentialHash = Sha1.hashHex(credential);
		urlBuilder.setParameter("action", "login");
		urlBuilder.setParameter("credential", URL.encodeQueryString(credentialHash));
		
		JsonpRequestBuilder requestBuilder = new JsonpRequestBuilder();
		try {
			requestBuilder.requestObject( urlBuilder.buildString(), new AsyncCallback<LoginResponse>() {
	
				@Override
				public void onFailure(Throwable caught) {
					onLoginError(caught.getMessage());
				}
	
				@Override
				public void onSuccess(LoginResponse response) {
					onLoginResponse(response);
				}

			});
		
		} catch (Throwable exception) {
			onLoginError(exception.getMessage());
		}		
	}
	
	private void onLoginResponse(LoginResponse loginResponse) {
	
		switch ( loginResponse.getStatus() ) {
			case LoginResponse.SUCCESS:
				onLoginSuccess();
				break;
			
			case LoginResponse.INVALID_CREDENTIAL:
				loginDialog.setProgress(false);
				loginDialog.setMessage(messages.loginAttemptOutcomeInvalidCredential());
				break;
				
			case LoginResponse.LOCKED:
				loginDialog.setProgress(false);
				loginDialog.setMessage(messages.loginAttemptOutcomeLocked());
				break;
			
			case LoginResponse.ERROR:
			default:
				onLoginError(loginResponse.getErrorMessage());
				break;
		}
	}
	
	private void onLoginError(String message) {
		loginDialog.setProgress(false);
		loginDialog.setMessage(messages.loginAttemptOutcomeError() + message);
	}
	
	private void onLoginSuccess() {
		if ( loginAfterChange ) {
			loginDialog.setMessage(messages.changeAttemptOutcomeGranted());
		} else {
			loginDialog.setMessage(messages.loginAttemptOutcomeGranted());
		}
		
		String protocol = Location.getProtocol();
		String hostPort = Location.getHost();
		String query = Location.getQueryString(); 
		String path = Location.getPath();
		String hash = Location.getHash();
		
		String newPath = path.replaceAll("(.*)login$", "$1");
		
		String newUrl = protocol + "//" + hostPort + newPath + query + hash;
		
		Location.replace(newUrl);
	}
	
	private void onChangeAttempt(String credential, String newCredential) {
		loginDialog.setProgress(true);
		this.newCredential = newCredential;
		
		UrlBuilder urlBuilder = new UrlBuilder(Globals.get().getSessionServiceUrl());
		String credentialHash = Sha1.hashHex(credential);
		String newCredentialHash = Sha1.hashHex(newCredential);
		urlBuilder.setParameter("action", "change");
		urlBuilder.setParameter("credential", URL.encodeQueryString(credentialHash));
		urlBuilder.setParameter("newCredential", URL.encodeQueryString(newCredentialHash));
		
		JsonpRequestBuilder requestBuilder = new JsonpRequestBuilder();
		try {
			requestBuilder.requestObject( urlBuilder.buildString(), new AsyncCallback<ChangeResponse>() {
	
				@Override
				public void onFailure(Throwable caught) {
					onChangeError(caught.getMessage());
				}
	
				@Override
				public void onSuccess(ChangeResponse response) {
					onChangeResponse(response);
				}
				
			});
		
		} catch (Throwable exception) {
			onChangeError(exception.getMessage());
		}		
	}
	
	private void onChangeResponse(ChangeResponse response) {
	
		switch ( response.getStatus() ) {
			case ChangeResponse.SUCCESS:
				onChangeSuccess();
				break;
			
			case ChangeResponse.INVALID_CREDENTIAL:
				loginDialog.setProgress(false);
				loginDialog.setMessage(messages.changeAttemptOutcomeInvalidCredential());
				break;
				
			case ChangeResponse.INVALID_NEW_CREDENTIAL:
				loginDialog.setProgress(false);
				loginDialog.setMessage(messages.changeAttemptOutcomeInvalidNewCredential());
				break;
			
			case ChangeResponse.ERROR:
			default:
				onChangeError(response.getErrorMessage());
				break;
		}
	}
	
	private void onChangeError(String message) {
		loginDialog.setProgress(false);
		loginDialog.setMessage(messages.changeAttemptOutcomeError() + message);
	}
	
	private void onChangeSuccess() {
		loginDialog.setMode(true);
		loginDialog.setMessage(messages.changeAttemptOutcomeGranted());

		onLoginAttempt(newCredential, true);
	}
	
}
