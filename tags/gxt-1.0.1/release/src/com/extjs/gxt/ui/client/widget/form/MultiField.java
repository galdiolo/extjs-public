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
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A field that displays multiple fields in a single row.
 */
public class MultiField<F extends Field> extends Field<F> {

  protected List<F> fields;
  protected HorizontalPanel hp;
  protected Validator validator;

  /**
   * Creates a new checkbox group.
   */
  public MultiField() {
    fields = new ArrayList<F>();
    baseStyle = "x-form-group";
    invalidStyle = "none";
  }

  /**
   * Creates a new checkbox group.
   * 
   * @param fieldLabel the field label
   * @param fields the field(s) to add
   */
  public MultiField(String fieldLabel, F... fields) {
    this();
    setFieldLabel(fieldLabel);
    for (F f : fields) {
      add(f);
    }
  }

  /**
   * Adds a field.
   * 
   * @param field the field to add
   */
  public void add(F field) {
    fields.add(field);
  }

  @Override
  public void disable() {
    super.disable();
    for (Field field : fields) {
      field.disable();
    }
  }

  @Override
  public void enable() {
    super.enable();
    for (Field field : fields) {
      field.enable();
    }
  }

  /**
   * Returns the field at the index.
   * 
   * @param index the index
   * @return the field
   */
  public F get(int index) {
    return fields.get(index);
  }

  /**
   * Returns all the child field's.
   * 
   * @return the fields
   */
  public List<F> getAll() {
    return new ArrayList<F>(fields);
  }

  /**
   * Returns the field's validator.
   * 
   * @return the validator
   */
  public Validator getValidator() {
    return validator;
  }

  @Override
  public void onBrowserEvent(Event event) {

  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {

  }

  @Override
  public void setReadOnly(boolean readOnly) {
    for (Field field : fields) {
      field.setReadOnly(readOnly);
    }
  }

  /**
   * Sets the field's validator.
   * 
   * @param validator the validator
   */
  public void setValidator(Validator validator) {
    this.validator = validator;
  }

  @Override
  protected void doAttachChildren() {
    WidgetHelper.doAttach(hp);
  }

  @Override
  protected void doDetachChildren() {
    WidgetHelper.doDetach(hp);
  }

  @Override
  protected El getInputEl() {
    if (fields.size() > 0) {
      return fields.get(0).getInputEl();
    }
    return super.getInputEl();
  }

  @Override
  protected void onRender(Element target, int index) {
    hp = new HorizontalPanel();
    hp.setStyleAttribute("paddingTop", "3px");

    for (Field f : fields) {
      hp.add(f);
      if (f.getFieldLabel() != null) {
        Text lbl = new Text(f.getFieldLabel());
        lbl.setStyleName("x-form-group-label");
        hp.add(lbl);
      }
    }

    hp.render(target, index);
    setElement(hp.getElement());
  }

  @Override
  protected boolean validateValue(String value) {
    // validate multi field
    if (validator != null) {
      String msg = validator.validate(this, value);
      if (msg != null) {
        markInvalid(msg);
        return false;
      }
    }

    // validate fields
    for (Field f : fields) {
      if (!f.validateValue(value)) {
        markInvalid("sdfdsffd");
        return false;
      }
    }

    clearInvalid();
    return true;
  }
}
