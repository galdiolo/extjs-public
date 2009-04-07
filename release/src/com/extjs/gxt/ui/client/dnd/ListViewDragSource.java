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
import com.extjs.gxt.ui.client.dnd.DND.Operation;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.ListView;
import com.google.gwt.user.client.Element;

/**
 * A <code>DragSource</code> implemenation for ListViews. Supports both single
 * and multi-selections. The selected model instances will be set as the data to
 * be dragged.
 */
public class ListViewDragSource extends DragSource {

  protected ListView listView;

  /**
   * Creates a new list view drag source instance.
   * 
   * @param listView the source list view
   */
  public ListViewDragSource(ListView listView) {
    super(listView);
    this.listView = listView;
    draggable.setStartDragDistance(0);
    setStatusText("{0} items selected");
  }

  /**
   * Returns the sources list view.
   * 
   * @return the list view
   */
  public ListView getListView() {
    return listView;
  }

  @Override
  protected void onDragDrop(DNDEvent e) {
    if (e.operation == Operation.MOVE) {
      Object data = e.data;
      if (data instanceof ModelData) {
        listView.getStore().remove((ModelData) data);
      } else if (data instanceof List) {
        List<ModelData> sel = (List) data;
        for (ModelData m : sel) {
          listView.getStore().remove(m);
        }
      }
    }
  }

  @Override
  protected void onDragStart(DNDEvent e) {
    Element r = listView.findElement(e.getTarget()).cast();
    if (r == null) {
      e.doit = false;
      return;
    }

    List sel = listView.getSelectionModel().getSelectedItems();
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
