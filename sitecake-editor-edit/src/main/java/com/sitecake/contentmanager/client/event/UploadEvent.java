package com.sitecake.contentmanager.client.event;

import java.util.List;

import com.google.gwt.event.shared.GwtEvent;
import com.sitecake.contentmanager.client.item.fileuploader.FileUploaderItem;
import com.sitecake.contentmanager.client.toolbar.ContentItemCreator;

public class UploadEvent extends GwtEvent<UploadHandler> {

	private static final Type<UploadHandler> TYPE = new Type<UploadHandler>();

	public static Type<UploadHandler> getType() {
		return TYPE;
	}

	private FileUploaderItem fileUploaderItem;
	
	private List<ContentItemCreator> contentItemCreators;
	
	public FileUploaderItem getFileUploaderItem() {
		return fileUploaderItem;
	}

	public List<ContentItemCreator> getContentItemCreators() {
		return contentItemCreators;
	}

	public UploadEvent(FileUploaderItem fileUploaderItem, List<ContentItemCreator> contentItemCreators) {
		this.fileUploaderItem = fileUploaderItem;
		this.contentItemCreators = contentItemCreators;
	}

	@Override
	public final Type<UploadHandler> getAssociatedType() {
		return TYPE;
	}
	
	@Override
	protected void dispatch(UploadHandler handler) {
		handler.onUpload(this);
	}

}
