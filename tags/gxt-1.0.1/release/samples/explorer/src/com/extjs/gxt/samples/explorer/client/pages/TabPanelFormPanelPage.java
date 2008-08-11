package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class TabPanelFormPanelPage extends LayoutContainer {

  public TabPanelFormPanelPage() {
    setLayout(new FlowLayout(10));
    
    FormPanel panel = new FormPanel();
    panel.setFrame(false);
    panel.setHeaderVisible(false);
    panel.setBodyBorder(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setLayout(new FitLayout());

    TabPanel tabs = new TabPanel();

    TabItem personal = new TabItem();
    personal.setText("Personal Details");
    personal.setLayout(new FormLayout());

    TextField<String> name = new TextField<String>();
    name.setFieldLabel("First Name");
    name.setValue("Darrell");
    personal.add(name);

    TextField<String> last = new TextField<String>();
    last.setFieldLabel("Last Name");
    last.setValue("Meyer");
    personal.add(last);

    TextField<String> company = new TextField<String>();
    company.setFieldLabel("Company");
    personal.add(company);

    TextField<String> email = new TextField<String>();
    email.setFieldLabel("Email");
    personal.add(email);

    tabs.add(personal);

    TabItem numbers = new TabItem();
    numbers.setText("Phone Numbers");
    numbers.setLayout(new FormLayout());

    TextField<String> home = new TextField<String>();
    home.setFieldLabel("Home");
    home.setValue("800-555-1212");
    numbers.add(home);

    TextField<String> business = new TextField<String>();
    business.setFieldLabel("Business");
    numbers.add(business);

    TextField<String> mobile = new TextField<String>();
    mobile.setFieldLabel("Mobile");
    numbers.add(mobile);

    TextField<String> fax = new TextField<String>();
    fax.setFieldLabel("Fax");
    numbers.add(fax);

    tabs.add(numbers);

    panel.add(tabs);
    panel.addButton(new Button("Cancel"));
    panel.addButton(new Button("Submit"));

    panel.setSize(340, 180);
    
    add(panel);
  }
  
}
