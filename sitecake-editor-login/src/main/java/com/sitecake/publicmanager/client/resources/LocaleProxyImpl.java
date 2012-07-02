package com.sitecake.publicmanager.client.resources;

import com.google.gwt.core.client.GWT;

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
	
	@Override
	public Messages messages() {
		return messages;
	}

	protected LocaleProxyImpl() {
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
		}
	}
	
	private LocaleCode getLocaleCode() {
		String interfaceLocale = getConfigParamValue(INTERFACE_LOCALE, "en");
		if ( interfaceLocale.equalsIgnoreCase(LocaleCode.EN.name()) ) {
			return LocaleCode.EN;
		} else if ( interfaceLocale.equalsIgnoreCase(LocaleCode.SL.name()) ) {
			return LocaleCode.SL;
		} else if ( interfaceLocale.equalsIgnoreCase(LocaleCode.SR.name()) ) {
			return LocaleCode.SR;
		} else if ( interfaceLocale.equalsIgnoreCase(LocaleCode.ES.name()) ) {
			return LocaleCode.ES;
		} else if ( interfaceLocale.equalsIgnoreCase(LocaleCode.DE.name()) ) {
			return LocaleCode.DE;
		} else if ( interfaceLocale.equalsIgnoreCase(LocaleCode.FR.name()) ) {
			return LocaleCode.FR;
		} else if ( interfaceLocale.equalsIgnoreCase(LocaleCode.DK.name()) ) {
			return LocaleCode.DK;
		} else if ( interfaceLocale.equalsIgnoreCase(LocaleCode.IT.name()) ) {
			return LocaleCode.IT;
		} else {
			return LocaleCode.EN;
		}
	}	
	
	private native String getConfigParamValue(String paramName, String defaultValue)/*-{
		//console.log(sitecakeGlobals, sitecakeGlobals.config, sitecakeGlobals.config[paramName]);
		if ( $wnd.sitecakeGlobals && $wnd.sitecakeGlobals.config &&
			$wnd.sitecakeGlobals.config[paramName] ) {
				return $wnd.sitecakeGlobals.config[paramName];
		} else {
				return defaultValue;
		}
	}-*/;
}
