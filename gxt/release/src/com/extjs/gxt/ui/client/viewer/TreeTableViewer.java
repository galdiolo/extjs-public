/*******************************************************************************
 * Copyright (c) 2007, 2008 GXT.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Items;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.extjs.gxt.ui.client.widget.tree.TreeSelectionModel;
import com.extjs.gxt.ui.client.widget.treetable.TreeTable;
import com.extjs.gxt.ui.client.widget.treetable.TreeTableItem;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * An interface to content providers for treetable-structure-oriented viewers.
 * 
 * Content providers for treetable viewers must implement either the
 * <code>ITreeContentProvider</code> interface or the
 * <code>IAsyncTreeContentProvider</code> interface.
 * 
 * <p>
 * This code is based on JFace API from the Eclipse Project.
 * </p>
 * 
 * @see TreeViewer
 */
public class TreeTableViewer extends Viewer<TreeContentProvider> implements Checkable,
    BaseTreeViewer {

  protected boolean checkable;
  private ViewerCell viewerCell = new ViewerCell();
  private TreeTable treeTable;
  private List<CheckStateListener> checkChangeListener;
  private boolean caching = true;
  private Map<Object, TreeTableItem> nodeMap = new HashMap<Object, TreeTableItem>();

  /**
   * Creates a new treetable viewer instance.
   * 
   * @param treeTable the underlying treetable widget
   */
  public TreeTableViewer(TreeTable treeTable) {
    this.treeTable = treeTable;
    hookWidget(treeTable);

    Listener l = new Listener<TreeEvent>() {
      public void handleEvent(TreeEvent ce) {
        switch (ce.type) {
          case Events.BeforeExpand: {
            TreeTableItem item = (TreeTableItem) ce.item;
            Object loaded = item.getData("loaded");
            if (loaded == null) {
              ce.doit = false;
              loadChildren(item, true);
            }
            break;
          }
          case Events.Collapse: {
            if (!caching) {
              TreeTableItem item = (TreeTableItem) ce.item;
              int count = item.getItemCount();
              for (int i = 0; i < count; i++) {
                item.remove(item.getItem(0));
              }
              item.setData("loaded", null);
            }
            break;
          }

        }
      }
    };

    treeTable.addListener(Events.BeforeExpand, l);
    treeTable.addListener(Events.Collapse, l);
  }

  /**
   * Adds the given child element to this viewer as a child of the given parent
   * element.
   * 
   * @param parent the parent element
   * @param child the child element
   */
  public void add(Object parent, Object child) {
    TreeTableItem p = findItem(parent);
    internalAdd(p, child, p.getItemCount());
  }

  public void addCheckStateListener(CheckStateListener listener) {
    if (checkChangeListener == null) {
      checkChangeListener = new ArrayList<CheckStateListener>();
    }
    if (!checkChangeListener.contains(listener)) {
      checkChangeListener.add(listener);
    }
  }

  /**
   * Applies the viewer's filters to the current elements.
   */
  public void applyFilters() {
    if (!rendered) {
      renderApplyFilters = true;
      return;
    }
    filterItems(treeTable.getRootItem());
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.extjs.gxt.ui.client.viewer.Viewer#findItem(java.lang.Object)
   */
  public TreeTableItem findItem(Object element) {
    for (TreeItem item : treeTable.getAllItems()) {
      Object itemElement = item.getData();
      if (getComparer().equals(element, itemElement)) {
        return (TreeTableItem) item;
      }
    }
    return null;
  }

  /**
   * Returns <code>true</code> if the viewer is caching.
   * 
   * @return the caching state
   */
  public boolean getCaching() {
    return caching;
  }

  public boolean getChecked(Object element) {
    TreeTableItem item = findItem(element);
    if (item != null) {
      return item.isChecked();
    }
    return false;
  }

  /**
   * Returns the viewer's tree widget.
   * 
   * @return the tree
   */
  public Tree getTree() {
    return treeTable;
  }

  /**
   * Returns the underlying treetable widget
   * 
   * @return the treetable
   */
  public TreeTable getTreeTable() {
    return treeTable;
  }

  /**
   * Returns the table viewer column for the specified column.
   * 
   * @param columnId the column id
   * @return the table viewer column
   */
  public TreeTableViewerColumn getViewerColumn(int columnId) {
    TableColumn column = getTreeTable().getColumnModel().getColumn(columnId);
    TreeTableViewerColumn vc = (TreeTableViewerColumn) column.getData(ViewerColumn.COLUMN_VIEWER_KEY);
    if (vc == null) {
      vc = new TreeTableViewerColumn(this, column);
    }
    return vc;
  }

  @Override
  public TreeTable getWidget() {
    return treeTable;
  }

  /**
   * Inserts the given element as a new child element of the given parent
   * element at the given position.
   * 
   * @param parent the parent element
   * @param child the child element
   * @param position the insert position
   */
  public void insert(Object parent, Object child, int position) {
    TreeTableItem p = findItem(parent);
    internalAdd(p, child, position);
  }

  /**
   * Refreshes this viewer starting with the given element.
   * 
   * @param element the element
   */
  public void refresh(Object element) {
    TreeTableItem item = findItem(element);
    if (item != null) {
      int count = item.getItemCount();
      for (int i = 0; i < count; i++) {
        item.remove(item.getItem(0));
      }
      item.setData("loaded", null);
      loadChildren(item, item.isExpanded());
    }
  }

  /**
   * Removes the given element from the viewer.
   * 
   * @param element the element to be removed
   */
  public void remove(Object element) {
    TreeTableItem item = findItem(element);
    if (item != null) {
      TreeTableItem parent = (TreeTableItem) item.getParentItem();
      parent.remove(item);
      removeElement(getElementFromItem(item));
      nodeMap.remove(element);
      item.setData(null);
    }
  }

  public void removeCheckStateListener(CheckStateListener listener) {
    if (checkChangeListener != null) {
      checkChangeListener.remove(listener);
    }
  }

  /**
   * Selects the elements.
   * 
   * @param element the element to be selected
   */
  public void select(Object element) {
    TreeTableItem item = findItem(element);
    doSelect(treeTable.getSelectionModel(), new Items(item));
  }

  /**
   * Sets whether the children should be cached after first being retrieved from
   * the content provider. When <code>false</code>, the tree items will be
   * removed when collapsed.Default value is <code>true</code>.
   * 
   * @param caching
   */
  public void setCaching(boolean caching) {
    this.caching = caching;
  }

  public boolean setChecked(Object element, boolean state) {
    TreeTableItem item = findItem(element);

    if (item != null) {
      item.setChecked(state);
      return true;
    }

    return false;
  }

  public void setContentProvider(TreeContentProvider contentProvider) {
    super.setContentProvider(contentProvider);
    if (contentProvider instanceof Checkable) {
      checkable = true;
    }
  }

  /**
   * Sets the expanded state for the element.
   * 
   * @param element the element
   * @param expanded the expand state
   */
  public void setExpanded(Object element, boolean expanded) {
    TreeTableItem item = findItem(element);
    if (item != null) {
      item.setExpanded(expanded);
    }
  }

  public void setInput(final Object input) {
    getContentProvider().inputChanged(this, this.input, input);
    this.input = input;
    getContentProvider().getChildren(input, new AsyncContentCallback<Object>() {
      public void setElements(List<Object> newElements) {
        elements = new ArrayList<Object>(newElements);
        onElementsReceived(elements);
      }
    });
  }

  public void setSelection(Selection selection, boolean reveal) {
    if (!rendered) {
      renderSelection = selection;
      return;
    }
    List<Object> selected = selection.toList();
    List<TreeItem> items = new ArrayList<TreeItem>();
    for (TreeItem item : treeTable.getAllItems()) {
      Object elem = getElementFromItem(item);
      if (selected.contains(elem)) {
        items.add(item);
      }
    }
    doSelect(treeTable.getSelectionModel(), new Items(items));
  }

  public void update() {
    for (TreeItem item : treeTable.getAllItems()) {
      updateInternal((TreeTableItem) item);
    }
  }

  public void update(Object elem) {
    TreeTableItem item = findItem(elem);
    if (item != null) {
      updateInternal(item);
    }
  }

  protected void add(Object elem) {

  }

  protected List<Object> getSelectedFromWidget() {
    ArrayList<Object> elems = new ArrayList<Object>();
    for (TreeItem item : treeTable.getSelectionModel().doGetSelectedItems()) {
      elems.add(getElementFromItem(item));
    }
    return elems;
  }

  protected void hookWidget(Component widget) {
    super.hookWidget(widget);
    init(treeTable);
    widget.addListener(Events.CheckChange, new Listener<ComponentEvent>() {

      public void handleEvent(ComponentEvent ce) {
        fireCheckStateChanged(ce);
      }
    });
  }

  protected void insert(Object elem, int index) {
    // do nothing
  }

  protected void onElementsReceived(List<Object> elements) {
    renderTree();
  }

  protected void renderItem(TreeTableItem parent, Object elem, int position) {
    boolean hasChildren = false;

    hasChildren = getContentProvider().hasChildren(elem);

    int cols = getTreeTable().getColumnCount();
    String[] values = new String[cols];
    String[] toolTips = new String[cols];
    String[] styles = new String[cols];

    String iconStyle = null;

    for (int j = 0; j < cols; j++) {
      CellLabelProvider lp = (CellLabelProvider) getViewerColumn(j).getLabelProvider();
      if (lp != null) {
        viewerCell.reset(elem, null, j, getTreeTable().getColumn(j).getId());
        lp.update(viewerCell);
        values[j] = viewerCell.getText();
        toolTips[j] = viewerCell.getToolTipText();
        styles[j] = viewerCell.getTextStyle();
        if (j == 0 && viewerCell.getIconStyle() != null) {
          iconStyle = viewerCell.getIconStyle();
        }
      }
    }

    BaseLabelProvider lp = getLabelProvider();
    if (lp != null) {
      values[0] = lp.getText(elem);
      if (lp.getIconStyle(elem) != null) {
        iconStyle = lp.getIconStyle(elem);
      }
    }

    TreeTableItem item = new TreeTableItem(values);
    item.setData(elem);
    item.setText(values[0]);
    item.setCellToolTips(toolTips);
    item.setIconStyle(iconStyle);
    item.setLeaf(!hasChildren);

    nodeMap.put(elem, item);

    if (checkable) {
      item.setChecked(((Checkable) getContentProvider()).getChecked(elem));
    }

    if (position == -1) {
      parent.add(item);
    } else {
      parent.insert(item, position);
    }

    for (int i = 0; i < styles.length; i++) {
      if (styles[i] != null) {
        item.setCellStyle(i, styles[i]);
      }
    }

  }

  private List<Object> filterChildren(TreeItem parent, List<Object> elems) {
    List<Object> temp = new ArrayList<Object>();
    for (Object e : elems) {
      if (isOrDecendantSelected(e)) {
        temp.add(e);
      }
    }
    return temp;
  }

  private void filterItems(TreeItem item) {
    if (item.isRoot() || isOrDecendantSelected(item.getData())) {
      item.setVisible(true);
      int count = item.getItemCount();
      for (int i = 0; i < count; i++) {
        filterItems(item.getItem(i));
      }
    } else {
      item.setVisible(false);
    }
  }

  private native void init(Tree tree) /*-{
     tree.@com.extjs.gxt.ui.client.widget.tree.Tree::isViewer = true;
    }-*/;

  private void fireCheckStateChanged(ComponentEvent ce) {
    if (checkChangeListener != null) {
      TreeItem item = (TreeItem) ce.component;

      CheckStateChangedEvent evt = new CheckStateChangedEvent(this, item.getData(),
          item.isChecked());
      Iterator it = checkChangeListener.iterator();

      while (it.hasNext()) {
        ((CheckStateListener) it.next()).checkStateChanged(evt);
      }
    }
  }

  private void internalAdd(TreeTableItem parent, Object elem, int position) {
    // if the children have not been loaded then do nothing
    if (parent.getData("loaded") != null) {
      if (!isFiltered(parent, elem)) {
        renderItem(parent, elem, position);
        if (getSorter() != null) {
          sortChildren(parent);
        }
      }
    }
  }

  /**
   * Returns <code>true</code> if the element or any child elements is not
   * filtered.
   * 
   * @param elem the element
   * @return the select state
   */

  private boolean isOrDecendantSelected(Object elem) {
    if (!isFiltered(null, elem)) {
      return true;
    }

    TreeItem item = (TreeItem) nodeMap.get(elem);
    if (item != null) {
      int count = item.getItemCount();
      for (int i = 0; i < count; i++) {
        TreeItem child = item.getItem(i);
        boolean result = isOrDecendantSelected(child.getData());
        if (result) {
          return true;
        }
      }
    }
    return false;
  }

  private void loadChildren(final TreeTableItem item, boolean expand) {

    // if there is an async call out for my children already, I want to make
    // sure that I don't make another call and load the same items twice
    if (!item.isEnabled()) {
      return;
    }
    item.setEnabled(false);

    if (item.getUI() != null) {
      item.getUI().onLoadingChange(true);
    }
    
    getContentProvider().getChildren(getElementFromItem(item),
        new AsyncContentCallback<Object>() {

          public void setElements(List<Object> children) {
            if (children == null) {
              item.setData("state", null);
              item.getUI().onLoadingChange(false);
              return;
            }
            if (item.getUI() != null) {
              item.getUI().onLoadingChange(false);
            }
            item.setEnabled(true);
            children = filterChildren(item, children);
            sortElements(children);
            for (Object e : children) {
              renderItem(item, e, -1);
            }
            item.setData("loaded", "true");
            if (item.hasChildren()) {
              item.setExpanded(true);
            } else {
              item.setLeaf(true);
              item.getUI().updateJointStyle();
            }

          }
        });

  }

  private void renderTree() {
    TreeTableItem root = (TreeTableItem) treeTable.getRootItem();
    root.setData(input);

    int count = root.getItemCount();
    for (int i = 0; i < count; i++) {
      root.remove(root.getItem(0));
    }

    List<Object> elems = new ArrayList<Object>(elements);
    sortElements(elems);
    for (Object e : elems) {
      if (getFilters().size() > 0) {
        if (isOrDecendantSelected(e)) {
          renderItem(root, e, -1);
        }
      } else {
        renderItem(root, e, -1);
      }
    }
  }

  private void sortChildren(TreeTableItem parent) {
    List<Object> list = new ArrayList<Object>();
    Element p = parent.getContainer();
    for (int i = 0; i < parent.getItemCount(); i++) {
      TreeTableItem item = (TreeTableItem) parent.getItem(i);
      DOM.removeChild(p, item.getElement());
      list.add((Object) item.getData());
    }
    sortElements(list);

    for (Object e : list) {
      TreeTableItem item = findItem(e);
      Element elem = item.getElement();
      DOM.appendChild(p, elem);
    }

  }

  private void updateInternal(TreeTableItem item) {
    Object elem = (Object) item.getData();

    String iconStyle = null;
    String text = null;

    int cols = treeTable.getColumnCount();
    for (int j = 0; j < cols; j++) {
      viewerCell.reset(elem, item, j, getTreeTable().getColumn(j).getId());
      CellLabelProvider clp = (CellLabelProvider) getViewerColumn(j).getLabelProvider();
      if (clp != null) {
        clp.update(viewerCell);
        item.setValue(j, viewerCell.getText());
        item.setCellToolTip(j, viewerCell.getToolTipText());
        String style = viewerCell.getTextStyle();
        if (style != null) {
          item.setCellStyle(j, style);
        }
        if (j == 0) {
          text = viewerCell.getText();
          iconStyle = viewerCell.getIconStyle();
        }
      }
    }

    BaseLabelProvider lp = getLabelProvider();
    if (lp != null) {
      text = lp.getText(elem);
      if (lp.getIconStyle(elem) != null) {
        iconStyle = lp.getIconStyle(elem);
      }
    }
    item.setText(text);
    item.setIconStyle(iconStyle);
  }

  private native void doSelect(TreeSelectionModel sm, Items items) /*-{
     sm.@com.extjs.gxt.ui.client.widget.tree.TreeSelectionModel::doSelect(Lcom/extjs/gxt/ui/client/widget/Items;)(items);
   }-*/;
}
