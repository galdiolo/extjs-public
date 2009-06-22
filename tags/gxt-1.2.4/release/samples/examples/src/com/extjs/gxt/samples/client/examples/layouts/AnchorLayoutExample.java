/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.layouts;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;

public class AnchorLayoutExample extends LayoutContainer {

  public AnchorLayoutExample() {
    setLayout(new FlowLayout(10));

    final Window w = new Window();
    w.setPlain(true);
    w.setSize(500, 300);
    w.setHeading("Resize Me");
    w.setLayout(new FitLayout());

    FormPanel panel = new FormPanel();
    panel.setBorders(false);
    panel.setBodyBorder(false);
    panel.setLabelWidth(55);
    panel.setPadding(5);
    panel.setHeaderVisible(false);

    TextField<String> field = new TextField<String>();
    field.setFieldLabel("Sent To");
    panel.add(field, new FormData("100%"));

    field = new TextField<String>();
    field.setFieldLabel("Subject");
    panel.add(field, new FormData("100%"));

    TextArea area = new TextArea();
    area.setHideLabel(true);
    panel.add(area, new FormData("100% -53"));

    w.addButton(new Button("Send"));
    w.addButton(new Button("Cancel"));
    w.add(panel);

    Button b = new Button("Open", new SelectionListener<ButtonEvent>() {

      @Override
      public void componentSelected(ButtonEvent ce) {
        w.show();
      }

    });
    add(b);
  }

}
