/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Provides a time input field with a time dropdown and automatic time
 * validation.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Expand</b> : FieldEvent(field)<br>
 * <div>Fires when the dropdown list is expanded.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : FieldEvent(field)<br>
 * <div>Fires when the dropdown list is collapsed.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeSelect</b> : FieldEvent(field)<br>
 * <div>Fires before a list item is selected.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Select</b> : FieldEvent(field)<br>
 * <div>Fires when a list item is selected.</div>
 * <ul>
 * <li>field : this</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class TimeField extends ComboBox {

  /**
   * The error text to display when the date in the cell is before minValue
   * (defaults to 'The time in this field must be equal to or after {0}').
   */
  public String minText;

  /**
   * The error text to display when the time is after maxValue (defaults to 'The
   * time in this field must be equal to or before {0}').
   */
  public String maxText;

  /**
   * The error text to display when the time in the field is invalid (defaults
   * to '{value} is not a valid time - it must be in the format {format}').
   */
  public String invalidText;

  /**
   * The date time format used to format each entry (defaults to
   * {@link DateTimeFormat#getShortDateFormat()}.
   */
  public DateTimeFormat format = DateTimeFormat.getShortTimeFormat();

  /**
   * The number of minutes between each time value in the list (defaults to 15).
   */
  public int increment = 15;

  private Date minValue;
  private Date maxValue;

  public TimeField() {

  }

  /**
   * Returns the field's max value.
   * 
   * @return the max value
   */
  public Date getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the fields minimum value.
   * 
   * @return the min value
   */
  public Date getMinValue() {
    return minValue;
  }

  /**
   * Sets the field's max value.
   * 
   * @param value the max value
   */
  public void setMaxValue(Date value) {
    this.maxValue = value;
  }

  /**
   * The minimum allowed time (no default value).
   * 
   * @param value the min date
   */
  public void setMinValue(Date value) {
    this.minValue = value;
  }

  @Override
  protected void initList() {
    DateWrapper min = minValue != null ? new DateWrapper(minValue) : new DateWrapper();
    min = min.clearTime();

    DateWrapper max = maxValue != null ? new DateWrapper(maxValue) : new DateWrapper();
    if (maxValue == null) {
      max = max.clearTime().addMinutes((24 * 60) - 1);
    }

    List<Record> times = new ArrayList<Record>();
    while (min.before(max)) {
      Record r = new Record();
      r.set("text", format.format(min.asDate()));
      times.add(r);
      min = min.addMinutes(increment);
    }

    Store store = new Store();
    store.add(times);

    setStore(store);
    displayField = "text";
    super.initList();
  }

}
