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
 * Event interface for effect events.
 */
public class EffectListener implements Listener<BaseEvent> {

  public void handleEvent(BaseEvent be) {
    switch (be.type) {
      case Events.EffectCancel:
        effectStart(be);
        break;
      case Events.EffectComplete:
        effectComplete(be);
        break;
      case Events.EffectStart:
        effectStart(be);
        break;
    }
  }

  /**
   * Fires prior to an effect starting.
   * 
   * @param be an event containing information about the event
   */
  public void effectStart(BaseEvent be) {

  }

  /**
   * Fires after an effect has been cancelled.
   * 
   * @param be an event containing information about the event
   */
  public void effectCancel(BaseEvent be) {

  }

  /**
   * Fires after an effect has completed.
   * 
   * @param be an event containing information about the event
   */
  public void effectComplete(BaseEvent be) {

  }

}
