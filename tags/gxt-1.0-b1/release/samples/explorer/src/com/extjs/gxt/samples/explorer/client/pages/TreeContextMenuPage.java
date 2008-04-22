/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.TreeBuilder;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.TextMenuItem;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class TreeContextMenuPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  private int count = 1;

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    final Tree tree = new Tree();
    tree.itemImageStyle = "icon-music";

    // quick way to build tree from Model instances.
    TreeBuilder.buildTree(tree, TestData.getTreeModel());

    Menu contextMenu = new Menu();
    contextMenu.setWidth(130);

    TextMenuItem insert = new TextMenuItem();
    insert.setText("Insert Item");
    insert.setIconStyle("icon-add");
    insert.addSelectionListener(new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        TreeItem item = tree.getSelectedItem();
        if (item != null) {
          TreeItem newItem = new TreeItem();
          newItem.setText("Add Child " + count++);
          item.add(newItem);
          item.setExpanded(true);
        }
      }
    });
    contextMenu.add(insert);

    TextMenuItem remove = new TextMenuItem();
    remove.setText("Remove Selected");
    remove.setIconStyle("icon-delete");
    remove.addSelectionListener(new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        TreeItem item = tree.getSelectedItem();
        if (item != null) {
          item.getParentItem().remove(item);
        }
      }
    });
    contextMenu.add(remove);

    tree.setContextMenu(contextMenu);

    setLayout(new FlowLayout(8));
    add(tree);
  }

}
