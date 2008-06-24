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

public class ContactController extends Controller {

  private ContactFolderView folderView;
  private ContactView contactView;

  public ContactController() {
    registerEventTypes(AppEvents.Init);
    registerEventTypes(AppEvents.NavContacts);
  }

  @Override
  public void initialize() {
    super.initialize();
    folderView = new ContactFolderView(this);
    contactView = new ContactView(this);
  }

  @Override
  public void handleEvent(AppEvent event) {
    if (event.type == AppEvents.Init) {
      forwardToView(folderView, event);
    }
    if (event.type == AppEvents.NavContacts) {
      forwardToView(contactView, event);
    }
  }

}
