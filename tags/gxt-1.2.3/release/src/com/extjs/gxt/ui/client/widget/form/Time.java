/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.util.DateWrapper;

/**
 * ModelData instance used with TimeField.
 * 
 * @see TimeField
 */
public class Time extends BaseModelData {

  /**
   * Creates a new time instance.
   */
  public Time() {

  }

  /**
   * Creates a new time instance.
   * 
   * @param date any date, hours and mintues will be used
   */
  public Time(Date date) {
    setHour(new DateWrapper(date).getHours());
    setMinutes(new DateWrapper(date).getMinutes());
  }

  /**
   * Creates a new time instance.
   * 
   * @param hour the (0-23)
   * @param minutes the minutes (0-59)
   */
  public Time(int hour, int minutes) {
    setHour(hour);
    setMinutes(minutes);
  }

  /**
   * Returns a new date instance form the time information.
   * 
   * @return the new date instance
   */
  public Date getDate() {
    DateWrapper w = new DateWrapper();
    w = w.clearTime();
    w = w.addHours(getHour());
    w = w.addMinutes(getMinutes());
    return w.asDate();
  }

  /**
   * Returns the hour.
   * 
   * @return the hour
   */
  public int getHour() {
    return (Integer) get("hour");
  }

  /**
   * Returns the mintues.
   * 
   * @return the mintues
   */
  public int getMinutes() {
    return (Integer) get("minutes");
  }

  /**
   * Sets the hour.
   * 
   * @param hour the hour
   */
  public void setHour(int hour) {
    set("hour", hour);
  }

  /**
   * Sets the minutes.
   * 
   * @param minutes the minutes
   */
  public void setMinutes(int minutes) {
    set("minutes", minutes);
  }

}
