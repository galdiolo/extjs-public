/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.util.Rectangle;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Html;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Provides additional static methods that allow you to manipulate the browser's
 * Document Object Model (DOM).
 * 
 * @see DOM
 */
public final class XDOM {

  /**
   * Returns true if the browser uses a visible box.
   */
  public static boolean isVisibleBox;

  private static El bodyEl;
  private static Element escapeElement = DOM.createDiv();
  private static int scrollBarHeight = Style.DEFAULT;
  private static int autoId = 0;
  private static int zIndexId = 1000;

  static {
    GXT.init();
    isVisibleBox = isVisibleBoxInternal();
  }

  /**
   * Creates an element form the given markup.
   * 
   * @param html the markup
   * @return the new element
   */
  public static Element create(String html) {
    Element div = DOM.createDiv();
    DOM.setInnerHTML(div, html);
    Element firstChild = DOM.getFirstChild(div);
    // support text node creation
    return (firstChild != null) ? firstChild : div;
  }
  
  /**
   * Escapes HTML string and returns valid text that will be shown rather than
   * rendered.
   * 
   * @param html the HTML
   * @return the escaped HTML string
   */
  public static String escapeHtml(String html) {
    escapeElement.setInnerHTML(html);
    return escapeElement.getInnerHTML();
  }

  /**
   * Returns the body element.
   * 
   * @return the body
   */
  public static native Element getBody() /*-{
      return $doc.body;
    }-*/;

  /**
   * Returns the body El.
   * 
   * @return the body
   */
  public static El getBodyEl() {
    if (bodyEl == null) {
      bodyEl = new El(getBody());
    }
    return bodyEl;
  }

  /**
   * Reloads the page.
   */
  public native static void reload() /*-{
     $wnd.location.reload();
   }-*/;

  /**
   * Returns the body elements horizontal scroll.
   * 
   * @return the scroll amount in pixels
   */
  public static native int getBodyScrollLeft() /*-{
      return $doc.body.scrollLeft;
    }-*/;

  /**
   * Return the body elements vertical scroll.
   * 
   * @return the scroll amount in pixels
   */
  public static native int getBodyScrollTop() /*-{
      return $doc.body.scrollTop;
    }-*/;

  /**
   * Returns the element's bounds.
   * 
   * @param elem the element
   * @param content <code>true</code> to adjust for box model issues
   * @return the elements bounds
   */
  public static Rectangle getBounds(Element elem, boolean content) {
    int x = DOM.getAbsoluteLeft(elem);
    int y = DOM.getAbsoluteTop(elem);
    int width = El.fly(elem).getWidth();
    int height = El.fly(elem).getHeight();
    if (content) {
      x += El.fly(elem).getBorderWidth("l");
      y += El.fly(elem).getBorderWidth("t");
      width -= El.fly(elem).getFrameWidth("lr");
      height -= El.fly(elem).getFrameWidth("tb");
    }
    width = Math.max(0, width);
    height = Math.max(0, height);
    return new Rectangle(x, y, width, height);
  }

  /**
   * Returns the document element.
   * 
   * @return the docuemnt
   */
  public static native Element getDocument() /*-{
      return $doc;
    }-*/;

  /**
   * Returns the element with the unique id.
   * 
   * @param id the id
   * @return the element, or null if no match
   */
  public static Element getElementById(String id) {
    try {
      return DOM.getElementById(id);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Returns the HTML head element.
   * 
   * @return the head
   */
  public static native Element getHead() /*-{
      return $doc.getElementsByTagName('head')[0];
    }-*/;

  /**
   * Returns the width of the scroll bar.
   * 
   * @return the scroll bar width
   */
  public static int getScrollBarWidth() {
    if (scrollBarHeight == Style.DEFAULT) {
      scrollBarHeight = getScrollBarWidthInternal();
    }
    return scrollBarHeight;
  }

  /**
   * Increments and returns the top z-index value.
   * 
   * @return the z-index
   */
  public static int getTopZIndex() {
    return ++zIndexId;
  }

  /**
   * Returns an unique id.
   * 
   * @return the id
   */
  public static String getUniqueId() {
    return "x-auto-" + autoId++;
  }

  /**
   * Returns the viewports size.
   * 
   * @return the size
   */
  public static native Size getViewportSize() /*-{
     var vw;
     var vh;
     if (typeof $wnd.innerWidth != 'undefined') {
       vw = $wnd.innerWidth;
       vh = $wnd.innerHeight;
     } else if (typeof $doc.documentElement != 'undefined'
               && typeof $doc.documentElement.clientWidth !=
               'undefined' && $doc.documentElement.clientWidth != 0) {
       vw = $doc.documentElement.clientWidth;
       vh = $doc.documentElement.clientHeight;
     } else {
       vw = $doc.getElementsByTagName('body')[0].clientWidth;
       vh = $doc.getElementsByTagName('body')[0].clientHeight;
     }
     var size = @com.extjs.gxt.ui.client.util.Size::newInstance(II)(vw, vh);
     return size;
   }-*/;

  /**
   * Inserts this element after the passed element in the DOM.
   * 
   * @param elem the element
   * @param after the element to insert after
   */
  public native static void insertAfter(Element elem, Element after) /*-{
       after.parentNode.insertBefore(elem, after.nextSibling);
    }-*/;

  /**
   * Sets the element's style name.
   * 
   * @param elem the element
   * @param style the style name
   */
  public static void setStyleName(Element elem, String style) {
    DOM.setElementProperty(elem, "className", style);
  }

  private static int getScrollBarWidthInternal() {
    LayoutContainer wc = new LayoutContainer();
    RootPanel.get().add(wc);
    wc.el().setVisibility(false);
    wc.setScrollMode(Scroll.AUTO);
    wc.setSize(300, 300);

    Html html = new Html("sdff");
    html.setHeight("284");
    html.setWidth("500");

    wc.add(html);

    int height = 17;

    for (int i = 280; i < 300; i++) {
      html.setHeight("" + i);
      wc.setVScrollPosition(20);
      if (wc.getVScrollPosition() == 1) {
        height = 300 - i + 1;
      }
    }
    RootPanel.get().remove(wc);
    return height;
  }

  private static native boolean isVisibleBoxInternal() /*-{
      if (!$wnd.isVisibleBox) {
        var d = $wnd.document;
        var test = d.createElement('div');
        d.body.appendChild(test);
        test.style.position = "absolute";
        test.style.border = "2px solid";
        test.style.height = "50";
        $wnd.isVisibleValue = test.offsetHeight == 50 ? true : false;
        $wnd.isVisibleBox = true;
        d.body.removeChild(test);
      }
     return $wnd.isVisibleValue;
   }-*/;

  private XDOM() {

  }

}