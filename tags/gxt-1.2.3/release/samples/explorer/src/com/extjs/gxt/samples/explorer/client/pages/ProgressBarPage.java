/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

public class ProgressBarPage extends LayoutContainer implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    ButtonBar buttonBar = new ButtonBar();

    buttonBar.add(new Button("Progress", new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        final MessageBox box = MessageBox.progress("Please wait", "Loading items...",
            "Initializing...");
        final ProgressBar bar = box.getProgressBar();
        final Timer t = new Timer() {
          float i;

          @Override
          public void run() {
            bar.updateProgress(i / 100, (int) i + "% Complete");
            i += 5;
            if (i > 105) {
              cancel();
              box.close();
              Info.display("Message", "Items were loaded", "");
            }
          }
        };
        t.scheduleRepeating(500);
      }
    }));

    buttonBar.add(new Button("Wait", new SelectionListener<ComponentEvent>() {
      public void componentSelected(ComponentEvent ce) {
        final MessageBox box = MessageBox.wait("Progress", "Saving your data, please wait...",
            "Saving...");
        Timer t = new Timer() {
          @Override
          public void run() {
            Info.display("Message", "Your fake data was saved", "");
            box.close();
          }
        };
        t.schedule(5000);
      }
    }));

    setScrollMode(Scroll.AUTO);
    add(buttonBar);
  }
}
