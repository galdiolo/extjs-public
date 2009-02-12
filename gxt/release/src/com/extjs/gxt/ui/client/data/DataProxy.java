/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Defines the interface for objects that can retrieve data.
 */
public interface DataProxy<C, D> {

  /**
   * Data should be retrieved using the specified load config.
   * 
   * @param reader the reader instance
   * @param loadConfig the config
   * @param callback the data callback
   */
  public void load(DataReader<C, D> reader, C loadConfig, AsyncCallback<D> callback);

}
