/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.sencha.gxt.legacy.client.core.js;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Interface for objects that wrap a native javascript object.
 */
public interface JsWrapper {

  /**
   * Returns the javscript object.
   * 
   * @return the object
   */
  public JavaScriptObject getJsObject();

}
