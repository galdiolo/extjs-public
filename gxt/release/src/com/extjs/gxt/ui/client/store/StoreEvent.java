/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.store;

import java.util.List;

import com.extjs.gxt.ui.client.event.BaseEvent;

public class StoreEvent extends BaseEvent {

  public Store store;

  public int index;

  public int operation;

  public List<Record> records;

  public Record record;

}
