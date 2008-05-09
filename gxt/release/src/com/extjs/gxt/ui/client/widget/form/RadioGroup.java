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

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.util.WidgetHelper;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * A group of Radio's.
 */
public class RadioGroup extends Field {

  private String name;
  private List<Radio> radios;
  private HorizontalPanel hp;
  private static int autoId = 0;

  /**
   * Creates a new radio group.
   */
  public RadioGroup() {
    this.name = "gxt.RadioGroup." + (autoId++);
    initComponent();
  }

  /**
   * Creates a new radio group.
   * 
   * @param name the group name
   */
  public RadioGroup(String name) {
    this.name = name;
    initComponent();
  }

  /**
   * Adds a radio to the group.
   * 
   * @param radio the radio to add
   */
  public void add(Radio radio) {
    radios.add(radio);
  }

  @Override
  public String getName() {
    return name;
  }

  /**
   * Returns the list of radios.
   * 
   * @return the list of radios
   */
  public List<Radio> getRadios() {
    return radios;
  }

  @Override
  public void onComponentEvent(ComponentEvent ce) {

  }

  @Override
  public void onBrowserEvent(Event event) {

  }

  @Override
  protected void doAttachChildren() {
    WidgetHelper.doAttach(hp);
  }

  @Override
  protected void doDetachChildren() {
    WidgetHelper.doDetach(hp);
  }

  protected void initComponent() {
    radios = new ArrayList<Radio>();
    baseStyle = "x-form-group";
  }

  protected void onRadioClick(Radio radio) {
    for (Radio r : radios) {
      if (r == radio) {
        r.setChecked(true);
      } else {
        r.setChecked(false);
      }
    }
  }

  @Override
  public Object getValue() {
    for (Radio r : radios) {
      if (r.isChecked()) {
        return r.getValue();
      }
    }
    return null;
  }

  @Override
  public void setValue(Object value) {
    for (Radio r : radios) {
      r.setChecked(value.equals(r.getValue()));
    }
  }

  @Override
  protected void onRender(Element parent, int index) {
    hp = new HorizontalPanel();
    hp.setStyleAttribute("paddingTop", "3px");

    for (Radio r : radios) {
      r.group = this;
      r.setName(name);
      hp.add(r);
      if (r.getFieldLabel() != null) {
        Text lbl = new Text(r.getFieldLabel());
        lbl.setStyleName("x-form-group-label");
        hp.add(lbl);
      }
    }

    hp.render(parent, index);
    setElement(hp.getElement());
  }

}
