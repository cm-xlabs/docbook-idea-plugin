package com.coremedia.docbook.idea;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;

/**
 * Register DocBook live templates.
 */
public class DocBookLiveTemplatesProvider implements DefaultLiveTemplatesProvider {

  private static final String[] DOCBOOK_LIVE_TEMPLATES = new String[]{"/liveTemplates/docbook"};

  public String[] getDefaultLiveTemplateFiles() {
    return DOCBOOK_LIVE_TEMPLATES;
  }

  public String[] getHiddenLiveTemplateFiles() {
    return null;
  }
}
