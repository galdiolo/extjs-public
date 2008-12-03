/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.resources.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class Country extends BaseModelData {

  public Country() {

  }

  public Country(String abbr, String name) {
    setAbbr(abbr);
    setName(name);
  }

  public String getName() {
    return get("name");
  }

  public void setName(String name) {
    set("name", name);
  }

  public String getAbbr() {
    return get("abbr");
  }

  public void setAbbr(String abbr) {
    set("abbr", abbr);
  }

}
