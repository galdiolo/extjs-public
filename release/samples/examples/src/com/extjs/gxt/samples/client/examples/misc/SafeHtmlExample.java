/*
 * Sencha GXT 2.3.0 - Sencha for GWT
 * Copyright(c) 2007-2013, Sencha, Inc.
 * licensing@sencha.com
 * 
 * http://www.sencha.com/products/gxt/license/
 */
 package com.extjs.gxt.samples.client.examples.misc;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class SafeHtmlExample extends LayoutContainer implements EntryPoint {

  public SafeHtmlExample() {
    final ContentPanel panel = new ContentPanel();
    panel.setFrame(true);
    panel.setHeadingHtml("SafeHtml Example");
    panel.setSize(400, -1);
    
    final TextField<String> titleText = new TextField<String>();
    titleText.setValue("<div onclick='alert(\"XSS Vunerable\");'>Click Me</div>");
    titleText.setWidth(380);
    
    panel.add(titleText);
    
    
    Button text = new Button("Title as Text");
    text.addSelectionListener(new SelectionListener<ButtonEvent>() {
      
      @Override
      public void componentSelected(ButtonEvent ce) {
        panel.setHeadingText(titleText.getValue());
      }
    });
    
    Button html = new Button("Title as HTML");
    html.addSelectionListener(new SelectionListener<ButtonEvent>() {
      
      @Override
      public void componentSelected(ButtonEvent ce) {
        panel.setHeadingHtml(titleText.getValue());
      }
    });
    
    panel.addButton(text);
    panel.addButton(html);

    add(panel, new FlowData(10));
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    setLayout(new FlowLayout());
  }

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

}
