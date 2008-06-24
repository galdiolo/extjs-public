/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client;

import com.extjs.gxt.samples.explorer.client.model.Entry;
import com.extjs.gxt.samples.explorer.client.model.ExplorerModel;
import com.extjs.gxt.samples.explorer.client.mvc.AppController;
import com.extjs.gxt.samples.explorer.client.mvc.ContentController;
import com.extjs.gxt.samples.explorer.client.mvc.NavigationController;
import com.extjs.gxt.themes.client.Slate;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class Explorer implements EntryPoint {

  private Dispatcher dispatcher;
  private ExplorerModel model;

  public Explorer() {
    model = new ExplorerModel();
    Registry.register("model", model);
  }

  public void onModuleLoad() {
    ThemeManager.register(Slate.SLATE);

    ExplorerServiceAsync service = (ExplorerServiceAsync) GWT.create(ExplorerService.class);
    ServiceDefTarget endpoint = (ServiceDefTarget) service;
    String moduleRelativeURL = GWT.getModuleBaseURL() + "service";
    endpoint.setServiceEntryPoint(moduleRelativeURL);
    Registry.register("service", service);

    FileServiceAsync fileservice = (FileServiceAsync) GWT.create(FileService.class);
    endpoint = (ServiceDefTarget) fileservice;
    moduleRelativeURL = GWT.getModuleBaseURL() + "fileservice";
    endpoint.setServiceEntryPoint(moduleRelativeURL);
    Registry.register("fileservice", fileservice);

    dispatcher = Dispatcher.get();
    dispatcher.addController(new AppController());
    dispatcher.addController(new NavigationController());
    dispatcher.addController(new ContentController());
    dispatcher.dispatch(AppEvents.Init);

    String hash = Window.Location.getHash();

    showPage(model.findEntry("overview"));

    if (!"".equals(hash)) {
      hash = hash.substring(1);
      Entry entry = model.findEntry(hash);
      if (entry != null) {
        showPage(entry);
      }
    }
  }

  public static void showPage(Entry entry) {
    AppEvent appEvent = new AppEvent(AppEvents.ShowPage, entry);
    appEvent.historyEvent = true;
    appEvent.token = entry.getId();
    Dispatcher.forwardEvent(appEvent);
  }

}
