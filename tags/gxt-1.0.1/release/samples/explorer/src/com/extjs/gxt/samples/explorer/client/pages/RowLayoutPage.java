/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;


import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class RowLayoutPage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    Viewport v = new Viewport();
    v.setLayout(new FitLayout());
    v.add(this);
    RootPanel.get().add(v);
  }
  
  public RowLayoutPage() {
    // next line is only used to pass layout to containing container
    // this will have NO effect outside of the explorer demo
    setData("layout", new FitLayout());
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    
    RowLayout layout = new RowLayout(Orientation.VERTICAL);
    setLayout(layout);

    Text label1 = new Text("Test Label 1");
    label1.setStyleAttribute("margin", "4px");
    label1.addStyleName("text");
    label1.setBorders(true);
    
    Text label2 = new Text("Test Label 2");
    label2.setStyleAttribute("margin", "0 4px");
    label2.addStyleName("text");
    label2.setBorders(true);
    
    Text label3 = new Text("Test Label 3");
    label3.setStyleAttribute("margin", "4px");
    label3.addStyleName("text");
    label3.setBorders(true);

    add(label1, new RowData(1, -1));
    add(label2, new RowData(1, 1));
    add(label3, new RowData(1, -1));

    setBorders(true);

  }

}
