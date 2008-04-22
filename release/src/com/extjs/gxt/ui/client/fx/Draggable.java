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
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DragEvent;
import com.extjs.gxt.ui.client.event.DragListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TypedListener;
import com.extjs.gxt.ui.client.util.Observable;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

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
public class Draggable extends Observable {

  /**
   * True to use a proxy widget during drag operation (defaults to true).
   */
  public boolean useProxy = true;

  /**
   * The style name used for proxy drags (defaults to 'my-drag-proxy').
   */
  public String proxyStyle = "x-drag-proxy";

  /**
   * True to stop vertical movement (defaults to false).
   */
  public boolean constrainVertical;

  /**
   * True to stop horizontal movement (defaults to false).
   */
  public boolean constrainHorizontal;

  /**
   * True to set constrain movement to the viewport (defaults to true).
   */
  public boolean constrainClient = true;

  /**
   * True to move source widget aftet a proxy drag (defaults to true).
   */
  public boolean moveAfterProxyDrag = true;

  /**
   * If specified, drag will be constrained by the container bounds.
   */
  public Component container;

  /**
   * True to set proxy dimensions the same as the drag widget (defaults to
   * true).
   */
  public boolean sizeProxyToSource = true;

  /**
   * True if the CSS z-index should be updated on the widget being dragged.
   * Setting this value to <code>true</code> will ensure that the dragged
   * element is always displayed over all other widgets (defaults to true).
   */
  public boolean updateZIndex = true;

  private Component dragWidget;
  private Component handle;
  private boolean dragging;
  private boolean enabled = true;
  private int dragStartX, dragStartY;
  private int lastX, lastY;
  private int clientWidth, clientHeight;
  private int conX, conY, conWidth, conHeight;
  private Rectangle startBounds;
  private El proxyEl;
  private EventPreview preview;
  private DragEvent dragEvent;
  private int xLeft = Style.DEFAULT, xRight = Style.DEFAULT;
  private int xTop = Style.DEFAULT, xBottom = Style.DEFAULT;

  /**
   * Creates a new draggable instance.
   * 
   * @param dragWidget the widget to be dragged
   */
  public Draggable(Component dragWidget) {
    this(dragWidget, dragWidget);
  }

  /**
   * Create a new draggable instance.
   * 
   * @param dragWidget the widget to be dragged
   * @param handle the widget drags will be initiated from
   */
  public Draggable(final Component dragWidget, final Component handle) {
    this.dragWidget = dragWidget;
    this.handle = handle;

    handle.addListener(Event.ONMOUSEDOWN, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        onMouseDown(ce);
      }
    });

    preview = new EventPreview() {
      public boolean onEventPreview(Event event) {
        DOM.eventCancelBubble(event, true);
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
          handle.el.addEventsSunk(Event.ONMOUSEDOWN);
        }
      });
    } else {
      handle.el.addEventsSunk(Event.ONMOUSEDOWN);
    }
  }

  /**
   * Adds a listener to receive drag events.
   * 
   * @param listener the drag listener to be added
   */
  public void addDragListener(DragListener listener) {
    TypedListener l = new TypedListener(listener);
    addListener(Events.DragStart, l);
    addListener(Events.DragMove, l);
    addListener(Events.DragCancel, l);
    addListener(Events.DragEnd, l);
  }

  /**
   * Returns the drag handle.
   * 
   * @return the drag handle
   */
  public Widget getDragHandle() {
    return handle;
  }

  /**
   * Returns the widget being dragged.
   * 
   * @return the drag widget
   */
  public Widget getDragWidget() {
    return dragWidget;
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
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeDragListener(DragListener listener) {
    if (eventTable == null) return;
    eventTable.unhook(Events.DragStart, listener);
    eventTable.unhook(Events.DragMove, listener);
    eventTable.unhook(Events.DragCancel, listener);
    eventTable.unhook(Events.DragEnd, listener);
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

  public void setProxy(Element element) {
    proxyEl = new El(element);
  }

  private void afterDrag() {
    El.fly(XDOM.getBody()).removeStyleName("x-unselectable");
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        dragWidget.enableEvents(true);
      }
    });
  }

  public void cancelDrag() {
    if (dragging) {
      DOM.removeEventPreview(preview);
      dragging = false;
      if (useProxy) {
        proxyEl.disableTextSelection(false);
        Element body = XDOM.getBody();
        DOM.removeChild(body, proxyEl.dom);
        proxyEl = null;
      }
      if (!useProxy) {
        dragWidget.el.setPagePosition(startBounds.x, startBounds.y);
      }

      fireEvent(Events.DragCancel);
      afterDrag();
    }
  }

  private void onMouseDown(ComponentEvent ce) {
    if (!enabled) {
      return;
    }
    Element target = ce.getTarget();
    String s = DOM.getElementProperty(target, "className");
    if (s != null && s.indexOf("x-nodrag") != -1) {
      return;
    }

    startBounds = XDOM.getBounds(dragWidget.getElement(), true);

    dragWidget.enableEvents(false);
    startDrag(ce.event);

    DOM.addEventPreview(preview);

    clientWidth = Window.getClientWidth() + XDOM.getBodyScrollLeft();
    clientHeight = Window.getClientHeight() + XDOM.getBodyScrollTop();

    dragStartX = ce.getClientX();
    dragStartY = ce.getClientY();

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

  private void onMouseMove(Event event) {
    if (proxyEl != null) {
      proxyEl.setVisibility(true);
    }
    int x = DOM.eventGetClientX(event);
    int y = DOM.eventGetClientY(event);

    if (dragging) {
      int left = startBounds.x + (x - dragStartX);
      int top = startBounds.y + (y - dragStartY);

      int width = dragWidget.getOffsetWidth();
      int height = dragWidget.getOffsetHeight();

      if (constrainClient) {
        left = Math.max(left, 0);
        top = Math.max(top, 0);
        left = Math.min(clientWidth - width, left);

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

      if (useProxy) {
        proxyEl.setLeftTop(left, top);
      } else {
        dragWidget.el.setPagePosition(left, top);
      }

      dragEvent.source = this;
      dragEvent.component = dragWidget;
      dragEvent.event = event;
      dragEvent.doit = true;
      fireEvent(Events.DragMove, dragEvent);

      if (!dragEvent.doit) {
        cancelDrag();
      }
    }

  }

  protected El createProxy() {
    proxyEl = new El(DOM.createDiv());
    proxyEl.setVisibility(false);
    proxyEl.setStyleName(proxyStyle);
    proxyEl.disableTextSelection(true);
    return proxyEl;
  }

  private void startDrag(Event event) {
    XDOM.getBodyEl().addStyleName("x-unselectable");

    dragWidget.el.makePositionable();

    if (updateZIndex) {
      dragWidget.el.updateZIndex(0);
    }

    DragEvent de = new DragEvent();
    de.draggable = this;
    de.component = dragWidget;
    de.event = event;
    de.x = startBounds.x;
    de.y = startBounds.y;
    
    lastX = startBounds.x;
    lastY = startBounds.y;
    fireEvent(Events.DragStart, de);

    if (dragEvent == null) {
      dragEvent = new DragEvent();
      dragEvent.draggable = this;
    }

    dragging = true;
    if (useProxy) {
      if (proxyEl == null) {
        createProxy();

      }
      Element body = XDOM.getBody();
      DOM.appendChild(body, proxyEl.dom);
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

  private void stopDrag(Event event) {
    if (dragging) {
      DOM.removeEventPreview(preview);
      dragging = false;
      if (useProxy) {
        if (moveAfterProxyDrag) {
          Rectangle rect = proxyEl.getBounds();
          dragWidget.el.setPagePosition(rect.x, rect.y);
        }
        proxyEl.disableTextSelection(false);
        Element body = XDOM.getBody();
        DOM.removeChild(body, proxyEl.dom);
        proxyEl = null;
      }
      DragEvent de = new DragEvent();
      de.draggable = this;
      de.component = dragWidget;
      de.event = event;
      de.x = lastX;
      de.y = lastY;
      fireEvent(Events.DragEnd, de);
      afterDrag();
    }
  }

}
