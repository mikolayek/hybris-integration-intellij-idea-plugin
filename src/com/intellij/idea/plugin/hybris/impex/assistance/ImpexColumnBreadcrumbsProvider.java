package com.intellij.idea.plugin.hybris.impex.assistance;

import com.intellij.idea.plugin.hybris.impex.ImpexLanguage;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexAnyHeaderMode;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderParameter;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexFullHeaderType;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexHeaderLine;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexTypes;
import com.intellij.idea.plugin.hybris.impex.psi.ImpexValueGroup;
import com.intellij.idea.plugin.hybris.impex.utils.ImpexPsiUtils;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.breadcrumbs.BreadcrumbsProvider;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ImpexColumnBreadcrumbsProvider implements BreadcrumbsProvider {

    @Override
    public Language[] getLanguages() {
        return new Language[]{ImpexLanguage.getInstance()};
    }

    @Override
    public boolean acceptElement(final @NotNull PsiElement element) {
        return element instanceof ImpexFullHeaderParameter ||
               element instanceof ImpexFullHeaderType ||
               element instanceof ImpexAnyHeaderMode;
    }

    @NotNull
    @Override
    public String getElementInfo(@NotNull PsiElement psi) {
        ImpexFullHeaderParameter headerParameter = getLinkedHeaderParameter(psi);
        if (headerParameter != null) {
            return headerParameter.getText();
        }

        //ImpexHeaderLine headerLine = PsiTreeUtil.getParentOfType(element, ImpexHeaderLine.class, false);
        //assert headerLine != null;

        psi = adjustWhiteSpaceAndSeparator(psi);

        headerParameter = PsiTreeUtil.getParentOfType(psi, ImpexFullHeaderParameter.class, false);
        if (headerParameter != null) {
            return headerParameter.getText();
        }

        ImpexAnyHeaderMode mode = PsiTreeUtil.getParentOfType(psi, ImpexAnyHeaderMode.class, false);
        if (mode != null) {
            return mode.getText();
        }

        ImpexFullHeaderType type = PsiTreeUtil.getParentOfType(psi, ImpexFullHeaderType.class, false);
        if (type != null) {
            return type.getHeaderTypeName().getText();
        }

        return "<error> : " + psi.getNode().getElementType() + ": " + psi.getText();
    }

    @Nullable
    @Override
    public PsiElement getParent(@NotNull PsiElement element) {

        final ImpexFullHeaderParameter linkedParameter = getLinkedHeaderParameter(element);
        if (linkedParameter != null) {
            return linkedParameter;
        }

        final ImpexFullHeaderParameter parentParameter =
            PsiTreeUtil.getParentOfType(element, ImpexFullHeaderParameter.class, true);
        if (parentParameter != null) {
            return parentParameter;
        }

        if (element instanceof ImpexAnyHeaderMode) {
            //we are done
            return null;
        }

        PsiElement adjusted = adjustWhiteSpaceAndSeparator(element);
        if (adjusted instanceof ImpexAnyHeaderMode) {
            //we are exactly at the space just after mode, mode itself had not been yet reported
            return adjusted;
        }

        if (adjusted instanceof ImpexFullHeaderParameter) {
            ImpexHeaderLine line = PsiTreeUtil.getParentOfType(adjusted, ImpexHeaderLine.class, false);
            return Optional.ofNullable(line)
                           .map(l -> Optional.<PsiElement>ofNullable(l.getFullHeaderType()).orElse(l.getAnyHeaderMode()))
                           .orElse(line);
        }
        if (adjusted instanceof ImpexFullHeaderType) {
            ImpexHeaderLine line = PsiTreeUtil.getParentOfType(adjusted, ImpexHeaderLine.class, false);
            return line == null ? null : line.getAnyHeaderMode();
        }

        return PsiTreeUtil.getParentOfType(
            adjusted,
            ImpexValueGroup.class,
            ImpexFullHeaderParameter.class,
            ImpexFullHeaderType.class,
            ImpexAnyHeaderMode.class
        );
    }

    @Nullable
    private static ImpexFullHeaderParameter getLinkedHeaderParameter(final @NotNull PsiElement psi) {

        return Optional.ofNullable(ImpexPsiUtils.getClosestSelectedValueGroupFromTheSameLine(psi))
                       .map(ImpexPsiUtils::getHeaderForValueGroup)
                       .map(o -> ObjectUtils.tryCast(o, ImpexFullHeaderParameter.class))
                       .orElse(null);
    }

    private static PsiElement adjustWhiteSpaceAndSeparator(PsiElement psi) {
        if (psi instanceof PsiWhiteSpace) {
            //white space should be adjusted to left
            PsiElement prev = PsiTreeUtil.skipSiblingsBackward(psi, PsiWhiteSpace.class);
            if (prev != null) {
                psi = prev;
            }
        } else if (psi.getNode().getElementType() == ImpexTypes.PARAMETERS_SEPARATOR && psi.getNextSibling() != null) {
            //separator is always belongs to the *next* parameter, so adjusting to right
            psi = psi.getNextSibling();
        }
        return psi;
    }

}
