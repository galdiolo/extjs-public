/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tree;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.Items;

/**
 * Multi-select tree selection model.
 */
public class MultiTreeSelectionModel extends SingleTreeSelectionModel {

  protected List<TreeItem> selected;

  /**
   * Returns the selected items.
   * 
   * @return the items
   */
  public List<TreeItem> getSelectedItems() {
    return new ArrayList<TreeItem>(selected);
  }

  /**
   * Selects the item(s).
   * 
   * @param items the item(s)
   */
  public void select(List<TreeItem> items) {
    doSelect(new Items(items), false, true);
  }

  @Override
  public void selectAll() {
    doSelect(new Items(tree.getAllItems()), false, false);
  }

  protected void doDeselectAll(boolean supressEvent) {
    boolean change = selected.size() > 0;
    for (TreeItem item : selected) {
      onSelectChange(item, false);
    }
    selected.clear();
    if (change && !supressEvent) {
      tree.fireEvent(Events.SelectionChange, new TreeEvent(tree));
    }
  }

  @Override
  protected void onRemove(TreeItem item) {
    if (selected.contains(item)) {
      deselect(item);
    }
  }

  protected void doSelect(Items<TreeItem> items, boolean keepExisting,
      boolean supressEvent) {
    boolean change = false;
    if (!keepExisting) {
      if (selected.size() > 0) {
        change = true;
      }
      doDeselectAll(false);
    }
    for (TreeItem item : items.getItems(tree)) {
      TreeEvent e = new TreeEvent(tree, selectedItem);
      if (selectedItem.fireEvent(Events.BeforeSelect, e)) {
        change = true;
        onSelectChange(item, true);
        selected.add(item);
        lastSelected = item;
        selectedItem.fireEvent(Events.Select, e);
      }

    }
    if (change && !supressEvent) {
      TreeEvent e = new TreeEvent(tree);
      e.selected = selected;
      tree.fireEvent(Events.SelectionChange, e);
    }
  }
}
