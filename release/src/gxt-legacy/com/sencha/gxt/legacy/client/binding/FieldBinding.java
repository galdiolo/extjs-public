/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.binding;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.Record;
import com.sencha.gxt.legacy.client.data.ModelData;
import com.sencha.gxt.legacy.client.data.ModelDataValueProvider;
import com.sencha.gxt.widget.core.client.form.Field;

/**
 * A two-way binding between a ModelData and Field instance. The binding will be
 * 1-way when the bound model does not support change events.
 * 
 * @see ModelData
 * @see Field
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class FieldBinding {

  protected Field field;
  protected ModelData model;
  protected String property;
  protected Store store;

  private HandlerRegistration changeRegistration;
  private final ChangeHandler changeHandler;
  // private ChangeListener modelListener;
  private Converter converter;
  private boolean updateOriginalValue = false;

  /**
   * Creates a new binding instance.
   * 
   * @param field the bound field for the binding
   */
  public FieldBinding(Field field, String property) {
    this.field = field;
    this.property = property;

    changeHandler = new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        onFieldChange(event);
      }
    };

    // modelListener = new ChangeListener() {
    // public void modelChanged(ChangeEvent event) {
    // if (event.getType() == ChangeEventSource.Update) {
    // onModelChange((PropertyChangeEvent) event);
    // }
    // }
    // };
  }

  /**
   * Binds the model and field. This method also updates the fields original
   * value which controls the dirty state of the field.
   * 
   * @param model the model to be bound
   */
  public void bind(ModelData model) {
    if (this.model != null) {
      unbind();
    }
    this.model = model;
    changeRegistration = field.addHandler(changeHandler, ChangeEvent.getType());
    // if (model instanceof Model) {
    // ((Model) model).addChangeListener(modelListener);
    // }
    updateField(updateOriginalValue);
  }

  /**
   * Returns the bindings converter.
   * 
   * @return the converter
   */
  public Converter getConverter() {
    return converter;
  }

  /**
   * Returns the bound field.
   * 
   * @return the field
   */
  public Field<Object> getField() {
    return field;
  }

  /**
   * Returns the bound model instance.
   * 
   * @return the model
   */
  public ModelData getModel() {
    return model;
  }

  /**
   * Returns the model's bound property name.
   * 
   * @return the property name
   */
  public String getProperty() {
    return property;
  }

  /**
   * Returns the binding's store.
   * 
   * @return the store or null
   */
  public Store getStore() {
    return store;
  }

  /**
   * Returns true if the field's original value is updated when the field is
   * bound.
   * 
   * @return true if original value is updated
   */
  public boolean isUpdateOriginalValue() {
    return updateOriginalValue;
  }

  /**
   * Sets the converter which is used to translate data types when updating
   * either the field or model.
   * 
   * @param converter the converter
   */
  public void setConverter(Converter converter) {
    this.converter = converter;
  }

  /**
   * Sets the store for the binding. When a store is specified edits are done
   * via Records instances obtained from the Store.
   * 
   * @param store the store
   */
  public void setStore(Store<? extends ModelData> store) {
    this.store = store;
  }

  /**
   * True to update the field's original value when bound (defaults to false).
   * 
   * @param updateOriginalValue true to update the original value
   */
  public void setUpdateOriginalValue(boolean updateOriginalValue) {
    this.updateOriginalValue = updateOriginalValue;
  }

  /**
   * Unbinds the model and field by removing all listeners.
   */
  public void unbind() {
    if (model != null) {
      // if (model instanceof Model) {
      // ((Model) model).removeChangeListener(modelListener);
      // }
      model = null;
    }
    changeRegistration.removeHandler();

    if (updateOriginalValue) {
      field.setOriginalValue(null);
    }
    field.clear();
  }

  /**
   * Updates the field's value with the model value.
   */
  public void updateField() {
    updateField(false);
  }

  /**
   * Updates the field's value and original value with the model value. Updating
   * the original value will reset the field to a non-dirty state.
   * 
   * @param updateOriginalValue true to update the original value
   */
  public void updateField(boolean updateOriginalValue) {
    Object val = onConvertModelValue(model.get(property));

    if (updateOriginalValue) {
      field.setOriginalValue(val);
    }

    field.setValue(val);
  }

  /**
   * Updates the model's value with the field value.
   */
  public void updateModel() {
    Object val = onConvertFieldValue(field.getValue());
    if (store != null) {
      Record r = store.getRecord(model);
      if (r != null) {
        // r.setValid(property, field.isValid());
        r.addChange(new ModelDataValueProvider<Object>(property), val);
      }
    } else {
      model.set(property, val);
    }

  }

  protected Object onConvertFieldValue(Object value) {
    if (converter != null) {
      return converter.convertFieldValue(value);
    }
    return value;
  }

  protected Object onConvertModelValue(Object value) {
    if (converter != null) {
      return converter.convertModelValue(value);
    }
    return value;
  }

  protected void onFieldChange(ChangeEvent e) {
    updateModel();
  }

  // protected void onModelChange(PropertyChangeEvent event) {
  // if (event.getName().equals(property)) {
  // updateField();
  // }
  // }

}
