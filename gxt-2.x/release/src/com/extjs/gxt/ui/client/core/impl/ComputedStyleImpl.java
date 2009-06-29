/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.core.impl;

import java.util.Map;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.FastMap;
import com.extjs.gxt.ui.client.util.Format;
import com.google.gwt.user.client.Element;

public class ComputedStyleImpl {

  public String getStyleAttribute(El elem, String name) {
    return getStyleAttribute(elem.dom, name);
  }

  protected Map<String, String> camelCache = new FastMap<String>();
  protected Map<String, String> hyphenCache = new FastMap<String>();

  public String getStyleAttribute(Element elem, String name) {
    return getComputedStyle(elem, checkHyphenCache(name), checkCamelCache(name), "");
  }

  public void setStyleAttribute(Element elem, String name, Object value) {
    elem.getStyle().setProperty(checkCamelCache(name), String.valueOf(value));
  }

  protected String checkCamelCache(String s) {
    String t = camelCache.get(s);
    if (t == null) {
      t = Format.camelize(getPropertyName(s));
      camelCache.put(s, t);
    }
    return t;
  }

  protected String checkHyphenCache(String s) {
    String t = hyphenCache.get(s);
    if (t == null) {
      t = Format.hyphenize(getPropertyName(s));
      hyphenCache.put(s, t);
    }
    return t;
  }

  protected String getPropertyName(String name) {
    if ("float".equals(name)) {
      return "cssFloat";
    }
    return name;
  }

  protected native String getComputedStyle(Element elem, String name, String name2, String pseudo) /*-{
    var v = elem.style[name2];
    if(v){
      return v;
    }
    var cStyle = $doc.defaultView.getComputedStyle(elem, pseudo);
    return cStyle ? String(cStyle.getPropertyValue(name)) : null;
  }-*/;

}
