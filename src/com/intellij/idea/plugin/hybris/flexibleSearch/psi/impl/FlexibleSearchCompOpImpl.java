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

public class FlexibleSearchCompOpImpl extends ASTWrapperPsiElement implements FlexibleSearchCompOp {

  public FlexibleSearchCompOpImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FlexibleSearchVisitor visitor) {
    visitor.visitCompOp(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlexibleSearchVisitor) accept((FlexibleSearchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public PsiElement getEqualsOperator() {
    return findChildByType(EQUALS_OPERATOR);
  }

  @Override
  @Nullable
  public PsiElement getGreaterThanOrEqualsOperator() {
    return findChildByType(GREATER_THAN_OR_EQUALS_OPERATOR);
  }

  @Override
  @Nullable
  public PsiElement getLessThanOrEqualsOperator() {
    return findChildByType(LESS_THAN_OR_EQUALS_OPERATOR);
  }

}
