/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * ListView event type.
 */
public class ListViewEvent extends BoxComponentEvent {

  /**
   * The source list view.
   */
  public ListView listView;

  public int index;

  public ModelData model;

  public Element element;

  public ListViewEvent(ListView listView, Event event) {
    super(listView, event);
    this.listView = listView;
  }

  public ListViewEvent(ListView listView) {
    super(listView);
    this.listView = listView;
  }

}
