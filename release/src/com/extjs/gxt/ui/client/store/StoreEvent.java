/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.store.Record.RecordUpdate;

/**
 * A store event.
 * 
 * @param <M> the model type
 */
public class StoreEvent<M extends ModelData> extends BaseEvent {

  /**
   * The source store.
   */
  public Store store;

  /**
   * The inset index.
   */
  public int index;

  /**
   * The operation
   */
  public RecordUpdate operation;

  /**
   * The record.
   */
  public Record record;

  /**
   * The the items.
   */
  public List<M> models;

  /**
   * The item.
   */
  public M model;

  /**
   * Creates a new store event.
   * 
   * @param store the store event
   */
  public StoreEvent(Store store) {
    super(store);
    this.store = store;
  }

}
