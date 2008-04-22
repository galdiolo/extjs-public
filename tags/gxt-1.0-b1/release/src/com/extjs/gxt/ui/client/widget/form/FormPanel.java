/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.widget.GenericContentPanel;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Element;

/**
 * A panel for displaying form wigets.
 */
public class FormPanel<T extends Field> extends GenericContentPanel<T> {

  /**
   * The label width in pixels (defaults to 75).
   */
  public int labelWidth = 75;

  /**
   * The default field width (defaults to 210).
   */
  public int fieldWidth = 210;

  /**
   * Valid values are "left," "top" and "right" (defaults to "left"). This
   * property cascades to child containers if not set.
   */
  public String labelAlign = "left";

  /**
   * Returns the form's valid state by querying all child fields.
   * 
   * @return true if value
   */
  public boolean isValid() {
    for (Component c : items) {
      Field f = (Field) c;
      if (!f.isValid()) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    FormLayout layout = new FormLayout();
    layout.defaultWidth = fieldWidth;
    layout.labelWidth = labelWidth;
    setLayout(layout);
    layout();
  }

}
