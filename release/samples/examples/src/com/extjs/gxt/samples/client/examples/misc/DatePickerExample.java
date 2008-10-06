/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.misc;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DatePickerExample extends LayoutContainer {

  public DatePickerExample() {
    setLayout(new FlowLayout(10));
    final DatePicker picker = new DatePicker();
    picker.addListener(Events.Select,  new Listener<ComponentEvent>() {
    
      public void handleEvent(ComponentEvent be) {
        String d = DateTimeFormat.getShortDateFormat().format(picker.getValue());
        Info.display("Date Selected", "You selected {0}.", new Params(d));
      }
    
    });
    add(picker);
  }
  
}
