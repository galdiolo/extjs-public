/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.dnd.DragSource;
import com.extjs.gxt.ui.client.dnd.DropTarget;
import com.extjs.gxt.ui.client.dnd.StatusProxy;
import com.extjs.gxt.ui.client.dnd.DND.Operation;
import com.extjs.gxt.ui.client.widget.Component;

public class DNDEvent extends DomEvent {

  public DragSource source;
  public DropTarget target;
  public DragEvent dragEvent;
  public Object data;
  public Component component;
  public StatusProxy status;
  public Operation operation;

  public DNDEvent(DragSource source) {
    super(source);
  }

}
