/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Interface for objects that can read raw data and produce a set of
 * <code>Model</code> objects.
 * 
 * @see Model
 */
public interface DataReader {

  /**
   * Reads the raw data and creates a set of Model instances.
   * 
   * @param data the data to read
   * @return a load result
   */
  public LoadResult read(LoadConfig loadConfig, Object data);

}
