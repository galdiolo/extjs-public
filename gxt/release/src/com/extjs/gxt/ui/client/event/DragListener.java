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
 * Drag listener.
 */
public class DragListener implements Listener<DragEvent> {

  public void handleEvent(DragEvent de) {
    switch (de.type) {
      case Events.DragCancel:
        dragCancel(de);
        break;
      case Events.DragEnd:
        dragEnd(de);
        break;
      case Events.DragMove:
        dragMove(de);
        break;
      case Events.DragStart:
        dragStart(de);
        break;
    }
  }

  /**
   * Fires after a drag begins.
   * 
   * @param de the drag event
   */
  public void dragStart(DragEvent de) {

  }

  /**
   * Fires after the mouse moves.
   * 
   * @param de the drag event
   */
  public void dragMove(DragEvent de) {

  }

  /**
   * Fires after a drag ends.
   * 
   * @param de the drag event
   */
  public void dragEnd(DragEvent de) {

  }

  /**
   * Fires after a drag is cancelled.
   * 
   * @param de the drag event
   */
  public void dragCancel(DragEvent de) {

  }

}
