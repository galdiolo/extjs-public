/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.event.BaseObservable;
import com.extjs.gxt.ui.client.event.MvcEvent;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;

/**
 * Dispatchers are responsible for dispatching application events to
 * controllers.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Dispatcher.BeforeDispatch</b> : MvcEvent(dispatcher, appEvent)<br>
 * <div>Fires before an event is dispatched. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>dispatcher : this</li>
 * <li>appEvent : the app event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Dispatcher.AfterDispatch</b> : MvcEvent(dispatcher, appEvent)<br>
 * <div>Fires after an event has been dispatched.</div>
 * <ul>
 * <li>dispatcher : this</li>
 * <li>appEvent : the app event</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 * 
 * @see DispatcherListener
 */
public class Dispatcher extends BaseObservable {

  /**
   * Fires before an event is dispatched (value is 955).
   */
  public static final int BeforeDispatch = 955;

  /**
   * Fires after an event has been dispatched (value is 960).
   */
  public static final int AfterDispatch = 960;

  private static Dispatcher instance;

  /**
   * Forwards an app event to the dispatcher.
   * 
   * @param event the app event
   */
  public static void forwardEvent(AppEvent event) {
    instance.dispatch(event);
  }

  /**
   * Creates and forwards an app event to the dispatcher.
   * 
   * @param eventType the app event type
   */
  public static void forwardEvent(int eventType) {
    instance.dispatch(eventType);
  }

  /**
   * Creates and fowards an app event to the dispatcher.
   * 
   * @param eventType the app event type
   * @param data the event data
   */
  public static void forwardEvent(int eventType, Object data) {
    instance.dispatch(new AppEvent(eventType, data));
  }

  /**
   * Creates and fowards an app event to the dispatcher.
   * 
   * @param eventType the app event type
   * @param data the event data
   * @param historyEvent true to mark event as a history event
   */
  public static void forwardEvent(int eventType, Object data, boolean historyEvent) {
    AppEvent ae = new AppEvent(eventType, data);
    ae.historyEvent = historyEvent;
    instance.dispatch(ae);
  }

  /**
   * Returns the singleton instance.
   * 
   * @return the dispatcher
   */
  public static Dispatcher get() {
    if (instance == null) {
      instance = new Dispatcher();
    }
    return instance;
  }

  private Map<String, AppEvent> history;

  private List<Controller> controllers;

  private Dispatcher() {
    instance = this;
    controllers = new ArrayList<Controller>();
    history = new HashMap<String, AppEvent>();
    History.addHistoryListener(new HistoryListener() {
      public void onHistoryChanged(String historyToken) {
        if (history.containsKey(historyToken)) {
          dispatch(history.get(historyToken), false);
        }
      }
    });
  }

  /**
   * Adds a controller.
   * 
   * @param controller the controller to be added
   */
  public void addController(Controller controller) {
    if (!controllers.contains(controller)) {
      controllers.add(controller);
    }
  }

  /**
   * Adds a listener to receive dispatch events.
   * 
   * @param listener the listener to add
   */
  public void addDispatcherListener(DispatcherListener listener) {
    addListener(BeforeDispatch, listener);
    addListener(AfterDispatch, listener);
  }

  /**
   * The dispatcher will query its controllers and pass the app event to any
   * controllers that can handle the particular event type.
   * 
   * @param event the app event
   */
  public void dispatch(AppEvent event) {
    dispatch(event, true);
  }

  /**
   * The dispatcher will query its controllers and pass the app event to
   * controllers that can handle the particular event type.
   * 
   * @param type the event type
   */
  public void dispatch(int type) {
    dispatch(new AppEvent(type));
  }

  /**
   * The dispatcher will query its controllers and pass the app event to
   * controllers that can handle the particular event type.
   * 
   * @param type the event type
   * @param data the app event data
   */
  public void dispatch(int type, Object data) {
    dispatch(new AppEvent(type, data));
  }

  /**
   * Returns all controllers.
   * 
   * @return the list of controllers
   */
  public List<Controller> getControllers() {
    return controllers;
  }

  /**
   * Returns the dispatcher's history cache.
   * 
   * @return the history
   */
  public Map<String, AppEvent> getHistory() {
    return history;
  }

  /**
   * Removes a controller.
   * 
   * @param controller the controller to be removed
   */
  public void removeController(Controller controller) {
    controllers.remove(controller);
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to be removed
   */
  public void removeDispatcherListener(DispatcherListener listener) {
    removeListener(BeforeDispatch, listener);
    removeListener(AfterDispatch, listener);
  }

  private void dispatch(AppEvent event, boolean createhistory) {
    MvcEvent e = new MvcEvent(this, event);
    e.source = this;
    e.appEvent = event;
    if (fireEvent(BeforeDispatch, e)) {
      for (Controller controller : controllers) {
        if (controller.canHandle(event)) {
          if (!controller.initialized) {
            controller.initialized = true;
            controller.initialize();
          }
          controller.handleEvent(event);
        }
      }
      fireEvent(AfterDispatch, e);
    }
    if (createhistory && event.historyEvent) {
      String token = event.token;
      if (token == null) {
        token = "" + new Date().getTime();
      }
      history.put(token, event);
      History.newItem(token, false);
    }
  }

}
