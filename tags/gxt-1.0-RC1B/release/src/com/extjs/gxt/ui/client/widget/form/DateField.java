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
public class DateField extends TriggerField<Date> {

  /**
   * DateField error messages.
   */
  public class DateFieldMessages extends TextFieldMessages {

    private String minText = "The date in this field must be equal to or after {0}";
    private String maxText = "The date in this field must be equal to or before {0}";
    private String invalidText;

    /**
     * Returns the invalid text.
     * 
     * @return the invalid text
     */
    public String getInvalidText() {
      return invalidText;
    }

    /**
     * Returns the max error text.
     * 
     * @return the error text
     */
    public String getMaxText() {
      return maxText;
    }

    /**
     * Returns the min error text.
     * 
     * @return the error text
     */
    public String getMinText() {
      return minText;
    }

    /**
     * "The error text to display when the date in the field is invalid " +
     * "(defaults to '{value} is not a valid date - it must be in the format
     * {format}')."
     * 
     * @param invalidText the invalid text
     */
    public void setInvalidText(String invalidText) {
      this.invalidText = invalidText;
    }

    /**
     * Sets the error text to display when the date in the cell is after
     * maxValue (defaults to 'The date in this field must be before {{@link #setMaxValue}').
     * 
     * @param maxText the max error text
     */
    public void setMaxText(String maxText) {
      this.maxText = maxText;
    }

    /**
     * The error text to display when the date in the cell is before minValue
     * (defaults to 'The date in this field must be after {@link #setMinValue}').
     * 
     * @param minText the min text
     */
    public void setMinText(String minText) {
      this.minText = minText;
    }

  }

  private Date minValue;
  private Date maxValue;
  private DateMenu menu;

  /**
   * Creates a new date field.
   */
  public DateField() {
    autoValidate = false;
    propertyEditor = new DateTimePropertyEditor();
    messages = new DateFieldMessages();
    setTriggerStyle("x-form-date-trigger");
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
  public DateFieldMessages getMessages() {
    return (DateFieldMessages) messages;
  }

  /**
   * Returns the field's min value.
   * 
   * @return the min value
   */
  public Date getMinValue() {
    return minValue;
  }

  @Override
  public DateTimePropertyEditor getPropertyEditor() {
    return (DateTimePropertyEditor) propertyEditor;
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
  public void setRawValue(String value) {
    super.setRawValue(value);
  }

  @Override
  protected void onBlur(ComponentEvent ce) {
    String v = getRawValue();
    try {
      setValue(getPropertyEditor().convertStringValue(v));
    } catch (Exception e) {

    }
    super.onBlur(ce);
  }

  @Override
  protected void onTriggerClick(ComponentEvent ce) {
    super.onTriggerClick(ce);
    if (disabled || isReadOnly()) {
      return;
    }
    if (menu == null) {
      menu = new DateMenu();
      menu.addListener(Events.Select, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          focusValue = getValue();
          setValue(menu.getDate());
          fireChangeEvent(focusValue, getValue());
        }
      });
    }
    DatePicker picker = menu.getDatePicker();

    Object v = getValue();
    Date d = null;
    if (v instanceof Date) {
      d = (Date) v;
    } else {
      d = new Date();
    }
    picker.setValue(d, true);
    picker.setMinDate(minValue);
    picker.setMaxDate(maxValue);

    menu.show(wrap.dom, "tl-bl?");
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

    DateTimeFormat format = getPropertyEditor().getFormat();

    Date date = null;

    try {
      date = getPropertyEditor().convertStringValue(value);
    } catch (Exception e) {

    }

    if (date == null) {
      String error = null;
      if (getMessages().getInvalidText() != null) {
        error = Format.substitute(getMessages().getInvalidText(), 0);
      } else {
        error = GXT.MESSAGES.dateField_invalidText(value, format.getPattern().toUpperCase());
      }
      markInvalid(error);
      return false;
    }

    if (minValue != null && date.before(minValue)) {

      String error = null;
      if (getMessages().getMinText() != null) {
        error = Format.substitute(getMessages().getMinText(), format.format(minValue));
      } else {
        error = GXT.MESSAGES.dateField_minText(format.format(minValue));
      }
      markInvalid(error);
      return false;
    }
    if (maxValue != null && date.after(maxValue)) {
      String error = null;
      if (getMessages().getMaxText() != null) {
        error = Format.substitute(getMessages().getMaxText(), format.format(maxValue));
      } else {
        error = GXT.MESSAGES.dateField_minText(format.format(maxValue));
      }
      markInvalid(error);
      return false;
    }

    return true;
  }

}
