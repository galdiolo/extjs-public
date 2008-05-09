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
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.Items;
import com.extjs.gxt.ui.client.widget.selection.SelectionModel;

/**
 * Abstract base class for Tree selection models.
 */
public abstract class TreeSelectionModel implements SelectionModel<Tree>,
    Listener<TreeEvent> {

  protected Tree tree;
  protected KeyNav<TreeEvent> keyNav;
  protected TreeItem lastSelected;

  public void bind(Tree tree) {
    if (keyNav == null) {
      createKeyNav(tree);
    }
    if (this.tree != null) {
      this.tree.removeListener(Events.OnClick, this);
      this.tree.removeListener(Events.Remove, this);
      keyNav.bind(null);
    }
    this.tree = tree;
    if (tree != null) {
      tree.addListener(Events.OnClick, this);
      tree.addListener(Events.Remove, this);
      keyNav.bind(tree);
    }
  }

  public abstract List<TreeItem> doGetSelectedItems();

  public void handleEvent(TreeEvent e) {
    switch (e.type) {
      case Events.OnClick:
        onClick(e);
        break;
      case Events.Remove:
        onRemove(e.item);
        break;
    }
  }

  public abstract void refresh();

  public void select(TreeItem item) {
    doSelect(new Items(item));
  }

  /**
   * Selects the item below the selected item in the tree, intelligently walking
   * the nodes.
   */
  public void selectNext() {
    TreeItem next = next();
    if (next != null) {
      doSelect(new Items(next));
    }
  }

  /**
   * Selects the item above the selected item in the tree, intelligently walking
   * the nodes.
   */
  public void selectPrevious() {
    TreeItem previous = previous();
    if (previous != null) {
      doSelect(new Items(previous));
    }
  }

  protected void createKeyNav(Tree tree) {
    keyNav = new KeyNav<TreeEvent>() {

      @Override
      public void onDown(TreeEvent ce) {
        onKeyDown(ce);
      }

      @Override
      public void onLeft(TreeEvent ce) {
        onKeyLeft(ce);
      }

      @Override
      public void onRight(TreeEvent ce) {
        onKeyRight(ce);
      }

      @Override
      public void onUp(TreeEvent ce) {
        onKeyUp(ce);
      }

    };
  }

  protected abstract void doDeselect(Items<TreeItem> items, boolean supressEvent);

  protected void doSelect(Items items) {
    doSelect(items, false, false);
  }

  protected abstract void doSelect(Items<TreeItem> items, boolean keepExisting,
      boolean supressEvent);

  protected void ensureExpanded(TreeItem item) {
    List<TreeItem> stack = new ArrayList<TreeItem>();
    item = item.getParentItem();
    while (item != null) {
      if (!item.isRoot()) {
        stack.add(item);
      }
      item = item.getParentItem();
    }

    for (int i = stack.size() - 1; i >= 0; i--) {
      TreeItem ti = stack.get(i);
      ti.setExpanded(true);
    }
  }

  protected TreeItem next() {
    TreeItem sel = lastSelected;
    if (sel == null) {
      return null;
    }
    if (sel.firstChild() != null && sel.isExpanded()) {
      return sel.firstChild();
    } else if (sel.nextSibling() != null) {
      return sel.nextSibling();
    } else if (sel.getParentItem() != null) {
      TreeItem p = sel.getParentItem();
      while (p != null) {
        if (p.nextSibling() != null) {
          return p.nextSibling();
        }
        p = p.getParentItem();
      }
    }
    return null;
  }

  protected void onClick(TreeEvent e) {

  }

  protected void onKeyDown(TreeEvent e) {
    selectNext();
  }

  protected void onKeyLeft(TreeEvent e) {
    e.preventDefault();
    if (lastSelected == null) return;
    if (!lastSelected.isLeaf() && lastSelected.isExpanded()) {
      lastSelected.setExpanded(false);
    } else if (lastSelected.getParentItem() != null
        && !lastSelected.getParentItem().isRoot()) {
      doSelect(new Items(lastSelected.getParentItem()));
    }
  }

  protected void onKeyRight(TreeEvent e) {
    e.preventDefault();
    if (lastSelected == null) return;
    if (!lastSelected.isLeaf()) {
      if (!lastSelected.isExpanded()) {
        lastSelected.setExpanded(true);
        return;
      }
    }
    selectNext();
  }

  protected void onKeyUp(TreeEvent e) {
    selectPrevious();
  }

  protected void onRemove(TreeItem item) {

  }

  protected void onSelectChange(TreeItem item, boolean select) {
    if (select) {
      ensureExpanded(item);
    }
    item.getUI().onSelectedChange(select);
  }

  protected TreeItem previous() {
    TreeItem sel = lastSelected;
    if (sel == null) {
      return null;
    }
    TreeItem prev = sel.previousSibling();
    if (prev != null) {
      if (!prev.isExpanded() || prev.getItemCount() < 1) {
        return prev;
      } else {
        TreeItem lastChild = prev.lastChild();
        while (lastChild != null && lastChild.getItemCount() > 0
            && lastChild.isExpanded()) {
          lastChild = lastChild.lastChild();
        }
        return lastChild;
      }
    } else if (sel.getParentItem() != null && !sel.getParentItem().isRoot()) {
      return sel.getParentItem();
    }
    return null;
  }

}
