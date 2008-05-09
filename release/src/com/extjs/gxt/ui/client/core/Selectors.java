/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p/>Marker interface for classes that can select El elements from a given
 * root element
 * 
 * <p/>The method signiture for methods should be El methodName(El element).
 */
public interface Selectors {

  /**
   * Used on {@link Selectors} subinterface methods to specity the css selector
   * to use
   */
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.CLASS)
  public @interface Selector {
    String value();
  }
}
