package com.sitecake.publicmanager.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.user.client.ui.RootPanel;
import com.sitecake.publicmanager.client.resources.ClientBundle;
import com.sitecake.publicmanager.client.resources.Messages;

public class Module implements EntryPoint {

	private static Messages messages = GinInjector.instance.getLocaleProxy().messages();
	
	@Override
	public void onModuleLoad() {
		
		// set uncaught exception handler
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable throwable) {
				showErrorMessageOverlay(throwable);
			}
		});

		// use a deferred command so that the handler catches onModuleLoad2() exceptions
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			
			@Override
			public void execute() {
				onModuleLoad2();
			}
		});
	}
	
	private void onModuleLoad2() {
		// inject needed global resources
		StyleInjector.inject(ClientBundle.INSTANCE.css().getText(),true);
		
		// initialize Gin mechanism
		final GinInjector injector = GinInjector.instance;
		
		// create the top container that all standalone widgets should be located within
		TopContainer topContainer = injector.getTopContainer();
		topContainer.getElement().setId("sitecake-toolbox");
		RootPanel.get().add(topContainer);
		
		injector.getLoginManager();
	}
	
	private void showErrorMessageOverlay(Throwable throwable) {
		
		String text = "Uncaught exception: ";
		while (throwable != null) {
			StackTraceElement[] stackTraceElements = throwable.getStackTrace();
			text += throwable.toString() + "\n";
			for (int i = 0; i < stackTraceElements.length; i++) {
				text += "    at " + stackTraceElements[i] + "\n";
			}
			throwable = throwable.getCause();
			if (throwable != null) {
				text += "Caused by: ";
			}
		}
		System.err.print(text);
		
		ErrorMessageOverlay overlay;
		if ( GWT.isScript() ) {
			overlay = new ErrorMessageOverlay(messages.errorMessage1(), messages.errorMessage2(), text);
		} else {
			overlay = new ErrorMessageOverlay(messages.errorMessage1(), messages.errorMessage2(), "");
		}
		
		RootPanel.get().add(overlay);
	}

}
