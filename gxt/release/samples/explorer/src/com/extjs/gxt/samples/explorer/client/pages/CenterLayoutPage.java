/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class CenterLayoutPage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    Viewport v = new Viewport();
    v.setLayout(new FitLayout());
    v.add(this);
    RootPanel.get().add(v);
  }
  
  public CenterLayoutPage() {
    // next line is only used to pass layout to containing container
    // this will have NO effect outside of the explorer demo
    setData("layout", new FillLayout());
    
    setLayout(new CenterLayout());

    ContentPanel panel = new ContentPanel();
    panel.setBodyStyle("padding: 6px");
    panel.setFrame(true);
    panel.setHeading("CenterLayout");
    panel.addText("I should be centered");
    panel.setWidth(200);

    add(panel);
  }


}
