/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.Collection;

/**
 * <p/>Primary interface for GXT model objects without support events.
 * 
 * <p/>For models with event support see {@link Model}.
 * 
 * <p/>For a default implementation see {@link BaseModelData}
 */
public interface ModelData {

  /**
   * Returns the named property from this model instance.
   * 
   * @param property the property name
   * @return the value
   */
  public Object get(String property);

  /**
   * Sets a property.
   * 
   * @param property the property name
   * @param value property value
   * @return the old value for the property
   */
  public Object set(String property, Object value);

  /**
   * Removes the named property from this model instance.
   * 
   * @param property the property name
   * @return the old value for the property
   */
  public Object remove(String property);

  /**
   * <p/>Returns an collection of the model's property names.
   * 
   * <p/>The collection should be a snapshot of the property names that the
   * model represents.
   * 
   * <p/>Changes to the returned collection should not mutate this model
   * instance.
   */
  public Collection<String> getPropertyNames();

}
