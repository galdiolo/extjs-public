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
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.RootPanel;

public class AppView extends View {

  private Viewport viewport;
  private ContentPanel west;
  private Container main;
  private ContentPanel center;


  public AppView(Controller controller) {
    super(controller);
  }

  protected void initialize() {
    viewport = new Viewport();
    viewport.setLayout(new BorderLayout());

    BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 200, 150, 350);
    westData.margins = new Margins(5, 0, 5, 5);

    west = new ContentPanel();
    west.setHeading("GXT Mail Demo");
    west.setLayout(new FillLayout());
    createNavigation();

    viewport.add(west, westData);

    main = new Container();
    BorderLayout layout = new BorderLayout();
    main.setLayout(layout);

    center = new ContentPanel();
    center.setLayout(new FillLayout());

    Container south = new Container();
    south.setBorders(true);
    south.setLayout(new FillLayout());

    BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
    centerData.margins = new Margins(0, 0, 5, 0);

    BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, .5f, 100, 1000);
    southData.split = true;
    southData.margins = new Margins(0, 0, 0, 0);

    main.add(center, centerData);
    main.add(south, southData);

    
    BorderLayoutData mainCenter = new BorderLayoutData(LayoutRegion.CENTER);
    mainCenter.margins = new Margins(5, 5, 5, 5);
    
    viewport.add(main, mainCenter);

    Registry.register("viewport", viewport);
    Registry.register("west", west);
    Registry.register("center", center);
    Registry.register("south", south);
    
    
    RootPanel.get().add(viewport);
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        viewport.layout();
        Dispatcher.forwardEvent(AppEvents.NavMail);
      }
    });


  }

  protected void handleEvent(AppEvent event) {
    // if (event.type == AppEvents.Init) {
    // ExpandItem item = expandBar.getItem(0);
    // expandBar.setExpanded(item, true);
    // }
  }

  private void createNavigation() {
    // expandBar = new ExpandBar(Style.SINGLE | Style.HEADER);
    // expandBar.setBorders(false);
    // expandBar.setHeaderHeight(28);
    //
    // ExpandItem mailItem = new ExpandItem();
    // mailItem.setText("Mail");
    // mailItem.addListener(Events.Expand, new Listener() {
    // public void handleEvent(BaseEvent be) {
    // Dispatcher.forwardEvent(AppEvents.NavMail);
    // }
    // });
    // expandBar.add(mailItem);
    //
    // ExpandItem taskItem = new ExpandItem();
    // taskItem.setText("Tasks");
    // taskItem.addListener(Events.Expand, new Listener() {
    // public void handleEvent(BaseEvent be) {
    // Dispatcher.forwardEvent(AppEvents.NavTasks);
    // }
    // });
    // expandBar.add(taskItem);
    //
    // ExpandItem contactsItem = new ExpandItem();
    // contactsItem.setText("Contacts");
    // contactsItem.addListener(Events.Expand, new Listener() {
    // public void handleEvent(BaseEvent be) {
    // Dispatcher.forwardEvent(AppEvents.NavContacts);
    // }
    // });
    // expandBar.add(contactsItem);
    //
    // Registry.register("mailItem", mailItem);

  }

}
