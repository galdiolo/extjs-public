/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModel;

public class Stock extends BaseModel {

  public Stock() {
  }

  public Stock(String name, String symbol, double open, double last) {
    set("name", name);
    set("symbol", symbol);
    set("open", open);
    set("last", last);
    set("date", new Date());
    set("change", last - open);
  }

  public Date getLastTrans() {
    return (Date) get("date");
  }

  public String getName() {
    return (String) get("name");
  }

  public String getSymbol() {
    return (String) get("symbol");
  }

  public double getOpen() {
    Double open = (Double) get("open");
    return open.doubleValue();
  }

  public double getLast() {
    Double open = (Double) get("last");
    return open.doubleValue();
  }

  public double getChange() {
    return getLast() - getOpen();
  }

  public double getPercentChange() {
    return getChange() / getOpen();
  }

  public String toString() {
    return getName();
  }

}
