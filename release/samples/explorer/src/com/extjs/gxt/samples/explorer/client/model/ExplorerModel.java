/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.model;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.samples.explorer.client.pages.AccordianLayoutPage;
import com.extjs.gxt.samples.explorer.client.pages.AsyncTreeViewerPage;
import com.extjs.gxt.samples.explorer.client.pages.BorderLayoutPage;
import com.extjs.gxt.samples.explorer.client.pages.ButtonPage;
import com.extjs.gxt.samples.explorer.client.pages.CenterLayoutPage;
import com.extjs.gxt.samples.explorer.client.pages.CheckBoxTreePage;
import com.extjs.gxt.samples.explorer.client.pages.ContentPanelPage;
import com.extjs.gxt.samples.explorer.client.pages.DataListPage;
import com.extjs.gxt.samples.explorer.client.pages.DataListViewerPage;
import com.extjs.gxt.samples.explorer.client.pages.DatePickerPage;
import com.extjs.gxt.samples.explorer.client.pages.DialogPage;
import com.extjs.gxt.samples.explorer.client.pages.DraggablePage;
import com.extjs.gxt.samples.explorer.client.pages.FormPanelPage;
import com.extjs.gxt.samples.explorer.client.pages.FxPage;
import com.extjs.gxt.samples.explorer.client.pages.MessageBoxPage;
import com.extjs.gxt.samples.explorer.client.pages.OverviewPage;
import com.extjs.gxt.samples.explorer.client.pages.Page;
import com.extjs.gxt.samples.explorer.client.pages.PagingTableViewerPage;
import com.extjs.gxt.samples.explorer.client.pages.PercentageTablePage;
import com.extjs.gxt.samples.explorer.client.pages.ResizablePage;
import com.extjs.gxt.samples.explorer.client.pages.RowLayoutPage;
import com.extjs.gxt.samples.explorer.client.pages.SplitButtonPage;
import com.extjs.gxt.samples.explorer.client.pages.TabPanelPage;
import com.extjs.gxt.samples.explorer.client.pages.TablePage;
import com.extjs.gxt.samples.explorer.client.pages.TableViewerFilterPage;
import com.extjs.gxt.samples.explorer.client.pages.TableViewerPage;
import com.extjs.gxt.samples.explorer.client.pages.ToolBarPage;
import com.extjs.gxt.samples.explorer.client.pages.ToolTipPage;
import com.extjs.gxt.samples.explorer.client.pages.TreeContextMenuPage;
import com.extjs.gxt.samples.explorer.client.pages.TreePage;
import com.extjs.gxt.samples.explorer.client.pages.TreeTableViewerPage;
import com.extjs.gxt.samples.explorer.client.pages.TreeViewerFilterPage;
import com.extjs.gxt.samples.explorer.client.pages.TreeViewerPage;
import com.extjs.gxt.samples.explorer.client.pages.WindowPage;
import com.extjs.gxt.samples.explorer.client.pages.XmlTableViewerPage;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.TreeModel;

public class ExplorerModel extends BaseTreeModel {

  private List<Entry> entries = new ArrayList<Entry>();

  public ExplorerModel() {
    set("overview", new Entry("Overview", new Page(new OverviewPage(), false)));

    Category viewers = new Category("Viewers");

    Category viewTables = new Category("Table Viewers");
    viewTables.addEntry("TableViewer", new TableViewerPage());
    viewTables.addEntry("Filter TableViewer", new TableViewerFilterPage());
    viewTables.addEntry("Paging TableViewer", new PagingTableViewerPage());
    viewTables.addEntry("XML TableViewer", new XmlTableViewerPage());
    viewers.add(viewTables);

    Category treeViewer = new Category("Tree Viewers");
    treeViewer.addEntry("Async TreeViewer", new AsyncTreeViewerPage());
    treeViewer.addEntry("TreeViewer", new TreeViewerPage());
    treeViewer.addEntry("Filter TreeViewer", new TreeViewerFilterPage());
    treeViewer.addEntry("TreeTableViewer", new TreeTableViewerPage());
    viewers.add(treeViewer);

    viewers.addEntry("DataListViewer", new DataListViewerPage());

    add(viewers);

    Category dataWidgets = new Category("Data Widgets");

    Category trees = new Category("Trees");
    trees.addEntry("Basic Tree", new TreePage());
    trees.addEntry("Checkbox Tree", new CheckBoxTreePage());
    trees.addEntry("Context Menu Tree", new TreeContextMenuPage());
    dataWidgets.add(trees);

    Category tables = new Category("Tables");
    tables.addEntry("Basic Table", new TablePage());
    tables.addEntry("% Columns", new PercentageTablePage());
    dataWidgets.add(tables);

    Category lists = new Category("Lists");
    lists.addEntry("DataList", new DataListPage());
    dataWidgets.add(lists);

    add(dataWidgets);

    Category panels = new Category("Panels");
    panels.addEntry("ContentPanel", new ContentPanelPage());
    panels.addEntry("FormPanel", new FormPanelPage());
    panels.addEntry("TabPanel", new TabPanelPage());
    add(panels);

    Category windows = new Category("Windows");
    windows.addEntry("Window", new WindowPage());
    windows.addEntry("Dialog", new DialogPage());
    windows.addEntry("MessageBox", new MessageBoxPage());
    add(windows);
    
    Category other = new Category("Misc Widgets");

    Category buttons = new Category("Buttons");
    buttons.addEntry("Basic Buttons", new ButtonPage());
    buttons.addEntry("Split Button", new SplitButtonPage());
    other.add(buttons);

    Category tooltips = new Category("ToolTip");
    tooltips.addEntry("ToolTips", new ToolTipPage());
    other.add(tooltips);

    Category toolbars = new Category("ToolBar & Menu");
    toolbars.addEntry("ToolBar - Menu", new ToolBarPage());
    other.add(toolbars);
    
    other.addEntry("Date Picker", new DatePickerPage());
    add(other);

    Category layouts = new Category("Layouts");
    layouts.addEntry("AccordianLayout", new AccordianLayoutPage());
    layouts.addEntry("BorderLayout", new BorderLayoutPage());
    layouts.addEntry("CenterLayout", new CenterLayoutPage());
    layouts.addEntry("RowLayout", new RowLayoutPage());
    add(layouts);

    Category fx = new Category("Fx");
    fx.addEntry("Draggable", new DraggablePage());
    fx.addEntry("Resizable", new ResizablePage());
    fx.addEntry("Fx", new FxPage());
    add(fx);

    loadEntries(this);
  }

  public Entry findEntry(String name) {
    if (get(name) != null) {
      return (Entry) get(name);
    }
    for (Entry entry : getEntries()) {
      if (name.equals(entry.getId())) {
        return entry;
      }
    }
    return null;
  }

  public List<Entry> getEntries() {
    return entries;

  }

  private void loadEntries(TreeModel model) {
    for (TreeModel child : model.getChildren()) {
      if (child instanceof Entry) {
        entries.add((Entry) child);
      } else if (child instanceof Category) {
        loadEntries(child);
      }
    }
  }

}
