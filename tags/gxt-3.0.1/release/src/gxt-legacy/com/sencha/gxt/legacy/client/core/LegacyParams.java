/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.legacy.client.core.js.JsObject;
import com.sencha.gxt.legacy.client.core.js.JsUtil;

/**
 * Aggregates both a list of values and a map of named values. Allows methods to
 * support both list and maps in a single parameter.
 * <p>
 * Note that only one type of values should be specified.
 * </p>
 */
public class LegacyParams {

  private List<Object> values;
  private Map<String, Object> mapValues;

  /**
   * True if the parameters are a list of values.
   * 
   */
  private boolean isList = false;

  /**
   * True if the parameters are a map of key / value pairs.
   * 
   */
  private boolean isMap = false;

  /**
   * Creates a new params instance.
   */
  public LegacyParams() {

  }

  /**
   * Creates a new params instance.
   * 
   * @param values the initial values
   */
  public LegacyParams(Map<String, Object> values) {
    mapValues = values;
  }

  /**
   * Creates a new params instance.
   * 
   * @param values the initial values
   */
  public LegacyParams(Object... values) {
    for (int i = 0; i < values.length; i++) {
      add(values[i]);
    }
  }

  /**
   * Creates a new parameters instance.
   * 
   * @param key the key
   * @param value the value
   */
  public LegacyParams(String key, Object value) {
    mapValues = new FastMap<Object>();
    mapValues.put(key, value);
  }

  /**
   * Adds a value.
   * 
   * @param value the value to add
   * @return this
   */
  public LegacyParams add(Object value) {
    isList = true;
    if (values == null) values = new ArrayList<Object>();
    values.add(value);
    return this;
  }

  /**
   * Returns the list values.
   * 
   * @return the list values
   */
  public List<Object> getList() {
    return values;
  }

  /**
   * Returns the values as a map.
   * 
   * @return the map of values
   */
  public Map<String, Object> getMap() {
    return mapValues;
  }

  /**
   * Returns the values as a JavaScriptObject.
   * 
   * @return the values
   */
  public JavaScriptObject getValues() {
    if (values != null) {
      return JsUtil.toJavaScriptArray(values.toArray());
    } else if (mapValues != null) {
      return JsUtil.toJavaScriptObject(mapValues);
    }
    return new JsObject().getJsObject();
  }

  /**
   * Returns true if the parameters are a list.
   * 
   * @return true if a list
   */
  public boolean isList() {
    return isList;
  }

  /**
   * Returns true if the parameters are a map.
   * 
   * @return true if a map
   */
  public boolean isMap() {
    return isMap;
  }

  /**
   * Sets a value.
   * 
   * @param key the key
   * @param value the value
   * @return this
   */
  public LegacyParams set(String key, Object value) {
    isMap = true;
    if (value == null) return this;
    if (mapValues == null) {
      mapValues = new FastMap<Object>();
    }
    mapValues.put(key, value);
    return this;
  }

}
