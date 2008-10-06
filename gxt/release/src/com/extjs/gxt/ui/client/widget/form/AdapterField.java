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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Allows any widget to be used in a <code>Formlayout</code>.
 */
public class AdapterField extends Field {

  /**
   * The wrapped widget.
   */
  protected Widget widget;

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
    }
    setElement(widget.getElement(), target, index);
  }

  @Override
  protected boolean validateValue(String value) {
    return true;
  }

}
