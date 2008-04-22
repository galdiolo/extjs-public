/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.event.ComponentEvent;

/**
 * Single radio field. Same as Checkbox, but provided as a convenience for
 * automatically setting the input type. Radio grouping is handled automatically
 * by the browser if you give each radio in a group the same name.
 */
public class Radio extends CheckBox {

  /**
   * The text that appears beside the checkbox (defaults to null).
   */
  public String boxLabel;
  
  protected RadioGroup group;

  /**
   * If this radio is part of a group, it will return the selected value.
   * 
   * @return the value
   */
  public String getGroupValue() {
    return null;
  }

  @Override
  protected void onClick(ComponentEvent be) {
    if (group != null) {
      group.onRadioClick(this);
    } else {
      boolean val = input.getBooleanElementAttribute("checked");
      if (val != checked) {
        setValue(String.valueOf(val));
      }
    }
  }

}
