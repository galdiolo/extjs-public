package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.samples.explorer.client.Explorer;
import com.extjs.gxt.samples.explorer.client.model.Entry;
import com.extjs.gxt.samples.explorer.client.model.ExplorerModel;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.DataViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.DataView;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class OverviewPage extends Container {

  private DataView dataView;
  
  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    
    ExplorerModel model = (ExplorerModel) Registry.get("model");
    Store store = new Store();
    store.add(model.getEntries());

    StringBuffer sb = new StringBuffer();
    sb.append("<div class='sample-box'>");
    sb.append("<div class='thumbd'><img src='{path}' width=120 height90></div>");
    sb.append("<div>{name}</div>");
    sb.append("</div>");

    dataView = new DataView();
    dataView.itemSelector = ".sample-box";
    dataView.overStyle = "sample-over";
    dataView.selectStyle = "none";
    dataView.setBorders(false);
    dataView.setStore(store);
    dataView.setTemplate(sb.toString());
    dataView.addListener(Events.SelectionChange, new Listener<DataViewEvent>() {
      public void handleEvent(DataViewEvent be) {
        Record record = (Record) dataView.getSelection().get(0);
        Entry entry = (Entry)record.getModel();
        Explorer.showPage(entry);
      }
    });

    add(dataView);
    
    Widget p = getParent();
    if (p instanceof Container) {
      Container ct = (Container) p;
      ct.setScrollMode(Scroll.AUTO);
    }

  }
}
