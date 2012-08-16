package com.sitecake.publicmanager.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window.Location;
import com.google.inject.Inject;
import com.sitecake.commons.client.config.ConfigRegistry;

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
		IT,
		RU,
		CS
	}
	
	private Messages messages;
	
	private ConfigRegistry configRegistry;
	
	@Override
	public Messages messages() {
		return messages;
	}

	@Inject
	protected LocaleProxyImpl(ConfigRegistry configRegistry) {
		this.configRegistry = configRegistry;
		
		LocaleCode locale = getLocaleCode();
		switch ( locale ) {
		case EN:
			messages = GWT.create(MessagesEn.class);
			break;
		case SL:
			messages = GWT.create(MessagesSl.class);
			break;
		case SR:
			messages = GWT.create(MessagesSr.class);
			break;
		case ES:
			messages = GWT.create(MessagesEs.class);
			break;
		case DE:
			messages = GWT.create(MessagesDe.class);
			break;
		case FR:
			messages = GWT.create(MessagesFr.class);
			break;
		case DK:
			messages = GWT.create(MessagesDk.class);
			break;
		case IT:
			messages = GWT.create(MessagesIt.class);
			break;
		case RU:
			messages = GWT.create(MessagesRu.class);
			break;
		case CS:
			messages = GWT.create(MessagesCs.class);
			break;
		}
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
		} else if ( LocaleCode.RU.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.RU;
		} else if ( LocaleCode.CS.name().equalsIgnoreCase(interfaceLocale) ) {
			return LocaleCode.CS;
		} else {
			return LocaleCode.EN;
		}
	}
	
	private native String getUserLocale()/*-{
		return $wnd.navigator.userLanguage || $wnd.navigator.language;		
	}-*/;
	
}
