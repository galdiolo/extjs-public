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
 *     Tom Schindl <tom.schindl@bestsolution.at> - bugfix in issues: 40
 *******************************************************************************/
package com.extjs.gxt.ui.client.viewer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Items;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.extjs.gxt.ui.client.widget.tree.TreeSelectionModel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * An interface to content providers for tree-structure-oriented viewers.
 * 
 * Content providers for tree viewers must implement either the
 * <code>ITreeContentProvider</code> interface or the
 * <code>IAsyncTreeContentProvider</code> interface.
 * 
 * @see TreeViewer
 */
public class TreeViewer<S extends TreeSelectionModel> extends Viewer<TreeContentProvider> implements Checkable,
    BaseTreeViewer {

  protected boolean checkable;
  private Tree<S> tree;
  private List<CheckStateListener> checkChangeListener;
  private boolean caching = true;
  private boolean force;

  /**
   * Creates a new tree viewer instance.
   * 
   * @param tree the underlying tree widget
   */
  public TreeViewer(Tree<S> tree) {
    this.tree = tree;
    preventRender = false;
    hookWidget(tree);
  }

  @Override
  public void applyFilters() {
    if (!rendered) {
      renderApplyFilters = true;
      return;
    }
    filterItems(tree.getRootItem());
  }

  private void filterItems(TreeItem item) {
    if (item.isRoot() || isOrDecendantSelected(null, item.getData())) {
      item.setVisible(true);
      int count = item.getItemCount();
      for (int i = 0; i < count; i++) {
        filterItems(item.getItem(i));
      }
    } else {
      item.setVisible(false);
    }
  }

  public void add(Object parent, Object child) {
    TreeItem p = findItem(parent);
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

  public TreeItem findItem(Object element) {
    for (TreeItem item : tree.getAllItems()) {
      Object data = getElementFromItem(item);
      if (getComparer().equals(element, data)) {
        return item;
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
    TreeItem item = findItem(element);

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
    List<TreeItem> items = tree.getChecked();
    if (items.size() == 0) {
      return DefaultSelection.emptySelection();
    }
    ArrayList<Object> checked = new ArrayList<Object>();
    for (TreeItem item : items) {
      checked.add((Object) item.getData());
    }
    return new DefaultSelection(checked);
  }

  /**
   * Returns the viewer's tree widget.
   * 
   * @return the tree
   */
  public Tree getTree() {
    return tree;
  }

  public Tree getWidget() {
    return tree;
  }

  public void insert(Object parent, Object child, int position) {
    TreeItem p = findItem(parent);
    internalAdd(p, child, position);
  }

  /**
   * Refreshes this viewer starting with the given element.
   * 
   * @param element the element
   */
  public void refresh(Object element) {
    TreeItem item = findItem(element);
    if (item != null) {
      int count = item.getItemCount();
      for (int i = 0; i < count; i++) {
        item.remove(item.getItem(0));
      }
      item.setData("loaded", null);
      loadChildren(item, item.isExpanded());
    }
  }

  public void remove(Object element) {
    TreeItem item = findItem(element);
    if (item != null) {
      TreeItem parent = item.getParentItem();
      parent.remove(item);
      removeElement(getElementFromItem(item));
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
    TreeItem item = findItem(element);
    doSelect(tree.getSelectionModel(), new Items(item));
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
    TreeItem item = findItem(element);

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
    TreeItem item = findItem(element);
    if (item != null) {
      item.setExpanded(expanded);
    }
  }

  public void setInput(final Object input) {
    getContentProvider().inputChanged(this, this.input, input);
    getContentProvider().getChildren(input, new AsyncContentCallback<Object>() {
      public void setElements(List<Object> elems) {
        elements = new ArrayList<Object>(elems);
        onElementsReceived(elements);
      }
    });
    this.input = input;
  }

  /**
   * Sets the viewer's input.
   * 
   * @param input the input
   * @param force true to force loading of all children
   */
  public void setInput(Object input, boolean force) {
    this.force = force;
    setInput(input);
  }

  public void setSelection(Selection selection, boolean reveal) {
    List<Object> selected = selection.toList();
    List<TreeItem> items = new ArrayList<TreeItem>();
    for (TreeItem item : tree.getAllItems()) {
      Object elem = getElementFromItem(item);
      if (selected.contains(elem)) {
        items.add(item);
      }
    }

    doSelect(tree.getSelectionModel(), new Items(items));
  }

  public void update() {
    TreeItem root = tree.getRootItem();
    for (TreeItem item : tree.getAllItems()) {
      if (item != root) {
        updateInternal(item);
      }
    }
  }

  public void update(Object element) {
    TreeItem item = findItem(element);
    if (item != null) {
      item.setData(element);
      updateInternal(item);
    }
  }

  protected void add(Object element) {
    // do nothing
  }

  protected List<Object> getSelectedFromWidget() {
    ArrayList<Object> elems = new ArrayList<Object>();
    for (TreeItem item : tree.getSelectionModel().doGetSelectedItems()) {
      elems.add(getElementFromItem(item));
    }
    return elems;
  }

  protected void hookWidget(Component widget) {
    super.hookWidget(widget);
    init(tree);
    widget.addListener(Events.CheckChange, new Listener<TreeEvent>() {

      public void handleEvent(TreeEvent te) {
        fireCheckStateChanged(te);
      }
    });

    Listener<TreeEvent> l = new Listener<TreeEvent>() {
      public void handleEvent(TreeEvent te) {
        switch (te.type) {
          case Events.BeforeExpand: {
            TreeItem item = te.item;
            Object loaded = item.getData("loaded");
            if (loaded == null) {
              te.doit = false;
              loadChildren(item, true);
            }
            break;
          }
          case Events.Collapse: {
            if (!caching) {
              TreeItem item = te.item;
              int count = item.getItemCount();
              for (int i = 0; i < count; i++) {
                item.remove(item.getItem(0));
              }
              item.setData("loaded", null);
              clearChildrenRendered(item);
            }
            break;
          }

        }
      }
    };

    tree.addListener(Events.BeforeExpand, l);
    tree.addListener(Events.Collapse, l);
  }

  protected void insert(Object elem, int index) {
    // do nothing
  }

  @Override
  protected void onElementsReceived(List<Object> elements) {
    if (preventRender && !rendered) {
      return;
    }
    renderTree();
  }

  protected TreeItem renderItem(TreeItem parent, Object element, int position) {
    boolean hasChildren = false;

    hasChildren = getContentProvider().hasChildren(element);

    LabelProvider lp = (LabelProvider) getLabelProvider();

    TreeItem item = new TreeItem();
    item.setData(element);
    item.setText(lp.getText(element));
    item.setIconStyle(lp.getIconStyle(element));
    item.setTextStyle(lp.getTextStyle(element));
    item.setLeaf(!hasChildren);

    if (checkable) {
      item.setChecked(((Checkable) getContentProvider()).getChecked(element));
    }

    if (position == -1) {
      parent.add(item);
    } else {
      parent.insert(item, position);
    }
    return item;
  }

  private native void clearChildrenRendered(TreeItem item) /*-{
        item.@com.extjs.gxt.ui.client.widget.tree.TreeItem::childrenRendered = false;
      }-*/;

  private List<Object> filterChildren(TreeItem parent, List<Object> elems) {
    List<Object> temp = new ArrayList<Object>();
    for (Object e : elems) {
      if (isOrDecendantSelected(parent.getData(), e)) {
        temp.add(e);
      }
    }
    return temp;
  }

  private void fireCheckStateChanged(TreeEvent te) {
    if (checkChangeListener != null) {
      TreeItem item = te.item;

      CheckStateChangedEvent evt = new CheckStateChangedEvent(this,
          getElementFromItem(item), item.isChecked());
      Iterator<CheckStateListener> it = checkChangeListener.iterator();

      while (it.hasNext()) {
        it.next().checkStateChanged(evt);
      }
    }
  }

  private native void init(Tree tree) /*-{
     tree.@com.extjs.gxt.ui.client.widget.tree.Tree::isViewer = true;
    }-*/;

  private void internalAdd(TreeItem parent, Object element, int position) {
    parent.setLeaf(false);
    // if the children have not been loaded then do nothing
    if (parent.getData("loaded") != null) {
      if (!isFiltered(parent, element)) {
        renderItem(parent, element, position);
        if (getSorter() != null) {
          sortChildren(parent);
        }
      }
    }
  }

  /**
   * Returns <code>true</code> if the element or any child elements is not
   * filetered.
   * 
   * @param element the element
   * @return the select state
   */
  private boolean isOrDecendantSelected(Object parent, Object element) {
    if (!isFiltered(parent, element)) {
      return true;
    }
    TreeItem item = findItem(element);
    if (item != null) {
      for (TreeItem child : item.getItems()) {
        boolean result = isOrDecendantSelected(element, child.getData());
        if (result) {
          return true;
        }
      }
    }
    return false;
  }

  private void loadChildren(final TreeItem item, boolean expand) {
    // if there is an async call out for my children already, I want to make
    // sure that I don't make another call and load the same items twice
    if (!item.isEnabled()) {
      return;
    }
    item.setEnabled(false);

    if (item.isRendered()) {
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
            if (item.isRendered()) {
              item.getUI().onLoadingChange(false);
            }

            item.setEnabled(true);
            children = filterChildren(item, children);
            sortElements(children);

            boolean f = item.getData("force") != null;
            item.setData("force", null);

            for (Object e : children) {
              TreeItem child = renderItem(item, e, -1);
              if (f) {
                child.setData("force", "true");
                loadChildren(child, false);
              }
            }
            item.setData("loaded", "true");
            if (!f && item.hasChildren()) {
              item.setExpanded(true);
            } else if (!item.hasChildren()) {
              item.setLeaf(true);
            }
            if (item.isRendered()) {
              item.getUI().updateJointStyle();
            }
          }
        });

  }

  private void renderTree() {
    TreeItem root = tree.getRootItem();
    root.setData(input);

    int count = root.getItemCount();
    for (int i = 0; i < count; i++) {
      root.remove(root.getItem(0));
    }

    List<Object> list = new ArrayList<Object>(elements);
    sortElements(list);

    for (Object element : list) {
      TreeItem item = null;
      if (getFilters().size() > 0) {
        if (isOrDecendantSelected(null, element)) {
          item = renderItem(root, element, -1);
        }
      } else {
        item = renderItem(root, element, -1);
      }
      if (force && item != null) {
        item.setData("force", "true");
        loadChildren(item, false);
      }
    }
  }

  private void sortChildren(TreeItem parent) {
    List<Object> list = new ArrayList<Object>();
    Element p = parent.getContainer();
    for (int i = 0; i < parent.getItemCount(); i++) {
      try {
        TreeItem item = parent.getItem(i);
        DOM.removeChild(p, item.getElement());
        list.add(getElementFromItem(item));
      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    sortElements(list);

    for (Object e : list) {
      TreeItem item = findItem(e);
      Element elem = item.getElement();
      DOM.appendChild(p, elem);
    }

  }

  private void updateInternal(TreeItem item) {
    BaseLabelProvider lp = getLabelProvider();
    Object elem = getElementFromItem(item);

    item.setText(lp.getText(elem));
    item.setIconStyle(lp.getIconStyle(elem));
    item.setTextStyle(lp.getTextStyle(elem));
  }

  private native void doSelect(TreeSelectionModel sm, Items items) /*-{
     sm.@com.extjs.gxt.ui.client.widget.tree.TreeSelectionModel::doSelect(Lcom/extjs/gxt/ui/client/widget/Items;)(items);
   }-*/;

}
