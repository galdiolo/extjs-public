/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.core.shared.event.CancellableEvent;

/**
 * Dispatchers are responsible for dispatching application events to
 * controllers.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>Dispatcher.BeforeDispatch</b> : MvcEvent(dispatcher, appEvent)<br>
 * <div>Fires before an event is dispatched. Listeners can cancel the action by
 * calling {@link CancellableEvent#setCancelled(boolean)}.</div>
 * <ul>
 * <li>dispatcher : this</li>
 * <li>appEvent : the application event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Dispatcher.AfterDispatch</b> : MvcEvent(dispatcher, appEvent)<br>
 * <div>Fires after an event has been dispatched.</div>
 * <ul>
 * <li>dispatcher : this</li>
 * <li>appEvent : the application event</li>
 * </ul>
 * </dd>
 * 
 * </dl>
 * 
 * @see DispatcherListener
 */
public class Dispatcher {
  private final HandlerManager manager = new HandlerManager(this);
  /**
   * Fires before an event is dispatched.
   */
  public static final GwtEvent.Type<DispatcherListener> BeforeDispatch = new GwtEvent.Type<DispatcherListener>();

  /**
   * Fires after an event has been dispatched.
   */
  public static final GwtEvent.Type<DispatcherListener> AfterDispatch = new GwtEvent.Type<DispatcherListener>();

  private static Dispatcher instance;

  private static boolean historyEnabled = true;

  /**
   * Forwards an application event to the dispatcher.
   * 
   * @param event the application event
   */
  public static void forwardEvent(AppEvent event) {
    get().dispatch(event);
  }

  /**
   * Creates and forwards an application event to the dispatcher.
   * 
   * @param eventType the application event type
   */
  public static void forwardEvent(GwtEvent.Type<?> eventType) {
    get().dispatch(eventType);
  }

  /**
   * Creates and forwards an application event to the dispatcher.
   * 
   * @param eventType the application event type
   * @param data the event data
   */
  public static void forwardEvent(GwtEvent.Type<?> eventType, Object data) {
    get().dispatch(new AppEvent(eventType, data));
  }

  /**
   * Creates and forwards an application event to the dispatcher.
   * 
   * @param eventType the application event type
   * @param data the event data
   * @param historyEvent true to mark event as a history event
   */
  public static void forwardEvent(GwtEvent.Type<?> eventType, Object data, boolean historyEvent) {
    AppEvent ae = new AppEvent(eventType, data);
    ae.setHistoryEvent(historyEvent);
    get().dispatch(ae);
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
  private Boolean supportsHistory = null;

  private Dispatcher() {
    controllers = new ArrayList<Controller>();
    history = new FastMap<AppEvent>();
    if (supportsHistory()) {
      History.addValueChangeHandler(new ValueChangeHandler<String>() {
        public void onValueChange(ValueChangeEvent<String> event) {
          String historyToken = event.getValue();
          if (history.containsKey(historyToken)) {
            dispatch(history.get(historyToken), false);
          }
        }
      });
    }
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
    manager.addHandler(BeforeDispatch, listener);
    manager.addHandler(AfterDispatch, listener);
  }

  /**
   * The dispatcher will query its controllers and pass the application event to
   * any controllers that can handle the particular event type.
   * 
   * @param event the application event
   */
  public void dispatch(AppEvent event) {
    dispatch(event, event.isHistoryEvent());
  }

  /**
   * The dispatcher will query its controllers and pass the application event to
   * controllers that can handle the particular event type.
   * 
   * @param type the event type
   */
  public void dispatch(GwtEvent.Type<?> type) {
    dispatch(new AppEvent(type));
  }

  /**
   * The dispatcher will query its controllers and pass the application event to
   * controllers that can handle the particular event type.
   * 
   * @param type the event type
   * @param data the app event data
   */
  public void dispatch(GwtEvent.Type<?> type, Object data) {
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
    manager.removeHandler(BeforeDispatch, listener);
    manager.removeHandler(AfterDispatch, listener);
  }

  private void dispatch(AppEvent event, boolean createhistory) {
    BeforeAppEvent before = new BeforeAppEvent(event);
    manager.fireEvent(before);
    if (before.isCanceled()) {
      List<Controller> copy = new ArrayList<Controller>(controllers);
      for (Controller controller : copy) {
        if (controller.canHandle(event)) {
          if (!controller.initialized) {
            controller.initialized = true;
            controller.initialize();
          }
          controller.handleEvent(event);
        }
      }
      manager.fireEvent(new AfterAppEvent(event));
    }
    if (createhistory && event.isHistoryEvent()) {
      String token = event.getToken();
      if (token == null) {
        token = "" + new Date().getTime();
      }
      history.put(token, event);
      if (supportsHistory()) {
        History.newItem(token, false);
      }
    }
  }

  private boolean supportsHistory() {
    if (supportsHistory == null) {
      supportsHistory = historyEnabled && GWT.isClient()
          && (Document.get().getElementById("__gwt_historyFrame") != null || !(GXT.isIE6() || GXT.isIE7()));
    }
    return supportsHistory;
  }
}
