/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.Events;

public class DNDListener implements Listener<DNDEvent> {

  public void handleEvent(DNDEvent e) {
    switch (e.type) {
      case Events.DragStart:
        dragStart(e);
        break;
      case Events.DragEnter:
        dragEnter(e);
        break;
      case Events.DragLeave:
        dragLeave(e);
        break;
      case Events.DragMove:
        dragMove(e);
        break;
      case Events.Drop:
        dragDrop(e);
        break;
    }
  }

  public void dragMove(DNDEvent e) {

  }

  public void dragStart(DNDEvent e) {

  }

  public void dragEnter(DNDEvent e) {

  }

  public void dragLeave(DNDEvent e) {

  }

  public void dragDrop(DNDEvent e) {

  }

}
