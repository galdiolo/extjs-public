/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;

/**
 * Listener for <code>Loader</code> events.
 */
public class LoadListener implements Listener<LoadEvent> {

  public void handleEvent(LoadEvent le) {
    switch (le.type) {
      case Loader.BeforeLoad:
        loaderBeforeLoad(le);
        break;
      case Loader.Load:
        loaderLoad(le);
        break;
      case Loader.LoadException:
        loaderLoadException(le);
        break;
    }
  }

  /**
   * Fires before a load operation begins. Action can be cancelled by setting
   * the doit field to false.
   * 
   * @param le the load event
   */
  public void loaderBeforeLoad(LoadEvent le) {

  }

  /**
   * Fires when an exception occurs during a load operation.
   * 
   * @param le the load event
   */
  public void loaderLoadException(LoadEvent le) {

  }

  /**
   * Fires after a load operation completes.
   * 
   * @param le the load event
   */
  public void loaderLoad(LoadEvent le) {

  }
}
