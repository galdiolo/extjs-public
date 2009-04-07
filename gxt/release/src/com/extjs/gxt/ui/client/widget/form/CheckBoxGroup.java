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

/**
 * A group of CheckBox's.
 */
public class CheckBoxGroup extends MultiField<CheckBox> {

  /**
   * Creates a new checkbox group.
   */
  public CheckBoxGroup() {
    setSpacing(5);
  }

  /**
   * Returns the first checked check box.
   */
  @Override
  public CheckBox getValue() {
    for (CheckBox check : fields) {
      if (check.getValue()) {
        return check;
      }
    }
    return null;
  }

  /**
   * Returns a list of all selected check boxes.
   * 
   * @return the list
   */
  public List<CheckBox> getValues() {
    List<CheckBox> values = new ArrayList<CheckBox>();
    for (CheckBox check : fields) {
      if (check.getValue()) {
        values.add(check);
      }
    }
    return values;
  }

}
