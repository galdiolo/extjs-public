/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.List;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

/**
 * Container event type.
 * 
 * <p/> Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see LayoutContainer
 */
public class ContainerEvent<C extends Container, I extends Component> extends ComponentEvent {

  /**
   * The source container.
   */
  public C container;

  /**
   * The child item.
   */
  public I item;
  
  /**
   * The selected items.
   */
  public List<I> selected;

  /**
   * The insert index.
   */
  public int index;

  /**
   * Creates a new event.
   * 
   * @param container the source container
   */
  public ContainerEvent(C container) {
    this(container, null);
  }

  /**
   * Creates a new event.
   * 
   * @param container the source container
   * @param component the related component
   */
  public ContainerEvent(C container, I component) {
    super(container);
    this.container = container;
    this.item = component;
  }

}
