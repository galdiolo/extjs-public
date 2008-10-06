/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.data;

public class QueryLoadConfig {

  private String query;
  private boolean forceAll;
  
  public boolean isForceAll() {
    return forceAll;
  }
  public void setForceAll(boolean forceAll) {
    this.forceAll = forceAll;
  }
  public String getQuery() {
    return query;
  }
  public void setQuery(String query) {
    this.query = query;
  }
  
  
}
