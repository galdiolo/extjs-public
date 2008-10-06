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

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.binder.TableBinder;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.StoreFilterField;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.table.CellRenderer;
import com.extjs.gxt.ui.client.widget.table.DateTimeCellRenderer;
import com.extjs.gxt.ui.client.widget.table.NumberCellRenderer;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class TableFilterPage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    setLayout(new FlowLayout(10));
    setMonitorResize(false);

    final NumberFormat currency = NumberFormat.getCurrencyFormat();
    final NumberFormat number = NumberFormat.getFormat("0.00");

    List<TableColumn> columns = new ArrayList<TableColumn>();

    TableColumn col = new TableColumn("name", "Company", 150);
    col.setMinWidth(75);
    col.setMaxWidth(300);
    columns.add(col);

    col = new TableColumn("symbol", "Symbol", 50);
    columns.add(col);

    col = new TableColumn("last", "Last", 100);
    col.setMaxWidth(100);
    col.setAlignment(HorizontalAlignment.RIGHT);
    col.setRenderer(new NumberCellRenderer(currency));
    columns.add(col);

    col = new TableColumn("change", "Change", 100);
    col.setAlignment(HorizontalAlignment.RIGHT);
    col.setRenderer(new CellRenderer<TableItem>() {
      public String render(TableItem item, String property, Object value) {
        double val = (Double) value;
        String style = val < 0 ? "red" : "green";
        return "<span style='color:" + style + "'>" + number.format(val) + "</span>";
      }
    });
    columns.add(col);

    col = new TableColumn("date", "Last Updated", 100);
    col.setAlignment(HorizontalAlignment.RIGHT);
    col.setRenderer(new DateTimeCellRenderer("MM/d/y"));
    columns.add(col);

    TableColumnModel cm = new TableColumnModel(columns);

    Table tbl = new Table(cm);
    tbl.setSelectionMode(SelectionMode.SIMPLE);
    tbl.setHorizontalScroll(true);

    ListStore<Stock> store = new ListStore<Stock>();
    store.add(TestData.getStocks());
    
    TableBinder<Stock> binder = new TableBinder<Stock>(tbl, store);
    binder.init();

    ContentPanel panel = new ContentPanel();
    panel.setCollapsible(true);
    panel.setFrame(true);
    panel.setAnimCollapse(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setIconStyle("icon-table");
    panel.setHeading("Filter TableViewer Demo");
    panel.setLayout(new FitLayout());
    panel.add(tbl);
    panel.setSize(575, 350);

    StoreFilterField<Stock> filterField = new StoreFilterField<Stock>() {

      @Override
      protected boolean doSelect(Store store, Stock parent, Stock record, String property, String filter) {
        String name = record.getName().toString().toLowerCase();
        if (name.startsWith(filter.toLowerCase())) {
          return true;
        }
        return false;
      }

    };

    filterField.setEmptyText("Filter company...");
    filterField.bind(store);

    // built in support for top component
    ToolBar toolBar = new ToolBar();
    toolBar.add(new AdapterToolItem(filterField));
    panel.setTopComponent(toolBar);

    // add buttons
    panel.addButton(new Button("Save"));
    panel.addButton(new Button("Cancel"));

    add(panel);

  }

}
