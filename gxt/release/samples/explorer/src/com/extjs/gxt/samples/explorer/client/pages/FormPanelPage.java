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
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.widget.Button;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class FormPanelPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    FormPanel panel = new FormPanel();
    panel.frame = true;
    panel.fieldWidth = 210;
    panel.labelWidth = 100;
    panel.buttonAlign = HorizontalAlignment.CENTER;
    panel.setHeading("Simple Form");
    panel.setIconStyle("icon-form");
    panel.setWidth(400);
    panel.setStyleAttribute("padding", "20");

    TextField text = new TextField();
    text.fieldLabel = "Name";
    text.setEmptyText("Enter your full name");
    text.setAllowBlank(false);
    text.setMinLength(4);
    panel.add(text);

    NumberField number = new NumberField();
    number.fieldLabel = "Age";
    number.setEmptyText("Enter your age");
    number.setAllowBlank(false);
    panel.add(number);

    text = new TextField();
    text.fieldLabel = "Email";
    panel.add(text);

    List<Stock> stocks = TestData.getStocks();
    Collections.sort(stocks, new Comparator<Stock>() {
      public int compare(Stock arg0, Stock arg1) {
        return arg0.getName().compareTo(arg1.getName());
      }
    });

    Store store = new Store();
    store.add(stocks);

    ComboBox combo = new ComboBox();
    combo.fieldLabel = "Company";
    combo.displayField = "name";
    combo.setWidth(210);
    combo.setStore(store);
    panel.add(combo);

    DateField date = new DateField();
    date.fieldLabel = "Birthday";
    date.setWidth(210);
    panel.add(date);

    TimeField time = new TimeField();
    time.fieldLabel = "Time";
    time.setWidth(200);
    panel.add(time);

    CheckBox check1 = new CheckBox();
    check1.boxLabel = "Classical";

    CheckBox check2 = new CheckBox();
    check2.boxLabel = "Rock";

    CheckBox check3 = new CheckBox();
    check3.boxLabel = "Blue";

    CheckBoxGroup checkGroup = new CheckBoxGroup();
    checkGroup.fieldLabel = "Music";
    checkGroup.add(check1);
    checkGroup.add(check2);
    checkGroup.add(check3);
    panel.add(checkGroup);

    Radio radio = new Radio();
    radio.name = "radio";
    radio.fieldLabel = "Red";

    Radio radio2 = new Radio();
    radio2.name = "radio";
    radio2.fieldLabel = "Blue";

    RadioGroup radioGroup = new RadioGroup("test");
    radioGroup.fieldLabel = "Favorite Color";
    radioGroup.add(radio);
    radioGroup.add(radio2);
    panel.add(radioGroup);

    panel.addButton(new Button("Save"));
    panel.addButton(new Button("Cancel"));

    add(panel);
  }

}
