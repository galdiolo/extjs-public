/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.fx.Fx;

/**
 * Fx event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see Fx
 */
public class FxEvent extends BaseEvent {

  /**
   * The source fx.
   */
  public Fx fx;

  public FxEvent(Fx fx) {
    this.fx = fx;
  }

}
