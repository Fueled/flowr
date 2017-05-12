package com.fueled.flowr.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.fueled.flowr.Flowr;
import com.fueled.flowr.FlowrDeepLinkHandler;
import com.fueled.flowr.FlowrDeepLinkInfo;
import com.fueled.flowr.FlowrFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generated class that will route the URL defined by a {@link com.fueled.flowr.annotations.DeepLink} annotation to the correct Fragment.
 * Created by julienFueled on 5/10/17.
 */

public class FlowrDeepLinkHandlerImpl<T extends Fragment & FlowrFragment> implements FlowrDeepLinkHandler {
    public static final String url1 = "/test";
    public static final String url2 = "/test2";
    public static final String url3 = "/test?message={message}";
    public static final String url4 = "/m/{message}";
    public static final String url5 = "/test3/{message}/{action}";
    public static final String url6 = "/";


    public Map<String, Class<? extends T>> linkFragmentMap;

    @SuppressWarnings("unchecked")
    public FlowrDeepLinkHandlerImpl() {
        addFragment(url1, (Class<? extends T>) HomeFragment.class);
        addFragment(url2, (Class<? extends T>) HomeFragment.class);
        addFragment(url3, (Class<? extends T>) HomeFragment.class);
        addFragment(url4, (Class<? extends T>) ViewFragment.class);
        addFragment(url5, (Class<? extends T>) ViewFragment.class);
        addFragment(url6, (Class<? extends T>) ViewFragment.class);
    }

    /**
     * Determine if a Uri match a pattern register through a {@link com.fueled.flowr.annotations.DeepLink} annotation and extract.
     *
     * @param uri     The URI that triggered the deep link.
     * @param pattern The pattern the run against the URI.
     * @return If the URI match a pattern, return a Bundle with the parameters otherwise return null.
     */
    private static Bundle bundleUriInfo(@NonNull Uri uri, @NonNull String pattern) {
        String regex = getRegexPattern(pattern);
        String path = uri.getPath();
        Log.w("TEST", path);
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
                Log.w("PARAM", bip + " : " + m.group(i));
                i++;
            }
            return data;
        }
        return null;
    }

    private static List<String> getNamedGroupCandidates(String regex) {
        List<String> namedGroups = new ArrayList<>();

        Matcher m = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>").matcher(regex);

        while (m.find()) {
            String test = m.group(1);
            namedGroups.add(test);
        }

        return namedGroups;
    }


    /**
     * Convert the {variable name} to a valid regex named group.
     *
     * @param pattern the pattern to parse.
     * @return a valid Regex pattern.
     */
    @NonNull
    private static String getRegexPattern(@NonNull String pattern) {
        return pattern.replaceAll("\\{(.+?)\\}", "(?<$1>\\.+?)");
    }

    /**
     * Cheat to be able to safely add Fragment to the Map.
     *
     * @param url      the URL to use as a key.
     * @param fragment the Fragment class to open when the url is used.
     */
    private void addFragment(String url, Class<? extends T> fragment) {
        if (linkFragmentMap == null) {
            linkFragmentMap = new HashMap<>();
        }
        linkFragmentMap.put(url, fragment);
    }

    /**
     * Parse an Intent containing a Deep Link and route it to the right fragment if it is valid
     *
     * @param intent The intent to parse for deep linking.
     * @return The deep link info.
     */
    @SuppressWarnings("unchecked")
    public FlowrDeepLinkInfo routeIntentToScreen(@NonNull Intent intent) {
        Uri uri = intent.getData();
        if (uri != null) {
            for (String uriPattern : linkFragmentMap.keySet()) {
                Bundle data = bundleUriInfo(uri, uriPattern);
                if (data != null) {
                    return new FlowrDeepLinkInfo(data, linkFragmentMap.get(uriPattern));
                }
            }
        }
        return null;
    }
}
