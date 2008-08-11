package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;

public class LayoutEvent extends BaseEvent {

  public Container container;
  public Layout layout;
  
  public LayoutEvent(Container container, Layout layout) {
    this.container = container;
    this.layout = layout;
  }
}
