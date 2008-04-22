/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import java.util.List;

import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.viewer.AsyncContentCallback;
import com.extjs.gxt.ui.client.viewer.TreeContentProvider;
import com.extjs.gxt.ui.client.viewer.ModelLabelProvider;
import com.extjs.gxt.ui.client.viewer.TreeViewer;
import com.extjs.gxt.ui.client.viewer.Viewer;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class AsyncTreeViewerPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    TreeContentProvider cp = new TreeContentProvider() {

      public Object getParent(Object element) {
        return ((TreeModel)element).getParent();
      }

      public boolean hasChildren(Object parent) {
        if (parent instanceof Folder) {
          return true;
        }
        return false;
      }

      public void getChildren(final Object parent,
          final AsyncContentCallback<Object> callback) {
        // emulate delay of rpc call
        Timer t = new Timer() {
          public void run() {
            callback.setElements((List)((TreeModel)parent).getChildren());
          }
        };
        t.schedule(1500);
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

      }

    };

    Folder folder = TestData.getTreeModel();

    Tree tree = new Tree();
    tree.itemImageStyle = "icon-music";

    TreeViewer viewer = new TreeViewer(tree);
    viewer.setContentProvider(cp);
    viewer.setLabelProvider(new ModelLabelProvider());
    viewer.setInput(folder);

    setLayout(new FlowLayout());
    add(tree);
  }

}
