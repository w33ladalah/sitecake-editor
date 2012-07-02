package com.sitecake.contentmanager.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;
import com.sitecake.contentmanager.client.EventBus;
import com.sitecake.contentmanager.client.SynchronizationBarrier;
import com.sitecake.contentmanager.client.config.ConfigRegistry;
import com.sitecake.contentmanager.client.event.ErrorNotificationEvent;
import com.sitecake.contentmanager.client.event.ErrorNotificationEvent.Level;

public class LocaleProxyImpl implements LocaleProxy {

	private static final String INTERFACE_LOCALE = "LocaleProxyImpl.interfaceLocale";
	
	public enum LocaleCode {
		EN,
		SL,
		SR,
		ES,
		DE,
		FR,
		DK,
		IT
	}
	
	private Messages messages;
	
	private SynchronizationBarrier synchronizationBarrier;
	
	private EventBus eventBus;
	
	private ConfigRegistry configRegistry;

	@Override
	public Messages messages() {
		return messages;
	}

	@Inject
	protected LocaleProxyImpl(SynchronizationBarrier synchronizationBarrier,
			EventBus eventBus, ConfigRegistry configRegistry) {
		this.synchronizationBarrier = synchronizationBarrier;
		this.eventBus = eventBus;
		this.configRegistry = configRegistry;
		
		synchronizationBarrier.lock();
		
		LocaleCode locale = getLocaleCode();
		switch ( locale ) {
		case EN:
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					messages = GWT.create(MessagesEn.class);
					completeProcess();
				}
				
				@Override
				public void onFailure(Throwable reason) {
					failureCompletition(reason);
				}
			});
			break;
		case SL:
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					messages = GWT.create(MessagesSl.class);
					completeProcess();
				}
				
				@Override
				public void onFailure(Throwable reason) {
					failureCompletition(reason);
				}
			});			
			break;
		case SR:
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					messages = GWT.create(MessagesSr.class);
					completeProcess();
				}
				
				@Override
				public void onFailure(Throwable reason) {
					failureCompletition(reason);
				}
			});			
			break;
		case ES:
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					messages = GWT.create(MessagesEs.class);
					completeProcess();
				}
				
				@Override
				public void onFailure(Throwable reason) {
					failureCompletition(reason);
				}
			});			
			break;
		case DE:
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					messages = GWT.create(MessagesDe.class);
					completeProcess();
				}
				
				@Override
				public void onFailure(Throwable reason) {
					failureCompletition(reason);
				}
			});			
			break;
		case FR:
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					messages = GWT.create(MessagesFr.class);
					completeProcess();
				}
				
				@Override
				public void onFailure(Throwable reason) {
					failureCompletition(reason);
				}
			});			
			break;
		case DK:
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					messages = GWT.create(MessagesDk.class);
					completeProcess();
				}
				
				@Override
				public void onFailure(Throwable reason) {
					failureCompletition(reason);
				}
			});			
			break;
		case IT:
			GWT.runAsync(new RunAsyncCallback() {
				
				@Override
				public void onSuccess() {
					messages = GWT.create(MessagesIt.class);
					completeProcess();
				}
				
				@Override
				public void onFailure(Throwable reason) {
					failureCompletition(reason);
				}
			});			
			break;
		}
	}
	
	private void completeProcess() {
		synchronizationBarrier.release();
	}

	private void failureCompletition(Throwable reason) {
		LocaleProxyImpl.this.eventBus.fireEvent(new ErrorNotificationEvent(Level.FATAL, reason.getMessage()));
		synchronizationBarrier.release();
	}
	
	private LocaleCode getLocaleCode() {
		String interfaceLocale = configRegistry.get(INTERFACE_LOCALE);
		if ( interfaceLocale == null || "".equals(interfaceLocale) ) {
			return LocaleCode.EN;
		} else if ( "auto".equalsIgnoreCase(interfaceLocale) ) {
			interfaceLocale = Location.getParameter("scln");
			if ( interfaceLocale == null || "".equals(interfaceLocale) ) {
				interfaceLocale = getUserLocale();
			}
		}
		
		if ( LocaleCode.SL.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.SL;
		} else if ( LocaleCode.SR.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.SR;
		} else if ( LocaleCode.ES.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.ES;
		} else if ( LocaleCode.DE.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.DE;
		} else if ( LocaleCode.FR.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.FR;
		} else if ( LocaleCode.DK.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.DK;
		} else if ( LocaleCode.IT.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.IT;
		} else {
			return LocaleCode.EN;
		}
	}
	
	private native String getUserLocale()/*-{
		return $wnd.navigator.userLanguage || $wnd.navigator.language;		
	}-*/;
}
