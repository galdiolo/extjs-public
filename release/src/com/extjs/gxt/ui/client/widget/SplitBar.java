/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DragEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SplitBarEvent;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Creates draggable splitter on the side of a widget.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Resize</b> : SplitBarEvent(splitBar, size)<br>
 * <div>Fires after the split bar has been moved.</div>
 * <ul>
 * <li>splitBar : this</li>
 * <li>size : the new size</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragStart</b> : SplitBarEvent(splitBar, dragEvent)<br>
 * <div>Fires after a drag has started.</div>
 * <ul>
 * <li>splitBar : this</li>
 * <li>dragEvent : the drag event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>DragEnd</b> : SplitBarEvent(splitBar, dragEvent)<br>
 * <div>Fires after a drag has ended.</div>
 * <ul>
 * <li>splitBar : this</li>
 * <li>dragEvent : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dl>
 */
public class SplitBar extends BoxComponent {

  /**
   * Transparent shim that allows drags over iframes.
   */
  private static Html shim;
  private static List<SplitBar> attachedBars;
  private static DelayedTask delayedTask;

  static {
    shim = new Html();
    shim.shim = true;
    shim.setStyleName("my-splitbar-shim");
    shim.setSize("2000", "2000");
    shim.setStyleAttribute("position", "absolute");
    RootPanel.get().add(shim);
    shim.setVisible(false);
    attachedBars = new ArrayList<SplitBar>();

    delayedTask = new DelayedTask(new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        int count = attachedBars.size();
        for (int i = 0; i < count; i++) {
          SplitBar bar = attachedBars.get(i);
          bar.sync();
        }
      }
    });
  }

  static void updateHandles() {
    delayedTask.delay(400);
  }

  /**
   * The minimum size of the resize widget (defaults to 10).
   */
  public int minSize = 10;

  /**
   * The maximum size of the resize widget (defaults to 2000).
   */
  public int maxSize = 2000;

  /**
   * The width of drag proxy during resizing (defaults to 2).
   */
  public int barWidth = 2;

  /**
   * The width of the drag handles (defaults to 5).
   */
  public int handleWidth = 5;

  /**
   * True to update the size of the the resize widget after a drag operation
   * using a proxy (defaults to true).
   */
  public boolean autoSize = true;

  /**
   * Sets the amount of pixels the bar should be offset to the top (defaults to
   * 0).
   */
  public int yOffset = 0;

  /**
   * The amount of pixels the bar should be offset to the left (defaults to 0).
   */
  public int xOffset = 0;

  private El resizeEl;
  private BoxComponent resizeWidget;
  private BoxComponent containerWidget;
  private Draggable draggable;
  private Rectangle startBounds;
  private Listener listener;
  private DelayedTask delay;
  private LayoutRegion region;

  /**
   * Creates a new split bar.
   * 
   * @param style the bar location
   * @param resizeWidget the widget being resized
   */
  public SplitBar(final LayoutRegion style, final BoxComponent resizeWidget) {
    this.region = style;
    this.resizeWidget = resizeWidget;
    this.resizeEl = resizeWidget.el;

    listener = new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        switch (ce.type) {
          case Events.Attach:
            el.insertBefore(resizeEl.dom);
            WidgetHelper.doAttach(SplitBar.this);
            sync();
            attachedBars.add(SplitBar.this);
            break;
          case Events.Detach:
            WidgetHelper.doDetach(SplitBar.this);
            el.removeFromParent();
            attachedBars.remove(SplitBar.this);
            break;
          case Events.Resize:
            delay.delay(400);
            break;
        }
      }
    };

    resizeWidget.addListener(Events.Attach, listener);
    resizeWidget.addListener(Events.Detach, listener);
    resizeWidget.addListener(Events.Resize, listener);

    setElement(DOM.createDiv());
    rendered = true;

    if (style == LayoutRegion.SOUTH || style == LayoutRegion.NORTH) {
      setStyleName("x-hsplitbar");
    } else {
      setStyleName("x-vsplitbar");
    }
    el.makePositionable(true);

    draggable = new Draggable(this);
    draggable.updateZIndex = false;
    draggable.proxyStyle = "x-splitbar-proxy";

    Listener dragListener = new Listener<DragEvent>() {
      public void handleEvent(DragEvent be) {
        if (be.type == Events.DragStart) {
          onStartDrag(be);
        }
        if (be.type == Events.DragEnd) {
          onEndDrag(be);
        }

        if (be.type == Events.DragCancel) {
          onCancelDrag(be);
        }
      }

    };
    draggable.addListener(Events.DragStart, dragListener);
    draggable.addListener(Events.DragEnd, dragListener);
    draggable.addListener(Events.DragCancel, dragListener);

    sinkEvents(Event.MOUSEEVENTS);

    if (resizeWidget.isAttached()) {
      BaseEvent be = new BaseEvent();
      be.type = Events.Attach;
      listener.handleEvent(be);
    }

    delay = new DelayedTask(new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        sync();
      }
    });
  }

  /**
   * Creates a new split bar.
   * 
   * @param style the bar location
   * @param resizeWidget the widget being resized
   * @param container the widget the split bar proxy will be sized to
   */
  public SplitBar(LayoutRegion style, BoxComponent resizeWidget, BoxComponent container) {
    this(style, resizeWidget);
    this.containerWidget = container;
    draggable.container = container;
  }

  /**
   * Returns the split bar's draggable instance.
   * 
   * @return the draggable instance
   */
  public Draggable getDraggable() {
    return draggable;
  }

  /**
   * Returns the resize widget.
   * 
   * @return the resize widget
   */
  public Component getResizeWidget() {
    return resizeWidget;
  }

  /**
   * Removes the split bar from the resize widget.
   */
  public void release() {
    resizeWidget.removeListener(Events.Attach, listener);
    resizeWidget.removeListener(Events.Detach, listener);
    resizeWidget.removeListener(Events.Resize, listener);
    RootPanel.get().remove(this);
  }

  public void sync() {
    if (!isAttached() || !resizeWidget.isAttached()) {
      return;
    }
    Rectangle rect = resizeEl.getBounds();
    int x = rect.x;
    int y = rect.y;

    if (!XDOM.isVisibleBox) {
      y -= resizeEl.getFrameWidth("t");
      x -= resizeEl.getFrameWidth("l");
    }

    int w = rect.width;
    int h = rect.height;

    switch (region) {
      case SOUTH:
        el.setBounds(x + yOffset, y + h + xOffset, w, handleWidth, false);
        break;
      case WEST:
        el.setBounds(x - barWidth + yOffset, y + xOffset, handleWidth, h, false);
        break;
      case NORTH:
        el.setBounds(x + yOffset, y - barWidth + xOffset, w, handleWidth, false);
        break;
      case EAST:
        el.setBounds(x + w + yOffset, y + xOffset, handleWidth, h, false);
        break;
    }

  }

  private void onCancelDrag(BaseEvent be) {
    shim.setVisible(false);
    resizeWidget.enableEvents(true);
    sync();
  }

  private void onEndDrag(DragEvent bee) {
    shim.setVisible(false);

    int x = bee.x;
    int y = bee.y;
    int width = resizeWidget.getOffsetWidth();
    int height = resizeWidget.getOffsetHeight();

    int diffY = y - startBounds.y;
    int diffX = x - startBounds.x;

    resizeWidget.enableEvents(true);

    SplitBarEvent be = new SplitBarEvent(this);

    switch (region) {
      case NORTH: {
        be.size = height - diffY;
        if (autoSize) {
          resizeEl.setY(y).setHeight(height);
        }
        break;
      }
      case SOUTH: {
        be.size = height + diffY;
        if (autoSize) {
          resizeWidget.setHeight(diffY);
        }
        break;
      }
      case WEST: {
        be.size = width - diffX;
        if (autoSize) {
          el.setX(x);
          resizeWidget.setWidth(width - diffX);
        }
        break;
      }
      case EAST: {
        be.size = width + diffX;
        if (autoSize) {
          resizeWidget.setWidth(diffX);
        }
        break;
      }
    }
    be.type = Events.DragEnd;
    be.component = this;
    fireEvent(Events.DragEnd, be);

    fireEvent(Events.Resize, be);
    sync();
  }

  private void onStartDrag(DragEvent de) {
    // adjust width of proxy
    if (region == LayoutRegion.WEST || region == LayoutRegion.EAST) {
      de.width = barWidth;
    } else {
      de.height = barWidth;
    }
    
    SplitBarEvent se = new SplitBarEvent(this);
    se.dragEvent = de;
    fireEvent(Events.DragStart, se);
    
    shim.setVisible(true);
    shim.show();
    shim.el.makePositionable(true);
    shim.el.updateZIndex(-1);

    resizeWidget.enableEvents(false);

    if (containerWidget != null) {
      switch (region) {
        case WEST:
        case EAST:
          int h = containerWidget.getHeight(true);
          de.height = h;
          break;
        case NORTH:
        case SOUTH:
          int w = containerWidget.getWidth(true);
          de.width = w;
          break;
      }
    }

    startBounds = new Rectangle();
    startBounds.y = de.y;
    startBounds.x = de.x;

    boolean v = region == LayoutRegion.WEST || region == LayoutRegion.EAST;
    int size;
    if (v) {
      size = resizeEl.getWidth();
    } else {
      size = resizeEl.getHeight();
    }

    int c1 = size - minSize;
    if (size < minSize) {
      c1 = 0;
    }
    int c2 = Math.max(maxSize - size, 0);
    if (v) {
      draggable.constrainVertical = true;
      draggable.setXConstraint(region == LayoutRegion.WEST ? c2 : c1,
          region == LayoutRegion.WEST ? c1 : c2);
    } else {
      draggable.constrainHorizontal = true;
      draggable.setYConstraint(region == LayoutRegion.NORTH ? c2 : c1,
          region == LayoutRegion.NORTH ? c1 : c2);
    }
  }

}
