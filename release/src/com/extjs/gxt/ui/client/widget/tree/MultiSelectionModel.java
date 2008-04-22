/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;


/**
 * A multi-select tree selection model.
 * 
 * <dl>
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>SelectionChange</b> : TreeEvent(tree)<br>
 * <div>Fires after the selection has changed.</div>
 * <ul>
 * <li>tree : this</li>
 * </ul>
 * </dd>
 * </dl>
 */
public class MultiSelectionModel extends TreeSelectionModel {

  private List<TreeItem> selItems;

  /**
   * Creates a new multi select selection model.
   */
  public MultiSelectionModel() {
    selItems = new ArrayList<TreeItem>();
  }

  public void deselectAll() {
    if (selItems.size() > 0) {
      Iterator<TreeItem> iter = selItems.iterator();
      while (iter.hasNext()) {
        TreeItem item = iter.next();
        iter.remove();
        item.getUI().onSelectedChange(false);
      }
      TreeEvent ce = new TreeEvent(tree);
      fireEvent(Events.SelectionChange, ce);
      tree.fireEvent(Events.SelectionChange, ce);
    }
  }

  /**
   * Returns a list of selected items.
   * 
   * @return the selected items
   */
  public List<TreeItem> getSelection() {
    return new ArrayList<TreeItem>(selItems);
  }

  public boolean isSelected(TreeItem item) {
    return selItems.contains(item);
  }

  public void select(TreeItem item) {
    select(item, false);
  }

  /**
   * Selects a item.
   * 
   * @param item the item to be selected
   * @param keepSelected <code>true</code> to preserve selections
   */
  public void select(TreeItem item, boolean keepSelected) {
    if (!keepSelected) {
      deselectAll();
    }
    if (isSelected(item)) {
      lastSelItem = item;
      return;
    }
    selItems.add(item);
    lastSelItem = item;
    item.getUI().onSelectedChange(true);
    TreeEvent te = new TreeEvent(tree);
    fireEvent(Events.SelectionChange, te);
    tree.fireEvent(Events.SelectionChange, te);
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        if (!GXT.isSafari) {
          selItem.focus();
        } else {
          tree.focus();
        }
      }
    });
  }

  /**
   * Selects a list of item's.
   * 
   * @param items the items to be selected
   */
  public void selectItems(List items) {
    for (int i = 0; i < items.size(); i++) {
      TreeItem item = (TreeItem) items.get(i);
      select(item);
    }
  }

  /**
   * Deslects a list of items.
   * 
   * @param items the items to be deselected
   */
  public void deselect(List items) {
    for (int i = 0; i < items.size(); i++) {
      TreeItem item = (TreeItem) items.get(i);
      deselect(item);
    }
  }

  public void deselect(TreeItem item) {
    if (selItems.contains(item)) {
      item.getUI().onSelectedChange(false);
      selItems.remove(item);
      TreeEvent be = new TreeEvent(tree);
      fireEvent(Events.SelectionChange, be);
      tree.fireEvent(Events.SelectionChange, be);
    }
  }

  protected void onItemClick(TreeItem item, ComponentEvent ce) {
    if (ce.isRightClick()) {
      return;
    }
    if (!ce.within(item.getUI().getJointEl())) {
      if (isSelected(item) && ce.isControlKey()) {
        deselect(item);
      } else {
        select(item, ce.isControlKey());
      }
    }
  }

}
