/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.google.gwt.user.client.Element;

/**
 * A group of Radio's.
 */
public class RadioGroup extends MultiField<Radio> {

  private static int autoId = 0;
  private String groupName;

  /**
   * Creates a new radio group.
   */
  public RadioGroup() {
    this.groupName = "gxt.RadioGroup." + (autoId++);
  }

  /**
   * Creates a new radio group.
   * 
   * @param name the group name
   */
  public RadioGroup(String name) {
    this.groupName = name;
  }

  /**
   * Returns the value of the selected radio.
   */
  @Override
  public Object getValue() {
    for (Radio r : fields) {
      if (r.getValue()) {
        return r.getValue();
      }
    }
    return null;
  }

  @Override
  public boolean isValid() {
    for (Radio radio : fields) {
      if (!radio.isValid()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void setValue(Object value) {
    for (Radio r : fields) {
      r.setValue(value.equals(r.getValue()));
    }
  }
  
  protected void onRadioSelected(Radio radio) {
    for (Radio r : fields) {
      if (r != radio && r.getValue()) {
        r.setValue(false);
      }
    }
  }

  protected void onRadioClick(Radio radio) {
    for (Radio r : fields) {
      if (r == radio) {
        r.setValue(true);
      } else if (r.getValue()){
        r.setValue(false);
      }
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    for (Radio r : fields) {
      r.group = this;
      r.setName(groupName);
    }
  }

}
