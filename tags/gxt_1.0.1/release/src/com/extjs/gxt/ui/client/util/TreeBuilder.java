/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;


import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Various ways of populating trees.
 * 
 * @see Tree
 */
public class TreeBuilder {
  
  /**
   * Populates a tree from existing dom elements. The tree item text is taken
   * from the 'title' attribute of the element.
   * 
   * @param tree the tree
   * @param root the root element
   */
  public static void buildTree(Tree tree, Element root) {
    process(tree.getRootItem(), root);
  }

  /**
   * Populates a tree from the given model.
   * 
   * @param tree the tree
   * @param model the model
   */
  public static void buildTree(Tree tree, TreeModel model) {
    TreeItem root = tree.getRootItem();
    for (int i = 0; i < model.getChildCount(); i++) {
      TreeModel m = model.getChild(i);
      TreeItem item = new TreeItem();
      WidgetHelper.setModel(item, m);
      item.setText(m.toString());
      root.add(item);
      process(item, m);
    }
  }

  private static void process(TreeItem parentItem, TreeModel model) {
    for (int i = 0; i < model.getChildCount(); i++) {
      TreeModel m = model.getChild(i);
      TreeItem item = new TreeItem();
      item.setText(m.toString());
      WidgetHelper.setModel(item, m);
      parentItem.add(item);
      process(item, m);
    }
  }

  private static void process(TreeItem item, Element parent) {
    int size = DOM.getChildCount(parent);
    for (int i = 0; i < size; i++) {
      Element li = DOM.getChild(parent, i);
      TreeItem childItem = new TreeItem();
      String id = DOM.getElementAttribute(li, "id");
      if (id != null && !id.equals("")) {
        childItem.setId(id);
      }
      childItem.setText(DOM.getElementProperty(li, "title"));
      item.add(childItem);
      for (int j = 0; j < DOM.getChildCount(li); j++) {
        Element subList = DOM.getChild(li, j);
        process(childItem, subList);
      }
    }
  }

}
