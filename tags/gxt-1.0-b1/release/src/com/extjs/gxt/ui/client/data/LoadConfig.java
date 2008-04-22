/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.Style.SortDir;

/**
 * <p/>Contains configuration data for data load operations.
 * 
 * <p/>For a default implementation see {@link BaseLoadConfig}.
 */
public interface LoadConfig {

  /**
   * The offset for the first record to retrieve.
   */
  public int getOffset();

  /**
   * The number of records being requested.
   */
  public int getLimit();

  /**
   * The field to sort by.
   */
  public String getSortField();

  /**
   * The requested sort direction.
   */
  public SortDir getSortDir();

}