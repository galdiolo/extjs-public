/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.layouts;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;

public class CenterLayoutExample extends LayoutContainer {

  public CenterLayoutExample() {
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
