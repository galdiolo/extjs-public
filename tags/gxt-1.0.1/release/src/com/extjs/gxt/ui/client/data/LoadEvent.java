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
 * @param <D> the result data type
 */
public class LoadEvent<C, D> extends BaseEvent {

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
  public D data;

  public Throwable exception;

  /**
   * Creates a new load event.
   * 
   * @param loader the data loader
   */
  public LoadEvent(Loader loader) {
    this.loader = loader;
  }

  /**
   * Creates a new load event.
   * 
   * @param loader the data loader
   * @param config the config object
   */
  public LoadEvent(Loader loader, C config) {
    this.loader = loader;
    this.config = config;
  }

  /**
   * Creates a new load event.
   * 
   * @param loader the data loader
   * @param config the config object
   * @param data the data
   */
  public LoadEvent(Loader loader, C config, D data) {
    this.loader = loader;
    this.config = config;
    this.data = data;
  }

  /**
   * Creates a new load event.
   * 
   * @param loader the data loader
   * @param config the config object
   * @param t the exception
   */
  public LoadEvent(Loader loader, C config, Throwable t) {
    this.loader = loader;
    this.config = config;
    this.exception = t;
  }

}
