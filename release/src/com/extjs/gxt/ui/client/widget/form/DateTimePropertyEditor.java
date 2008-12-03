/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class DateTimePropertyEditor implements PropertyEditor<Date> {

  protected DateTimeFormat format = DateTimeFormat.getShortDateFormat();

  public DateTimePropertyEditor() {

  }

  public DateTimePropertyEditor(DateTimeFormat format) {
    this.format = format;
  }

  public DateTimePropertyEditor(String pattern) {
    this.format = DateTimeFormat.getFormat(pattern);
  }

  public DateTimeFormat getFormat() {
    return format;
  }

  public void setFormat(DateTimeFormat format) {
    this.format = format;
  }

  public Date convertStringValue(String value) {
    return format.parse(value);
  }

  public String getStringValue(Date value) {
    return format.format(value);
  }

}
