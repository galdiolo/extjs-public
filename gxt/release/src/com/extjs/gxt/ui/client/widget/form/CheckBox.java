/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Single checkbox field.
 * 
 * <dl>
 * <dt>Events:</dt>
 * 
 * <dd><b>Change</b> : FieldEvent(field, value, oldValue)<br>
 * <div>Fires when the field value has changed.</div>
 * <ul>
 * <li>field : this</li>
 * <li>value : the new value</li>
 * <li>oldValue : the old value</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class CheckBox extends Field<Boolean> {

  protected El wrap, input;

  /**
   * The text that appears beside the checkbox (defaults to null).
   */
  private String boxLabel;

  public CheckBox() {
    value = false;
    propertyEditor = new BooleanPropertyEditor();
  }

  @Override
  public void clearInvalid() {
    // do nothing
  }

  public String getBoxLabel() {
    return boxLabel;
  }

  @Override
  public String getRawValue() {
    String propName = isAttached() ? "checked" : "defaultChecked";
    return input.dom.getPropertyString(propName);
  }

  @Override
  public void markInvalid(String msg) {

  }

  /**
   * The text that appears beside the checkbox.
   * 
   * @param boxLabel the box label
   */
  public void setBoxLabel(String boxLabel) {
    this.boxLabel = boxLabel;
  }

  @Override
  public void setRawValue(String value) {
    boolean b = Boolean.valueOf(value).booleanValue();
    getInputEl().dom.setPropertyBoolean("checked", b);
    getInputEl().dom.setPropertyBoolean("defaultChecked", b);
  }

  @Override
  public void setValue(Boolean value) {
    setValueInternal(value);
    if (rendered) {
      String v = value == null ? "" : propertyEditor.getStringValue(value);
      setRawValue(v);
      validate();
    }
  }
  
  @Override
  protected El getInputEl() {
    return input;
  }

  @Override
  protected El getStyleEl() {
    return input;
  }

  @Override
  protected void onClick(ComponentEvent ce) {
    if (readOnly) {
      ce.stopEvent();
      return;
    }
    boolean v = getInputEl().dom.getPropertyBoolean("checked");
    setValue(v);
    focusValue = v;
    fireChangeEvent(!v, v);
  }
  
  @Override
  protected void onRender(Element target, int index) {
    if (this instanceof Radio) {
      input = new El(DOM.createInputRadio(""));
    } else {
      input = new El(DOM.createInputCheck());
    }

    input.setStyleAttribute("marginTop", "3px");

    wrap = new El(DOM.createDiv());
    wrap.dom.setPropertyString("hideFocus", "hideFocus");
    wrap.dom.setClassName("x-form-check-wrap");
    wrap.dom.appendChild(input.dom);
    setElement(wrap.dom, target, index);

    if (getBoxLabel() != null) {
      Element div = DOM.createDiv();
      div.setClassName("x-form-cb-label");
      div.setInnerHTML(boxLabel);
      wrap.dom.appendChild(div);
    }

    super.onRender(target, index);

    focusStyle = null;
  }

  private native void setValueInternal(Boolean b) /*-{
    this.@com.extjs.gxt.ui.client.widget.form.Field::value = b;
  }-*/;

}
