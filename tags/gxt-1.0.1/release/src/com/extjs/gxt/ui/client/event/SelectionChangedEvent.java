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
import com.extjs.gxt.ui.client.util.Util;

/**
 * Selection event type.
 * 
 * @param <M> the data type
 */
public class SelectionChangedEvent<M extends ModelData> extends BaseEvent {

  private SelectionProvider provider;
  private List<M> selection;

  /**
   * Creates a new selection event.
   * 
   * @param provider the selection provider
   * @param selection the selection
   */
  public SelectionChangedEvent(SelectionProvider provider, List<M> selection) {
    this.provider = provider;
    this.selection = selection;
  }

  /**
   * Creates a new selection event.
   * 
   * @param provider the selection provider
   * @param selection the selection
   */
  public SelectionChangedEvent(SelectionProvider provider, M selection) {
    this.provider = provider;
    this.selection = Util.createList(selection);
  }

  /**
   * Returns the first selected item.
   * 
   * @return the selected item
   */
  public M getSelectedItem() {
    if (selection.size() > 0) {
      return selection.get(0);
    }
    return null;
  }

  /**
   * Returns the selection.
   * 
   * @return the selection
   */
  public List<M> getSelection() {
    return selection;
  }

  /**
   * Returns the selection provider.
   * 
   * @return the provider
   */
  public SelectionProvider getSelectionProvider() {
    return provider;
  }
}
