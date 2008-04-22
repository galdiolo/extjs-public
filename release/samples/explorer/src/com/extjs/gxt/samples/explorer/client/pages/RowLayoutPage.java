/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;


import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class RowLayoutPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    
    RowLayout layout = new RowLayout(Orientation.VERTICAL);
    layout.margin = 4;
    layout.spacing = 4;

    Container wc = new Container();
    wc.setLayout(layout);

    Text label1 = new Text("Test Label 1");
    label1.setBorders(true);
    Text label2 = new Text("Test Label 2");
    label2.setBorders(true);
    Text label3 = new Text("Test Label 3");
    label3.setBorders(true);

    label1.setData(new RowData(RowData.FILL_HORIZONTAL));
    label2.setData(new RowData(RowData.FILL_BOTH));
    label3.setData(new RowData(RowData.FILL_BOTH));
    
    wc.add(label1);
    wc.add(label2);
    wc.add(label3);

    wc.setBorders(true);
    El.fly(label1.getElement()).setBorders(true);
    El.fly(label2.getElement()).setBorders(true);
    El.fly(label3.getElement()).setBorders(true);

    setLayout(new FillLayout(15));
    add(wc);
    
    Widget p = getParent();
    if (p instanceof Container) {
      Container c = (Container) p;
      c.setLayout(new FitLayout());
      c.layout(true);
    }
  }

}
