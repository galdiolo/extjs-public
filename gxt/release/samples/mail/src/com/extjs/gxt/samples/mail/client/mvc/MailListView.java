/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.mail.client.mvc;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.samples.mail.client.AppEvents;
import com.extjs.gxt.samples.resources.client.Folder;
import com.extjs.gxt.samples.resources.client.MailItem;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.viewer.DefaultSelection;
import com.extjs.gxt.ui.client.viewer.ModelCellLabelProvider;
import com.extjs.gxt.ui.client.viewer.ModelContentProvider;
import com.extjs.gxt.ui.client.viewer.SelectionChangedEvent;
import com.extjs.gxt.ui.client.viewer.SelectionChangedListener;
import com.extjs.gxt.ui.client.viewer.TableViewer;
import com.extjs.gxt.ui.client.viewer.TableViewerColumn;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.table.RowSelectionModel;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.extjs.gxt.ui.client.widget.table.TableItem;

public class MailListView extends View {

  private Table<RowSelectionModel> table;
  private TableViewer viewer;
  private Folder folder;

  public MailListView(Controller controller) {
    super(controller);
  }

  protected void handleEvent(AppEvent event) {
    if (event.type == AppEvents.ViewMailItems) {
      Folder f = (Folder) event.data;

      ContentPanel center = (ContentPanel) Registry.get("center");
      center.setHeading(f.getName());

      center.removeAll();
      center.add(table);
      center.layout(true);

      Container south = (Container) Registry.get("south");
      south.removeAll();

      if (folder != f) {
        folder = f;
        viewer.setInput(folder);
        if (folder.getChildCount() > 0) {
          viewer.setSelection(new DefaultSelection(f.getChild(0)));
        }
      } else {

        TableItem item = table.getSelectionModel().getSelectedItem();
        if (item != null) {
          MailItem mail = (MailItem) item.getData();
          showMailItem(mail);
        }
      }

    }
  }

  protected void initialize() {
    List<TableColumn> columns = new ArrayList<TableColumn>();
    columns.add(new TableColumn("sender", "Sender", .2f));
    columns.add(new TableColumn("email", "Email", .3f));
    columns.add(new TableColumn("subject", "Subject", .5f));

    TableColumnModel cm = new TableColumnModel(columns);

    table = new Table(cm);
    table.setSelectionModel(new RowSelectionModel(SelectionMode.MULTI));
    table.setBorders(false);

    viewer = new TableViewer(table);
    viewer.setContentProvider(new ModelContentProvider());
    viewer.addSelectionListener(new SelectionChangedListener() {
      public void selectionChanged(SelectionChangedEvent event) {
        MailItem m = (MailItem) event.getSelection().getFirstElement();
        if (m != null) {
          showMailItem(m);
        }
      }
    });

    TableViewerColumn col = viewer.getViewerColumn(0);
    col.setLabelProvider(new ModelCellLabelProvider());

    col = viewer.getViewerColumn(1);
    col.setLabelProvider(new ModelCellLabelProvider());

    col = viewer.getViewerColumn(2);
    col.setLabelProvider(new ModelCellLabelProvider());

  }

  private void showMailItem(MailItem item) {
    AppEvent evt = new AppEvent(AppEvents.ViewMailItem, item);
    fireEvent(evt);
  }

}
