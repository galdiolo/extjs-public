/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.viewer.RemoteContentProvider;

/**
 * Interface for objects that can retrieve remote data.
 * 
 * @see DataLoader
 * @see RemoteContentProvider
 */
public interface Loader {

  /**
   * Fires before a request is made for data (value is 300).
   */
  public static final int BeforeLoad = 300;

  /**
   * Fires when new data has been loaded (value is 301).
   */
  public static final int Load = 301;

  /**
   * Fires if an exception occurs while retrieving data (value is 302).
   */
  public static final int LoadException = 302;

  /**
   * Returns <code>true</code> if remote sorting is enabled.
   * 
   * @return the remote sort state
   */
  public boolean getRemoteSort();

  /**
   * Returns the current sort direction.
   * 
   * @return the sort direction
   */
  public SortDir getSortDir();

  /**
   * Returns the current sort field.
   * 
   * @return the sort field
   */
  public String getSortField();

  /**
   * Returns the offset of the first record.
   * 
   * @return the current offset
   */
  public int getOffset();

  /**
   * Returns the current page size;
   * 
   * @return the current page size
   */
  public int getPageSize();

  /**
   * Sets the current sort dir.
   * 
   * @param dir the sort dir
   */
  public void setSortDir(SortDir dir);

  /**
   * Sets the current sort field.
   * 
   * @param field the sort field
   */
  public void setSortField(String field);

  /**
   * Sets the remote sort state.
   * 
   * @param remote true for remote sort, false for local sorting
   */
  public void setRemoteSort(boolean remote);

  /**
   * Loads the data using the specified configuation.
   * 
   * @param offset the offset of the first record to return
   * @param pageSize the page size
   */
  public void load(int offset, int pageSize);

  /**
   * 
   * Loads the data using the current configuration
   * 
   * implementors are to determine what the current configuration is
   */
  public void load();

  /**
   * Adds a listener bound by the given event type.
   * 
   * @param eventType the eventType
   * @param listener the listener to be added
   */
  public void addListener(int eventType, Listener listener);

  /**
   * Removes a listener.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void removeListener(int eventType, Listener listener);

}
