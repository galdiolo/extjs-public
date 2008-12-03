/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.client.examples.model.Entry;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class Page extends TabPanel {

  protected Entry entry;

  public LayoutContainer getContent() {
    return entry.getExample();
  }

  public Page(Entry entry) {
    this.entry = entry;

    setTabPosition(TabPosition.BOTTOM);
    setBorderStyle(false);
    setBodyBorder(false);

    TabItem demo = new TabItem();
    demo.setScrollMode(Scroll.AUTO);
    if (entry.isFill()) {
      demo.setLayout(new FitLayout());
      demo.setScrollMode(Scroll.NONE);
    }

    demo.setText("Demo");
    demo.add(entry.getExample());
    add(demo);

    if (entry.isClosable()) {
      TabItem source = new TabItem();
      source.setText("Source");
      source.setUrl(entry.getSourceUrl());
      add(source);
    }
  }

}
