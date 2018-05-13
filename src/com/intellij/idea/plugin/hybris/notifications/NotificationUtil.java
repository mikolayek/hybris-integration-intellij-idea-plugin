package com.intellij.idea.plugin.hybris.notifications;

import com.intellij.idea.plugin.hybris.common.utils.HybrisI18NBundleUtils;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.ui.SystemNotifications;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Eugene.Kudelevsky
 */
public class NotificationUtil {

    private NotificationUtil() {
    }

    public static final NotificationGroup NOTIFICATION_GROUP = NotificationGroup.balloonGroup(
        HybrisI18NBundleUtils.message("hybris.notification.group"));

    public static void showSystemNotificationIfNotActive(
        @NotNull Project project,
        @NotNull String notificationName,
        @NotNull String notificationTitle,
        @NotNull String notificationText
    ) {
        final JFrame frame = WindowManager.getInstance().getFrame(project);

        if (frame != null && !frame.hasFocus()) {
            SystemNotifications.getInstance().notify(notificationName, notificationTitle, notificationText);
        }
    }
}
