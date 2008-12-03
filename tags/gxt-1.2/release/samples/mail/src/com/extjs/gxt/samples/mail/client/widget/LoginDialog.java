/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.widget;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.StatusButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Timer;

public class LoginDialog extends Dialog {

  protected StatusButtonBar buttonBar;
  protected TextField<String> userName;
  protected TextField<String> password;
  protected Button reset;
  protected Button login;

  public LoginDialog() {
    FormLayout layout = new FormLayout();
    layout.setLabelWidth(90);
    layout.setDefaultWidth(155);
    setLayout(layout);

    setButtons("");
    setIconStyle("user");
    setHeading("GXT Mail Demo Login");
    setModal(true);
    setBodyBorder(true);
    setBodyStyle("padding: 8px;background: none");
    setWidth(300);
    setResizable(false);

    KeyListener keyListener = new KeyListener() {
      public void componentKeyUp(ComponentEvent event) {
        validate();
      }

    };

    userName = new TextField<String>();
    userName.setMinLength(4);
    userName.setFieldLabel("Username");
    userName.addKeyListener(keyListener);
    add(userName);

    password = new TextField<String>();
    password.setMinLength(4);
    password.setPassword(true);
    password.setFieldLabel("Password");
    password.addKeyListener(keyListener);
    add(password);

    setFocusWidget(userName);

    buttonBar = new StatusButtonBar();
    setButtonBar(buttonBar);

  }

  @Override
  protected void createButtons() {
    reset = new Button("Reset");
    reset.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        userName.reset();
        password.reset();
        validate();
        userName.focus();
      }

    });

    login = new Button("Login");
    login.disable();
    login.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        onSubmit();
      }
    });

    buttonBar.add(reset);
    buttonBar.add(login);
  }

  protected void onSubmit() {
    buttonBar.getStatusBar().showBusy("Please wait...");
    buttonBar.disable();
    Timer t = new Timer() {

      @Override
      public void run() {
        LoginDialog.this.hide();
      }

    };
    t.schedule(2000);
  }

  protected boolean hasValue(TextField<String> field) {
    return field.getValue() != null && field.getValue().length() > 0;
  }

  protected void validate() {
    login.setEnabled(hasValue(userName) && hasValue(password) && password.getValue().length() > 3);
  }

}
