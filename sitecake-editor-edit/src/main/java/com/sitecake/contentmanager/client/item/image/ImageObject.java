package com.sitecake.contentmanager.client.item.image;

/**
 * Representation of an image managed by the sc backend.
 * After an image has been uploaded to the backend, the image
 * is stored and resized and an instance of <code>ImageObject</code>
 * is returned back to the sc editor.
 */
public class ImageObject {

	/**
	 * The image object id - an UUID.
	 */
	private String id;
	
	/**
	 * The URL of the uploaded original image.
	 */
	private String url;
	
	/**
	 * The URL of the processed/resized image.
	 */
	private String resizedUrl;
	
	/**
	 * The width of the resized image.
	 */
	private int resizedWidth;
	
	/**
	 * The height of the resized image.
	 */
	private int resizedHeight;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResizedUrl() {
		return resizedUrl;
	}

	public void setResizedUrl(String resizedUrl) {
		this.resizedUrl = resizedUrl;
	}

	public int getResizedWidth() {
		return resizedWidth;
	}

	public void setResizedWidth(int resizedWidth) {
		this.resizedWidth = resizedWidth;
	}

	public int getResizedHeight() {
		return resizedHeight;
	}

	public void setResizedHeight(int resizedHeight) {
		this.resizedHeight = resizedHeight;
	}

	/**
	 * Returns a cloned <code>ImageObject</code> instance.
	 */
	public ImageObject clone() {
		ImageObject clone = new ImageObject();
		
		clone.id = id;
		clone.url = url;
		clone.resizedUrl = resizedUrl;
		clone.resizedWidth = resizedWidth;
		clone.resizedHeight = resizedHeight;
		
		return clone;
	}
}
