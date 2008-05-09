/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.mvc;

import com.extjs.gxt.samples.explorer.client.Explorer;
import com.extjs.gxt.samples.explorer.client.model.Entry;
import com.extjs.gxt.samples.explorer.client.model.ExplorerModel;
import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.viewer.DataListViewer;
import com.extjs.gxt.ui.client.viewer.ModelContentProvider;
import com.extjs.gxt.ui.client.viewer.ModelLabelProvider;
import com.extjs.gxt.ui.client.viewer.ModelTreeContentProvider;
import com.extjs.gxt.ui.client.viewer.SelectionChangedEvent;
import com.extjs.gxt.ui.client.viewer.SelectionChangedListener;
import com.extjs.gxt.ui.client.viewer.SelectionService;
import com.extjs.gxt.ui.client.viewer.SourceSelectionChangedListener;
import com.extjs.gxt.ui.client.viewer.TreeViewer;
import com.extjs.gxt.ui.client.viewer.Viewer;
import com.extjs.gxt.ui.client.viewer.ViewerFilterField;
import com.extjs.gxt.ui.client.viewer.ViewerSorter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.IconButton;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.TabPanel.TabPosition;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.tree.Tree;

public class NavigationView extends View {

  private ExplorerModel model;
  private ContentPanel westPanel;
  private ToolBar toolBar;
  private TabPanel tabPanel;
  private TabItem listItem, treeItem;
  private TreeViewer treeViewer;
  private DataListViewer listViewer;
  private ViewerFilterField filter;

  public NavigationView(Controller controller) {
    super(controller);
  }

  protected void initialize() {
    model = (ExplorerModel) Registry.get("model");
    SelectionService.get().addListener(new SelectionChangedListener() {
      public void selectionChanged(SelectionChangedEvent event) {
        BaseTreeModel m = (BaseTreeModel) event.getSelection().getFirstElement();
        if (m instanceof Entry) {
          Explorer.showPage((Entry) m);
        }
      }
    });

    filter = new ViewerFilterField<Model, Model>() {
      protected boolean doSelect(Model parent, Model element, String filter) {
        if (element instanceof Folder) {
          return false;
        }
        String name = element.get("name").toString().toLowerCase();
        if (name.indexOf(filter.toLowerCase()) != -1) {
          return true;
        }
        return false;
      }
    };

    westPanel = (ContentPanel) Registry.get("westPanel");
    westPanel.setHeading("Navigation");
    westPanel.setLayout(new FitLayout());
    westPanel.add(createTabPanel());

    toolBar = (ToolBar)westPanel.getTopComponent();
    IconButton filterBtn = new IconButton("icon-filter");
    filterBtn.setWidth(20);
    toolBar.add(new AdapterToolItem(filterBtn));
    toolBar.add(new AdapterToolItem(filter));

    createListContent();
    createTreeContent();

  }

  private TabPanel createTabPanel() {
    tabPanel = new TabPanel();
    tabPanel.setMinTabWidth(70);
    tabPanel.setBorderStyle(false);
    tabPanel.setBodyBorder(false);
    tabPanel.setTabPosition(TabPosition.BOTTOM);

    treeItem = new TabItem();
    treeItem.setText("Tree");
    tabPanel.add(treeItem);

    listItem = new TabItem();
    listItem.setText("List");
    tabPanel.add(listItem);
    
    return tabPanel;
  }

  private void createTreeContent() {
    Tree tree = new Tree();
    tree.setItemIconStyle("icon-list");
    treeViewer = new TreeViewer(tree);
    treeViewer.setLabelProvider(new ModelLabelProvider());
    treeViewer.setContentProvider(new ModelTreeContentProvider());
    treeViewer.setInput(model, true);
    SelectionService.get().addListener(new SourceSelectionChangedListener(treeViewer));
    SelectionService.get().register(treeViewer);

    filter.bind(treeViewer);

    treeItem.setBorders(false);
    treeItem.setScrollMode(Scroll.AUTO);
    treeItem.add(tree);
  }

  private void createListContent() {
    listItem.setLayout(new FitLayout());
    listItem.setBorders(false);

    DataList dataList = new DataList();
    dataList.setScrollMode(Scroll.AUTO);
    dataList.setBorders(false);
    dataList.setFlatStyle(true);
    listViewer = new DataListViewer(dataList);
    listViewer.setSorter(new ViewerSorter<Entry>() {
      @Override
      public int compare(Viewer viewer, Entry e1, Entry e2) {
        return e1.getName().compareTo(e2.getName());
      }

    });
    listViewer.setLabelProvider(new ModelLabelProvider() {
      @Override
      public String getText(ModelData element) {
        Entry entry = (Entry) element;
        return entry.getName();
      }

      @Override
      public String getIconStyle(ModelData element) {
        return "icon-list";
      }

    });
    listViewer.setContentProvider(new ModelContentProvider());
    listViewer.setInput(model.getEntries());
    SelectionService.get().addListener(new SourceSelectionChangedListener(listViewer));
    SelectionService.get().register(listViewer);
    filter.bind(listViewer);
    listItem.add(dataList);
  }

  protected void handleEvent(AppEvent event) {

  }

}
