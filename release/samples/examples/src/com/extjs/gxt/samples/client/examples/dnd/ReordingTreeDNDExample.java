/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.dnd;

import com.extjs.gxt.samples.client.Examples;
import com.extjs.gxt.samples.client.ExamplesModel;
import com.extjs.gxt.samples.client.examples.model.Category;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.dnd.TreeDragSource;
import com.extjs.gxt.ui.client.dnd.TreeDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.DNDListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.user.client.Element;

public class ReordingTreeDNDExample extends LayoutContainer {

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);

    LayoutContainer container = new LayoutContainer();
    container.setSize(280, 400);
    container.setBorders(true);
    container.setScrollMode(Scroll.AUTO);

    setStyleAttribute("margin", "10px");

    TreeStore store = new TreeStore();
    TreeModel root = (ExamplesModel) Registry.get(Examples.MODEL);
    root.set("name", "Ext GWT");
    store.add(root, true);

    final Tree tree = new Tree();
    tree.getStyle().setLeafIconStyle("icon-list");

    TreeBinder binder = new TreeBinder(tree, store) {
      @Override
      public boolean hasChildren(ModelData parent) {
        if ("My Files".equals(parent.get("name")) || parent instanceof Category) {
          return true;
        }
        return super.hasChildren(parent);
      }
    };
    binder.setAutoLoad(true);
    binder.setDisplayProperty("name");
    binder.init();

    tree.getRootItem().getItem(0).setExpanded(true);

    TreeDragSource source = new TreeDragSource(binder);
    source.addDNDListener(new DNDListener() {
      @Override
      public void dragStart(DNDEvent e) {
        TreeItem item = tree.findItem(e.getTarget());
        if (item != null && item == tree.getRootItem().getItem(0) && tree.getRootItem().getItemCount() == 1) {
          e.doit = false;
          e.status.setStatus(false);
          return;
        }
        super.dragStart(e);
      }
    });

    TreeDropTarget target = new TreeDropTarget(binder);
    target.setAllowSelfAsSource(true);
    target.setFeedback(Feedback.BOTH);

    container.add(tree);
    add(container);
  }

}
