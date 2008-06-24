/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.Events;

/**
 * Listener for components that can be selected. Selection is a GXT event which
 * should not be confused with the browser click event.
 */
public abstract class SelectionListener<E extends ComponentEvent> implements Listener<E> {

  public void handleEvent(E ce) {
    if (ce.type == Events.Select) {
      componentSelected(ce);
    }
  }

  /**
   * Fires after a component is selected.
   * 
   * @param ce the component event
   */
  public abstract void componentSelected(E ce);

}
