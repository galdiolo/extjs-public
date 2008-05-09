/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

/**
 * An app event subclass for history events.
 */
public class HistoryEvent extends AppEvent {

  /**
   * Creates a new history event.
   * 
   * @param type the event type
   * @param data the event data
   * @param token the history token
   */
  public HistoryEvent(int type, Object data, String token) {
    super(type, data);
    this.token = token;
    historyEvent = true;
  }

  /**
   * Creates a new history event.
   * 
   * @param type the event type
   * @param token the history token
   */
  public HistoryEvent(int type, String token) {
    super(type);
    this.token = token;
    historyEvent = true;
  }

}
