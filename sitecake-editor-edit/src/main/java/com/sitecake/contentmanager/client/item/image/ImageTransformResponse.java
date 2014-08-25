package com.sitecake.contentmanager.client.item.image;

import com.sitecake.commons.client.util.BasicServiceResponse;

public class ImageTransformResponse extends BasicServiceResponse {
	protected ImageTransformResponse() {}

	public final native String getUrl()/*-{
		return ( this.url ) ? this.url : null;
	}-*/;	
}
