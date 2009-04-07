/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.dnd;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.google.gwt.user.client.Element;

public class DualListFieldExample extends LayoutContainer {

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    setStyleAttribute("margin", "10px");

    FormPanel panel = new FormPanel();
    panel.setHeading("Drag and Drop Lists");

    DualListField lists = new DualListField();
    lists.setFieldLabel("Stocks");

    ListField from = lists.getFromList();
    from.setDisplayField("name");
    ListStore store = new ListStore();
    store.setStoreSorter(new StoreSorter());
    store.add(TestData.getShortStocks());
    from.setStore(store);

    ListField to = lists.getToList();
    to.setDisplayField("name");
    store = new ListStore();
    store.setStoreSorter(new StoreSorter());
    to.setStore(store);

    panel.add(lists);
    panel.setWidth(550);

    add(panel);
  }

}
