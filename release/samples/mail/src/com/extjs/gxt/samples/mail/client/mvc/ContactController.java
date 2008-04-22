/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;

public class ContactController extends Controller {

  public ContactController() {
    registerEventTypes(AppEvents.NavContacts);
  }

  public void handleEvent(AppEvent event) {
    if (event.type == AppEvents.NavContacts) {
      ContentPanel center = (ContentPanel) Registry.get("center");
      center.setHeading("Contacts");
      center.removeAll();

      Container south = (Container) Registry.get("south");
      south.removeAll();
    }
  }

}
