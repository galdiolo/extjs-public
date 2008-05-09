/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ext GWT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.util;

import java.util.EventListener;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TypedListener;

/**
 * Maps listeners by event type.
 */
public class EventTable {
  int[] types;
  Listener[] listeners;
  int level;

  public void hook(int eventType, Listener listener) {
    if (types == null) types = new int[4];
    if (listeners == null) listeners = new Listener[4];
    int length = types.length, index = length - 1;
    while (index >= 0) {
      if (types[index] != 0) break;
      --index;
    }
    index++;
    if (index == length) {
      int[] newTypes = new int[length + 4];
      System.arraycopy(types, 0, newTypes, 0, length);
      types = newTypes;
      Listener[] newListeners = new Listener[length + 4];
      System.arraycopy(listeners, 0, newListeners, 0, length);
      listeners = newListeners;
    }
    types[index] = eventType;
    listeners[index] = listener;
  }

  public boolean hooks(int eventType) {
    if (types == null) return false;
    for (int i = 0; i < types.length; i++) {
      if (types[i] == eventType) return true;
    }
    return false;
  }

  public boolean sendEvent(BaseEvent event) {
    if (types == null) return true;

    level += level >= 0 ? 1 : -1;
    try {
      for (int i = 0; i < types.length; i++) {
        if (types[i] == event.type) {
          Listener listener = listeners[i];
          if (listener != null) listener.handleEvent(event);
        }
      }
      return event.doit;
    } finally {
      boolean compact = level < 0;
      level -= level >= 0 ? 1 : -1;
      if (compact && level == 0) {
        int index = 0;
        for (int i = 0; i < types.length; i++) {
          if (types[i] != 0) {
            types[index] = types[i];
            listeners[index] = listeners[i];
            index++;
          }
        }
        for (int i = index; i < types.length; i++) {
          types[i] = 0;
          listeners[i] = null;
        }
      }
    }

  }

  public int size() {
    if (types == null) return 0;
    int count = 0;
    for (int i = 0; i < types.length; i++) {
      if (types[i] != 0) count++;
    }
    return count;
  }

  void remove(int index) {
    if (level == 0) {
      int end = types.length - 1;
      System.arraycopy(types, index + 1, types, index, end - index);
      System.arraycopy(listeners, index + 1, listeners, index, end - index);
      index = end;
    } else {
      if (level > 0) level = -level;
    }
    types[index] = 0;
    listeners[index] = null;
  }

  public void unhook(int eventType, Listener listener) {
    if (types == null) return;
    for (int i = 0; i < types.length; i++) {
      if (types[i] == eventType && listeners[i] == listener) {
        remove(i);
        return;
      }
    }
  }

  public void unhook(int eventType, EventListener listener) {
    if (types == null) return;
    for (int i = 0; i < types.length; i++) {
      if (types[i] == eventType) {
        if (listeners[i] instanceof TypedListener) {
          TypedListener typedListener = (TypedListener) listeners[i];
          if (typedListener.getEventListener() == listener) {
            remove(i);
            return;
          }
        }
      }
    }
  }

  public void removeAllListeners() {
    types = null;
    listeners = null;
    level = 0;
  }

  public boolean hasListener(int type) {
    if (types == null) return false;
    for (int i = 0; i < types.length; i++) {
      if (types[i] == type) return true;
    }
    return false;
  }

}
