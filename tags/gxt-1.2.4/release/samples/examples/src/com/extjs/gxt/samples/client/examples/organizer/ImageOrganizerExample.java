/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.organizer;

import java.util.List;

import com.extjs.gxt.samples.client.ExampleService;
import com.extjs.gxt.samples.client.ExampleServiceAsync;
import com.extjs.gxt.samples.client.examples.model.Photo;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.TreeDropTarget;
import com.extjs.gxt.ui.client.dnd.DND.Feedback;
import com.extjs.gxt.ui.client.dnd.DND.Operation;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.tree.Tree;
import com.extjs.gxt.ui.client.widget.tree.TreeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ImageOrganizerExample extends LayoutContainer {

  private Tree tree;

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);

    LayoutContainer container = new LayoutContainer();
    container.setStyleAttribute("margin", "20px");
    container.setSize(650, 300);
    container.setBorders(true);
    container.setLayout(new BorderLayout());

    ContentPanel west = new ContentPanel();
    west.setHeading("My Albums");

    ToolBar toolBar = new ToolBar();
    TextToolItem newAlbum = new TextToolItem("New Album", "icon-album");
    newAlbum.addSelectionListener(new SelectionListener<ComponentEvent>() {
      @Override
      public void componentSelected(ComponentEvent ce) {
        MessageBox.prompt("New Album", "Enter the new album name:", new Listener<WindowEvent>() {
          public void handleEvent(WindowEvent be) {
            MessageBoxEvent e = (MessageBoxEvent) be;
            if (be.buttonClicked.getItemId().equals(Dialog.OK) && e.value != null) {
              TreeItem album1 = new TreeItem(e.value);
              album1.setLeaf(false);
              album1.setIconStyle("icon-album");
              tree.getRootItem().add(album1);
            }
          }
        });
      }
    });
    toolBar.add(newAlbum);
    west.setTopComponent(toolBar);

    tree = new Tree();
    tree.getStyle().setLeafIconStyle("user");
    TreeItem album1 = new TreeItem("Album 1");
    album1.setLeaf(false);
    album1.setIconStyle("icon-album");
    tree.getRootItem().add(album1);
    west.add(tree);

    BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 200, 100, 300);
    westData.setMargins(new Margins(5, 0, 5, 5));
    westData.setSplit(true);
    container.add(west, westData);

    ContentPanel center = new ContentPanel();
    center.setHeading("My Images");
    center.setScrollMode(Scroll.AUTO);

    BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
    centerData.setMargins(new Margins(5));
    container.add(center, centerData);

    final ExampleServiceAsync service = (ExampleServiceAsync) GWT.create(ExampleService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) service;
    String moduleRelativeURL = GWT.getModuleBaseURL() + "service";
    endpoint.setServiceEntryPoint(moduleRelativeURL);

    RpcProxy proxy = new RpcProxy() {
      @Override
      protected void load(Object loadConfig, AsyncCallback callback) {
        service.getPhotos(callback);
      }
    };

    ListLoader loader = new BaseListLoader(proxy, new BeanModelReader());
    ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
    loader.load();

    ListView view = new ListView<BeanModel>() {
      @Override
      protected BeanModel prepareData(BeanModel model) {
        Photo photo = model.getBean();
        long size = photo.getSize() / 1000;
        model.set("shortName", Util.ellipse(photo.getName(), 15));
        model.set("sizeString", NumberFormat.getFormat("#0").format(size) + "k");
        model.set("dateString", DateTimeFormat.getMediumDateTimeFormat().format(photo.getDate()));
        return model;
      }
    };
    view.setId("img-chooser-view");
    view.setTemplate(getTemplate());
    view.setBorders(false);
    view.setStore(store);
    view.setItemSelector("div.thumb-wrap");

    center.add(view);

    new ListViewDragSource(view);

    TreeDropTarget target = new TreeDropTarget(tree) {

      @Override
      protected void handleAppendDrop(DNDEvent event, TreeItem item) {
        List<BeanModel> sel = (List) event.data;
        for (BeanModel bean : sel) {
          Photo photo = bean.getBean();
          TreeItem newItem = new TreeItem();
          newItem.setText(photo.getName().substring(0, photo.getName().indexOf(".")));
          item.add(newItem);
        }
      }

    };
    target.setOperation(Operation.COPY);
    target.setFeedback(Feedback.APPEND);

    add(container);
  }

  private native String getTemplate() /*-{
   return ['<tpl for=".">',
   '<div class="thumb-wrap" id="{name}" style="border: 1px solid white">',
   '<div class="thumb"><img src="{path}" title="{name}"></div>',
   '<span>{shortName}</span></div>',
   '</tpl>'].join("");
   }-*/;
}