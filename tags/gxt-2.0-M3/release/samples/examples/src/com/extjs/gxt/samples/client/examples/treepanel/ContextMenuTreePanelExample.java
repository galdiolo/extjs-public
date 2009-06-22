/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.treepanel;

import java.util.List;

import com.extjs.gxt.samples.client.Examples;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.model.Folder;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.Element;

public class ContextMenuTreePanelExample extends LayoutContainer {
  private int count = 1;

  @SuppressWarnings("unchecked")
  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    final Folder model = TestData.getTreeModel();

    final TreeStore<ModelData> store = new TreeStore<ModelData>();
    store.add((List)model.getChildren(), true);

    final TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
    tree.setDisplayProperty("name");
    tree.getStyle().setLeafIcon(Examples.IMAGES.music());
    tree.setWidth(250);

    Menu contextMenu = new Menu();
    contextMenu.setWidth(130);

    MenuItem insert = new MenuItem();
    insert.setText("Insert Item");
    insert.setIcon(Examples.IMAGES.add());
    insert.addSelectionListener(new SelectionListener<MenuEvent>() {
      public void componentSelected(MenuEvent ce) {
        ModelData folder = tree.getSelectionModel().getSelectedItem();
        if (folder != null) {
          Folder child = new Folder("Add Child " + count++);
          store.add(folder, child, false);   
          tree.setExpanded(folder, true);
        }
      }
    });
    contextMenu.add(insert);

    MenuItem remove = new MenuItem();
    remove.setText("Remove Selected");
    remove.setIcon(Examples.IMAGES.delete());
    remove.addSelectionListener(new SelectionListener<MenuEvent>() {
      public void componentSelected(MenuEvent ce) {
        ModelData folder = tree.getSelectionModel().getSelectedItem();
        if (folder != null) {
          ModelData p = store.getParent(folder);
          if (p == null) {
            store.remove(folder);
          } else {
            store.remove(p, folder);
          }
        }
      }
    });
    contextMenu.add(remove);

    tree.setContextMenu(contextMenu);

    setLayout(new FlowLayout(8));
    add(tree);
  }
}
