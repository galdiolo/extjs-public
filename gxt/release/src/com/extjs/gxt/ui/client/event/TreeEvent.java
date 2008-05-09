/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.event;

import java.util.List;

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
public class TreeEvent extends ComponentEvent {

  /**
   * The source tree.
   */
  public Tree tree;

  /**
   * The source tree item.
   */
  public TreeItem item;

  /**
   * The child item.
   */
  public TreeItem child;

  /**
   * The insert index.
   */
  public int index;
  
  public TreeItem selectedItem;
  
  public List<TreeItem> selected;

  public TreeEvent(Tree tree) {
    super(tree);
    this.tree = tree;
  }
  
  public TreeEvent(Tree tree, TreeItem item) {
    this(tree);
    this.item = item;
  }
}
