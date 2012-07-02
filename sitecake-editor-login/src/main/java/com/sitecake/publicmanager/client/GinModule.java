package com.sitecake.publicmanager.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.sitecake.commons.client.util.DomSelector;
import com.sitecake.commons.client.util.impl.SizzleDomSelector;
import com.sitecake.publicmanager.client.resources.LocaleProxy;
import com.sitecake.publicmanager.client.resources.LocaleProxyImpl;

public class GinModule extends AbstractGinModule {

	@Override
	protected void configure() {
		
		bind(DomSelector.class).to(SizzleDomSelector.class).in(Singleton.class);
		
		bind(LoginManager.class).to(LoginManagerImpl.class).in(Singleton.class);
		
		bind(TopContainer.class).in(Singleton.class);
		
		bind(LocaleProxy.class).to(LocaleProxyImpl.class).in(Singleton.class);
	}

}
