/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentPanelPage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }
  
  public ContentPanelPage() {
    // next line is only used to pass layout to containing container
    // this will have NO effect outside of the explorer demo
    setData("layout", new FitLayout());
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    Listener listener = new Listener<ComponentEvent>() {
      public void handleEvent(ComponentEvent ce) {
        ContentPanel cp = (ContentPanel) ce.component;
        String n = cp.getTitleText();
        if (ce.type == Events.Expand) {
          Info.display("Panel Change", "The '{0}' panel was expanded", n);
        } else {
          Info.display("Panel Change", "The '{0}' panel was collapsed", n);
        }
      }
    };

    final HorizontalPanel vp = new HorizontalPanel();
    vp.setSpacing(10);

    ContentPanel cp = new ContentPanel();
    cp.setFrame(true);
    cp.setWidth(200);
    cp.setHeading("Framed Panel");
    cp.setLayout(new FlowLayout());
    cp.setIconStyle("icon-list");

    LayoutContainer c = new LayoutContainer();
    c.setStyleAttribute("backgroundColor", "white");
    c.setStyleName("pad-text");
    c.setBorders(true);
    c.addText(TestData.DUMMY_TEXT_SHORT);
    cp.add(c);

    vp.add(cp);

    cp = new ContentPanel();
    cp.setCollapsible(true);
    cp.setWidth(200);
    cp.setBodyStyleName("pad-text");
    cp.setHeading("Collapsible");
    cp.addText(TestData.DUMMY_TEXT_SHORT);
    cp.addListener(Events.Expand, listener);
    cp.addListener(Events.Collapse, listener);
    vp.add(cp);

    cp = new ContentPanel();
    cp.setWidth(200);
    cp.setBodyStyleName("pad-text");
    cp.getHeader().setStyleAttribute("cursor", "move");
    cp.setHeading("Draggable");
    cp.addText(TestData.DUMMY_TEXT_SHORT);
    cp.disableTextSelection(true);
    vp.add(cp);

    Draggable d = new Draggable(cp);
    d.setUseProxy(false);
    d.setContainer(this);

    add(vp);

  }

}
