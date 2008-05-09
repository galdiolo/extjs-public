/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.ui.rebind.core;

import java.io.PrintWriter;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.Selectors;
import com.extjs.gxt.ui.client.core.Selectors.Selector;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This is the thread-safe Generator for
 * {@link com.extjs.gxt.ui.client.core.Selectors Selector} subinterfaces.
 */
public class SelectorsGenerator extends Generator {

  @Override
  public String generate(TreeLogger logger, GeneratorContext context, String typeName)
      throws UnableToCompleteException {
    return new SelectorsSourceWriter(logger, context).generate(typeName);
  }

  public static class SelectorsSourceWriter {
    private TreeLogger logger;
    private GeneratorContext context;
    private JClassType type;
    private JClassType elType;

    private ClassSourceFileComposerFactory composer;
    private SourceWriter sw;

    public SelectorsSourceWriter(TreeLogger logger, GeneratorContext context) {
      this.logger = logger;
      this.context = context;
    }

    public String generate(String typeName) throws UnableToCompleteException {
      TypeOracle oracle = context.getTypeOracle();

      try {
        type = oracle.getType(typeName);
        elType = oracle.getType(El.class.getName());
      } catch (NotFoundException e) {
        logger.log(TreeLogger.ERROR, "Class " + typeName + " not found.", e);
        throw new UnableToCompleteException();
      }

      composer = new ClassSourceFileComposerFactory(type.getPackage().getName(),
          type.getName() + "Impl");
      composer.addImplementedInterface(type.getQualifiedSourceName());
      composer.addImport(Selectors.class.getName());
      composer.addImport(elType.getQualifiedSourceName());
      
      PrintWriter pw = context.tryCreate(logger, type.getPackage().getName(),
          type.getName() + "Impl");

      if (pw != null) {
        sw = composer.createSourceWriter(context, pw);

        for (JMethod method : type.getMethods()) {
          createMethod(method);
        }

        sw.commit(logger);
      }

      return typeName + "Impl";
    }

    private void createMethod(JMethod method) throws UnableToCompleteException {

      if (!(method.getReturnType() == elType)) {
        logger.log(TreeLogger.ERROR, "Method " + method.getName() + " does not return "
            + elType);
        throw new UnableToCompleteException();
      }

      String selector;
      Selector selectorAnn = method.getAnnotation(Selector.class);
      if (selectorAnn == null || "".equals(selectorAnn.value())) {
        logger.log(TreeLogger.ERROR, "Method " + method.getName()
            + " does not have a @Selector annotation");
        throw new UnableToCompleteException();
      }
      selector = selectorAnn.value();

      // public El icon(El root) {
      // return el.selectNode(".icon");
      // }
      //

      sw.indent();
      sw.print("public " + elType.getQualifiedSourceName() + " " + method.getName() + "("
          + elType.getQualifiedSourceName() + " root) {");
      sw.indent();
      sw.println("El el = root.selectNode(\"" + escape(selector) + "\");");
      // TODO do all selectors have to return a node ? Should null checking be
      // done here, or in the caller ?
      // sw.println("assert el != null : \"" + method.getName() + " using
      // selector '" + escape(selector) + "' returned a null element\";");
      sw.println("return el;");
      sw.outdent();
      sw.println("}");
    }

  }

}
