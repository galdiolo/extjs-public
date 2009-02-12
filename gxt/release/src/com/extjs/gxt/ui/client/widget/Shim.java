/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * An class that supports placing a shim over the client window, and optionally
 * just over IFrames.
 * <p/>
 * Used by Draggable, Resizable and SplitBar
 */
public class Shim {

  private List<El> shims = new ArrayList<El>();

  /**
   * Creates and covers the area with a Shim. If shimIframes is true will only
   * covers IFrames.
   * 
   * @param shimIframes true if you want to cover only Iframes
   */
  public void cover(boolean shimIframes) {
    if (shimIframes) {
      Element[] iframes = XDOM.getBodyEl().select("iframe");
      for(Element e : iframes) {
        El f = El.fly(e);
        if (f.dom.getAttribute("src").length() > 0) {
          Rectangle bounds = f.getBounds();
          if (bounds.height > 0 && bounds.width > 0 && f.isVisible()) {
            shims.add(createShim(bounds.x, bounds.y, bounds.width, bounds.height));
          }
        }
      }
    } else {
      shims.add(createShim(0, 0, Window.getClientWidth(), Window.getClientHeight()));
    }
  }

  /**
   * Uncovers and removes the shim.
   */
  public void uncover() {
    while (!shims.isEmpty()) {
      shims.get(0).remove();
      shims.remove(0);
    }
  }

  protected El createShim(int left, int top, int width, int height) {
    El shim = new El(DOM.createDiv());
    shim.setSize(width, height);
    shim.setStyleAttribute("backgroundColor", "red");
    RootPanel.getBodyElement().appendChild(shim.dom);
    shim.makePositionable(true);
    shim.setLeftTop(left, top);
    shim.setStyleAttribute("opacity", 0);
    shim.updateZIndex(-1);
    shim.setVisible(true);
    return shim;
  }
}
