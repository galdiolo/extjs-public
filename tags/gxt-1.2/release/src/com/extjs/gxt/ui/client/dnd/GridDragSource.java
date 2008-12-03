/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.dnd;

import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.user.client.Element;

/**
 * A <code>DragSource</code> implementation for Grids. Supports both single and
 * multi-selections. The selected model instances will be set as the data to be
 * dragged.
 */
public class GridDragSource extends DragSource {

  protected Grid grid;

  /**
   * Creates a new grid drag source instance.
   * 
   * @param grid the grid
   */
  public GridDragSource(Grid grid) {
    super(grid);
    this.grid = grid;
    draggable.setStartDragDistance(0);
  }

  /**
   * Returns the sources grid component.
   * 
   * @return the grid
   */
  public Grid getGrid() {
    return grid;
  }

  @Override
  protected void onDragDrop(DNDEvent e) {
    Object data = e.data;
    if (data instanceof ModelData) {
      grid.getStore().remove((ModelData) data);
    } else if (data instanceof List) {
      List<ModelData> sel = (List) data;
      for (ModelData m : sel) {
        grid.getStore().remove(m);
      }
    }
  }

  @Override
  protected void onDragStart(DNDEvent e) {
    Element r = grid.getView().findRow(e.getTarget()).cast();
    if (r == null) {
      e.doit = false;
      return;
    }

    List sel = grid.getSelectionModel().getSelectedItems();
    if (sel.size() > 0) {
      e.doit = true;
      e.data = sel;

      if (getStatusText() == null) {
        e.status.update(GXT.MESSAGES.grid_ddText(sel.size()));
      } else {
        e.status.update(Format.substitute(getStatusText(), sel.size()));
      }
    }
  }

}
