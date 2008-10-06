/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

public class BaseGroupingLoadConfig extends BaseListLoadConfig implements GroupingLoadConfig {

  public String getGroupBy() {
    return (String)get("groupBy");
  }

  public void setGroupBy(String groupBy) {
    set("groupBy", groupBy);
  }

}
