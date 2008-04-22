/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.menu;

import java.util.Date;

import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.DatePicker;

/**
 * A Menu for choosing a date.
 */
public class DateMenu extends Menu {

  /**
   * The internal date picker.
   */
  protected DatePicker picker;

  private DateMenuItem item;

  public DateMenu() {
    item = new DateMenuItem();
    picker = item.picker;
    add(item);
    baseStyle = "x-date-menu";
    autoHeight = true;
  }

  /**
   * Returns the selected date.
   * 
   * @return the date
   */
  public Date getDate() {
    return item.picker.getValue();
  }

  /**
   * Returns the date picker.
   * 
   * @return the date picker
   */
  public DatePicker getDatePicker() {
    return picker;
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    picker.onAttach();
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    WidgetHelper.doDetach(picker);
  }

}
