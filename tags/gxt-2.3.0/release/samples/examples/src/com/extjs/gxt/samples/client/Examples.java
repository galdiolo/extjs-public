/*
 * Sencha GXT 2.3.0 - Sencha for GWT
 * Copyright(c) 2007-2013, Sencha, Inc.
 * licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
 package com.extjs.gxt.samples.client;

import java.util.Map;

import com.extjs.gxt.samples.client.examples.model.Category;
import com.extjs.gxt.samples.client.examples.model.Entry;
import com.extjs.gxt.themes.client.Access;
import com.extjs.gxt.themes.client.Slate;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class Examples implements EntryPoint {

  public static boolean isExplorer() {
    String test = Window.Location.getPath();
    if (test.indexOf("pages") != -1) {
      return false;
    }
    return true;
  }

  public static final String SERVICE = GWT.getModuleBaseURL() + "service";
  public static final String FILE_SERVICE = GWT.getModuleBaseURL() + "fileservice";
  public static final String MODEL = "model";

  public void onModuleLoad() {
    ThemeManager.register(Slate.SLATE);
    ThemeManager.register(Access.ACCESS);

    String name = GWT.getModuleName();
    if (!"com.extjs.gxt.samples.Examples".equals(name)) {
      return;
    }
    ExampleServiceAsync service = (ExampleServiceAsync) GWT.create(ExampleService.class);
    Registry.register(SERVICE, service);

    FileServiceAsync fileservice = (FileServiceAsync) GWT.create(FileService.class);
    Registry.register(FILE_SERVICE, fileservice);

    Map<String, Entry> examples = new FastMap<Entry>();

    ExamplesModel model = new ExamplesModel();
    for (int i = 0; i < model.getChildren().size(); i++) {
      Category cat = (Category) model.getChildren().get(i);
      for (int j = 0; j < cat.getChildren().size(); j++) {
        Entry entry = (Entry) cat.getChildren().get(j);
        examples.put(entry.getId(), entry);
      }
    }

    Registry.register(MODEL, model);

  }

}
