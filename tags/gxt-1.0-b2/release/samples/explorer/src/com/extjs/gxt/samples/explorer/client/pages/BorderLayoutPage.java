/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;

public class BorderLayoutPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    Viewport v = new Viewport();
    v.add(this);
    v.layout(true);
  }
  
  public BorderLayoutPage() {
    setData("layout", new FitLayout());
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    setLayout(new BorderLayout());
    
    ContentPanel north = new ContentPanel();
    ContentPanel west = new ContentPanel();
    ContentPanel center = new ContentPanel();
    ContentPanel east = new ContentPanel();
    ContentPanel south = new ContentPanel();
    
    BorderLayoutData northData = new BorderLayoutData(LayoutRegion.NORTH, 100);
    northData.split = true;
    northData.margins = new Margins(5, 5, 0, 5);
    
    BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 200);
    westData.split = true;
    westData.collapsible = true;
    westData.margins = new Margins(5, 5, 5, 5);
    
    BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER);
    centerData.margins = new Margins(5, 0, 5, 0);
    
    
    BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 200);
    eastData.split = true;
    eastData.collapsible = true;
    eastData.margins = new Margins(5, 5, 5, 5);
    
    BorderLayoutData southData = new BorderLayoutData(LayoutRegion.SOUTH, 100);
    southData.split = true;
    southData.margins = new Margins(0, 5, 5, 5);
    
    add(north, northData);
    add(west, westData);
    add(center, centerData);
    add(east, eastData);
    add(south, southData);

  }

}