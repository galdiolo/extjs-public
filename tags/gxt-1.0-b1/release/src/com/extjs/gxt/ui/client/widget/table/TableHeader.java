/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TableEvent;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.SplitBar;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.TextMenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public class TableHeader extends BoxComponent {

  protected static String html;

  static {
    StringBuffer sb = new StringBuffer();
    sb.append("<div class=my-tbl-header style='position: relative'>");
    sb.append("<table border=0 cellpadding=0 cellspacing=0 style='position:relative'><tbody><tr class=my-tbl-header-row>");
    sb.append("</tr></table></div>");
    html = sb.toString();
  }

  protected List<TableColumnUI> columns;
  protected BaseTable table;
  protected TableColumnModel columnModel;
  protected TableColumnUI sortColumn, endColumn, hoverColumn;
  protected Element headerRow;

  private DelayedTask task = new DelayedTask(new Listener<ComponentEvent>() {
    public void handleEvent(ComponentEvent ce) {
      updateSplitBars();
    }
  });

  public TableHeader() {

  }

  public TableHeader(BaseTable table) {
    this.table = table;
    this.columnModel = this.table.getColumnModel();
  }

  public void sort(int index, SortDir dir) {
    TableColumn column = table.getColumn(index);
    column.sortDir = dir;
    onSortChange(column, dir);
  }

  public void addColumn(TableColumnUI ui) {
    Element td = DOM.createTD();
    fly(td).setStyleName("my-tbl-col");

    ui.render(td);
    DOM.appendChild(headerRow, td);
    columns.add(ui);
  }

  public SplitBar createSplitBar(LayoutRegion direction, TableColumnUI column) {
    return new SplitBar(direction, column, (BoxComponent) table);
  }

  public TableColumnUI createTableColumnUI(int index) {
    return new TableColumnUI(table, index);
  }

  protected void doAttachChildren() {
    int count = columns.size() - 1;
    for (int i = 0; i < count; i++) {
      WidgetHelper.doAttach(getColumnUI(i));
    }
  }

  protected void doDetachChildren() {
    int count = columns.size() - 1;
    for (int i = 0; i < count; i++) {
      WidgetHelper.doDetach(getColumnUI(i));
    }
  }

  public TableColumnUI getColumnUI(int index) {
    return columns.get(index);
  }

  public void init(BaseTable table) {
    this.table = table;
    this.columnModel = this.table.getColumnModel();
  }

  public void onColumnClick(TableColumnUI columnUI) {
    TableEvent ce = new TableEvent((Table) table);
    ce.columnIndex = columnUI.index;

    if (!((Component) table).fireEvent(Events.ColumnClick, ce)) {
      return;
    }

    if (columnUI.column.isSortable()) {

      SortDir sortDir = SortDir.toggle(columnUI.column.sortDir);
      table.sort(ce.columnIndex, sortDir);
    }
  }

  protected void onColumnMouseMove(TableColumnUI column, BaseEvent be) {

  }

  @Override
  protected void onRender(Element target, int index) {
    setElement(XDOM.create(html), target, index);
    headerRow = el.selectNode(".my-tbl-header-row").dom;

    columns = new ArrayList<TableColumnUI>();
    int cols = columnModel.getColumnCount();
    for (int i = 0; i < cols; i++) {
      TableColumnUI columnUI = createTableColumnUI(i);
      addColumn(columnUI);
    }
    endColumn = createTableColumnUI(cols);
    endColumn.end = true;
    addColumn(endColumn);

    disableContextMenu(true);
  }

  protected void onRightClick(final TableColumn column, Event event) {
    DOM.eventCancelBubble(event, true);
    DOM.eventPreventDefault(event);

    if (!table.getColumnContextMenu()) {
      return;
    }

    final int x = DOM.eventGetClientX(event);
    final int y = DOM.eventGetClientY(event);
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        onShowContextMenu(column).showAt(x, y);
      }
    });
  }

  protected Menu onShowContextMenu(final TableColumn column) {
    final Menu menu = new Menu();

    if (column.isSortable()) {
      TextMenuItem item = new TextMenuItem();
      item.setText(GXT.MESSAGES.gridView_sortAscText());
      item.setIconStyle("my-icon-asc");
      item.addSelectionListener(new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          table.sort(column.index, SortDir.ASC);
        }

      });
      menu.add(item);

      item = new TextMenuItem();
      item.setText(GXT.MESSAGES.gridView_sortDescText());
      item.setIconStyle("my-icon-desc");
      item.addSelectionListener(new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          table.sort(column.index, SortDir.DESC);
        }
      });
      menu.add(item);
    }

    TextMenuItem columns = new TextMenuItem();
    columns.setText(GXT.MESSAGES.gridView_columnsText());
    columns.setIconStyle("icon-columns");

    final Menu columnMenu = new Menu();

    int cols = columnModel.getColumnCount();
    for (int i = 0; i < cols; i++) {
      final TableColumn def = columnModel.getColumn(i);
      final CheckMenuItem check = new CheckMenuItem();
      check.setHideOnClick(false);
      check.setText(def.getText());
      check.checked = !def.isHidden();
      check.addSelectionListener(new SelectionListener() {

        public void componentSelected(ComponentEvent ce) {
          def.setHidden(!check.isChecked());
          showColumn(def.index, !def.isHidden());

          if (columnModel.getVariableColumnCount() > 0) {
            resizeColumns(false, true);
          }

          if (columnModel.getVisibleColumnCount() == 1) {
            for (MenuItem item : columnMenu.getItems()) {
              CheckMenuItem check = (CheckMenuItem) item;
              if (check.isChecked()) {
                item.disable();
              }
            }
          } else if (columnModel.getVisibleColumnCount() == 2) {
            for (MenuItem item : columnMenu.getItems()) {
              item.enable();
            }
          }
        }

      });
      columnMenu.add(check);

      if (columnModel.getVisibleColumnCount() == 1) {
        for (MenuItem item : columnMenu.getItems()) {
          CheckMenuItem ci = (CheckMenuItem) item;
          if (ci.isChecked()) {
            ci.disable();
          }
        }
      }
    }

    columns.setSubMenu(columnMenu);
    menu.add(columns);

    return menu;
  }

  public int getSortColumn() {
    if (sortColumn != null) {
      return sortColumn.index;
    }
    return Style.DEFAULT;
  }

  protected void onSortChange(TableColumn column, SortDir sortDir) {
    column.sortDir = sortDir;
    if (sortColumn != null) {
      getColumnUI(sortColumn.index).onSortChange(SortDir.NONE);
    }
    sortColumn = getColumnUI(column.index);
    sortColumn.onSortChange(sortDir);
  }

  protected void resizeColumn(int index, boolean resizeBody) {
    TableColumn column = columnModel.getColumn(index);
    TableColumnUI ui = getColumnUI(index);
    if (column.isHidden()) {
      ui.setVisible(false);
      return;
    } else {
      ui.setVisible(true);
    }
    int w = columnModel.getWidthInPixels(column.index);

    if (w != ui.lastWidth) {
      Element td = DOM.getParent(ui.getElement());
      w -= fly(td).getBorderWidth("lr");

      fly(td).setWidth(w);

      SplitBar splitBar = ui.splitBar;
      if (splitBar != null) {
        if (column.isResizable()) {
          splitBar.setVisible(true);
        }
        splitBar.minSize = column.getMinWidth();
        splitBar.maxSize = column.getMaxWidth();
      }
      task.delay(400);
      if (resizeBody) {
        doTableComponentResizeCells(index);
      }

    }

    ui.lastWidth = w;

    if (resizeBody) {
      doTableComponentResize();
    }
  }

  protected void resizeColumns(boolean fireEvent, boolean resizeBody) {
    int tw = Math.max(columnModel.getTotalWidth(), ((Component) table).getOffsetWidth()) + 100;
    setWidth(tw);
    el.firstChild().setWidth(tw);
    endColumn.el.setWidth("100%");

    int cols = columnModel.getColumnCount();
    for (int i = 0; i < cols; i++) {
      resizeColumn(i, resizeBody);
    }

    task.delay(100);

    if (resizeBody) {
      doTableComponentResize();
    }

  }

  protected void showColumn(int index, boolean show) {
    TableColumnUI ui = getColumnUI(index);
    ui.el.getParent().setVisible(false);
    doTableComponentShowColumn(index, show);
    updateSplitBars();
    doTableComponentResize();
  }

  protected void updateSplitBars() {
    int count = columns.size() - 1;
    for (int i = 0; i < count; i++) {
      TableColumnUI ui = getColumnUI(i);
      if (ui.splitBar != null) {
        ui.splitBar.sync();
      }
    }
  }

  protected void doTableComponentResize() {
    if (table instanceof Table) {
      ((Table) table).getView().resize();
    }
  }

  protected void doTableComponentResizeCells(int columnIndex) {
    if (table instanceof Table) {
      ((Table) table).getView().resizeCells(columnIndex);
    }
  }

  protected void doTableComponentShowColumn(int index, boolean show) {
    if (table instanceof Table) {
      ((Table) table).getView().showColumn(index, show);
    }
  }

}
