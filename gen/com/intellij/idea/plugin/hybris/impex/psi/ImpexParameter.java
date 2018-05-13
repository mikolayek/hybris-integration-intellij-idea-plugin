// This is a generated file. Not intended for manual editing.
package com.intellij.idea.plugin.hybris.impex.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ImpexParameter extends PsiElement {

  @NotNull
  List<ImpexMacroUsageDec> getMacroUsageDecList();

  @NotNull
  List<ImpexModifiers> getModifiersList();

  @Nullable
  ImpexSubParameters getSubParameters();

}
