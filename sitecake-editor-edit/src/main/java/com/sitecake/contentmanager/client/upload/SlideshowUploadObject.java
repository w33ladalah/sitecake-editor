package com.sitecake.contentmanager.client.upload;

import com.sitecake.contentmanager.client.dom.File;

public class SlideshowUploadObject extends ImageUploadObject {

	private String thumbnailUrl;
	
	private int requestedThumbnailDimension;
	
	private int thumbnailWidth;
	
	private int thumbnailHeight;

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	int getRequestedThumbnailDimension() {
		return requestedThumbnailDimension;
	}

	public void setRequestedThumbnailDimension(int requestedThumbnailDimension) {
		this.requestedThumbnailDimension = requestedThumbnailDimension;
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}
	
	public SlideshowUploadObject(File file) {
		super(file);
	}
}
