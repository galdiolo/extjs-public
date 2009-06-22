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
import com.extjs.gxt.samples.client.FileServiceAsync;
import com.extjs.gxt.samples.client.examples.model.FileModel;
import com.extjs.gxt.samples.client.examples.model.FolderModel;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.data.TreeLoader;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class AsyncTreePanelExample extends LayoutContainer {

  private TreeLoader<FileModel> loader;
  private TreePanel<FileModel> tree;

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);

    setLayout(new FlowLayout(10));

    final FileServiceAsync service = (FileServiceAsync) Registry.get(Examples.FILE_SERVICE);

    // data proxy
    RpcProxy<List<FileModel>> proxy = new RpcProxy<List<FileModel>>() {
      @Override
      protected void load(Object loadConfig, AsyncCallback<List<FileModel>> callback) {
        service.getFolderChildren((FileModel) loadConfig, callback);
      }
    };

    // tree loader
    loader = new BaseTreeLoader<FileModel>(proxy) {
      @Override
      public boolean hasChildren(FileModel parent) {
        return parent instanceof FolderModel;
      }
    };

    // trees store
    TreeStore<FileModel> store = new TreeStore<FileModel>(loader);
    store.setStoreSorter(new StoreSorter<FileModel>() {

      @Override
      public int compare(Store<FileModel> store, FileModel m1, FileModel m2,
          String property) {
        boolean m1Folder = m1 instanceof FolderModel;
        boolean m2Folder = m2 instanceof FolderModel;

        if (m1Folder && !m2Folder) {
          return -1;
        } else if (!m1Folder && m2Folder) {
          return 1;
        }

        return m1.getName().compareTo(m2.getName());
      }
    });

    tree = new TreePanel<FileModel>(store);
    tree.setDisplayProperty("name");
    tree.setIconProvider(new ModelIconProvider<FileModel>() {

      public AbstractImagePrototype getIcon(FileModel model) {
        if (!(model instanceof FolderModel)) {
          String ext = model.getName().substring(model.getName().lastIndexOf(".") + 1);

          // new feature, using image paths rather than style names
          if ("xml".equals(ext)) {
            return IconHelper.createPath("samples/images/icons/page_white_code.png");
          } else if ("java".equals(ext)) {
            return IconHelper.createPath("samples/images/icons/page_white_cup.png");
          } else if ("html".equals(ext)) {
            return IconHelper.createPath("samples/images/icons/html.png");
          } else {
            return IconHelper.createPath("samples/images/icons/page_white.png");
          }
        }
        return null;
      }
    });

    add(tree);
  }
}
