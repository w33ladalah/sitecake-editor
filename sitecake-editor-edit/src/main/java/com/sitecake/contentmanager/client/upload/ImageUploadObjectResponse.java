package com.sitecake.contentmanager.client.upload;


public class ImageUploadObjectResponse extends UploadObjectResponse {

	protected ImageUploadObjectResponse() {}

	public final native String getResizedUrl()/*-{
		return ( this.resizedUrl ) ? this.resizedUrl : null;
	}-*/;

	public final native int getResizedWidth()/*-{
		return ( this.resizedWidth ) ? this.resizedWidth : 0;
	}-*/;
	
	public final native int getResizedHeight()/*-{
		return ( this.resizedHeight ) ? this.resizedHeight : 0;
	}-*/;

}
