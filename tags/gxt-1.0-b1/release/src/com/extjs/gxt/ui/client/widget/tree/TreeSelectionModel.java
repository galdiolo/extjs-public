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
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.util.Observable;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * A single-select tree selection model.
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
 * 
 * @see MultiSelectionModel
 */
public class TreeSelectionModel extends Observable {

  protected Tree tree;
  protected TreeItem selItem;
  protected TreeItem lastSelItem;
  protected Listener treeListener;

  public TreeSelectionModel() {
    treeListener = new Listener<TreeEvent>() {
      public void handleEvent(TreeEvent te) {
        int type = te.type;
        TreeItem item = te.item;
        switch (type) {
          case Events.MouseDown:
            if (!te.isRightClick()) {
              onItemClick(item, te);
            }
            break;
          case Events.KeyDown:
            onKeyDown(te);
            break;
        }
        te.cancelBubble();
      }
    };
  }

  /**
   * Deselect a item.
   * 
   * @param item the item to be deselected
   */
  public void deselect(TreeItem item) {
    if (selItem == item) {
      deselectAll();
    }
  }

  /**
   * Deselects all selections.
   */
  public void deselectAll() {
    if (selItem != null) {
      selItem.getUI().onSelectedChange(false);
      selItem = null;
      TreeEvent te = new TreeEvent(tree);
      fireEvent(Events.SelectionChange, te);
      tree.fireEvent(Events.SelectionChange, te);
    }
  }

  /**
   * Returns the selected item.
   * 
   * @return the selected item or <code>null</code> if no selection
   */
  public TreeItem getSelected() {
    return selItem;
  }

  /**
   * Returns a array of selected items.
   * 
   * @return the selected items
   */
  public List<TreeItem> getSelection() {
    List<TreeItem> list = new ArrayList<TreeItem>();
    if (selItem != null) {
      list.add(selItem);
    }
    return list;
  }

  /**
   * Returns the model's tree.
   * 
   * @return the tree
   */
  public Tree getTree() {
    return tree;
  }

  /**
   * Binds the model to the specified tree.
   * 
   * @param tree the tree
   */
  public void init(Tree tree) {
    this.tree = tree;
    tree.addListener(Events.MouseDown, treeListener);
    tree.addListener(Events.KeyDown, treeListener);
  }

  /**
   * Returns <code>true</code> if the item is selected.
   * 
   * @param item the item
   * @return the selected state
   */
  public boolean isSelected(TreeItem item) {
    return selItem == item;
  }

  /**
   * Selects a item.
   * 
   * @param item the item to be selected
   */
  public void select(final TreeItem item) {
    if (isSelected(item)) {
      return;
    }
    if (!tree.isRendered()) {
      tree.addListener(Events.Render, new Listener<ComponentEvent>() {
        public void handleEvent(ComponentEvent ce) {
          removeListener(Events.Render, this);
          select(item);
        }
      });
      return;
    }
    TreeItem last = selItem != null ? selItem : lastSelItem;
    if (last != null) {
      last.getUI().onSelectedChange(false);
    }
    selItem = item;
    lastSelItem = item;

    if (!selItem.isRendered()) {
      ensureExpanded(selItem);
    }

    selItem.getUI().onSelectedChange(true);
    TreeEvent te = new TreeEvent(tree);
    te.item = selItem;
    fireEvent(Events.SelectionChange, te);
    tree.fireEvent(Events.SelectionChange, te);

    if (GXT.isSafari) {
      DeferredCommand.addCommand(new Command() {
        public void execute() {
          tree.focus();
        }
      });
    }

  }

  /**
   * Selects the item below the selected item in the tree, intelligently walking
   * the nodes.
   */
  public void selectNext() {
    TreeItem sel = selItem != null ? selItem : lastSelItem;
    if (sel == null) {
      return;
    }
    if (sel.firstChild() != null && sel.isExpanded()) {
      select(sel.firstChild());
    } else if (sel.nextSibling() != null) {
      select(sel.nextSibling());
    } else if (sel.getParentItem() != null) {
      TreeItem p = sel.getParentItem();
      while (p != null) {
        if (p.nextSibling() != null) {
          select(p.nextSibling());
          return;
        }
        p = p.getParentItem();
      }
    }
  }

  /**
   * Selects the item above the selected item in the tree, intelligently walking
   * the nodes.
   */
  public void selectPrevious() {
    TreeItem sel = selItem != null ? selItem : lastSelItem;
    if (sel == null) {
      return;
    }
    TreeItem prev = sel.previousSibling();
    if (prev != null) {
      if (!prev.isExpanded() || prev.getItemCount() < 1) {
        select(prev);
      } else {
        TreeItem lastChild = prev.lastChild();
        while (lastChild != null && lastChild.getItemCount() > 0) {
          lastChild = lastChild.lastChild();
        }
        select(lastChild);
      }
    } else if (sel.getParentItem() != null && !sel.getParentItem().isRoot()) {
      select(sel.getParentItem());
    }
  }

  protected void onItemClick(TreeItem item, ComponentEvent ce) {
    if (!ce.within(item.getUI().getJointEl()) && !ce.within(item.getUI().getCheckEl())) {
      if (isSelected(item) && ce.isControlKey()) {
        deselect(item);
      } else {
        select(item);
      }
    }
  }

  protected void onKeyDown(ComponentEvent ce) {
    TreeItem sel = selItem != null ? selItem : lastSelItem;
    if (sel == null) {
      return;
    }
    int key = ce.getKeyCode();
    switch (key) {
      case KeyboardListener.KEY_DOWN:
        ce.stopEvent();
        selectNext();
        break;
      case KeyboardListener.KEY_UP:
        ce.stopEvent();
        selectPrevious();
        break;
      case KeyboardListener.KEY_LEFT:
        ce.preventDefault();
        if (!sel.isLeaf() && sel.isExpanded()) {
          sel.setExpanded(false);
        } else if (sel.getParentItem() != null && !sel.getParentItem().isRoot()) {
          select(sel.getParentItem());
        }
        break;
      case KeyboardListener.KEY_RIGHT:
        ce.preventDefault();
        if (!sel.isLeaf()) {
          if (!sel.isExpanded()) {
            sel.setExpanded(true);
            return;
          }
        }
        selectNext();
        break;
    }
  }

  private void ensureExpanded(TreeItem item) {
    List<TreeItem> stack = new ArrayList<TreeItem>();
    item = item.getParentItem();
    while (item != null) {
      stack.add(item);
      if (item.isRendered()) {
        break;
      } item = item.getParentItem();
    }
    
    for (int i = stack.size() - 1; i >= 0; i--) {
      TreeItem ti = stack.get(i);
      ti.setExpanded(true);
    }
  }

}
