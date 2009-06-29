/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.dnd;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DualListField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.Element;

public class DualListFieldExample extends LayoutContainer {

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    setStyleAttribute("margin", "10px");

    FormPanel panel = new FormPanel();
    panel.setHeading("Drag and Drop Lists");

    final DualListField<Stock> lists = new DualListField<Stock>();

    lists.setFieldLabel("Stocks");

    ListField<Stock> from = lists.getFromList();
    from.setDisplayField("name");
    ListStore<Stock> store = new ListStore<Stock>();
    store.setStoreSorter(new StoreSorter<Stock>());
    store.add(TestData.getShortStocks());
    from.setStore(store);
    ListField<Stock> to = lists.getToList();
    to.setDisplayField("name");
    store = new ListStore<Stock>();
    store.setStoreSorter(new StoreSorter<Stock>());
    to.setStore(store);

    panel.add(lists, new FormData("98%"));
    panel.setWidth(550);

    add(panel);

  }

}
