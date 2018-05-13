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

public class FlexibleSearchCharacterLikePredicateImpl extends ASTWrapperPsiElement implements FlexibleSearchCharacterLikePredicate {

  public FlexibleSearchCharacterLikePredicateImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull FlexibleSearchVisitor visitor) {
    visitor.visitCharacterLikePredicate(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof FlexibleSearchVisitor) accept((FlexibleSearchVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public FlexibleSearchCharacterPattern getCharacterPattern() {
    return findChildByClass(FlexibleSearchCharacterPattern.class);
  }

  @Override
  @NotNull
  public FlexibleSearchRowValuePredicand getRowValuePredicand() {
    return findNotNullChildByClass(FlexibleSearchRowValuePredicand.class);
  }

  @Override
  @Nullable
  public FlexibleSearchValueExpression getValueExpression() {
    return findChildByClass(FlexibleSearchValueExpression.class);
  }

}
