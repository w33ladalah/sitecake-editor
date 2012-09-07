package com.sitecake.contentmanager.client.item.map;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.maps.client.Map;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.base.HasLatLng;
import com.google.gwt.maps.client.base.LatLng;
import com.sitecake.commons.client.util.JsStringUtil;

public class GoogleEmbeddedMap implements EmbeddedMap {

	private double ratio;
	
	private double width;
	
	private double height;
	
	private String publicCode;
	
	private String editCode;
	
	private String url;
	
	private Map map;
	
	@Override
	public double getRatio() {
		return ratio;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
	}

	@Override
	public void setRatio(double value) {
		ratio = value;
	}

	@Override
	public void setWidth(double value) {
		width = value;
	}

	@Override
	public void setHeight(double value) {
		height = value;
	}

	@Override
	public String getPublicCode() {
		String result = publicCode;
		result = result.replaceAll("height=\"###\"", "height=\"" + Double.valueOf(height).intValue() + "\"");
		result = result.replaceAll("width=\"###\"", "width=\"" + Double.valueOf(width).intValue() + "\"");
		return result;
	}

	@Override
	public String getEditCode() {
		String result = editCode;
		result = result.replaceAll("height=\"###\"", "height=\"" + Double.valueOf(height).intValue() + "\"");
		result = result.replaceAll("width=\"###\"", "width=\"" + Double.valueOf(width).intValue() + "\"");
		return result;		
	}

	@Override
	public EmbeddedMap cloneMap() {
		GoogleEmbeddedMap clone = new GoogleEmbeddedMap();
		clone.ratio = this.ratio;
		clone.width = this.width;
		clone.height = this.height;
		clone.publicCode = this.publicCode;
		clone.editCode = this.editCode;
		return clone;
	}

	@Override
	public void resizeTarget(JsArray<Element> target) {
		Element iframe = target.get(0);
		if ( iframe != null ) {
			int targetWidth = Double.valueOf(width).intValue();
			int targetHeight = Double.valueOf(height).intValue();
			
			iframe.setAttribute("width", String.valueOf(targetWidth));
			iframe.setAttribute("height", String.valueOf(targetHeight));
		}
		
		if ( map != null ) {
			triggerResize(map.getJso());
		}
	}

	public void generateEditMap(Element container) {
		MapOptions options = getMapOptions(url);
		map = new Map((com.google.gwt.user.client.Element)container, options);
		setKmlLayer(map.getJso(), url+"&output=kml");
	}
	
	public void removeEditMap(Element container) {
		String zoom = String.valueOf(map.getZoom());

		HasLatLng latlng = map.getCenter();
		String ll = String.valueOf(latlng.getLatitude()) + "," + String.valueOf(latlng.getLongitude());
		
		String mapTypeId = map.getMapTypeId();
		String mapType;
		MapTypeId mapTypes = new MapTypeId();
		if ( mapTypes.getTerrain().equals(mapTypeId) ) {
			mapType = "p";
		} else if ( mapTypes.getSatellite().equals(mapTypeId) ) {
			mapType = "k";
		} else if ( mapTypes.getHybrid().equals(mapTypeId) ) {
			mapType = "h";
		} else {
			mapType = "";
		}
		
		url = url.replaceAll("(\\?|&)ll=[0-9\\.,\\-]+", "$1ll=" + ll);
		
		url = url.replaceAll("(\\?|&)z=[0-9]+", "");
		url = url + "&z=" + zoom;
		
		url = url.replaceAll("(\\?|&)t=[a-z]+", "");
		if ( mapType.length() > 0 ) {
			url = url + "&t=" + mapType;
		}
		
		publicCode = "<iframe height=\"###\" frameborder=\"0\" width=\"###\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"  src=\"" + 
			url + "&output=embed" + "\"></iframe>";
		editCode = "<iframe height=\"###\" frameborder=\"0\" width=\"###\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"  src=\"" + 
			url + "&output=embed" + "\"></iframe>";
	
		map = null;
		container.setInnerHTML(getEditCode());
	}
	
	private MapOptions getMapOptions(String url) {
		MapOptions options = new MapOptions();
		java.util.Map<String, String> urlParams = new HashMap<String, String>();
		
		String[] urlParts = url.split("\\?");
		if ( urlParts != null && urlParts.length > 1 ) {
			String[] queryParts = urlParts[1].split("&(amp;)?");
			for ( int i = 0; i < queryParts.length; i++ ) {
				String[] paramParts = queryParts[i].split("=");
				String paramName = paramParts[0];
				String paramValue = paramParts.length > 1 ? paramParts[1] : "";
				urlParams.put(paramName, paramValue);
			}
		}
		
		String zoomStr = urlParams.get("z");
		if ( zoomStr == null || "".equals(zoomStr) ) {
			if ( urlParams.containsKey("q") ) {
				zoomStr = "16";
			} else {
				zoomStr = "3";
			}
		}
		int zoom = Integer.parseInt(zoomStr);
		options.setZoom(zoom);
		
		MapTypeId mapTypes = new MapTypeId();
		String mapTypeId;
		String mapTypeStr = urlParams.get("t");
		if ( "k".equals(mapTypeStr) ) {
			mapTypeId = mapTypes.getSatellite();
		} else if ( "h".equals(mapTypeStr) ) {
			mapTypeId = mapTypes.getHybrid();
		} else if ( "p".equals(mapTypeStr) ) {
			mapTypeId = mapTypes.getTerrain();
		} else {
			mapTypeId = mapTypes.getRoadmap();
		}
		options.setMapTypeId(mapTypeId);
		
		String llStr = urlParams.get("ll");
		if ( llStr != null && !"".equals(llStr) ) {
			String[] parts = llStr.split(",");
			double lat = Double.parseDouble(parts[0]);
			double lng = Double.parseDouble(parts[1]);
			LatLng ll = new LatLng(lat, lng);
			options.setCenter(ll);
		} else {
			options.setCenter(new LatLng(34.59, -21.62));
			options.setZoom(3);
		}
		
		options.setDraggable(true);
		options.setMapTypeControl(true);
		options.setNavigationControl(true);
		options.setScaleControl(true);
		options.setScrollwheel(true);
		return options;
	}
	
	private native void triggerResize(JavaScriptObject map)/*-{
		$wnd.google.maps.event.trigger(map, 'resize');
	}-*/;
	
	private native void setKmlLayer(JavaScriptObject map, String url)/*-{
		var kmlLayer = new $wnd.google.maps.KmlLayer(url, {preserveViewport: true});
		kmlLayer.setMap(map);		
	}-*/;
	
	private static final String GOOGLE_MAP_RE = "(^|\\s|\")(https?://maps\\.google(\\.[a-zA-Z]{2,4}){1,2}/[^\\?]*\\?[^\"\\s]+)";

	public static GoogleEmbeddedMap create(String input) {
		
		try {
			List<String> matches = JsStringUtil.match(GOOGLE_MAP_RE, input);
			if ( matches == null || matches.size() < 3 || matches.get(2) == null ) return null;
			
			GoogleEmbeddedMap map = new GoogleEmbeddedMap();
			String mapUrl = matches.get(2);
			if ( mapUrl.indexOf("output=embed") == -1 ) {
				mapUrl += "&output=embed";
			}
			
			map.url = mapUrl.replaceAll("&(amp;)?output=[a-z]*", "");
			map.url = mapUrl.replace("&amp;", "&");
			
			matches = JsStringUtil.match("\\swidth=\"?([0-9]+)", input);
			if ( matches != null && matches.size() > 0 ) {
				map.width = Double.parseDouble(matches.get(1));
			} else {
				map.width = -1;
			}
			
			matches = JsStringUtil.match("\\sheight=\"?([0-9]+)", input);
			if ( matches != null && matches.size() > 0 ) {
				map.height = Double.parseDouble(matches.get(1));
			} else {
				map.height = -1;
			}
			
			if ( map.width != -1 && map.height != -1 ) {
				map.ratio = map.width / map.height;
			} else {
				map.ratio = 1.0;
			}
			
			map.publicCode = "<iframe height=\"###\" frameborder=\"0\" width=\"###\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"  src=\"" + 
				map.url + "&output=embed" + "\"></iframe>";
			map.editCode = "<iframe height=\"###\" frameborder=\"0\" width=\"###\" frameborder=\"0\" scrolling=\"no\" marginheight=\"0\" marginwidth=\"0\"  src=\"" + 
				map.url + "&output=embed" + "\"></iframe>";
			
			return map;
		} catch (Throwable e) {
			return null;
		}
	}
	
	
}
