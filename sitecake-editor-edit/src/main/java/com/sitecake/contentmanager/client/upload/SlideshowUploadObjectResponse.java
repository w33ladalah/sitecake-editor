package com.sitecake.contentmanager.client.upload;

public class SlideshowUploadObjectResponse extends ImageUploadObjectResponse {

	protected SlideshowUploadObjectResponse() {}

	public final native String getThumbnailUrl()/*-{
		return ( this.thumbnailUrl ) ? this.thumbnailUrl : null;
	}-*/;

	public final native int getThumbnailWidth()/*-{
		return ( this.thumbnailWidth ) ? this.thumbnailWidth : 0;
	}-*/;

	public final native int getThumbnailHeight()/*-{
		return ( this.thumbnailHeight ) ? this.thumbnailHeight : 0;
	}-*/;
	
}
