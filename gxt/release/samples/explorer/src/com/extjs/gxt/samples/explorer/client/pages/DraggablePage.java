/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class DraggablePage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    
    HorizontalPanel vp = new HorizontalPanel();
    vp.verticalAlign = VerticalAlignment.TOP;
    vp.spacing = 10;

    ContentPanel cp = new ContentPanel();
    cp.collapsible = true;
    cp.setHeading("Proxy Drag");
    cp.addText(TestData.DUMMY_TEXT_SHORT);
    cp.setWidth(200);

    Draggable d = new Draggable(cp);

    vp.add(cp);

    cp = new ContentPanel();
    cp.collapsible = true;
    cp.bodyStyle = "padding: 4 8px";
    cp.setHeading("Direct Drag");
    cp.setIconStyle("icon-text");
    cp.addText("Drags can only be started from the header.");
    cp.setWidth(200);
    vp.add(cp);

    d = new Draggable(cp, cp.getHeader());
    d.container = this;
    d.useProxy = false;

    cp = new ContentPanel();
    cp.bodyStyle = "padding: 4 8px";
    cp.setHeading("Constrain");
    cp.setIconStyle("icon-text");
    cp.addText("Can only be dragged vertically.");
    cp.setWidth(200);
    vp.add(cp);

    d = new Draggable(cp, cp.getHeader());
    d.container = this;
    d.constrainHorizontal = true;

    add(vp);
    setScrollMode(Scroll.AUTO);

    Widget p = getParent();
    if (p instanceof Container) {
      Container ct = (Container) p;
      ct.setLayout(new FitLayout());
      ct.layout(true);
    }
  }

}
