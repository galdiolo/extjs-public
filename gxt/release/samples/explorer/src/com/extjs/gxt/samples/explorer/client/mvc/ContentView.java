/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.mvc;

import com.extjs.gxt.samples.explorer.client.AppEvents;
import com.extjs.gxt.samples.explorer.client.model.Entry;
import com.extjs.gxt.samples.explorer.client.pages.Page;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.viewer.SelectionService;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class ContentView extends View {

  private TabPanel tabPanel;

  public ContentView(Controller controller) {
    super(controller);
  }

  public void initialize() {
    tabPanel = new TabPanel();
    tabPanel.setBorderStyle(false);
    tabPanel.setBodyBorder(false);
    tabPanel.setTabScroll(true);
    tabPanel.setAnimScroll(true);
    SelectionService.get().register(tabPanel);
    ContentPanel center = (ContentPanel) Registry.get("centerPanel");
    center.add(tabPanel);
  }

  public void onShowPage(Entry entry) {
    Page page = entry.getPage();
    TabItem item = tabPanel.findItem(page.getId(), false);
    if (item == null) {
      item = new TabItem();
      item.setLayoutOnChange(true);
      item.setData(entry);
      item.setClosable(page.isClosable());
      item.setId(page.getId());
      item.setText(entry.getName());
      item.setLayout(new FitLayout());
      item.add(page);
      tabPanel.add(item);
    }
    tabPanel.setSelection(item);
  }

  protected void handleEvent(AppEvent event) {
    switch (event.type) {
      case AppEvents.ShowPage:
        Entry entry = (Entry) event.data;
        onShowPage(entry);
        break;
    }
  }

}
