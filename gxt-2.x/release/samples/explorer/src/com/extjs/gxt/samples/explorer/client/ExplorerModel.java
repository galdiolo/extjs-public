/*
 * Sencha GXT 2.3.0 - Sencha for GWT
 * Copyright(c) 2007-2013, Sencha, Inc.
 * licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
 package com.extjs.gxt.samples.explorer.client;

import com.extjs.gxt.samples.client.ExamplesModel;
import com.extjs.gxt.samples.client.examples.model.Entry;
import com.extjs.gxt.samples.explorer.client.pages.OverviewPage;

public class ExplorerModel extends ExamplesModel {

  public ExplorerModel() {
    set("overview", new Entry("Overview", new OverviewPage(), null, true, false));
  }

}
