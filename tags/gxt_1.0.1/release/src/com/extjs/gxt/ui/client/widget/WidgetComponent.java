/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Creates a component from a widget. This allows widget instances to be treated
 * as components.
 */
public class WidgetComponent extends BoxComponent {

  /**
   * The wrapped widget.
   */
  protected Widget widget;

  /**
   * Creates a new component wrapper.
   * 
   * @param widget the widget to be wrapped
   */
  public WidgetComponent(Widget widget) {
    assert widget != null : "widget must not be null";
    this.widget = widget;
  }

  /**
   * Returns the underlying widget.
   * 
   * @return the widget
   */
  public Widget getWidget() {
    return widget;
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(widget.getElement(), target, index);
  }

  @Override
  public void onAttach() {
    super.onAttach();
    WidgetHelper.doAttach(widget);
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    WidgetHelper.doDetach(widget);
  }
}
