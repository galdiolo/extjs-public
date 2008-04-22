/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.custom;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.Record;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.util.Theme;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.Element;

/**
 * A combo box for selecting the GXT themes. Changing themes will cause the
 * application to be reloaded.
 */
public class ThemeSelector extends ComboBox {

  private List<Theme> themes;

  public ThemeSelector() {
    setEditable(false);
    valueField = "id";
    displayField = "name";
    setWidth("100px");
  }

  /**
   * Sets the list of themes.
   * 
   * @param themes the themes
   */
  public void setThemes(List<Theme> themes) {
    this.themes = themes;
  }

  protected List<Theme> createThemes() {
    List<Theme> themes = new ArrayList<Theme>();
    themes.add(Theme.BLUE);
    themes.add(Theme.GRAY);
    return themes;
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

    if (themes == null) {
      themes = createThemes();
    }

    Store store = new Store();
    store.add(themes);
    setStore(store);

    super.onRender(parent, index);

    String theme = GXT.getThemeId();
    if (theme == null) {
      setValue(Theme.BLUE);
    }
    Record r = findRecord(valueField, theme);
    if (r != null) {
      setValue(r);
    }
  }
}
