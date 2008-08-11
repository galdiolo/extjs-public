/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class AutoHeightTabPanelPage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
  
  public AutoHeightTabPanelPage() {
    setLayout(new FlowLayout(8));
    
    TabPanel panel = new TabPanel();
    panel.setAutoHeight(true);
    panel.setPlain(true);
    panel.setWidth(400);
    
    String txt = TestData.DUMMY_TEXT_SHORT;
    
    TabItem item = new TabItem();
    item.setText("Tab 1");
    item.addStyleName("pad-text");
    item.addText(txt);
    
    TabItem item2 = new TabItem();
    item2.setText("Tab 2");
    item2.addStyleName("pad-text");
    item2.addText(txt + "<br>" + txt);
    
    TabItem item3 = new TabItem();
    item3.setText("Tab 3");
    item3.disable();
    
    panel.add(item);
    panel.add(item2);
    panel.add(item3);
    
    add(panel);
  }

}
