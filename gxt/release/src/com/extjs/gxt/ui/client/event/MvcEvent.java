/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;

/**
 * MVC event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 */
public class MvcEvent extends BaseEvent {

  public Dispatcher dispatcher;

  public AppEvent appEvent;

  public String name;

  public MvcEvent(Dispatcher d, AppEvent ae) {
    this.dispatcher = d;
    this.appEvent = ae;
  }

}
