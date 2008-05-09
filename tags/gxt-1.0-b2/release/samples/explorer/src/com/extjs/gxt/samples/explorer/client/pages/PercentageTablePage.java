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

import com.extjs.gxt.samples.resources.client.Stock;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.widget.Button;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.table.CellRenderer;
import com.extjs.gxt.ui.client.widget.table.DateTimeCellRenderer;
import com.extjs.gxt.ui.client.widget.table.NumberCellRenderer;
import com.extjs.gxt.ui.client.widget.table.RowSelectionModel;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;

public class PercentageTablePage extends Container implements EntryPoint {

  public void onModuleLoad() {
    Viewport v = new Viewport();
    v.setLayout(new FillLayout(8));
    v.add(this);
    v.layout();
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    FlowLayout layout = new FlowLayout();
    layout.margin = 10;
    setLayout(layout);

    final NumberFormat currency = NumberFormat.getCurrencyFormat();
    final NumberFormat number = NumberFormat.getFormat("0.00");

    List<TableColumn> columns = new ArrayList<TableColumn>();

    TableColumn col = new TableColumn("Company", .35f);
    col.setMinWidth(50);
    col.setMaxWidth(300);
    columns.add(col);

    col = new TableColumn("Symbol", .15f);
    col.setMinWidth(75);
    columns.add(col);

    col = new TableColumn("Last", .15f);
    col.setMinWidth(50);
    col.setMaxWidth(125);
    col.setAlignment(HorizontalAlignment.RIGHT);
    col.setRenderer(new NumberCellRenderer(currency));
    columns.add(col);

    col = new TableColumn("Change", .15f);
    col.setMinWidth(50);
    col.setAlignment(HorizontalAlignment.RIGHT);
    col.setRenderer(new CellRenderer() {
      public String render(String property, Object value) {
        double val = ((Double) value).doubleValue();
        String style = val < 0 ? "red" : "green";
        return "<span style='color:" + style + "'>" + number.format(val) + "</span>";
      }
    });
    columns.add(col);

    col = new TableColumn("Last Updated", .15f);
    col.setMinWidth(50);
    col.setAlignment(HorizontalAlignment.RIGHT);
    col.setRenderer(new DateTimeCellRenderer("MM/d/y"));
    columns.add(col);

    TableColumnModel cm = new TableColumnModel(columns);
    Table tbl = new Table(cm);
    tbl.setSelectionModel(new RowSelectionModel(SelectionMode.MULTI));

    List<Stock> stocks = TestData.getStocks();
    for (int i = 0; i < stocks.size(); i++) {
      Stock stock = stocks.get(i);
      Object[] values = new Object[5];
      values[0] = stock.getName();
      values[1] = stock.getSymbol();
      values[2] = stock.getLast();
      values[3] = stock.getLast() - stock.getOpen();
      values[4] = stock.getLastTrans();

      TableItem item = new TableItem(values);
      tbl.add(item);
    }

    ContentPanel panel = new ContentPanel();
    panel.setCollapsible(true);
    panel.setFrame(true);
    panel.setAnimCollapse(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setIconStyle("icon-table");
    panel.setHeading("Table Demo");
    panel.setLayout(new FitLayout());
    panel.add(tbl);
    panel.setSize(575, 300);

    // built in support for top component
    ToolBar toolBar = new ToolBar();
    toolBar.add(new TextToolItem("Add", "icon-add"));
    toolBar.add(new SeparatorToolItem());
    toolBar.add(new TextToolItem("Remove", "icon-delete"));
    toolBar.add(new SeparatorToolItem());
    toolBar.add(new TextToolItem("Configure", "icon-plugin"));
    panel.setTopComponent(toolBar);

    // add buttons
    panel.addButton(new Button("Save"));
    panel.addButton(new Button("Cancel"));
    
    add(panel);

  }

}