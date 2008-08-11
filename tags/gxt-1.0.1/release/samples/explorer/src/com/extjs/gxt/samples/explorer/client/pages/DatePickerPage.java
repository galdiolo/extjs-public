/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class DatePickerPage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
  
  public DatePickerPage() {
    setHeight("100%");

    FlowLayout layout = new FlowLayout();
    layout.setRemovePositioning(false);
    setLayout(layout);

    DatePicker picker = new DatePicker();
    picker.setPosition(10, 10);
    add(picker);
  }

}
