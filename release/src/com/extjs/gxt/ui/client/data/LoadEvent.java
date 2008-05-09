/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import com.extjs.gxt.ui.client.event.BaseEvent;

/**
 * Instances of this class are sent as a result of load operations.
 * 
 * @param <C> the load config type
 * @param <R> the load result type
 */
public class LoadEvent<C extends LoadConfig, R extends LoadResult> extends BaseEvent {

  /**
   * The loader that triggered this event.
   */
  public Loader loader;

  /**
   * The LoadConfig object
   */
  public C config;

  /**
   * The LoadResult object.
   */
  public R result;

  public LoadEvent(Loader loader, C config, R result) {
    this.loader = loader;
    this.config = config;
    this.result = result;
  }

}
