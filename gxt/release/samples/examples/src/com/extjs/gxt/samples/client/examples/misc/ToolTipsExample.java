/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.misc;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;

public class ToolTipsExample extends LayoutContainer {

  public ToolTipsExample() {
    VerticalPanel vp = new VerticalPanel();
    vp.setSpacing(10);

    Button btn = new Button("Print");
    btn.setToolTip(new ToolTipConfig("Information", "Prints the current document"));
    vp.add(btn);

    add(vp);
  }

}
