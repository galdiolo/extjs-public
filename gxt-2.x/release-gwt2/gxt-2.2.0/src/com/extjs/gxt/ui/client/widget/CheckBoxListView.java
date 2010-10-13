/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Element;

public class CheckBoxListView<M extends ModelData> extends ListView<M> {

  protected List<M> checkedPreRender;
  private String checkBoxSelector = ".x-view-item-checkbox";

  public String getCheckBoxSelector() {
    return checkBoxSelector;
  }

  public List<M> getChecked() {
    if (rendered) {
      List<M> l = new ArrayList<M>();
      NodeList<Element> nodes = el().select(checkBoxSelector);
      for (int i = 0; i < nodes.getLength(); i++) {
        if (InputElement.is(nodes.getItem(i))) {
          InputElement e = InputElement.as(nodes.getItem(i));
          if (e.isChecked()) {
            l.add(getStore().getAt(i));
          }
        }
      }
      return l;
    } else {
      return checkedPreRender != null ? new ArrayList<M>(checkedPreRender) : new ArrayList<M>();
    }
  }

  public void setCheckBoxSelector(String checkBoxSelector) {
    this.checkBoxSelector = checkBoxSelector;
  }

  /**
   * Selects a specific item in the view
   * 
   * @param m the modeldata that should be checked
   * @param checked true to check
   */
  public void setChecked(M m, boolean checked) {
    if (rendered) {
      NodeList<Element> nodes = el().select(checkBoxSelector);
      int index = store.indexOf(m);
      if (index != -1) {
        Element e = nodes.getItem(index);
        if (e != null) {
          ((InputElement) e.cast()).setChecked(checked);
        }
      }
    } else {
      if (checkedPreRender == null) {
        checkedPreRender = new ArrayList<M>();
      }
      if (checked) {
        if (!checkedPreRender.contains(m)) {
          checkedPreRender.add(m);
        }
      } else {
        checkedPreRender.remove(m);
      }
    }
  }

  @Override
  protected void afterRender() {
    super.afterRender();
    if (checkedPreRender != null) {
      for (M m : checkedPreRender) {
        setChecked(m, true);
      }
      checkedPreRender = null;
    }
  }

  @Override
  protected void onRender(Element target, int index) {
    if (getTemplate() == null) {
      String spacing = GXT.isIE ? "0" : "3";
      setTemplate(XTemplate.create("<tpl for=\".\"><div class='x-view-item x-view-item-check'><table cellspacing='"
          + spacing
          + "' cellpadding=0><tr><td><input class=\"x-view-item-checkbox\" type=\"checkbox\" /></td><td><td>{"
          + getDisplayProperty() + "}</td></tr></table></div></tpl>"));
    }
    super.onRender(target, index);
  }
}
