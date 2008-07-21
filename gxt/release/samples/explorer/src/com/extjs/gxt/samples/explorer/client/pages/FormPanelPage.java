/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.extjs.gxt.samples.resources.client.Stock;
import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class FormPanelPage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  public FormPanelPage() {
    // next 2 line is only used to pass layout to containing container
    // this will have NO effect outside of the explorer demo
    setData("layout", new FitLayout());
    
    setScrollMode(Scroll.AUTO);
    
    
    FormPanel panel = new FormPanel();
    panel.setFrame(true);
    panel.setFieldWidth(210);
    panel.setLabelWidth(100);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setHeading("Simple Form");
    panel.setIconStyle("icon-form");
    panel.setWidth(400);
    panel.setStyleAttribute("padding", "20");

    TextField text = new TextField();
    text.setFieldLabel("Name");
    text.setEmptyText("Enter your full name");
    text.setAllowBlank(false);
    text.setMinLength(4);
    panel.add(text);

    NumberField number = new NumberField();
    number.setFieldLabel("Age");
    number.setEmptyText("Enter your age");
    number.setAllowBlank(false);
    panel.add(number);

    text = new TextField();
    text.setFieldLabel("Email");
    panel.add(text);

    List<Stock> stocks = TestData.getStocks();
    Collections.sort(stocks, new Comparator<Stock>() {
      public int compare(Stock arg0, Stock arg1) {
        return arg0.getName().compareTo(arg1.getName());
      }
    });

    ListStore store = new ListStore();
    store.add(stocks);

    ComboBox combo = new ComboBox();
    combo.setFieldLabel("Company");
    combo.setDisplayField("name");
    combo.setStore(store);
    panel.add(combo);

    DateField date = new DateField();
    date.setFieldLabel("Birthday");
    panel.add(date);

    TimeField time = new TimeField();
    time.setFieldLabel("Time");
    panel.add(time);

    CheckBox check1 = new CheckBox();
    check1.setBoxLabel("Classical");

    CheckBox check2 = new CheckBox();
    check2.setBoxLabel("Rock");
    check2.setValue(true);

    CheckBox check3 = new CheckBox();
    check3.setFieldLabel("Blue");

    CheckBoxGroup checkGroup = new CheckBoxGroup();
    checkGroup.setFieldLabel("Music");
    checkGroup.add(check1);
    checkGroup.add(check2);
    checkGroup.add(check3);
    panel.add(checkGroup);

    Radio radio = new Radio();
    radio.setName("radio");
    radio.setFieldLabel("Red");
    radio.setValue(true);

    Radio radio2 = new Radio();
    radio2.setName("radio");
    radio2.setFieldLabel("Blue");

    RadioGroup radioGroup = new RadioGroup("test");
    radioGroup.setFieldLabel("Favorite Color");
    radioGroup.add(radio);
    radioGroup.add(radio2);
    panel.add(radioGroup);

    TextArea description = new TextArea();
    description.setPreventScrollbars(true);
    description.setFieldLabel("Description");
    panel.add(description);

    panel.addButton(new Button("Save"));
    panel.addButton(new Button("Cancel"));

    add(panel);
  }

}
