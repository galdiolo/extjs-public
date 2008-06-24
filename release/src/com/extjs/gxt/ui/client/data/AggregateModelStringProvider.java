/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows multiple providers to be registered by data type and property name.
 */
public class AggregateModelStringProvider implements ModelStringProvider {

  private static ModelStringProvider defaultProvider = new ModelStringProvider() {
    public String getStringValue(ModelData model, String property) {
      Object value = model.get(property);
      return value != null ? value.toString() : null;
    }
  };

  private ModelStringProvider stringProvider;
  private Map<String, ModelStringProvider> propertyProviders;
  private Map<Class, ModelStringProvider> classProviders;

  /**
   * Registers a provider by property name.
   * 
   * @param property the property name
   * @param provider the string provider
   */
  public void register(String property, ModelStringProvider provider) {
    if (propertyProviders == null) {
      propertyProviders = new HashMap<String, ModelStringProvider>();
    }
    propertyProviders.put(property, provider);
  }

  /**
   * Registers a provider by type.
   * 
   * @param type the type
   * @param provider the string provider
   */
  public void register(Class type, ModelStringProvider provider) {
    if (classProviders == null) {
      classProviders = new HashMap<Class, ModelStringProvider>();
    }
    classProviders.put(type, provider);
  }

  /**
   * Sets the default provider. This provider will be used of no others
   * providers match the request.
   * 
   * @param provider the default string provider
   */
  public void setDefault(ModelStringProvider provider) {
    this.stringProvider = provider;
  }

  public String getStringValue(ModelData model, String property) {
    if (propertyProviders != null && propertyProviders.containsKey(property)) {
      return propertyProviders.get(property).getStringValue(model, property);
    }

    if (classProviders != null && model.get(property) != null) {
      Class clz = model.<Object>get(property).getClass();
      if (classProviders.containsKey(clz)) {
        return classProviders.get(clz).getStringValue(model, property);
      }
    }
    ModelStringProvider temp = stringProvider != null ? stringProvider : defaultProvider;
    return temp.getStringValue(model, property);
  }

}
