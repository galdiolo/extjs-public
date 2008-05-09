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

public class MailController extends Controller {

  private MailFolderView folderView;
  private MailListView listView;
  private MailItemView itemView;

  public MailController() {
    registerEventTypes(AppEvents.Init);
    registerEventTypes(AppEvents.NavMail);
    registerEventTypes(AppEvents.ViewMailItems);
    registerEventTypes(AppEvents.ViewMailItem);
  }

  @Override
  public void handleEvent(AppEvent event) {
    switch (event.type) {
      case AppEvents.Init:
        forwardToView(folderView, event);
        break;
      case AppEvents.NavMail:
        forwardToView(folderView, event);
        break;
      case AppEvents.ViewMailItems:
        forwardToView(listView, event);
        break;
      case AppEvents.ViewMailItem:
        forwardToView(itemView, event);
        break;
    }
  }

  public void initialize() {
    folderView = new MailFolderView(this);
    listView = new MailListView(this);
    itemView = new MailItemView(this);
  }

}
