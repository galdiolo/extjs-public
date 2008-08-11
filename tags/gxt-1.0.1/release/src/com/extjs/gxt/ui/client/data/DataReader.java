/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Interface for objects that translate raw data into a given type.
 * 
 * @param <C> the config type
 * @param <D> the data type
 */
public interface DataReader<C, D> {

  /**
   * Reads the raw data and returns the typed data.
   * 
   * @param data the data to read
   * @return the data
   */
  public D read(C loadConfig, Object data);

}
