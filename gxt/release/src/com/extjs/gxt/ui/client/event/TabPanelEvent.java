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
 * @see TabPanel
 */
public class TabPanelEvent extends ContainerEvent<TabPanel, TabItem> {

  public TabPanelEvent(TabPanel container) {
    super(container);
  }

  public TabPanelEvent(TabPanel container, TabItem item) {
    super(container, item);
  }

}
