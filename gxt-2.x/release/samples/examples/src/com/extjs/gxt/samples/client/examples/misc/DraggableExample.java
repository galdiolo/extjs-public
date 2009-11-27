/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.misc;

import com.extjs.gxt.samples.resources.client.Resources;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.Element;

public class DraggableExample extends LayoutContainer {

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    HorizontalPanel vp = new HorizontalPanel();
    vp.setVerticalAlign(VerticalAlignment.TOP);
    vp.setSpacing(10);

    ContentPanel cp = new ContentPanel();
    cp.setCollapsible(true);
    cp.setIcon(Resources.ICONS.text());
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
    cp.setIcon(Resources.ICONS.text());
    cp.addText("Drags can only be started from the header.");
    cp.setWidth(200);
    vp.add(cp);

    d = new Draggable(cp, cp.getHeader());
    d.setContainer(this);
    d.setUseProxy(false);

    cp = new ContentPanel();
    cp.setBodyStyle("padding: 4 8px;fontSize: 12px");
    cp.setHeading("Constrain");
    cp.setIcon(Resources.ICONS.text());
    cp.addText("Can only be dragged vertically.");
    cp.setWidth(200);
    vp.add(cp);

    d = new Draggable(cp, cp.getHeader());
    d.setContainer(this);
    d.setConstrainHorizontal(true);

    add(vp);
    setScrollMode(Scroll.AUTO);
  }

}
