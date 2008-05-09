package com.extjs.gxt.ui.client.widget.tree;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Selectors;

public interface TreeSelectors extends Selectors {

  @Selector(".my-tree-joint")
  public El joint(El root);
  
  @Selector(".my-tree-joint")
  public El check(El root);
  
  @Selector(".my-tree-icon")
  public El icon(El root);
}
