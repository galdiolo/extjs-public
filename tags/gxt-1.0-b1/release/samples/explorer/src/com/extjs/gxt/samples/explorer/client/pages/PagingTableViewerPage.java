/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.samples.explorer.client.ExplorerServiceAsync;
import com.extjs.gxt.samples.explorer.client.model.Post;
import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseLoadConfig;
import com.extjs.gxt.ui.client.data.BaseModelStringProvider;
import com.extjs.gxt.ui.client.data.DataCallback;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.BaseLoadResult.FailedLoadResult;
import com.extjs.gxt.ui.client.data.BaseLoadResult.ModelCollectionLoadResult;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.viewer.AsyncContentCallback;
import com.extjs.gxt.ui.client.viewer.ModelCellLabelProvider;
import com.extjs.gxt.ui.client.viewer.RemoteContentProvider;
import com.extjs.gxt.ui.client.viewer.TableViewer;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.PagingToolBar;
import com.extjs.gxt.ui.client.widget.ToolButton;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class PagingTableViewerPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);

    final ExplorerServiceAsync service = (ExplorerServiceAsync) Registry.get("service");

    if (service == null) {
      MessageBox box = new MessageBox();
      box.buttons = MessageBox.OK;
      box.icon = MessageBox.INFO;
      box.title = "Information";
      box.message = "No service detected";
      box.show();
      return;
    }

    FlowLayout layout = new FlowLayout();
    layout.margin = 10;
    setLayout(layout);

    List<TableColumn> columns = new ArrayList<TableColumn>();
    columns.add(new TableColumn("forum", "Fourm", 150));
    columns.add(new TableColumn("username", "Username", 100));
    columns.add(new TableColumn("subject", "Subject", 200));
    columns.add(new TableColumn("date", "Date", 100));

    // create the column model
    TableColumnModel cm = new TableColumnModel(columns);
    Table table = new Table(cm);
    table.horizontalScroll = true;

    final RemoteContentProvider cp = new RemoteContentProvider() {

      @Override
      public void getElements(Object input, AsyncContentCallback callback) {
        callback.setElements((List) input);
      }

      @Override
      public void loadData(BaseLoadConfig config, final DataCallback callback) {
        service.getPosts(config, new AsyncCallback<ModelCollectionLoadResult<Post>>() {
          public void onSuccess(ModelCollectionLoadResult<Post> result) {
            callback.setResult(result);
          }

          public void onFailure(Throwable caught) {
            callback.setResult(new FailedLoadResult(caught));
          }
        });
      }

    };
    cp.setRemoteSort(true);

    final TableViewer viewer = new TableViewer(table);
    viewer.setContentProvider(cp);

    final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MM/d/y");

    ModelCellLabelProvider lp = new ModelCellLabelProvider();
    lp.modelStringProvider = new BaseModelStringProvider() {

      @Override
      public String getStringValue(ModelData model, String propertyName) {
        if (model.get(propertyName) instanceof Date) {
          return dateFormat.format((Date) model.get(propertyName));
        } else {
          return super.getStringValue(model, propertyName);
        }
      };
    };
    viewer.setDefaultCellLabelProvider(lp);

    final PagingToolBar toolBar = new PagingToolBar(50);
    toolBar.bind(cp);

    ContentPanel panel = new ContentPanel();
    panel.frame = true;
    panel.collapsible = true;
    panel.animCollapse = false;
    panel.buttonAlign = HorizontalAlignment.CENTER;
    panel.setIconStyle("icon-table");
    panel.setHeading("Paging TableViewer");
    panel.setLayout(new FitLayout());
    panel.add(table);
    panel.setSize(600, 450);

    panel.getHeader().addTool(new ToolButton("x-tool-refresh", new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        toolBar.refresh();
      }
    }));

    panel.setBottomComponent(toolBar);
    add(panel);

    // table needs to be attached for mask to display properly
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        toolBar.first();
      }
    });

  }

}
