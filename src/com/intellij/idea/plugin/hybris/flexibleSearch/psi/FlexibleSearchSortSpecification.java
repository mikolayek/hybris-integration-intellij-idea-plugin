// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.flexibleSearch.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface FlexibleSearchSortSpecification extends PsiElement {

  @Nullable
  FlexibleSearchNullOrdering getNullOrdering();

  @Nullable
  FlexibleSearchOrderingSpecification getOrderingSpecification();

  @NotNull
  FlexibleSearchSortKey getSortKey();

}
