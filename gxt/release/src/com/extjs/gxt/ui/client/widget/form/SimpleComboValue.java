/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * A <code>ModelData</code> instance used in a <code>SimpleComboBox</code>.
 *
 * @param <T> the data type
 */
public class SimpleComboValue<T> extends BaseModelData {
  
  protected SimpleComboValue(T value) {
    setValue(value);
  }

  /**
   * Returns the value.
   * 
   * @return the value
   */
  public T getValue() {
    return (T)get("value");
  }

  /**
   * Sets the value.
   * 
   * @param value the value
   */
  public void setValue(T value) {
    set("value", value);
  }
}
