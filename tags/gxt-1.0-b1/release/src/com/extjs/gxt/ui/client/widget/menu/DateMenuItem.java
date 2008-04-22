/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.DatePicker;
import com.google.gwt.user.client.Element;

/**
 * A MenuItem that displays a DatePicker.
 */
public class DateMenuItem extends MenuItem {

  protected DatePicker picker;

  /**
   * Creates a new menu item.
   */
  public DateMenuItem() {
    hideOnClick = true;
    picker = new DatePicker();
    picker.addListener(Events.Select, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        parentMenu.fireEvent(Events.Select, ce);
        parentMenu.hide();
      }
    });
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    picker.render(target, index);
    setElement(picker.getElement());
  }

  @Override
  protected void handleClick(ComponentEvent be) {
    picker.onComponentEvent((ComponentEvent) be);
  }

}
