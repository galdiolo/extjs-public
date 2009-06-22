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

  private static List<SplitBar> attachedBars;
  private static DelayedTask delayedTask;

  static {
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

  private boolean autoSize = true;
  private int yOffset = 0;
  private int xOffset = 0;
  private int minSize = 10;
  private int maxSize = 2000;
  private int handleWidth = 5;
  private int barWidth = 2;
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
    this.resizeEl = resizeWidget.el();

    listener = new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        switch (ce.type) {
          case Events.Attach:
            if (!disabled) {
              el().insertBefore(resizeEl.dom);
              ComponentHelper.doAttach(SplitBar.this);
              sync();
              attachedBars.add(SplitBar.this);
            }
            break;
          case Events.Detach:
            if (!disabled) {
              ComponentHelper.doDetach(SplitBar.this);
              el().removeFromParent();
              attachedBars.remove(SplitBar.this);
            }
            break;
          case Events.Resize:
            delay.delay(400);
            break;
        }
      }
    };

    if (style == LayoutRegion.SOUTH || style == LayoutRegion.NORTH) {
      setStyleName("x-hsplitbar");
    } else {
      setStyleName("x-vsplitbar");
    }

    resizeWidget.addListener(Events.Attach, listener);
    resizeWidget.addListener(Events.Detach, listener);
    resizeWidget.addListener(Events.Resize, listener);

    draggable = new Draggable(this);
    draggable.setUpdateZIndex(false);
    draggable.setStartDragDistance(0);
    draggable.setProxyStyle("x-splitbar-proxy");

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

    if (resizeWidget.isAttached()) {
      BaseEvent be = createComponentEvent(null);
      be.type = Events.Attach;
      listener.handleEvent(be);
    }

    delay = new DelayedTask(new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        sync();
      }
    });

    setElement(DOM.createDiv());
    disableTextSelection(true);
    el().makePositionable(true);
    sinkEvents(Event.MOUSEEVENTS);
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
    draggable.setContainer(container);
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    SplitBarEvent e = new SplitBarEvent(this);
    e.event = event;
    return e;
  }

  /**
   * Returns the bar width.
   * 
   * @return the bar width
   */
  public int getBarWidth() {
    return barWidth;
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
   * Returns the handle width.
   * 
   * @return the handle width
   */
  public int getHandleWidth() {
    return handleWidth;
  }

  /**
   * Returns the maximum size.
   * 
   * @return the max size
   */
  public int getMaxSize() {
    return maxSize;
  }

  /**
   * @return the minSize
   */
  public int getMinSize() {
    return minSize;
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
   * Returns the x offset.
   * 
   * @return the xOffset the x offset value
   */
  public int getXOffset() {
    return xOffset;
  }

  /**
   * Returns the y offset.
   * 
   * @return the y offset
   */
  public int getYOffset() {
    return yOffset;
  }

  /**
   * Returns the auto size state.
   * 
   * @return true if auto size is enabled
   */
  public boolean isAutoSize() {
    return autoSize;
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

  /**
   * True to update the size of the the resize widget after a drag operation
   * using a proxy (defaults to true).
   * 
   * @param autoSize the auto size state
   */
  public void setAutoSize(boolean autoSize) {
    this.autoSize = autoSize;
  }

  /**
   * Sets the width of drag proxy during resizing (defaults to 2).
   * 
   * @param barWidth the bar width
   */
  public void setBarWidth(int barWidth) {
    this.barWidth = barWidth;
  }

  /**
   * Sets the width of the drag handles (defaults to 5).
   * 
   * @param handleWidth the handle width
   */
  public void setHandleWidth(int handleWidth) {
    this.handleWidth = handleWidth;
  }

  /**
   * Sets the maximum size of the resize widget (defaults to 2000).
   * 
   * @param maxSize the maximum size
   */
  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  /**
   * Sets he minimum size of the resize widget (defaults to 10).
   * 
   * @param minSize the minimum size
   */
  public void setMinSize(int minSize) {
    this.minSize = minSize;
  }

  /**
   * The amount of pixels the bar should be offset to the left (defaults to 0).
   * 
   * @param xOffset the xOffset to set
   */
  public void setXOffset(int xOffset) {
    this.xOffset = xOffset;
  }

  /**
   * Sets the amount of pixels the bar should be offset to the top (defaults to
   * 0).
   * 
   * @param yOffset the yOffset to set
   */
  public void setYOffset(int yOffset) {
    this.yOffset = yOffset;
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
        el().setBounds(x + getYOffset(), y + h + getXOffset(), w, getHandleWidth(), false);
        break;
      case WEST:
        el().setBounds(x - getHandleWidth() + getYOffset(), y + getXOffset(), getHandleWidth(), h,
            false);
        break;
      case NORTH:
        el().setBounds(x + getYOffset(), y - getHandleWidth() + getXOffset(), w, getHandleWidth(),
            false);
        break;
      case EAST:
        el().setBounds(x + w + getYOffset(), y + getXOffset(), getHandleWidth(), h, false);
        break;
    }

  }

  private void onCancelDrag(BaseEvent be) {
    resizeWidget.enableEvents(true);
    sync();
  }

  private void onEndDrag(DragEvent de) {
    int x = de.x;
    int y = de.y;
    int width = resizeWidget.getOffsetWidth();
    int height = resizeWidget.getOffsetHeight();

    int diffY = y - startBounds.y;
    int diffX = x - startBounds.x;

    resizeWidget.enableEvents(true);

    SplitBarEvent be = new SplitBarEvent(this);

    switch (region) {
      case NORTH: {
        be.size = height - diffY;
        if (isAutoSize()) {
          resizeEl.setY(y).setHeight(height);
        }
        break;
      }
      case SOUTH: {
        be.size = height + diffY;
        if (isAutoSize()) {
          resizeWidget.setHeight(diffY);
        }
        break;
      }
      case WEST: {
        be.size = width - diffX;
        if (isAutoSize()) {
          el().setX(x);
          resizeWidget.setWidth(width - diffX);
        }
        break;
      }
      case EAST: {
        be.size = width + diffX;
        if (isAutoSize()) {
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
      de.width = getBarWidth();
    } else {
      de.height = getBarWidth();
    }

    SplitBarEvent se = new SplitBarEvent(this);
    se.dragEvent = de;
    fireEvent(Events.DragStart, se);

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

    int c1 = size - getMinSize();
    if (size < getMinSize()) {
      c1 = 0;
    }
    int c2 = Math.max(getMaxSize() - size, 0);
    if (v) {
      draggable.setConstrainVertical(true);
      draggable.setXConstraint(region == LayoutRegion.WEST ? c2 : c1, region == LayoutRegion.WEST
          ? c1 : c2);
    } else {
      draggable.setConstrainHorizontal(true);
      draggable.setYConstraint(region == LayoutRegion.NORTH ? c2 : c1, region == LayoutRegion.NORTH
          ? c1 : c2);
    }
  }

}
