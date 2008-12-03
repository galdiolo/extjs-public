package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.binder.DataListBinder;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToggleToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class DataListStorePage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  public DataListStorePage() {
    setLayout(new FlowLayout(10));

    final DataList list = new DataList();
    list.setSelectionMode(SelectionMode.MULTI);
    list.setFlatStyle(true);

    final ListStore<Stock> store = new ListStore<Stock>();
    store.add(TestData.getStocks());

    DataListBinder<Stock> binder = new DataListBinder<Stock>(list, store);
    binder.setDisplayProperty("name");
    binder.init();

    ContentPanel panel = new ContentPanel();
    panel.setFrame(true);
    panel.setCollapsible(true);
    panel.setAnimCollapse(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setIconStyle("icon-list");
    panel.setHeading("Store DataList");
    panel.setLayout(new FitLayout());
    panel.add(list);
    panel.setSize(230, 250);

    ToolBar toolBar = new ToolBar();

    final StoreFilter<Stock> filter = new StoreFilter<Stock>() {
      public boolean select(Store store, Stock parent, Stock item, String property) {
        if (item.getName().charAt(0) == 'A') {
          return false;
        }
        return true;
      }
    };
    store.addFilter(filter);

    TextToolItem sort = new TextToolItem("Sort");
    sort.setIconStyle("my-icon-asc");
    sort.addSelectionListener(new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        store.sort("name", SortDir.ASC);
      }
    });

    final ToggleToolItem filterItem = new ToggleToolItem("Filter A's");
    filterItem.setIconStyle("icon-filter");
    filterItem.addSelectionListener(new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        if (filterItem.isPressed()) {
          store.applyFilters("name");
        } else {
          store.clearFilters();
        }

      }
    });
    toolBar.add(sort);
    toolBar.add(new SeparatorToolItem());
    toolBar.add(filterItem);
    panel.setTopComponent(toolBar);

    add(panel);

  }

}
