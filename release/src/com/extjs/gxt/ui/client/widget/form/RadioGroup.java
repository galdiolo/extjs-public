/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.GXT;
import com.google.gwt.user.client.Element;

/**
 * A group of Radio's.
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Change</b> : FieldEvent(field, value, oldValue)<br>
 * <div>Fires after a child radio is selected.</div>
 * <ul>
 * <li>field : this the group, not the radio</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class RadioGroup extends MultiField<Radio> {

  /**
   * TextField Messages.
   */
  public class RadioGroupMessages extends FieldMessages {
    private String selectionRequired = GXT.MESSAGES.textField_blankText();

    public String getSelectionRequired() {
      return selectionRequired;
    }

    public void setSelectionRequired(String selectionRequired) {
      this.selectionRequired = selectionRequired;
    }
  }

  private static int autoId = 0;
  private String groupName;
  private boolean selectionRequired;

  /**
   * Creates a new radio group.
   */
  public RadioGroup() {
    this("gxt.RadioGroup." + (autoId++));
  }

  /**
   * Creates a new radio group.
   * 
   * @param name the group name
   */
  public RadioGroup(String name) {
    this.groupName = name;
    messages = new RadioGroupMessages();
  }

  @Override
  public RadioGroupMessages getMessages() {
    return (RadioGroupMessages) messages;
  }

  /**
   * Returns the selected radio.
   */
  @Override
  public Radio getValue() {
    for (Radio r : fields) {
      if (r.getValue()) {
        return r;
      }
    }
    return null;
  }

  /**
   * Returns true if a selection is required.
   * 
   * @return the selection required state
   */
  public boolean isSelectionRequired() {
    return selectionRequired;
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isValid() {
    if (selectionRequired) {
      boolean sel = false;
      for (Radio radio : fields) {
        if (radio.getValue()) {
          sel = true;
        }
      }
      if (!sel) {
        markInvalid(getMessages().getSelectionRequired());
        return false;
      }
    }
    for (Radio radio : fields) {
      if (!radio.isValid()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Sets whether a selecton is required when validating the group (defaults to
   * false).
   * 
   * @param selectionRequired true to require a selection
   */
  public void setSelectionRequired(boolean selectionRequired) {
    this.selectionRequired = selectionRequired;
  }

  @Override
  public void setValue(Radio value) {
    for (Radio r : fields) {
      if (r.equals(value)) {
        r.setValue(true);
      }
    }
  }

  protected void onRadioClick(Radio radio) {
    for (Radio r : fields) {
      if (r == radio) {
        r.setValue(true);
      } else {
        r.setValue(false);
      }
    }
  }

  protected void onRadioSelected(Radio radio) {
    for (Radio r : fields) {
      if (r != radio && r.getValue()) {
        r.setValue(false);
      }
    }
    clearInvalid();
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
