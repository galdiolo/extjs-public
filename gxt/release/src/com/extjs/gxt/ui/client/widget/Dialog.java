/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.client.widget;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.ButtonBarEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.google.gwt.user.client.Element;

/**
 * A <code>Window</code> with specialized support for buttons.
 * <p>
 * The internal buttons can be retrieved from the button bar using their
 * respective ids ('ok', 'cancel', 'yes', 'no', 'cancel') or by index. Any call
 * to {@link #getButtonBar()} before the dialog is rendered will cause the
 * buttons to be created based on the {@link #buttons} value..
 * </p>
 */
public class Dialog extends Window {

  /**
   * Button constant that displays a single OK button.
   */
  public static final String OK = "ok";

  /**
   * Button constant that displays a single CANCEL button.
   */
  public static final String CANCEL = "cancel";

  /**
   * Button constant that displays a OK and CANCEL button.
   */
  public static final String OKCANCEL = "okcancel";

  /**
   * Button constant that displays a YES and NO button.
   */
  public static final String YESNO = "yesno";

  /**
   * Button constant for the itemId of a NO button.
   */
  public static final String NO = "no";

  /**
   * Button constant for the itemId of a YES button.
   */
  public static final String YES = "yes";

  /**
   * Button constant that displays a YES, NO, and CANCEL button.
   */
  public static final String YESNOCANCEL = "yesnocancel";

  /**
   * The OK button text (defaults to 'OK');
   */
  public String okText = GXT.MESSAGES.messageBox_ok();

  /**
   * The Close button text (defaults to 'Close').
   */
  public String closeText = GXT.MESSAGES.messageBox_close();

  /**
   * The Cancel button text (defaults to 'Cancel').
   */
  public String cancelText = GXT.MESSAGES.messageBox_cancel();

  /**
   * The Yes button text (defaults to 'Yes').
   */
  public String yesText = GXT.MESSAGES.messageBox_yes();

  /**
   * The No button text (defaults to 'No').
   */
  public String noText = GXT.MESSAGES.messageBox_no();

  protected Button okBtn;
  protected Button closeBtn;
  protected Button cancelBtn;
  protected Button noBtn, yesBtn;

  private boolean hideOnButtonClick = false;
  private String buttons = OK;
  private boolean buttonsInitialized;

  @Override
  public ButtonBar getButtonBar() {
    createButtons();
    return super.getButtonBar();
  }

  /**
   * Returns the last pressed button.
   * 
   * @return the button or <code>null</code> if no button pressed
   */
  public Button getButtonPressed() {
    return buttonBar.getButtonPressed();
  }

  /**
   * Returns the button's.
   * 
   * @return the buttons the buttons
   */
  public String getButtons() {
    return buttons;
  }

  /**
   * Returns true if the dialog will be hidden on any button click.
   * 
   * @return the hide on button click state
   */
  public boolean isHideOnButtonClick() {
    return hideOnButtonClick;
  }

  /**
   * Sets the buttons to display (defaults to OK).
   * 
   * <pre>
   * Dialog.OK
   * Dialog.CANCEL
   * Dialog.OKCANCEL
   * Dialog.YESNO
   * Dialog.YESNOCANCEL
   * </pre>
   */
  public void setButtons(String buttons) {
    this.buttons = buttons;
  }

  /**
   * True to hide the dialog on any button click.
   * 
   * @param hideOnButtonClick true to hide
   */
  public void setHideOnButtonClick(boolean hideOnButtonClick) {
    this.hideOnButtonClick = hideOnButtonClick;
  }

  protected void createButtons() {
    if (!buttonsInitialized) {

      buttonsInitialized = true;
      if (getButtons().indexOf(OK) != -1) {
        okBtn = new Button(okText);
        okBtn.setItemId(OK);
        addButton(okBtn);
      }

      if (getButtons().indexOf(YES) != -1) {
        yesBtn = new Button(yesText);
        yesBtn.setItemId(YES);
        addButton(yesBtn);
      }

      if (getButtons().indexOf(NO) != -1) {
        noBtn = new Button(noText);
        noBtn.setItemId(NO);
        addButton(noBtn);
      }

      if (getButtons().indexOf(CANCEL) != -1) {
        cancelBtn = new Button(cancelText);
        cancelBtn.setItemId(CANCEL);
        addButton(cancelBtn);
      }

      if (getButtons().indexOf("close") != -1) {
        closeBtn = new Button(closeText);
        closeBtn.setItemId("close");
        addButton(closeBtn);
      }
    }
  }

  /**
   * Called after a button in the button bar is selected. Default implementation
   * closes are hides the dialog if the close button was pressed, determined by
   * {@link #closeAction}.
   * 
   * @param button the button
   */
  protected void onButtonPressed(Button button) {
    if (button == closeBtn) {
      hide(button);
    }
    if (isHideOnButtonClick()) {
      hide(button);
    }
  }

  protected void onRender(Element parent, int pos) {
    buttonBar.setButtonAlign(getButtonAlign());

    buttonBar.addListener(Events.Select, new Listener<ButtonBarEvent>() {
      public void handleEvent(ButtonBarEvent bbe) {
        onButtonPressed(bbe.item);
      }
    });

    createButtons();

    super.onRender(parent, pos);
  }

}
