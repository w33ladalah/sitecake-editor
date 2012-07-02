package com.sitecake.contentmanager.client.item.map;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;

public interface EmbeddedMap {
	public double getRatio();
	public double getWidth();
	public double getHeight();
	public void setRatio(double value);
	public void setWidth(double value);
	public void setHeight(double value);
	public String getPublicCode();
	public String getEditCode();
	public EmbeddedMap cloneMap();
	public void resizeTarget(JsArray<Element> target);
}
