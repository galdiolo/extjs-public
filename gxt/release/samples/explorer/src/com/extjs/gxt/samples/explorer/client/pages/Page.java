/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;

public class Page extends TabPanel {

  protected LayoutContainer content;
  private boolean closable = true;

  public Page(LayoutContainer content) {
    this(content, true);
  }

  public Page(LayoutContainer content, boolean closeable) {
    this.content = content;
    this.closable = closeable;

    setTabPosition(TabPosition.BOTTOM);
    setBorderStyle(false);
    setBodyBorder(false);

    TabItem demo = new TabItem();

    Layout l = (Layout) content.getData("layout");
    if (l != null) {
      demo.setLayout(l);
    }
    if (content.getData("scroll") != null) {
      demo.setScrollMode(Scroll.AUTO);
    }
    demo.setText("Demo");
    demo.add(content);
    add(demo);

    String name = content.getClass().getName();
    name = name.substring(name.lastIndexOf(".") + 1);
    name = "code/" + name + ".html";

    TabItem source = new TabItem();
    source.setText("Source");
    source.setUrl(name);
    add(source);
  }

  public boolean isClosable() {
    return closable;
  }
}
