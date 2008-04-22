/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.samples.resources.client.MailItem;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class MailItemView extends View {

  private Container south;
  private ContentPanel panel;
  private Html header;
  private String headerHTML = "<div class=mail-item-detail><h1>{0}</h1><h2><b>From:</b> {1}</h2><h3><b>To:</b> {2}</h3></div>";

  public MailItemView(Controller controller) {
    super(controller);
  }

  protected void initialize() {
    south = (Container) Registry.get("south");
    south.setLayout(new FitLayout());

    panel = new ContentPanel();
    panel.border = false;
    panel.header = false;
    panel.setScrollMode(Scroll.AUTO);

    header = new Html();
    header.setStyleName("mail-item-detail");
    panel.setTopComponent(header);

    south.add(panel);
    south.layout(true);
  }

  protected void handleEvent(AppEvent event) {
    if (event.type == AppEvents.ViewMailItem) {
      MailItem item = (MailItem) event.data;

      if (item != null) {
        Params p = new Params();
        p.add(item.getSubject());
        p.add(item.getSender());
        p.add(item.getEmail());

        String s = Format.substitute(headerHTML, p);
        header.el.setInnerHtml(s);

        panel.removeAll();
        panel.addText("<div style='padding: 12px'>" + item.getBody() + "</div>");

        south.layout(true);

      } else {
        panel.removeAll();
      }

    }
  }

}
