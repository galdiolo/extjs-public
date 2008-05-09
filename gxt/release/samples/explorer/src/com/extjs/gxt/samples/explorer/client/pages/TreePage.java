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
import com.extjs.gxt.ui.client.widget.Button;
import com.extjs.gxt.ui.client.widget.ButtonBar;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;

public class TreePage extends Container implements EntryPoint {

  public void onModuleLoad() {

  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    final Tree tree = new Tree();
    tree.setItemIconStyle("icon-music");

    // quick way to build tree from Model instances.
    TreeBuilder.buildTree(tree, TestData.getTreeModel());

    ButtonBar buttonBar = new ButtonBar();

    buttonBar.add(new Button("Expand All", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        tree.expandAll();
      }
    }));
    buttonBar.add(new Button("Collapse All", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        tree.collapseAll();
      }
    }));

    VerticalPanel vp = new VerticalPanel();
    vp.setSpacing(10);

    vp.add(buttonBar);
    vp.add(tree);

    setLayout(new FlowLayout());
    add(vp);
  }

}
