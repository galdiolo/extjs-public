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
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Change</b> : FieldEvent(field)<br>
 * <div>Fires when the radio's value changes.</div>
 * <ul>
 * <li>field : this</li>
 * <li>oldValue : the old value</li>
 * <li>value : the new value</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class Radio extends CheckBox {

  protected RadioGroup group;

  /**
   * Returns the radios container group.
   * 
   * @return the group
   */
  public RadioGroup getGroup() {
    return group;
  }

  @Override
  public void setValue(Boolean value) {
    boolean old = getValue();
    if (value == null) {
      value = new Boolean(false);
    }
    super.setValue(value);
    // fire change event when value is changed, not on blur
    fireChangeEvent(old, value);
    if (value && group != null) {
      group.onRadioSelected(this);
    }
  }

  @Override
  protected void onClick(ComponentEvent be) {
    if (readOnly) {
      be.stopEvent();
      return;
    }
    if (group != null) {
      group.onRadioClick(this);
    } else {
      focusValue = !getValue();
      setValue(true);
    }
  }

}
