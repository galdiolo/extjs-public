/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget.form;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A group of CheckBox's.
 */
public class CheckBoxGroup extends Field {

  private List<CheckBox> checks;
  private HorizontalPanel hp;

  /**
   * Creates a new checkbox group.
   */
  public CheckBoxGroup() {
    checks = new ArrayList<CheckBox>();
    baseStyle = "x-form-group";
  }

  @Override
  protected void doAttachChildren() {
    WidgetHelper.doAttach(hp);
  }

  @Override
  protected void doDetachChildren() {
    WidgetHelper.doDetach(hp);
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {

  }

  @Override
  protected El getInputEl() {
    if (checks.size() > 0) {
      return checks.get(0).getInputEl();
    }
    return super.getInputEl();
  }

  @Override
  public void onBrowserEvent(Event event) {

  }

  /**
   * Adds a checkbox to the group.
   * 
   * @param checkBox the checkbox to add
   */
  public void add(CheckBox checkBox) {
    checks.add(checkBox);
  }

  @Override
  protected void onRender(Element target, int index) {
    hp = new HorizontalPanel();
    hp.setStyleAttribute("paddingTop", "3px");

    for (CheckBox cb : checks) {
      hp.add(cb);
      if (cb.getFieldLabel() != null) {
        Text lbl = new Text(cb.getFieldLabel());
        lbl.setStyleName("x-form-group-label");
        hp.add(lbl);
      }
    }

    hp.render(target, index);
    setElement(hp.getElement());
  }

}
