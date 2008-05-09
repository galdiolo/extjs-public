/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.custom;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.Element;

/**
 * A combo box for selecting the GXT themes. Changing themes will cause the
 * application to be reloaded.
 */
public class ThemeSelector extends ComboBox {

  public ThemeSelector() {
    setEditable(false);
    setValueField("id");
    setDisplayField("name");
    setWidth("100px");
  }

  @Override
  protected void onRender(Element parent, int index) {
    addListener(Events.Select, new Listener<FieldEvent>() {
      public void handleEvent(FieldEvent be) {
        Theme t = (Theme) getSelectedRecord();
        if (t != getValue()) {
          GXT.switchTheme(t);
        }
      }
    });

    Store store = new Store();
    store.add(ThemeManager.getThemes());
    setStore(store);

    super.onRender(parent, index);

    String theme = GXT.getThemeId();
    if (theme == null) {
      setValue(Theme.BLUE);
    }
    Record r = findRecord(getValueField(), theme);
    if (r != null) {
      setValue(r);
    }
  }
}
