/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.EventListener;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseTypedListener;
import com.extjs.gxt.ui.client.event.LoadListener;

class LoadTypedListener extends BaseTypedListener {

  public LoadTypedListener(EventListener listener) {
    super(listener);
  }

  @Override
  public void handleEvent(BaseEvent be) {
    LoadListener listener = (LoadListener) eventListener;
    LoadEvent le = (LoadEvent) be;
    switch (be.type) {
      case Loader.BeforeLoad:
        listener.loaderBeforeLoad(le);
        break;
      case Loader.LoadException:
        listener.loaderLoadException(le);
        break;
      case Loader.Load:
        listener.loaderLoad(le);
        break;
    }
  }

}
