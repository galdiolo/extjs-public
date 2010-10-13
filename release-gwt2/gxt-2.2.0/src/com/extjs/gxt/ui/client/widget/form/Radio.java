/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.util.Util;

/**
 * Single radio field. Same as Checkbox, but provided as a convenience for
 * automatically setting the input type. Radio grouping is handled automatically
 * by the browser if you give each radio in a group the same name.
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>Field Focus</dd>
 * <dd>Field Blur</dd>
 * <dd>Field Change</dd>
 * <dd>Field Invalid</dd>
 * <dd>Field Valid</dd>
 * <dd>Field KeyPress</dd>
 * <dd>Field SpecialKey</dd>
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
    if (value == null) {
      value = new Boolean(false);
    }
    focusValue = value;
    if (value && group != null) {
      group.onRadioSelected(this);
    }
    super.setValue(value);
  }

  @Override
  protected void fireChangeEvent(Object oldValue, Object value) {
    super.fireChangeEvent(oldValue, value);
    if (value != null && value instanceof Boolean && ((Boolean) value).booleanValue()
        && !Util.equalWithNull(oldValue, value) && group != null) {
      FieldEvent e = new FieldEvent(group);
      e.setOldValue(oldValue);
      e.setValue(value);
      group.fireEvent(Events.Change, e);
    }
  }

  @Override
  protected void onClick(ComponentEvent be) {
    // if we click the boxLabel, the browser fires an own click event
    // automatically, so we ignore one of it
    if (boxLabelEl != null && boxLabelEl.dom.isOrHasChild(be.getTarget())) {
      return;
    }
    if (readOnly) {
      be.stopEvent();
      return;
    }
    setValue(true);
  }

}
