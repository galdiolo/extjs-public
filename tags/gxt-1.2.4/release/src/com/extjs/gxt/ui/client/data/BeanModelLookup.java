/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.google.gwt.core.client.GWT;

/**
 * Returns <code>BeanModelFactories</code> for java beans.
 */
public abstract class BeanModelLookup {

  private static BeanModelLookup instance = (BeanModelLookup) GWT.create(BeanModelLookup.class);

  /**
   * Returns the singleton bean model lookup.
   * 
   * @return the singleton instance
   */
  public static BeanModelLookup get() {
    return instance;
  }

  /**
   * Returns the factory for the given bean.
   * 
   * @param bean the bean class
   * @return the factory
   */
  public abstract BeanModelFactory getFactory(Class bean);

}
