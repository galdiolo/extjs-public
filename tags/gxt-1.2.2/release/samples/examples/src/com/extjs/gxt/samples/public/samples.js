/*
 * Ext JS Library 2.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

SamplePanel = Ext.extend(Ext.DataView, {
    autoHeight: true,
    frame:true,
    cls:'demos',
    itemSelector: 'dd',
    overClass: 'over',
    
    tpl : new Ext.XTemplate(
        '<div id="sample-ct">',
            '<tpl for=".">',
            '<div><a name="{id}"></a><h2><div>{title}</div></h2>',
            '<dl>',
                '<tpl for="samples">',
                    '<dd ext:url="{url}"><img src="images/thumbs/{icon}"/>',
                        '<div><h4>{text}</h4><p>{desc}</p></div>',
                    '</dd>',
                '</tpl>',
            '<div style="clear:left"></div></dl></div>',
            '</tpl>',
        '</div>'
    ),

    onClick : function(e){
        var group = e.getTarget('h2', 3, true);
        if(group){
            group.up('div').toggleClass('collapsed');
        }else {
            var t = e.getTarget('dd', 5, true);
            if(t && !e.getTarget('a', 2)){
                var url = t.getAttributeNS('ext', 'url');
                window.open(url);
            }
        }
        return SamplePanel.superclass.onClick.apply(this, arguments);
    }
});


Ext.EventManager.on(window, 'load', function(){
    var catalog = [{
        title: 'Combination Samples',
        samples: [{
            text: 'Explorer Demo',
            url: 'http://extjs.com/explorer',
            icon: 'explorer.gif',
            desc: 'Explore the Ext GWT Components and quickly view the source code to see the API in action.'
        },{
            text: 'Mail App',
            url: 'http://extjs.com/mail',
            icon: 'mail.gif',
            desc: 'A mail application with a preview pane that retrieves data using the Ext GWT data loading API.'
        },{
            text: 'Web Desktop',
            url: 'http://extjs.com/deploy/gxt-1.2.2/samples/desktop/www/com.extjs.gxt.samples.desktop.DesktopApp/',
            icon: 'desktop.gif',
            desc: 'Demonstrates how one could build a desktop in the browser using Ext components including a module plugin system.'
        }]
    },{
        title: 'Grids',
        samples: [{
            text: 'Basic Grid',
            url: 'grid/grid.html',
            icon: 'basicgrid.gif',
            desc: 'A basic read-only grid loaded from local data that demonstrates the use of custom column renderers.'
        },{
            text: 'Editable Grid',
            url: 'grid/editable.html',
            icon: 'editablegrid.gif',
            desc: 'An editable grid loaded from local data that shows multiple types of grid editors..'
        },{
            text: 'XML Grid',
            url: 'grid/xml.html',
            icon: 'xmlgrid.gif',
            desc: 'A simple read-only grid loaded from XML data.'
        },{
            text: 'Paging',
            url: 'grid/paging.html',
            icon: 'paging.gif',
            desc: 'A grid with server side paging, using GWT RPC.'
        },{
            text: 'Grouping',
            url: 'grid/grouping.html',
            icon: 'grouping.gif',
            desc: 'A basic grouping grid showing collapsible data groups that can be customized via the "Group By" header menu option.'
        },{
            text: 'Live Group Summary',
            url: 'grid/totals.html',
            icon: 'livegroupsummary.gif',
            desc: 'Advanced grouping grid that allows cell editing and includes custom dynamic summary calculations.'
        },{
            text: 'Grid Plugins',
            url: 'grid/plugins.html',
            icon: 'gridplugins.gif',
            desc: 'Multiple grids customized via plugins: expander rows, checkbox selection and row numbering.'
        }]
    },{
        title: 'Tabs',
        samples: [{
            text: 'Basic Tabs',
            url: 'tabs/tabs.html',
            icon: 'basictabs.gif',
            desc: 'Basic tab functionality including autoHeight, tabs from markup, Ajax loading and tab events.'
        },{
            text: 'Advanced Tabs',
            url: 'tabs/advanced.html',
            icon: 'advancedtabs.gif',
            desc: 'Advanced tab features including tab scrolling, adding tabs programmatically.'
        }]
    },{
        title: 'Drag and Drop',
        samples: [{
            text: 'Basic DND',
            url: 'dnd/basicdnd.html',
            icon: 'basicdnd.gif',
            desc: 'A basic DND example not using any specialized drag sources or drop targets.'
        },{
            text: 'List to List',
            url: 'dnd/listtolist.html',
            icon: 'listtolist.gif',
            desc: 'Drag and drop between two lists.'
        },{
            text: 'Grid to Grid',
            url: 'dnd/gridtogrid.html',
            icon: 'gridtogrid.gif',
            desc: 'Drag and drop between two grids supporting both appends and inserts.'
        },{
	        text: 'Tree to Tree',
	        url: 'dnd/treetotree.html',
	        icon: 'treetotree.gif',
	        desc: 'Drag and drop between two sorted trees.'
        },{
	        text: 'Reordering Tree',
	        url: 'dnd/reorderingtree.html',
	        icon: 'reorderingtree.gif',
	        desc: 'A single tree where nodes and leafs can be reordered.'
        },{
	        text: 'Image Organizer',
	        url: 'dnd/imageorganizer.html',
	        icon: 'imageorganizer.gif',
	        desc: 'The image organizer shows an example of dragging a picture from a list to a folder in a tree.'
        }]
    },{
        title: 'Windows',
        samples: [{
            text: 'Tree to Tree',
            url: 'window/hello.html',
            icon: 'helloworld.gif',
            desc: 'Simple "Hello World" window that contains a basic TabPanel.'
        },{
            text: 'Accordion Window',
            url: 'window/accordion.html',
            icon: 'accordionwindow.gif',
            desc: 'Window with a nested AccordionLayout.'
        },{
            text: 'MessageBox',
            url: 'window/messagebox.html',
            icon: 'messagebox.gif',
            desc: 'Different styles include confirm, alert, prompt, progress and wait and also support custom icons.'
        },{
            text: 'Dialog',
            url: 'window/dialog.html',
            icon: 'dialog.gif',
            desc: 'Windows with specialized support for buttons.'
        }]
    },{
        title: 'Trees',
        samples: [{
            text: 'Basic Tree',
            url: 'tree/basic.html',
            icon: 'basictree.gif',
            desc: 'A basic singl-select tree with custom icons.'
        },{
            text: 'Async Tree',
            url: 'tree/async.html',
            icon: 'asynctree.gif',
            desc: 'A tree that loads its children on-demand.'
 		},{
            text: 'Context Menu Tree',
            url: 'tree/contextmenu.html',
            icon: 'contextmenutree.gif',
            desc: 'A tree with custom context menu for adding and removing nodes.'
        },{
            text: 'Checkbox Tree',
            url: 'tree/checkbox.html',
            icon: 'checkboxtree.gif',
            desc: 'A checkbox tree with cascading check behavior.'
        },{
            text: 'Filter Tree',
            url: 'tree/filter.html',
            icon: 'filtertree.gif',
            desc: 'A tree that filters its content based on the input in a store filter text field.'
		},{
            text: 'TreeTable',
            url: 'tree/treetable.html',
            icon: 'treetable.gif',
            desc: 'A custom tree with table based column support.'
        }]
    },{
        title: 'Layout Managers',
        samples: [{
            text: 'Border Layout',
            url: 'layouts/borderlayout.html',
            icon: 'borderlayout.gif',
            desc: 'A complex BorderLayout implementation that shows nesting multiple components and sub-layouts.'
        },{
            text: 'Accordion Layout',
            url: 'layouts/accordionlayout.html',
            icon: 'accordionlayout.gif',
            desc: 'A example of accordioan layout which stacks its chidlren in collapsible panels.'
        },{
            text: 'Anchor Layout',
            url: 'layouts/anchorlayout.html',
            icon: 'anchorlayout.gif',
            desc: 'A simple example of anchoring form fields to a window for flexible form resizing.'
		},{
            text: 'Row Layout',
            url: 'layouts/rowlayout.html',
            icon: 'rowlayout.gif',
            desc: 'Lays out the components in a single row or column, allowing precise control over sizing.'
        },{
            text: 'CenterLayout',
            url: 'layouts/centerlayout.html',
            icon: 'centerlayout.gif',
            desc: 'Centers the child component in its container.'
		},{
            text: 'Portal Demo',
            url: 'portal/portal.html',
            icon: 'portal.gif',
            desc: 'A page layout using several custom extensions to provide a web portal interface.'
        }]
    },{
        title: 'ComboBox',
        samples: [{
            text: 'Basic ComboBox',
            url: 'forms/combos.html',
            icon: 'combobox.gif',
            desc: 'Basic combos with auto-complete, type ahead, custom templates.'
        },{
            text: 'ComboBox Templates',
            url: 'forms/forumsearch.html',
            icon: 'forumsearch.gif',
            desc: 'Customized combo with template-based list rendering, remote loading and paging.'
        }]
    },{
        title: 'Forms',
        samples: [{
            text: 'Forms',
            url: 'forms/forms.html',
            icon: 'forms.gif',
            desc: 'Various example forms showing collapsible fieldsets.'
        },{
            text: 'Advanced Forms',
            url: 'forms/advanced.html',
            icon: 'advancedforms.gif',
            desc: 'Advanced form layouts with nested column layout and tab panels.'
        },{
            text: 'DualListField',
            url: 'forms/duallistfield.html',
            icon: 'duallistfield.gif',
            desc: 'A field that displays two list fields and allows selections to be dragged between lists.'
        },{
            text: 'File Upload',
            url: 'forms/fileupload.html',
            icon: 'fileupload.gif',
            desc: 'A field that allows a user to upload a file via a standard HTML submit.'
        }]
    },{
        title: 'Data Binding',
        samples: [{
            text: 'Basic Binding',
            url: 'binding/basicbinding.html',
            icon: 'basicbinding.gif',
            desc: 'Basic binding between model and a form.'
        },{
            text: 'Grid Binding',
            url: 'binding/gridbinding.html',
            icon: 'gridbinding.gif',
            desc: 'Demonstrates an example of binding a model to a form based on the selection of a grid.'
        },{
            text: 'Grid Store Binding',
            url: 'binding/gridstorebinding.html',
            icon: 'gridstorebinding.gif',
            desc: 'Edits are made to the grid are done via the store via records. Edits are cached and can be committed or rejected.'
        }]
    },{
        title: 'Toolbars and Menus',
        samples: [{
            text: 'Basic Toolbar',
            url: 'toolbar/toolbar.html',
            icon: 'basictoolbar.gif',
            desc: 'Toolbar and menus that contain various components like date pickers, sub-menus and more.'
        }]
    },{
        title: 'Templates and Lists',
        samples: [{
            text: 'Templates',
            url: 'core/templates.html',
            icon: 'templates.gif',
            desc: 'A simple example of rendering views from templates bound to data objects.'
        },{
            text: 'ListView',
            url: 'view/listview.html',
            icon: 'listview.gif',
            desc: 'A template driven multi-selct list view.'
		},{
            text: 'Advanced ListView',
            url: 'view/chooser.html',
            icon: 'advancedlistview.gif',
            desc: 'A more customized ListView supporting sorting and filtering with multiple templates.'
		},{
            text: 'List',
            url: 'list/datalist.html',
            icon: 'datalist.gif',
            desc: 'Includes both a single and multi select data list.'
        }]
    },{
        title: 'Miscellaneous',
        samples: [{
            text: 'Buttons',
            url: 'misc/buttons.html',
            icon: 'buttons.gif',
            desc: 'Various button exmamples including icons, disabled, toggled, and split buttons.'
        },{
            text: 'ToolTips',
            url: 'tips/tooltips.html',
            icon: 'tooltips.gif',
            desc: 'Custom tooltip with title and text.'
		},{
            text: 'DatePicker',
            url: 'misc/datepicker.html',
            icon: 'datepicker.gif',
            desc: 'Component used to select a date.'
        },{
            text: 'Resizable',
            url: 'misc/resizable.html',
            icon: 'resizable.gif',
            desc: 'Example of adding 8-way resizing to a content panel.'
		},{
            text: 'Draggable',
            url: 'misc/draggable.html',
            icon: 'draggable.gif',
            desc: 'Examples of making any element resizable with various configuration options.'
		},{
            text: 'Fx',
            url: 'misc/fx.html',
            icon: 'fx.gif',
            desc: 'Examples of different effects including sliding and moving.'
        }]
    }];
    
	var url = window.location.host;
	var server = url.indexOf('extjs.com') != -1;
	if (!server) {
		catalog.splice(0,1);
	}

    for(var i = 0, c; c = catalog[i]; i++){
        c.id = 'sample-' + i;
    }

    var store = new Ext.data.JsonStore({
        idProperty: 'id',
        fields: ['id', 'title', 'samples'],
        data: catalog
    });

    new Ext.Panel({
        autoHeight: true,
        collapsible: true,
        frame: true,
        title: 'View Samples',
        items: new SamplePanel({
            store: store
        })
    }).render('all-demos');

    var tpl = new Ext.XTemplate(
        '<tpl for="."><li><a href="#{id}">{title:stripTags}</a></li></tpl>'
    );
    tpl.overwrite('sample-menu', catalog);

    Ext.select('#sample-spacer').remove();

    setTimeout(function(){
        Ext.get('loading').remove();
        Ext.get('loading-mask').fadeOut({remove:true});
    }, 250);

    if(window.console && window.console.firebug){
        Ext.Msg.alert('Warning', 'Firebug is known to cause performance issues with Ext JS.');
    }
});