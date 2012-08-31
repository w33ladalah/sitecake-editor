package com.sitecake.contentmanager.client.item.map;

import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.sitecake.commons.client.util.CSSStyleDeclaration;
import com.sitecake.commons.client.util.DomSelector;
import com.sitecake.commons.client.util.DomUtil;
import com.sitecake.contentmanager.client.EventBus;
import com.sitecake.contentmanager.client.GinInjector;
import com.sitecake.contentmanager.client.commons.Axis;
import com.sitecake.contentmanager.client.commons.Point;
import com.sitecake.contentmanager.client.commons.Rectangle;
import com.sitecake.contentmanager.client.event.ErrorNotificationEvent;
import com.sitecake.contentmanager.client.item.ContentItem;
import com.sitecake.contentmanager.client.resources.Messages;

public class MapItem extends ContentItem {
	
	public static final String DISCRIMINATOR = "sc-map";
	
	public static final String CONTENT_TYPE_NAME = "MAP";
	
	interface MapItemUiBinder extends UiBinder<Element, MapItem> {}
	
	private static MapItemUiBinder uiBinder = GWT.create(MapItemUiBinder.class);
	
	interface CssStyle extends CssResource {
		String working();
		String edit();
	}
	
	@UiField
	CssStyle cssStyle;
	
	@UiField
	DivElement codeContainer;
	
	@UiField
	DivElement selectOverlay;
	
	@UiField
	DivElement resizeControles;
	
	@UiField
	DivElement controlOverlay;
	
	@UiField
	Element handleNW;
	
	@UiField
	Element handleNE;
	
	@UiField
	Element handleSW;
	
	@UiField
	Element handleSE;
	
	private Point mousePosStart;
	
	private Point mousePosCurrent;
	
	private double startWidth;
	
	private double startHeight;
	
	private double dx;
	
	private double dy;
	
	private boolean mouseDown = false;
	
	private boolean mouseMoved = false;
	
	private boolean modified = false;
	
	private String oldEditCode;
	
	private boolean resizeRatioLocked;
	
	private Axis dragAxis;
	
	private Rectangle RESIZE_MIN_RECT = new Rectangle(new Point(0, 0), 50, 30);
	
	private Rectangle RESIZE_MAX_RECT = new Rectangle(new Point(0, 0), 1, 1);
	
	private EventBus eventBus = GinInjector.instance.getEventBus();
	
	private Messages messages = GinInjector.instance.getLocaleProxy().messages();
	
	private DomSelector domSelector = GinInjector.instance.getDomSelector();
	
	public static MapItem create(String text) {
		MapItem item = GWT.create(MapItem.class);
		try {
			item.init(text);
		} catch (IllegalArgumentException ex) {
			item.eventBus.fireEventDeferred(new ErrorNotificationEvent(
					ErrorNotificationEvent.Level.WARNING, item.messages.invalidMapInputCode()));
			item = null;
		}
		return item;
	}
	
	private GoogleEmbeddedMap embeddedMap;
	
	protected MapItem() {
		super();
	}

	void init(String text) {
		init(text, null);
	}
	
	void init(Element origElement) {
		init(origElement.getInnerHTML(), origElement);
	}
	
	private void init(String text, Element origElement) throws IllegalArgumentException {
		Element element = uiBinder.createAndBindUi(this);
		if ( origElement != null ) {
			DomUtil.replaceElement(origElement, element);
		}
		setElement(element);

		embeddedMap = parse(text);
		if ( embeddedMap == null ) {
			throw new IllegalArgumentException();
		}
		codeContainer.setInnerHTML(embeddedMap.getEditCode());
		
		mousePosStart = new Point(0, 0);
		mousePosCurrent = new Point(0, 0);
		
		resize(embeddedMap.getWidth(), embeddedMap.getHeight());
		
		initHandlers();
	}
	
	private void initHandlers() {
		addDomHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				mouseDown(event);
			}
		}, MouseDownEvent.getType());

		addDomHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				mouseUp(event);
			}
		}, MouseUpEvent.getType());
		
		addDomHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				mouseMove(event);
			}
		}, MouseMoveEvent.getType());
	}
	
	private void mouseDown(MouseDownEvent event) {
		if ( !edited ) {
			event.stopPropagation();
			event.preventDefault();
			return;
		}
		
		mouseMoved = false;
		if ( !mouseDown && shouldCapture(event) ) {
			event.stopPropagation();
			mouseDown = true;
			dragAxis = getDragAxis(event.getNativeEvent().getEventTarget());
			
			int x = event.getClientX() + Window.getScrollLeft();
			int y = event.getClientY() + Window.getScrollTop();
			mousePosStart.setXY(x, y);
			startWidth = embeddedMap.getWidth();
			startHeight = embeddedMap.getHeight();
			
			resizeRatioLocked = event.getNativeEvent().getShiftKey();
			
			RESIZE_MAX_RECT.setWidth(getMaxWidth());
			if ( resizeRatioLocked ) {
				RESIZE_MAX_RECT.setHeight(RESIZE_MAX_RECT.getWidth() / embeddedMap.getRatio());
				RESIZE_MIN_RECT.setHeight(RESIZE_MIN_RECT.getWidth() / embeddedMap.getRatio());
			} else {
				RESIZE_MAX_RECT.setHeight(10000);
			}
			
			
			DOM.setCapture(getElement());
		}
		event.preventDefault();
	}
	
	private void mouseUp(MouseUpEvent event) {
		if ( !edited ) {
			event.stopPropagation();
			event.preventDefault();
			return;
		}
		
		if ( mouseDown ) {
			mouseDown = false;
			DOM.releaseCapture(getElement());
			if ( mouseMoved ) {
				dragStop();
			}
		}
	}
	
	private void mouseMove(MouseMoveEvent event) {
		if ( !edited ) {
			event.stopPropagation();
			event.preventDefault();
			return;
		}
		
		if ( mouseDown ) {
			modified = true;
			int x = event.getClientX() + Window.getScrollLeft();
			int y = event.getClientY() + Window.getScrollTop();
			mousePosCurrent.setXY(x, y);
			dx = mousePosCurrent.getX() - mousePosStart.getX();
			dy = mousePosCurrent.getY() - mousePosStart.getY();

			if ( !mouseMoved ) {
				mouseMoved = true;
				DOMUtil.cancelAllDocumentSelections();
				dragStart();
			} else {
				dragMove();
			}
		
		}
		DOM.eventPreventDefault(DOM.eventGetCurrentEvent());
	}
	
	private boolean shouldCapture(MouseDownEvent event) {
		boolean result = false;
		
		EventTarget eventTarget = event.getNativeEvent().getEventTarget();
		result = ( handleNW.equals(eventTarget) || handleNE.equals(eventTarget) || 
			handleSW.equals(eventTarget) || handleSE.equals(eventTarget) );
		
		return result;
	}
	
	private Axis getDragAxis(EventTarget eventTarget) {
		Axis axis = Axis.NONE;
		
		if ( handleNW.equals(eventTarget) ) {
			axis = Axis.NW;
		} else if ( handleNE.equals(eventTarget) ) {
			axis = Axis.NE;
		} else if ( handleSW.equals(eventTarget) ) {
			axis = Axis.SW;
		} else if ( handleSE.equals(eventTarget) ) {
			axis = Axis.SE;
		}

		return axis;
	}
	
	private void dragStart() {
		getElement().addClassName(cssStyle.working());
	}
	
	private void dragStop() {
		getElement().removeClassName(cssStyle.working());
		//eventBus.fireEventDeferred(new EditCompleteEvent());			
	}
	
	private void dragMove() {
		if ( dragAxis.equals(Axis.NW) || dragAxis.equals(Axis.SW) ) {
			dx = -dx;
		}
		if ( dragAxis.equals(Axis.NW) || dragAxis.equals(Axis.NE) ) {
			dy = -dy;
		}
		
		resize();			
	}

	
	@Override
	public void setSelected(boolean select) {
		if ( select && !selected ) {
			selected = true;
			selectOverlay.getStyle().setDisplay(Display.BLOCK);
		} else if ( !select && selected ){
			selected = false;
			selectOverlay.getStyle().setDisplay(Display.NONE);
		}
	}
	
	@Override
	public ContentItem cloneItem() {
		MapItem clone = MapItem.create(embeddedMap.getPublicCode());
		cloneItem(clone);
		return clone;
	}

	@Override
	public String getContentTypeName() {
		return CONTENT_TYPE_NAME;
	}
	
	@Override
	public String getItemSelector() {
		return "div." + DISCRIMINATOR;
	}

	@Override
	public String getHtml() {
		return "<div class=\"" + DISCRIMINATOR + "\" " + 
				"style=\"width:" + embeddedMap.getWidth() + "px;height:" + 
				embeddedMap.getHeight() + "px\">" +
				embeddedMap.getPublicCode() + 
			"</div>";
	}
	
	@Override
	public void startEditing(String mode) {
		if ( edited ) return;
		
		edited = true;
		setSelected(false);
		
		modified = false;
		oldEditCode = embeddedMap.getEditCode();
		getElement().addClassName(cssStyle.edit());
		codeContainer.setInnerHTML("");
		embeddedMap.generateEditMap(codeContainer);
	}

	@Override
	public boolean stopEditing(boolean cancel) {
		getElement().removeClassName(cssStyle.edit());
		embeddedMap.removeEditMap(codeContainer);
		boolean dirty = !embeddedMap.getEditCode().equals(oldEditCode);
		dirty |= super.stopEditing(cancel) | modified;
		return dirty;
	}

	private int getMaxWidth() {
		Double maxWidth = 0.0;
		
		if ( container != null ) {
			maxWidth = CSSStyleDeclaration.get(container.getElement()).getPropertyValueDouble("width");
			CSSStyleDeclaration cssStyle = CSSStyleDeclaration.get(getElement());
			maxWidth -= cssStyle.getPropertyValueInt("margin-left");
			maxWidth -= cssStyle.getPropertyValueInt("margin-right");
			maxWidth -= cssStyle.getPropertyValueInt("padding-left");
			maxWidth -= cssStyle.getPropertyValueInt("padding-right");
			maxWidth -= cssStyle.getPropertyValueInt("border-left-width");
			maxWidth -= cssStyle.getPropertyValueInt("border-right-width");
		}
		
		return maxWidth.intValue();
	}
	
	@Override
	public void onInclusion() {
		super.onInclusion();
		// TODO: check why is this called three times on every drag/drop
		int maxWidth = getMaxWidth();
		if ( embeddedMap.getWidth() <= 0.0 || embeddedMap.getWidth() > maxWidth ) {
			resize(maxWidth, maxWidth/embeddedMap.getRatio());
		}
	}
	
	private void resize() {
		// dx, dy, RESIZE_MAX_RECT, RESIZE_MIN_RECT, resizeRatioLocked, dragAxis
		if ( resizeRatioLocked ) {
			dy = dx / embeddedMap.getRatio();
		}
		
		double newWidth = startWidth + dx;
		double newHeight = startHeight + dy;
		
		if ( !resizeRatioLocked ) {
			embeddedMap.setRatio(newWidth / newHeight);
		}
		
		if ( resizeRatioLocked ) {
			if ( newWidth < RESIZE_MIN_RECT.getWidth() ) {
				newWidth = RESIZE_MIN_RECT.getWidth();
				newHeight = RESIZE_MIN_RECT.getHeight();
			} else if ( newWidth > RESIZE_MAX_RECT.getWidth() ) {
				newWidth = RESIZE_MAX_RECT.getWidth();
				newHeight = RESIZE_MAX_RECT.getHeight();
			}			
		} else {
			if ( newWidth < RESIZE_MIN_RECT.getWidth() ) {
				newWidth = RESIZE_MIN_RECT.getWidth();
			} else if ( newWidth > RESIZE_MAX_RECT.getWidth() ) {
				newWidth = RESIZE_MAX_RECT.getWidth();
			}
			if ( newHeight < RESIZE_MIN_RECT.getHeight() ) {
				newHeight = Double.valueOf(RESIZE_MIN_RECT.getHeight()).intValue();
			} else if ( newHeight > RESIZE_MAX_RECT.getHeight() ) {
				newHeight = Double.valueOf(RESIZE_MAX_RECT.getHeight()).intValue();
			}
		}
		
		resize(newWidth, newHeight);
	}
	
	private void resize(double width, double height) {
		embeddedMap.setWidth(width);
		embeddedMap.setHeight(height);
		
		int targetWidth = Double.valueOf(width).intValue();
		int targetHeight = Double.valueOf(height).intValue();
		
		getElement().getStyle().setWidth(targetWidth, Unit.PX);
		getElement().getStyle().setHeight(targetHeight, Unit.PX);
		JsArray<Element> target = domSelector.select("iframe", codeContainer);
		embeddedMap.resizeTarget(target);
	}
	
	public static GoogleEmbeddedMap parse(String input) {
		GoogleEmbeddedMap embeddedVideo = GoogleEmbeddedMap.create(input);
		
		return embeddedVideo;
	}
	
	public static boolean testText(String input) {
		return parse(input) != null;
	}
	
}
