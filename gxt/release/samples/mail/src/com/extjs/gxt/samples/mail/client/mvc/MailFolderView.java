/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.samples.mail.client.model.MailModel;
import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.viewer.ModelLabelProvider;
import com.extjs.gxt.ui.client.viewer.ModelTreeContentProvider;
import com.extjs.gxt.ui.client.viewer.SelectionChangedEvent;
import com.extjs.gxt.ui.client.viewer.SelectionChangedListener;
import com.extjs.gxt.ui.client.viewer.TreeViewer;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordianLayout;
import com.extjs.gxt.ui.client.widget.tree.Tree;

public class MailFolderView extends View {

  private Tree tree;
  private TreeViewer folders;

  public MailFolderView(Controller controller) {
    super(controller);
  }

  protected void initialize() {
    ContentPanel west = (ContentPanel) Registry.get("west");
    west.setLayout(new AccordianLayout());

    // mail
    ContentPanel mail = new ContentPanel();
    mail.setHeading("Mail");

    tree = new Tree();
    tree.itemImageStyle = "tree-folder";

    folders = new TreeViewer(tree);
    folders.setLabelProvider(new ModelLabelProvider());
    folders.setContentProvider(new ModelTreeContentProvider() {
      @Override
      public boolean hasChildren(Object element) {
        return false;
      }
    });

    folders.addSelectionListener(new SelectionChangedListener() {
      public void selectionChanged(SelectionChangedEvent se) {
        Folder f = (Folder) se.getSelection().getFirstElement();
        AppEvent evt = new AppEvent(AppEvents.ViewMailItems, f);
        fireEvent(evt);
      }
    });
    MailModel model = (MailModel) Registry.get("model");
    folders.setInput(model);

    mail.add(tree);

    // tasks
    ContentPanel tasks = new ContentPanel();
    tasks.setHeading("Tasks");

    // contacts
    ContentPanel contacts = new ContentPanel();
    contacts.setHeading("Contacts");

    west.add(mail);
    west.add(tasks);
    west.add(contacts);
    west.layout(true);

  }

  protected void handleEvent(AppEvent event) {
    // Folder mail = (Folder) Registry.get("mail");
     if (event.type == AppEvents.NavMail) {
    // if (folders.getSelection().size() == 0) {
    // // inbox
    // folders.select((Folder) mail.getChild(0));
    // } else {
    // Folder f = (Folder) folders.getSelection().getFirstElement();
    // AppEvent evt = new AppEvent(AppEvents.ViewMailItems, f);
    // fireEvent(evt);
    // }
    //
     }
  }

}
