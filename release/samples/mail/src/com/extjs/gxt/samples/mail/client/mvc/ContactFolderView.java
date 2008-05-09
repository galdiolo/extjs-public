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

public class ContactFolderView extends View {

  private ContentPanel contacts;

  public ContactFolderView(Controller controller) {
    super(controller);
  }

  @Override
  protected void initialize() {
    contacts = new ContentPanel();
    contacts.setHeading("Contacts");
    contacts.addListener(Events.Expand, new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent be) {
        Dispatcher.get().dispatch(AppEvents.NavContacts);
      }
    });

    ContentPanel west = (ContentPanel) Registry.get("west");
    west.add(contacts);
  }

  @Override
  protected void handleEvent(AppEvent event) {

  }

}
