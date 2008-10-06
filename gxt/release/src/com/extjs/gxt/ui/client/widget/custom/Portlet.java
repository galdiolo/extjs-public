/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.custom;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

public class Portlet extends ContentPanel {

  public Portlet() {
    this(new FlowLayout());
    addStyleName("x-portlet");
  }

  public Portlet(Layout layout) {
    super(layout);
    setFrame(true);
    getHeader().setStyleAttribute("cursor", "move");
    setBodyStyle("backgroundColor: white;");
  }

}
