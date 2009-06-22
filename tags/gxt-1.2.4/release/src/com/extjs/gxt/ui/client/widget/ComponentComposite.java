/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * A GWT <code>Composite</code> subclass that handles GXT lazy rendering. This
 * class should be used anytime a Composite is wrapping a Component instance.
 * 
 * @see WidgetComponent
 */
public class ComponentComposite extends Composite {

  protected Component component;

  @Override
  protected void initWidget(final Widget widget) {
    if (widget instanceof Component) {
      component = (Component) widget;
      if (!component.isRendered()) {
        component.render(DOM.createDiv());
      }
    }
    super.initWidget(widget);
  }

}
