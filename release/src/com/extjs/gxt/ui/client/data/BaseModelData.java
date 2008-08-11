/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Default <code>ModelData</code> implementation.
 */
public class BaseModelData implements ModelData, Serializable {

  protected RpcMap map;
  protected boolean allowNestedValues = true;

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
    super();
    setProperties(properties);
  }

  public <X> X get(String property) {
    if (allowNestedValues && property.indexOf(".") != -1) {
      List<String> paths = Arrays.asList(property.split("\\."));
      return (X) getNestedValue(this, paths);
    }
    return map == null ? null : (X) map.get(property);
  }

  /**
   * Returns a property value.
   * 
   * @param property the property name
   * @param valueWhenNull
   * @return the value
   */
  public <X> X get(String property, X valueWhenNull) {
    X value = (X) get(property);
    return (value == null) ? valueWhenNull : value;
  }

  public Map<String, Object> getProperties() {
    HashMap<String, Object> newMap = new HashMap<String, Object>();
    if (map != null) {
      newMap.putAll(map);
    }
    return newMap;
  }

  public Collection<String> getPropertyNames() {
    Set<String> set = new HashSet<String>();
    if (map != null) {
      set.addAll(map.keySet());
    }
    return set;
  }

  public <X> X remove(String property) {
    return map == null ? null : (X) map.remove(property);
  }

  /**
   * Sets the property and fires an <i>Update</i> event.
   * 
   * @param property the property name
   * @param value the property value
   */
  public <X> X set(String property, X value) {
    if (map == null) {
      map = new RpcMap();
    }
    if (allowNestedValues && property.indexOf(".") != -1) {
      List<String> paths = Arrays.asList(property.split("\\."));
      return (X) setNestedValue(this, paths, value);
    }
    return (X) map.put(property, value);
  }

  /**
   * Sets the properties.
   * 
   * @param properties the properties
   */
  public void setProperties(Map<String, Object> properties) {
    for (String property : properties.keySet()) {
      set(property, properties.get(property));
    }
  }

  @Override
  public String toString() {
    return map == null ? "empty" : map.toString();
  }

  protected <X> X getNestedValue(ModelData model, List<String> paths) {
    if (paths.size() == 1) {
      return (X) model.get(paths.get(0));
    } else {
      Object obj = model.get(paths.get(0));
      if (obj != null && obj instanceof ModelData) {
        ArrayList<String> tmp = new ArrayList<String>(paths);
        tmp.remove(0);
        return (X) getNestedValue((ModelData) obj, tmp);
      }
    }
    return null;
  }

  protected <X> X setNestedValue(ModelData model, List<String> paths, Object value) {
    if (paths.size() == 1) {
      return (X) model.set(paths.get(0), value);
    } else {
      Object data = model.get(paths.get(0));
      if (data != null && data instanceof ModelData) {
        ArrayList<String> tmp = new ArrayList<String>(paths);
        tmp.remove(0);
        return (X) setNestedValue((ModelData) data, tmp, value);
      }
    }
    return null;
  }
}
