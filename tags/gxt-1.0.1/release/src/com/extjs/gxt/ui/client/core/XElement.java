/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.core;

import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Size;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

final class XElement extends JavaScriptObject {

  private static XElement xelem = XElement.create(DOM.createDiv());
  
  public static XElement fly(Element element) {
    return xelem.bind(element);
  }
  
  public static native XElement create(Element elem) /*-{
    return new $wnd.GXT.Ext.Element(elem);
  }-*/;
  
  protected XElement() {
    
  }
  
  public final native XElement bind(Element element) /*-{
    this.dom = element;
    return this;
  }-*/;
  
  /**
   * Adds the style name to the element. Duplicate classes are automatically
   * filtered out.
   * 
   * @param style the new style name
   * @return this
   */
  public final native XElement addStyleName(String style) /*-{
    return this.addClass(style);
  }-*/;
  
  /**
   * Aligns the element with another element relative to the specified anchor
   * points. Two values from the table below should be passed separated by a
   * dash, the first value is used as the element's anchor point, and the second
   * value is used as the target's anchor point.
   * <p>
   * In addition to the anchor points, the position parameter also supports the
   * "?" character. If "?" is passed at the end of the position string, the
   * element will attempt to align as specified, but the position will be
   * adjusted to constrain to the viewport if necessary. Note that the element
   * being aligned might be swapped to align to a different position than that
   * specified in order to enforce the viewport constraints. Following are all
   * of the supported anchor positions:
   * </p>
   * <dl>
   * <dt>Following are all of the supported anchor positions:</dt>
   * </dl>
   * <code><pre>
   *  Value  Description
   *  -----  -----------------------------
   *  tl     The top left corner (default)
   *  t      The center of the top edge
   *  tr     The top right corner
   *  l      The center of the left edge
   *  c      In the center of the element
   *  r      The center of the right edge
   *  bl     The bottom left corner
   *  b      The center of the bottom edge
   *  br     The bottom right corner
   * </code></pre>
   * 
   * @param align the element to align to
   * @param pos the position to align to
   * @param offsets the offsets or <code>null</code>
   * @return this
   */
  public final native XElement alignTo(Element align, String pos, int[] offsets) /*-{
    return this.alignTo(align, pos, offsets);
  }-*/;
  
  /**
   * Sets the X position of the element based on page coordinates. Element must
   * be part of the DOM tree to have page coordinates.
   * 
   * @param x the x coordinate
   * @return this
   */
  public final native XElement setX(int x) /*-{
    return this.setX(x);
  }-*/;
  
  /**
   * Returns the x,y coordinates specified by the anchor position on the
   * element.
   * 
   * @param anchor the specified anchor position (defaults to "c"). See
   *            {@link #alignTo} for details on supported anchor positions.
   * @param local <code>true</code> to get the local (element
   *            top/left-relative) anchor position instead of page coordinates
   * @return the position
   */
  public final native Point getAnchorXY(String anchor, boolean local) /*-{
    var xy = this.getAnchorXY(anchor, local);
    return @com.extjs.gxt.ui.client.util.Point::newInstance(DD)(xy[0],xy[1]);
  }-*/;
  
  /**
   * Gets the x,y coordinates to align this element with another element. See
   * {@link #alignTo} for more info on the supported position values.
   * 
   * @param align the element to align to
   * @param pos the position to align to
   * @param offsets the offsets or <code>null</code>
   * @return the point
   */
  public final native Point getAlignToXY(Element align, String pos, int ox, int oy) /*-{
    ox = ox || 0;
    oy = oy || 0;
    var xy = this.getAlignToXY(align, pos, ox, oy);
    return @com.extjs.gxt.ui.client.util.Point::newInstance(DD)(xy[0],xy[1]);
  }-*/;
  
  /**
   * Returns the element's style name.
   * 
   * @return the style name
   */
  public final native String getStyleName() /*-{
    return this.dom.className;
  }-*/;
  
  public final native Size getStyleSize() /*-{
    var s = this.getStyleSize();
    return @com.extjs.gxt.ui.client.util.Size::newInstance(II)(s.width, s.height);
  }-*/;
  
  /**
   * Sets a style attribute.
   * 
   * @param attr the attribute name
   * @param value the int value
   * @return this
   */
  public final native XElement setStyleAttribute(String attr, Object value) /*-{
    return this.setStyle(attr, value);
  }-*/;
  
  /**
   * Gets the current position of the element based on page coordinates. Element
   * must be part of the DOM tree to have page coordinates.
   * 
   * @return the location
   */
  public final native Point getXY() /*-{
    var xy = this.getXY();
    return @com.extjs.gxt.ui.client.util.Point::newInstance(DD)(xy[0],xy[1]);
  }-*/;
  
  
}
