package com.sitecake.commons.client.util;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;


public class DomUtil {
	public static boolean hasClassName(Element element, String className) {
		if ( "".equals(element.getClassName()) ) {
			return false;
		} else {
			return JavaScriptRegExp.test("(^|.*\\s)" + className + "(\\s.*|$)", element.getClassName());
		}
	}
	
	public static void hideElement(Element element) {
		element.getStyle().setDisplay(Display.NONE);
	}
	
	public static void showElement(Element element) {
		element.getStyle().setDisplay(Display.BLOCK);
	}

	public static native void replaceElement(Element node, Element newNode) /*-{
		var p = node.parentNode;
		if (!p) {
			return;
		}
		p.insertBefore(newNode, node);
		p.removeChild(node);
	}-*/;

	private static native void execCommandNative(String cmd, String param)/*-{
		$doc.execCommand(cmd, false, param);
	}-*/;
	
	public static void execCommand(String cmd, String param) {
		try {
			execCommandNative(cmd, param);
		} catch (JavaScriptException e) {}
	}

	public static boolean queryCommandState(String cmd) {
		try {
			return queryCommandStateNative(cmd);
		} catch (JavaScriptException e) { 
			return false;
		}
	}

	private static native boolean queryCommandStateNative(String cmd) /*-{
    	return !!$doc.queryCommandState(cmd);
	}-*/;	
}
