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

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Element;

/**
 * A panel for displaying form wigets. By default, FormPanel uses a FormLayout,
 * but this may be overridden.
 * 
 * <p/>FormPanel supports nested layout containers. Fields should only be added
 * to layout containers with a form layout. The form panel settings only apply
 * to the panel's direct children.
 */
public class FormPanel extends ContentPanel {

  /**
   * Label alignment enumeration.
   */
  public enum LabelAlign {
    LEFT, TOP, RIGHT;
  }

  private LabelAlign labelAlign = LabelAlign.LEFT;
  private int labelWidth = 75;
  private int fieldWidth = 210;

  public FormPanel() {

  }

  /**
   * Returns all of the panel's child fields. Nested containers are included in
   * the returned list.
   * 
   * @return the fields
   */
  public List<Field> getFields() {
    List<Field> fields = new ArrayList<Field>();
    getChildFields(this, fields);
    return fields;
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
   * Returns the form's valid state by querying all child fields.
   * 
   * @return true if value
   */
  public boolean isValid() {
    boolean valid = true;
    for (Field f : getFields()) {
      if (!f.isValid()) {
        valid = false;
      }
    }
    return valid;
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
   * Sets all of the panel's fields read only state.
   * 
   * @param readOnly true for read only
   */
  public void setReadOnly(boolean readOnly) {
    for (Field f : getFields()) {
      f.setReadOnly(readOnly);
    }
  }

  @Override
  protected void onRemove(Component item) {
    super.onRemove(item);
    if (rendered && item.isRendered()) {
      El wrap = item.el().findParentNode(".x-form-item", 3);
      if (wrap != null) {
        wrap.removeFromParent();
      }
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    body.setStyleAttribute("background", "none");

    if (getLayout() == null) {
      FormLayout layout = new FormLayout();
      layout.setDefaultWidth(fieldWidth);
      layout.setLabelWidth(labelWidth);
      layout.setLabelAlign(labelAlign);
      setLayout(layout);
    }
  }

  private void getChildFields(Container<Component> c, List<Field> fields) {
    for (Component comp : c.getItems()) {
      if (comp instanceof Field) {
        fields.add((Field) comp);
      } else if (comp instanceof Container) {
        getChildFields((Container) comp, fields);
      }
    }

  }

}
