package com.sitecake.contentmanager.client.item.image;

/**
 * Representation of an image managed by the sc backend.
 * After an image has been uploaded to the backend, the image
 * is stored and resized and an instance of <code>ImageObject</code>
 * is returned back to the sc editor.
 */
public class ImageObject {
	
	/**
	 * The Image URL (src attribute of the IMG tag).
	 */
	private String src;
	
	private String srcset;
	
	private String sizes;
	
	/**
	 * The width of the resized image in px.
	 */
	private int width;
	
	/**
	 * The height of the resized image.
	 */
	private int height;

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
	
	public String getSrcset() {
		return srcset;
	}

	public void setSrcset(String srcset) {
		this.srcset = srcset;
	}

	public String getSizes() {
		return sizes;
	}

	public void setSizes(String sizes) {
		this.sizes = sizes;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Returns a cloned <code>ImageObject</code> instance.
	 */
	public ImageObject clone() {
		ImageObject clone = new ImageObject();

		clone.src = src;
		clone.srcset = srcset;
		clone.sizes = sizes;
		clone.width = width;
		clone.height = height;
		
		return clone;
	}
}
