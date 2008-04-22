/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.layout.AccordianLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class NestedBorderLayoutPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    setLayout(new BorderLayout());

    ContentPanel west = new ContentPanel();
    west.setLayout(new AccordianLayout());
    west.setHeading("West");
    
    Html html = new Html(TestData.DUMMY_TEXT_SHORT);
    west.add(html);

    // ExpandBar expandBar = new ExpandBar(Style.SINGLE | Style.HEADER);
    // expandBar.setBorders(false);
    // fillBar(expandBar);
    // west.add(expandBar);
    //
    // WidgetContainer main = new WidgetContainer();
    // BorderLayout layout = new BorderLayout();
    // layout.margin = 0;
    // main.setLayout(layout);
    //  
    // ContentPanel top = new ContentPanel();
    // top.setTitleText("Top Panel");
    //
    // SimplePanel bottom = new SimplePanel();
    // MyDOM.setStyleName(bottom.getElement(), "my-border");
    // DOM.setStyleAttribute(bottom.getElement(), "backgroundColor", "white");
    //  
    // WidgetContainer btm = new WidgetContainer();
    // btm.setBorders(true);
    // btm.setStyleAttribute("backgroundColor", "white");
    //  
    //  
    // // use percentages for top panel height
    // main.add(top, new BorderLayoutData(Style.NORTH, .5f, 100, 400));
    // main.add(btm, new BorderLayoutData(Style.CENTER));
    //  
    // c.add(west, new BorderLayoutData(Style.WEST, 150, 100, 300));
    // c.add(main, new BorderLayoutData(Style.CENTER));
    //
    // container.setLayout(new FillLayout(8));
    // container.add(c);
    //
    // }
    //
    // private void fillBar(ExpandBar expandBar) {
    // ExpandItem item1 = new ExpandItem();
    // item1.setText("Item 1");
    // item1.getContainer().addText(TestData.DUMMY_TEXT_SHORT);
    // expandBar.add(item1);
    //
    // ExpandItem item2 = new ExpandItem();
    // item2.setText("Item 2");
    // item2.getContainer().addText(TestData.DUMMY_TEXT_SHORT);
    // expandBar.add(item2);
    //
    // ExpandItem item3 = new ExpandItem();
    // item3.setText("Item 3");
    // item3.getContainer().addText(TestData.DUMMY_TEXT_SHORT);
    // expandBar.add(item3);
    //  
    // item1.setExpanded(true);
  }

}
