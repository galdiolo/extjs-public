/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * Provides a selection service where <code>SelectionProviders</code> can
 * register, allowing any listeners to be notifed of selection events.
 */
public class SelectionService {

  private static SelectionService instance = new SelectionService();

  /**
   * Returns the singleton instance.
   * 
   * @return the service
   */
  public static SelectionService get() {
    return instance;
  }

  private SelectionChangedListener listener;
  private List<SelectionProvider> providers;
  private List<SelectionChangedListener> listeners;

  private SelectionService() {
    listener = new SelectionChangedListener<ModelData>() {
      public void selectionChanged(SelectionChangedEvent event) {
        onSelectionChanged(event);
      }
    };
    listeners = new ArrayList<SelectionChangedListener>();
    providers = new ArrayList<SelectionProvider>();
  }

  /**
   * Adds a listener to be notified of selection events from any registered
   * selection providers.
   * 
   * @param listener the listener to add
   */
  public void addListener(SelectionChangedListener listener) {
    listeners.add(listener);
  }

  /**
   * Returns a list of all current listeners.
   * 
   * @return the listeners
   */
  public List<SelectionChangedListener> getListeners() {
    return new ArrayList<SelectionChangedListener>(listeners);
  }

  /**
   * Returns the list of current providers.
   * 
   * @return the providers
   */
  public List<SelectionProvider> getProviders() {
    return new ArrayList<SelectionProvider>(providers);
  }

  /**
   * Registers a selection provider.
   * 
   * @param provider the provider to add
   */
  public void register(SelectionProvider provider) {
    provider.addSelectionChangedListener(listener);
    providers.add(provider);
  }

  /**
   * Removes a previously added listener.
   * 
   * @param listener the listener to remove
   */
  public void removeListener(SelectionChangedListener listener) {
    listeners.remove(listener);
  }

  /**
   * Unregisters a selection provider.
   * 
   * @param provider the provider to unregister
   */
  public void unregister(SelectionProvider provider) {
    provider.removeSelectionListener(listener);
    providers.remove(provider);
  }

  /**
   * Called when any selection changed event is received from any registered
   * providers.
   * 
   * @param event the selection changed event
   */
  protected void onSelectionChanged(SelectionChangedEvent event) {
    for (SelectionChangedListener l : listeners) {
      l.selectionChanged(event);
    }
  }
}
