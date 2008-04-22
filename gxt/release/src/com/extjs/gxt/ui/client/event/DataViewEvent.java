/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.DataView;
import com.google.gwt.user.client.Element;

/**
 * DataView event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see DataView
 */
public class DataViewEvent extends ComponentEvent {

  /**
   * The source data view instance.
   */
  public DataView view;

  /**
   * The event element.
   */
  public Element element;

  /**
   * The element index.
   */
  public int index;

  /**
   * Creates a new data view event.
   * 
   * @param view the source view
   */
  public DataViewEvent(DataView view) {
    super(view);
    this.view = view;
  }

}
