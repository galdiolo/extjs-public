/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.core;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.XDOM;
import com.extjs.gxt.ui.client.Style.Direction;
import com.extjs.gxt.ui.client.Style.ScrollDir;
import com.extjs.gxt.ui.client.fx.BaseEffect;
import com.extjs.gxt.ui.client.fx.Fx;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.fx.Move;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Markup;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.util.Point;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.Region;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.util.TextMetrics;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;

/**
 * Represents an Element in the DOM.
 */
public class El {

  /**
   * VisMode enumeration. Specifies the the element should hidden using the CSS
   * display or visibility style.
   * 
   */
  public enum VisMode {
    DISPLAY, VISIBILITY
  }

  /**
   * The globally shared El instance.
   */
  private static El global = new El(DOM.createDiv());

  static {
    GXT.init();
  }

  /**
   * Tests to see if the value has units, otherwise appends the default (px).
   * 
   * @param value the value
   * @return the value with units
   */
  public static String addUnits(String value) {
    return addUnitsInternal(value, "px");
  }

  public static El fly(com.google.gwt.dom.client.Element element) {
    global.dom = (Element) element;
    return global;
  }

  /**
   * Gets the globally shared flyweight El, with the passed node as the active
   * element. Do not store a reference to this element - the dom node can be
   * overwritten by other code.
   * 
   * @param element the element to be wrapped
   * @return the global El object
   */
  public static El fly(Element element) {
    global.dom = element;
    return global;
  }

  private static native String addUnitsInternal(String v, String defaultUnit) /*-{
      if(v === "" || v == "auto"){
      return v;
      }
      if(v === undefined){
      return '';
      }
      if(typeof v == "number" || !/\d+(px|em|%|en|ex|pt|in|cm|mm|pc)$/i.test(v)){
      return v + (defaultUnit || 'px');
      }
      return v;
      }-*/;

  private native static void disableContextMenuInternal(Element elem, boolean disable) /*-{
      if (disable) {
      elem.oncontextmenu = function() {  return false};
      } else {
      elem.oncontextmenu = null;
      }
      }-*/;

  private native static void disableTextSelectInternal(Element e, boolean disable)/*-{
         if (disable) {
           e.ondrag = function (evt) {
            var targ;
            if (!e) var e = $wnd.event;
            if (e.target) targ = e.target;
            else if (e.srcElement) targ = e.srcElement;
            if (targ.nodeType == 3) // defeat Safari bug
            targ = targ.parentNode;
            if (targ.tagName == 'INPUT') {
              return true;
            }
            return false; 
           };
           e.onselectstart = function (e) { 
            var targ;
            if (!e) var e = $wnd.event;
            if (e.target) targ = e.target;
            else if (e.srcElement) targ = e.srcElement;
            if (targ.nodeType == 3) // defeat Safari bug
            targ = targ.parentNode;
            if (targ.tagName == 'INPUT') {
              return true;
            }
            return false; 
           };
         } else {
           e.ondrag = null;
           e.onselectstart = null;
         }
         }-*/;

  /**
   * The wrapped dom element.
   */
  public Element dom;

  private VisMode visiblityMode = VisMode.DISPLAY;
  private String originalDisplay = "block";
  private El _mask;
  private El _maskMsg;
  private boolean isClipped;
  private String[] originalClipped;

  /**
   * Creates a new el instance.
   * 
   * @param element the element to be wrapped
   */
  public El(Element element) {
    this.dom = element;
  }

  /**
   * Creates a new El instance from the HTML fragment.
   * 
   * @param html the html
   */
  public El(String html) {
    this(XDOM.create(html));
  }

  /**
   * Adds the event type to the element's sunk events.
   * 
   * @param event the events to add
   * @return this
   */
  public El addEventsSunk(int event) {
    int bits = DOM.getEventsSunk(dom);
    DOM.sinkEvents(dom, bits | event);
    return this;
  }

  /**
   * Adds the style name to the element. Duplicate styles are automatically
   * filtered out.
   * 
   * @param style the new style name
   * @return this
   */
  public El addStyleName(String style) {
    XElement.fly(dom).addStyleName(style);
    return this;
  }

  /**
   * Ensures the element is within the browser viewport.
   * 
   * @param p the target destination
   * @return the new location
   */
  public Point adjustForConstraints(Point p) {
    return getConstrainToXY(XDOM.getBody(), p);
  }

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
  public El alignTo(Element align, String pos, int[] offsets) {
    if (offsets == null) {
      offsets = new int[] {0, 0};
    }
    Point p = getAlignToXY(align, pos, offsets);
    setXY(p);
    return this;
  }

  /**
   * Appends a element.
   * 
   * @param child the element to add
   * @return the child element
   */
  public El appendChild(Element child) {
    dom.appendChild(child);
    return new El(child);
  }

  /**
   * More flexible version of {@link #setStyleAttribute} for setting style
   * properties.
   * 
   * @param styles a style specification string, e.g. "width:100px"
   * @return this
   */
  public El applyStyles(String styles) {
    Ext.call(dom, "applyStyles", styles);
    return this;
  }

  /**
   * Blinks the element.
   * 
   * @param config the fx config
   * @return this
   */
  public El blink(FxConfig config) {
    BaseEffect.blink(this, config, 50);
    return this;
  }

  /**
   * Removes focus.
   * 
   * @return this
   */
  public El blur() {
    return setFocus(false);
  }

  /**
   * Wraps the specified element with a special markup/CSS block.
   * 
   * @param style a base CSS class to apply to the containing wrapper element
   *          (defaults to 'x-box').
   * @return this
   */
  public El boxWrap(String style) {
    String s = style != null ? style : "x-box";
    El temp = insertHtml("beforeBegin", Format.substitute("<div class={0}>" + Markup.BBOX, s)
        + "</div>");
    temp.child("." + s + "-mc").appendChild(dom);
    return temp;
  }

  /**
   * Centers the element in the viewport.
   * 
   * @return this
   */
  public El center() {
    return center(null);
  }

  /**
   * Centers the element.
   * 
   * @param constrainViewport true to contstrain the element position to the
   *          viewport.
   * @return this
   */
  public El center(boolean constrainViewport) {
    Element container = null;
    int width = container == null ? Window.getClientWidth() : container.getOffsetWidth();
    int height = container == null ? Window.getClientHeight() : container.getOffsetHeight();

    if (container == null) {
      container = XDOM.getBody();
    }

    int w = getWidth();
    int h = getHeight();

    int left = (width / 2) - (w / 2) + container.getScrollTop();
    int top = (height / 2) - (h / 2) + container.getScrollLeft();

    if (constrainViewport) {
      left = Math.max(5, left);
      top = Math.max(5, top);
    }

    setLeftTop(left, top);
    sync(true);
    return this;
  }

  /**
   * Centers an element.
   * 
   * @param container the container element
   * @return this
   */
  public El center(Element container) {
    int width = container == null ? Window.getClientWidth() : container.getOffsetWidth();
    int height = container == null ? Window.getClientHeight() : container.getOffsetHeight();

    if (container == null) {
      container = XDOM.getBody();
    }

    int w = getWidth();
    int h = getHeight();

    int left = (width / 2) - (w / 2) + container.getScrollTop();
    int top = (height / 2) - (h / 2) + container.getScrollLeft();

    setLeftTop(left, top);
    sync(true);
    return this;
  }

  /**
   * Selects a single child at any depth below this element based on the passed
   * CSS selector.
   * 
   * @param selector the css selector
   * @return the child element
   */
  public El child(String selector) {
    Element child = DomQuery.selectNode(selector, dom);
    return child == null ? null : new El(child);
  }

  /**
   * Selects a single child at any depth below this element based on the passed
   * CSS selector.
   * 
   * @param selector the css selector
   * @return the child element
   */
  public Element childElement(String selector) {
    return DomQuery.selectNode(selector, dom);
  }

  /**
   * Returns the element's child.
   * 
   * @param index the index of the child element
   * @return the child element
   */
  public El childNode(int index) {
    return new El(DOM.getChild(dom, index));
  }

  /**
   * Generators a native dom click on the element.
   * 
   * @return this
   */
  public native El click() /*-{
      var dom = this.@com.extjs.gxt.ui.client.core.El::dom;
      if (dom.click) {
      dom.click();
      }
      else {
      var event = $doc.createEvent("MouseEvents");
      event.initEvent('click', true, true, $wnd, 0, 0, 0, 0, 0, false, false, false, false, 1, dom);
      dom.dispatchEvent(event);    
      }
      return this;
      }-*/;

  /**
   * Clips overflow on the element.
   * 
   * @return this
   */
  public El clip() {
    if (!isClipped) {
      isClipped = true;
      originalClipped = new String[3];
      originalClipped[0] = getStyleAttribute("overflow");
      originalClipped[1] = getStyleAttribute("overflowX");
      originalClipped[2] = getStyleAttribute("overflowY");
      setStyleAttribute("overflow", "hidden");
      setStyleAttribute("overflowX", "hidden");
      setStyleAttribute("overflowY", "hidden");
    }
    return this;
  }

  /**
   * Clones the element.
   * 
   * @param deep true to clone children
   * @return the new element
   */
  public native Element cloneNode(boolean deep) /*-{
      var dom = this.@com.extjs.gxt.ui.client.core.El::dom;
      return dom.cloneNode(deep);
      }-*/;

  /**
   * Creates and adds a child using the HTML fragment.
   * 
   * @param html the html fragment
   * @return the new child
   */
  public El createChild(String html) {
    return appendChild(XDOM.create(html));
  }

  /**
   * Creates and inserts a child using the HTML fragment.
   * 
   * @param html the html fragment
   * @param insertBefore a child element of this element
   * @return the new child
   */
  public El createChild(String html, Element insertBefore) {
    Element element = XDOM.create(html);
    int idx = DOM.getChildIndex(dom, insertBefore);
    insertChild(element, idx);
    return new El(element);
  }

  /**
   * Disables the element.
   * 
   * @return this
   */
  public native El disable() /*-{
      var dom = this.@com.extjs.gxt.ui.client.core.El::dom;
      dom.disabled = true;
      return this;
      }-*/;

  /**
   * Enables and disables the browsers default context menu for the specified
   * element.A circular reference will be created when disabling text selection.
   * Disabling should be cleared when the element is detached. See the
   * <code>Component</code> source for an example.
   * 
   * @param disable true to disable, false to enable
   * @return this
   */
  public El disableContextMenu(boolean disable) {
    disableContextMenuInternal(dom, disable);
    return this;
  }

  /**
   * Enables or disables text selection for the element. A circular reference
   * will be created when disabling text selection. Disabling should be cleared
   * when the element is detached. See the <code>Component</code> source for an
   * example.
   * 
   * @param disable true to disable, false to enable
   * @return this
   */
  public El disableTextSelection(boolean disable) {
    if (GXT.isGecko) {
      setStyleName("x-unselectable", disable);
    }
    disableTextSelectInternal(dom, disable);
    return this;
  }

  /**
   * Selects a single *direct* child based on the passed CSS selector (the
   * selector should not contain an id).
   * 
   * @param selector the CSS selector
   * @return the child element
   */
  public El down(String selector) {
    Element elem = DomQuery.selectNode(" > " + selector, dom);
    return fly(elem);
  }

  /**
   * Enables the element.
   * 
   * @return this
   */
  public native El enable()/*-{
         var dom = this.@com.extjs.gxt.ui.client.core.El::dom;
         dom.disabled = false;
         return this;
         }-*/;

  /**
   * Convenience method for setVisibilityMode(VisibilityMode.DISPLAY).
   * 
   * @param display what to set display to when visible
   * @return this
   */
  public El enableDisplayMode(String display) {
    setVisibilityMode(VisMode.DISPLAY);
    if (display != null) {
      originalDisplay = display;
    }
    return this;
  }

  /**
   * Fades in the element.
   * 
   * @param config the fx config
   * @return this
   */
  public El fadeIn(FxConfig config) {
    BaseEffect.fadeIn(this, config);
    return this;
  }

  /**
   * Fades out the element.
   * 
   * @param config the fx config
   * @return this
   */
  public El fadeOut(FxConfig config) {
    BaseEffect.fadeOut(this, config);
    return this;
  }

  /**
   * Toggles the element visibility using a fade effect.
   * 
   * @param config the fx config
   * @return this
   */
  public El fadeToggle(FxConfig config) {
    if (!isVisible() || !isVisibility()) {
      BaseEffect.fadeIn(this, config);
    } else {
      BaseEffect.fadeOut(this, config);
    }
    return this;
  }

  /**
   * Looks at this node and then at parent nodes for a match of the passed
   * simple selector (e.g. div.some-class or span:first-child).
   * 
   * @param selector the simple selector to test
   * @return the matching element
   */
  public El findParent(String selector, int maxDepth) {
    Element elem = Ext.findParent(dom, selector, maxDepth);
    if (elem == null) {
      return null;
    }
    return new El(elem);
  }

  /**
   * Looks at this node and then at parent nodes for a match of the passed
   * simple selector (e.g. div.some-class or span:first-child).
   * 
   * @param selector the simple selector to test
   * @param maxDepth the max depth
   * @return the matching element
   */
  public Element findParentElement(String selector, int maxDepth) {
    return Ext.findParent(dom, selector, maxDepth);
  }

  /**
   * Looks at parent nodes for a match of the passed simple selector (e.g.
   * div.some-class or span:first-child).
   * 
   * @param selector the simple selector to test
   * @param maxDepth The max depth to search as a number or element
   * @return the matching element
   */
  public El findParentNode(String selector, int maxDepth) {
    El p = getParent();
    return p != null ? p.findParent(selector, maxDepth) : null;
  }

  /**
   * Returns the element's first child.
   * 
   * @return the first child
   */
  public El firstChild() {
    return new El(DOM.getFirstChild(dom));
  }

  /**
   * Tries to focus the element.
   * 
   * @return this
   */
  public El focus() {
    return setFocus(true);
  }

  /**
   * Gets the x,y coordinates to align this element with another element. See
   * {@link #alignTo} for more info on the supported position values.
   * 
   * @param align the element to align to
   * @param pos the position to align to
   * @param offsets the offsets or <code>null</code>
   * @return the point
   */
  public Point getAlignToXY(Element align, String pos, int[] offsets) {
    if (offsets == null) {
      offsets = new int[] {0, 0};
    }
    return XElement.fly(dom).getAlignToXY(align, pos, offsets[0], offsets[1]);
  }

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
  public Point getAnchorXY(String anchor, boolean local) {
    return XElement.fly(dom).getAnchorXY(anchor, local);
  }

  /**
   * Returns the width of the border(s) for the specified side(s).
   * 
   * @param sides can be t, l, r, b or any combination of those to add multiple
   *          values. For example, passing lr would get the border (l)eft width
   *          + the border (r)ight width.
   * @return the width of the sides passed added together
   */
  public int getBorderWidth(String sides) {
    return Ext.callInt(dom, "getBorderWidth", sides);
  }

  /**
   * Returns the bottom Y coordinate of the element (element Y position +
   * element height).
   * 
   * @param local
   * @return the buttom value
   */
  public int getBottom(boolean local) {
    if (!local) {
      return getY() + getHeight();
    } else {
      return getTop() + getHeight();
    }
  }

  /**
   * Returns the elements bounds in page coordiates.
   * 
   * @return the bounds
   */
  public Rectangle getBounds() {
    return getBounds(false);
  }

  /**
   * Returns the element's bounds in page coordinates.
   * 
   * @param local if true the element's left and top are returned instead of
   *          page coordinates
   * @return the element's bounds
   */
  public Rectangle getBounds(boolean local) {
    Size s = getSize();
    Rectangle rect = new Rectangle();
    rect.width = s.width;
    rect.height = s.height;
    if (local) {
      rect.x = getLeft(true);
      rect.y = getTop(true);
    } else {
      Point p = getXY();
      rect.x = p.x;
      rect.y = p.y;
    }
    return rect;
  }

  /**
   * Returns a child element.
   * 
   * @param index the child index
   * @return the child
   */
  public El getChild(int index) {
    Element child = DOM.getChild(dom, index);
    return new El(child);
  }

  /**
   * Returns the index of the child element.
   * 
   * @return the index
   */
  public int getChildIndex(Element child) {
    return DOM.getChildIndex(dom, child);
  }

  /**
   * Returns the element's client height property.
   * 
   * @return the client width value
   */
  public int getClientHeight() {
    return DOM.getElementPropertyInt(dom, "clientHeight");
  }

  /**
   * Returns the element's client width property.
   * 
   * @return the client width value
   */
  public int getClientWidth() {
    return DOM.getElementPropertyInt(dom, "clientWidth");
  }

  /**
   * Returns either the offsetHeight or the height of this element based on it's
   * CSS height.
   * 
   * @return the height
   */
  public int getComputedHeight() {
    int h = getHeight();
    if (h == 0) {
      h = getIntStyleAttribute("height");
    }
    return h;
  }

  /**
   * Returns either the offsetWidth or the width of this element based on it's
   * CSS width.
   * 
   * @return the width
   */
  public int getComputedWidth() {
    int w = getWidth();
    if (w == 0) {
      w = getIntStyleAttribute("width");
    }
    return w;
  }

  /**
   * Returns the sum width of the padding and borders for the passed "sides".
   * See #getBorderWidth() for more information about the sides.
   * 
   * @param sides sides
   * @return the width
   */
  public int getFrameWidth(String sides) {
    return Ext.callInt(dom, "getFrameWidth", sides);
  }

  /**
   * Returns the offset height of the element.
   * 
   * @return the height
   */
  public int getHeight() {
    return getHeight(false);
  }

  /**
   * Returns the element's height.
   * 
   * @param content true to get the height minus borders and padding
   * @return the element's height
   */
  public int getHeight(boolean content) {
    int h = DOM.getElementPropertyInt(dom, "offsetHeight");
    if (content & !XDOM.isVisibleBox) {
      h -= getFrameWidth("tb");
    }
    return h;
  }

  /**
   * Returns the element's id.
   * 
   * @return the id
   */
  public String getId() {
    return DOM.getElementProperty(dom, "id");
  }

  /**
   * Returns the element's inner HTML.
   * 
   * @return the inner html
   */
  public String getInnerHtml() {
    return DOM.getInnerHTML(dom);
  }

  /**
   * Returns the element's style value.
   * 
   * @param attr the attribute name
   * @return the value
   */
  public int getIntStyleAttribute(String attr) {
    String v = DOM.getStyleAttribute(dom, attr);
    if (v == null || v.equals("")) {
      return 0;
    }
    return Integer.parseInt(v);
  }

  /**
   * Returns the element's content area bounds.
   * 
   * @return the bounds
   */
  public Rectangle getLayoutBounds() {
    Rectangle r = getBounds();
    r.width -= getFrameWidth("lr");
    r.height -= getFrameWidth("tb");
    return r;
  }

  /**
   * Returns the top Y coordinate.
   * 
   * @return the top value
   */
  public int getLeft() {
    return getLeft(true);
  }

  /**
   * Gets the left X coordinate.
   * 
   * @param local true to get the local css position instead of page coordinate
   * @return the left value
   */
  public int getLeft(boolean local) {
    return Ext.callInt(dom, "getLeft", local ? "true" : "false");
  }

  /**
   * Returns an object with properties top, left, right and bottom representing
   * the margins of this element unless sides is passed, then it returns the
   * calculated width of the sides (see #getPadding).
   * 
   * @param sides any combination of l, r, t, b to get the sum of those sides
   * @return the margins
   */
  public int getMargins(String sides) {
    if (getStyleAttribute("margin").equals("")) {
      return 0;
    }
    return Ext.callInt(dom, "getMargins", sides);
  }

  /**
   * Returns the offsets of this element from the passed element. Both element
   * must be part of the DOM tree and not have display:none to have page
   * coordinates.
   * 
   * @param elem the element to get the offsets from
   * @return the offsets
   */
  public Point getOffsetsTo(Element elem) {
    return offsetsTo(elem);
  }

  /**
   * Returns the element's outer HTML.
   * 
   * @return the inner html
   */
  public String getOuterHtml() {
    return dom.getAttribute("outerHTML");
  }

  /**
   * Gets the width of the padding(s) for the specified side(s).
   * 
   * @param sides can be t, l, r, b or any combination of those to add multiple
   *          values. For example, passing lr would get the border (l)eft width
   *          + the border (r)ight width.
   * @return the width of the sides passed added together
   */
  public int getPadding(String sides) {
    return Ext.callInt(dom, "getPadding", sides);
  }

  /**
   * Returns the element's parent.
   * 
   * @return the parent
   */
  public El getParent() {
    return new El(DOM.getParent(dom));
  }

  /**
   * Returns the region of the given element. The element must be part of the
   * DOM tree to have a region.
   * 
   * @return a region containing top, left, bottom, right
   */
  public Region getRegion() {
    Rectangle bounds = getBounds();
    Region r = new Region();
    r.left = bounds.x;
    r.top = bounds.y;
    r.right = r.left + bounds.width;
    r.bottom = r.top + bounds.height;
    return r;
  }

  /**
   * Returns the right X coordinate of the element (element X position + element
   * width).
   * 
   * @param local <code>true</code> to get the local css position instead of
   *          page coordinate
   * @return the right value
   */
  public int getRight(boolean local) {
    if (!local) {
      return getX() + getWidth();
    } else {
      return getLeft(false) + getWidth();
    }
  }

  /**
   * Returns the horiztontal scroll position.
   * 
   * @return the scroll position
   */
  public int getScrollLeft() {
    return DOM.getElementPropertyInt(dom, "scrollLeft");
  }

  /**
   * Returnst the current vertical scroll position.
   * 
   * @return the scroll poistion
   */
  public int getScrollTop() {
    return DOM.getElementPropertyInt(dom, "scrollTop");
  }

  /**
   * Returns the size of the element.
   * 
   * @return the size
   */
  public Size getSize() {
    return getSize(false);
  }

  /**
   * Returns the element's size.
   * 
   * @param content true to get the size minus borders and padding
   * @return the size
   */
  public Size getSize(boolean content) {
    return new Size(getWidth(content), getHeight(content));
  }

  /**
   * Normalizes currentStyle and computedStyle.
   * 
   * @param attr the style attribute whose value is returned.
   * @return the current value of the style attribute for this element.
   */
  public String getStyleAttribute(String attr) {
    return Ext.callString(dom, "getStyle", attr);
  }

  /**
   * Returns the style width.
   * 
   * @return the style width
   */
  public int getStyleHeight() {
    String h = getStyleAttribute("height");
    if (h == null || h.equals("")) return 0;
    if (h.matches("(auto|em|%|en|ex|pt|in|cm|mm|pc)")) {
      return 0;
    }
    h = h.replaceAll("px", "");
    if (h.length() > 0) {
      return Integer.parseInt(h);
    }
    return 0;
  }

  /**
   * Returns the element's style name.
   * 
   * @return the style name
   */
  public String getStyleName() {
    return dom.getClassName();
  }

  /**
   * Returns the element's size, using style attribute before offsets.
   * 
   * @return the size
   */
  public Size getStyleSize() {
    return XElement.fly(dom).getStyleSize();
  }

  /**
   * Returns the style width. A value is only returned if the specified style is
   * in pixels.
   * 
   * @return the style width
   */
  public int getStyleWidth() {
    String w = getStyleAttribute("width");
    if (w == null || w.equals("")) return 0;
    if (w.matches("(auto|em|%|en|ex|pt|in|cm|mm|pc)")) {
      return 0;
    }
    w = w.replaceAll("px", "");
    if (w.length() > 0) {
      return Integer.parseInt(w);
    }
    return 0;
  }

  /**
   * Returns the element's sub child.
   * 
   * @param depth the child node depth
   * @return the child element
   */
  public Element getSubChild(int depth) {
    Element child = dom;
    while (depth-- > 0) {
      child = DOM.getChild(child, 0);
    }
    return child;
  }

  /**
   * Returns the measured width of the element's text.
   * 
   * @return the width
   */
  public int getTextWidth() {
    String html = getInnerHtml();
    TextMetrics metrics = TextMetrics.get();
    metrics.bind(dom);
    return metrics.getWidth(html);
  }

  /**
   * Returns the top Y coordinate.
   * 
   * @return the top value
   */
  public int getTop() {
    return getTop(true);
  }

  /**
   * Gets the top Y coordinate.
   * 
   * @param local true to get the local css position instead of page coordinate
   * @return the top value
   */
  public int getTop(boolean local) {
    return Ext.callInt(dom, "getTop", local ? "true" : "false");
  }

  /**
   * Returns the the "value" attribute.
   * 
   * @return the value
   */
  public String getValue() {
    return DOM.getElementProperty(dom, "value");
  }

  /**
   * Returns the offset width.
   * 
   * @return the width
   */
  public int getWidth() {
    return DOM.getElementPropertyInt(dom, "offsetWidth");
  }

  /**
   * Returns the element's width.
   * 
   * @param content true to get the width minus borders and padding
   * @return the width
   */
  public int getWidth(boolean content) {
    int w = getWidth();
    if (content) {
      w -= getFrameWidth("lr");
    }
    return w;
  }

  /**
   * Gets the current X position of the element based on page coordinates.
   * Element must be part of the DOM tree to have page coordinates.
   * 
   * @return the x position of the element
   */
  public int getX() {
    return DOM.getAbsoluteLeft(dom);
  }

  /**
   * Gets the current position of the element based on page coordinates. Element
   * must be part of the DOM tree to have page coordinates.
   * 
   * @return the location
   */
  public native Point getXY() /*-{
      var dom = this.@com.extjs.gxt.ui.client.core.El::dom;
      $wnd.dom = dom;
      var xy = $wnd.GXT._el.fly(dom).getXY();
      var p = @com.extjs.gxt.ui.client.util.Point::newInstance(II)(xy[0],xy[1]);
      return p;
      }-*/;

  /**
   * Gets the current Y position of the element based on page coordinates.
   * 
   * @return the y position of the element
   */
  public int getY() {
    return DOM.getAbsoluteTop(dom);
  }

  /**
   * Returns the element's z-index.
   * 
   * @return the z-index
   */
  public int getZIndex() {
    try {
      return DOM.getIntStyleAttribute(dom, "zIndex");
    } catch (Exception e) {
      return 0;
    }
  }

  /**
   * Checks if the specified CSS style name exists on this element's DOM node.
   * 
   * @param style the style name
   * @return true if the style name exists, else false
   */
  public boolean hasStyleName(String style) {
    String cls = DOM.getElementProperty(dom, "className");
    return (" " + cls + " ").indexOf(" " + style + " ") != -1 ? true : false;
  }

  /**
   * Inserts this element before the passed element.
   * 
   * @param before the element to insert before
   * @return this
   */
  public El insertBefore(Element before) {
    before.getParentElement().insertBefore(dom, before);
    return this;
  }

  /**
   * Inserts the elmenent as a child before the given element.
   * 
   * @param child the element to insert
   * @param before the element the child will be inserted before
   * @return this
   */
  public El insertBefore(Element child, Element before) {
    dom.insertBefore(child, before);
    return this;
  }

  /**
   * Inserts the elmenents as a child before the given element.
   * 
   * @param elements the elements to insert
   * @param before the element the children will be inserted before
   * @return this
   */
  public El insertBefore(Element[] elements, Element before) {
    for (int i = 0; i < elements.length; i++) {
      insertBefore(elements[i], before);
    }
    return this;
  }

  /**
   * Inserts an element at the specified index.
   * 
   * @param child the child element
   * @param index the insert location
   * @return this
   */
  public El insertChild(Element child, int index) {
    DOM.insertChild(dom, child, index);
    return this;
  }

  public El insertChild(Element[] children, int index) {
    for (int i = children.length - 1; i >= 0; i--) {
      DOM.insertChild(dom, children[i], index);
    }
    return this;
  }

  /**
   * Inserts an element as the first child.
   * 
   * @param element the child element
   * @return this
   */
  public El insertFirst(Element element) {
    DOM.insertChild(dom, element, 0);
    return this;
  }

  public El insertFirst(Element[] elems) {
    for (int i = 0; i < elems.length; i++) {
      DOM.appendChild(dom, elems[i]);
    }
    return this;
  }

  /**
   * Creates and inserts a child element.
   * 
   * @param html the HTML fragment
   * @return the new child
   */
  public El insertFirst(String html) {
    return new El(DomHelper.insertFirst(dom, html));
  }

  /**
   * Inserts an html fragment into this element
   * 
   * @param where where to insert the html in relation to el - beforeBegin,
   *          afterBegin, beforeEnd, afterEnd.
   * @param html the HTML frament
   * @return the inserted node (or nearest related if more than 1 inserted)
   */
  public El insertHtml(String where, String html) {
    return new El(DomHelper.insertHtml(where, dom, html));
  }

  /**
   * Inserts the element into the given parent.
   * 
   * @param parent the parent element
   * @return this
   */
  public El insertInto(Element parent) {
    fly(parent).appendChild(dom);
    return this;
  }

  /**
   * Inserts a element.
   * 
   * @param parent the parent element
   * @param index the insert index
   * @return this
   */
  public El insertInto(Element parent, int index) {
    fly(parent).insertChild(dom, index);
    return this;
  }

  /**
   * Inserts the passed element as a sibling of this element.
   * 
   * @param elem the element to insert
   * @param where 'before' or 'after'
   * @return the inserted element
   */
  public Element insertSibling(Element elem, String where) {
    Element refNode = where.equals("before") ? dom : nextSibling();
    if (refNode == null) {
      DOM.appendChild(DOM.getParent(dom), elem);
    } else {
      DOM.insertBefore(getParent().dom, elem, refNode);
    }
    return elem;
  }

  /**
   * Inserts the passed elements as a sibling of this element.
   * 
   * @param elems the elements to insert
   * @param where where 'before' or 'after'
   * @return this
   */
  public El insertSibling(Element[] elems, String where) {
    for (int i = 0; i < elems.length; i++) {
      insertSibling(elems[i], where);
    }
    return this;
  }

  /**
   * Returns true if this element matches the passed simple selector (e.g.
   * div.some-class or span:first-child).
   * 
   * @param selector selector
   * @return true if the element matches the selector, else false
   */
  public boolean is(String selector) {
    return DomQuery.is(dom, selector);
  }

  /**
   * Returns true if the element is part of the browser's DOM.
   * 
   * @return the dom state
   */
  public boolean isConnected() {
    Element p = dom;
    while (p != null) {
      if (p == XDOM.getBody()) {
        return true;
      }
      p = DOM.getParent(p);
    }
    return false;
  }

  /**
   * Returns true if this element is masked.
   * 
   * @return the masked state
   */
  public boolean isMasked() {
    return _mask != null && _mask.isVisible();
  }

  /**
   * Returns <code>true</code> if the element is visible using the css
   * 'visibiliy' attribute.
   * 
   * @return the visible state
   */
  public boolean isVisibility() {
    return !DOM.getStyleAttribute(dom, "visibility").equals("hidden");
  }

  /**
   * Checks whether the element is currently visible using both visibility and
   * display properties.
   * 
   * @return <code>true</code> if the element is currently visible, else
   *         <code>false</code>
   */
  public boolean isVisible() {
    return isVisible(false);
  }

  /**
   * Returns whether the element is currently visible using the CSS display
   * property.
   * 
   * @param blockOnly true to only test
   * 
   * @return true if visible
   */
  public boolean isVisible(boolean blockOnly) {
    String v = getStyleAttribute("visibility");
    String d = getStyleAttribute("display");
    if (blockOnly) {
      return d != null && d.equals("block");
    }

    if (v != null && v.equals("hidden")) {
      return false;
    } else if (d != null && d.equals("none")) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * Returns the element's last child.
   * 
   * @return the last child
   */
  public El lastChild() {
    return El.fly(DOM.getChild(dom, DOM.getChildCount(dom) - 1));
  }

  /**
   * Retrieves the data using the request builder and updates the element'c
   * contents.
   * <p>
   * This method is subject to change.
   * 
   * @param builder the request builder
   */
  public Request load(RequestBuilder builder) {
    try {
      builder.setCallback(new RequestCallback() {

        public void onError(Request request, Throwable exception) {
          setInnerHtml(exception.getMessage());
        }

        public void onResponseReceived(Request request, Response response) {
          setInnerHtml(response.getText());
        }

      });
      return builder.send();
    } catch (Exception e) {
      setInnerHtml(e.getMessage());
      return null;
    }
  }

  /**
   * Makes an element positionable.
   */
  public void makePositionable() {
    String position = DOM.getStyleAttribute(dom, "position");
    if (position.equals("") || position.equals("static")) {
      DOM.setStyleAttribute(dom, "position", "relative");
    }
  }

  /**
   * Makes an element positionable.
   * 
   * @param absolute <code>true</code> to position absolutely
   * @return this
   */
  public El makePositionable(boolean absolute) {
    if (absolute) {
      DOM.setStyleAttribute(dom, "position", "absolute");
    } else {
      makePositionable();
    }
    return this;
  }

  /**
   * Puts a mask over this element to disable user interaction.
   * 
   * @param message a message to display in the mask
   * @return the mask element
   */
  public El mask(String message) {
    makePositionable();
    if (_maskMsg != null) {
      _maskMsg.remove();
    }
    if (_mask != null) {
      _mask.remove();
    }

    _mask = new El("<div class='ext-el-mask'></div>");

    addStyleName("x-masked");
    _mask.setDisplayed(true);

    appendChild(_mask.dom);

    _maskMsg = new El(
        "<div class='ext-el-mask-msg' style='z-index: 100'><div style='background-color: white'></div></div>");
    _maskMsg.firstChild().setInnerHtml(message);
    _maskMsg.setDisplayed(true);

    appendChild(_maskMsg.dom);

    if (GXT.isIE && !(GXT.isIE && GXT.isStrict)) {
      _mask.setWidth(getClientWidth());
    }

    _maskMsg.center(dom);

    return _mask;
  }

  /**
   * Returns the elements next sibling.
   * 
   * @return the sibling element
   */
  public Element nextSibling() {
    return DOM.getNextSibling(dom);
  }

  /**
   * Returns the offsets between two elements. Both element must be part of the
   * DOM tree and not have display:none to have page coordinates.
   * 
   * @param to the to element
   * @return the xy page offsets
   */
  public Point offsetsTo(Element to) {
    Point o = El.fly(dom).getXY();
    Point e = El.fly(to).getXY();
    return new Point(o.x - e.x, o.y - e.y);
  }

  /**
   * Selects child nodes based on the passed CSS selector (the selector should
   * not contain an id).
   * 
   * @param selector the selector/xpath query
   * @return the matching elements
   */
  public Element[] query(String selector) {
    return DomQuery.select(selector, dom);
  }

  /**
   * Removes this element from the DOM
   */
  public void remove() {
    Element p = getParent().dom;
    DOM.removeChild(p, dom);
  }

  /**
   * Removes a child.
   * 
   * @param child the child to remove
   * @return this
   */
  public El removeChild(Element child) {
    DOM.removeChild(dom, child);
    return this;
  }

  /**
   * Removes all the elements children.
   */
  public void removeChildren() {
    Element child = null;
    while ((child = firstChild().dom) != null) {
      DOM.removeChild(dom, child);
    }
    setInnerHtml("");
  }

  /**
   * Removes the element from it's parent.
   */
  public void removeFromParent() {
    Element p = DOM.getParent(dom);
    if (p != null) {
      DOM.removeChild(p, dom);
    }
  }

  /**
   * Removes a style name.
   * 
   * @param style the style name to remove
   * @return this
   */
  public El removeStyleName(String style) {
    Ext.call(dom, "removeClass", style);
    return this;
  }

  /**
   * Replaces a style name on the element with another. If the old name does not
   * exist, the new name will simply be added.
   * 
   * @param oldStyle the style to replace
   * @param newStyle the new style
   * @return this
   */
  public El replaceStyleName(String oldStyle, String newStyle) {
    removeStyleName(oldStyle);
    addStyleName(newStyle);
    return this;
  }

  /**
   * Scrolls the element into view.
   * 
   * @param container the container element
   * @param hscroll <code>false</code> to disable horizontal scrolling.
   */
  public native void scrollIntoView(Element container, boolean hscroll) /*-{
      var elem = this.@com.extjs.gxt.ui.client.core.El::dom;
      var c = container || $doc.body;
      var o = this.@com.extjs.gxt.ui.client.core.El::offsetsTo(Lcom/google/gwt/user/client/Element;)(container);
      var l = o.@com.extjs.gxt.ui.client.util.Point::x;
      var t = o.@com.extjs.gxt.ui.client.util.Point::y;
      l = l + c.scrollLeft;
      t = t + c.scrollTop;
      var b = t + elem.offsetHeight;
      var r = l + elem.offsetWidth;
      
      var ch = c.clientHeight;
      var ct = parseInt(c.scrollTop, 10);
      var cl = parseInt(c.scrollLeft, 10);
      var cb = ct + ch;
      var cr = cl + c.clientWidth;
      
      if (t < ct){
      c.scrollTop = t;
      }else if(b > cb){
      c.scrollTop = b-ch;
      }
      c.scrollTop = c.scrollTop; 
      
      if(hscroll !== false){
      if(l < cl){
      c.scrollLeft = l;
      } else if(r > cr){
      c.scrollLeft = r-c.clientWidth;
      }
      c.scrollLeft = c.scrollLeft;
      }
      }-*/;

  /**
   * Scrolls this element the specified scroll point.
   * 
   * @param side either "left" for scrollLeft values or "top" for scrollTop
   *          values.
   * @param value the new scroll value
   * @return this
   */
  public El scrollTo(String side, int value) {
    if ("left".equalsIgnoreCase("left")) {
      setScrollLeft(value);
    }
    if ("right".equalsIgnoreCase("top")) {
      setScrollTop(value);
    }
    return this;
  }

  /**
   * Scrolls this element the specified scroll point.
   * 
   * @param side side either "left" for scrollLeft values or "top" for scrollTop
   *          values.
   * @param value the new scroll value
   * @param config the fx config
   * @return this
   */
  public El scrollTo(String side, int value, FxConfig config) {
    BaseEffect.scroll(this, config, ScrollDir.HORIZONTAL, value);
    return this;
  }

  /**
   * Selects a group of elements.
   * 
   * @param selector selector the selector/xpath query
   * @return the matching elements
   */
  public Element[] select(String selector) {
    return DomQuery.select(selector, dom);
  }

  /**
   * Selects a single element.
   * 
   * @param selector the CSS selector
   * @return the matching element
   */
  public El selectNode(String selector) {
    Element el = DomQuery.selectNode(selector, dom);
    if (el != null) {
      return new El(el);
    }
    return null;
  }

  /**
   * Adds or removes a border. The style name 'x-border' is added to the widget
   * to display a border.
   * 
   * @param show the show state
   * @return this
   */
  public El setBorders(boolean show) {
    if (show) {
      addStyleName("x-border");
      setStyleAttribute("borderWidth", "1px");
    } else {
      setStyleAttribute("borderWidth", "0px");
    }
    return this;
  }

  /**
   * Sets the element's bounds.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the new width
   * @param height the new height
   * @return this
   */
  public El setBounds(int x, int y, int width, int height) {
    return setBounds(x, y, width, height, false);
  }

  /**
   * Sets the element's bounds.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @param width the new width
   * @param height the new height
   * @param adjust true to adjust for box model issues
   * @return this
   */
  public El setBounds(int x, int y, int width, int height, boolean adjust) {
    setPagePosition(x, y);
    setSize(width, height, adjust);
    return this;
  }

  /**
   * Sets the element's bounds.
   * 
   * @param bounds the new bounds
   * @return this
   */
  public El setBounds(Rectangle bounds) {
    setBounds(bounds.x, bounds.y, bounds.width, bounds.height);
    return this;
  }

  /**
   * Sets the element's bounds.
   * 
   * @param bounds the new bounds
   * @param content <code>true</code> to adjust for box model issues
   * @return this
   */
  public El setBounds(Rectangle bounds, boolean content) {
    setBounds(bounds.x, bounds.y, bounds.width, bounds.height, content);
    return this;
  }

  /**
   * Sets the CSS display property.
   * 
   * @param display true to display the element using its default display
   * @return this
   */
  public El setDisplayed(boolean display) {
    String value = display ? originalDisplay : "none";
    setStyleAttribute("display", value);
    return this;
  }

  /**
   * Sets the CSS display property.
   * 
   * @param display the display value
   * @return this
   */
  public El setDisplayed(String display) {
    setStyleAttribute("display", display);
    return this;
  }

  /**
   * Sets an element's attribute.
   * 
   * @param attr the attribute name
   * @param value the value
   * @return this
   */
  public El setElementAttribute(String attr, boolean value) {
    DOM.setElementPropertyBoolean(dom, attr, value);
    return this;
  }

  /**
   * Sets an element's attribute.
   * 
   * @param attr the attribute name
   * @param value the value
   * @return this
   */
  public El setElementAttribute(String attr, int value) {
    return setElementAttribute(attr, "" + value);
  }

  /**
   * Sets an element's attribute.
   * 
   * @param attr the attribute name
   * @param value the value
   * @return this
   */
  public El setElementAttribute(String attr, String value) {
    DOM.setElementAttribute(dom, attr, value);
    return this;
  }

  /**
   * True to focus, false to blur.
   * 
   * @param focus the new focus state
   */
  public native El setFocus(boolean focus) /*-{
      var dom = this.@com.extjs.gxt.ui.client.core.El::dom;
      try {
      if (focus) {
      dom.focus();
      } else {
      dom.blur();
      }
      } 
      catch(err) {
      }
      return this;
      }-*/;

  /**
   * Sets the elements height.
   * 
   * @param height the height
   * @return this
   */
  public El setHeight(int height) {
    setHeight(height + "px");
    return this;
  }

  /**
   * Sets the elements height.
   * 
   * @param height the height
   * @param adjust <code>true</code> to adjust for box model issues
   * @return this
   */
  public El setHeight(int height, boolean adjust) {
    if (height == Style.DEFAULT || height < 1) {
      return this;
    }
    if (adjust && !XDOM.isVisibleBox) {
      height -= getFrameWidth("tb");
    }
    setStyleAttribute("height", height + "px");
    return this;
  }

  /**
   * Sets the elements height.
   * 
   * @param height the height
   * @return this
   */
  public El setHeight(String height) {
    DOM.setStyleAttribute(dom, "height", addUnits(height));
    return this;
  }

  /**
   * Sets the the icon for an element either as a CSS style name or image path.
   * 
   * @param style the style are image path
   */
  public void setIconStyle(String style) {
    if (Util.isImagePath(style)) {
      setStyleAttribute("backgroundImage", "url(" + style + ")");
    } else {
      setStyleName(style);
    }
  }

  /**
   * Sets the element's id.
   * 
   * @param id the new id
   * @return this
   */
  public El setId(String id) {
    DOM.setElementProperty(dom, "id", id);
    return this;
  }

  /**
   * Sets the element's inner html.
   * 
   * @param html the new HTML
   * @return this
   */
  public El setInnerHtml(String html) {
    DOM.setInnerHTML(dom, html);
    return this;
  }

  /**
   * Sets an element's property.
   * 
   * @param property the property name
   * @param value the value
   * @return this
   */
  public El setIntElementProperty(String property, int value) {
    DOM.setElementPropertyInt(dom, property, value);
    return this;
  }

  /**
   * Sets the element's left position directly using CSS style (instead of
   * {@link #setX}).
   * 
   * @param left the left value
   * @return this
   */
  public El setLeft(int left) {
    DOM.setStyleAttribute(dom, "left", left + "px");
    return this;
  }

  /**
   * Quick set left and top adding default units.
   * 
   * @param left the left value
   * @param top the top value
   * @return this
   */
  public El setLeftTop(int left, int top) {
    setLeft(left);
    setTop(top);
    return this;
  }

  /**
   * Sets the elements's padding.
   * 
   * @param padding the padding
   * @return this
   */
  public El setPadding(Padding padding) {
    setStyleAttribute("paddingLeft", padding.left);
    setStyleAttribute("paddingTop", padding.top);
    setStyleAttribute("paddingRight", padding.right);
    setStyleAttribute("paddingBottom", padding.bottom);
    return this;
  }

  /**
   * Sets the element's position in page coordinates.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @return this
   */
  public El setPagePosition(int x, int y) {
    setX(x);
    setY(y);
    return this;
  }

  /**
   * Sets the element's horizontal scroll position.
   * 
   * @param left the left value
   * @return this
   */
  public El setScrollLeft(int left) {
    DOM.setElementPropertyInt(dom, "scrollLeft", left);
    return this;
  }

  /**
   * Sets the element's vertical scroll position.
   * 
   * @param top the top value
   * @return this
   */
  public El setScrollTop(int top) {
    DOM.setElementPropertyInt(dom, "scrollTop", top);
    return this;
  }

  /**
   * Sets the element's size.
   * 
   * @param width the new width
   * @param height the new height
   * @return this
   */
  public El setSize(int width, int height) {
    setSize(width, height, false);
    return this;
  }

  /**
   * Set the size of the element.
   * 
   * @param width the new width
   * @param height the new height
   * @param adjust <code>true</code> to adjust for box model issues
   * @return this
   */
  public El setSize(int width, int height, boolean adjust) {
    if (width != Style.DEFAULT) {
      setWidth(width, adjust);
    }
    if (height != Style.DEFAULT) {
      setHeight(height, adjust);
    }
    return this;
  }

  /**
   * Sets the element's size.
   * 
   * @param size the size
   * @return this
   */
  public El setSize(Size size) {
    setSize(size.width, size.height);
    return this;
  }

  /**
   * Sets the element's size.
   * 
   * @param width the new size
   * @param height the new height
   * @return this
   */
  public El setSize(String width, String height) {
    if (width != null && !width.equals(Style.UNDEFINED)) {
      DOM.setStyleAttribute(dom, "width", addUnits(width));
    }
    if (height != null && !height.equals(Style.UNDEFINED)) {
      DOM.setStyleAttribute(dom, "height", addUnits(height));
    }
    return this;
  }

  /**
   * Sets a style attribute.
   * 
   * @param attr the attribute name
   * @param value the int value
   * @return this
   */
  public El setStyleAttribute(String attr, Object value) {
    XElement.fly(dom).setStyleAttribute(attr, value);
    return this;
  }

  /**
   * Sets the element's style attribute.
   * 
   * @param attr the CSS style name
   * @param value the value
   * @return this
   */
  public El setStyleAttribute(String attr, String value) {
    XElement.fly(dom).setStyleAttribute(attr, value);
    return this;
  }

  /**
   * Sets the element's style name.
   * 
   * @param style the style name
   * @return this
   */
  public El setStyleName(String style) {
    DOM.setElementProperty(dom, "className", style);
    return this;
  }

  /**
   * Adds or removes the style name.
   * 
   * @param style the style name
   * @param add true to add, false to remove
   * @return this
   */
  public El setStyleName(String style, boolean add) {
    if (add) {
      addStyleName(style);
    } else {
      removeStyleName(style);
    }
    return this;
  }

  /**
   * Sets the element's size using style attributes.
   * 
   * @param width the width
   * @param height the height
   * @return this
   */
  public El setStyleSize(int width, int height) {
    setStyleAttribute("width", width);
    setStyleAttribute("height", height);
    return this;
  }

  /**
   * Sets the element's tab index.
   * 
   * @param index the tab index
   * @return this
   */
  public El setTabIndex(int index) {
    DOM.setElementPropertyInt(dom, "tabIndex", index);
    return null;
  }

  /**
   * Sets the element's title property.
   * 
   * @param title the new title
   * @return this
   */
  public El setTitle(String title) {
    DOM.setElementProperty(dom, "title", title);
    return this;
  }

  /**
   * Sets the element's top position directly using CSS style (instead of
   * {@link #setY}).
   * 
   * @param top the top value
   * @return this
   */
  public El setTop(int top) {
    DOM.setStyleAttribute(dom, "top", top + "px");
    return this;
  }

  /**
   * Sets the element's value property.
   * 
   * @param value the value
   */
  public El setValue(String value) {
    DOM.setElementProperty(dom, "value", value);
    return this;
  }

  /**
   * Sets the elements css 'visibility' property. Behavior is different than
   * using the 'display' property.
   * 
   * @param visible <code>true</code> to show, <code>false</code> to hide
   * @return this
   */
  public El setVisibility(boolean visible) {
    String value = visible ? "visible" : "hidden";
    DOM.setStyleAttribute(dom, "visibility", value);
    return this;
  }

  /**
   * Sets the element's visibility mode. When setVisible() is called it will use
   * this to determine whether to set the visibility or the display property.
   * 
   * @param visMode value {link VisMode#VISIBILITY}} or {@link VisMode#DISPLAY}
   * @return this
   */
  public El setVisibilityMode(VisMode visMode) {
    visiblityMode = visMode;
    return this;
  }

  /**
   * Sets the visibility of the element (see details). If the vis mode is set to
   * DISPLAY, it will use the display property to hide the element, otherwise it
   * uses visibility. The default is to hide and show using the DISPLAY
   * property.
   * 
   * @param visible whether the element is visible
   * @return this
   */
  public El setVisible(boolean visible) {
    if (visiblityMode == VisMode.DISPLAY) {
      setDisplayed(visible);
    } else {
      setVisibility(visible);
    }
    return this;
  }

  /**
   * Sets the element's width.
   * 
   * @param width the new width
   * @return this
   */
  public El setWidth(int width) {
    DOM.setStyleAttribute(dom, "width", width + "px");
    return this;
  }

  /**
   * Sets the elements's width.
   * 
   * @param width the new width
   * @param adjust <code>true</code> to adjust for box model issues
   * @return this
   */
  public El setWidth(int width, boolean adjust) {
    if (width == Style.DEFAULT || width < 1) {
      return this;
    }
    if (adjust && !XDOM.isVisibleBox) {
      width -= getFrameWidth("lr");
    }
    DOM.setStyleAttribute(dom, "width", width + "px");
    return this;
  }

  /**
   * Sets the element's width.
   * 
   * @param width the new width
   * @return this
   */
  public El setWidth(String width) {
    DOM.setStyleAttribute(dom, "width", addUnits(width));
    return this;
  }

  /**
   * Sets the X position of the element based on page coordinates. Element must
   * be part of the DOM tree to have page coordinates.
   * 
   * @param x the x coordinate
   * @return this
   */
  public El setX(int x) {
    XElement.fly(dom).setX(x);
    return this;
  }

  /**
   * Sets the elements position in page coordinates.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @return this
   */
  public El setXY(int x, int y) {
    setX(x);
    setY(y);
    return this;
  }

  /**
   * Sets the elements position in page coordinates.
   * 
   * @param x the x coordinate
   * @param y the y coordinate
   * @return this
   */
  public El setXY(int x, int y, FxConfig config) {
    if (config == null) {
      setXY(x, y);
    } else {
      Fx fx = new Fx(config);
      fx.run(new Move(this, x, y));
    }
    return this;
  }

  /**
   * Sets the element's position in page coordinates.
   * 
   * @param p the position
   * @return this
   */
  public El setXY(Point p) {
    setXY(p.x, p.y);
    return this;
  }

  /**
   * Sets the Y position of the element based on page coordinates. Element must
   * be part of the DOM tree to have page coordinates.
   * 
   * @param y the y coordinate
   * @return this
   */
  public native El setY(int y) /*-{
      var dom = this.@com.extjs.gxt.ui.client.core.El::dom;
      $wnd.GXT._el.fly(dom).setY(y);
      return this;
      }-*/;

  /**
   * Sets the element's z-index.
   * 
   * @param zIndex the z-index value
   * @return this
   */
  public El setZIndex(int zIndex) {
    DOM.setIntStyleAttribute(dom, "zIndex", Math.max(0, zIndex));
    return this;
  }

  /**
   * Slides the element in.
   * 
   * @param direction the direction
   * @param config the fx config
   * @return this
   */
  public El slideIn(Direction direction, FxConfig config) {
    BaseEffect.slideIn(this, config, direction);
    return this;
  }

  /**
   * Slides the element out.
   * 
   * @param direction the direction
   * @param config the fx config
   * @return this
   */
  public El slideOut(Direction direction, FxConfig config) {
    BaseEffect.slideOut(this, config, direction);
    return this;
  }

  /**
   * Returns the element's sub child.
   * 
   * @param depth the child node depth
   * @return the child element
   */
  public El subChild(int depth) {
    Element child = dom;
    while (depth-- > 0) {
      child = DOM.getChild(child, 0);
    }
    return fly(child);
  }

  /**
   * Synchronizes the layer.
   * 
   * @param show true to show
   */
  public void sync(boolean show) {
    // layer implements
  }

  public String toString() {
    return getOuterHtml();
  }

  /**
   * Return clipping (overflow) to original clipping before clip() was called.
   * 
   * @return this
   */
  public El unclip() {
    if (isClipped) {
      isClipped = false;
      setStyleAttribute("overflow", originalClipped[0]);
      setStyleAttribute("overflowX", originalClipped[1]);
      setStyleAttribute("overflowY", originalClipped[2]);
    }
    return this;
  }

  /**
   * Removes a previously applied mask.
   * 
   * return this
   */
  public El unmask() {
    if (_mask != null) {
      if (_maskMsg != null) {
        _maskMsg.remove();
        _maskMsg = null;
      }
      removeStyleName("x-masked");
      _mask.remove();
      _mask = null;
    }
    return this;
  }

  /**
   * Unwraps the child element.
   * 
   * @param bounds the original bounds
   */
  public void unwrap(Element child, Rectangle bounds) {
    El.fly(child).setLeftTop(bounds.x, bounds.y);
    Element p = dom.getParentElement().cast();
    int pos = DOM.getChildIndex(p, dom);
    p.removeChild(dom);
    DOM.insertChild(p, child, pos);
  }

  /**
   * Walks up the dom looking for a parent node that matches the passed simple
   * selector (e.g. div.some-class or span:first-child).
   * 
   * @param selector the simple selector to test
   * @param maxDepth The max depth to search as a number or element
   * @return the matching element
   */
  public El up(String selector, int maxDepth) {
    return findParentNode(selector, maxDepth);
  }

  /**
   * Sets the innerHTML to the given markup.
   * 
   * @param html the html
   * @return this
   */
  public El update(String html) {
    DOM.setInnerHTML(dom, html);
    return this;
  }

  /**
   * Sets the element's z-index using {@link XDOM#getTopZIndex()} to ensure it
   * has the highest values.
   * 
   * @param adj the adjustment to be applied to the z-index value
   * 
   * @return this
   */
  public El updateZIndex(int adj) {
    setZIndex(XDOM.getTopZIndex() + adj);
    return this;
  }

  /**
   * Wraps the element with the specified wrapper. The wrapper will have the
   * same size and position of the element. The original bounds can be used to
   * 'unwrap' the element.
   * 
   * @param wrapper the wrapper element
   * @return the original bounds
   */
  public Rectangle wrap(Element wrapper) {
    El wrap = new El(wrapper);
    wrap.setVisible(false);

    String pos = getStyleAttribute("position");
    wrap.setStyleAttribute("position", pos);

    int l = getLeft();
    int t = getTop();

    setLeft(5000);
    setVisible(true);

    int h = getComputedHeight();
    int w = getComputedWidth();

    setLeft(1);
    setStyleAttribute("overflow", "hidden");
    setVisible(false);

    wrap.insertBefore(dom);
    wrap.appendChild(dom);

    wrap.setStyleAttribute("overflow", "hidden");

    wrap.setLeft(l);
    wrap.setTop(t);

    setTop(0);
    setLeft(0);

    return new Rectangle(l, t, w, h);
  }

  private Point getConstrainToXY(Element elem, Point proposedXY) {
    int vw, vh, vx = 0, vy = 0;
    if (elem == XDOM.getBody()) {
      vw = XDOM.getViewportSize().width;
      vh = XDOM.getViewportSize().height;
    } else {
      vw = fly(elem).getWidth();
      vh = fly(elem).getHeight();
    }

    vx = getLeft();
    vy = getTop();

    Point xy = proposedXY;
    int x = xy.x;
    int y = xy.y;

    int vr = vx + vw;
    int vb = vy + vh;

    int w = getWidth();
    int h = getHeight();

    if ((x + w) > vr) {
      x = vr - w;
    }
    if ((y + h) > vb) {
      y = vb - h;

    }

    // then make sure top/left isn't negative
    if (x < vx) {
      x = vx;
    }
    if (y < vy) {
      y = vy;
    }

    return new Point(x, y);
  }
}
