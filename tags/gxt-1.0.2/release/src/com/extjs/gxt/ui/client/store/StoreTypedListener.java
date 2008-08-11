/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import java.util.EventListener;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseTypedListener;

class StoreTypedListener extends BaseTypedListener {

  public StoreTypedListener(EventListener listener) {
    super(listener);
  }

  @Override
  public void handleEvent(BaseEvent be) {
    StoreListener listener = (StoreListener)eventListener;
    switch (be.type) {
      case Store.Filter:
        listener.storeFilter((StoreEvent)be);
        break;
      case Store.Sort:
        listener.storeSort((StoreEvent)be);
        break;
      case Store.BeforeDataChanged:
        listener.storeBeforeDataChanged((StoreEvent)be);
        break;
      case Store.DataChanged:
        listener.storeDataChanged((StoreEvent)be);
        break;
      case Store.Add:
        listener.storeAdd((StoreEvent)be);
        break;
      case Store.Remove:
        listener.storeRemove((StoreEvent)be);
        break;
      case Store.Update:
        listener.storeUpdate((StoreEvent)be);
        break;
      case Store.Clear:
        listener.storeClear((StoreEvent)be);
        break;
    }
  }

}
