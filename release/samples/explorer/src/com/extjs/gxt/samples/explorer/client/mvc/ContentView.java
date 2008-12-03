/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.mvc;

import com.extjs.gxt.samples.client.examples.model.Entry;
import com.extjs.gxt.samples.explorer.client.AppEvents;
import com.extjs.gxt.samples.explorer.client.pages.Page;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TabPanelEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.History;

public class ContentView extends View {

  private TabPanel tabPanel;
  private Page current;

  public ContentView(Controller controller) {
    super(controller);
  }

  public void initialize() {
    tabPanel = new TabPanel();
    tabPanel.setBorderStyle(false);
    tabPanel.setBodyBorder(false);
    tabPanel.setTabScroll(true);
    tabPanel.setAnimScroll(true);
    tabPanel.addListener(Events.Remove, new Listener<TabPanelEvent>() {

      public void handleEvent(TabPanelEvent be) {
        TabItem item = be.item;
        Entry entry = (Entry) item.getData("entry");
        Dispatcher.forwardEvent(AppEvents.HidePage, entry);
      }

    });
    tabPanel.addListener(Events.Select, new Listener<TabPanelEvent>() {
      public void handleEvent(TabPanelEvent be) {
        String token = History.getToken();
        Entry entry = (Entry)be.item.getData("entry");
        if (token != null && (!token.equals(entry.getId()))) {
          History.newItem(entry.getId(), false);
        }
      }
    });
    ContentPanel center = (ContentPanel) Registry.get(AppView.CENTER_PANEL);
    center.add(tabPanel);
  }

  public void onShowPage(Entry entry) {
    Page page = entry.get("page");
    if (page == null) {
      page = new Page(entry);
      entry.set("page", page);
    }
    if (page == current) {
      return;
    }
    current = page;

    TabItem item = tabPanel.findItem(page.getId(), false);
    if (item == null) {
      item = new TabItem();
      item.setData("entry", entry);
      item.setClosable(entry.isClosable());
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
      case AppEvents.HidePage:
        current = null;
        break;
    }
  }

}
