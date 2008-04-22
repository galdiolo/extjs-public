/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <code>Controllers</code> process and respond to application events.
 */
public abstract class Controller {

  /**
   * Forward an event to a view. Ensures the view is initialized before
   * forwarding the event.
   * 
   * @param view the view to forward the event
   * @param event the event to be forwarded
   */
  public static void forwardToView(View view, AppEvent event) {
    if (!view.initialized) {
      view.initialize();
      view.initialized = true;
    }
    view.handleEvent(event);
  }

  protected Controller parent;
  protected List<Controller> children;
  
  boolean initialized;
  private Set<Integer> supportedEvents;

  /**
   * Add a child controller.
   * 
   * @param controller the controller to added
   */
  public void addChild(Controller controller) {
    if (children == null) children = new ArrayList<Controller>();
    children.add(controller);
    controller.parent = this;
  }

  /**
   * Determines if the controller can handle the particular event. Default
   * implementation checks against registered event types then queries all child
   * controllers.
   * 
   * @param event the event
   * @return <code>true</code> if event can be handled, <code>false</code>
   *         otherwise
   */
  public boolean canHandle(AppEvent event) {
    if (supportedEvents != null && supportedEvents.contains(event.type)) return true;
    if (children != null) {
      for (Controller c : children) {
        if (c.canHandle(event)) return true;
      }
    }
    return false;
  }

  /**
   * Forwards an event to any child controllers who can handle the event.
   * 
   * @param event the event to forward
   */
  public void forwardToChild(AppEvent event) {
    if (children != null) {
      for (Controller c : children) {
        if (!c.initialized) {
          c.initialize();
          c.initialized = true;
        } 
        if (c.canHandle(event)) {
          c.handleEvent(event);
        }
      }
    }
  }

  /**
   * Processes the event.
   * 
   * @param event the current event
   */
  public abstract void handleEvent(AppEvent event);

  /**
   * Called once prior to handleEvent being called.
   */
  public void initialize() {

  }

  /**
   * Registers the event type.
   * 
   * @param types the event types
   */
  protected void registerEventTypes(int... types) {
    if (supportedEvents == null) {
      supportedEvents = new HashSet<Integer>();
    }
    if (types != null) {
      for (int type : types) {
        supportedEvents.add(type);
      }
    }
    
  }

}
