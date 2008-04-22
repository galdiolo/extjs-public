/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;

/**
 * Container event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see Container
 */
public class ContainerEvent extends ComponentEvent {

  /**
   * The source container.
   */
  public Container container;

  /**
   * The child item.
   */
  public Component item;

  /**
   * The insert index.
   */
  public int index;

  /**
   * Creates a new event.
   * 
   * @param container the source container
   */
  public ContainerEvent(Container container) {
    super(container);
    this.container = container;
  }

}
