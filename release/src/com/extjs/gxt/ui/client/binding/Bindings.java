/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.binding;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.form.Field;

/**
 * Aggregates one to many field bindings.
 * 
 * @see FieldBinding
 */
public class Bindings {

  protected ModelData model;
  protected Map<Field, FieldBinding> bindings;

  /**
   * Creates a new bindings instance.
   */
  public Bindings() {
    bindings = new HashMap<Field, FieldBinding>();
  }

  /**
   * Adds a field binding.
   * 
   * @param binding the binding instance to add
   */
  public void addFieldBinding(FieldBinding binding) {
    bindings.put(binding.getField(), binding);
  }

  /**
   * Binds the model instance.
   * 
   * @param model the model
   */
  public void bind(ModelData model) {
    if (this.model != null) {
      unbind();
    }
    this.model = model;
    for (FieldBinding binding : bindings.values()) {
      binding.bind(model);
    }
  }

  /**
   * Clears all fields by setting the value for each field to <code>null</code>.
   */
  public void clear() {
    for (FieldBinding fieldBinding : getBindings()) {
      fieldBinding.getField().setValue(null);
    }
  }

  /**
   * Returns the field binding for the given field.
   * 
   * @param field the field
   * @return the field binding or null of no match
   */
  public FieldBinding getBinding(Field field) {
    return bindings.get(field);
  }

  /**
   * Returens all bindings.
   * 
   * @return the collection of bindings
   */
  public Collection<FieldBinding> getBindings() {
    return bindings.values();
  }

  /**
   * Removes a field binding.
   * 
   * @param binding the binding instance to add
   */
  public void removeFieldBinding(FieldBinding binding) {
    bindings.remove(binding.getField());
  }

  /**
   * Unbinds the current model.
   */
  public void unbind() {
    if (model != null) {
      for (FieldBinding binding : bindings.values()) {
        binding.unbind();
      }
      model = null;
    }
  }

}
