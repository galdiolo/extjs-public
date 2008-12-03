/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
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
import com.extjs.gxt.samples.client.examples.core.TemplateExample;
import com.extjs.gxt.samples.client.examples.dnd.DualListFieldExample;
import com.extjs.gxt.samples.client.examples.dnd.GridToGridExample;
import com.extjs.gxt.samples.client.examples.dnd.ListViewDNDExample;
import com.extjs.gxt.samples.client.examples.dnd.ReordingTreeDNDExample;
import com.extjs.gxt.samples.client.examples.dnd.TreeToTreeExample;
import com.extjs.gxt.samples.client.examples.forms.AdvancedComboBoxExample;
import com.extjs.gxt.samples.client.examples.forms.AdvancedFormsExample;
import com.extjs.gxt.samples.client.examples.forms.ComboBoxExample;
import com.extjs.gxt.samples.client.examples.forms.FileUploadExample;
import com.extjs.gxt.samples.client.examples.forms.FormsExample;
import com.extjs.gxt.samples.client.examples.grid.BeanModelGridExample;
import com.extjs.gxt.samples.client.examples.grid.EditableGridExample;
import com.extjs.gxt.samples.client.examples.grid.GridExample;
import com.extjs.gxt.samples.client.examples.grid.GridPluginsExample;
import com.extjs.gxt.samples.client.examples.grid.GroupingGridExample;
import com.extjs.gxt.samples.client.examples.grid.PagingBeanModelGridExample;
import com.extjs.gxt.samples.client.examples.grid.PagingGridExample;
import com.extjs.gxt.samples.client.examples.grid.TotalsGridExample;
import com.extjs.gxt.samples.client.examples.grid.XmlGridExample;
import com.extjs.gxt.samples.client.examples.layouts.AccordionLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.AnchorLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.BorderLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.CenterLayoutExample;
import com.extjs.gxt.samples.client.examples.layouts.RowLayoutExample;
import com.extjs.gxt.samples.client.examples.list.DataListExample;
import com.extjs.gxt.samples.client.examples.misc.ButtonsExample;
import com.extjs.gxt.samples.client.examples.misc.DatePickerExample;
import com.extjs.gxt.samples.client.examples.misc.DraggableExample;
import com.extjs.gxt.samples.client.examples.misc.FxExample;
import com.extjs.gxt.samples.client.examples.misc.ResizableExample;
import com.extjs.gxt.samples.client.examples.misc.ToolTipsExample;
import com.extjs.gxt.samples.client.examples.model.Category;
import com.extjs.gxt.samples.client.examples.model.Entry;
import com.extjs.gxt.samples.client.examples.organizer.ImageOrganizerExample;
import com.extjs.gxt.samples.client.examples.portal.PortalExample;
import com.extjs.gxt.samples.client.examples.tabs.AdvancedTabExample;
import com.extjs.gxt.samples.client.examples.tabs.BasicTabExample;
import com.extjs.gxt.samples.client.examples.toolbar.ToolBarExample;
import com.extjs.gxt.samples.client.examples.tree.AsyncTreeExample;
import com.extjs.gxt.samples.client.examples.tree.BasicTreeExample;
import com.extjs.gxt.samples.client.examples.tree.CheckboxTreeExample;
import com.extjs.gxt.samples.client.examples.tree.ContextMenuTreeExample;
import com.extjs.gxt.samples.client.examples.tree.FilterTreeExample;
import com.extjs.gxt.samples.client.examples.tree.TreeTableExample;
import com.extjs.gxt.samples.client.examples.view.ImageChooserExample;
import com.extjs.gxt.samples.client.examples.view.ListViewExample;
import com.extjs.gxt.samples.client.examples.windows.AccordionWindowExample;
import com.extjs.gxt.samples.client.examples.windows.DialogExample;
import com.extjs.gxt.samples.client.examples.windows.HelloWindowExample;
import com.extjs.gxt.samples.client.examples.windows.MessageBoxExample;
import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.extjs.gxt.ui.client.data.TreeModel;

public class ExamplesModel extends BaseTreeModel {

  protected List<Entry> entries = new ArrayList<Entry>();

  public ExamplesModel() {
    Category grids = new Category("Grids");
    grids.addEntry("Basic Grid", new GridExample());
    grids.addEntry("Grid Plugins", new GridPluginsExample());
    grids.addEntry("Editable Grid", new EditableGridExample());
    grids.addEntry("Xml Grid", new XmlGridExample());
    grids.addEntry("Paging", new PagingGridExample());
    grids.addEntry("Grouping", new GroupingGridExample());
    grids.addEntry("Live Group Summary", new TotalsGridExample());
    grids.addEntry("BeanModel Grid", new BeanModelGridExample());
    grids.addEntry("Paging BeanModel Grid", new PagingBeanModelGridExample());
    add(grids);

    Category tabs = new Category("Tabs");
    tabs.addEntry("Basic Tabs", new BasicTabExample());
    tabs.addEntry("Advanced Tabs", new AdvancedTabExample());
    add(tabs);

    Category dnd = new Category("Drag and Drop");
    dnd.addEntry("List to List", new ListViewDNDExample());
    
    dnd.addEntry("Grid to Grid", new GridToGridExample());
    dnd.addEntry("Tree to Tree", new TreeToTreeExample());
    dnd.addEntry("Reordering Tree", new ReordingTreeDNDExample());
    dnd.addEntry("Image Organizer", new ImageOrganizerExample());
    add(dnd);

    Category windows = new Category("Windows");
    windows.addEntry("Hello World", new HelloWindowExample());
    windows.addEntry("Accordion Window", new AccordionWindowExample());
    windows.addEntry("Dialog", new DialogExample());
    windows.addEntry("MessageBox", new MessageBoxExample());
    add(windows);

    Category trees = new Category("Trees");
    trees.addEntry("Basic Tree", new BasicTreeExample());
    trees.addEntry("Context Menu Tree", new ContextMenuTreeExample());
    trees.addEntry("Async Tree", new AsyncTreeExample());
    trees.addEntry("Filter Tree", new FilterTreeExample());
    trees.addEntry("Checkbox Tree", new CheckboxTreeExample());
    trees.addEntry("TreeTable", new TreeTableExample());
    add(trees);

    Category layouts = new Category("Layouts");
    layouts.addEntry("BorderLayout", new BorderLayoutExample(), true);
    layouts.addEntry("AccordionLayout", new AccordionLayoutExample());
    layouts.addEntry("AnchorLayout", new AnchorLayoutExample());
    layouts.addEntry("RowLayout", new RowLayoutExample(), true);
    layouts.addEntry("Portal", new PortalExample(), true);
    layouts.addEntry("CenterLayout", new CenterLayoutExample(), true);
    add(layouts);

    Category combos = new Category("Combos");
    combos.addEntry("ComboBox", new ComboBoxExample());
    combos.addEntry("Advanced ComboBox", new AdvancedComboBoxExample());
    add(combos);

    Category forms = new Category("Forms");
    forms.addEntry("Forms", new FormsExample());
    forms.addEntry("Advanced Forms", new AdvancedFormsExample());
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
    add(toolbar);

    Category templates = new Category("Templates & Lists");
    templates.addEntry("Templates", new TemplateExample());
    templates.addEntry("ListView", new ListViewExample());
    templates.addEntry("Advanced ListView", new ImageChooserExample());
    templates.addEntry("DataList", new DataListExample());
    add(templates);

    Category misc = new Category("Misc");
    misc.addEntry("Buttons", new ButtonsExample());
    misc.addEntry("ToolTips", new ToolTipsExample());
    misc.addEntry("DatePicker", new DatePickerExample());
    misc.addEntry("Draggable", new DraggableExample(), true);
    misc.addEntry("Resizable", new ResizableExample(), true);
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

  private void loadEntries(TreeModel<TreeModel> model) {
    for (TreeModel child : model.getChildren()) {
      if (child instanceof Entry) {
        entries.add((Entry) child);
      } else if (child instanceof Category) {
        loadEntries(child);
      }
    }
  }
}
