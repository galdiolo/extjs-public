/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

/**
 * TabPanel event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see TabPanel
 */
public class TabPanelEvent extends ComponentEvent {

  /**
   * The event source.
   */
  public TabPanel tabPanel;

  /**
   * The tab item.
   */
  public TabItem item;

  /**
   * The insert index.
   */
  public int index;

  /**
   * Creates a new tab panel event.
   * 
   * @param tabPanel the source panel
   */
  public TabPanelEvent(TabPanel tabPanel) {
    super(tabPanel);
    this.tabPanel = tabPanel;
  }

}
