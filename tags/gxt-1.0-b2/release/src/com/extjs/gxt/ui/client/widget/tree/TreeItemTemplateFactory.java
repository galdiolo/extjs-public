package com.extjs.gxt.ui.client.widget.tree;

import com.extjs.gxt.ui.client.core.Template;
import com.extjs.gxt.ui.client.core.TemplateFactory;

public interface TreeItemTemplateFactory extends TemplateFactory {

  @Cache
  public Template createTemplate();
}
