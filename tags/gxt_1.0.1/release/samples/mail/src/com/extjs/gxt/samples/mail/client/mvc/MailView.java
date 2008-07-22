/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import java.util.List;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.samples.mail.client.widget.MailItemPanel;
import com.extjs.gxt.samples.mail.client.widget.MailListPanel;
import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.samples.resources.client.MailItem;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class MailView extends View {

  private LayoutContainer container;
  private MailListPanel mailListPanel;
  private MailItemPanel mailItemPanel;

  public MailView(Controller controller) {
    super(controller);
  }

  protected void handleEvent(AppEvent event) {
    if (event.type == AppEvents.NavMail) {
      LayoutContainer wrapper = (LayoutContainer) Registry.get("center");
      wrapper.removeAll();
      wrapper.add(container);
      wrapper.layout();
    }

    if (event.type == AppEvents.ViewMailItems) {
      LayoutContainer wrapper = (LayoutContainer) Registry.get("center");

      Folder f = (Folder) event.getData("folder");
      mailListPanel.setHeading(f.getName());

      ListStore<MailItem> store = mailListPanel.getStore();
      store.removeAll();
      store.add((List) event.data);

      wrapper.layout();

      List items = (List) event.data;
      if (items.size() > 0) {
        mailListPanel.getBinder().setSelection((MailItem) items.get(0));
      } else {
        mailItemPanel.showItem(null);
      }
    }

    if (event.type == AppEvents.ViewMailItem) {
      MailItem item = (MailItem) event.data;
      mailItemPanel.showItem(item);
    }
  }

  @Override
  protected void initialize() {
    container = new LayoutContainer();

    BorderLayout layout = new BorderLayout();
    layout.setEnableState(false);
    container.setLayout(layout);

    mailListPanel = new MailListPanel();
    container.add(mailListPanel, new BorderLayoutData(LayoutRegion.CENTER));

    mailItemPanel = new MailItemPanel();
    BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, .5f, 200, 1000);
    southData.setSplit(true);
    southData.setMargins(new Margins(5, 0, 0, 0));
    container.add(mailItemPanel, southData);
  }

}
