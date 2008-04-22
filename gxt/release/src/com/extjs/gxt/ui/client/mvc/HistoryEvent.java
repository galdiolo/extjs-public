/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.mvc;

/**
 * An app event subclass will specialized support history.
 */
public class HistoryEvent extends AppEvent {

  public HistoryEvent(int type, Object data) {
    super(type, data);
    historyEvent = true;
  }

  public HistoryEvent(int type) {
    super(type);
    historyEvent = true;
  }

}
