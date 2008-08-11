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
 * <code>PropertyEditory</code> that uses a {@link NumberFormat}.
 * 
 */
public class NumberPropertyEditor implements PropertyEditor<Number> {

  protected NumberFormat format = NumberFormat.getDecimalFormat();

  public NumberPropertyEditor() {

  }

  public NumberFormat getFormat() {
    return format;
  }

  public void setFormat(NumberFormat format) {
    this.format = format;
  }

  public NumberPropertyEditor(String pattern) {
    format = NumberFormat.getFormat(pattern);
  }

  public NumberPropertyEditor(NumberFormat format) {
    this.format = format;
  }

  public Double convertStringValue(String value) {
    return format.parse(value);
  }

  public String getStringValue(Number value) {
    return format.format(value.doubleValue());
  }

}
