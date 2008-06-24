/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.fx.Resizable;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class ResizablePage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    ContentPanel cp = new ContentPanel();
    cp.setHeading("8-Way Resizing");
    cp.setIconStyle("icon-text");
    cp.addText("<div class=text>" + TestData.DUMMY_TEXT_SHORT + "</div>");

    cp.setSize(200, 125);

    Draggable d = new Draggable(cp);
    if (getParent() instanceof Component) {
      d.setContainer((Component)getParent());
    }
    new Resizable(cp);

    setLayout(new FlowLayout(10));
    add(cp);
    
    Widget p = getParent();
    if (p instanceof LayoutContainer) {
      LayoutContainer ct = (LayoutContainer) p;
      ct.setLayout(new FitLayout());
      ct.layout();
    }

  }

}
