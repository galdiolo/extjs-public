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
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.util.ThemeManager;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.Element;

/**
 * A combo box for selecting the GXT themes. Changing themes will cause the
 * application to be reloaded.
 */
public class ThemeSelector extends ComboBox<Theme> {

  public ThemeSelector() {
    setEditable(false);
    setDisplayField("name");
    setWidth("100px");
  }

  @Override
  protected void beforeRender() {
    super.beforeRender();
    ListStore store = new ListStore();
    store.add(ThemeManager.getThemes());
    setStore(store);

    String theme = GXT.getThemeId();
    if (theme == null) {
      setValue(Theme.BLUE);
    }
    Theme r = findModel("id", theme);
    if (r != null) {
      setValue(r);
    }
  }

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    addListener(Events.Change, new Listener<FieldEvent>() {
      public void handleEvent(FieldEvent be) {
        Theme t = (Theme)be.value;
        GXT.switchTheme(t);
      }
    });

  }
}
