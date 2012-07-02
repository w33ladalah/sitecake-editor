package com.sitecake.contentmanager.client.upload;

import com.sitecake.commons.client.util.MimeType;
import com.sitecake.contentmanager.client.dom.File;

public class UploadObject {
	
	public enum Status {
		INITALIZED,
		UPLOADED,
		ERROR,
		ABORTED
	}
	
	private String id;
	
	private Status status;
	
	private File file;
	
	private String fileName;
	
	private String mimeType;
	
	private String url;

	private String errorMessage;
	
	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	void setStatus(Status status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	void setUrl(String url) {
		this.url = url;
	}

	public File getFile() {
		return file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public UploadObject(File file) {
		super();
		this.file = file;

		status = Status.INITALIZED;
		fileName = file.name();
		
		mimeType = file.mediaType();
		if ( mimeType == null || "".equals(mimeType) ) {
			String fileExt = fileName.substring(fileName.lastIndexOf('.') + 1);
			mimeType = MimeType.lookup(fileExt).toString();
		}
	}
	
}
