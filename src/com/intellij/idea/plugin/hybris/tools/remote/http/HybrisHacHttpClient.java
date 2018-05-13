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

package com.intellij.idea.plugin.hybris.tools.remote.http;

import com.google.gson.Gson;
import com.intellij.idea.plugin.hybris.tools.remote.http.flexibleSearch.TableBuilder;
import com.intellij.idea.plugin.hybris.tools.remote.http.impex.HybrisHttpResult;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE;
import static org.jsoup.Jsoup.parse;

public class HybrisHacHttpClient extends AbstractHybrisHacHttpClient {

    private static final Logger LOG = Logger.getInstance(HybrisHacHttpClient.class);

    public static HybrisHacHttpClient getInstance(@NotNull final Project project) {
        return project.getComponent(HybrisHacHttpClient.class);
    }

    public @NotNull
    HybrisHttpResult validateImpex(final Project project, final String content) {
        final HttpResponse response = getHttpResponse(project, content, "/console/impex/import/validate");
        HybrisHttpResult.HybrisHttpResultBuilder resultBuilder = HybrisHttpResult.HybrisHttpResultBuilder.createResult();
        resultBuilder = resultBuilder.httpCode(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == SC_SERVICE_UNAVAILABLE) {
            return resultBuilder.errorMessage(response.getStatusLine().getReasonPhrase()).build();
        }
        final Document document;
        try {
            document = Jsoup.parse(response.getEntity().getContent(), StandardCharsets.UTF_8.name(), "");
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
            return resultBuilder.errorMessage(e.getMessage()).build();
        }
        final Element impexResultStatus = document.getElementById("validationResultMsg");
        if (impexResultStatus == null) {
            return resultBuilder.errorMessage("No data in response").build();
        }
        final boolean hasDataLevelAttr = impexResultStatus.hasAttr("data-level");
        final boolean hasDataResultAttr = impexResultStatus.hasAttr("data-result");
        if (hasDataLevelAttr && hasDataResultAttr) {
            if ("error".equals(impexResultStatus.attr("data-level"))) {
                final String dataResult = impexResultStatus.attr("data-result");
                return resultBuilder.errorMessage(dataResult).build();
            } else {
                final String dataResult = impexResultStatus.attr("data-result");
                return resultBuilder.output(dataResult).build();
            }
        }
        return resultBuilder.errorMessage("No data in response").build();
    }

    public @NotNull
    HybrisHttpResult importImpex(final Project project, final String content) {
        final HttpResponse response = getHttpResponse(project, content, "/console/impex/import");
        HybrisHttpResult.HybrisHttpResultBuilder resultBuilder = HybrisHttpResult.HybrisHttpResultBuilder.createResult();
        resultBuilder = resultBuilder.httpCode(response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == SC_SERVICE_UNAVAILABLE) {
            return resultBuilder.errorMessage(response.getStatusLine().getReasonPhrase()).build();
        }
        final Document document;
        try {
            document = Jsoup.parse(response.getEntity().getContent(), StandardCharsets.UTF_8.name(), "");
        } catch (IOException e) {
            LOG.warn(e.getMessage(), e);
            return resultBuilder.errorMessage(e.getMessage()).build();
        }
        final Element impexResultStatus = document.getElementById("impexResult");
        if (impexResultStatus == null) {
            return resultBuilder.errorMessage("No data in response").build();
        }
        final boolean hasDataLevelAttr = impexResultStatus.hasAttr("data-level");
        final boolean hasDataResultAttr = impexResultStatus.hasAttr("data-result");
        if (hasDataLevelAttr && hasDataResultAttr) {
            if ("error".equals(impexResultStatus.attr("data-level"))) {
                final String dataResult = impexResultStatus.attr("data-result");
                final Element detailMessage = document.getElementsByClass("impexResult").first().children().first();
                return HybrisHttpResult.HybrisHttpResultBuilder.createResult()
                                                               .errorMessage(dataResult)
                                                               .detailMessage(detailMessage.text())
                                                               .build();
            } else {
                final String dataResult = impexResultStatus.attr("data-result");
                return HybrisHttpResult.HybrisHttpResultBuilder.createResult().output(dataResult).build();
            }
        }
        return resultBuilder.errorMessage("No data in response").build();
    }

    public @NotNull
    HybrisHttpResult executeFlexibleSearch(final Project project, final String content) {

        final List<BasicNameValuePair> params = asList(
            new BasicNameValuePair("scriptType", "flexibleSearch"),
            new BasicNameValuePair("commit", "false"),
            new BasicNameValuePair("flexibleSearchQuery", content),
            new BasicNameValuePair("sqlQuery", ""),
            new BasicNameValuePair("maxCount", "100")
        );
        HybrisHttpResult.HybrisHttpResultBuilder resultBuilder = HybrisHttpResult.HybrisHttpResultBuilder.createResult();
        final String actionUrl = getHostHacURL(project) + "/console/flexsearch/execute";

        final HttpResponse response = post(project, actionUrl, params, true);
        resultBuilder = resultBuilder.httpCode(response.getStatusLine().getStatusCode());
        final Document document;
        try {
            document = parse(response.getEntity().getContent(), StandardCharsets.UTF_8.name(), "");
        } catch (final IOException e) {
            return resultBuilder.errorMessage(e.getMessage() + ' ' + actionUrl).httpCode(SC_BAD_REQUEST).build();
        }
        final Elements fsResultStatus = document.getElementsByTag("body");
        if (fsResultStatus == null) {
            return resultBuilder.errorMessage("No data in response").build();
        }
        final HashMap json = new Gson().fromJson(fsResultStatus.text(), HashMap.class);
        if (json.get("exception") != null) {
            return HybrisHttpResult.HybrisHttpResultBuilder.createResult()
                                                           .errorMessage(json.get("exception").toString())
                                                           .detailMessage(json.get("exception").toString()).build();
        } else {
            TableBuilder tableBuilder = new TableBuilder();

            final List<String> headers = (List<String>) json.get("headers");
            final List<List<String>> resultList = (List<List<String>>) json.get("resultList");

            tableBuilder.addRow(headers.toArray(new String[]{}));
            resultList.forEach(row -> tableBuilder.addRow(row.toArray(new String[]{})));

            return resultBuilder.output(tableBuilder.toString()).build();
        }
    }

    public @NotNull
    HybrisHttpResult executeGroovyScript(final Project project, final String content) {

        final List<BasicNameValuePair> params = asList(
            new BasicNameValuePair("scriptType", "groovy"),
            new BasicNameValuePair("commit", "false"),
            new BasicNameValuePair("script", content)
        );
        HybrisHttpResult.HybrisHttpResultBuilder resultBuilder = HybrisHttpResult.HybrisHttpResultBuilder.createResult();
        final String actionUrl = getHostHacURL(project) + "/console/scripting/execute";

        final HttpResponse response = post(project, actionUrl, params, true);
        resultBuilder = resultBuilder.httpCode(response.getStatusLine().getStatusCode());
        final Document document;
        try {
            document = parse(response.getEntity().getContent(), StandardCharsets.UTF_8.name(), "");
        } catch (final IOException e) {
            return resultBuilder.errorMessage(e.getMessage() + ' ' + actionUrl).httpCode(SC_BAD_REQUEST).build();
        }
        final Elements fsResultStatus = document.getElementsByTag("body");
        if (fsResultStatus == null) {
            return resultBuilder.errorMessage("No data in response").build();
        }
        final HashMap json = new Gson().fromJson(fsResultStatus.text(), HashMap.class);
        if (json.get("stacktraceText") != null && isNotEmpty(json.get("stacktraceText").toString())) {
            return HybrisHttpResult.HybrisHttpResultBuilder.createResult()
                                                           .errorMessage(json.get("stacktraceText").toString())
                                                           .detailMessage(json.get("stacktraceText").toString())
                                                           .build();
        } else {
            return resultBuilder.output(json.get("executionResult").toString()).build();
        }
    }

    private HttpResponse getHttpResponse(final Project project, final String content, final String urlSuffix) {
        final List<BasicNameValuePair> params = getParamList(content);
        final String actionUrl = getHostHacURL(project) + urlSuffix;
        return post(project, actionUrl, params, false);
    }

    private List<BasicNameValuePair> getParamList(String content) {
        return asList(
            new BasicNameValuePair("scriptContent", content),
            new BasicNameValuePair("validationEnum", "IMPORT_STRICT"),
            new BasicNameValuePair("encoding", "UTF-8"),
            new BasicNameValuePair("maxThreads", "4")
        );
    }
}
