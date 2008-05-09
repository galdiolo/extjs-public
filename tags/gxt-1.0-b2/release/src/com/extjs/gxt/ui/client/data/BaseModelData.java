/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Default <code>ModelData</code> implementation.
 */
public class BaseModelData implements ModelData, Serializable {

  protected RpcMap map;

  /**
   * Creates a new model data instance.
   */
  public BaseModelData() {
  }

  /**
   * Creates a new model with the given properties.
   * 
   * @param properties the initial properties
   */
  public BaseModelData(Map<String, Object> properties) {
    for (String property : properties.keySet()) {
      set(property, properties.get(property));
    }
  }

  public Object get(String property) {
    return map == null ? null : map.get(property);
  }

  /**
   * Returns a property value.
   * 
   * @param property the property name
   * @param valueWhenNull
   * @return the value
   */
  public Object get(String property, Object valueWhenNull) {
    Object value = get(property);
    return (value == null) ? valueWhenNull : value;
  }

  public Collection<String> getPropertyNames() {
    Set<String> set = new HashSet<String>();
    if (map != null) {
      set.addAll(map.keySet());
    }
    return set;
  }

  public Object remove(String property) {
    return map == null ? null : map.remove(property);
  }

  /**
   * Sets the property and fires an <i>Update</i> event.
   * 
   * @param property the property name
   * @param value the property value
   */
  public Object set(String property, Object value) {
    if (map == null) {
      map = new RpcMap();
    }
    return map.put(property, value);
  }

  @Override
  public String toString() {
    return map == null ? "empty" : map.toString();
  }
  
  public Map<String, Object> asMap() {
    return new HashMap<String, Object>(map);
  }
}
