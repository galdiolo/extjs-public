/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.util;

import java.util.Iterator;
import java.util.Map;

/**
 * Formatting functions.
 */
public class Format {

  /**
   * Substitutes the indexed parameters.
   * 
   * @param text the text
   * @param param the parameter
   * @return the new text
   */
  public static String substitute(String text, int param) {
    return substitute(text, safeRegexReplacement("" + param));
  }

  /**
   * Substitutes the named parameters. The passed keys and values must be
   * Strings.
   * 
   * @param text the text
   * @param params the parameters
   * @return the new text
   */
  public static String substitute(String text, Map<String, Object> params) {
    Iterator<String> it = params.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      text = text.replaceAll("\\{" + key + "}", safeRegexReplacement(params.get(key).toString()));
    }
    return text;
  }

  public static String substitute(String text, Params params) {
    if (params.isMap) {
      return substitute(text, params.getMap());
    } else if (params.isList) {
      return substitute(text, params.getList().toArray());
    }
    return text;

  }

  /**
   * Substitutes the indexed parameters.
   * 
   * @param text the text
   * @param params the parameters
   * @return the new text
   */
  public static String substitute(String text, Object... params) {
    for (int i = 0; i < params.length; i++) {
      Object p = params[i];
      if (p == null) {
        p = "";
      }
      text = text.replaceAll("\\{" + i + "}", safeRegexReplacement(p.toString()));
    }
    return text;
  }

  /**
   * Substitutes the named parameters. The passed keys and values must be
   * Strings.
   * 
   * @param text the text
   * @param keys the parameter names
   * @param params the parameter values
   * @return the new text
   */
  public static String substitute(String text, String[] keys, Map params) {
    for (int i = 0; i < keys.length; i++) {
      text = text.replaceAll("\\{" + keys[i] + "}",
          safeRegexReplacement((String) params.get(keys[i])));
    }
    return text;
  }

  /**
   * Replace any \ or $ characters in the replacement string with an escaped \\
   * or \$.
   * 
   * @param replacement the regular expression replacement text
   * @return null if replacement is null or the result of escaping all \ and $
   *         characters
   */
  private static String safeRegexReplacement(String replacement) {
    if (replacement == null) {
      return replacement;
    }

    return replacement.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\$", "\\\\\\$");
  }
}
