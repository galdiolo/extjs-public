/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Element;

/**
 * Sets multiple CSS rules on a style element.
 */
public class StyleTemplate {
  private Element styleElem;
  private Map ruleMap = new HashMap();

  /**
   * Creates a new template.
   * 
   * @param styleElem the HTML style element
   */
  public StyleTemplate(Element styleElem) {
    this.styleElem = styleElem;
  }

  /**
   * Sets a new rule.
   * 
   * @param selector the CSS selector
   * @param rule the rule
   */
  public void set(String selector, String rule) {
    if (rule == null) {
      ruleMap.remove(selector);
    } else {
      ruleMap.put(selector, rule);
    }
  }

  /**
   * Applies the rules to the element.
   */
  public void apply() {
    StringBuffer sb = new StringBuffer();
    for (Iterator iter = ruleMap.entrySet().iterator(); iter.hasNext();) {
      Map.Entry entry = (Entry) iter.next();
      sb.append("\n" + entry.getKey()).append(" {").append(entry.getValue()).append("}");
    }
    CSS.setRules(styleElem, sb.toString());
  }
};