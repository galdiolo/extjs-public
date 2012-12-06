/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.examples.resources.client.model;

import java.util.HashMap;

public class ModelItem extends HashMap<String, Double> {
  private String key;

  public ModelItem(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
