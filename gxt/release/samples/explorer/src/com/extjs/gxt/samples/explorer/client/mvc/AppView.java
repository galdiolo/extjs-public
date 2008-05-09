/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.mvc;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.custom.ThemeSelector;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.RootPanel;

public class AppView extends View {

  private Viewport viewport;
  private ContentPanel centerPanel;
  private HtmlContainer northPanel;
  private ContentPanel westPanel;

  public AppView(Controller controller) {
    super(controller);
  }

  protected void handleEvent(AppEvent event) {
    switch (event.type) {
    }
  }

  protected void initialize() {
    viewport = new Viewport();
    viewport.setLayout(new BorderLayout());

    createNorth();
    createWest();
    createCenter();

    RootPanel.get().add(viewport);
  }

  private void createCenter() {
    centerPanel = new ContentPanel();
    centerPanel.setBorders(false);
    centerPanel.setHeaderVisible(false);
    centerPanel.setLayout(new FitLayout());

    BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
    data.margins = new Margins(5, 5, 5, 0);
    centerPanel.setData(data);
    viewport.add(centerPanel);
    Registry.register("centerPanel", centerPanel);
  }

  private void createWest() {
    BorderLayoutData data = new BorderLayoutData(LayoutRegion.WEST, 220, 150, 320);
    data.margins = new Margins(5, 5, 5, 5);
    data.collapsible = true;
    westPanel = new ContentPanel();
    westPanel.setData(data);

    ToolBar toolBar = new ToolBar();
    westPanel.setTopComponent(toolBar);

    viewport.add(westPanel);
    Registry.register("westPanel", westPanel);
  }

  private void createNorth() {
    StringBuffer sb = new StringBuffer();
    sb.append("<div id='demo-header' class='x-small-editor'><div id='demo-theme' class='x-form-field-wrap'></div><div id=demo-title>GXT Explorer Demo</div></div>");

    northPanel = new HtmlContainer(sb.toString());
    ThemeSelector selector = new ThemeSelector();
    northPanel.add(selector, ".x-form-field-wrap");

    BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 33);
    data.margins = new Margins();
    northPanel.setData(data);
    viewport.add(northPanel, data);
    Registry.register("northPanel", northPanel);
  }

}
