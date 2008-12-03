package com.extjs.gxt.samples.explorer.client;

import com.extjs.gxt.samples.client.ExamplesModel;
import com.extjs.gxt.samples.client.examples.model.Entry;
import com.extjs.gxt.samples.explorer.client.pages.OverviewPage;

public class ExplorerModel extends ExamplesModel {

  public ExplorerModel() {
    set("overview", new Entry("Overview", new OverviewPage(), true, false));
  }

}
