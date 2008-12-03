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

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
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
public class TimeField extends ComboBox<ModelData> {

  /**
   * TimeField error messages.
   */
  public class TimeFieldMessages extends ComboBoxMessages {

    private String minText;
    private String maxText;

    /**
     * Returns the max text.
     * 
     * @return the max text
     */
    public String getMaxText() {
      return maxText;
    }

    /**
     * Returns the min text.
     * 
     * @return the min text
     */
    public String getMinText() {
      return minText;
    }

    /**
     * Sets the error text to display when the time in the field is invalid
     * (defaults to '{value} is not a valid time - it must be in the format
     * {format}').
     * 
     * @param invalidText the invalid text
     */
    public void setInvalidText(String invalidText) {
      super.setInvalidText(invalidText);
    }

    /**
     * Sets the error text to display when the time is after maxValue (defaults
     * to 'The time in this field must be equal to or before {0}').
     * 
     * @param maxText the max text
     */
    public void setMaxText(String maxText) {
      this.maxText = maxText;
    }

    /**
     * Sets the error text to display when the date in the cell is before
     * minValue (defaults to 'The time in this field must be equal to or after
     * {0}').
     * 
     * @param minText the min text
     */
    public void setMinText(String minText) {
      this.minText = minText;
    }

  }

  /**
   * The number of minutes between each time value in the list (defaults to 15).
   */
  private int increment = 15;

  /**
   * The date time format used to format each entry (defaults to
   * {@link DateTimeFormat#getShortDateFormat()}.
   */
  private DateTimeFormat format = DateTimeFormat.getShortTimeFormat();

  private Date minValue;
  private Date maxValue;

  public TimeField() {
    setMessages(new TimeFieldMessages());
  }

  /**
   * Returns the current date value.
   * 
   * @return the value
   */
  public Date getDateValue() {
    ModelData value = getValue();
    if (value != null) {
      return format.parse((String) value.get("text"));
    }
    return null;
  }

  /**
   * Returns the date time format.
   * 
   * @return the date time format
   */
  public DateTimeFormat getFormat() {
    return format;
  }

  /**
   * Returns the number of minutes between each time value.
   * 
   * @return the increment
   */
  public int getIncrement() {
    return increment;
  }

  /**
   * Returns the field's max value.
   * 
   * @return the max value
   */
  public Date getMaxValue() {
    return maxValue;
  }

  @Override
  public TimeFieldMessages getMessages() {
    return (TimeFieldMessages) messages;
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
   * Sets the date time format used to format each entry (defaults to
   * {@link DateTimeFormat#getShortDateFormat()}.
   * 
   * @param format the date time format
   */
  public void setFormat(DateTimeFormat format) {
    this.format = format;
  }

  /**
   * Sets the number of minutes between each time value in the list (defaults to
   * 15).
   * 
   * @param increment the increment
   */
  public void setIncrement(int increment) {
    this.increment = increment;
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
    if (minValue == null) {
      min = min.clearTime();
    }

    DateWrapper max = maxValue != null ? new DateWrapper(maxValue) : new DateWrapper();
    if (maxValue == null) {
      max = max.clearTime().addMinutes((24 * 60) - 1);
    }

    List times = new ArrayList();
    while (min.before(max)) {
      BaseModelData r = new BaseModelData();
      r.set("text", getFormat().format(min.asDate()));
      times.add(r);
      min = min.addMinutes(increment);
    }

    ListStore store = new ListStore();
    store.add(times);

    setStore(store);
    setDisplayField("text");
    super.initList();
  }

}
