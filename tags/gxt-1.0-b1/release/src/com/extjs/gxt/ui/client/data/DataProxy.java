/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Defines the interface for objects that can retrieve data.
 */
public interface DataProxy<C extends LoadConfig> {

  /**
   * Data should be retrived using the specified load config.
   * 
   * @param reader the reader instance
   * @param loadConfig the config
   * @param callback the data callback
   */
  public void load(DataReader reader, C loadConfig, DataCallback callback);

}
