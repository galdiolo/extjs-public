/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;


import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.samples.resources.client.Music;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.Model;
import com.extjs.gxt.ui.client.data.TreeModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.viewer.SelectionChangedListener;
import com.extjs.gxt.ui.client.viewer.ModelComparator;
import com.extjs.gxt.ui.client.viewer.ModelLabelProvider;
import com.extjs.gxt.ui.client.viewer.ModelTreeContentProvider;
import com.extjs.gxt.ui.client.viewer.SelectionChangedEvent;
import com.extjs.gxt.ui.client.viewer.TreeViewer;
import com.extjs.gxt.ui.client.viewer.ViewerSorter;
import com.extjs.gxt.ui.client.widget.Button;
import com.extjs.gxt.ui.client.widget.ButtonBar;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class TreeViewerPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  private int count = 1;
  
  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    
    final Folder folder = TestData.getTreeModel();

    Tree tree = new Tree();
    tree.itemImageStyle = "icon-music";

    final TreeViewer viewer = new TreeViewer(tree);
    viewer.setContentProvider(new ModelTreeContentProvider());
    viewer.setLabelProvider(new ModelLabelProvider());
    viewer.setSorter(new ViewerSorter(new ModelComparator()));

    viewer.addSelectionListener(new SelectionChangedListener() {
      public void selectionChanged(SelectionChangedEvent se) {
        BaseTreeModel m = (BaseTreeModel) se.getSelection().getFirstElement();
        Info.display("Selection Changed", "{0} was selected", (String) m.get("name"));
      }
    });

    viewer.setInput(folder);

    ButtonBar buttonBar = new ButtonBar();
    
    buttonBar.add(new Button("Remove Selected", new SelectionListener() {
      public void componentSelected(ComponentEvent be) {
        if (!viewer.getSelection().isEmpty()) {
          TreeModel m = (BaseTreeModel) viewer.getSelection().getFirstElement();
          TreeModel p = m.getParent();
          if (p != null) {
            p.remove(m);
          }
        }
      }
    }));
    
    buttonBar.add(new Button("Add", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        Music music = new Music("New Music " + count++, "Unknown", "Unknown");
        TreeModel m = folder.getChild(0);

        // m will fire a add change event. The ModelContentProvider is
        // listening and will then instruct the viewer to add the new object.
        m.add(music);

        viewer.setExpanded(m, true);
      }
    }));

    buttonBar.add(new Button("Update", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        Model m = folder.getChild(0);
        m.set("name", "I have been updated");
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
