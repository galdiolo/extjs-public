/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TableEvent;
import com.extjs.gxt.ui.client.util.DelayedTask;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.AbstractContainer;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

/**
 * The table is used to display two-dimensional table of cells.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : TableEvent(table, item)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the <code>doit</code> field to <code>false</code>
 * to cancel the action.</div>
 * <ul>
 * <li>table : this</li>
 * <li>item : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : TableEvent(table, item)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code> field to <code>false</code> to
 * cancel the action.</div>
 * <ul>
 * <li>table : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : TableEvent(table, item)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>table : this</li>
 * <li>item : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : TableEvent(table this, item)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>table : this</li>
 * <li>item : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SelectionChange</b> : TableEvent(table this)<br>
 * <div>Fired after the selection has changed</div>
 * <ul>
 * <li>table : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : TableEvent(table)<br>
 * <div>Fires before the tables context menu is shown. Listeners can set the <code>doit</code> field to
 * <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>table : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellClick</b> : TableEvent(table, rowIndex, cellIndex, event)<br>
 * <div>Fired after a cell is clicked</div>
 * <ul>
 * <li>table : this</li>
 * <li>rowIndex : row index</li>
 * <li>cellIndex : cell index</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CellDoubleClick</b> : TableEvent(table, rowIndex, cellIndex, event)<br>
 * <div>Fired after a cell is double clicked</div>
 * <ul>
 * <li>table : this</li>
 * <li>rowIndex : row index</li>
 * <li>cellIndex : cell index</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ColumnClick</b> : TableEvent(table, columnIndex)<br>
 * <div>Fired after a column is clicked</div>
 * <ul>
 * <li>table : this</li>
 * <li>columnIndex : the column index</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowClick</b> : TableEvent(table, index cell index, event)<br>
 * <div>Fired after a row is clicked</div>
 * <ul>
 * <li>table : this</li>
 * <li>rowIndex : the row index</li>
 * <li>index : the cell index</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>RowDoubleClick</b> : TableEvent(table, rowIndex, cellIndex, event)<br>
 * <div>Fired after a row is double clicked</div>
 * <ul>
 * <li>table : this</li>
 * <li>rowIndex : the row index</li>
 * <li>index : the cell index</li>
 * <li>event : the dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SortChange</b> : TableEvent(table, index, size)<br>
 * <div>Fires before the table is sorted. Listeners can set the <code>doit</code> field to <code>false</code> to
 * cancel the action.</div>
 * <ul>
 * <li>widget : this</li>
 * <li>index : the column index</li>
 * <li>size : the sort direction</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>KeyPress</b> : TableEvent(table)<br>
 * <div>Fires before the table is sorted. Listeners can set the <code>doit</code> field to <code>false</code> to
 * cancel the action.</div>
 * <ul>
 * <li>table : this</li>
 * </ul>
 * </dd>
 * 
 * @see TableColumn
 * @see TableColumnModel
 */
public class Table extends AbstractContainer<TableItem> implements BaseTable {

  /**
   * True to display vertical borders on the table data (defaults to false).
   */
  public boolean verticalLines;

  /**
   * Sets the selection mode (defaults to SINGLE).
   * <p>
   * Valid values are:
   * <ul>
   * <li>SINGLE - single selection</li>
   * <li>MULTI - multi selection</li>
   * </ul>
   * </p>
   */
  public SelectionMode selectionMode = SelectionMode.SINGLE;

  /**
   * True to display a horizonatal scroll bar when needed (defaults to true).
   */
  public boolean horizontalScroll = true;

  /**
   * True to highlight the current row (defaults to true).
   */
  public boolean highlight = true;

  /**
   * True to diable the column context menu (defaults to false).
   */
  public boolean disableColumnContextMenu;

  /**
   * True to bulk render the table when first rendered (defaults to true). When true, widget are not supported in table
   * cells.
   */
  public boolean buildRender = true;

  /**
   * The table's column model.
   */
  protected TableColumnModel cm;

  /**
   * The selection model.
   */
  protected RowSelectionModel sm;

  /**
   * The table header.
   */
  protected TableHeader header;

  private Map nodes = new HashMap();
  private TableView view;
  private Size lastSize;
  private int lastLeft;
  private boolean ignoreFist = true;

  private DelayedTask scrollTask = new DelayedTask(new Listener<ComponentEvent>() {
    public void handleEvent(ComponentEvent ce) {
      header.updateSplitBars();
    }
  });

  /**
   * Creates a new single select table. A column model must be set before the table is rendered.
   */
  public Table() {
    focusable = true;
    baseStyle = "my-tbl";
    attachChildren = false;
  }

  /**
   * Creates a new table.
   * 
   * @param cm the column model
   */
  public Table(TableColumnModel cm) {
    this();
    this.cm = cm;
    cm.table = this;
  }

  /**
   * Adds a item to the table.
   * 
   * @param item the item to be added
   */
  public void add(TableItem item) {
    insert(item, getItemCount());
  }

  /**
   * Deselects the item at the given index.
   * 
   * @param index the item to deselect
   */
  public void deselect(int index) {
    getSelectionModel().deselect(index);
  }

  /**
   * Deselects all items.
   */
  public void deselectAll() {
    getSelectionModel().deselectAll();
  }

  /**
   * Returns the column at the specified index.
   * 
   * @param index the column index
   * @return the column
   */
  public TableColumn getColumn(int index) {
    return cm.getColumn(index);
  }

  /**
   * Returns the column with the given id.
   * 
   * @param id the column id
   * @return the column
   */
  public TableColumn getColumn(String id) {
    return cm.getColumn(id);
  }

  /**
   * Returns the column context menu enabed state.
   * 
   * @return <code>true</code> if enabled, <code>false</code> otherwise.
   */
  public boolean getColumnContextMenu() {
    return !disableColumnContextMenu;
  }

  /**
   * Returns the number of columns contained in the table.
   * 
   * @return the number of columns
   */
  public int getColumnCount() {
    return cm.getColumnCount();
  }

  /**
   * Returns the table's column model.
   * 
   * @return the column model
   */
  public TableColumnModel getColumnModel() {
    return cm;
  }

  public Menu getContextMenu() {
    return super.getContextMenu();
  }

  /**
   * Returns the selected item. If the list is multi-select, returns the first selected item.
   * 
   * @return the item or <code>null</code> if no selections
   */
  public TableItem getSelectedItem() {
    return getSelectionModel().getSelection().size() == 0 ? null
        : (TableItem) sm.getSelection().get(0);
  }

  /**
   * Returns an array of <code>TableItems</code> that are currently selected.
   * 
   * @return a list of selected items
   */
  public List<TableItem> getSelection() {
    return new ArrayList<TableItem>(getSelectionModel().getSelection());
  }

  /**
   * Returns the table's selection model.
   * 
   * @return the selection model
   */
  public RowSelectionModel getSelectionModel() {
    if (sm == null) {
      initSelectionModel();
    }
    return sm;
  }

  /**
   * Returns the table's header.
   * 
   * @return the table header
   */
  public TableHeader getTableHeader() {
    if (header == null) {
      header = new TableHeader(this);
    }
    return header;
  }

  /**
   * Returns the table's view.
   * 
   * @return the view
   */
  public TableView getView() {
    if (view == null) {
      view = new TableView();
    }
    return view;
  }

  /**
   * Inserts a item into the table.
   * 
   * @param item the item to insert
   * @param index the insert location
   */
  public void insert(TableItem item, int index) {
    TableEvent te = new TableEvent(this);
    te.item = item;
    te.index = index;
    if (fireEvent(Events.BeforeAdd, te)) {
      items.add(index, item);
      register(item);
      if (rendered) {
        view.renderItem(item, index);
      }
      fireEvent(Events.Add, te);
    }
  }

  public void onComponentEvent(ComponentEvent ce) {
    TableItem item = findItem(ce.getTarget());
    if (item != null) {
      item.onComponentEvent(ce);
    }
    TableEvent te = new TableEvent(this);
    te.event = ce.event;
    switch (ce.type) {
      case Event.ONKEYPRESS:
        fireEvent(Events.KeyPress, te);
        break;
      case Event.ONKEYDOWN:
        fireEvent(Events.KeyDown, te);
    }

  }

  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    int type = DOM.eventGetType(event);

    if (type == Event.ONSCROLL) {
      int left = view.getScrollEl().getScrollLeft();
      if (left == lastLeft) {
        return;
      }
      lastLeft = left;
      header.el.setLeft(-left);
      scrollTask.delay(400);
    }
  }

  /**
   * Recalculates the ui based on the table's current size.
   */
  public void recalculate() {
    onResize(getOffsetWidth(), getOffsetHeight());
  }

  /**
   * Removes the item from the table.
   * 
   * @param item the item to be removed
   */
  public boolean remove(TableItem item) {
    TableEvent te = new TableEvent(this);
    te.item = item;
    if (fireEvent(Events.BeforeRemove, te)) {
      getSelectionModel().remove(item);
      items.remove(item);
      unregister(item);
      if (rendered) {
        view.removeItem(item);
      }
      fireEvent(Events.Remove, te);
      return true;
    }
    return false;
  }

  /**
   * Removes all the item's.
   */
  public void removeAll() {
    int count = getItemCount();
    for (int i = 0; i < count; i++) {
      remove(getItem(0));
    }
  }

  /**
   * Scrolls the item into view.
   * 
   * @param item the item
   */
  public void scrollIntoView(TableItem item) {
    item.el.scrollIntoView(view.getScrollEl().dom, false);
  }

  /**
   * Selects the item at the given index.
   * 
   * @param index the row to select
   */
  public void select(int index) {
    getSelectionModel().select(index);
  }

  /**
   * Selects the the item.
   * 
   * @param item the item to be selected
   */
  public void select(TableItem item) {
    select(indexOf(item));
  }

  public void setColumnModel(TableColumnModel cm) {
    this.cm = cm;
    cm.table = this;
  }

  @Override
  public void setContextMenu(Menu menu) {
    super.setContextMenu(menu);
  }

  /**
   * Sets the table's selection model.
   * 
   * @param sm the selection model
   */
  public void setSelectionModel(RowSelectionModel sm) {
    if (sm != null) {
      this.sm.unbind(this);
      this.sm = null;
    }
    this.sm = sm;
    this.sm.init(this);
  }

  /**
   * Sets the table's header. Should only be called when providing a custom table header. Has no effect if called after
   * the table has been rendered.
   * 
   * @param header the table header
   */
  public void setTableHeader(TableHeader header) {
    if (!isRendered()) {
      this.header = header;
    }
  }

  /**
   * Sets the table's view. Provides a way to provide specialized views. table views.
   * 
   * @param view the view
   */
  public void setView(TableView view) {
    this.view = view;
  }

  /**
   * Sorts the table using the specified column index.
   * 
   * @param index the column index
   * @param sortDir the direction to sort (NONE, ASC, DESC)
   */
  public void sort(int index, SortDir sortDir) {
    if (rendered) {
      TableEvent te = new TableEvent(this);
      te.columnIndex = index;
      te.sortDir = sortDir;
      if (fireEvent(Events.SortChange, te)) {
        getTableHeader().sort(index, sortDir);
        getView().sort(index, sortDir);
      }
    }
  }

  protected void doAttachChildren() {
    WidgetHelper.doAttach(header);
    if (!buildRender) {
      int count = getItemCount();
      for (int i = 0; i < count; i++) {
        TableItem item = getItem(i);
        if (item.hasWidgets) {
          int ct = item.getValues().length;
          for (int j = 0; j < ct; j++) {
            Object obj = item.getValue(j);
            if (obj != null && obj instanceof Widget) {
              WidgetHelper.doAttach((Widget) item.getValue(j));
            }
          }
        }
      }
    }
  }

  protected void doDetachChildren() {
    WidgetHelper.doDetach(header);
    if (!buildRender) {
      int count = getItemCount();
      for (int i = 0; i < count; i++) {
        TableItem item = getItem(i);
        if (item.hasWidgets) {
          int ct = item.getValues().length;
          for (int j = 0; j < ct; j++) {
            Object obj = item.getValue(j);
            if (obj != null && obj instanceof Widget) {
              WidgetHelper.doDetach((Widget) item.getValue(j));
            }
          }
        }
      }
    }
  }

  protected String getRenderedValue(int column, Object value) {
    TableColumn col = cm.getColumn(column);
    CellRenderer r = col.getRenderer();
    if (r != null) {
      return r.render(col.getId(), value);
    } else {
      if (value != null) {
        return value.toString();
      }
      return null;
    }
  }

  protected void initSelectionModel() {
    if (selectionMode == SelectionMode.MULTI) {
      sm = new RowSelectionModel(SelectionMode.MULTI);
    } else {
      sm = new RowSelectionModel(SelectionMode.SINGLE);
    }
    sm.init(this);
  }

  @Override
  protected void onRender(Element target, int index) {
    setElement(DOM.createDiv(), target, index);

    if (sm == null) {
      initSelectionModel();
    }

    header = getTableHeader();
    header.render(el.dom);
    header.init(this);

    view = getView();
    view.init(this);
    view.render();
    view.renderItems();
  }

  protected void onResize(int width, int height) {
    if (ignoreFist) {
      ignoreFist = false;
      header.resizeColumns(false, false);
      view.resize();
      return;
    }
    int h = width;
    int w = height;
    if (lastSize != null) {
      if (lastSize.width == w && lastSize.height == h) {
        return;
      }
    }
    lastSize = new Size(w, h);
    header.resizeColumns(false, true);
  }

  protected void onRightClick(ComponentEvent ce) {
    TableItem item = findItem(ce.getTarget());
    if (item != null) {
      item.onClick(ce);
    }
    super.onRightClick(ce);
  }

  protected void onShowContextMenu(int x, int y) {
    super.onShowContextMenu(x, y);
    getView().clearHoverStyles();
  }

  private void register(TableItem item) {
    nodes.put(item.getId(), item);
  }

  private void unregister(TableItem item) {
    nodes.remove(item.getId());
  }

}
