/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Displays static text.
 */
public class LabelField extends Field {

  private String text;

  /**
   * Creates a new label field.
   */
  public LabelField() {
    baseStyle = "x-form-label";
    setLabelSeparator("");
  }

  /**
   * Creates a new label field.
   * 
   * @param text the label text
   */
  public LabelField(String text) {
    this();
    setText(text);
  }

  /**
   * Returns the field's text.
   * 
   * @return the text
   */
  public String getText() {
    return text;
  }

  @Override
  public Object getValue() {
    return text;
  }

  @Override
  public boolean isValid() {
    return true;
  }

  @Override
  public void markInvalid(String msg) {

  }

  /**
   * Sets the lable's text.
   * 
   * @param text the text as HTML
   */
  public void setText(String text) {
    this.text = text;
    if (rendered) {
      getElement().setInnerHTML(text);
    }
  }

  @Override
  public void setValue(Object value) {
    setText(value != null ? value.toString() : "");
  }

  @Override
  protected void onRender(Element parent, int index) {
    setElement(DOM.createDiv(), parent, index);
    if (text != null) {
      setText(text);
    }
  }

  @Override
  protected boolean validateValue(String value) {
    return true;
  }

}
