package com.intellij.idea.plugin.hybris.project.wizard;

import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.idea.plugin.hybris.common.services.CommonIdeaService;
import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.projectImport.ProjectImportWizardStep;
import com.intellij.ui.BrowserHyperlinkListener;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;

public class DiscountImportstep extends ProjectImportWizardStep {

    private JPanel rootPanel;
    private JEditorPane discountEditorPane;
    private JLabel discountLabel;
    private static final Logger LOG = Logger.getInstance(DiscountImportstep.class);

    public DiscountImportstep(final WizardContext wizardContext) {
        super(wizardContext);
    }

    @Override
    public JComponent getComponent() {
        return rootPanel;
    }

    @Override
    public void updateDataModel() {
    }

    @Override
    public void updateStep() {
        if (CommonIdeaService.getInstance().isFansTargetGroup()) {
            discountLabel.setText(HybrisI18NBundleUtils.message("support.us.step.title"));
            discountEditorPane.setText(HybrisI18NBundleUtils.message("support.us.step.text"));
        }
    }

    @Override
    public boolean isStepVisible() {
        return CommonIdeaService.getInstance().isFansTargetGroup() || CommonIdeaService.getInstance().isDiscountTargetGroup();
    }

    private void createUIComponents() {
        final Font font = UIManager.getFont("Label.font");
        final String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: " + font.getSize() + "pt; }";
        discountEditorPane = new JEditorPane();
        discountEditorPane.setEditorKit(new HTMLEditorKit());
        ((HTMLDocument) discountEditorPane.getDocument()).getStyleSheet().addRule(bodyRule);
        discountEditorPane.addHyperlinkListener(BrowserHyperlinkListener.INSTANCE);
    }
}
