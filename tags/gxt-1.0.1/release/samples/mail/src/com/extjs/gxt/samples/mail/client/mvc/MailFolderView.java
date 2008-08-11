/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.binder.TreeBinder;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.data.TreeModelReader;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.tree.Tree;

public class MailFolderView extends View {

  private Tree tree;
  private TreeStore<Folder> store;
  private TreeBinder<Folder> binder;
  private TreeLoader<Folder> loader;

  public MailFolderView(Controller controller) {
    super(controller);
  }

  protected void initialize() {

  }

  protected void initUI() {
    ContentPanel west = (ContentPanel) Registry.get("west");
    west.setLayout(new AccordionLayout());

    ContentPanel mail = new ContentPanel();
    mail.setHeading("Mail");
    mail.addListener(Events.Expand, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent be) {
        Dispatcher.get().dispatch(AppEvents.NavMail);
      }
    });

    tree = new Tree();
    tree.setItemIconStyle("tree-folder");

    loader = new BaseTreeLoader(new TreeModelReader());
    store = new TreeStore<Folder>(loader);

    binder = new TreeBinder<Folder>(tree, store);
    binder.setDisplayProperty("name");
    binder.setAutoSelect(true);
    binder.addSelectionChangedListener(new SelectionChangedListener<ModelData>() {
      public void selectionChanged(SelectionChangedEvent se) {
        Folder f = (Folder) se.getSelection().get(0);
        AppEvent evt = new AppEvent(AppEvents.ViewMailItems, f);
        fireEvent(evt);
      }
    });

    mail.add(tree);

    west.add(mail);
  }

  protected void handleEvent(AppEvent event) {
    switch (event.type) {
      case AppEvents.Init:
        initUI();
        break;
    }

    if (event.type == AppEvents.NavMail) {
      Folder f = (Folder) event.data;
      if (f != null) {
        loader.addListener(Loader.Load, new LoadListener() {
          @Override
          public void loaderLoad(LoadEvent le) {
            loader.removeLoadListener(this);
          }
        });
        loader.load(f);
      }

    }
  }
}
