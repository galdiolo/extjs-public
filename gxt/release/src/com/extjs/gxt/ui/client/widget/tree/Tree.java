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

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.selection.Selectable;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A standard hierarchical tree widget. The tree contains a hierarchy of
 * <code>TreeItems</code> that the user can open, close, and select.
 * 
 * <p/>The root item cannot be displayed.
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
 * <dd><b>Remove</b> : TreeEvent(tree, item, child)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>tree : this</li>
 * <li>item : item</li>
 * <li>child : the item being removed</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>SelectionChange</b> : TreeEvent(tree, selected)<br>
 * <div>Fires after a item has been removed.</div>
 * <ul>
 * <li>tree : this</li>
 * <li>selected : the selected items</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Expand</b> : TreeEvent(tree, item)<br>
 * <div>Fires after a item has been expanded.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>Collapse</b> : TreeEvent(tree, item)<br>
 * <div>Fires ater a item is collapsed.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>CheckChange</b> : TreeEvent(tree, item)<br>
 * <div>Fires after a check state change.</div>
 * <ul>
 * <li>item : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>ContextMenu</b> : TreeEvent(tree)<br>
 * <div>Fires before the tree's context menu is shown.</div>
 * <ul>
 * <li>component : this</li>
 * </ul>
 * </dd>
 * 
 * <dd><b>KeyPress</b> : TreeEvent(tree, event)<br>
 * <div>Fires when a key is pressed.</div>
 * <ul>
 * <li>event : dom event</li>
 * </ul>
 * </dd>
 * 
 * <dt><b>CSS:</b></dt>
 * <dd>.my-tree (the tree itself)</dd>
 * <dd>.my-tree-item-text span (the tree item text)</dd>
 * </dl>
 */
public class Tree extends Container<TreeItem> implements Selectable<TreeItem> {

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

  protected TreeItem root;
  protected TreeSelectionModel sm;
  protected boolean isViewer;
  private String openNodeIconStyle = "tree-folder-open";
  private String nodeIconStyle = "tree-folder";
  private String itemStyle = "my-treeitem";
  private String itemImageStyle;
  private CheckCascade checkStyle = CheckCascade.PARENTS;
  private CheckNodes checkNodes = CheckNodes.BOTH;
  private boolean animate = true;
  private boolean checkable;
  private int indentWidth = 18;
  private Map<String, TreeItem> nodeHash;

  /**
   * Creates a new single select tree.
   */
  public Tree() {
    attachChildren = false;
    baseStyle = "my-tree";
    focusable = true;
    createRootItem();
    root.root = true;
    nodeHash = new HashMap<String, TreeItem>();
    setSelectionModel(new TreeSelectionModel());
  }

  /**
   * Collapses all item's.
   */
  public void collapseAll() {
    boolean anim = animate;
    if (anim) animate = false;
    root.setExpanded(false, true);
    if (anim) animate = true;
  }
  
  /**
   * Expands all item's.
   */
  public void expandAll() {
    boolean anim = animate;
    if (anim) animate = false;
    root.setExpanded(true, true);
    if (anim) animate = true;
  }

  /**
   * Expands a specified path. A path can be retrieved from a tree item with
   * {@link TreeItem#getPath()}.
   * 
   * @param path the path to expand
   * @return <code>true</code> if all paths expanded
   */
  public boolean expandPath(String path) {
    if (path == null) return false;
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
    Element elem = fly(element).findParentElement("." + itemStyle, 15);
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
    temp.addAll(nodeHash.values());
    return temp;
  }

  /**
   * Returns true if animations are enabled.
   * 
   * @return the animate state
   */
  public boolean getAnimate() {
    return animate;
  }

  /**
   * Returns true if check boxs are enabled.
   * 
   * @return the checkbox state
   */
  public boolean getCheckable() {
    return checkable;
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

  /**
   * Returns the child nodes value.
   * 
   * @return the child nodes value
   */
  public CheckNodes getCheckNodes() {
    return checkNodes;
  }

  /**
   * The check style value.
   * 
   * @return the check style
   */
  public CheckCascade getCheckStyle() {
    return checkStyle;
  }

  @Override
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

  @Override
  public TreeItem getItem(int index) {
    return getRootItem().getItem(index);
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
   * Returns the item icon style.
   * 
   * @return the icon style
   */
  public String getItemIconStyle() {
    return itemImageStyle;
  }

  /**
   * Returns the node icon style.
   * 
   * @return the icon style
   */
  public String getNodeIconStyle() {
    return nodeIconStyle;
  }

  /**
   * Returns the open node icon style.
   * 
   * @return the icon style
   */
  public String getOpenNodeIconStyle() {
    return openNodeIconStyle;
  }

  /**
   * Returns the tree's root item. The root item cannot be displayed.
   * 
   * @return the root item
   */
  public TreeItem getRootItem() {
    return root;
  }

  /**
   * Returns the selected item.
   * 
   * @return the item
   */
  public TreeItem getSelectedItem() {
    return (TreeItem) sm.getSelectedItem();
  }

  /**
   * Returns the selected items.
   * 
   * @return the selected items
   */
  public List<TreeItem> getSelectedItems() {
    return sm.getSelectedItems();
  }

  public SelectionMode getSelectionMode() {
    return sm.getSelectionMode();
  }

  /**
   * Returns the tree's selection model.
   * 
   * @return the selection model
   */
  public TreeSelectionModel getSelectionModel() {
    return sm;
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {
    super.onComponentEvent(ce);
    TreeEvent te = (TreeEvent) ce;
    if (te.item != null) {
      te.item.onComponentEvent(te);
    }
  }

  public void onSelectChange(TreeItem item, boolean select) {
    item.getUI().onSelectedChange(select);
  }

  @Override
  public boolean removeAll() {
    getRootItem().removeAll();
    nodeHash.clear();
    return true;
  }

  /**
   * Sets whether expand /collapse should be animated (defaults to true).
   * 
   * @param animate the animate state
   */
  public void setAnimate(boolean animate) {
    this.animate = animate;
  }

  /**
   * Sets whether checkboxes are used in the tree.
   * 
   * @param checkable true for checkboxes
   */
  public void setCheckable(boolean checkable) {
    this.checkable = checkable;
  }

  /**
   * Sets which tree items will display a check box (defaults to BOTH).
   * <p>
   * Valid values are:
   * <ul>
   * <li>BOTH - both nodes and leafs</li>
   * <li>PARENT - only nodes with children</li>
   * <li>LEAF - only leafs</li>
   * </ul>
   * 
   * @param checkNodes the child nodes value
   */
  public void setCheckNodes(CheckNodes checkNodes) {
    this.checkNodes = checkNodes;
  }

  /**
   * Sets the cascading behavior for check tree (defaults to PARENTS).
   * <p>
   * Valid values are:
   * <ul>
   * <li>NONE - no cascading</li>
   * <li>PARENTS - cascade to parents</li>
   * <li>CHILDREN - cascade to children</li>
   * </ul>
   * 
   * @param checkStyle the child style
   */
  public void setCheckStyle(CheckCascade checkStyle) {
    this.checkStyle = checkStyle;
  }

  @Override
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
   * Sets the global icon style for leaf tree items. Individual tree items can
   * override this value by setting the the item's icon style.
   * 
   * @param itemImageStyle the image style
   */
  public void setItemIconStyle(String itemImageStyle) {
    this.itemImageStyle = itemImageStyle;
  }

  /**
   * The global icon style for tree items with children (defaults to
   * 'tree-folder'). Individual tree items can override this value by setting
   * the the item's icon style.
   * 
   * @param nodeIconStyle the node icon style
   */
  public void setNodeIconStyle(String nodeIconStyle) {
    this.nodeIconStyle = nodeIconStyle;
  }

  /**
   * Sets the global icon style for expanded tree items (defaults to
   * 'tree-folder-open'). Individual tree items can override this value by
   * setting the the item's icon style.
   * 
   * @param openNodeIconStyle the open node icon style
   */
  public void setOpenNodeIconStyle(String openNodeIconStyle) {
    this.openNodeIconStyle = openNodeIconStyle;
  }

  public void setSelectedItem(TreeItem item) {
    sm.select(item);
  }

  public void setSelectedItems(List<TreeItem> items) {
    sm.select(items);
  }

  /**
   * Sets the table's selection mode.
   * 
   * @param mode the selection mode
   */
  public void setSelectionMode(SelectionMode mode) {
    setSelectionModel(new TreeSelectionModel(mode));
  }

  /**
   * Sets the tree's selection model.
   * 
   * @param sm the tree selection model
   */
  public void setSelectionModel(TreeSelectionModel sm) {
    assert sm != null;
    if (this.sm != null) {
      this.sm.bind(null);
    }
    this.sm = sm;
    sm.bind(this);
  }

  @Override
  protected ComponentEvent createComponentEvent(Event event) {
    return new TreeEvent(this, event == null ? null : findItem(DOM.eventGetTarget(event)));
  }

  @Override
  protected ContainerEvent createContainerEvent(TreeItem item) {
    return new TreeEvent(this, item);
  }

  protected void createRootItem() {
    root = new RootTreeItem(this);
    root.tree = this;
  }

  @Override
  protected void onRender(Element target, int index) {
    super.onRender(target, index);
    setElement(DOM.createDiv(), target, index);

    root.render(getElement());

    if (!root.childrenRendered) {
      root.renderChildren();
    }

    disableTextSelection(true);
    el().addEventsSunk(Event.ONCLICK | Event.ONDBLCLICK | Event.KEYEVENTS | Event.MOUSEEVENTS);
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
