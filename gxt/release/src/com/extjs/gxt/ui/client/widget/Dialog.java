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
 * A <code>Window</code> with specialized support for buttons. Defaults to a
 * dialog with an 'ok' button.</p>
 * 
 * Code snippet:
 * 
 * <pre>
   Dialog d = new Dialog();
   d.setHeading("Exit Warning!");
   d.addText("Do you wish to save before exiting?");
   d.setBodyStyle("fontWeight:bold;padding:13px;");
   d.setSize(300, 100);
   d.setHideOnButtonClick(true);
   d.setButtons(Dialog.YESNOCANCEL);
   d.show();
 * </pre>
 * 
 * <p />
 * The internal buttons can be retrieved from the button bar using their
 * respective ids ('ok', 'cancel', 'yes', 'no', 'cancel') or by index. The
 * method {@link #getButtonBar()} creates the buttons, so any call before the
 * dialog is rendered will cause the buttons to be created based on the
 * {@link #setButtons(String)} value.
 * 
 * <dl>
 * <dt>Inherited Events:</dt>
 * <dd>Window Activate</dd>
 * <dd>Window Deactivate</dd>
 * <dd>Window Minimize</dd>
 * <dd>Window Maximize</dd>
 * <dd>Window Restore</dd>
 * <dd>Window Resize</dd>
 * <dd>ContentPanel BeforeExpand</dd>
 * <dd>ContentPanel Expand</dd>
 * <dd>ContentPanel BeforeCollapse</dd>
 * <dd>ContentPanel Collapse</dd>
 * <dd>ContentPanel BeforeClose</dd>
 * <dd>ContentPanel Close</dd>
 * <dd>LayoutContainer AfterLayout</dd>
 * <dd>ScrollContainer Scroll</dd>
 * <dd>Container BeforeAdd</dd>
 * <dd>Container Add</dd>
 * <dd>Container BeforeRemove</dd>
 * <dd>Container Remove</dd>
 * <dd>BoxComponent Move</dd>
 * <dd>BoxComponent Resize</dd>
 * <dd>Component Enable</dd>
 * <dd>Component Disable</dd>
 * <dd>Component BeforeHide</dd>
 * <dd>Component Hide</dd>
 * <dd>Component BeforeShow</dd>
 * <dd>Component Show</dd>
 * <dd>Component Attach</dd>
 * <dd>Component Detach</dd>
 * <dd>Component BeforeRender</dd>
 * <dd>Component Render</dd>
 * <dd>Component BrowserEvent</dd>
 * <dd>Component BeforeStateRestore</dd>
 * <dd>Component StateRestore</dd>
 * <dd>Component BeforeStateSave</dd>
 * <dd>Component SaveState</dd>
 * </dl>
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
   * Returns the button by id.
   * 
   * @param id the button id
   * @return the button
   */
  public Button getButtonById(String id) {
    return getButtonBar().getButtonById(id);
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
   * Sets the buttons to display (defaults to OK). Must be one of:
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

  /**
   * Creates the buttons based on button creation constant
   */
  protected void createButtons() {
    if (!buttonsInitialized) {

      buttonsInitialized = true;
      if (buttons.indexOf(OK) != -1) {
        okBtn = new Button(okText);
        okBtn.setItemId(OK);
        setFocusWidget(okBtn);
        addButton(okBtn);
      }

      if (buttons.indexOf(YES) != -1) {
        yesBtn = new Button(yesText);
        yesBtn.setItemId(YES);
        setFocusWidget(yesBtn);
        addButton(yesBtn);
      }

      if (buttons.indexOf(NO) != -1) {
        noBtn = new Button(noText);
        noBtn.setItemId(NO);
        addButton(noBtn);
      }

      if (buttons.indexOf(CANCEL) != -1) {
        cancelBtn = new Button(cancelText);
        cancelBtn.setItemId(CANCEL);
        addButton(cancelBtn);
      }

      if (buttons.indexOf("close") != -1) {
        closeBtn = new Button(closeText);
        closeBtn.setItemId("close");
        addButton(closeBtn);
      }
    }
  }

  /**
   * Called after a button in the button bar is selected. If
   * {@link #setHideOnButtonClick(boolean)} is true, closes or hides the dialog
   * when any button is pressed, determined by {@link #closeAction}.
   * 
   * @param button the button
   */
  protected void onButtonPressed(Button button) {
    if (button == closeBtn || hideOnButtonClick) {
      if (closeAction == CloseAction.HIDE) {
        hide(button);
      } else {
        close(button);
      }
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
