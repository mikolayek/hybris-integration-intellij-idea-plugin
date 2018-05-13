// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.idea.plugin.hybris.flexibleSearch.psi.FlexibleSearchTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.idea.plugin.hybris.flexibleSearch.psi.*;

public class FlexibleSearchInPredicateImpl extends ASTWrapperPsiElement implements FlexibleSearchInPredicate {

  public FlexibleSearchInPredicateImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FlexibleSearchVisitor visitor) {
    visitor.visitInPredicate(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlexibleSearchVisitor) accept((FlexibleSearchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<FlexibleSearchRowValuePredicand> getRowValuePredicandList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchRowValuePredicand.class);
  }

  @Override
  @Nullable
  public FlexibleSearchTableSubquery getTableSubquery() {
    return findChildByClass(FlexibleSearchTableSubquery.class);
  }

}
