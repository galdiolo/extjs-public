/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of the <code>ChangeEventSource</code> interface.
 */
public class ChangeEventSupport implements ChangeEventSource {

  protected List<ChangeListener> listeners;

  public void addChangeListener(ChangeListener listener) {
    if (listeners == null) {
      listeners = new ArrayList<ChangeListener>();
    }
    listeners.add(listener);
  }

  public void notify(ChangeEvent event) {
    if (listeners != null) {
      for (ChangeListener listener : listeners) {
        listener.modelChanged(event);
      }
    }
  }

  public void removeChangeListener(ChangeListener listener) {
    if (listeners != null) {
      listeners.remove(listener);
    }
  }

}
