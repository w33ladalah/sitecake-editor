package com.sitecake.contentmanager.client.dnd;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.sitecake.contentmanager.client.dom.DataTransfer;
import com.sitecake.contentmanager.client.dom.FileList;
import com.sitecake.contentmanager.client.item.ContentItem;
import com.sitecake.contentmanager.client.item.HasContentItemCreator;
import com.sitecake.contentmanager.client.item.fileuploader.FileUploaderItem;
import com.sitecake.contentmanager.client.item.fileuploader.FileUploaderItem.Type;
import com.sitecake.contentmanager.client.item.map.MapItem;
import com.sitecake.contentmanager.client.item.video.VideoItem;
import com.sitecake.contentmanager.client.toolbar.ContentItemCreator;

public class DnDUploadDragWidget extends Widget implements HasContentItemCreator {

	private static DnDUploadDragWidgetUiBinder uiBinder = GWT
			.create(DnDUploadDragWidgetUiBinder.class);

	interface DnDUploadDragWidgetUiBinder extends
			UiBinder<Element, DnDUploadDragWidget> {
	}

	private DataTransfer dataTransfer;
	
	private FileList fileList;
	
	public DnDUploadDragWidget() {
		setElement(uiBinder.createAndBindUi(this));
	}

	@Override
	public ContentItemCreator getContentItemCreator() {
		return new ContentItemCreator() {
			public ContentItem create() {
				ContentItem newItem = null;

				if ( fileList != null && fileList.length() > 0 ) {
					newItem = FileUploaderItem.create(Type.GENERIC, fileList);
				} else {
					String text = dataTransfer.getData("Text");
					if ( text != null && !"".equals(text) ) {
						if ( VideoItem.testText(text) ) {
							newItem = VideoItem.create(text);
						} else if ( MapItem.testText(text) ) {
							newItem = MapItem.create(text);
						}
					}
				}
				return newItem;
			}
		};		
	}

	public void setDataTransfer(DataTransfer dataTransfer) {
		this.dataTransfer = dataTransfer;
		this.fileList = ( dataTransfer != null ) ? dataTransfer.files() : null;
	}
	
	public void setFileList(FileList fileList) {
		this.dataTransfer = null;
		this.fileList = fileList;
	}
	
}
