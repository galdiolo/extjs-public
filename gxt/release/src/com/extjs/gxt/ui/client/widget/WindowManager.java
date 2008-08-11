/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * An object that represents a group of {@link Window} instances and provides
 * z-order management and window activation behavior.
 */
public class WindowManager {

  private static WindowManager instance;

  /**
   * Returns the singleton instance.
   * 
   * @return the window manager
   */
  public static WindowManager get() {
    if (instance == null) instance = new WindowManager();
    return instance;
  }

  private Window front;
  private List<Window> list;
  private Stack<Window> accessList;
  private Comparator<Window> comparator;
  private Listener listener;

  public WindowManager() {
    list = new ArrayList<Window>();
    accessList = new Stack<Window>();
    comparator = new Comparator<Window>() {
      public int compare(Window w1, Window w2) {
        Long d1 = (Long) w1.getData("_gxtdate");
        Long d2 = (Long) w2.getData("_gxtdate");
        return d1 == null || d1 < d2 ? -1 : 1;
      }
    };
    listener = new Listener() {
      public void handleEvent(BaseEvent be) {
        activateLast();
      }
    };
  }

  /**
   * Brings the specified window to the front of any other active windows.
   * 
   * @param window the window return True if the dialog was brought to the
   *          front, else false if it was already in front
   */
  public boolean bringToFront(Window window) {
    if (window != front) {
      window.setData("_gxtdate", System.currentTimeMillis());
      orderWindows(false);
      return true;
    }

    return false;
  }

  /**
   * Gets a registered window by id.
   * 
   * @param id the window id
   * @return the window
   */
  public Window get(String id) {
    int count = list.size();
    for (int i = 0; i < count; i++) {
      Window w = (Window) list.get(i);
      if (id.equals(w.getId())) {
        return w;
      }
    }
    return null;
  }

  /**
   * Gets the currently-active window in the group.
   * 
   * @return the active window
   */
  public Window getActive() {
    return front;
  }

  /**
   * Hides all windows in the group.
   */
  public void hideAll() {
    int count = list.size();
    for (int i = 0; i < count; i++) {
      Window w = (Window) list.get(i);
      w.hide();
    }
  }

  /**
   * Sends the specified window to the back of other active windows.
   * 
   * @param window the window
   * @return the window
   */
  public Window sendToBack(Window window) {
    window.setData("_gxtdate", System.currentTimeMillis());
    orderWindows(true);
    return window;
  }

  /**
   * Unregisters the window.
   * 
   * @param window the window to unregister
   */
  public void unregister(Window window) {
    list.remove(window);
    window.removeListener(Events.Hide, listener);
    accessList.remove(window);
  }

  protected void register(Window window) {
    list.add(window);
    accessList.push(window);
    window.setData("_gxtdate", System.currentTimeMillis());
    window.addListener(Events.Hide, listener);
  }

  private void activateLast() {
    for (int i = accessList.size() - 1; i >= 0; --i) {
      Window w = (Window) accessList.get(i);
      if (w.isVisible()) {
        setActiveWin(w);
        return;
      }
    }
    setActiveWin(null);
  }

  private void orderWindows(boolean reverse) {
    if (accessList.size() > 0) {
      Collections.sort(accessList, comparator);
      if (reverse) {
        Collections.reverse(accessList);
      }
      for (int i = 0; i < accessList.size(); i++) {
        Window w = (Window) accessList.get(i);
        w.el().updateZIndex(0);
        front = w;
      }
    }
  }

  private void setActiveWin(Window window) {
    if (window != front) {
      if (front != null) {
        front.setActive(false);
      }
      front = window;
      if (window != null) {
        window.setActive(true);
      }
    }
  }
}
