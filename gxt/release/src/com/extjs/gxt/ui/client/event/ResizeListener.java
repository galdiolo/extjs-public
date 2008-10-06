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
 * Resize listener.
 */
public class ResizeListener implements Listener<ResizeEvent>{

  public void handleEvent(ResizeEvent be) {
    switch (be.type) {
      case Events.ResizeStart:
        resizeStart(be);
        break;
      case Events.ResizeEnd:
        resizeEnd(be);
        break;
    }
  }
  
  public void resizeStart(ResizeEvent re) {
    
  }
  
  public void resizeEnd(ResizeEvent re) {
    
  }

}
