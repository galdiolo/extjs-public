/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import java.util.Arrays;
import java.util.List;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.google.gwt.user.client.Event;

/**
 * A grid selection model and component plugin. To use, add the column config to
 * the column model using {@link #getColumn()} and add this object to the grids
 * plugin.
 * 
 * @param <M> the model data type
 */
public class CheckBoxSelectionModel<M extends ModelData> extends GridSelectionModel<M> implements ComponentPlugin {

  protected ColumnConfig config;

  public CheckBoxSelectionModel() {
    super();
    config = newColumnConfig();
    config.setId("checker");
    config.setWidth(20);
    config.setSortable(false);
    config.setResizable(false);
    config.setFixed(true);
    config.setMenuDisabled(true);
    config.setDataIndex("");
    config.setRenderer(new GridCellRenderer<M>() {
      public String render(M model, String property, ColumnData config, int rowIndex, int colIndex, ListStore<M> store,
          Grid<M> grid) {
        config.cellAttr = "rowspan='2'";
        return "<div class='x-grid3-row-checker'>&#160;</div>";
      }
    });
  }

  /**
   * Returns the column config.
   * 
   * @return the column config
   */
  public ColumnConfig getColumn() {
    return config;
  }

  @SuppressWarnings("unchecked")
  public void init(Component component) {
    this.grid = (Grid) component;
    grid.addListener(Events.HeaderClick, new Listener<GridEvent>() {
      public void handleEvent(GridEvent e) {
        onHeaderClick(e);
      }
    });
    this.store = grid.getStore();
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void handleMouseDown(GridEvent<M> e) {
    if (e.getEvent().getButton() == Event.BUTTON_LEFT && e.getTarget().getClassName().equals("x-grid3-row-checker")) {
      M m = listStore.getAt(e.getRowIndex());
      if (m != null) {
        if (isSelected(m)) {
          doDeselect(Arrays.asList(m), false);
        } else {
          doSelect(Arrays.asList(m), true, false);
        }
      }
    } else {
      super.handleMouseDown(e);
    }
  }

  protected ColumnConfig newColumnConfig() {
    return new ColumnConfig();
  }

  protected void onHeaderClick(GridEvent<M> e) {
    ColumnConfig c = grid.getColumnModel().getColumn(e.getColIndex());
    if (c == config) {
      El hd = e.getTargetEl().getParent();
      boolean isChecked = hd.hasStyleName("x-grid3-hd-checker-on");
      if (isChecked) {
        setChecked(false);
        deselectAll();
      } else {
        setChecked(true);
        selectAll();
      }
    }
  }

  @Override
  protected void onAdd(List<? extends M> models) {
    super.onAdd(models);
    setChecked(getSelection().size() == grid.getStore().getCount());
  }

  @Override
  protected void onClear(StoreEvent<M> se) {
    super.onClear(se);
    setChecked(false);
  }

  @Override
  protected void onRemove(M model) {
    super.onRemove(model);
    setChecked(getSelection().size() == grid.getStore().getCount());
  }

  @Override
  protected void onSelectChange(M model, boolean select) {
    super.onSelectChange(model, select);
    setChecked(getSelection().size() == grid.getStore().getCount());
  }

  private void setChecked(boolean checked) {
    El hd = grid.getView().innerHd.child("div.x-grid3-hd-checker");
    hd.getParent().setStyleName("x-grid3-hd-checker-on", checked);
  }

}
