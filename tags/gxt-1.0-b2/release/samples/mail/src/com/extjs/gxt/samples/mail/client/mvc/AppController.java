/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;

public class AppController extends Controller {

  private AppView appView;

  public AppController() {
    registerEventTypes(AppEvents.Init);
  }

  public void handleEvent(AppEvent event) {
    if (event.type == AppEvents.Init) {
      forwardToView(appView, event);
    }
  }

  public void initialize() {
    appView = new AppView(this);
  }

}
