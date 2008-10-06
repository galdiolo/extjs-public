/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.layouts;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class RowLayoutExample extends LayoutContainer {

  public RowLayoutExample() {
    setLayout(new FlowLayout(10));
    
    ContentPanel panel = new ContentPanel();
    panel.setHeading("RowLayout");
    panel.setLayout(new RowLayout(Orientation.VERTICAL));
    panel.setSize(400, 300);
    panel.setFrame(true);
    panel.setCollapsible(true);
    
    Text label1 = new Text("Test Label 1");
    label1.setStyleAttribute("margin", "4px");
    label1.addStyleName("pad-text");
    label1.setStyleAttribute("backgroundColor", "white");
    label1.setBorders(true);
    
    Text label2 = new Text("Test Label 2");
    label2.setStyleAttribute("margin", "0 4px");
    label2.addStyleName("pad-text");
    label2.setStyleAttribute("backgroundColor", "white");
    label2.setBorders(true);
    
    Text label3 = new Text("Test Label 3");
    label3.setStyleAttribute("margin", "4px");
    label3.addStyleName("pad-text");
    label3.setStyleAttribute("backgroundColor", "white");
    label3.setBorders(true);

    panel.add(label1, new RowData(1, -1));
    panel.add(label2, new RowData(1, 1));
    panel.add(label3, new RowData(1, -1));

    add(panel);
  }
  
}
