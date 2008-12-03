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
import com.extjs.gxt.ui.client.widget.form.FormPanel;

/**
 * Aggregates one to many field bindings.
 */
public class Bindings {

  protected FormPanel panel;
  protected ModelData model;
  protected Map<Field, FieldBinding> bindings;

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
   * Removes a field binding.
   * 
   * @param binding the binding instance to add
   */
  public void removeFieldBinding(FieldBinding binding) {
    bindings.remove(binding);
  }

  /**
   * Returens all bindings.
   * 
   * @return the collection of bindings
   */
  public Collection<FieldBinding> getBindings() {
    return bindings.values();
  }

  public void bind(ModelData model) {
    if (this.model != null) {
      unbind();
    }
    this.model = model;
    for (FieldBinding binding : bindings.values()) {
      binding.bind(model);
    }
  }

  public void unbind() {
    if (model != null) {
      for (FieldBinding binding : bindings.values()) {
        binding.unbind();
      }
      model = null;
    }
  }

}
