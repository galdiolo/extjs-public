/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import java.util.EventListener;

/**
 * Store event interface.
 */
public interface StoreListener extends EventListener {

  /**
   * Fires when Records have been added to the Store.
   * 
   * @param se the store event
   */
  public void add(StoreEvent se);

  /**
   * Fires when a Record has been removed from the Store.
   * 
   * @param se the store event
   */
  public void remove(StoreEvent se);

  /**
   * Fires when a Record has been updated.
   * 
   * @param se the store event
   */
  public void update(StoreEvent se);

  /**
   * Fires when the data cache has been cleared.
   * 
   * @param se the store event
   */
  public void clear(StoreEvent se);

  /**
   * Fires after a new set of Records has been loaded.
   * 
   * @param se the store event
   */
  public void load(StoreEvent se);

  /**
   * Fires after a new set of Records has been loaded.
   * 
   * @param se the store event
   */
  public void loadException(StoreEvent se);

  /**
   * Fires before a request is made for a new data object.
   * 
   * @param se the store event
   */
  public void beforeLoad(StoreEvent se);

  /**
   * Fires when the data cache has changed, and a widget which is using this
   * Store as a Record cache should refresh its view.
   * 
   * @param se the store event
   */
  public void dataChanged(StoreEvent se);

}
