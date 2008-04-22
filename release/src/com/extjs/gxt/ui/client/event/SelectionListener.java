/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.EventListener;

/**
 * Interface for components that can be selected. Selection is a GXT event which
 * should not be confused with the browser click event.
 */
public interface SelectionListener extends EventListener {

  /**
   * Fires after a component is selected.
   * 
   * @param ce the component event
   */
  public void componentSelected(ComponentEvent ce);

}
