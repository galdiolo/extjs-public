/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;


import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.TreeEvent;
import com.extjs.gxt.ui.client.util.TreeBuilder;
import com.extjs.gxt.ui.client.widget.Button;
import com.extjs.gxt.ui.client.widget.ButtonBar;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.tree.SingleTreeSelectionModel;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class CheckBoxTreePage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  private String path;

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    final Tree<SingleTreeSelectionModel> tree = new Tree();
    tree.setCheckable(true);
    tree.setItemIconStyle("icon-music");
    tree.addListener(Events.SelectionChange, new Listener<TreeEvent>() {
      public void handleEvent(TreeEvent te) {
        TreeItem item = te.selectedItem;
        Info.display("Selection Changed", "The '{0}' item was selected", item.getText());
      }
    });

    // quick way to build tree from Model instances.
    TreeBuilder.buildTree(tree, TestData.getTreeModel());

    ButtonBar buttonBar = new ButtonBar();
    buttonBar.add(new Button("Copy Selected Path", new SelectionListener<TreeEvent>() {
      public void componentSelected(TreeEvent ce) {
        TreeItem item =  ce.selectedItem;
        if (item == null) return;
        path = item.getPath();
        String[] ids = path.split(",");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
          TreeItem ti = tree.getItemById(ids[i]);
          sb.append("/" + ti.getText());
        }
        Info.display("Get Path", "The current path is {0}", sb.toString().substring(1));
      }
    }));
    buttonBar.add(new Button("Restore Path", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        tree.expandPath(path);
      }
    }));

    buttonBar.add(new Button("Get Checked", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        StringBuffer sb = new StringBuffer();
        for (TreeItem item : tree.getChecked()) {
          sb.append(", " + item.getText());
        }
        String s = sb.toString();
        if (s.length() > 1) s = s.substring(2);
        if (s.length() > 100) s = s.substring(0, 100) + "...";
        Info.display("Checked Items", s, "");
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
