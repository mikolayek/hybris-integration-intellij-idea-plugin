/*
 * This file is part of "hybris integration" plugin for Intellij IDEA.
 * Copyright (C) 2014-2016 Alexander Bartash <AlexanderBartash@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.intellij.idea.plugin.hybris.statistics.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.idea.plugin.hybris.settings.HybrisApplicationSettingsComponent;
import com.intellij.idea.plugin.hybris.statistics.StatsCollector;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.ui.LicensingFacade;
import com.intellij.util.containers.ContainerUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 28/2/17.
 */
@State(name = "HybrisStatistics", storages = @Storage("hybrisStatistics.xml"))
public class DefaultStatsCollector implements StatsCollector, PersistentStateComponent<Element> {

    private static final Logger LOG = Logger.getInstance(DefaultStatsCollector.class);
    private static final int MAX_QUEUE_SIZE = 1000;
    private static final long DELAY_AFTER_FAILURE = TimeUnit.MINUTES.toMillis(10);
    private static final String ATTR_ACTION = "action";
    private static final String ATTR_NAME = "params";
    private static final String ATTR_DATE = "date";
    private static final String TAG_ENTITY = "entity";

    private final Map<ACTIONS, Long> cache = new HashMap<>();
    private final List<Entity> queue = ContainerUtil.newArrayList();
    private final Object lock = new Object();

    private long lastFailureTime = 0;
    private volatile boolean disposed = false;
    private boolean isOpenCollectiveContributor = false;

    @Override
    public void initComponent() {
        new Thread(this::flushingLoop).start();
    }

    @Override
    public void disposeComponent() {
        disposed = true;

        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void flushingLoop() {
        while (!disposed) {
            List<Entity> entitiesToFlush;

            synchronized (lock) {

                while (!disposed) {
                    final long neededDelay = DELAY_AFTER_FAILURE - (System.currentTimeMillis() - lastFailureTime);

                    if (!queue.isEmpty() && neededDelay <= 0) {
                        break;
                    }
                    try {
                        lock.wait(Math.max(0, neededDelay));
                    } catch (InterruptedException e) {
                        LOG.error(e);
                        return;
                    }
                }
                entitiesToFlush = ContainerUtil.newArrayList(queue);
            }
            if (disposed) {
                return;
            }
            try {
                final StatsResponse response = createStatsRequest(buildParameters(entitiesToFlush)).call();
                final int statusCode = response.getResponse().getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    synchronized (lock) {
                        queue.removeAll(entitiesToFlush);
                    }
                } else {
                    LOG.debug("HTTP response status code: " + statusCode);
                    lastFailureTime = System.currentTimeMillis();
                }
            } catch (Exception e) {
                if (!(e instanceof IOException)) {
                    LOG.error(e);
                }
                lastFailureTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void collectStat(@NotNull final ACTIONS action) {
        collectStat(action, null);
    }

    @Override
    public void collectStat(@NotNull final ACTIONS action, @Nullable final String parameters) {
        collectStat(new Entity(action, parameters, getCurrentDateTimeWithTimeZone()));
    }

    @Override
    public boolean isOpenCollectiveContributor() {
        return isOpenCollectiveContributor;
    }

    @Override
    public void setOpenCollectiveContributor(final boolean isOpenCollectiveContributor) {
        this.isOpenCollectiveContributor = isOpenCollectiveContributor;
    }

    public void collectStat(@NotNull final Entity entity) {
        HybrisApplicationSettingsComponent.getInstance().addUsedAction(entity.getAction());

        if (isOpenCollectiveContributor() && HybrisApplicationSettingsComponent.getInstance().getState().isDisallowedSendingStatistics()) {
            return;
        }

        if (shouldPostStat(entity.getAction())) {
            cache.put(entity.getAction(), System.currentTimeMillis());

            synchronized (lock) {
                queue.add(entity);

                if (queue.size() > MAX_QUEUE_SIZE) {
                    queue.remove(0);
                }
                lock.notifyAll();
            }
        }
    }

    private boolean shouldPostStat(final ACTIONS action) {
        Long sendTime = cache.get(action);
        if (sendTime == null) {
            return true;
        }
        long diff = System.currentTimeMillis() - sendTime;
        return diff > TimeUnit.HOURS.toMillis(12);
    }

    @NotNull
    protected StatsRequest createStatsRequest(
        @NotNull final List<NameValuePair> urlParameters
    ) {
        return new StatsRequest(urlParameters);
    }

    protected List<NameValuePair> buildParameters(@NotNull final List<Entity> entities) {
        final List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("ide_version", getIdeVersion()));
        urlParameters.add(new BasicNameValuePair("ide_type", getIdeType()));

        final String registeredTo = getRegisteredTo();
        if (null != registeredTo) {
            urlParameters.add(new BasicNameValuePair("registered_to", DigestUtils.sha512Hex(registeredTo)));
        }

        String loginName = getLoginName();
        if (null != loginName) {
            urlParameters.add(new BasicNameValuePair("login_name", DigestUtils.sha512Hex(loginName)));
        }
        String osName = System.getProperty("os.name").toLowerCase();
        urlParameters.add(new BasicNameValuePair("os", osName));


        urlParameters.add(new BasicNameValuePair("plugin_version", getPluginVersion()));
        urlParameters.add(new BasicNameValuePair("request_date", getCurrentDateTimeWithTimeZone()));
        final JsonArray jsonActions = new JsonArray();

        for (Entity entity : entities) {
            final JsonObject jsonEntity = new JsonObject();
            jsonEntity.addProperty("name", entity.getAction().name());
            jsonEntity.addProperty("date", entity.getDateStr());
            final String parameters = entity.getParameters();

            if (parameters != null) {
                jsonEntity.addProperty("params", parameters);
            }
            jsonActions.add(jsonEntity);
        }
        urlParameters.add(new BasicNameValuePair("actions", jsonActions.toString()));
        return urlParameters;
    }

    protected String getCurrentDateTimeWithTimeZone() {
        final ZonedDateTime localDateTime = ZonedDateTime.now();
        return localDateTime.toString();
    }

    protected String getPluginVersion() {
        final IdeaPluginDescriptor plugin = PluginManager.getPlugin(PluginId.getId(HybrisConstants.PLUGIN_ID));
        return null == plugin ? null : plugin.getVersion();
    }

    protected String getIdeVersion() {
        return ApplicationInfo.getInstance().getFullVersion();
    }

    protected String getIdeType() {
        return ApplicationInfo.getInstance().getBuild().getProductCode();
    }

    @Nullable
    protected String getRegisteredTo() {
        final LicensingFacade instance = LicensingFacade.getInstance();

        return null == instance ? null : instance.getLicensedToMessage();
    }

    protected String getLoginName() {
        String osName = System.getProperty("os.name").toLowerCase();
        String className = null;
        String methodName = "getUsername";

        if (osName.contains("windows")) {
            className = "com.sun.security.auth.module.NTSystem";
            methodName = "getName";
        } else if (osName.contains("linux")) {
            className = "com.sun.security.auth.module.UnixSystem";
        } else if (osName.contains("solaris") || osName.contains("sunos")) {
            className = "com.sun.security.auth.module.SolarisSystem";
        }

        if (className != null) {
            try {
                Class<?> c = Class.forName(className);
                Method method = c.getDeclaredMethod(methodName);
                Object o = c.newInstance();
                return (String) method.invoke(o);
            } catch (ClassNotFoundException | NoSuchMethodException |
                IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
                // no-op
            }
        }

        return System.getProperty("user.name");
    }

    @Nullable
    @Override
    public Element getState() {
        synchronized (lock) {
            final Element element = new Element("state");

            for (Entity entity : queue) {
                final Element entityElement = new Element(TAG_ENTITY);
                entityElement.setAttribute(ATTR_ACTION, entity.getAction().name());
                entityElement.setAttribute(ATTR_DATE, entity.getDateStr());
                final String parameters = entity.getParameters();

                if (parameters != null) {
                    entityElement.setAttribute(ATTR_NAME, parameters);
                }
                element.addContent(entityElement);
            }
            return element;
        }
    }

    @Override
    public void loadState(final Element state) {
        synchronized (lock) {
            queue.clear();

            for (Element element : state.getChildren(TAG_ENTITY)) {
                final String actionName = element.getAttributeValue(ATTR_ACTION);
                final String dateStr = element.getAttributeValue(ATTR_DATE);
                final String parameters = element.getAttributeValue(ATTR_NAME);

                if (actionName != null && dateStr != null) {
                    try {
                        final ACTIONS action = ACTIONS.valueOf(actionName);
                        queue.add(new Entity(action, parameters, dateStr));
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
            lock.notifyAll();
        }
    }
}
