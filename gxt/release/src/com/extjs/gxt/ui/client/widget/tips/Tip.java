/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.tips;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Shadow.ShadowPosition;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
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

  protected int quickShowInterval = 250;

  private ShadowPosition shadowPosition = ShadowPosition.SIDES;
  private int minWidth = 40;
  private int maxWidth = 300;
  private boolean closable;

  /**
   * Creates a new tip instance.
   */
  public Tip() {
    frame = true;
    baseStyle = "x-tip";
    shim = true;
    setAutoHeight(true);
    setShadow(true);
  }

  /**
   * Returns the maximum width.
   * 
   * @return the max width
   */
  public int getMaxWidth() {
    return maxWidth;
  }

  /**
   * Returns the minimum width.
   * 
   * @return the minimum width
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Returns the shadow position.
   * 
   * @return the shadow position
   */
  public ShadowPosition getShadowPosition() {
    return shadowPosition;
  }

  /**
   * Returns true if the tip is closable.
   * 
   * @return the closable state
   */
  public boolean isClosable() {
    return closable;
  }

  /**
   * True to render a close tool button into the tooltip header (defaults to
   * false).
   * 
   * @param closable the closable state
   */
  public void setClosable(boolean closable) {
    this.closable = closable;
  }

  /**
   * Sets he maximum width of the tip in pixels (defaults to 300). The maximum
   * supported value is 500.
   * 
   * @param maxWidth the max width
   */
  public void setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
  }

  /**
   * Sets the minimum width of the tip in pixels (defaults to 40).
   * 
   * @param minWidth the min width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * Sets the shadow position (defauts to SIDES).
   * 
   * @param shadowPosition the shadow position
   */
  public void setShadowPosition(ShadowPosition shadowPosition) {
    this.shadowPosition = shadowPosition;
  }

  /**
   * Shows this tip at the specified position.
   * 
   * @param x the x coordinate
   * @param y the y coordiate
   */
  public void showAt(int x, int y) {
    RootPanel.get().add(this);
    el().makePositionable(true);
    updateContent();
    el().setVisibility(true);
    el().setVisible(true);
    if (width == null) {
      Element body = getElement("body");
      int bw = fly(body).getTextWidth();
      if (getHeading() != null) {
        bw = Math.max(bw, head.el().child("span").getTextWidth());
      }
      bw += getFrameWidth() + (closable ? 20 : 0) + fly(body).getPadding("lr");
      
      setWidth(Util.constrain(bw, minWidth, maxWidth));
    }
    Point p = new Point(x, y);
    p = el().adjustForConstraints(p);
    setPagePosition(p.x, p.y);
  }

  /**
   * Shows this tip at the specified position.
   * 
   * @param point the position
   */
  public void showAt(Point point) {
    showAt(point.x, point.y);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    if (closable) {
      setHeaderVisible(true);
      head.addTool(new ToolButton("x-tool-close", new SelectionListener<ComponentEvent>() {
        public void componentSelected(ComponentEvent ce) {
          hide();
        }
      }));
    }
    super.onRender(parent, pos);
  }

  protected void updateContent() {

  }

}
