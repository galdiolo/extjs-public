/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.Date;

import com.extjs.gxt.ui.client.widget.DatePicker;

/**
 * DatePicker event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see DatePicker
 */
public class DatePickerEvent extends ComponentEvent {

  public DatePicker datePicker;

  public Date date;

  public DatePickerEvent(DatePicker datePicker) {
    super(datePicker);
    this.datePicker = datePicker;
  }

}
