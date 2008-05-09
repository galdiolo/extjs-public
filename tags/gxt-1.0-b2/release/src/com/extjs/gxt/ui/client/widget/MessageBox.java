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
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.user.client.Element;

/**
 * Utility class for generating different styles of message boxes.
 * 
 * <p>
 * Note that the MessageBox is asynchronous. Unlike a regular JavaScript
 * <code>alert</code> (which will halt browser execution), showing a
 * MessageBox will not cause the code to stop.
 * </p>
 */
public class MessageBox {

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
   * Button constant that displays a YES, NO, and CANCEL button.
   */
  public static final String YESNOCANCEL = "yesnocancel";

  /**
   * The CSS style name that provides the INFO icon image.
   */
  public static String INFO = "ext-mb-info";

  /**
   * The CSS style name that provides the WARNING icon image.
   */
  public static String WARNING = "ext-mb-warning";

  /**
   * The CSS style name that provides the QUESTION icon image.
   */
  public static String QUESTION = "ext-mb-question";

  /**
   * The CSS style name that provides the ERROR icon image.
   */
  public static String ERROR = "ext-mb-error";

  /**
   * Displays a standard read-only message box with an OK button (comparable to
   * the basic JavaScript alert prompt).
   * 
   * @param title the title bar text
   * @param msg the message box body text
   * @param callback listener to be called when the box is closed
   * @return the new message box instance
   */
  public static MessageBox alert(String title, String msg, Listener callback) {
    MessageBox box = new MessageBox();
    box.modal = false;
    box.title = title;
    box.message = msg;
    box.callback = callback;
    box.buttons = OK;
    box.icon = WARNING;
    box.show();
    return box;
  }

  /**
   * Displays a message box with an infinitely auto-updating progress bar. This
   * can be used to block user interaction while waiting for a long-running
   * process to complete that does not have defined intervals. You are
   * responsible for closing the message box when the process is complete.
   * 
   * @param title the title bar text
   * @param msg the message box body text
   * @param progressText the text to display inside the progress bar
   * @return the new message box instance
   */
  public static MessageBox wait(String title, String msg, String progressText) {
    MessageBox box = new MessageBox();
    box.title = title;
    box.message = msg;
    box.wait = true;
    box.progressText = progressText;
    box.buttons = "";
    box.closable = false;
    box.show();
    return box;
  }

  /**
   * Displays a confirmation message box with Yes and No buttons (comparable to
   * JavaScript's confirm).
   * 
   * @param title the title bar text
   * @param msg the message box body text
   * @param callback the listener invoked after the message box is closed
   * @return the new message box instance
   */
  public static MessageBox confirm(String title, String msg, Listener callback) {
    MessageBox box = new MessageBox();
    box.title = title;
    box.message = msg;
    box.callback = callback;
    box.icon = QUESTION;
    box.buttons = YESNO;
    box.show();
    return box;
  }

  /**
   * Displays a message box with a progress bar. This message box has no buttons
   * and is not closeable by the user. You are responsible for updating the
   * progress bar as needed via {@link MessageBox#updateProgress}
   * 
   * @param title the title bar text
   * @param msg the message box body text
   * @param progressText the text to display inside the progress bar
   * @return the new message box
   */
  public static MessageBox progress(String title, String msg, String progressText) {
    MessageBox box = new MessageBox();
    box.title = title;
    box.message = msg;
    box.progress = true;
    box.progressText = progressText;
    box.buttons = "";
    box.closable = false;
    box.show();
    return box;
  }

  /**
   * Displays a message box with OK and Cancel buttons prompting the user to
   * enter some text (comparable to JavaScript's prompt).
   * 
   * @param title the title bar text
   * @param msg the message box body text
   * @param callback the listener invoked after the message box is closed
   * @return the new message box
   */
  public static MessageBox prompt(String title, String msg, Listener callback) {
    return prompt(title, msg, false, callback);
  }

  public static MessageBox prompt(String title, String msg, boolean multiline,
      Listener callback) {
    MessageBox box = new MessageBox();
    box.title = title;
    box.message = msg;
    box.prompt = true;
    box.multiline = multiline;
    box.callback = callback;
    box.show();
    return box;
  }

  /**
   * The default height in pixels of the message box's multiline textarea if
   * displayed (defaults to 75).
   */
  public int defaultTextHeight = 75;

  /**
   * The maximum width in pixels of the message box (defaults to 600).
   */
  public int maxWidth = 600;

  /**
   * The minimum width in pixels of the message box (defaults to 100).
   */
  public int minWidth = 100;

  /**
   * False to allow user interaction with the page while the message box is
   * displayed (defaults to true).
   */
  public boolean modal = true;

  /**
   * True to display a progress bar (defaults to false).
   */
  public boolean progress = false;

  /**
   * The text to display inside the progress bar if progress = true (defaults to
   * "").
   */
  public String progressText = "";

  /**
   * The minimum width in pixels of the message box if it is a progress-style
   * dialog. This is useful for setting a different minimum width than text-only
   * dialogs may need (defaults to 250).
   */
  public int minProgressWidth = 250;

  /**
   * False to hide the top-right close button (defaults to true). Note that
   * progress and wait dialogs will ignore this property and always hide the
   * close button as they can only be closed programmatically.
   */
  public boolean closable = true;

  /**
   * True to display a progress bar (defaults to false).
   */
  public boolean wait;

  /**
   * True to prompt the user to enter single-line text (defaults to false).
   */
  public boolean prompt = false;

  /**
   * True to prompt the user to enter multi-line text (defaults to false).
   */
  public boolean multiline = false;

  /**
   * The buttons to display (defaults to OK).
   * 
   * <pre>
   * MessageBox.OK
   * MessageBox.CANCEL
   * MessageBox.OKCANCEL
   * MessageBox.YESNO
   * MessageBox.YESNOCANCEL
   * </pre>
   */
  public String buttons = OK;

  /**
   * The title text.
   */
  public String title;

  /**
   * A string that will replace the existing message box body text (defaults to
   * the XHTML-compliant non-breaking space character '&#160;').
   */
  public String message = "&#160;";
  /**
   * A CSS class that provides a background image to be used as an icon for the
   * dialog (e.g., MessageBox.WARNING or 'custom-class', defaults to "").
   * 
   * <pre>
   * MessageBox.INFO
   * MessageBox.WARNING
   * MessageBox.QUESTION
   * MessageBox.ERROR
   * </pre>
   */
  public String icon = "";

  /**
   * Listener to be called when the message box is closed.
   */
  public Listener callback;

  private Dialog dialog;
  private Element iconEl;
  private Element msgEl;
  private ProgressBar progressBar;
  private TextField textBox;
  private TextArea textArea;

  /**
   * Returns the underlying window.
   * 
   * @return the window
   */
  public Dialog getDialog() {
    if (dialog == null) {
      dialog = new Dialog() {

        @Override
        protected void afterRender() {
          super.afterRender();
          addStyleName("x-window-dlg");

          El body = new El(dialog.getElement("body"));

          String html = "<div class='ext-mb-icon x-hidden'></div><div class=ext-mb-content><span class=ext-mb-text></span><br /></div>";
          body.setInnerHtml(html);

          iconEl = body.firstChild().dom;
          El contentEl = body.childNode(1);
          El msgEl = contentEl.firstChild();
          msgEl.update(message);

          if (prompt && !multiline) {
            textBox = new TextField();
            dialog.setFocusWidget(textBox);
            textBox.render(contentEl.dom, 2);
            textBox.el.setWidth(GXT.isIE ? "100%" : "90%");
            icon = null;
          }

          if (multiline) {
            textArea = new TextArea();
            textArea.render(contentEl.dom, 2);
            textArea.el.setWidth(GXT.isIE ? "100%" : "90%");
            dialog.setFocusWidget(textArea);
            icon = null;
          }

          if (progress || wait) {
            progressBar = new ProgressBar();
            progressBar.render(body.dom);
            if (wait) {
              progressBar.auto();
            }
            if (progressText != null) {
              progressBar.updateText(progressText);
            }
            icon = null;
          }

          setIcon(icon);
        }

        @Override
        protected void initTools() {
          setClosable(false);
          super.initTools();
        }

      };

      dialog.setHeading(title);
      dialog.setResizable(false);
      dialog.setConstrain(true);
      dialog.setMinimizable(false);
      dialog.setMaximizable(false);
      dialog.setClosable(false);
      dialog.setModal(true);
      dialog.setButtonAlign(HorizontalAlignment.CENTER);
      dialog.setMinHeight(80);
      dialog.setPlain(true);
      dialog.setFooter(true);
      dialog.setButtons(buttons);
      dialog.buttonPressedAction = "hide";
      if (callback != null) {
        dialog.addListener(Events.Hide, callback);
      }
    }
    return dialog;
  }

  /**
   * Returns the box's progress applies.
   * 
   * @return the progress bar
   */
  public ProgressBar getProgressBar() {
    return progressBar;
  }

  /**
   * Returns the box's text box.
   * 
   * @return the text box
   */
  public TextField getTextBox() {
    return textBox;
  }

  /**
   * Hides the message box if it is displayed.
   */
  public void hide() {
    if (dialog.isVisible()) {
      dialog.hide();
    }
  }

  /**
   * Returns true if the message box is currently displayed.
   * 
   * @return the visible state
   */
  public boolean isVisible() {
    return dialog != null && dialog.isVisible();
  }

  /**
   * Adds the specified icon to the dialog. By default, the class 'ext-mb-icon'
   * is applied for default styling, and the class passed in is expected to
   * supply the background image url. Pass in empty string ('') to clear any
   * existing icon. The following built-in icon classes are supported, but you
   * can also pass in a custom class name:
   * 
   * <pre>
   * MessageBox.INFO
   * MessageBox.WARNING
   * MessageBox.QUESTION
   * MessageBox.ERROR
   * </pre>
   * 
   * @param iconStyle the icon style
   */
  public void setIcon(String iconStyle) {
    El el = El.fly(iconEl);
    if (iconStyle != null) {
      el.removeStyleName("x-hidden");
      el.replaceStyleName(this.icon, iconStyle);
    } else {
      el.replaceStyleName(this.icon, "x-hidden");
      icon = "";
    }
  }

  /**
   * Displays the message box.
   */
  public void show() {
    dialog = getDialog();
    dialog.show();
  }

  /**
   * Updates a progress-style message box's text and progress bar. Only relevant
   * on message boxes initiated via {@link #progress}.
   * 
   * @param value any number between 0 and 1 (e.g., .5)
   * @param text the progress text to display inside the progress bar or null
   * @return this
   */
  public MessageBox updateProgress(double value, String text) {
    if (progressBar != null) {
      progressBar.updateProgress(value, text);
    }
    return this;
  }

  /**
   * Updates the message box body text.
   * 
   * @param text the new text or null to clear
   * @return this
   */
  public MessageBox updateText(String text) {
    El.fly(msgEl).update(text != null ? text : "&#160;");
    return this;
  }

}