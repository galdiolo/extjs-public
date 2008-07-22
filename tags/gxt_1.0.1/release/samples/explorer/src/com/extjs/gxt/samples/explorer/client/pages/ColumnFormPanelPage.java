/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class ColumnFormPanelPage extends LayoutContainer {

  public ColumnFormPanelPage() {
    setLayout(new FlowLayout(10));

    FormPanel panel = new FormPanel();
    panel.setFrame(true);
    panel.setIconStyle("icon-form");
    panel.setCollapsible(true);
    panel.setHeading("FormPanel");
    panel.setSize(470, -1);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setLayout(new FlowLayout());

    LayoutContainer main = new LayoutContainer();
    main.setWidth(450);
    main.setLayout(new ColumnLayout());

    LayoutContainer left = new LayoutContainer();

    FormLayout layout = new FormLayout();
    layout.setLabelAlign(LabelAlign.TOP);
    left.setLayout(layout);

    TextField first = new TextField();
    first.setFieldLabel("First Name");
    first.setData("anchorSpec", "-20");
    left.add(first);

    TextField company = new TextField();
    company.setFieldLabel("Company");
    company.setData("anchorSpec", "-20");
    left.add(company);

    DateField birthday = new DateField();
    birthday.setFieldLabel("Birthday");
    birthday.setData("anchorSpec", "-20");
    left.add(birthday);

    LayoutContainer right = new LayoutContainer();

    layout = new FormLayout();
    layout.setLabelAlign(LabelAlign.TOP);
    right.setLayout(layout);

    TextField last = new TextField();
    last.setFieldLabel("Last");
    last.setData("anchorSpec", "-20");
    right.add(last);

    TextField email = new TextField();
    email.setFieldLabel("Email");
    email.setData("anchorSpec", "-20");
    right.add(email);

    Radio radio1 = new Radio();
    radio1.setBoxLabel("Yes");

    Radio radio2 = new Radio();
    radio2.setBoxLabel("No");

    RadioGroup group = new RadioGroup();
    group.setFieldLabel("Ext GWT User");
    group.add(radio1);
    group.add(radio2);
    right.add(group);

    main.add(left, new ColumnData(.5));
    main.add(right, new ColumnData(.5));

    panel.add(main);

    LayoutContainer area = new LayoutContainer();
    area.setWidth(450);

    layout = new FormLayout();
    layout.setLabelAlign(LabelAlign.TOP);

    area.setLayout(layout);

    TextArea a = new TextArea();
    a.setFieldLabel("Comment");
    
    String adj = GXT.isIE ? "-20" : "-30"; // looking into
    
    a.setData("anchorSpec", adj);
    area.add(a);

    panel.add(area);

    panel.addButton(new Button("Cancel"));
    panel.addButton(new Button("Submit"));

    add(panel);

  }

}
