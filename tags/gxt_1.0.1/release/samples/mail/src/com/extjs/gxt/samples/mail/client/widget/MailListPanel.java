/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.samples.resources.client.MailItem;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

public class MailListPanel extends ContentPanel {

  private Table table;
  private ListStore<MailItem> store;
  private TableBinder<MailItem> binder;
  
  public MailListPanel() {
    ToolBar toolBar = new ToolBar();
    TextToolItem create = new TextToolItem("Create");
    create.setIconStyle("icon-email-add");
    toolBar.add(create);

    TextToolItem reply = new TextToolItem("Reply");
    reply.setIconStyle("icon-email-reply");
    toolBar.add(reply);

    setTopComponent(toolBar);
    

    List<TableColumn> columns = new ArrayList<TableColumn>();
    columns.add(new TableColumn("sender", "Sender", .2f));
    columns.add(new TableColumn("email", "Email", .3f));
    columns.add(new TableColumn("subject", "Subject", .5f));

    TableColumnModel cm = new TableColumnModel(columns);

    table = new Table(cm);
    table.setSelectionMode(SelectionMode.MULTI);
    table.setBorders(false);
    
    add(table);

    store = new ListStore<MailItem>();

    binder = new TableBinder<MailItem>(table, store);
    binder.setAutoSelect(true);
    binder.addSelectionChangedListener(new SelectionChangedListener<MailItem>() {
      public void selectionChanged(SelectionChangedEvent<MailItem> event) {
        MailItem m = event.getSelectedItem();
        showMailItem(m);
      }
    });
    
    setLayout(new FitLayout());
  }
  
  public ListStore<MailItem> getStore() {
    return store;
  }
  
  public TableBinder<MailItem> getBinder() {
    return binder;
  }
  
  private void showMailItem(MailItem item) {
    AppEvent evt = new AppEvent(AppEvents.ViewMailItem, item);
    Dispatcher.forwardEvent(evt);
  }
  
}
