package com.coremedia.docbook.idea;

import com.intellij.javaee.ResourceRegistrar;
import com.intellij.javaee.StandardResourceProvider;

/**
 * Register the DocBook RelaxNG schema (with some CoreMedia limitations).
 */
public class DocBookResourceProvider implements StandardResourceProvider{

  public void registerResources(ResourceRegistrar registrar) {
    registrar.addStdResource("http://docbook.org/ns/docbook", "/com/coremedia/docbook/idea/coremedia-schema.rnc", getClass());
  }
}
