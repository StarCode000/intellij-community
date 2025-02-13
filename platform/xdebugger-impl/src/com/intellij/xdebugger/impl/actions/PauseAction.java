// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.xdebugger.impl.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.impl.DebuggerSupport;
import com.intellij.xdebugger.impl.ui.DebuggerUIUtil;
import org.jetbrains.annotations.NotNull;

public class PauseAction extends XDebuggerActionBase {
  @Override
  protected @NotNull DebuggerActionHandler getHandler(final @NotNull DebuggerSupport debuggerSupport) {
    return debuggerSupport.getPauseHandler();
  }

  @Override
  protected boolean isHidden(@NotNull AnActionEvent event) {
    if (!isPauseResumeMerged()) {
      return super.isHidden(event);
    }
    Project project = event.getProject();
    if (project == null) {
      return false;
    }
    XDebugSession session = DebuggerUIUtil.getSession(event);
    if (session == null || session.isStopped()) {
      return false;
    }
    return super.isHidden(event) || session.isPaused();
  }

  static boolean isPauseResumeMerged() {
    return Registry.is("debugger.merge.pause.and.resume");
  }
}
