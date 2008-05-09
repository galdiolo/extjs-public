/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;


import com.extjs.gxt.ui.client.widget.Button;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;

public class ToolTipPage extends Container implements EntryPoint {

  public void onModuleLoad() {

  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    
    VerticalPanel vp = new VerticalPanel();
    vp.setSpacing(10);
    
    Button btn = new Button("Print");
    btn.setToolTip(new ToolTipConfig("Information", "Prints the current document"));
    vp.add(btn);
    
    add(vp);
  }

}
