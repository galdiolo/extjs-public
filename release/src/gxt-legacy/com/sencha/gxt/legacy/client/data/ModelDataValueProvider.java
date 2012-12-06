/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.data;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * Simple ValueProvider implementation that can work with a {@link ModelData}
 * class, fetching properties with the given string key.
 * 
 * @param <V> the property type
 */
public class ModelDataValueProvider<V> implements ValueProvider<ModelData, V> {
  private final String property;

  public ModelDataValueProvider(String property) {
    this.property = property;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ModelDataValueProvider) {
      return property.equals(((ModelDataValueProvider<?>) obj).property);
    }
    return false;
  }

  @Override
  public String getPath() {
    return property;
  }

  @Override
  public V getValue(ModelData object) {
    return object.<V> get(property);
  }

  @Override
  public int hashCode() {
    return property.hashCode();
  }

  @Override
  public void setValue(ModelData object, V value) {
    object.set(property, value);
  }
}
