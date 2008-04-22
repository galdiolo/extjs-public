/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ext GWT - derived implementation
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.DataLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TableEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableItem;

/**
 * A concrete viewer based on a <code>Table</code> widget.
 * 
 * @see Table
 */
public class TableViewer<T extends TableItem> extends StructuredViewer {

  private ViewerCell viewerCell = new ViewerCell();
  private Table table;
  private Loader loader;
  private Listener<LoadEvent> loadListener;
  private CellLabelProvider defaultCellLabelProvider;

  /**
   * Creates a new table viewer.
   * 
   * @param table the table
   */
  public TableViewer(final Table table) {
    this.table = table;
    hookWidget(table);
  }

  @Override
  public void add(Object element) {
    renderItem(element, table.getItemCount());
  }

  @Override
  public TableItem findItem(Object element) {
    int size = table.getItemCount();
    for (int i = 0; i < size; i++) {
      TableItem item = table.getItem(i);
      Object itemElement = getElementFromItem(item);
      if (getComparer().equals(element, itemElement)) {
        return item;
      }
    }
    return null;
  }

  @Override
  public List<Object> getActiveElements() {
    ArrayList<Object> list = new ArrayList<Object>();
    int count = table.getItemCount();
    for (int i = 0; i < count; i++) {
      TableItem item = table.getItem(i);
      if (item.isVisible()) {
        list.add(getElementFromItem(item));
      }
    }
    return list;
  }

  /**
   * Returns the table viewer column for the specified column.
   * 
   * @param columnId the column id
   * @return the table viewer column
   */
  public TableViewerColumn getViewerColumn(int columnId) {
    TableColumn column = table.getColumnModel().getColumn(columnId);
    TableViewerColumn vc = (TableViewerColumn) column.getData(ViewerColumn.COLUMN_VIEWER_KEY);
    if (vc == null) {
      vc = new TableViewerColumn(this, column);
    }
    return vc;
  }

  /**
   * Returns the viewer's table widget.
   * 
   * @return the table
   */
  public Table getTable() {
    return table;
  }

  @Override
  public Table getWidget() {
    return table;
  }

  @Override
  public void remove(Object element) {
    TableItem item = findItem(element);
    if (item != null) {
      table.remove(item);
      removeElement(element);
      item.setData(null);
    }
  }

  @Override
  public void setContentProvider(StructuredContentProvider contentProvider) {
    super.setContentProvider(contentProvider);
    if (contentProvider instanceof Loader) {
      bind((Loader) contentProvider);
    }
  }

  @Override
  public void setSelection(Selection elementSelection, boolean reveal) {
    List<Object> selected = elementSelection.toList();
    int ct = table.getItemCount();

    for (int i = 0; i < ct; i++) {
      TableItem item = table.getItem(i);
      Object itemElement = item.getData();
      if (selected.contains(itemElement)) {
        table.select(i);
      } else {
        table.deselect(i);
      }
    }
  }

  @Override
  public void update() {
    int ct = table.getItemCount();
    for (int i = 0; i < ct; i++) {
      updateInternal(table.getItem(i));
    }
  }

  @Override
  public void update(Object element) {
    TableItem item = findItem(element);
    if (item != null) {
      item.setData(element);
      updateInternal(item);
    }
  }

  protected void doLocalSort(int col) {
    ViewerColumn vc = getViewerColumn(col);

    final ViewerSorter sorter = vc.getViewerSorter();
    final TableViewer fViewer = this;

    List items = table.getItems();

    SortDir dir = table.getColumn(col).getSortDir();
    dir = dir == SortDir.ASC ? SortDir.DESC : SortDir.ASC;

    Collections.sort(items, dir.comparator(new Comparator<TableItem>() {
      public int compare(TableItem item1, TableItem item2) {
        Object o1 = item1.getData();
        Object o2 = item2.getData();
        return sorter.compare(fViewer, o1, o2);
      }
    }));

  }

  protected void doRemoteSort(TableEvent te) {
    int col = te.columnIndex;
    TableColumn column = table.getColumn(col);
    if (column.isSortable()) {
      SortDir dir = table.getColumn(col).getSortDir();
      dir = dir == SortDir.ASC ? SortDir.DESC : SortDir.ASC;
      
      loader.setSortDir(dir);
      loader.setSortField(column.getId());
      loader.load();
      table.getTableHeader().sort(col, dir);
      te.sortDir = dir;
      te.doit = false;
    }
  }

  protected void doSort(TableEvent te) {
    if (loader != null && loader.getRemoteSort()) {
      doRemoteSort(te);
      return;
    }

    int col = te.columnIndex;
    doLocalSort(col);

    table.getTableHeader().sort(col, te.sortDir);
    table.getView().reorderItems();
    te.doit = false;

  }

  @Override
  protected List<Object> getSelectedFromWidget() {
    ArrayList<Object> elems = new ArrayList<Object>();
    for (TableItem item : table.getSelection()) {
      elems.add((Object) item.getData());
    }
    return elems;
  }

  @Override
  protected void hookWidget(Component widget) {
    super.hookWidget(widget);
    table.addListener(Events.SortChange, new Listener<TableEvent>() {
      public void handleEvent(TableEvent te) {
        doSort(te);
      }
    });
  }

  @Override
  protected void insert(Object element, int index) {
    renderItem(element, index);
    TableItem item = findItem(element);
    if (item != null) {
      item.setVisible(isFiltered(null, element) ? false : true);
    }
  }

  @Override
  protected void onElementsReceived(List<Object> elements) {
    if (preventRender && !rendered) {
      renderElements = elements;
      return;
    }
    renderTable();
  }

  @Override
  protected void preserveSelections(Selection selection) {
    Iterator<Object> it = selection.iterator();
    while (it.hasNext()) {
      Object element = it.next();
      TableItem item = findItem(element);
      if (item != null) {
        table.select(item);
      }
    }
  }

  protected void renderItem(Object element, int index) {
    int cols = table.getColumnCount();
    String[] values = new String[cols];
    String[] toolTips = new String[cols];

    for (int j = 0; j < cols; j++) {
      CellLabelProvider lp = getCellLabelProvider(j);
      viewerCell.reset(element, null, j, table.getColumn(j).getId());
      lp.update(viewerCell);
      values[j] = viewerCell.getText();
      toolTips[j] = viewerCell.getToolTipText();
    }
    TableItem item = new TableItem(values);

    item.setData(element);
    item.setCellToolTips(toolTips);

    table.insert((T) item, index);
    update(element);
  }

  private CellLabelProvider getCellLabelProvider(int j) {
    CellLabelProvider lp = (CellLabelProvider) getViewerColumn(j).getLabelProvider();
    if (lp == null) {
      lp = getDefaultCellLabelProvider();
    }
    if (lp == null) {
      throw new RuntimeException("no label provider for column " + j);
    }
    return lp;
  }

  protected void renderTable() {
    table.removeAll();
    List<Object> list = new ArrayList<Object>(elements);
    sortElements(list);
    int i = 0;
    for (Object element : list) {
      renderItem(element, i++);
    }
    // applyFilters();
  }

  private void onBeforeLoad(LoadEvent de) {
    table.el.mask("Loading...");
  }

  private void onLoad(LoadEvent de) {
    Selection<Object> sel = getSelection();
    // apply sort if sorting locally
    // if (!de.loader.getRemoteSort()) {
    // if (de.loader.getSortField() != null) {
    // String field = de.loader.getSortField();
    // TableColumn column = table.getColumn(field);
    // if (column != null) {
    // int index = table.getColumnModel().indexOf(column);
    // doLocalSort(index);
    // }
    // }
    // }
    setInput(de.result.getData());
    preserveSelections(sel);
    table.el.unmask();

  }

  private void onLoadException(LoadEvent le) {
    table.el.unmask();
  }

  private void bind(Loader loader) {
    if (loadListener == null) {
      loadListener = new Listener<LoadEvent>() {
        public void handleEvent(LoadEvent le) {
          switch (le.type) {
            case Loader.BeforeLoad:
              onBeforeLoad(le);
              break;
            case DataLoader.Load:
              onLoad(le);
              break;
            case DataLoader.LoadException:
              onLoadException(le);
              break;
          }
        }
      };
    }
    if (loader != null) {
      loader.removeListener(DataLoader.BeforeLoad, loadListener);
      loader.removeListener(DataLoader.Load, loadListener);
      loader.removeListener(DataLoader.LoadException, loadListener);
    }
    this.loader = loader;
    if (this.loader != null) {
      this.loader.addListener(DataLoader.BeforeLoad, loadListener);
      this.loader.addListener(DataLoader.Load, loadListener);
      this.loader.addListener(DataLoader.LoadException, loadListener);
    }
  }

  private void updateInternal(TableItem item) {
    Object elem = getElementFromItem(item);
    int cols = table.getColumnCount();;
    for (int j = 0; j < cols; j++) {
      viewerCell.reset(elem, item, j, table.getColumn(j).getId());
      CellLabelProvider lp = getCellLabelProvider(j);
      lp.update(viewerCell);
      String text = viewerCell.getText();
      item.setValue(j, text);
      item.setCellToolTip(j, viewerCell.getToolTipText());

      String style = viewerCell.getTextStyle();
      if (style != null) {
        item.setCellStyle(j, style);
      }
    }
  }

  public void setDefaultCellLabelProvider(CellLabelProvider defaultCellLabelProvider) {
    this.defaultCellLabelProvider = defaultCellLabelProvider;
  }

  public CellLabelProvider getDefaultCellLabelProvider() {
    return defaultCellLabelProvider;
  }

}
