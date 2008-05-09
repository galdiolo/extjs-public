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
   * The number of records being requested.
   */
  public int getLimit();

  /**
   * The offset for the first record to retrieve.
   */
  public int getOffset();

  /**
   * The requested sort direction.
   */
  public SortDir getSortDir();

  /**
   * The field to sort by.
   */
  public String getSortField();

  /**
   * Sets the limit.
   * 
   * @param limit the limit
   */
  public void setLimit(int limit);

  /**
   * Sets the offset.
   * 
   * @param offset the offset
   */
  public void setOffset(int offset);

  /**
   * Sets the sort dir.
   * 
   * @param sortDir the sort dir
   */
  public void setSortDir(SortDir sortDir);

  /**
   * Sets the sort field.
   * 
   * @param sortField the sort field
   */
  public void setSortField(String sortField);

}