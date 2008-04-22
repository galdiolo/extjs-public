/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     GXT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.ArrayList;
import java.util.List;

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
    listener = new SelectionChangedListener() {
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
    provider.addSelectionListener(listener);
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
   * Called when any selection changed evetn is received from any registered
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
