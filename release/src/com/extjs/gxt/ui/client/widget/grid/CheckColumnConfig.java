/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;

public class CheckColumnConfig extends ColumnConfig implements ComponentPlugin {

  protected Grid grid;

  public CheckColumnConfig() {
    super();
    init();
  }

  public CheckColumnConfig(String id, String name, int width) {
    super(id, name, width);
    init();
  }

  public void init(Component component) {
    this.grid = (Grid) component;
    grid.addListener(Events.CellMouseDown, new Listener<GridEvent>() {
      public void handleEvent(GridEvent e) {
        onMouseDown(e);
      }
    });
  }

  protected void onMouseDown(GridEvent ge) {
    String cls = ge.getTarget().getClassName();
    if (cls != null && cls.indexOf("x-grid3-cc-" + getId()) != -1) {
      ge.stopEvent();
      int index = grid.getView().findRowIndex(ge.getTarget());
      ModelData m = grid.getStore().getAt(index);
      Record r = grid.getStore().getRecord(m);
      boolean b = (Boolean) m.get(getDataIndex());
      r.set(getDataIndex(), !b);
    }
  }

  protected void init() {
    setRenderer(new GridCellRenderer() {
      public String render(ModelData model, String property, ColumnData config, int rowIndex,
          int colIndex, ListStore store) {
        Boolean v = model.get(property);
        String on = v ? "-on" : "";
        config.css = "x-grid3-check-col-td";
        return "<div class='x-grid3-check-col" + on + " x-grid3-cc-" + getId() + "'>&#160;</div>";
      }
    });
  }

}
