/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.fx.Effect;
import com.extjs.gxt.ui.client.fx.Fx;

/**
 * Fx event type.
 * 
 * @see Fx
 */
public class FxEvent extends BaseEvent {

  /**
   * The source fx.
   */
  public Fx fx;

  /**
   * The current effect.
   */
  public Effect effect;

  public FxEvent(Fx fx, Effect effect) {
    super(fx);
    this.fx = fx;
    this.effect = effect;
  }

}
