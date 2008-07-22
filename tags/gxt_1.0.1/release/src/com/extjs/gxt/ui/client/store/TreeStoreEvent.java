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

/**
 * A tree store event.
 * 
 * @param <M> the model type
 */
public class TreeStoreEvent<M extends ModelData> extends StoreEvent<M> {

  /**
   * The parent model.
   */
  public M parent;

  /**
   * The child.
   */
  public M child;

  /**
   * The children.
   */
  public List<M> children;

  /**
   * Creates a new tree store event.
   * 
   * @param store the source store
   */
  public TreeStoreEvent(TreeStore store) {
    super(store);
  }

}
