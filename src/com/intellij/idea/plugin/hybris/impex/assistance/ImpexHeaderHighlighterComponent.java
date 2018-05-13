/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.impex.assistance;

import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerAdapter;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiTreeChangeListener;
import com.intellij.psi.util.PsiUtilBase;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;


/**
 * Created 19:56 11 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexHeaderHighlighterComponent implements ApplicationComponent {

    protected final CommonIdeaService commonIdeaService;
    protected final CaretListener caretListener = new ImpexHeaderHighlightingCaretListener();
    protected final CaretListener caretColumnListener = new ImpexColumnHighlightingCaretListener();
    protected final ProjectManagerListener projectManagerListener = new ImpexProjectManagerListener();
    protected final PsiTreeChangeListener psiTreeChangeListener = new ImpexPsiTreeChangeListener();
    protected final EditorFactoryListener editorFactoryListener = new ImpexEditorFactoryListener();
    protected final ImpexHeaderNameHighlighterService impexHeaderNameHighlighterService;
    protected final ImpexColumnHighlighterService impexColumnHighlighterService;

    public ImpexHeaderHighlighterComponent(
        final CommonIdeaService commonIdeaService,
        final ImpexHeaderNameHighlighterService impexHeaderNameHighlighterService,
        final ImpexColumnHighlighterService impexColumnHighlighterService
    ) {
        Validate.notNull(commonIdeaService);
        Validate.notNull(impexHeaderNameHighlighterService);
        Validate.notNull(impexColumnHighlighterService);

        this.commonIdeaService = commonIdeaService;
        this.impexColumnHighlighterService = impexColumnHighlighterService;
        this.impexHeaderNameHighlighterService = impexHeaderNameHighlighterService;
    }

    @Override
    public void initComponent() {
        EditorFactory.getInstance().getEventMulticaster().addCaretListener(caretListener);
        EditorFactory.getInstance().getEventMulticaster().addCaretListener(caretColumnListener);
        ProjectManager.getInstance().addProjectManagerListener(this.projectManagerListener);
    }

    @Override
    public void disposeComponent() {
        EditorFactory.getInstance().getEventMulticaster().removeCaretListener(caretListener);
        EditorFactory.getInstance().getEventMulticaster().removeCaretListener(caretColumnListener);
        ProjectManager.getInstance().removeProjectManagerListener(this.projectManagerListener);
    }

    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getName();
    }

    protected class ImpexHeaderHighlightingCaretListener implements CaretListener {

        @Override
        public void caretPositionChanged(final CaretEvent e) {
            if (commonIdeaService.isTypingActionInProgress()) {
                return;
            }

            impexHeaderNameHighlighterService.highlightCurrentHeader(e.getEditor());
        }

        @Override
        public void caretAdded(final CaretEvent e) {
        }

        @Override
        public void caretRemoved(final CaretEvent e) {
        }
    }

    protected class ImpexColumnHighlightingCaretListener implements CaretListener {

        @Override
        public void caretPositionChanged(final CaretEvent e) {
            if (commonIdeaService.isTypingActionInProgress()) {
                return;
            }

            impexColumnHighlighterService.highlightCurrentColumn(e.getEditor());
        }

        @Override
        public void caretAdded(final CaretEvent e) {
        }

        @Override
        public void caretRemoved(final CaretEvent e) {
        }
    }

    protected class ImpexProjectManagerListener extends ProjectManagerAdapter implements ProjectManagerListener {

        @Override
        public void projectOpened(final Project project) {
            super.projectOpened(project);
            PsiManager.getInstance(project).addPsiTreeChangeListener(psiTreeChangeListener);
            EditorFactory.getInstance().addEditorFactoryListener(editorFactoryListener, project);
        }

        @Override
        public void projectClosed(final Project project) {
            super.projectClosed(project);
            PsiManager.getInstance(project).removePsiTreeChangeListener(psiTreeChangeListener);
        }

    }

    protected class ImpexPsiTreeChangeListener implements PsiTreeChangeListener {

        private void highlightHeader(final PsiTreeChangeEvent psiTreeChangeEvent) {
            final PsiFile file = psiTreeChangeEvent.getFile();

            if (null == file) {
                return;
            }

            final Editor editor = PsiUtilBase.findEditor(file);

            if (null == editor) {
                return;
            }

            impexHeaderNameHighlighterService.highlightCurrentHeader(editor);
            impexColumnHighlighterService.highlightCurrentColumn(editor);
        }

        @Override
        public void beforeChildAddition(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforeChildRemoval(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforeChildReplacement(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforeChildMovement(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforeChildrenChange(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforePropertyChange(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childAdded(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childRemoved(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childReplaced(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childrenChanged(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
//            this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childMoved(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void propertyChanged(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }
    }

    protected class ImpexEditorFactoryListener implements EditorFactoryListener {

        @Override
        public void editorCreated(@NotNull final EditorFactoryEvent editorFactoryEvent) {

        }

        @Override
        public void editorReleased(@NotNull final EditorFactoryEvent editorFactoryEvent) {
            impexHeaderNameHighlighterService.releaseEditorData(editorFactoryEvent.getEditor());
            impexColumnHighlighterService.releaseEditorData(editorFactoryEvent.getEditor());
        }
    }
}
