/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.grid;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.HttpProxy;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.XmlReader;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.http.client.RequestBuilder;

public class XmlGridExample extends LayoutContainer {

  public XmlGridExample() {
    setLayout(new FlowLayout(10));

    List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
    columns.add(new ColumnConfig("Sender", "Sender", 100));
    columns.add(new ColumnConfig("Email", "Email", 165));
    columns.add(new ColumnConfig("Phone", "Phone", 100));
    columns.add(new ColumnConfig("State", "State", 50));
    columns.add(new ColumnConfig("Zip", "Zip Code", 65));

    // create the column model
    ColumnModel cm = new ColumnModel(columns);

    // defines the xml structure
    ModelType type = new ModelType();
    type.root = "records";
    type.recordName = "record";
    type.addField("Sender", "Name");
    type.addField("Email");
    type.addField("Phone");
    type.addField("State");
    type.addField("Zip");

    // use a http proxy to get the data
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "data.xml");
    HttpProxy proxy = new HttpProxy(builder);

    // need a loader, proxy, and reader
    XmlReader reader = new XmlReader(type);

    final BaseListLoader loader = new BaseListLoader(proxy, reader);

    ListStore<ModelData> store = new ListStore<ModelData>(loader);
    final Grid grid = new Grid<ModelData>(store, cm);
    grid.setBorders(true);
    grid.setAutoExpandColumn("Sender");

    ContentPanel panel = new ContentPanel();
    panel.setFrame(true);
    panel.setCollapsible(true);
    panel.setAnimCollapse(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setIconStyle("icon-table");
    panel.setHeading("XML Table Demo");
    panel.setLayout(new FitLayout());
    panel.add(grid);
    panel.setSize(575, 350);

    // add buttons
    Button load = new Button("Load XML");
    load.addSelectionListener(new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        loader.load();
      }
    });
    panel.addButton(load);
    add(panel);

  }

}
