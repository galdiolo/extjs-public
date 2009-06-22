/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentManager;

/**
 * Event type for ComponentManager.
 */
public class ComponentManagerEvent extends BaseEvent {

  /**
   * The component being registered or unregistred.
   */
  public Component component;

  public ComponentManagerEvent(ComponentManager manager, Component component) {
    super(manager);
    this.component = component;
  }

}
