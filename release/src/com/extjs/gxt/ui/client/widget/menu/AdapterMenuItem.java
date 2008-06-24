/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows any widget to be placed in a menu.
 */
public class AdapterMenuItem extends Item {

  /**
   * The wrapped widget.
   */
  protected Widget widget;

  /**
   * Creates a new adapter.
   * 
   * @param widget the widget to be adapted
   */
  public AdapterMenuItem(Widget widget) {
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

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    if (widget instanceof Component) {
      ((Component) widget).render(target, index);
    }
    setElement(widget.getElement(), target, index);
  }

  protected void doAttachChildren() {
    super.doAttachChildren();
    WidgetHelper.doAttach(widget);
  }

  protected void doDetachChildren() {
    super.doDetachChildren();
    WidgetHelper.doDetach(widget);
  }

}
