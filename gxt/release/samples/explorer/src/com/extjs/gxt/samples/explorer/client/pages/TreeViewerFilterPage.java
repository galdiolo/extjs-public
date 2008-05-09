/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.viewer.ModelLabelProvider;
import com.extjs.gxt.ui.client.viewer.ModelTreeContentProvider;
import com.extjs.gxt.ui.client.viewer.TreeViewer;
import com.extjs.gxt.ui.client.viewer.ViewerFilterField;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class TreeViewerFilterPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    Tree tree = new Tree();
    tree.setItemIconStyle("icon-music");

    TreeViewer viewer = new TreeViewer(tree);
    viewer.setContentProvider(new ModelTreeContentProvider());
    viewer.setLabelProvider(new ModelLabelProvider());
    
    ViewerFilterField filterField = new ViewerFilterField<Model, Model>() {

      @Override
      protected boolean doSelect(Model parent, Model element, String filter) {
        // only match leaf nodes
        if (element instanceof Folder) {
          return false;
        }
        String name = element.get("name").toString().toLowerCase();
        if (name.startsWith(filter.toLowerCase())) {
          return true;
        }
        return false;
      }

    };
    filterField.bind(viewer);

    viewer.setInput(TestData.getTreeModel(), true);

    VerticalPanel panel = new VerticalPanel();
    panel.addStyleName("x-small-editor");
    panel.setSpacing(8);

    panel.add(new Text("Enter a search string such as 'vio'"));
    panel.add(filterField);
    panel.add(tree);

    add(panel);
  }

}
