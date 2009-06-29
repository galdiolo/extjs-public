/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.misc;

import com.extjs.gxt.samples.client.Examples;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class DraggableExample extends LayoutContainer {

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    HorizontalPanel vp = new HorizontalPanel();
    vp.setVerticalAlign(VerticalAlignment.TOP);
    vp.setSpacing(10);

    ContentPanel cp = new ContentPanel();
    cp.setCollapsible(true);
    cp.setIcon(Examples.ICONS.text());
    cp.setBodyStyleName("pad-text");
    cp.setHeading("Proxy Drag");
    cp.addText(TestData.DUMMY_TEXT_SHORT);
    cp.setWidth(200);

    Draggable d = new Draggable(cp);

    vp.add(cp);

    cp = new ContentPanel();
    cp.setCollapsible(true);
    cp.setBodyStyle("padding: 4 8px;fontSize: 12px");
    cp.setHeading("Direct Drag");
    cp.setIcon(Examples.ICONS.text());
    cp.addText("Drags can only be started from the header.");
    cp.setWidth(200);
    vp.add(cp);

    d = new Draggable(cp, cp.getHeader());
    d.setContainer(this);
    d.setUseProxy(false);

    cp = new ContentPanel();
    cp.setBodyStyle("padding: 4 8px;fontSize: 12px");
    cp.setHeading("Constrain");
    cp.setIcon(Examples.ICONS.text());
    cp.addText("Can only be dragged vertically.");
    cp.setWidth(200);
    vp.add(cp);

    d = new Draggable(cp, cp.getHeader());
    d.setContainer(this);
    d.setConstrainHorizontal(true);

    add(vp);
    setScrollMode(Scroll.AUTO);

    Widget p = getParent();
    if (p instanceof LayoutContainer) {
      LayoutContainer ct = (LayoutContainer) p;
      ct.setLayout(new FitLayout());
      ct.layout();
    }
  }

}
