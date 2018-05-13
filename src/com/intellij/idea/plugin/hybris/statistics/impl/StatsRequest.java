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

import com.intellij.idea.plugin.hybris.common.HybrisConstants;
import com.intellij.openapi.util.Ref;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.net.IdeHttpClientHelpers;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.Callable;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by Martin Zdarsky-Jones (martin.zdarsky@hybris.com) on 28/2/17.
 */
public class StatsRequest implements Callable<StatsResponse> {

    private static final int TIMEOUT = 60000;

    private static final Object computerNameLock = new Object();
    @Nullable
    private static Ref<String> cachedComputerNameRef = null;

    @NotNull
    private final List<NameValuePair> urlParameters;

    public StatsRequest(@NotNull final List<NameValuePair> urlParameters) {
        this.urlParameters = ContainerUtil.newArrayList(urlParameters);
    }

    @Override
    public StatsResponse call() throws Exception {
        final RequestConfig.Builder builder = RequestConfig
            .custom()
            .setConnectTimeout(TIMEOUT)
            .setConnectionRequestTimeout(TIMEOUT)
            .setSocketTimeout(TIMEOUT);
        IdeHttpClientHelpers.ApacheHttpClient4.setProxyForUrlIfEnabled(builder, HybrisConstants.STATS_COLLECTOR_URL);
        final RequestConfig config = builder.build();
        final HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

        final List<NameValuePair> patchedUrlParameters = ContainerUtil.newArrayList(urlParameters);
        patchUrlParameters(patchedUrlParameters);
        final HttpPost post = new HttpPost(HybrisConstants.STATS_COLLECTOR_URL);
        post.setEntity(new UrlEncodedFormEntity(patchedUrlParameters, UTF_8));
        return new StatsResponse(client.execute(post));
    }

    protected void patchUrlParameters(@NotNull final List<NameValuePair> urlParameters) {
        final String computerName = getComputerName();

        if (computerName != null) {
            urlParameters.add(new BasicNameValuePair("computer_name", DigestUtils.sha512Hex(computerName)));
        }
    }

    //the same user can have multiple computers with identical license. This would affect the user counter.
    protected static String getComputerName() {
        synchronized (computerNameLock) {
            if (cachedComputerNameRef == null) {
                cachedComputerNameRef = Ref.create(computeComputerName());
            }
            return cachedComputerNameRef.get();
        }
    }

    @Nullable
    private static String computeComputerName() {
        try {
            final String result = InetAddress.getLocalHost().getHostName();
            if (StringUtils.isNotEmpty(result)) {
                return result.trim();
            }
        } catch (UnknownHostException e) {
            // failed;  try alternate means.
        }

        // try environment properties.
        final String envCompName = System.getenv("COMPUTERNAME");
        if (envCompName != null) {
            return envCompName;
        }
        final String host = System.getenv("HOSTNAME");
        if (host != null) {
            return host;
        }

        return null;
    }
}
