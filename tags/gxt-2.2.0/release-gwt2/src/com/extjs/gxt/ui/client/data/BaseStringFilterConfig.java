/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

public class BaseStringFilterConfig extends BaseFilterConfig {

  public BaseStringFilterConfig() {
    super();
  }

  public BaseStringFilterConfig(String type, Object value) {
    super(type, value);
  }

  public BaseStringFilterConfig(String type, String comparison, Object value) {
    super(type, comparison, value);
  }

  public boolean isFiltered(ModelData model, Object test, String comparison, Object value) {
    String t = (String) test;
    String v = (String)value;
    if (value == null) {
      return false;
    }
    return v.toLowerCase().indexOf(t) == -1;
  }

}
