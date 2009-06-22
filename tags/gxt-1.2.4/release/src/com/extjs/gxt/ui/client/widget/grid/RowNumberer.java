/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * A <code>ColumnConfig</code> that provides an automatic row numbering column.
 * 
 * <p /> By default, the row numbers will not be updated when models are added and
 * removed from the store after the grid has been rendered. The row numbers can
 * be recalculated by calling refresh(false) on the GridView.
 * 
 * <p /> RowNumberer is a ComponentPlugin and can be registered with the Grid, when
 * this is done, refresh(false) will automatically called when the store is
 * updated.
 * 
 * Code snippet:
 * 
 * <pre>
    RowNumberer r = new RowNumberer();
    
    List&lt;ColumnConfig> configs = new ArrayList&lt;ColumnConfig>();
    configs.add(r);
    
    Grid<Stock> grid = new Grid<Stock>(store, cm);
    
    // add row numberer as grid plugin to have row nubmers updated when
    // store is modified (add, remove, and filtered)
    grid.addPlugin(r);
 * </pre>
 */
public class RowNumberer extends ColumnConfig implements ComponentPlugin {
  private Grid grid;

  public RowNumberer() {
    setHeader("");
    setWidth(23);
    setSortable(false);
    setResizable(false);
    setFixed(true);
    setMenuDisabled(true);
    setDataIndex("");
    setId("numberer");

    setRenderer(new GridCellRenderer() {
      public String render(ModelData model, String property, ColumnData d, int rowIndex,
          int colIndex, ListStore store) {
        d.cellAttr = "rowspan='2'";
        return "" + (rowIndex + 1);
      }
    });
  }

  public void init(Component component) {
    grid = (Grid) component;
    grid.getStore().addStoreListener(new StoreListener() {
      public void storeAdd(StoreEvent se) {
        doRefresh();
      }

      public void storeFilter(StoreEvent se) {
        doRefresh();
      }

      public void storeRemove(StoreEvent se) {
        doRefresh();
      }

    });

  }

  private void doRefresh() {
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        grid.getView().refresh(false);
      }
    });
  }

}
