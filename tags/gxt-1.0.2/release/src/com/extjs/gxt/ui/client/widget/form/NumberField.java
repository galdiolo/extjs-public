/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.util.Format;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * Numeric text field that provides automatic keystroke filtering and numeric
 * validation.
 */
public class NumberField extends TextField<Number> {

  /**
   * NumberField messages.
   */
  public class NumberFieldMessages extends TextFieldMessages {
    private String minText;
    private String maxText;
    private String nanText;

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
     * @return the min eror text
     */
    public String getMinText() {
      return minText;
    }

    /**
     * Returns the not a number error text.
     * 
     * @return the not a number error text
     */
    public String getNanText() {
      return nanText;
    }

    /**
     * Error text to display if the maximum value validation fails (defaults to
     * "The maximum value for this field is {maxValue}").
     * 
     * @param maxText the max error text
     */
    public void setMaxText(String maxText) {
      this.maxText = maxText;
    }

    /**
     * Sets the Error text to display if the minimum value validation fails
     * (defaults to "The minimum value for this field is {minValue}").
     * 
     * @param minText min error text
     */
    public void setMinText(String minText) {
      this.minText = minText;
    }

    /**
     * Sets the error text to display if the value is not a valid number. For
     * example, this can happen if a valid character like '.' or '-' is left in
     * the field with no number (defaults to "{value} is not a valid number").
     * 
     * @param nanText the not a number text
     */
    public void setNanText(String nanText) {
      this.nanText = nanText;
    }
  }

  private String baseChars = "0123456789`abcdefghi";
  private String decimalSeparator = ".";
  private boolean allowNegative = true;
  private boolean allowDecimals = true;
  private List<Character> allowed;
  private double minValue = Double.NEGATIVE_INFINITY;
  private double maxValue = Double.MAX_VALUE;

  /**
   * Creates a new number field.
   */
  public NumberField() {
    messages = new NumberFieldMessages();
    propertyEditor = new NumberPropertyEditor();
  }

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
   * Returns the base characters.
   * 
   * @return the base characters
   */
  public String getBaseChars() {
    return baseChars;
  }

  /**
   * Returns the field's decimal seperator.
   * 
   * @return the seperator
   */
  public String getDecimalSeparator() {
    return decimalSeparator;
  }

  /**
   * Returns the field's number format.
   * 
   * @return the number format
   */
  public NumberFormat getFormat() {
    return getPropertyEditor().getFormat();
  }

  /**
   * Returns the fields max value.
   * 
   * @return the max value
   */
  public double getMaxValue() {
    return maxValue;
  }

  @Override
  public NumberFieldMessages getMessages() {
    return (NumberFieldMessages) messages;
  }

  /**
   * Returns the field's minimum value.
   * 
   * @return the min value
   */
  public double getMinValue() {
    return minValue;
  }

  @Override
  public NumberPropertyEditor getPropertyEditor() {
    return (NumberPropertyEditor) propertyEditor;
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
   * Sets the base set of characters to evaluate as valid numbers (defaults to
   * '0123456789').
   * 
   * @param baseChars the base character
   */
  public void setBaseChars(String baseChars) {
    assertPreRender();
    this.baseChars = baseChars;
  }

  /**
   * Sets the character(s) to allow as the decimal separator (defaults to '.').
   * 
   * @param decimalSeparator
   */
  public void setDecimalSeparator(String decimalSeparator) {
    this.decimalSeparator = decimalSeparator;
  }

  /**
   * Sets the cell's number formatter.
   * 
   * @param format the format
   */
  public void setFormat(NumberFormat format) {
    getPropertyEditor().setFormat(format);
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
  protected void onKeyDown(FieldEvent fe) {
    super.onKeyDown(fe);
    char key = (char) fe.getKeyCode();
    
    if (fe.getKeyCode() == 190) {
      key = '.';
    }
    
    if (fe.isSpecialKey() || key == KeyboardListener.KEY_BACKSPACE || key == KeyboardListener.KEY_DELETE) {
      return;
    }

    if (!allowed.contains(key)) {
      fe.stopEvent();
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    allowed = new ArrayList<Character>();
    for (int i = 0; i < baseChars.length(); i++) {
      allowed.add(baseChars.charAt(i));
    }
    if (allowNegative) {
      allowed.add('-');
    }
    if (allowDecimals) {
      for (int i = 0; i < decimalSeparator.length(); i++) {
        allowed.add(decimalSeparator.charAt(i));
      }
    }
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

    String v = value.replace(decimalSeparator, ".");

    Double d = null;
    try {
      d = getPropertyEditor().convertStringValue(value);
    } catch (Exception e) {
      String error = "";
      if (getMessages().getNanText() == null) {
        error = GXT.MESSAGES.numberField_nanText(v);
      } else {
        error = Format.substitute(getMessages().getNanText(), v);
      }
      markInvalid(error);
      return false;
    }
    if (d < minValue) {
      String error = "";
      if (getMessages().getMinText() == null) {
        error = GXT.MESSAGES.numberField_minText(minValue);
      } else {
        error = Format.substitute(getMessages().getMinText(), minValue);
      }
      markInvalid(error);
      return false;
    }

    if (d > maxValue) {
      String error = "";
      if (getMessages().getMaxText() == null) {
        error = GXT.MESSAGES.numberField_maxText(maxValue);
      } else {
        error = Format.substitute(getMessages().getMaxText(), maxValue);
      }
      markInvalid(error);
      return false;
    }
    return true;
  }
}
