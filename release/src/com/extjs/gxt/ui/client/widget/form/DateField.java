/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import java.util.Date;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.menu.DateMenu;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * Provides a date input field with a {@link DatePicker} dropdown and automatic
 * date validation.
 */
public class DateField extends TriggerField {

  /**
   * The default date format which can be overriden for localization support.
   */
  public DateTimeFormat format = DateTimeFormat.getShortDateFormat();

  /**
   * The error text to display when the date in the cell is before minValue
   * (defaults to 'The date in this field must be after {@link #setMinValue}').
   */
  public String minText = "The date in this field must be equal to or after {0}";

  /**
   * The error text to display when the date in the cell is after maxValue
   * (defaults to 'The date in this field must be before {{@link #setMaxValue}').
   */
  public String maxText = "The date in this field must be equal to or before {0}";

  /**
   * "The error text to display when the date in the field is invalid " +
   * "(defaults to '{value} is not a valid date - it must be in the format
   * {format}')."
   */
  public String invalidText;

  private Date minValue;
  private Date maxValue;
  private DateMenu menu;

  /**
   * Creates a new date field.
   */
  public DateField() {
    autoValidate = false;
  }

  /**
   * Returns the current selected date.
   * 
   * @return the date
   */
  public Date getDate() {
    return (Date) getValue();
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
   * Returns the field's min value.
   * 
   * @return the min value
   */
  public Date getMinValue() {
    return minValue;
  }

  /**
   * Sets the date.
   * 
   * @param date the new date
   */
  public void setDate(Date date) {
    setValue(format.format(date));
  }

  /**
   * Sets the field's max value.
   * 
   * @param maxValue the max value
   */
  public void setMaxValue(Date maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * The maximum date allowed.
   * 
   * @param minValue the max value
   */
  public void setMinValue(Date minValue) {
    this.minValue = minValue;
  }

  @Override
  protected void onTriggerClick(ComponentEvent ce) {
    super.onTriggerClick(ce);
    if (menu == null) {
      menu = new DateMenu();
      menu.addListener(Events.Select, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          setDate(menu.getDate());
        }
      });
    }
    DatePicker picker = menu.getDatePicker();
    
    Object v = getValue();
    Date d = null;
    if (v instanceof Date) {
      d = (Date)v;
    } else {
      d = new Date();
    }
    picker.setValue(d);
    picker.setMinDate(minValue);
    picker.setMaxDate(maxValue);

    menu.show(wrap.dom, "tl-bl?");
  }

  protected Date parseDate(String date) {
    try {
      return format.parse(date);
    } catch (Exception e) {

    }
    return null;
  }

  @Override
  public void setValue(Object value) {
    Date d = null;
    if (value instanceof Date) {
      d = (Date) value;
    } else if (value instanceof String) {
      d = parseDate((String) value);
    }
    if (d != null) {
      super.setValue(format.format(d));
    } else {
      setValue(value);
    }

  }

  @Override
  protected void onBlur(ComponentEvent ce) {
    String v = getRawValue();
    try {
      Date d = format.parse(v);
      setValue(d);
    } catch (Exception e) {

    }
    super.onBlur(ce);
  }

  @Override
  public Object getValue() {
    if (value != null && value instanceof Date) {
      return value;
    }

    try {
      return format.parse(super.getValue().toString());
    } catch (Exception e) {

    }
    return "";
  }

  @Override
  protected boolean validateValue(String value) {
    if (!super.validateValue(value)) {
      return false;
    }
    if (value.length() < 1) { // if it's blank and textfield didn't flag it then
      // it's valid
      return true;
    }
    Date date = parseDate(value);
    if (date == null) {
      String error = null;
      if (invalidText != null) {
        error = Format.substitute(invalidText, 0);
      } else {
        error = GXT.MESSAGES.dateField_invalidText(value,
            format.getPattern().toUpperCase());
      }
      markInvalid(error);
      return false;
    }

    if (minValue != null && date.before(minValue)) {
      String error = null;
      if (minText != null) {
        error = Format.substitute(minText, format.format(minValue));
      } else {
        error = GXT.MESSAGES.dateField_minText(format.format(minValue));
      }
      markInvalid(error);
      return false;
    }
    if (maxValue != null && date.after(maxValue)) {
      String error = null;
      if (maxText != null) {
        error = Format.substitute(maxText, format.format(maxValue));
      } else {
        error = GXT.MESSAGES.dateField_minText(format.format(maxValue));
      }
      markInvalid(error);
      return false;
    }

    return true;
  }

}
