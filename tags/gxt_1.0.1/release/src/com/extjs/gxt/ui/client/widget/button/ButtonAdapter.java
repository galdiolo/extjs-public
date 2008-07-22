/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.button;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.Component;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows any widget to be placed in a button bar.
 */
public class ButtonAdapter extends Button {

  /**
   * The wrapped widget.
   */
  protected Widget widget;

  /**
   * Creates a new adapter.
   * 
   * @param widget the widget to be adapted
   */
  public ButtonAdapter(Widget widget) {
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

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    WidgetHelper.doAttach(widget);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    WidgetHelper.doDetach(widget);
  }
  
  @Override
  protected El getFocusEl() {
    return el();
  }

  @Override
  protected void onDisable() {

  }
  
  @Override
  protected void onEnable() {

  }
  
  @Override
  protected void onRender(Element target, int index) {
    if (widget instanceof Component) {
      ((Component) widget).render(target, index);
    }
    setElement(widget.getElement(), target, index);
  }

}
