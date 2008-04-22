/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

/**
 * Defines the interface for object returned from a remote procudure call.
 * 
 * @param <Data> the data type
 */
public interface LoadResult<Data> {

  /**
   * Returns the success status for the load.
   * 
   * @return true for sucess
   */
  public boolean isSuccess();

  /**
   * Returns the total count. This value will not equal the number of records
   * being returned when paging is used.
   * 
   * @return the total count
   */
  public int getTotalLength();

  /**
   * Returns the current offset of the results.
   * 
   * @return the offset
   */
  public int getOffset();

  /**
   * Returns the data being returned from the load.
   * 
   * @return the data
   */
  public Data getData();
}