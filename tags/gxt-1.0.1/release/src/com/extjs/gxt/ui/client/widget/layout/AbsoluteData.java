/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.layout;

import com.extjs.gxt.ui.client.Style;

/**
 * Layout data for <code>AbsoluteLayout</code>.
 */
public class AbsoluteData extends AnchorData {

  private int x = Style.DEFAULT;
  private int y = Style.DEFAULT;

  /**
   * Returns the x coordinate value.
   * 
   * @return the x value
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y coordinate value
   * 
   * @return the y value
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the x position (defaults to DEFAULT).
   * 
   * @param x the x coordinate value
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Sets the y posittion (defaults to DEFAULT).
   * 
   * @param y coordinate value
   */
  public void setY(int y) {
    this.y = y;
  }

}
