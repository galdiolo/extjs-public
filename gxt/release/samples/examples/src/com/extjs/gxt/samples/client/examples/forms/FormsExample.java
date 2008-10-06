/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.forms;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.extjs.gxt.samples.resources.client.TestData;
import com.extjs.gxt.samples.resources.client.model.Stock;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class FormsExample extends LayoutContainer {

  private VerticalPanel vp;

  public FormsExample() {
    vp = new VerticalPanel();
    vp.setSpacing(10);

    createForm1();
    createForm2();
    add(vp);
  }

  private void createForm1() {
    FormPanel simple = new FormPanel();
    simple.setHeading("Simple Form");
    simple.setFrame(true);
    simple.setWidth(350);

    TextField<String> firstName = new TextField<String>();
    firstName.setFieldLabel("Name");
    firstName.setAllowBlank(false);
    simple.add(firstName);

    TextField<String> email = new TextField<String>();
    email.setFieldLabel("Email");
    simple.add(email);
    
    List<Stock> stocks = TestData.getStocks();
    Collections.sort(stocks, new Comparator<Stock>() {
      public int compare(Stock arg0, Stock arg1) {
        return arg0.getName().compareTo(arg1.getName());
      }
    });

    ListStore<Stock> store = new ListStore<Stock>();
    store.add(stocks);

    ComboBox<Stock> combo = new ComboBox<Stock>();
    combo.setFieldLabel("Company");
    combo.setDisplayField("name");
    combo.setStore(store);
    simple.add(combo);

    DateField date = new DateField();
    date.setFieldLabel("Birthday");
    simple.add(date);

    TimeField time = new TimeField();
    time.setFieldLabel("Time");
    simple.add(time);

    CheckBox check1 = new CheckBox();
    check1.setBoxLabel("Classical");

    CheckBox check2 = new CheckBox();
    check2.setBoxLabel("Rock");
    check2.setValue(true);

    CheckBox check3 = new CheckBox();
    check3.setBoxLabel("Blue");

    CheckBoxGroup checkGroup = new CheckBoxGroup();
    checkGroup.setFieldLabel("Music");
    checkGroup.add(check1);
    checkGroup.add(check2);
    checkGroup.add(check3);
    simple.add(checkGroup);

    Radio radio = new Radio();
    radio.setName("radio");
    radio.setBoxLabel("Red");
    radio.setValue(true);

    Radio radio2 = new Radio();
    radio2.setName("radio");
    radio2.setBoxLabel("Blue");

    RadioGroup radioGroup = new RadioGroup("test");
    radioGroup.setFieldLabel("Favorite Color");
    radioGroup.add(radio);
    radioGroup.add(radio2);
    simple.add(radioGroup);

    TextArea description = new TextArea();
    description.setPreventScrollbars(true);
    description.setFieldLabel("Description");
    simple.add(description);

    vp.add(simple);
  }

  private void createForm2() {
    FormPanel form2 = new FormPanel();
    form2.setFrame(true);
    form2.setHeading("Simple Form with FieldSets");
    form2.setWidth(350);
    form2.setLayout(new FlowLayout());

    FieldSet fieldSet = new FieldSet();
    fieldSet.setHeading("User Information");
    fieldSet.setCheckboxToggle(true);
    
    FormLayout layout = new FormLayout();
    layout.setLabelWidth(75);
    layout.setPadding(4);
    fieldSet.setLayout(layout);
    
    TextField<String> firstName = new TextField<String>();
    firstName.setFieldLabel("First Name");
    firstName.setAllowBlank(false);
    fieldSet.add(firstName);

    TextField<String> lastName = new TextField<String>();
    lastName.setFieldLabel("Last Name");
    fieldSet.add(lastName);

    TextField<String> company = new TextField<String>();
    company.setFieldLabel("Company");
    fieldSet.add(company);

    TextField<String> email = new TextField<String>();
    email.setFieldLabel("Email");
    fieldSet.add(email);
    
    form2.add(fieldSet);
    
    fieldSet = new FieldSet();
    fieldSet.setHeading("Phone Numbers");
    fieldSet.setCollapsible(true);
    
    layout = new FormLayout();
    layout.setLabelWidth(75);
    layout.setPadding(4);
    fieldSet.setLayout(layout);
    
    TextField<String> field = new TextField<String>();
    field.setFieldLabel("Home");
    fieldSet.add(field);

    field = new TextField<String>();
    field.setFieldLabel("Business");
    fieldSet.add(field);
    
    field = new TextField<String>();
    field.setFieldLabel("Mobile");
    fieldSet.add(field);
    
    field = new TextField<String>();
    field.setFieldLabel("Fax");
    fieldSet.add(field);
    
    form2.add(fieldSet);
    form2.setButtonAlign(HorizontalAlignment.CENTER);
    form2.addButton(new Button("Save"));
    form2.addButton(new Button("Cancel"));

    vp.add(form2);
  }

}
