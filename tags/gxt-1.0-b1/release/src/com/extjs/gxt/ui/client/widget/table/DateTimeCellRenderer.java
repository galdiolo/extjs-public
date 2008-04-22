/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * A <code>CellRenderer</code> implementation for date and times.
 * 
 * @see DateTimeFormat
 */
public class DateTimeCellRenderer implements CellRenderer {

  private DateTimeFormat format;

  /**
   * Creates a date time cell renderer.
   * 
   * @param pattern the date time format
   */
  public DateTimeCellRenderer(String pattern) {
    this.format = DateTimeFormat.getFormat(pattern);
  }

  /**
   * Creates a new date time cell renderer.
   * 
   * @param format the date time format
   */
  public DateTimeCellRenderer(DateTimeFormat format) {
    this.format = format;
  }

  public String render(String property, Object value) {
    if (value == null) {
      return "";
    }
    return format.format((Date) value);
  }
}
