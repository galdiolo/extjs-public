/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Selection event type.
 *
 * @param <M> the model data type
 */
public class SelectionEvent<M extends ModelData> extends BaseEvent {

  /**
   * The selection.
   */
  public List<M> selection;
  
  /**
   * The model.
   */
  public M model;
  
  /**
   * The index.
   */
  public int index;
  
  public SelectionEvent(Object source, List<M> selection) {
    super(source);
    this.selection = selection;
  }
  
  public SelectionEvent(Object source, M model) {
    super(source);
    this.model = model;
  }
  
}
