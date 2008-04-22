/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A standard hierarchical tree widget. The tree contains a hierarchy of
 * <code>TreeItems</code> that the user can open, close, and select.
 * 
 * <dt><b>Events:</b></dt>
 * 
 * <dd><b>BeforeAdd</b> : TreeEvent(item, child, index)<br>
 * <div>Fires before a item is added or inserted. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>item : this</li>
 * <li>child : the item being added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeRemove</b> : TreeEvent(item, child)<br>
 * <div>Fires before a item is removed. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the action.</div>
 * <ul>
 * <li>item : this</li>
 * <li>child : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeExpand</b> : TreeEvent(item)<br>
 * <div>Fires before a item is expanded. Listeners can set the <code>doit</code>
 * field to <code>false</code> to cancel the expand.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>BeforeCollapse</b> : TreeEvent(item)<br>
 * <div>Fires before a item is collapsed. Listeners can set the
 * <code>doit</code> field to <code>false</code> to cancel the collapse.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Add</b> : TreeEvent(item, child, index)<br>
 * <div>Fires after a item has been added or inserted.</div>
 * <ul>
 * <li>item : this</li>
 * <li>child : the item that was added</li>
 * <li>index : the index at which the item will be added</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Remove</b> : TreeEvent(item, child)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>item : this</li>
 * <li>child : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : TreeEvent(item)<br>
 * <div>Fires after a item has been expanded.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : TreeEvent(item)<br>
 * <div>Fires ater a item is collapsed.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : TreeEvent(item)<br>
 * <div>Fires after a check state change.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : ComponentEvent(component)<br>
 * <div>Fires before the tree's context menu is shown.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>MouseOver</b> : TreeEvent(item, event)<br>
 * <div>Fires when the mouse is over an item.</div>
 * <ul>
 * <li>item : active item</li>
 * <li>event : dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>MouseOut</b> : TreeEvent(item, event)<br>
 * <div>Fires when the mouse leaves an item.</div>
 * <ul>
 * <li>item : active item</li>
 * <li>event : dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>MouseDown</b> : TreeEvent(item, event)<br>
 * <div>Fires when the mouse is pressed.</div>
 * <ul>
 * <li>item : active item</li>
 * <li>event : dom event</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>KeyDown</b> : TreeEvent(item, event)<br>
 * <div>Fires when the key is pressed.</div>
 * <ul>
 * <li>item : the active item</li>
 * <li>event : dom event</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-tree (the tree itself)</dd>
 * <dd>.my-tree-item-text span (the tree item text)</dd>
 * </dl>
 */
public class Tree extends BoxComponent {

  /**
   * Check cascade enum.
   */
  public enum CheckCascade {
    NONE, CHILDREN, PARENTS;
  }

  /**
   * Check nodes enum.
   */
  public enum CheckNodes {
    PARENT, LEAF, BOTH;
  }

  /**
   * Sets the tree selection mode (defaults to SINGLE). Valid values are SINGLE
   * and MULTI.
   */
  public SelectionMode selectionMode = SelectionMode.SINGLE;

  /**
   * True to animate tree item expand and collapse (defaults to true).
   */
  public boolean animate = true;

  /**
   * True for a check box tree (defaults to false).
   */
  public boolean checkable;

  /**
   * The global icon style for leaf tree items (defaults to null). Individual
   * tree items can override this value by setting the the item's icon style.
   */
  public String itemImageStyle;

  /**
   * The global icon style for tree items with children (defaults to
   * 'tree-folder'). Individual tree items can override this value by setting
   * the the item's icon style.
   */
  public String nodeImageStyle = "tree-folder";

  /**
   * The global icon style for expanded tree items (defaults to
   * 'tree-folder-open'). Individual tree items can override this value by
   * setting the the item's icon style.
   */
  public String openNodeImageStyle = "tree-folder-open";

  /**
   * Sets which tree items will display a check box (defaults to BOTH).
   * <p>
   * Valid values are:
   * <ul>
   * <li>BOTH - both nodes and leafs</li>
   * <li>PARENT - only nodes with children</li>
   * <li>LEAF - only leafs</li>
   * </ul>
   */
  public CheckNodes checkNodes = CheckNodes.BOTH;

  /**
   * Sets the cascading behavior for check tree (defaults to PARENTS).
   * <p>
   * Valid values are:
   * <ul>
   * <li>NONE - no cascading</li>
   * <li>PARENTS - cascade to parents</li>
   * <li>CHILDREN - cascade to children</li>
   * </ul>
   */
  public CheckCascade checkStyle = CheckCascade.PARENTS;

  protected TreeItem root;
  protected TreeSelectionModel sm;
  protected boolean isViewer;

  private int indentWidth = 18;
  private Map<String, TreeItem> nodeHash;

  /**
   * Creates a new single select tree.
   */
  public Tree() {
    focusable = true;
    createRootItem();
    root.root = true;
    nodeHash = new HashMap<String, TreeItem>();
  }

  /**
   * Collapses all item's.
   */
  public void collapseAll() {
    root.setExpanded(false, true);
  }

  /**
   * Deselects a item.
   * 
   * @param item the item to be deselected
   */
  public void deselect(TreeItem item) {
    getSelectionModel().deselect(item);
  }

  /**
   * Deselects all selections.
   */
  public void deselectAll() {
    getSelectionModel().deselectAll();
  }

  /**
   * Expands all item's.
   */
  public void expandAll() {
    root.setExpanded(true, true);
  }

  /**
   * Expands a specified path. A path can be retrieved from a tree item with
   * {@link TreeItem#getPath()}.
   * 
   * @param path the path to expand
   * @return <code>true</code> if all paths expanded
   */
  public boolean expandPath(String path) {
    String[] ids = path.split(",");
    if (ids.length == 0) return false;
    if (ids[0].equals(root.getId())) {
      root.setExpanded(true);

      TreeItem current = root;
      for (int i = 1; i < ids.length; i++) {
        String id = ids[i];
        boolean match = false;
        for (int j = 0; j < current.getItemCount(); j++) {
          TreeItem child = current.getItem(j);
          if (!match && child.getId().equals(id)) {
            child.setExpanded(true);
            current = child;
            match = true;
            break;
          }
        }
        if (!match) {
          return false;
        }
      }

    }
    return true;
  }

  /**
   * Returns the tree whose element or child elements match the passed target.
   * 
   * @param element the target element
   * @return the matching tree item or <code>null</code> if no match
   */
  public TreeItem findItem(Element element) {
    El elem = fly(element).findParent(".my-treeitem", 15);
    if (elem != null) {
      String id = elem.getId();
      if (id != null && !id.equals("")) {
        TreeItem item = getItemById(id);
        return item;
      }
    }
    return null;
  }

  /**
   * Returns all tree item's contained by the tree.
   * 
   * @return all tree item's
   */
  public List<TreeItem> getAllItems() {
    List<TreeItem> temp = new ArrayList<TreeItem>();
    temp.add(root);
    Iterator<TreeItem> it = nodeHash.values().iterator();
    while (it.hasNext()) {
      temp.add(it.next());
    }
    return temp;
  }

  /**
   * Returns a list of id's for all checked items.
   * 
   * @return the list of checked id's
   */
  public List<TreeItem> getChecked() {
    List<TreeItem> list = new ArrayList<TreeItem>();
    Iterator<TreeItem> it = nodeHash.values().iterator();
    while (it.hasNext()) {
      TreeItem item = it.next();
      if (item.isChecked()) {
        list.add(item);
      }
    }
    return list;
  }

  public Menu getContextMenu() {
    // made public
    return super.getContextMenu();
  }

  /**
   * Returns the indent width.
   * 
   * @return the indent width
   */
  public int getIndentWidth() {
    return indentWidth;
  }

  /**
   * Returns the item by id.
   * 
   * @param id the id of the element to return
   * @return the item
   */
  public TreeItem getItemById(String id) {
    return nodeHash.get(id);
  }

  /**
   * Returns the tree's root item.
   * 
   * @return the root item
   */
  public TreeItem getRootItem() {
    return root;
  }

  /**
   * Returns the selected item.
   * 
   * @return the selected item or <code>null</code> if no selection
   */
  public TreeItem getSelectedItem() {
    return getSelectionModel().getSelected();
  }

  /**
   * Returns an array of selected items.
   * 
   * @return the selected items
   */
  public List<TreeItem> getSelection() {
    ArrayList<TreeItem> sel = new ArrayList<TreeItem>();
    if (sm == null) {
      return sel;
    }
    if (sm != null && sm instanceof MultiSelectionModel) {
      sel.addAll(sm.getSelection());
    } else {
      TreeItem item = getSelectedItem();
      if (item != null) {
        sel.add(item);
      }
    }
    return sel;
  }

  /**
   * Returns the selection mode.
   * 
   * @return the selection mode
   */
  public SelectionMode getSelectionMode() {
    return selectionMode;
  }

  /**
   * Returns the tree's selection model.
   * 
   * @return the selection model
   */
  public TreeSelectionModel getSelectionModel() {
    if (sm == null) {
      if (selectionMode == SelectionMode.SINGLE) {
        sm = new TreeSelectionModel();
      } else {
        sm = new MultiSelectionModel();
      }
    }
    return sm;
  }

  public void onComponentEvent(ComponentEvent ce) {

  }

  public void onBrowserEvent(Event event) {
    int type = DOM.eventGetType(event);

    // hack to receive keyboard events in safari
    if (GXT.isSafari && type == Event.ONKEYDOWN) {
      if (sm.getSelected() != null) {
        TreeEvent te = new TreeEvent(this);
        te.event = event;
        sm.onKeyDown(te);
        return;
      }
    }

    if (type == Event.ONMOUSEUP) {
      if (DOM.eventGetButton(event) == Event.BUTTON_RIGHT
          || (GXT.isMac && DOM.eventGetCtrlKey(event))) {
        super.onBrowserEvent(event);
        return;
      }
    }

    if (!disabled) {
      TreeItem item = findItem(DOM.eventGetTarget(event));
      if (item != null) {
        item.onBrowserEvent(event);
      }
    }
  }

  public void setContextMenu(Menu menu) {
    super.setContextMenu(menu);
  }

  /**
   * Sets the number of pixels child items are indented. Default value is 18.
   * 
   * @param indentWidth the indent width
   */
  public void setIndentWidth(int indentWidth) {
    this.indentWidth = indentWidth;
  }

  /**
   * Sets the selection to the tree items. The current selection is cleared
   * before the new items are selected. If the tree is single-select then all
   * items are ignored.
   * 
   * 
   * @param selected the items to select
   */
  public void setSelection(List selected) {
    if (getSelectionModel() instanceof MultiSelectionModel) {
      ((MultiSelectionModel) sm).selectItems(selected);
    }
  }

  /**
   * Sets the selection to the item. The current selection is cleared before the
   * new items are selected.
   * 
   * @param item the item to select
   */
  public void setSelection(TreeItem item) {
    getSelectionModel().select(item);
  }

  /**
   * Sets the selection mode for the list. Calling after the tree has been
   * rendered will have no effect.
   * 
   * @param selectionMode the selection mode
   */
  public void setSelectionMode(SelectionMode selectionMode) {
    if (!isRendered()) {
      this.selectionMode = selectionMode;
    }
  }

  protected void createRootItem() {
    root = new RootTreeItem(this);
    root.tree = this;
  }

  protected void initSelectionModel() {
    if (selectionMode == SelectionMode.MULTI) {
      sm = new MultiSelectionModel();
    } else {
      sm = new TreeSelectionModel();
    }
    sm.init(this);
  }

  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv(), target, index);
    setStyleName("my-tree");

    if (selectionMode == SelectionMode.MULTI) {
      sm = new MultiSelectionModel();
    } else {
      sm = new TreeSelectionModel();
    }

    this.sm.init(this);

    root.render(getElement());

    if (!root.childrenRendered) {
      root.renderChildren();
    }

    disableTextSelection(true);
    sinkEvents(Event.ONCLICK | Event.ONDBLCLICK | Event.KEYEVENTS | Event.MOUSEEVENTS);
  }

  protected void onRightClick(ComponentEvent ce) {
    TreeItem item = findItem(ce.getTarget());
    if (selectionMode == SelectionMode.SINGLE) {
      if (item != null) {
        setSelection(item);
      }
    } else {
      if (item != null && !sm.isSelected(item)) {
        setSelection(item);
      }
    }
    super.onRightClick(ce);
  }

  void registerItem(TreeItem item) {
    nodeHash.put(item.getId(), item);
  }

  void unregisterItem(TreeItem item) {
    int count = item.getItemCount();
    for (int i = 0; i < count; i++) {
      unregisterItem(item.getItem(i));
    }
    nodeHash.remove(item.getId());
  }
}
