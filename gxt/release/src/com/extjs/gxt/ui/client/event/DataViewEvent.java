/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.DataView;
import com.extjs.gxt.ui.client.widget.DataViewItem;

/**
 * DataView event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see DataView
 */
public class DataViewEvent extends ContainerEvent<DataView, DataViewItem> {

  /**
   * The source data view instance.
   */
  public DataView view;

  public DataViewEvent(DataView view) {
    super(view);
  }

  public DataViewEvent(DataView view, DataViewItem item) {
    super(view, item);
  }

}
