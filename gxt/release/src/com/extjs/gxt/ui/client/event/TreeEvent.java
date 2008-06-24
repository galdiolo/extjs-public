/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;

/**
 * Tree event type.
 * 
 * <p/>Note: For a given event, only the fields which are appropriate will be
 * filled in. The appropriate fields for each event are documented by the event
 * source.
 * 
 * @see Tree
 */
public class TreeEvent extends ContainerEvent<Tree, TreeItem> {

  /**
   * The source tree.
   */
  public Tree tree;

  /**
   * The child item.
   */
  public TreeItem child;

  /**
   * Creates a new tree event.
   * 
   * @param tree the source tree
   */
  public TreeEvent(Tree tree) {
    super(tree);
    this.tree = tree;
  }

  /**
   * Creates a new tree event.
   * 
   * @param tree the source tree
   * @param item the tree item
   */
  public TreeEvent(Tree tree, TreeItem item) {
    super(tree, item);
    this.tree = tree;
  }
}
