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

public class FlexibleSearchPredicateImpl extends ASTWrapperPsiElement implements FlexibleSearchPredicate {

  public FlexibleSearchPredicateImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FlexibleSearchVisitor visitor) {
    visitor.visitPredicate(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlexibleSearchVisitor) accept((FlexibleSearchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public FlexibleSearchBetweenPredicate getBetweenPredicate() {
    return findChildByClass(FlexibleSearchBetweenPredicate.class);
  }

  @Override
  @Nullable
  public FlexibleSearchCompOp getCompOp() {
    return findChildByClass(FlexibleSearchCompOp.class);
  }

  @Override
  @Nullable
  public FlexibleSearchExistsPredicate getExistsPredicate() {
    return findChildByClass(FlexibleSearchExistsPredicate.class);
  }

  @Override
  @Nullable
  public FlexibleSearchInPredicate getInPredicate() {
    return findChildByClass(FlexibleSearchInPredicate.class);
  }

  @Override
  @Nullable
  public FlexibleSearchLikePredicate getLikePredicate() {
    return findChildByClass(FlexibleSearchLikePredicate.class);
  }

  @Override
  @Nullable
  public FlexibleSearchNullPredicate getNullPredicate() {
    return findChildByClass(FlexibleSearchNullPredicate.class);
  }

  @Override
  @NotNull
  public List<FlexibleSearchRowValuePredicand> getRowValuePredicandList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, FlexibleSearchRowValuePredicand.class);
  }

}
