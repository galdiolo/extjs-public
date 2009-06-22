/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.binding.Bindings;
import com.extjs.gxt.ui.client.data.ModelData;

/**
 * BindingEvent type.
 */
public class BindingEvent extends BaseEvent {
  
  /**
   * The model being bound.
   */
  public ModelData model;

  /**
   * Creates a new bindings event.
   * 
   * @param bindings the bindings instance
   * @param model the bound model
   */
  public BindingEvent(Bindings bindings, ModelData model) {
    super(bindings);
    this.model = model;
  }
}
