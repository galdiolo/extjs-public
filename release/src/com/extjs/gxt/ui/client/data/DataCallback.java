/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Callback used when sending remote data back to the client.
 */
public interface DataCallback {

  /**
   * Sets the load results.
   * 
   * @param result the result of the data load
   */
  public void setResult(LoadResult result);

}
