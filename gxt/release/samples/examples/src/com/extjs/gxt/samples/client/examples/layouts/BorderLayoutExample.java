/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.layouts;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;

public class BorderLayoutExample extends LayoutContainer {

  public BorderLayoutExample() {
    setLayout(new BorderLayout());

    ContentPanel north = new ContentPanel();
    ContentPanel west = new ContentPanel();
    ContentPanel center = new ContentPanel();
    ContentPanel east = new ContentPanel();
    ContentPanel south = new ContentPanel();

    BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 100);
    northData.setCollapsible(true);
    northData.setFloatable(true);
    northData.setSplit(true);
    northData.setMargins(new Margins(5, 5, 0, 5));

    BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 200);
    westData.setSplit(true);
    westData.setCollapsible(true);
    westData.setMargins(new Margins(5));

    BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
    centerData.setMargins(new Margins(5, 0, 5, 0));

    BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 200);
    eastData.setSplit(true);
    eastData.setCollapsible(true);
    eastData.setMargins(new Margins(5));

    BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 100);
    southData.setSplit(true);
    southData.setCollapsible(true);
    southData.setFloatable(true);
    southData.setMargins(new Margins(0, 5, 5, 5));

    add(north, northData);
    add(west, westData);
    add(center, centerData);
    add(east, eastData);
    add(south, southData);
  }

}
