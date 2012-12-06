/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.examples.resources.client.model;

import com.sencha.gxt.core.client.ValueProvider;

public class MapValueProvider implements ValueProvider<ModelItem, Double> {
  private String field;

  public MapValueProvider(String field) {
    this.field = field;
  }

  @Override
  public String getPath() {
    return field;
  }

  @Override
  public Double getValue(ModelItem object) {
    return object.get(field);
  }

  @Override
  public void setValue(ModelItem object, Double value) {
    object.put(field, value);
  }
}
