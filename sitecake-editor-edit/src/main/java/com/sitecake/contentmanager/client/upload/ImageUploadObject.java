package com.sitecake.contentmanager.client.upload;

import com.sitecake.contentmanager.client.dom.File;

public class ImageUploadObject extends UploadObject {

	private String resizedUrl;
	
	private int requestedResizedWidth;
	
	private int resizedWidth;
	
	private int resizedHeight;
	
	public String getResizedUrl() {
		return resizedUrl;
	}

	void setResizedUrl(String resizedUrl) {
		this.resizedUrl = resizedUrl;
	}

	int getRequestedResizedWidth() {
		return requestedResizedWidth;
	}

	public void setRequestedResizedWidth(int requestedResizedWidth) {
		this.requestedResizedWidth = requestedResizedWidth;
	}

	public int getResizedWidth() {
		return resizedWidth;
	}

	void setResizedWidth(int resizedWidth) {
		this.resizedWidth = resizedWidth;
	}

	public int getResizedHeight() {
		return resizedHeight;
	}

	void setResizedHeight(int resizedHeight) {
		this.resizedHeight = resizedHeight;
	}

	public ImageUploadObject(File file) {
		super(file);
	}

}
