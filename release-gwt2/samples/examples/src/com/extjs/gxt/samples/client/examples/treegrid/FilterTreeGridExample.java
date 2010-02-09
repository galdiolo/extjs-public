/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.treegrid;

import java.util.Arrays;

import com.extjs.gxt.samples.resources.client.Resources;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.model.Folder;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGridCellRenderer;
import com.google.gwt.user.client.Element;

public class FilterTreeGridExample extends LayoutContainer {

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);

    setLayout(new FlowLayout(10));

    Folder model = TestData.getTreeModel();

    TreeStore<ModelData> store = new TreeStore<ModelData>();

    store.add(model.getChildren(), true);

    ColumnConfig name = new ColumnConfig("name", "Name", 100);
    name.setRenderer(new TreeGridCellRenderer<ModelData>());

    ColumnConfig date = new ColumnConfig("author", "Author", 100);

    ColumnConfig size = new ColumnConfig("genre", "Genre", 100);

    ColumnModel cm = new ColumnModel(Arrays.asList(name, date, size));

    ContentPanel cp = new ContentPanel();
    cp.setBodyBorder(false);
    cp.setHeading("TreeGrid");
    cp.setButtonAlign(HorizontalAlignment.CENTER);
    cp.setLayout(new FitLayout());
    cp.setFrame(true);
    cp.setSize(600, 300);

    TreeGrid<ModelData> tree = new TreeGrid<ModelData>(store, cm);
    tree.setBorders(true);
    tree.getStyle().setLeafIcon(Resources.ICONS.music());
    tree.setAutoExpandColumn("name");
    tree.setTrackMouseOver(false);

    cp.add(tree);

    add(cp);
    
    StoreFilterField<ModelData> filter = new StoreFilterField<ModelData>() {

      @Override
      protected boolean doSelect(Store<ModelData> store, ModelData parent,
          ModelData record, String property, String filter) {
        // only match leaf nodes
        if (record instanceof Folder) {
          return false;
        }
        String name = record.get("name");
        name = name.toLowerCase();
        if (name.startsWith(filter.toLowerCase())) {
          return true;
        }
        return false;
      }

    };
    filter.bind(store);

    ToolBar toolBar = new ToolBar();
    toolBar.setBorders(true);
    toolBar.add(new LabelToolItem("Filter:"));
    toolBar.add(filter);

    cp.setTopComponent(toolBar);
    
    
  }
}
