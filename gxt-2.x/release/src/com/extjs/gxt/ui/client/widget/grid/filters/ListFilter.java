/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.grid.filters;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseListFilterConfig;
import com.extjs.gxt.ui.client.data.FilterConfig;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FilterEvent;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Util;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ListFilter extends Filter {

  private String displayProperty = "text";
  private ListMenu listMenu;

  public ListFilter(String dataIndex, ListStore store) {
    super(dataIndex);

    menu = new ListMenu(this, store);
    listMenu = (ListMenu) menu;
  }

  public String getDisplayProperty() {
    return displayProperty;
  }

  @Override
  public List<FilterConfig> getSerialArgs() {
    BaseListFilterConfig config = new BaseListFilterConfig("list", listMenu.getValue());
    return Util.createList(config);
  }

  @Override
  public Object getValue() {
    List<String> values = new ArrayList<String>();
    for (ModelData m : listMenu.getSelected()) {
      values.add((String) m.get(displayProperty));
    }
    return values;
  }

  @Override
  public boolean isActivatable() {
    return getValue() != null && ((List) getValue()).size() > 0;
  }

  public void setDisplayProperty(String displayProperty) {
    this.displayProperty = displayProperty;
  }

  @Override
  public void setValue(Object value) {
    listMenu.setSelected((List) value);
  }

  @Override
  public boolean validateModel(ModelData model) {
    String value = getModelValue(model);
    List<String> values = (List) getValue();
    return values.size() == 0 || values.contains(value);
  }

  protected void onCheckChange(MenuEvent be) {
    setActive(isActivatable(), false);
    fireEvent(Events.Update, new FilterEvent(this));
  }

}
