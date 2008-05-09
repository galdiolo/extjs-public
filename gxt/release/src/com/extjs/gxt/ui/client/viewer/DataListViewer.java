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
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.DataListEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.DataList;
import com.extjs.gxt.ui.client.widget.DataListItem;
import com.google.gwt.user.client.ui.Widget;

/**
 * A concrete viewer based for a <code>List</code>.
 * 
 * @see DataList
 */
public class DataListViewer extends StructuredViewer implements Checkable {

  private DataList dataList;
  private ArrayList<CheckStateListener> checkChangeListener;

  @Override
  public void applyFilters() {
    if (!rendered) {
      renderApplyFilters = true;
      return;
    }
    if (elements == null) return;
    for (Object element : elements) {
      Widget w = findItem(element);
      if (w != null) {
        if (isFiltered(null, element)) {
          w.setVisible(false);
        } else {
          w.setVisible(true);
        }
      }
    }
  }

  /**
   * Creates a new list intance.
   * 
   * @param dataList the list
   */
  public DataListViewer(DataList dataList) {
    this.dataList = dataList;
    dataList.getElement();
    hookWidget(dataList);
  }

  @Override
  public void add(Object element) {
    renderItem(element, dataList.getItemCount());
  }

  public void addCheckStateListener(CheckStateListener listener) {
    if (checkChangeListener == null) {
      checkChangeListener = new ArrayList<CheckStateListener>();
    }
    if (!checkChangeListener.contains(listener)) {
      checkChangeListener.add(listener);
    }
  }

  @Override
  public DataListItem findItem(Object element) {
    int size = dataList.getItemCount();
    for (int i = 0; i < size; i++) {
      DataListItem item = dataList.getItem(i);
      Object itemElement = getElementFromItem(item);
      if (getComparer().equals(element, itemElement)) {
        return item;
      }
    }
    return null;
  }

  @Override
  public List<Object> getActiveElements() {
    ArrayList<Object> activeElements = new ArrayList<Object>();
    for (DataListItem item : dataList.getItems()) {
      if (item.isVisible()) {
        activeElements.add(getElementFromItem(item));
      }
    }
    return activeElements;
  }

  public boolean getChecked(Object element) {
    DataListItem item = (DataListItem) findItem(element);
    if (item != null) {
      return item.isChecked();
    }
    return false;
  }

  /**
   * Returns the current checked selection.
   * 
   * @return the checked elements
   */
  public Selection getCheckedSelection() {
    List<DataListItem> items = dataList.getChecked();
    if (items.size() == 0) {
      return DefaultSelection.emptySelection();
    }
    List<Object> checked = new ArrayList<Object>();
    for (DataListItem item : items) {
      checked.add(getElementFromItem(item));
    }
    return new DefaultSelection(checked);
  }

  @Override
  public DataList getWidget() {
    return dataList;
  }

  @Override
  public void insert(Object element, int index) {
    renderItem(element, index);
  }

  @Override
  public void remove(Object element) {
    DataListItem item = (DataListItem) findItem(element);
    if (item != null) {
      removeElement(element);
      item.setData(null);
    }
  }

  public void removeCheckStateListener(CheckStateListener listener) {
    if (checkChangeListener != null) {
      checkChangeListener.remove(listener);
    }
  }

  public boolean setChecked(Object element, boolean state) {
    DataListItem item = (DataListItem) findItem(element);
    if (item != null) {
      item.setChecked(state);
      return true;
    }
    return false;
  }

  @Override
  public void setSelection(Selection elementSelection, boolean reveal) {
    if (!rendered) {
      renderSelection = elementSelection;
      return;
    }
    List<Object> selectedElements = elementSelection.toList();
    for (DataListItem item : dataList.getItems()) {
      Object itemElement = item.getData();
      if (selectedElements.contains(itemElement)) {
        dataList.select(item);
      }
    }
  }

  @Override
  public void update() {
    int ct = dataList.getItemCount();
    for (int i = 0; i < ct; i++) {
      updateInternal(dataList.getItem(i));
    }
  }

  @Override
  public void update(Object element) {
    DataListItem item = (DataListItem) findItem(element);
    if (item != null) {
      item.setData(element);
      updateInternal(item);
    }

  }

  @Override
  protected List<Object> getSelectedFromWidget() {
    List<Object> selection = new ArrayList<Object>();
    for (DataListItem item : dataList.getSelectedItems()) {
      selection.add(getElementFromItem(item));
    }
    return selection;
  }

  @Override
  protected void hookWidget(Component widget) {
    super.hookWidget(widget);
    widget.addListener(Events.CheckChange, new Listener<DataListEvent>() {
      public void handleEvent(DataListEvent dle) {
        fireCheckStateChanged(dle);
      }
    });
  }

  @Override
  protected void onElementsReceived(List<Object> elements) {
    renderList();
  }

  protected void renderItem(Object element, int position) {
    BaseLabelProvider lp = getLabelProvider();
    DataListItem item = new DataListItem();
    item.setData(element);
    item.setText(lp.getText(element));
    String style = lp.getIconStyle(element);
    if (style != null) {
      item.setIconStyle(lp.getIconStyle(element));
    }
    dataList.insert(item, position);
  }

  protected void renderList() {
    dataList.removeAll();
    List<Object> filteredElements = filter(elements);
    sortElements(filteredElements);
    int i = 0;
    for (Object element : filteredElements) {
      renderItem(element, i++);
    }
  }

  private void fireCheckStateChanged(DataListEvent dle) {
    if (checkChangeListener != null) {
      DataListItem item = dle.item;
      CheckStateChangedEvent evt = new CheckStateChangedEvent(this, item.getData(),
          item.isChecked());

      for (CheckStateListener listener : checkChangeListener) {
        listener.checkStateChanged(evt);
      }
    }
  }

  private void updateInternal(DataListItem item) {
    BaseLabelProvider lp = getLabelProvider();
    Object elem = (Object) item.getData();
    item.setText(lp.getText(elem));
    String style = lp.getIconStyle(elem);
    if (style != null) {
      item.setIconStyle(lp.getIconStyle(elem));
    }
  }

}
