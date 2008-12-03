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

/**
 * XElement is intended to replace El in a future release. It extends
 * JavaScriptObject which is a new feature in GWT 1.5.
 */
final class XElement extends JavaScriptObject {

  private static XElement xelem = XElement.create(DOM.createDiv());

  /**
   * Creates a new xelement.
   * 
   * @param elem the element to wrap
   * @return the x element
   */
  public static native XElement create(Element elem) /*-{
      return new $wnd.GXT.Ext.Element(elem);
    }-*/;

  /**
   * Returns the globally shared fly-weight xelement.
   * 
   * @param element
   * @return this
   */
  public static XElement fly(Element element) {
    return xelem.bind(element);
  }

  protected XElement() {

  }

  /**
   * Adds the style name to the element. Duplicate classes are automatically
   * filtered out.
   * 
   * @param style the new style name
   */
  public final native void addStyleName(String style) /*-{
    this.addClass(style);
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
  public final native void alignTo(Element align, String pos, int[] offsets) /*-{
      this.alignTo(align, pos, offsets);
    }-*/;

  /**
   * More flexible version of {@link #setStyleAttribute} for setting style
   * properties.
   * 
   * @param styles a style specification string, e.g. "width:100px"
   */
  public final native void applyStyles(String styles) /*-{
      this.applyStyles(styles);
    }-*/;

  /**
   * Binds the element to this instance.
   * 
   * @param element the element bind
   * @return this
   */
  public final native XElement bind(Element element) /*-{
      this.dom = element;
      return this;
    }-*/;

  /**
   * Looks at this node and then at parent nodes for a match of the passed
   * simple selector (e.g. div.some-class or span:first-child).
   * 
   * @param selector the simple selector to test
   * @param maxDepth the max depth
   * @return the matching element
   */
  public final native Element findParentElement(String selector, int maxDepth) /*-{
     return this.findParent(selector, maxDepth);
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
   * Returns the x,y coordinates specified by the anchor position on the
   * element.
   * 
   * @param anchor the specified anchor position (defaults to "c"). See
   *          {@link #alignTo} for details on supported anchor positions.
   * @param local <code>true</code> to get the local (element top/left-relative)
   *          anchor position instead of page coordinates
   * @return the position
   */
  public final native Point getAnchorXY(String anchor, boolean local) /*-{
      var xy = this.getAnchorXY(anchor, local);
      return @com.extjs.gxt.ui.client.util.Point::newInstance(DD)(xy[0],xy[1]);
    }-*/;

  /**
   * Returns the width of the border(s) for the specified side(s).
   * 
   * @param sides can be t, l, r, b or any combination of those to add multiple
   *          values. For example, passing lr would get the border (l)eft width
   *          + the border (r)ight width.
   * @return the width of the sides passed added together
   */
  public final native int getBorderWidth(String sides) /*-{
     return this.getBorderWidth(sides);
   }-*/;

  /**
   * Returns the sum width of the padding and borders for the passed "sides".
   * See #getBorderWidth() for more information about the sides.
   * 
   * @param sides sides
   * @return the width
   */
  public final native int getFrameWidth(String sides) /*-{
      return this.getFrameWidth(sides);
    }-*/;

  /**
   * Returns an object with properties top, left, right and bottom representing
   * the margins of this element unless sides is passed, then it returns the
   * calculated width of the sides (see #getPadding).
   * 
   * @param sides any combination of l, r, t, b to get the sum of those sides
   * @return the margins
   */
  public final native int getMargins(String sides) /*-{
    return this.getMargins(sides);
  }-*/;
  
  /**
   * Gets the left X coordinate.
   * 
   * @param local true to get the local css position instead of page coordinate
   */
  public final native int getLeft(boolean local) /*-{
    return this.getLeft(local);
  }-*/;
  
  /**
   * Gets the top Y coordinate.
   * 
   * @param local true to get the local css position instead of page coordinate
   */
  public final native int getTop(boolean local) /*-{
    return this.getTop(local);
  }-*/;

  /**
   * Gets the width of the padding(s) for the specified side(s).
   * 
   * @param sides can be t, l, r, b or any combination of those to add multiple
   *          values. For example, passing lr would get the border (l)eft width
   *          + the border (r)ight width.
   * @return the width of the sides passed added together
   */
  public final native int getPadding(String sides) /*-{
      return this.getPadding(sides);
    }-*/;

  /**
   * Normalizes currentStyle and computedStyle.
   * 
   * @param attr the style attribute whose value is returned.
   * @return the current value of the style attribute for this element.
   */
  public final native String getStyleAttribute(String attr) /*-{
      return this.getStyle(attr);
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
   * Gets the current position of the element based on page coordinates. Element
   * must be part of the DOM tree to have page coordinates.
   * 
   * @return the location
   */
  public final native Point getXY() /*-{
      var xy = this.getXY();
      return @com.extjs.gxt.ui.client.util.Point::newInstance(DD)(xy[0],xy[1]);
    }-*/;

  /**
   * Removes a style name.
   * 
   * @param style the style name to remove
   */
  public final native void removeStyleName(String style) /*-{
    this.removeClass(style);
  }-*/;

  /**
   * Sets a style attribute.
   * 
   * @param attr the attribute name
   * @param value the int value
   * @return this
   */
  public final native void setStyleAttribute(String attr, Object value) /*-{
      this.setStyle(attr, value);
    }-*/;

  /**
   * Sets the X position of the element based on page coordinates. Element must
   * be part of the DOM tree to have page coordinates.
   * 
   * @param x the x coordinate
   * @return this
   */
  public final native void setX(int x) /*-{
      this.setX(x);
    }-*/;

  /**
   * Sets the Y position of the element based on page coordinates. Element must
   * be part of the DOM tree to have page coordinates.
   * 
   * @param y the y coordinate
   */
  public final native void setY(int y) /*-{
     this.setY(y);
   }-*/;

}
