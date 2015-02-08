package com.sitecake.contentmanager.client.item.image;

import com.sitecake.commons.client.util.BasicServiceResponse;

public class ImageTransformResponse extends BasicServiceResponse {
	protected ImageTransformResponse() {}

	public final native String getSrc()/*-{
		return ( this.src ) ? this.src : null;
	}-*/;
	
	public final native String getSrcset()/*-{
		return ( this.srcset ) ? this.srcset : null;
	}-*/;
	
	public final native String getSizes()/*-{
		return ( this.sizes ) ? this.sizes : null;
	}-*/;	
}
