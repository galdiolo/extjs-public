/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Selection event type. A selection event is fired when the selection state of
 * a individual model changes. A <code>SelectionChangedEvent</code> is fired
 * when the overall selection state of a component changes.
 * 
 * @param <M> the model data type
 * 
 * @see SelectionChangedEvent
 */
public class SelectionEvent<M extends ModelData> extends BaseEvent {

  /**
   * The model.
   */
  public M model;

  /**
   * The index.
   */
  public int index;

  public SelectionEvent(Object source, M model) {
    super(source);
    this.model = model;
  }

}
