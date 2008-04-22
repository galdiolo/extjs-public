/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;


import com.extjs.gxt.ui.client.XDOM;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * Provides functions for removing and adding stylesheets.
 */
public class CSS {

  /**
   * Removes a style or link tag by id.
   * 
   * @param id the id of the tag
   */
  public static void removeStyleSheet(String id) {
    Element elem = DOM.getElementById(id);
    if (elem != null) {
      Element p = DOM.getParent(elem);
      DOM.setElementProperty(p, "disabled", "disabled");
      DOM.removeChild(p, elem);
    }
  }

  /**
   * Adds a stylesheet to the document.
   * 
   * @param id the id
   * @param url the stylesheet url
   */
  public static void addStyleSheet(String id, String url) {
    Element link = DOM.createElement("link");
    DOM.setElementProperty(link, "rel", "stylesheet");
    DOM.setElementProperty(link, "type", "text/css");
    DOM.setElementProperty(link, "id", id);
    DOM.setElementProperty(link, "href", url);
    DOM.setElementProperty(link, "disabled", "");
    Element elem = XDOM.getHead();
    DOM.appendChild(elem, link);
  }

  /**
   * Adds a rules string in a <code>&ltdstyle&gt&ltd/style&gt</code> element.
   * 
   * @param style the <code>&ltdstyle&gt&ltd/style&gt</code> element
   * @param cssStr the rules string
   */
  native public static void setRules(Element style, String cssStr) /*-{
   style.setAttribute("type", "text/css");
   if(style.styleSheet){// IE
    style.styleSheet.cssText = cssStr;
   } else {// w3c
    while (style.firstChild) {
     style.removeChild(style.firstChild);
    }
    var cssText = $doc.createTextNode(cssStr);
    style.appendChild(cssText);
   }
  }-*/;

}
