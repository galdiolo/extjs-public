/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.util.Format;
import com.google.gwt.i18n.client.NumberFormat;

/**
 * Numeric text field that provides automatic keystroke filtering and numeric
 * validation.
 */
public class NumberField extends TextField {

  /**
   * Character(s) to allow as the decimal separator (defaults to '.').
   */
  public char decimalSeperator = '.';

  /**
   * The maximum precision to display after the decimal separator (defaults to
   * 2).
   */
  public int decimalPrecision = 2;

  /**
   * Error text to display if the minimum value validation fails (defaults to
   * "The minimum value for this field is {minValue}").
   */
  public String minText;

  /**
   * Error text to display if the maximum value validation fails (defaults to
   * "The maximum value for this field is {maxValue}").
   */
  public String maxText;

  /**
   * Error text to display if the value is not a valid number. For example, this
   * can happen if a valid character like '.' or '-' is left in the field with
   * no number (defaults to "{value} is not a valid number").
   */
  public String nanText;

  private NumberFormat format = NumberFormat.getDecimalFormat();
  private boolean allowNegative = true;
  private boolean allowDecimals = true;
  private double minValue = Double.MIN_VALUE;
  private double maxValue = Double.MAX_VALUE;

  /**
   * Returns true of decimal values are allowed.
   * 
   * @return the allow decimal state
   */
  public boolean getAllowDecimals() {
    return allowDecimals;
  }

  /**
   * Returns true if negative values are allowed.
   * 
   * @return the allow negative value state
   */
  public boolean getAllowNegative() {
    return allowNegative;
  }

  /**
   * Returns the field's number format.
   * 
   * @return the number format
   */
  public NumberFormat getFormat() {
    return format;
  }

  /**
   * Returns the fields max value.
   * 
   * @return the max value
   */
  public double getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the field's minimum value.
   * 
   * @return the min value
   */
  public double getMinValue() {
    return minValue;
  }

  /**
   * Sets whether decimal value are allowed (defaults to true).
   * 
   * @param allowDecimals true to allow negative values
   */
  public void setAllowDecimals(boolean allowDecimals) {
    this.allowDecimals = allowDecimals;
  }

  /**
   * Sets whether negative value are allowed.
   * 
   * @param allowNegative true to allow negative values
   */
  public void setAllowNegative(boolean allowNegative) {
    this.allowNegative = allowNegative;
  }

  /**
   * Sets the cell's number formatter.
   * 
   * @param format the format
   */
  public void setFormat(NumberFormat format) {
    this.format = format;
  }

  /**
   * Sets the field's max allowable value.
   * 
   * @param maxValue the max value
   */
  public void setMaxValue(double maxValue) {
    this.maxValue = maxValue;
  }

  /**
   * Sets the field's minimum allowed value.
   * 
   * @param minValue the min value
   */
  public void setMinValue(double minValue) {
    this.minValue = minValue;
  }

  @Override
  public void setValue(Object value) {
    if (value instanceof Double) {
      super.setValue(format.format(((Double) value)));
    } else {
      super.setValue(value);
    }
    this.value = value;
  }

  @Override
  protected boolean validateValue(String value) {
    if (!super.validateValue(value)) {
      return false;
    }
    if (value.length() < 1) { // if it's blank and textfield didn't flag it then
      // its valid it's valid
      return true;
    }

    String v = value.replace(decimalSeperator, '.');

    Double d = null;
    try {
      d = Double.parseDouble(v);
    } catch (Exception e) {
      String error = "";
      if (nanText == null) {
        nanText = GXT.MESSAGES.numberField_nanText(v);
      } else {
        nanText = Format.substitute(nanText, v);
      }
      markInvalid(error);
      return false;
    }
    if (d < minValue) {
      String error = "";
      if (minText == null) {
        error = GXT.MESSAGES.numberField_minText(d);
      } else {
        error = Format.substitute(minText, d);
      }
      markInvalid(error);
      return false;
    }

    if (d > maxValue) {
      String error = "";
      if (maxText == null) {
        error = GXT.MESSAGES.numberField_maxText(d);
      } else {
        error = Format.substitute(maxText, d);
      }
      markInvalid(error);
      return false;
    }
    return true;
  }

}
