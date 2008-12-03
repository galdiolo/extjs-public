/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.custom;

import com.extjs.gxt.ui.client.fx.Draggable;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;

/**
 * A specialized content panel contained in a Portal.
 */
public class Portlet extends ContentPanel {

  private boolean pinned = false;

  public Portlet() {
    this(new FlowLayout());
    addStyleName("x-portlet");
  }

  public Portlet(Layout layout) {
    super(layout);
    setFrame(true);
    getHeader().setStyleAttribute("cursor", "move");
    setBodyStyle("backgroundColor: white;");
  }

  /**
   * True to "pin" the porlet and stop the user from being able to move the
   * portlet (defauls to false).
   * 
   * @param pinned true for pinned
   */
  public void setPinned(boolean pinned) {
    this.pinned = pinned;
    String c = pinned ? "default" : "move";
    getHeader().setStyleAttribute("cursor", c);
    if (getData("gxt.draggable") != null) {
      Draggable d = (Draggable) getData("gxt.draggable");
      d.setEnabled(!pinned);
    }
  }

  /**
   * Returns true if the portal is pinned.
   * 
   * @return true if pinned
   */
  public boolean isPinned() {
    return pinned;
  }

}
