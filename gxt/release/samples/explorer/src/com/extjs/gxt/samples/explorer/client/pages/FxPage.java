/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.fx.FxStyle;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.widget.Button;
import com.extjs.gxt.ui.client.widget.ButtonBar;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class FxPage extends Container implements EntryPoint {

  private ContentPanel cp;
  
  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    VerticalPanel vp = new VerticalPanel();
    vp.spacing = 10;

    cp = new ContentPanel();
    cp.collapsible = true;
    cp.setHeading("FX Demo");
    cp.setIconStyle("icon-text");
    cp.addText(TestData.DUMMY_TEXT_SHORT);

    cp.setWidth(200);

    ButtonBar buttonBar = new ButtonBar();
    buttonBar.add(new Button("Slide In / Out", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        if (cp.isVisible()) {
          cp.el.slideOut(Direction.UP);
        } else {
          cp.el.slideIn(Direction.DOWN);
        }
      }
    }));
    buttonBar.add(new Button("Fade In / Out", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        FxStyle fx = new FxStyle(cp.el.dom);
        fx.fadeToggle();
      }
    }));
    buttonBar.add(new Button("Move", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        Rectangle rect = cp.el.getBounds();
        FxStyle fx = new FxStyle(cp.el.dom);
        fx.move(rect.x + 50, rect.y + 50);
      }
    }));

    buttonBar.add(new Button("Blink", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        FxStyle fx = new FxStyle(cp.el.dom);
        fx.blink();
      }
    }));

    vp.add(buttonBar);
    vp.add(cp);

    setLayout(new FlowLayout());
    add(vp);
  }

}
