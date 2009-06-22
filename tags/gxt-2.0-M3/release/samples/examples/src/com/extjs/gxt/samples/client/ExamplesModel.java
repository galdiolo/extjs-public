/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007-2009, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.client;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.samples.client.examples.binding.BasicBindingExample;
import com.extjs.gxt.samples.client.examples.binding.GridBindingExample;
import com.extjs.gxt.samples.client.examples.binding.GridStoreBindingExample;
import com.extjs.gxt.samples.client.examples.button.ButtonAlignExample;
import com.extjs.gxt.samples.client.examples.button.ButtonsExample;
import com.extjs.gxt.samples.client.examples.chart.AdvancedChartExample;
import com.extjs.gxt.samples.client.examples.chart.BasicChartExample;
import com.extjs.gxt.samples.client.examples.chart.ChartGalleryExample;
import com.extjs.gxt.samples.client.examples.core.TemplateExample;
import com.extjs.gxt.samples.client.examples.dnd.BasicDNDExample;
import com.extjs.gxt.samples.client.examples.dnd.DualListFieldExample;
import com.extjs.gxt.samples.client.examples.dnd.GridToGridExample;
import com.extjs.gxt.samples.client.examples.dnd.ListViewDNDExample;
import com.extjs.gxt.samples.client.examples.dnd.ReorderingTreePanelDNDExample;
import com.extjs.gxt.samples.client.examples.dnd.TreePanelToTreePanelExample;
import com.extjs.gxt.samples.client.examples.forms.AdvancedComboBoxExample;
import com.extjs.gxt.samples.client.examples.forms.AdvancedFormsExample;
import com.extjs.gxt.samples.client.examples.forms.ComboBoxExample;
import com.extjs.gxt.samples.client.examples.forms.FileUploadExample;
import com.extjs.gxt.samples.client.examples.forms.FormsExample;
import com.extjs.gxt.samples.client.examples.grid.AggregationGridExample;
import com.extjs.gxt.samples.client.examples.grid.BeanModelGridExample;
import com.extjs.gxt.samples.client.examples.grid.BufferedGridExample;
import com.extjs.gxt.samples.client.examples.grid.ColumnGroupingExample;
import com.extjs.gxt.samples.client.examples.grid.EditableBufferedGridExample;
import com.extjs.gxt.samples.client.examples.grid.EditableGridExample;
import com.extjs.gxt.samples.client.examples.grid.GridExample;
import com.extjs.gxt.samples.client.examples.grid.GridPluginsExample;
import com.extjs.gxt.samples.client.examples.grid.GroupingGridExample;
import com.extjs.gxt.samples.client.examples.grid.JsonGridExample;
import com.extjs.gxt.samples.client.examples.grid.MemoryPagingGridExample;
import com.extjs.gxt.samples.client.examples.grid.PagingBeanModelGridExample;
import com.extjs.gxt.samples.client.examples.grid.PagingGridExample;
import com.extjs.gxt.samples.client.examples.grid.RowEditorExample;
import com.extjs.gxt.samples.client.examples.grid.TotalsGridExample;
import com.extjs.gxt.samples.client.examples.grid.WidgetRenderingExample;
import com.extjs.gxt.samples.client.examples.grid.XmlGridExample;
import com.extjs.gxt.samples.client.examples.layouts.AccordionLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.AnchorLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.BorderLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.CardLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.CenterLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.HBoxLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.RowLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.VBoxLayoutExample;
import com.extjs.gxt.samples.client.examples.misc.CustomSliderExample;
import com.extjs.gxt.samples.client.examples.misc.DatePickerExample;
import com.extjs.gxt.samples.client.examples.misc.DraggableExample;
import com.extjs.gxt.samples.client.examples.misc.FxExample;
import com.extjs.gxt.samples.client.examples.misc.ResizableExample;
import com.extjs.gxt.samples.client.examples.misc.SliderExample;
import com.extjs.gxt.samples.client.examples.misc.ToolTipsExample;
import com.extjs.gxt.samples.client.examples.model.Category;
import com.extjs.gxt.samples.client.examples.model.Entry;
import com.extjs.gxt.samples.client.examples.organizer.ImageOrganizerExample;
import com.extjs.gxt.samples.client.examples.portal.PortalExample;
import com.extjs.gxt.samples.client.examples.tabs.AdvancedTabExample;
import com.extjs.gxt.samples.client.examples.tabs.BasicTabExample;
import com.extjs.gxt.samples.client.examples.toolbar.AdvancedToolBarExample;
import com.extjs.gxt.samples.client.examples.toolbar.MenuBarExample;
import com.extjs.gxt.samples.client.examples.toolbar.StatusToolBarExample;
import com.extjs.gxt.samples.client.examples.toolbar.ToolBarExample;
import com.extjs.gxt.samples.client.examples.toolbar.ToolBarOverflowExample;
import com.extjs.gxt.samples.client.examples.treegrid.AsyncTreeGridExample;
import com.extjs.gxt.samples.client.examples.treegrid.EditorTreeGridExample;
import com.extjs.gxt.samples.client.examples.treegrid.RowEditorTreeGridExample;
import com.extjs.gxt.samples.client.examples.treegrid.RowNumberTreeGridExample;
import com.extjs.gxt.samples.client.examples.treegrid.TreeGridExample;
import com.extjs.gxt.samples.client.examples.treepanel.AsyncTreePanelExample;
import com.extjs.gxt.samples.client.examples.treepanel.AsyncXmlTreePanelExample;
import com.extjs.gxt.samples.client.examples.treepanel.BasicTreePanelExample;
import com.extjs.gxt.samples.client.examples.treepanel.CheckBoxTreePanelExample;
import com.extjs.gxt.samples.client.examples.treepanel.ContextMenuTreePanelExample;
import com.extjs.gxt.samples.client.examples.treepanel.FastTreePanelExample;
import com.extjs.gxt.samples.client.examples.treepanel.FilterTreePanelExample;
import com.extjs.gxt.samples.client.examples.view.CheckBoxListViewExample;
import com.extjs.gxt.samples.client.examples.view.ImageChooserExample;
import com.extjs.gxt.samples.client.examples.view.ListViewExample;
import com.extjs.gxt.samples.client.examples.windows.AccordionWindowExample;
import com.extjs.gxt.samples.client.examples.windows.DialogExample;
import com.extjs.gxt.samples.client.examples.windows.HelloWindowExample;
import com.extjs.gxt.samples.client.examples.windows.MessageBoxExample;
import com.extjs.gxt.ui.client.Style.HideMode;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeModel;

public class ExamplesModel extends BaseTreeModel {

  protected List<Entry> entries = new ArrayList<Entry>();

  public ExamplesModel() {
    Category grids = new Category("Grids");
    grids.addEntry("Basic Grid", new GridExample());
    grids.addEntry("Column Grouping", new ColumnGroupingExample());
    grids.addEntry("Aggregation Row Grid", new AggregationGridExample());
    grids.addEntry("Grid Plugins", new GridPluginsExample());
    grids.addEntry("Editable Grid", new EditableGridExample());
    grids.addEntry("Xml Grid", new XmlGridExample());
    grids.addEntry("Json Grid", new JsonGridExample());
    grids.addEntry("Paging", new PagingGridExample());
    grids.addEntry("Local Paging", new MemoryPagingGridExample());
    grids.addEntry("Grouping", new GroupingGridExample());
    grids.addEntry("Live Group Summary", new TotalsGridExample());
    grids.addEntry("BeanModel Grid", new BeanModelGridExample());
    grids.addEntry("Paging BeanModel Grid", new PagingBeanModelGridExample());
    grids.addEntry("Buffered Grid", new BufferedGridExample());
    grids.addEntry("Editable Buffered Grid", new EditableBufferedGridExample());
    grids.addEntry("RowEditor Grid", new RowEditorExample());
    grids.addEntry("Widget Renderer Grid", new WidgetRenderingExample());
    add(grids);

    Category treeGrids = new Category("TreeGrid");
    treeGrids.addEntry("Basic TreeGrid", new TreeGridExample());
    treeGrids.addEntry("Async TreeGrid", new AsyncTreeGridExample());
    treeGrids.addEntry("RowNumber TreeGrid", new RowNumberTreeGridExample());
    treeGrids.addEntry("EditorTreeGrid", new EditorTreeGridExample());
    treeGrids.addEntry("RowEditor TreeGrid", new RowEditorTreeGridExample());
    add(treeGrids);

    Category treePanels = new Category("TreePanel");
    treePanels.addEntry("Basic Tree", new BasicTreePanelExample());
    treePanels.addEntry("Context Menu Tree", new ContextMenuTreePanelExample());
    treePanels.addEntry("Async Tree", new AsyncTreePanelExample());
    treePanels.addEntry("Async Xml TreePanel", new AsyncXmlTreePanelExample());
    treePanels.addEntry("Filter Tree", new FilterTreePanelExample());
    treePanels.addEntry("Checkbox Tree", new CheckBoxTreePanelExample());
    treePanels.addEntry("Fast Tree", new FastTreePanelExample());
    add(treePanels);

    Category tabs = new Category("Tabs");
    tabs.addEntry("Basic Tabs", new BasicTabExample());
    tabs.addEntry("Advanced Tabs", new AdvancedTabExample());
    add(tabs);

    Category charts = new Category("Charts");
    charts.addEntry("Basic Chart", new BasicChartExample(), false, true, HideMode.OFFSETS);
    charts.addEntry("Chart Gallery", new ChartGalleryExample(), false, true, HideMode.OFFSETS);
    charts.addEntry("Advanced Charts", new AdvancedChartExample(), false, true, HideMode.OFFSETS);
    add(charts);

    Category dnd = new Category("Drag and Drop");
    dnd.addEntry("Basic DnD", new BasicDNDExample());
    dnd.addEntry("List to List", new ListViewDNDExample());
    dnd.addEntry("Grid to Grid", new GridToGridExample());
    dnd.addEntry("Tree to Tree", new TreePanelToTreePanelExample());
    dnd.addEntry("Reordering Tree", new ReorderingTreePanelDNDExample());
    dnd.addEntry("Image Organizer", new ImageOrganizerExample());
    add(dnd);

    Category windows = new Category("Windows");
    windows.addEntry("Hello World", new HelloWindowExample());
    windows.addEntry("Accordion Window", new AccordionWindowExample());
    windows.addEntry("Dialog", new DialogExample());
    windows.addEntry("MessageBox", new MessageBoxExample());
    add(windows);

    Category layouts = new Category("Layouts");
    layouts.addEntry("AccordionLayout", new AccordionLayoutExample());
    layouts.addEntry("AnchorLayout", new AnchorLayoutExample());
    layouts.addEntry("BorderLayout", new BorderLayoutExample(), true);
    layouts.addEntry("CardLayout", new CardLayoutExample());
    layouts.addEntry("CenterLayout", new CenterLayoutExample(), true);
    layouts.addEntry("RowLayout", new RowLayoutExample(), true);
    layouts.addEntry("Portal", new PortalExample(), true);
    layouts.addEntry("VBoxLayout", new VBoxLayoutExample(), true);
    layouts.addEntry("HBoxLayout", new HBoxLayoutExample(), true);
    add(layouts);

    Category combos = new Category("Combos");
    combos.addEntry("ComboBox", new ComboBoxExample());
    combos.addEntry("Advanced ComboBox", new AdvancedComboBoxExample());
    add(combos);

    Category forms = new Category("Forms");
    forms.addEntry("Forms", new FormsExample());
    forms.addEntry("Advanced Forms", new AdvancedFormsExample(), false, true, HideMode.OFFSETS);
    forms.addEntry("DualListField", new DualListFieldExample());
    forms.addEntry("File Upload", new FileUploadExample());
    add(forms);

    Category binding = new Category("Binding");
    binding.addEntry("Basic Binding", new BasicBindingExample());
    binding.addEntry("Grid Binding", new GridBindingExample());
    binding.addEntry("Grid Store Binding", new GridStoreBindingExample());
    add(binding);

    Category toolbar = new Category("ToolBar & Menus");
    toolbar.addEntry("Basic Toolbar", new ToolBarExample());
    toolbar.addEntry("Status Toolbar", new StatusToolBarExample());
    toolbar.addEntry("Advanced Toolbar", new AdvancedToolBarExample());
    toolbar.addEntry("Overflow Toolbar", new ToolBarOverflowExample());
    toolbar.addEntry("MenuBar", new MenuBarExample());
    add(toolbar);

    Category templates = new Category("Templates & Lists");
    templates.addEntry("Templates", new TemplateExample());
    templates.addEntry("ListView", new ListViewExample());
    templates.addEntry("CheckBoxListView", new CheckBoxListViewExample());
    templates.addEntry("Advanced ListView", new ImageChooserExample());
    add(templates);

    Category button = new Category("Button");
    button.addEntry("Buttons", new ButtonsExample());
    button.addEntry("Button Aligning", new ButtonAlignExample());
    add(button);

    Category misc = new Category("Misc");
    misc.addEntry("ToolTips", new ToolTipsExample());
    misc.addEntry("DatePicker", new DatePickerExample());
    misc.addEntry("Draggable", new DraggableExample(), true);
    misc.addEntry("Resizable", new ResizableExample(), true);
    misc.addEntry("Slider", new SliderExample());
    misc.addEntry("Custom Slider", new CustomSliderExample());
    misc.addEntry("Fx", new FxExample(), true);
    add(misc);

    loadEntries(this);
  }

  public Entry findEntry(String name) {
    if (get(name) != null) {
      return (Entry) get(name);
    }
    for (Entry entry : getEntries()) {
      if (name.equals(entry.getId())) {
        return entry;
      }
    }
    return null;
  }

  public List<Entry> getEntries() {
    return entries;

  }

  private void loadEntries(TreeModel model) {
    for (ModelData child : model.getChildren()) {
      if (child instanceof Entry) {
        entries.add((Entry) child);
      } else if (child instanceof Category) {
        loadEntries((Category) child);
      }
    }
  }
}
