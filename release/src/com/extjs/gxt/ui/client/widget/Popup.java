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
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * A panel that can be displayed over other widgets.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeShow</b> : ComponentEvent(component)<br>
 * <div>Fires before the popup is displayed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Show</b> : ComponentEvent(component)<br>
 * <div>Fires after a popup is displayed.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeShow</b> : ComponentEvent(component)<br>
 * <div>Fires before the popup is hidden. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Hide</b> : ComponentEvent(component)<br>
 * <div>Fires after a popup is hidden.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.x-popup (the popup itself)</dd>
 * </dl>
 */
public class Popup extends Container implements EventPreview {

  /**
   * The yOffset when constrainViewport == true (defaults to 15).
   */
  private int yOffset = 15;

  /**
   * The xOffset when constrainViewport == true (defaults to 10).
   */
  private int xOffset = 10;

  /**
   * True to enable event preview (defaults to true).
   */
  private boolean eventPreview = true;

  /**
   * True to enable animations when showing and hiding (defaults to false).
   */
  private boolean animate;

  /**
   * True to move focus to the popup when being opened (defaults to true).
   */
  private boolean autoFocus = true;

  /**
   * True to close the popup when the user clicks outside of the menu (default
   * to true).
   */
  private boolean autoHide = true;

  /**
   * True to ensure popup is dislayed within the browser's viewport.
   */
  private boolean constrainViewport = true;

  /**
   * The default {@link El#alignTo} anchor position value for this menu relative
   * to its element of origin (defaults to "tl-bl?").
   */
  private String defaultAlign = "tl-bl?";

  private List<Element> ignoreElements;
  private Element alignElem;
  private String alignPos;
  private int[] alignOffsets;
  private Point alignPoint;

  /**
   * Creates a new popup panel.
   */
  public Popup() {
    baseStyle = "x-podpup";
    shim = true;
  }

  /**
   * Centers the panel within the viewport.
   */
  public void center() {
    if (rendered) {
      el.center();
    }
  }

  /**
   * Returns the default alignment.
   * 
   * @return the default align
   */
  public String getDefaultAlign() {
    return defaultAlign;
  }

  /**
   * Any elements added to this list will be ignored when auto close is enabled.
   * 
   * @return the list of ignored elements
   */
  public List getIgnoreList() {
    if (ignoreElements == null) {
      ignoreElements = new ArrayList<Element>();
    }
    return ignoreElements;
  }

  /**
   * Returns the x offset.
   * 
   * @return the offset
   */
  public int getXOffset() {
    return xOffset;
  }

  /**
   * Returns the y offsets.
   * 
   * @return the offset
   */
  public int getYOffset() {
    return yOffset;
  }

  /**
   * Hides the popup.
   */
  public void hide() {
    if (!fireEvent(Events.BeforeHide, new ComponentEvent(this))) {
      return;
    }
    if (isEventPreview()) {
      DOM.removeEventPreview(this);
    }
    if (isAnimate()) {
      el.fadeOut(new Listener<FxEvent>() {
        public void handleEvent(FxEvent fe) {
          afterHide();
        }
      });
    } else {
      afterHide();
    }
  }

  /**
   * Returns true if animations are enabled.
   * 
   * @return the animation state
   */
  public boolean isAnimate() {
    return animate;
  }

  /**
   * Returns true if auto focus is enabled.
   * 
   * @return the auto focus state
   */
  public boolean isAutoFocus() {
    return autoFocus;
  }

  /**
   * Returns true if auto hide is enabled.
   * 
   * @return the auto hide state
   */
  public boolean isAutoHide() {
    return autoHide;
  }

  /**
   * Returns true if contrain to viewport is enabled.
   * 
   * @return the constrain viewport state
   */
  public boolean isConstrainViewport() {
    return constrainViewport;
  }

  /**
   * Returns true if event preview is enabled.
   * 
   * @return the event preview state
   */
  public boolean isEventPreview() {
    return eventPreview;
  }

  public boolean onEventPreview(Event event) {
    return handleEventPreview(new ComponentEvent(this, event));
  }

  /**
   * True to enable animations when showing and hiding (defaults to false).
   * 
   * @param animate true to enable animations
   */
  public void setAnimate(boolean animate) {
    this.animate = animate;
  }

  /**
   * True to move focus to the popup when being opened (defaults to true).
   * 
   * @param autoFocus true for auto focus
   */
  public void setAutoFocus(boolean autoFocus) {
    this.autoFocus = autoFocus;
  }

  /**
   * True to close the popup when the user clicks outside of the menu (default
   * to true).
   * 
   * @param autoHide true for auto hide
   */
  public void setAutoHide(boolean autoHide) {
    this.autoHide = autoHide;
  }

  /**
   * True to ensure popup is dislayed within the browser's viewport.
   * 
   * @param constrainViewport true to constrain
   */
  public void setConstrainViewport(boolean constrainViewport) {
    this.constrainViewport = constrainViewport;
  }

  /**
   * The default {@link El#alignTo} anchor position value for this menu relative
   * to its element of origin (defaults to "tl-bl?").
   * 
   * @param defaultAlign the default alignment
   */
  public void setDefaultAlign(String defaultAlign) {
    this.defaultAlign = defaultAlign;
  }

  /**
   * True to enable event preview (defaults to true).
   * 
   * @param eventPreview true to enable event preview
   */
  public void setEventPreview(boolean eventPreview) {
    this.eventPreview = eventPreview;
  }

  /**
   * Sets the popup's content.
   * 
   * @param item the content item
   */
  public void setItem(Component item) {
    removeAll();
    add(item);
  }

  /**
   * Sets the xOffset when constrainViewport == true (defaults to 10).
   * 
   * @param xOffset the x offset
   */
  public void setXOffset(int xOffset) {
    this.xOffset = xOffset;
  }

  /**
   * Sets the yOffset when constrainViewport == true (defaults to 15).
   * 
   * @param yOffset the offset
   */
  public void setYOffset(int yOffset) {
    this.yOffset = yOffset;
  }

  /**
   * Displays the popup.
   */
  public void show() {
    if (!fireEvent(Events.BeforeShow, new ComponentEvent(this))) return;
    Point p = new Point((int) Window.getClientWidth() / 2,
        (int) Window.getClientHeight() / 2);
    showAt(p.x, p.y);
  }

  /**
   * Displays the popup aligned to the bottom left of the widget. For exact
   * control of popup position see {@link #show(Element, String, int[])}.
   * 
   * @param widget the widget to use for alignment
   */
  public void show(Component widget) {
    if (!fireEvent(Events.BeforeShow, new ComponentEvent(this))) {
      return;
    }
    setItem(widget);
    alignElem = widget.getElement();
    onShowPopup();
  }

  /**
   * Displays the popup.
   * 
   * @param elem the element to align to
   * @param pos the position
   */
  public void show(Element elem, String pos) {
    if (!fireEvent(Events.BeforeShow, new ComponentEvent(this))) return;
    alignElem = elem;
    alignPos = pos;
    onShowPopup();
  }

  /**
   * Displays the popup.
   * 
   * @param elem the element to align to
   * @param pos the postion
   * @param offsets the offsets
   */
  public void show(Element elem, String pos, int[] offsets) {
    if (!fireEvent(Events.BeforeShow, new ComponentEvent(this))) {
      return;
    }
    alignElem = elem;
    alignPos = pos;
    alignOffsets = offsets;
    onShowPopup();
  }

  /**
   * Shows the popup at the specified location.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void showAt(int x, int y) {
    if (!fireEvent(Events.BeforeShow, new ComponentEvent(this))) {
      return;
    }
    alignPoint = new Point(x, y);
    onShowPopup();
  }

  protected void afterHide() {
    RootPanel.get().remove(this);
    hidden = true;
    if (layer != null) {
      layer.hideShadow();
    }
    fireEvent(Events.Hide, new ComponentEvent(this));
  }

  protected void afterShow() {
    if (layer != null) {
      layer.sync(true);
    }
    if (isAutoFocus()) {
      focus();
    }
    fireEvent(Events.Open, new ComponentEvent(this));
  }

  protected boolean handleEventPreview(ComponentEvent ce) {
    switch (ce.type) {
      case Event.ONMOUSEDOWN:
      case Event.ONMOUSEUP:
      case Event.ONMOUSEMOVE:
      case Event.ONCLICK:
      case Event.ONDBLCLICK: {
        if (DOM.getCaptureElement() == null) {
          if (!ce.within(getElement())) {
            if (isAutoHide() && (ce.type == Event.ONCLICK) || ce.isRightClick()) {
              if (ignoreElements != null) {
                for (int i = 0; i < ignoreElements.size(); i++) {
                  Element elem = (Element) ignoreElements.get(i);
                  if (DOM.isOrHasChild(elem, ce.getTarget())) {
                    return true;
                  }
                }
              }
              if (onAutoHide(ce.event)) {
                hide();
                return true;
              }
            }
            return false;
          }
        }
        break;
      }
      case Event.ONKEYUP:
        handleKeyUp(ce);
        break;
    }
    return true;
  }

  protected void handleKeyUp(ComponentEvent ce) {
    int code = ce.getKeyCode();
    ce.component = this;
    onKeyPress(ce);
    switch (code) {
      case KeyboardListener.KEY_ESCAPE:
        onAutoHide(ce.event);
    }
  }

  /**
   * Subclasses may override to cancel the hide from an auto hide.
   * 
   * @param event the current event
   * @return true to close, false to cancel
   */
  protected boolean onAutoHide(Event event) {
    return true;
  }

  protected void onKeyPress(BaseEvent be) {

  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setStyleAttribute("zIndex", "100");
    el.makePositionable(true);
  }

  protected Popup onShowPopup() {
    RootPanel.get().add(this);

    hidden = false;

    Point p = null;

    if (alignElem != null) {
      alignPos = alignPos != null ? alignPos : getDefaultAlign();
      alignOffsets = alignOffsets != null ? alignOffsets : new int[] {0, 2};
      p = el.getAlignToXY(alignElem, alignPos, alignOffsets);
    } else if (alignPoint != null) {
      p = alignPoint;
    }

    el.setLeftTop(p.x, p.y);

    alignElem = null;
    alignPos = null;
    alignOffsets = null;
    alignPoint = null;

    el.setStyleAttribute("zIndex", XDOM.getTopZIndex());
    el.makePositionable(true).setVisibility(false);

    if (isConstrainViewport()) {
      int clientHeight = Window.getClientHeight() + XDOM.getBodyScrollTop();
      int clientWidth = Window.getClientWidth() + XDOM.getBodyScrollLeft();

      Rectangle r = el.getBounds();

      int x = r.x;
      int y = r.y;

      if (y + r.height > clientHeight) {
        y = clientHeight - r.height - getYOffset();
        el.setTop(y);
      }
      if (x + r.width > clientWidth) {
        x = clientWidth - r.width - getXOffset();
        el.setLeft(x);
      }
    }

    el.setVisibility(true);

    if (isEventPreview()) {
      DOM.addEventPreview(this);
    }

    if (isAnimate()) {
      el.fadeIn(new Listener<FxEvent>() {
        public void handleEvent(FxEvent fe) {
          afterShow();
        }
      });
    } else {
      afterShow();
    }

    return this;
  }
}
