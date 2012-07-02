package com.sitecake.commons.client.util;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class CSSStyleDeclaration extends JavaScriptObject {

	protected CSSStyleDeclaration() {
	}
	
	public static final native CSSStyleDeclaration get(Element element)/*-{
		return $doc.defaultView.getComputedStyle(element, '');
	}-*/;

	public final native String getPropertyValue(String propertyName)/*-{
		return this.getPropertyValue(propertyName);
	}-*/;
	
	public final int getPropertyValueInt(String propertyName) {
		String value = getPropertyValue(propertyName);
		int result = 0;
		if ( propertyName != null && !"".equals(propertyName) ) {
			result = Integer.valueOf(value.replaceAll("px", "").trim());
		}
		return result;
	}
	
	public final double getPropertyValueDouble(String propertyName) {
		String value = getPropertyValue(propertyName);
		double result = 0;
		if ( propertyName != null && !"".equals(propertyName) ) {
			result = Double.valueOf(value.replaceAll("px", "").trim());
		}
		return result;
	}	
}
