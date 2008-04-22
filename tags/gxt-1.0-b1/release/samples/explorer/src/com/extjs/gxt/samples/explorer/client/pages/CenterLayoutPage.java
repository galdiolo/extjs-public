/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class CenterLayoutPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    setLayout(new CenterLayout());

    ContentPanel panel = new ContentPanel();
    panel.bodyStyle = "padding: 6px";
    panel.frame = true;
    panel.setHeading("CenterLayout");
    panel.addText("I should be centered");
    panel.setWidth(200);

    add(panel);
    
    layout(true);

    Widget p = getParent();
    if (p instanceof Container) {
      Container ct = (Container) p;
      ct.setLayout(new FillLayout());
      ct.layout(true);
    }
  }

}
