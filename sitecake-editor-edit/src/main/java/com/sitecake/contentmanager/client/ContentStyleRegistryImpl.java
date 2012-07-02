package com.sitecake.contentmanager.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JsArrayString;

public class ContentStyleRegistryImpl implements ContentStyleRegistry {
	
	private static final String STYLES_OBJECT_NAME = "sitecakeGlobals.styles";
	
	private Map<String, Map<String, List<String>>> styles;
	
	public ContentStyleRegistryImpl() {
		styles = new HashMap<String, Map<String, List<String>>>();
		initStyles();
	}
	
	@Override
	public List<String> get(String containerName, String contentType) {
		List<String> contentTypeStyles = null;
		if ( styles.containsKey(containerName) ) {
			Map<String, List<String>> containerStyles = styles.get(containerName);
			if ( containerStyles.containsKey(contentType) ) {
				contentTypeStyles = containerStyles.get(contentType);
			}
		}
		return contentTypeStyles;
	}

	private void putStyles(String containerName, String contentType, JsArrayString styles) {

		List<String> contentTypeStyles = new ArrayList<String>();
		for ( int i=0; i < styles.length(); i++ ) {
			contentTypeStyles.add(styles.get(i));
		}
		
		Map<String, List<String>> containerStyles = this.styles.get(containerName);
		if ( containerStyles == null ) {
			containerStyles = new HashMap<String, List<String>>();
			this.styles.put(containerName, containerStyles);
		}
		
		containerStyles.put(contentType, contentTypeStyles);
	}
	
	private native void initStyles()/*-{
		var stylesObject = eval('$wnd.' + @com.sitecake.contentmanager.client.ContentStyleRegistryImpl::STYLES_OBJECT_NAME);
		
		for ( var containerName in stylesObject ) {
			var containerStyles = stylesObject[containerName];
			for ( var contentType in containerStyles ) {
				var contentTypeStyles = containerStyles[contentType];
				this.@com.sitecake.contentmanager.client.ContentStyleRegistryImpl::putStyles(Ljava/lang/String;Ljava/lang/String;Lcom/google/gwt/core/client/JsArrayString;)
						(containerName, contentType, contentTypeStyles);
			}
		}
	}-*/;
}