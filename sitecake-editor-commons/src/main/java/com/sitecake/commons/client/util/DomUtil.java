package com.sitecake.commons.client.util;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.sitecake.commons.client.util.dom.CSSStyleDeclaration;


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
	
	public static double getElementInnerWidth(Element element) {
		if (element == null) return 0;
		
		double width,
				clientWidth = element.getClientWidth(), // width + padding-left + padding-right
				paddingLeft = 0,
				paddingRight = 0;
		
		try {
			paddingLeft = CSSStyleDeclaration.get(element).getPropertyValueDouble("padding-left");
		} catch (Exception e) {
			paddingLeft = 0;
		}
		
		try {
			paddingRight = CSSStyleDeclaration.get(element).getPropertyValueDouble("padding-right");
		} catch (Exception e) {
			paddingRight = 0;
		}
		
		width = clientWidth - paddingLeft - paddingRight;
		
		return (width < 0) ? 0 : width;
	}
	
	public static double getElementOuterWidth(Element element) {
		if (element == null) return 0;
		
		double width;
		width = element.getOffsetWidth();
		return width;
	}
	
	public static native String outerHtml(Element element)/*-{
		var txt, ax, el = $doc.createElement("div");
		el.appendChild(element.cloneNode(false));
		txt = el.innerHTML;      
		ax = txt.indexOf('>') + 1;
		txt = txt.substring(0, ax) + element.innerHTML + txt.substring(ax);
		el = null;
		return txt;	
	}-*/;
}
