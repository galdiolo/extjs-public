/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows any widget to be used in a <code>Formlayout</code>.
 * 
 * <p/> By default, the wrapped widget will not be resized when the field is
 * resized. This can be changed by calling {@link #setResizeWidget(boolean)}.
 */
public class AdapterField extends Field {

  /**
   * The wrapped widget.
   */
  protected Widget widget;

  private boolean resizeWidget;

  /**
   * Creates a new adapter field.
   * 
   * @param widget the widget to be wrapped
   */
  public AdapterField(Widget widget) {
    this.widget = widget;
  }

  /**
   * Returns the wrapped widget.
   * 
   * @return the widget
   */
  public Widget getWidget() {
    return widget;
  }

  /**
   * Returns true if the wrapped widget is being resized.
   * 
   * @return true is resizing is enabled
   */
  public boolean isResizeWidget() {
    return resizeWidget;
  }

  @Override
  public boolean isValid() {
    return validateValue(null);
  }

  /**
   * True to resize the wrapped widget when the field is resized (defaults to
   * false).
   * 
   * @param resizeWidget true to resize the wrapped widget
   */
  public void setResizeWidget(boolean resizeWidget) {
    this.resizeWidget = resizeWidget;
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(widget);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(widget);
  }

  @Override
  protected void onRender(Element target, int index) {
    if (widget instanceof Component) {
      Component c = (Component) widget;
      if (!c.isRendered()) {
        c.render(target, index);
      }
      if (c instanceof LayoutContainer) {
        ((LayoutContainer)c).layout();
      }
    }
    setElement(widget.getElement(), target, index);
  }

  @Override
  protected void onResize(int width, int height) {
    super.onResize(width, height);
    if (resizeWidget) {
      if (width != -1) {
        widget.setWidth(width + "px");
      }
      if (height != -1) {
        widget.setHeight(height + "px");
      }
    }
  }

  @Override
  protected boolean validateValue(String value) {
    return true;
  }

}
