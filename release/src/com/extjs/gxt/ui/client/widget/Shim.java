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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

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
   * @param shimElements true if you want to cover only Iframes
   */
  public void cover(boolean shimElements) {
    if (shimElements) {
      Element[] elements = XDOM.getBodyEl().select("iframe");
      shim(elements);
      elements = XDOM.getBodyEl().select("object");
      shim(elements);
      elements = XDOM.getBodyEl().select("applet");
      shim(elements);
      elements = XDOM.getBodyEl().select("embed");
      shim(elements);
    } else {
      shims.add(createShim(null, 0, 0, Window.getClientWidth(), Window.getClientHeight()));
    }
  }

  public void setStyleAttribute(String attr, String value) {
    for (El shim : shims) {
      shim.setStyleAttribute(attr, value);
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

  protected El createShim(El el, int left, int top, int width, int height) {
    Layer shim = new Layer();
    shim.enableShim();
    shim.setVisible(false);
    shim.addStyleName("x-drag-overlay");
    shim.setSize(width, height);
    shim.setLeftTop(left, top);
    shim.update("&#160;");
    XDOM.getBody().appendChild(shim.dom);
    shim.setVisible(true);
    if (el != null) {
      shim.setZIndex(el.getZIndex() + 10);
    }
    return shim;
  }

  protected void shim(Element[] elements) {
    for (Element e : elements) {
      El f = new El(e);
      if ((f.dom.getAttribute("src").length() > 0)
          || !e.getTagName().toLowerCase().equals("iframe")) {
        Rectangle bounds = f.getBounds();
        if (bounds.height > 0 && bounds.width > 0 && f.isVisible()) {
          shims.add(createShim(f, bounds.x, bounds.y, bounds.width, bounds.height));
        }
      }
    }
  }
}
