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
public class FormPanel extends GenericContentPanel<Field> {

  /**
   * Label alignment enumeration.
   */
  public enum LabelAlign {
    LEFT, TOP, RIGHT;
  }

  private LabelAlign labelAlign = LabelAlign.LEFT;
  private int labelWidth = 75;
  private int fieldWidth = 210;
  private int padding = 10;

  public FormPanel() {

  }

  /**
   * Returns the field width.
   * 
   * @return the field width
   */
  public int getFieldWidth() {
    return fieldWidth;
  }

  /**
   * Returns the label alignment.
   * 
   * @return the label alignment
   */
  public LabelAlign getLabelAlign() {
    return labelAlign;
  }

  /**
   * Returns the default width.
   * 
   * @return the label width
   */
  public int getLabelWidth() {
    return labelWidth;
  }

  /**
   * Returns the panel's padding.
   * 
   * @return the padding
   */
  public int getPadding() {
    return padding;
  }

  /**
   * Returns the form's valid state by querying all child fields.
   * 
   * @return true if value
   */
  public boolean isValid() {
    for (Component c : getItems()) {
      Field f = (Field) c;
      if (!f.isValid()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Sets the default field width (defaults to 210).
   * 
   * @param fieldWidth the field width
   */
  public void setFieldWidth(int fieldWidth) {
    this.fieldWidth = fieldWidth;
  }

  /**
   * Sets the label alignment.
   * 
   * @param align the alignment
   */
  public void setLabelAlign(LabelAlign align) {
    this.labelAlign = align;
  }

  /**
   * Sets the default label width.
   * 
   * @param labelWidth the label width
   */
  public void setLabelWidth(int labelWidth) {
    this.labelWidth = labelWidth;
  }

  /**
   * Sets the padding to be applied to the forms children (defaults to
   * 20,pre-render).
   * 
   * @param padding the padding
   */
  public void setPadding(int padding) {
    this.padding = padding;
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    body.setStyleAttribute("padding", padding);

    FormLayout layout = new FormLayout();
    layout.defaultWidth = getFieldWidth();
    layout.labelWidth = getLabelWidth();
    setLayout(layout);
    layout();

  }

}
