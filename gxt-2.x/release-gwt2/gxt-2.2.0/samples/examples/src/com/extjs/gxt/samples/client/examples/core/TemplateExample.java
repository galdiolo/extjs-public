/*
 * Ext GWT 2.2.0 - Ext for GWT
 * Copyright(c) 2007-2010, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client.examples.core;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Element;

public class TemplateExample extends LayoutContainer {

  @Override
  protected void onRender(Element parent, int index) {
    super.onRender(parent, index);
    class Kid extends BaseModelData {
      public Kid(String name, int age) {
        set("name", name);
        set("age", age);
      }
    }

    final ModelData person = new BaseModelData();
    person.set("name", "Darrell Meyer");
    person.set("company", "Ext JS, LCC");
    person.set("product", "Ext GWT");
    person.set("location", "Washington, DC");

    List<Kid> kids = new ArrayList<Kid>();
    kids.add(new Kid("Alec", 4));
    kids.add(new Kid("Lia", 2));
    kids.add(new Kid("Andrew", 1));

    person.set("kids", kids);

    VerticalPanel vp = new VerticalPanel();
    vp.setSpacing(10);

    final ContentPanel panel = new ContentPanel();
    panel.setHeading("Basic Template");
    panel.setWidth(300);
    panel.setBodyStyleName("pad-text");

    ToolBar toolbar = new ToolBar();
    Button apply = new Button("Apply Template");
    apply.addSelectionListener(new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent ce) {
        Template template = new Template(getBasicTemplate());
        template.overwrite(panel.getBody().dom, Util.getJsObject(person));
      }
    });
    toolbar.add(apply);
    panel.setTopComponent(toolbar);

    final ContentPanel xpanel = new ContentPanel();
    xpanel.setHeading("XTemplate Test");
    xpanel.setWidth(300);
    xpanel.setBodyStyleName("pad-text");

    ToolBar toolBar = new ToolBar();
    toolBar.add(new Button("Apply Template", new SelectionListener<ButtonEvent>() {
      @Override
      public void componentSelected(ButtonEvent ce) {
        XTemplate tpl = XTemplate.create(getTemplate());
        xpanel.removeAll();
        xpanel.addText(tpl.applyTemplate(Util.getJsObject(person, 3)));
        xpanel.layout();
      }
    }));
    xpanel.setTopComponent(toolBar);

    vp.add(panel);
    vp.add(xpanel);
    add(vp);
  }

  private native String getBasicTemplate() /*-{
    return ['<p>Name: {name}</p>',
    '<p>Company: {company}</p>',
    '<p>Location: {location}</p>'].join("");
  }-*/;

  private native String getTemplate() /*-{
    var html = [
    '<p>Name: {name}</p>',
    '<p>Company: {company}</p>',
    '<p>Location: {location}</p>',
    '<p>Kids:</p>',
    '<tpl for="kids" if="name==\'Darrell Meyer\'">',
    '<tpl if="age &gt; 1"><p>{#}. {parent.name}\'s kid - {name}</p></tpl>',
    '</tpl>'
    ];
    return html.join("");
  }-*/;

}
