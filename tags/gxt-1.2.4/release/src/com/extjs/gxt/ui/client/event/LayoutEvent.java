/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;

public class LayoutEvent extends BaseEvent {

  public Container container;
  public Layout layout;

  public LayoutEvent(Container container, Layout layout) {
    super(container);
    this.container = container;
    this.layout = layout;
  }
}
