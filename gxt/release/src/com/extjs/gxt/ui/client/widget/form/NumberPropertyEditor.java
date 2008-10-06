/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * <code>PropertyEditory</code> that uses a {@link NumberFormat}. When
 * converting a Number to a String, if a format has not been specified, toString
 * will be called on the passed Number. When converting from a String to a
 * Number, the number type ({@link #setType(Class)} will be used to determine
 * the new number instance type.
 * 
 * <pre><code>
 * NumberPropertyEditor editor = new NumberPropertyEditor(Integer.class);
 * </code></pre>
 */
public class NumberPropertyEditor implements PropertyEditor<Number> {

  protected NumberFormat format;
  protected Class type;

  /**
   * Creates a new number property editor with the default number type (Double).
   */
  public NumberPropertyEditor() {

  }

  /**
   * Creates a new number property editor.
   * 
   * @param type the number class (Short, Integer, Long, Float, Double)
   */
  public NumberPropertyEditor(Class type) {
    this.type = type;
  }

  /**
   * Returns the number class.
   * 
   * @return the number class
   */
  public Class getType() {
    return type;
  }

  /**
   * Sets the number type used when converting a string to a number.
   * 
   * @param type the type (Short, Integer, Long, Float, Double)
   */
  public void setType(Class type) {
    this.type = type;
  }

  /**
   * Creates a new number property editor.
   * 
   * @param format the number format
   */
  public NumberPropertyEditor(NumberFormat format) {
    this.format = format;
  }

  /**
   * Creates a new number property editor.
   * 
   * @param pattern the number format pattern
   */
  public NumberPropertyEditor(String pattern) {
    format = NumberFormat.getFormat(pattern);
  }

  public Number convertStringValue(String value) {
    if (format != null) {
      Double d = format.parse(value);
      if (type == Short.class) {
        return d.shortValue();
      } else if (type == Integer.class) {
        return d.intValue();
      } else if (type == Long.class) {
        return d.longValue();
      } else if (type == Float.class) {
        return d.floatValue();
      }
      return d;
    }
    if (type == Short.class) {
      return Short.valueOf(value);
    } else if (type == Integer.class) {
      return Integer.valueOf(value);
    } else if (type == Long.class) {
      return Long.valueOf(value);
    } else if (type == Float.class) {
      return Float.valueOf(value);
    }
    return new Double(value);
  }

  /**
   * Returns the editor's format.
   * 
   * @return the number format
   */
  public NumberFormat getFormat() {
    return format;
  }

  public String getStringValue(Number value) {
    if (format != null) {
      return format.format(value.doubleValue());
    }
    return value.toString();
  }

  /**
   * Sets the editor's format.
   * 
   * @param format the format
   */
  public void setFormat(NumberFormat format) {
    this.format = format;
  }

}