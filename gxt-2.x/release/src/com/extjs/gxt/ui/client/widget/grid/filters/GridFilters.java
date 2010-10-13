/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.data.FilterConfig;
import com.extjs.gxt.ui.client.data.FilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FilterEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.ColumnHeader.Head;
import com.extjs.gxt.ui.client.widget.grid.filters.Filter.FilterMessages;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters.GridFiltersMessages;
import com.extjs.gxt.ui.client.widget.grid.filters.NumericFilter.NumericFilterMessages;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;

/**
 * <p>
 * GridFilter is a plugin for grids that allow for a slightly more robust
 * representation of filtering than what is provided by the default store.
 * </p>
 * <p>
 * Filtering is adjusted by the user using the grid's column header menu (this
 * menu can be disabled through configuration). Through this menu users can
 * configure, enable, and disable filters for each column.
 * </p>
 */
@SuppressWarnings("unused")
public class GridFilters implements ComponentPlugin {
  public static class GridFiltersMessages {
    private String filterText = GXT.MESSAGES.gridFilters_filterText();

    /**
     * @return the filterText
     */
    public String getFilterText() {
      return filterText;
    }

    /**
     * @param filterText the filterText to set
     */
    public void setFilterText(String filterText) {
      this.filterText = filterText;
    }
  }

  protected Grid<ModelData> grid;

  private boolean autoReload = true;
  private CheckMenuItem checkFilterItem;
  private DelayedTask deferredUpdate = new DelayedTask(new Listener<BaseEvent>() {
    public void handleEvent(BaseEvent be) {
      reload();
    }
  });
  private Listener<FilterEvent> filterListener = new Listener<FilterEvent>() {
    public void handleEvent(FilterEvent be) {
      onStateChange(be.getFilter());
    }
  };
  private Menu filterMenu;
  private Map<String, Filter> filters;
  private String filterStyle = "x-filtered-column";
  private Listener<LoadEvent> loadListener = new Listener<LoadEvent>() {
    public void handleEvent(LoadEvent le) {
      EventType type = le.getType();
      if (type == Loader.BeforeLoad) {
        onBeforeLoad(le);
      } else if (type == Loader.Load) {
        onLoad(le);
      }
    }
  };
  private boolean local = false;
  private GridFiltersMessages msgs;
  private SeparatorMenuItem seperatorItem;

  private ListStore<ModelData> store;

  private int updateBuffer = 500;

  public GridFilters() {
    filters = new HashMap<String, Filter>();
    setMessages(new GridFiltersMessages());
  }

  /**
   * Adds the given filter.
   * 
   * @param filter the filter to be added
   */
  public void addFilter(Filter filter) {
    filters.put(filter.getDataIndex(), filter);
    filter.addListener(Events.Update, filterListener);
    filter.addListener(Events.Activate, filterListener);
    filter.addListener(Events.Deactivate, filterListener);
  }

  /**
   * Function to take the active filters data and build it into a query.
   * 
   * @param filters the active filters
   * @return the list of filter configs
   */
  public List<FilterConfig> buildQuery(List<Filter> filters) {
    List<FilterConfig> configs = new ArrayList<FilterConfig>();
    int len = filters.size();

    for (Filter f : filters) {
      List<FilterConfig> temp = f.getSerialArgs();
      for (FilterConfig tempConfig : temp) {
        tempConfig.setField(f.getDataIndex());
        configs.add(tempConfig);
      }
    }
    return configs;
  }

  /**
   * Removes filter related query parameters from the provided object.
   * 
   * @param config the load config
   */
  public void cleanParams(FilterPagingLoadConfig config) {
    config.setFilterConfigs(new ArrayList<FilterConfig>());
  }

  /**
   * Turns all filters off. This does not clear the configuration information
   * (see {@link #removeAll}).
   */
  public void clearFilters() {
    for (Filter f : filters.values()) {
      f.setActive(false, false);
    }
  }

  public Filter getFilter(String dataIndex) {
    return filters.get(dataIndex);
  }

  /**
   * Returns a list of the currently active filters.
   * 
   * @return the list of active filters
   */
  public List<Filter> getFilterData() {
    List<Filter> configs = new ArrayList<Filter>();
    for (Filter f : filters.values()) {
      if (f.isActive()) {
        configs.add(f);
      }
    }
    return configs;
  }

  public GridFiltersMessages getMessages() {
    return msgs;
  }

  /**
   * Returns the number of milliseconds to defer store updates.
   * 
   * @return the update buffer
   */
  public int getUpdateBuffer() {
    return updateBuffer;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void init(Component component) {
    this.grid = (Grid) component;

    grid.addListener(Events.HeaderContextMenu, new Listener<GridEvent>() {
      public void handleEvent(GridEvent be) {
        onContextMenu(be);
      }
    });

    bindStore(grid.getStore(), true);
  }

  /**
   * Returns true if auto load is enabled.
   * 
   * @return the auto load state
   */
  public boolean isAutoReload() {
    return autoReload;
  }

  /**
   * Returns true if local filtering is enabled.
   * 
   * @return true for local
   */
  public boolean isLocal() {
    return local;
  }

  /**
   * Removes all filters.
   */
  public void removeAll() {
    filters.clear();
  }

  /**
   * Removes the given filter.
   * 
   * @param filter the filter to be removed
   */
  public void removeFilter(Filter filter) {
    filters.remove(filter.getDataIndex());
  }

  /**
   * Tree to reload the datasource when a filter change happens (defaults to
   * true). Set this to false to prevent the datastore from being reloaded if
   * there are changes to the filters.
   * 
   * @param autoLoad true to enable auto reload
   */
  public void setAutoReload(boolean autoLoad) {
    this.autoReload = autoLoad;
  }

  /**
   * True to use Store filter functions (local filtering) instead of the default
   * server side filtering (defaults to false).
   * 
   * @param local true for local
   */
  public void setLocal(boolean local) {
    this.local = local;
  }

  public void setMessages(GridFiltersMessages messages) {
    msgs = messages;
    if (checkFilterItem != null) {
      checkFilterItem.setText(getMessages().getFilterText());
    }
  }

  /**
   * Number of milliseconds to defer store updates since the last filter change
   * (defaults to 500).
   * 
   * @param updateBuffer the buffer in milliseconds
   */
  public void setUpdateBuffer(int updateBuffer) {
    this.updateBuffer = updateBuffer;
  }

  public void updateColumnHeadings() {
    int cols = grid.getColumnModel().getColumnCount();
    for (int i = 0; i < cols; i++) {
      ColumnConfig config = grid.getColumnModel().getColumn(i);
      if (!config.isHidden()) {
        Head h = grid.getView().getHeader().getHead(i);
        if (h != null && h.isRendered()) {
          Filter f = getFilter(config.getDataIndex());
          if (f != null) {
            h.el().setStyleName(filterStyle, f.isActive());
          }
        }
      }
    }
  }

  protected void bindStore(ListStore<ModelData> store, boolean initial) {
    if (!initial && this.store != null) {
      if (local) {
        this.store.removeListener(Loader.Load, loadListener);
      } else {
        this.store.getLoader().removeListener(Loader.BeforeLoad, loadListener);
      }
    }
    if (store != null) {
      if (local) {
        store.addListener(Loader.Load, loadListener);
      } else {
        store.getLoader().addListener(Loader.BeforeLoad, loadListener);
      }
    }
    this.store = store;
  }

  protected Filter getMenuFilter(MenuEvent me) {
    MenuItem item = (MenuItem) me.getItem();
    ColumnConfig config = grid.getColumnModel().getColumn((Integer) item.getData("index"));
    return getFilter(config.getDataIndex());
  }

  protected StoreFilter<ModelData> getModelFilter() {
    final List<Filter> f = new ArrayList<Filter>();
    for (Filter filter : filters.values()) {
      if (filter.isActive()) {
        f.add(filter);
      }
    }
    StoreFilter<ModelData> storeFilter = new StoreFilter<ModelData>() {
      public boolean select(Store<ModelData> store, ModelData parent, ModelData item, String property) {
        for (int i = 0; i < f.size(); i++) {
          Filter test = f.get(i);
          if (!test.validateModel(item)) {
            return false;
          }
        }
        return true;
      }
    };
    return storeFilter;
  }

  protected void onBeforeCheck(MenuEvent me) {
    Filter f = getMenuFilter(me);
    me.setCancelled(me.isChecked() && !f.isActivatable());
  }

  protected void onBeforeLoad(LoadEvent le) {
    FilterPagingLoadConfig config = le.getConfig();
    cleanParams(config);
    List<FilterConfig> filterConfigs = buildQuery(getFilterData());
    config.setFilterConfigs(filterConfigs);
  }

  protected void onCheckChange(MenuEvent me) {
    me.stopEvent();
    CheckMenuItem item = (CheckMenuItem) me.getItem();
    getMenuFilter(me).setActive(me.isChecked(), false);
  }

  @SuppressWarnings("rawtypes")
  protected void onContextMenu(GridEvent be) {
    int column = be.getColIndex();

    if (seperatorItem == null) {
      seperatorItem = new SeparatorMenuItem();
    }
    seperatorItem.removeFromParent();

    if (checkFilterItem == null) {
      checkFilterItem = new CheckMenuItem(getMessages().getFilterText());
      checkFilterItem.addListener(Events.CheckChange, new Listener<MenuEvent>() {
        public void handleEvent(MenuEvent me) {
          onCheckChange(me);
        }
      });
      checkFilterItem.addListener(Events.BeforeCheckChange, new Listener<MenuEvent>() {
        public void handleEvent(MenuEvent me) {
          onBeforeCheck(me);
        }
      });
    }
    checkFilterItem.removeFromParent();
    checkFilterItem.setData("index", column);

    Filter f = getFilter(grid.getColumnModel().getColumn(column).getDataIndex());
    if (f != null) {
      checkFilterItem.show();

      filterMenu = f.getMenu();
      checkFilterItem.setChecked(f.isActive());
      checkFilterItem.setSubMenu(filterMenu);
    } else {
      checkFilterItem.hide();
      return;
    }

    Menu menu = be.getMenu();
    menu.add(seperatorItem);
    menu.add(checkFilterItem);

    updateColumnHeadings();
  }

  protected void onLoad(LoadEvent le) {
    store.filter("");
  }

  protected void onReconfigure() {
    List<Filter> temp = new ArrayList<Filter>();
    for (Filter f : filters.values()) {
      temp.add(f);
    }

    bindStore(grid.getStore(), false);
    store.clearFilters();
    removeAll();

    for (Filter f : temp) {
      addFilter(f);
    }
    updateColumnHeadings();
  }

  protected void onStateChange(Filter filter) {
    checkFilterItem.setChecked(filter.isActive());
    if ((autoReload || local)) {
      deferredUpdate.delay(updateBuffer);
    }
    updateColumnHeadings();
  }

  @SuppressWarnings("rawtypes")
  protected void reload() {
    if (local) {
      store.clearFilters();
      if (store.getFilters() != null) {
        store.getFilters().clear();
      }
      store.addFilter(getModelFilter());
      store.filter("");
    } else {
      deferredUpdate.cancel();
      Loader l = store.getLoader();
      l.load();
    }
  }

  @SuppressWarnings("rawtypes")
  protected void saveState(Grid grid, Map<String, Object> state) {

  }

}
