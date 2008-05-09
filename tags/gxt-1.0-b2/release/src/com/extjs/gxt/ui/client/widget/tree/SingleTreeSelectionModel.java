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
 * Single select tree selection model.
 */
public class SingleTreeSelectionModel extends TreeSelectionModel {

  protected TreeItem selectedItem;

  public void deselectAll() {
    if (selectedItem != null) {
      onSelectChange(selectedItem, false);
      tree.fireEvent(Events.SelectionChange, new TreeEvent(tree));
    }
  }

  /**
   * Deselects the item.
   * 
   * @param item the item to be deselected
   */
  public void deselect(TreeItem item) {
    if (isSelected(item)) {
      onSelectChange(item, false);
      doDeselect(new Items(item), false);
    }
  }

  @Override
  public List<TreeItem> doGetSelectedItems() {
    List<TreeItem> items = new ArrayList<TreeItem>();
    if (selectedItem != null) {
      items.add(selectedItem);
    }
    return items;
  }

  /**
   * Returns the selected item.
   * 
   * @return the selected item
   */
  public TreeItem getSelectedItem() {
    return selectedItem;
  }

  /**
   * Returns true if the item is selected.
   * 
   * @param item the item
   * @return true if selected
   */
  public boolean isSelected(TreeItem item) {
    return item == selectedItem;
  }

  @Override
  public void refresh() {
    if (selectedItem != null) {
      onSelectChange(selectedItem, true);
    }
  }

  public void selectAll() {
    // do nothing
  }

  @Override
  protected void doDeselect(Items<TreeItem> items, boolean supressEvent) {
    if (items.isSingle()) {
      TreeItem item = items.getItem(tree);
      if (item != null && item == selectedItem) {
        onSelectChange(selectedItem, false);
        selectedItem = null;
        if (!supressEvent) {
          tree.fireEvent(Events.SelectionChange, new TreeEvent(tree));
        }
      }
    }
  }

  @Override
  protected void doSelect(Items<TreeItem> items, boolean keepExisting,
      boolean supressEvent) {
    if (items.isSingle()) {
      boolean change = false;
      TreeItem item = items.getItem(tree);
      if (selectedItem != null && selectedItem == item) {
        return;
      }
      if (selectedItem != null) {
        onSelectChange(selectedItem, false);
        change = true;
      }
      selectedItem = item;
      if (selectedItem != null) {
        TreeEvent e = new TreeEvent(tree, selectedItem);
        if (selectedItem.fireEvent(Events.BeforeSelect, e)) {
         
          onSelectChange(selectedItem, true);
          lastSelected = selectedItem;
          change = true;
          selectedItem.fireEvent(Events.Select, e);
        }
      }
      if (change) {
        TreeEvent e = new TreeEvent(tree);
        e.selectedItem = selectedItem;
        tree.fireEvent(Events.SelectionChange, e);
      }

    }
  }

  @Override
  protected void onRemove(TreeItem item) {
    if (item == selectedItem) {
      deselect(item);
    }
  }

  @Override
  protected void onClick(TreeEvent e) {
    TreeItem item = e.item;
    if (item != null) {
      if (!e.within(item.getUI().getJointEl()) && !e.within(item.getUI().getCheckEl())) {
        if (isSelected(item) && e.isControlKey()) {
          deselect(item);
        } else {
          doSelect(new Items(item));
        }
      }
    }
  }

}
