/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.Observable;

/**
 * Interface for objects that can load remote data.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeLoad</b> : LoadEvent(loader, config)<br>
 * <div>Fires before a load operation. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Load</b> : LoadEvent(loader, config, result)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * <li>result : the load result</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>LoadException</b> : LoadEvent(loader, config, result)<br>
 * <div>Fires after the button is selected.</div>
 * <ul>
 * <li>loader : this</li>
 * <li>config : the load config</li>
 * <li>result : the load result</li>
 * </ul>
 * </dd>
 * </dl>
 * 
 * @see ListLoader
 * @see TreeLoader
 */
public interface Loader<C> extends Observable {

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
   * Adds a load listener.
   * 
   * @param listener the listener to add
   */
  public void addLoadListener(LoadListener listener);

  /**
   * 
   * Loads the data using the current configuration.
   * 
   * @return true if the load was requested
   */
  public boolean load();

  /**
   * Loads the data using the given load configuration.
   * 
   * @param loadConfig the load config
   * @return true if the load was requested
   */
  public boolean load(C loadConfig);

  /**
   * Removes a load listener.
   * 
   * @param listener the listener to remove
   */
  public void removeLoadListener(LoadListener listener);

}
