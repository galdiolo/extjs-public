/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.fx;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DragEvent;
import com.extjs.gxt.ui.client.event.DragListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Shim;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Adds drag behavior to any widget. Drag operations can be initiated from the
 * widget itself, or another widget, such as the header in a dialog.
 * 
 * <p/>It is possible to specify event targets that will be ignored. If the
 * target element has a 'my-nodrag' style it will not trigger a drag operation.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>DragStart</b> : DragEvent(draggable, component, event) <br>
 * Fires after a drag has started.
 * <ul>
 * <li>draggable : this</li>
 * <li>component : drag component</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragMove</b> : DragEvent(draggable, component, event)<br>
 * Fires after the mouse moves.
 * <ul>
 * <li>draggable : this</li>
 * <li>component : drag component</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragCancel</b> : DragEvent(draggable, component, event)<br>
 * Fires after a drag has been cancelled.
 * <ul>
 * <li>draggable : this</li>
 * <li>component : drag component</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragEnd</b> : DragEvent(draggable, component, event) <br>
 * Fires after a drag has ended.
 * <ul>
 * <li>draggable : this</li>
 * <li>component : drag widget</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class Draggable extends BaseObservable {

  protected int conX, conY, conWidth, conHeight;
  protected int dragStartX, dragStartY;
  protected int lastX, lastY;
  protected Rectangle startBounds;
  protected El proxyEl;

  // config
  private boolean updateZIndex = true;
  private boolean sizeProxyToSource = true;
  private boolean constrainHorizontal;
  private boolean moveAfterProxyDrag = true;
  private boolean constrainVertical;
  private boolean constrainClient = true;
  private boolean useProxy = true;
  private int xLeft = Style.DEFAULT, xRight = Style.DEFAULT;
  private int xTop = Style.DEFAULT, xBottom = Style.DEFAULT;
  private String proxyStyle = "x-drag-proxy";
  private Component container;
  private Component dragWidget;
  private Component handle;
  private boolean dragging;
  private boolean enabled = true;
  private int clientWidth, clientHeight;
  private EventPreview preview;
  private DragEvent dragEvent;
  private int startDragDistance = 2;
  private Shim shim = new Shim();

  /**
   * Creates a new draggable instance.
   * 
   * @param dragComponent the component to be dragged
   */
  public Draggable(Component dragComponent) {
    this(dragComponent, dragComponent);
  }

  /**
   * Create a new draggable instance.
   * 
   * @param dragComponent the component to be dragged
   * @param handle the component drags will be initiated from
   */
  public Draggable(final Component dragComponent, final Component handle) {
    this.dragWidget = dragComponent;
    this.handle = handle;

    handle.addListener(Event.ONMOUSEDOWN, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        onMouseDown(ce);
      }
    });

    preview = new EventPreview() {
      public boolean onEventPreview(Event event) {
        DOM.eventPreventDefault(event);
        switch (DOM.eventGetType(event)) {
          case Event.ONKEYDOWN:
            int key = DOM.eventGetKeyCode(event);
            if (key == KeyboardListener.KEY_ESCAPE && dragging) {
              cancelDrag();
            }
            break;
          case Event.ONMOUSEMOVE:
            onMouseMove(event);
            break;
          case Event.ONMOUSEUP:
            stopDrag(event);
            break;
        }
        return true;
      }
    };

    if (!handle.isRendered()) {
      handle.addListener(Events.Render, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent be) {
          handle.removeListener(Events.Render, this);
          handle.el().addEventsSunk(Event.ONMOUSEDOWN);
        }
      });
    } else {
      handle.el().addEventsSunk(Event.ONMOUSEDOWN);
    }
  }

  /**
   * Adds a listener to receive drag events.
   * 
   * @param listener the drag listener to be added
   */
  public void addDragListener(DragListener listener) {
    addListener(Events.DragStart, listener);
    addListener(Events.DragMove, listener);
    addListener(Events.DragCancel, listener);
    addListener(Events.DragEnd, listener);
  }

  /**
   * Cancels the drag if running.
   */
  public void cancelDrag() {
    if (dragging) {
      DOM.removeEventPreview(preview);
      dragging = false;
      if (isUseProxy()) {
        proxyEl.disableTextSelection(false);
        Element body = XDOM.getBody();
        DOM.removeChild(body, proxyEl.dom);
      }
      if (!isUseProxy()) {
        dragWidget.el().setPagePosition(startBounds.x, startBounds.y);
      }

      fireEvent(Events.DragCancel, new DragEvent(this));
      afterDrag();
    }
  }

  /**
   * Returns the drag container.
   * 
   * @return the drag container
   */
  public Component getContainer() {
    return container;
  }

  /**
   * Returns the drag handle.
   * 
   * @return the drag handle
   */
  public Component getDragHandle() {
    return handle;
  }

  /**
   * Returns the widget being dragged.
   * 
   * @return the drag widget
   */
  public Component getDragWidget() {
    return dragWidget;
  }

  /**
   * Returns the proxy style.
   * 
   * @return the proxy style
   */
  public String getProxyStyle() {
    return proxyStyle;
  }

  /**
   * Returns the number of pixels the cursor must move before dragging begins.
   * 
   * @return the distance in pixels
   */
  public int getStartDragDistance() {
    return startDragDistance;
  }

  /**
   * Returns true if drag is constrained to the viewport.
   * 
   * @return the contstrain client state
   */
  public boolean isConstrainClient() {
    return constrainClient;
  }

  /**
   * Returns true if horizontal movement is contstrained.
   * 
   * @return the horizontal constrain state
   */
  public boolean isConstrainHorizontal() {
    return constrainHorizontal;
  }

  /**
   * Returns true if vertical movement is constrained.
   * 
   * @return true if vertical movement constrainer
   */
  public boolean isConstrainVertical() {
    return constrainVertical;
  }

  /**
   * Returns <code>true</code> if a drag is in progress.
   * 
   * @return the drag state
   */
  public boolean isDragging() {
    return dragging;
  }

  /**
   * Returns true if the drag widget is moved after a proxy drag.
   * 
   * @return the move after proxy state
   */
  public boolean isMoveAfterProxyDrag() {
    return moveAfterProxyDrag;
  }

  /**
   * Returns true if the proxy element is sized to match the drag widget.
   * 
   * @return the size proxy to source state
   */
  public boolean isSizeProxyToSource() {
    return sizeProxyToSource;
  }

  /**
   * Returns true if the z-index is updated after a drag.
   * 
   * @return the update z-index state
   */
  public boolean isUpdateZIndex() {
    return updateZIndex;
  }

  /**
   * Returns true if proxy element is enabled.
   * 
   * @return the use proxy state
   */
  public boolean isUseProxy() {
    return useProxy;
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeDragListener(DragListener listener) {
    if (hasListeners()) {
      removeListener(Events.DragStart, listener);
      removeListener(Events.DragMove, listener);
      removeListener(Events.DragCancel, listener);
      removeListener(Events.DragEnd, listener);
    }
  }

  /**
   * True to set constrain movement to the viewport (defaults to true).
   * 
   * @param constrainClient true to constrian to viewport
   */
  public void setConstrainClient(boolean constrainClient) {
    this.constrainClient = constrainClient;
  }

  /**
   * True to stop horizontal movement (defaults to false).
   * 
   * @param constrainHorizontal true to stop horizontal movement
   */
  public void setConstrainHorizontal(boolean constrainHorizontal) {
    this.constrainHorizontal = constrainHorizontal;
  }

  /**
   * True to stop vertical movement (defaults to false).
   * 
   * @param constrainVertical true to stop vertical movement
   */
  public void setConstrainVertical(boolean constrainVertical) {
    this.constrainVertical = constrainVertical;
  }

  /**
   * Specifies a container to which the drag widget is constrained.
   * 
   * @param container the container
   */
  public void setContainer(Component container) {
    this.container = container;
  }

  /**
   * Enables dragging if the argument is <code>true</code>, and disables it
   * otherwise.
   * 
   * @param enabled the new enabled state
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * True to move source widget after a proxy drag (defaults to true).
   * 
   * @param moveAfterProxyDrag true to move after a proxy drag
   */
  public void setMoveAfterProxyDrag(boolean moveAfterProxyDrag) {
    this.moveAfterProxyDrag = moveAfterProxyDrag;
  }

  /**
   * Sets the proxy element.
   * 
   * @param element the proxy element
   */
  public void setProxy(Element element) {
    proxyEl = new El(element);
  }

  /**
   * Sets the style name used for proxy drags (defaults to 'my-drag-proxy').
   * 
   * @param proxyStyle the proxy style
   */
  public void setProxyStyle(String proxyStyle) {
    this.proxyStyle = proxyStyle;
  }

  /**
   * True to set proxy dimensions the same as the drag widget (defaults to
   * true).
   * 
   * @param sizeProxyToSource true to update proxy size
   */
  public void setSizeProxyToSource(boolean sizeProxyToSource) {
    this.sizeProxyToSource = sizeProxyToSource;
  }

  /**
   * Specifies how far the cursor must move after mousedown to start dragging
   * (defaults to 2).
   * 
   * @param startDragDistance the start distance in pixels
   */
  public void setStartDragDistance(int startDragDistance) {
    this.startDragDistance = startDragDistance;
  }

  /**
   * True if the CSS z-index should be updated on the widget being dragged.
   * Setting this value to <code>true</code> will ensure that the dragged
   * element is always displayed over all other widgets (defaults to true).
   * 
   * @param updateZIndex true update the z-index
   */
  public void setUpdateZIndex(boolean updateZIndex) {
    this.updateZIndex = updateZIndex;
  }

  /**
   * True to use a proxy widget during drag operation (defaults to true).
   * 
   * @param useProxy true use a proxy
   */
  public void setUseProxy(boolean useProxy) {
    this.useProxy = useProxy;
  }

  /**
   * Constrains the horizontal travel.
   * 
   * @param left the number of pixels the element can move to the left
   * @param right the number of pixels the element can move to the right
   */
  public void setXConstraint(int left, int right) {
    xLeft = left;
    xRight = right;
  }

  /**
   * Constrains the vertical travel.
   * 
   * @param top the number of pixels the element can move to the up
   * @param bottom the number of pixels the element can move to the down
   */
  public void setYConstraint(int top, int bottom) {
    xTop = top;
    xBottom = bottom;
  }

  protected void afterDrag() {
    XDOM.getBodyEl().removeStyleName("x-unselectable");
    XDOM.getBodyEl().removeStyleName("x-dd-cursor");
    shim.uncover();
  }

  protected El createProxy() {
    proxyEl = new El(DOM.createDiv());
    proxyEl.setVisibility(false);
    proxyEl.dom.setClassName(getProxyStyle());
    proxyEl.disableTextSelection(true);
    return proxyEl;
  }

  protected void onMouseDown(ComponentEvent ce) {
    if (!enabled || ce.event.getButton() != Event.BUTTON_LEFT) {
      return;
    }
    Element target = ce.getTarget();
    String s = DOM.getElementProperty(target, "className");
    if (s != null && s.indexOf("x-nodrag") != -1) {
      return;
    }
    
    ce.preventDefault();

    startBounds = dragWidget.el().getBounds();

    dragStartX = ce.getClientX();
    dragStartY = ce.getClientY();

    DOM.addEventPreview(preview);

    clientWidth = Window.getClientWidth() + XDOM.getBodyScrollLeft();
    clientHeight = Window.getClientHeight() + XDOM.getBodyScrollTop();

    if (container != null) {
      conX = container.getAbsoluteLeft();
      conY = container.getAbsoluteTop();
      conWidth = container.getOffsetWidth();
      conHeight = container.getOffsetHeight();
    }

    if (proxyEl != null) {
      proxyEl.setVisibility(true);
    }

  }

  protected void onMouseMove(Event event) {
    String cls = event.getTarget().getClassName();
    if (cls!=null && cls.contains("x-insert")) {
      return;
    }

    int x = DOM.eventGetClientX(event);
    int y = DOM.eventGetClientY(event);

    if (!dragging && Math.abs(dragStartX - x) > startDragDistance) {
      startDrag(event);
    }
    if (!dragging && Math.abs(dragStartY - y) > startDragDistance) {
      startDrag(event);
    }

    if (dragging) {
      if (proxyEl != null) {
        proxyEl.setVisibility(true);
      }

      int left = startBounds.x + (x - dragStartX);
      int top = startBounds.y + (y - dragStartY);

      int width = dragWidget.getOffsetWidth();
      int height = dragWidget.getOffsetHeight();

      if (useProxy) {
        width = proxyEl.getWidth();
        height = proxyEl.getHeight();
      }

      if (constrainClient) {
        left = Math.max(left, 0);
        top = Math.max(top, 0);

        left = Math.max(0, Math.min(clientWidth - width, left));

        if (Math.min(clientHeight - height, top) > 0) {
          top = Math.max(2, Math.min(clientHeight - height, top));
        }
      }

      if (container != null) {
        left = Math.max(left, conX);
        left = Math.min(conX + conWidth - dragWidget.getOffsetWidth(), left);
        top = Math.min(conY + conHeight - dragWidget.getOffsetHeight(), top);
        top = Math.max(top, conY);
      }

      if (xLeft != Style.DEFAULT) {
        left = Math.max(startBounds.x - xLeft, left);
      }
      if (xRight != Style.DEFAULT) {
        left = Math.min(startBounds.x + xRight, left);
      }

      if (xTop != Style.DEFAULT) {
        top = Math.max(startBounds.y - xTop, top);

      }
      if (xBottom != Style.DEFAULT) {
        top = Math.min(startBounds.y + xBottom, top);
      }

      if (constrainHorizontal) {
        left = startBounds.x;
      }
      if (constrainVertical) {
        top = startBounds.y;
      }

      lastX = left;
      lastY = top;

      if (hasListeners()) {
        dragEvent.source = this;
        dragEvent.component = dragWidget;
        dragEvent.event = event;
        dragEvent.doit = true;
        dragEvent.x = x;
        dragEvent.y = y;
        fireEvent(Events.DragMove, dragEvent);
      }

      if (useProxy) {
        int tl = (hasListeners() && dragEvent.x != x) ? dragEvent.x : left;
        int tt = (hasListeners() && dragEvent.y != y) ? dragEvent.y : top;
        proxyEl.setLeftTop(tl, tt);
      } else {
        dragWidget.el().setPagePosition(left, top);
      }

      if (!dragEvent.doit) {
        cancelDrag();
      }
    }

  }

  protected void startDrag(Event event) {
    XDOM.getBodyEl().addStyleName("x-unselectable");
    XDOM.getBodyEl().addStyleName("x-dd-cursor");
    dragWidget.el().makePositionable();
    
    shim.cover(true);

    if (updateZIndex) {
      dragWidget.el().updateZIndex(0);
    }

    DragEvent de = new DragEvent(this);
    de.component = dragWidget;
    de.event = event;
    de.x = startBounds.x;
    de.y = startBounds.y;
    de.doit = true;

    lastX = startBounds.x;
    lastY = startBounds.y;

    if (!fireEvent(Events.DragStart, de)) {
      stopDrag(event);
      return;
    }

    if (dragEvent == null) {
      dragEvent = new DragEvent(this);
    }

    dragging = true;
    if (useProxy) {
      if (proxyEl == null) {
        createProxy();
      }
      Element body = XDOM.getBody();
      DOM.appendChild(body, proxyEl.dom);
      proxyEl.setVisibility(false);
      proxyEl.setStyleAttribute("zIndex", XDOM.getTopZIndex());
      proxyEl.makePositionable(true);

      if (sizeProxyToSource) {
        proxyEl.setBounds(startBounds);
      } else {
        proxyEl.setXY(startBounds.x, startBounds.y);
      }

      // did listeners change size?
      if (de.height > 0) {
        proxyEl.setHeight(de.height, true);
      }
      if (de.width > 0) {
        proxyEl.setWidth(de.width, true);
      }
    }
  }

  protected void stopDrag(Event event) {
    DOM.removeEventPreview(preview);

    if (dragging) {
      dragging = false;
      if (isUseProxy()) {
        if (isMoveAfterProxyDrag()) {
          Rectangle rect = proxyEl.getBounds();
          dragWidget.el().setPagePosition(rect.x, rect.y);
        }
        proxyEl.disableTextSelection(false);
        Element body = XDOM.getBody();
        DOM.removeChild(body, proxyEl.dom);
      }
      DragEvent de = new DragEvent(this);
      de.component = dragWidget;
      de.event = event;
      de.x = lastX;
      de.y = lastY;
      fireEvent(Events.DragEnd, de);
      afterDrag();
    }
  }

}
