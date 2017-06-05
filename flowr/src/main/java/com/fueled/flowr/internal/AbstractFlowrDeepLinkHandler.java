package com.fueled.flowr.internal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.fueled.flowr.Flowr;
import com.fueled.flowr.FlowrFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by husseinaladeen@gmail.com on 04/06/2017.
 * Copyright (c) 2017 Hussein Ala. All rights reserved.
 */
public class AbstractFlowrDeepLinkHandler<T extends Fragment & FlowrFragment>
        implements FlowrDeepLinkHandler {

    private static final String NAMED_PARAM_REGEX = "\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>";
    private static final Pattern NAMED_PARAM_PATTERN = Pattern.compile(NAMED_PARAM_REGEX);

    private final Map<String, Class<? extends T>> linkFragmentMap;

    public AbstractFlowrDeepLinkHandler() {
        linkFragmentMap = new HashMap<>();
    }

    /**
     * Add a new link between a specific URL pattern and a specific fragment class.
     *
     * @param url      the URL pattern to link the fragment class to.
     * @param fragment the fragment class to be linked to the URL pattern.
     */
    protected void addFragment(String url, Class<? extends T> fragment) {
        linkFragmentMap.put(url, fragment);
    }

    /**
     * Generates a bundle of the url named path variables and parameters.
     *
     * @param uri     the url to generate the bundle from.
     * @param pattern the pattern to extract named path variables with.
     * @return a bundle containing all path variables and parameters as strings.
     */
    @Nullable
    private static Bundle bundleUriInfo(Uri uri, String pattern) {
        String regex = getRegexPattern(pattern);
        String path = uri.getPath();
        Matcher m = Pattern.compile(regex).matcher(path);

        if (m.matches()) {
            Bundle data = new Bundle();
            data.putString(Flowr.DEEP_LINK_URL, uri.toString());
            Iterator<String> params = getNamedGroupCandidates(regex).iterator();

            //start at 1 because 0 is the searched string
            int i = 1;
            while (params.hasNext()) {
                String bip = params.next();
                data.putString(bip, m.group(i));
                i++;
            }

            return data;
        }

        return null;
    }

    private static String getRegexPattern(String pattern) {
        return pattern.replaceAll("\\{(.+?)\\}", "(?<$1>\\.+?)");
    }

    private static List<String> getNamedGroupCandidates(String regex) {
        List<String> namedGroups = new ArrayList<>();

        Matcher m = NAMED_PARAM_PATTERN.matcher(regex);

        while (m.find()) {
            String test = m.group(1);
            namedGroups.add(test);
        }

        return namedGroups;
    }

    /**
     * @inheritDoc
     */
    @Nullable
    public FlowrDeepLinkInfo getDeepLinkInfoForIntent(@NonNull Intent intent) {
        Uri uri = intent.getData();

        if (uri != null) {
            for (String uriPattern : linkFragmentMap.keySet()) {
                Bundle data = bundleUriInfo(uri, uriPattern);
                if (data != null) {
                    return new FlowrDeepLinkInfo<>(data, linkFragmentMap.get(uriPattern));
                }
            }
        }

        return null;
    }

}
