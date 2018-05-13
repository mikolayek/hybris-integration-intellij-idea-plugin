// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FlexibleSearchTableExpression extends PsiElement {

  @NotNull
  FlexibleSearchFromClause getFromClause();

  @Nullable
  FlexibleSearchGroupByClause getGroupByClause();

  @Nullable
  FlexibleSearchOrderByClause getOrderByClause();

  @Nullable
  FlexibleSearchWhereClause getWhereClause();

}
