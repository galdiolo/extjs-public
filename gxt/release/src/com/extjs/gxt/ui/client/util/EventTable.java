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

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.BaseTypedListener;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * Maps listeners by event type.
 */
public class EventTable {

  private HashMap<Integer, List<Listener>> map;

  /**
   * Returns <code>true</code> if the event table has any listeners for the
   * given event type.
   * 
   * @param eventType the event type
   * @return the listener state
   */
  public boolean hasListener(int eventType) {
    if (map == null) {
      return false;
    }
    List<Listener> list = map.get(eventType);
    return (list != null && list.size() > 0);
  }

  /**
   * Hooks the listener by the given event type and element.
   * 
   * @param eventType the event type
   * @param listener the listener to be added
   */
  public void hook(int eventType, Listener listener) {
    if (map == null) {
      map = new HashMap<Integer, List<Listener>>();
    }
    List<Listener> listeners = map.get(eventType);
    if (listeners == null) {
      listeners = new ArrayList<Listener>();
      map.put(eventType, listeners);
    }
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }

  }

  /**
   * Removes all mapped listeners.
   */
  public void removeAllListeners() {
    map = null;
  }

  /**
   * Sends the event to any registered listeners.
   * 
   * @param be the base event
   * @return <code>true</code> if event was cancelled by any listeners
   */
  public boolean sendEvent(BaseEvent be) {
    if (map == null) return true;
    List<Listener> list = map.get(be.type);
    if (list == null) return true;
    for (int i = list.size() - 1; i >= 0; i--) {
      list.get(i).handleEvent(be);
    }
    return be.doit;
  }

  public void unhook(int eventType, EventListener listener) {
    if (map == null) return;
    List list = (List) map.get(eventType);
    if (list == null) return;
    Iterator<Listener> iter = list.iterator();
    while (iter.hasNext()) {
      Listener l = iter.next();
      if (l instanceof BaseTypedListener) {
        if (((BaseTypedListener) l).getEventListener() == listener) {
          iter.remove();
        }
      }
    }
  }

  /**
   * Unhooks the given listener for the given event type.
   * 
   * @param eventType the event type
   * @param listener the listener to be removed
   */
  public void unhook(int eventType, Listener listener) {
    if (map == null) return;
    List<Listener> list = map.get(eventType);
    if (list == null) return;
    Iterator<Listener> iter = list.iterator();
    while (iter.hasNext()) {
      Listener l = iter.next();
      if (l == listener) {
        iter.remove();
      }
    }
  }
}
