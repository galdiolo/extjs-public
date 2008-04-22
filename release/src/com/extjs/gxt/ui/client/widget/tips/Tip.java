/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tips;


import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ToolButton;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is the base class for {@link ToolTip} that provides the basic layout and
 * positioning that all tip-based classes require. This class can be used
 * directly for simple, statically-positioned tips that are displayed
 * programmatically, or it can be extended to provide custom tip
 * implementations.
 */
public class Tip extends ContentPanel {

  /**
   * True to render a close tool button into the tooltip header (defaults to
   * false).
   */
  public boolean closable;

  /**
   * Width in pixels of the tip (defaults to DEFAULT). Width will be ignored if it
   * exceeds the bounds of {@link #minWidth} or {@link #maxWidth}. The maximum
   * supported value is 500.
   */
  public int width = Style.DEFAULT;

  /**
   * The minimum width of the tip in pixels (defaults to 40).
   */
  public int minWidth = 40;

  /**
   * The maximum width of the tip in pixels (defaults to 300). The maximum
   * supported value is 500.
   */
  public int maxWidth = 300;

  /**
   * True or "sides" for the default effect, "frame" for 4-way shadow, and
   * "drop" for bottom-right shadow (defaults to "sides").
   */
  public String shadowPosition = "sides";

  protected int quickShowInterval = 250;

  /**
   * Creates a new tip instance.
   */
  public Tip() {
    frame = true;
    baseStyle = "x-tip";
    shim = true;
    autoHeight = true;
    shadow = true;
  }

  /**
   * Shows this tip at the specified position.
   * 
   * @param point the position
   */
  public void showAt(Point point) {
    showAt(point.x, point.y);
  }

  /**
   * Shows this tip at the specified position.
   * 
   * @param x the x coordinate
   * @param y the y coordiate
   */
  public void showAt(int x, int y) {
    RootPanel.get().add(this);
    el.makePositionable(true);
    updateContent();
    el.setVisibility(true);
    el.setVisible(true);
    if (width == Style.DEFAULT) {
      Element body = getElement("body");
      int bw = fly(body).getTextWidth() + 10;
      if (title != null) {
        bw = Math.max(bw, head.el.child("span").getTextWidth());
      }
      bw += getFrameWidth() + (closable ? 20 : 0) + fly(body).getPadding("lr");
      setWidth(Util.constrain(bw, minWidth, maxWidth));
    }
    Point p = new Point(x, y);
    p = el.adjustForConstraints(p);
    setPagePosition(p.x, p.y);
  }
  
  protected void updateContent() {
    
  }

  protected void onRender(Element parent, int pos) {
    if (closable) {
      header = true;
      head.addTool(new ToolButton("my-tool-close", new SelectionListener() {
        public void componentSelected(ComponentEvent ce) {
          hide();
        }
      }));
    }
    super.onRender(parent, pos);
  }

}
