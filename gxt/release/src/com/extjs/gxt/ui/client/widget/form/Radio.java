/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.google.gwt.user.client.Element;

/**
 * Single radio field. Same as Checkbox, but provided as a convenience for
 * automatically setting the input type. Radio grouping is handled automatically
 * by the browser if you give each radio in a group the same name.
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
  public void setChecked(boolean checked) {
    this.checked = checked;
    if (rendered) {
      input.setElementAttribute("checked", checked);
    }
  }

  @Override
  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  protected void onClick(ComponentEvent be) {
    if (group != null) {
      group.onRadioClick(this);
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    if (checked) {
      setChecked(true);
    }
  }

}
