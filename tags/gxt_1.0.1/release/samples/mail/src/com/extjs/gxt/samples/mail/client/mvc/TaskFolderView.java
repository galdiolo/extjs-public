/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.widget.ContentPanel;

public class TaskFolderView extends View {

  private ContentPanel tasks;

  public TaskFolderView(Controller controller) {
    super(controller);
  }

  @Override
  protected void handleEvent(AppEvent event) {
    switch (event.type) {
      case AppEvents.Init:
        initUI();
        break;
    }
  }
  
  @Override
  protected void initialize() {

  }

  protected void initUI() {
    tasks = new ContentPanel();
    tasks.setHeading("Tasks");
    tasks.addListener(Events.Expand, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent be) {
        Dispatcher.get().dispatch(AppEvents.NavTasks);
      }
    });

    ContentPanel west = (ContentPanel) Registry.get("west");
    west.add(tasks);
  }
}
