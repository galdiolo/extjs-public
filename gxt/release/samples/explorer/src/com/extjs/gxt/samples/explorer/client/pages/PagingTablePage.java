/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.samples.client.ExampleServiceAsync;
import com.extjs.gxt.samples.client.Examples;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.PagingToolBar;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class PagingTablePage extends LayoutContainer {

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    final ExampleServiceAsync service = (ExampleServiceAsync) Registry.get(Examples.SERVICE);

    if (service == null) {
      MessageBox box = new MessageBox();
      box.setButtons(MessageBox.OK);
      box.setIcon(MessageBox.INFO);
      box.setTitle("Information");
      box.setMessage("No service detected");
      box.show();
      return;
    }

    FlowLayout layout = new FlowLayout(10);
    setLayout(layout);

    List<TableColumn> columns = new ArrayList<TableColumn>();
    columns.add(new TableColumn("forum", "Forum", 150));
    columns.add(new TableColumn("username", "Username", 100));
    columns.add(new TableColumn("subject", "Subject", 200));
    columns.add(new TableColumn("date", "Date", 100));

    // create the column model
    TableColumnModel cm = new TableColumnModel(columns);
    Table table = new Table(cm);

    RpcProxy proxy = new RpcProxy() {
      @Override
      public void load(Object loadConfig, AsyncCallback callback) {
        service.getPosts((PagingLoadConfig) loadConfig, callback);
      }
    };

    // loader
    final BasePagingLoader loader = new BasePagingLoader(proxy);
    loader.setRemoteSort(true);

    // store
    ListStore<Stock> store = new ListStore<Stock>(loader);

    // binder
    new TableBinder<Stock>(table, store);

    final PagingToolBar toolBar = new PagingToolBar(50);
    toolBar.bind(loader);

    ContentPanel panel = new ContentPanel();
    panel.setFrame(true);
    panel.setCollapsible(true);
    panel.setAnimCollapse(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setIconStyle("icon-table");
    panel.setHeading("Paging Table");
    panel.setLayout(new FitLayout());
    panel.add(table);
    panel.setSize(600, 450);
    panel.setBottomComponent(toolBar);

    loader.load(0, 50);

    add(panel);
  }
}
