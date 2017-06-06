package com.fueled.flowr.internal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.fueled.flowr.AbstractFlowrFragment;
import com.fueled.flowr.Flowr;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by hussein@fueled.com on 05/06/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractFlowrDeepLinkHandlerTest {

    @Mock Intent intent;
    @Mock Uri uri;
    @Mock Bundle bundle;

    private AbstractFlowrDeepLinkHandler<AbstractFlowrFragment> deepLinkHandler;

    @Before
    public void setup() {
        deepLinkHandler = spy(new AbstractFlowrDeepLinkHandler<AbstractFlowrFragment>());

        when(intent.getData()).thenReturn(uri);
        when(deepLinkHandler.getNewBundle()).thenReturn(bundle);
    }

    @Test
    public void testMapLinkWithNoArguments() {
        when(uri.getPath()).thenReturn("/hello");
        when(uri.toString()).thenReturn("http://fueled.com/hello");

        deepLinkHandler.addFragment("/hi", DemoFragment.class);
        deepLinkHandler.addFragment("/hello", SampleFragment.class);

        FlowrDeepLinkInfo<AbstractFlowrFragment> info = deepLinkHandler.getDeepLinkInfoForIntent(intent);

        verify(bundle).putString(Flowr.DEEP_LINK_URL, "http://fueled.com/hello");
        verifyNoMoreInteractions(bundle);

        assertNotNull(info);
        assertEquals(SampleFragment.class, info.fragment);
        assertNotNull(info.data);
    }

    @Test
    public void testMapLinkWithPathVariable() {
        when(uri.getPath()).thenReturn("/m/123");
        when(uri.toString()).thenReturn("http://fueled.com/m/123");

        deepLinkHandler.addFragment("/m/{id}", DemoFragment.class);
        deepLinkHandler.addFragment("/hello", SampleFragment.class);

        FlowrDeepLinkInfo<AbstractFlowrFragment> info = deepLinkHandler.getDeepLinkInfoForIntent(intent);

        verify(bundle).putString(Flowr.DEEP_LINK_URL, "http://fueled.com/m/123");
        verify(bundle).putString("id", "123");
        verifyNoMoreInteractions(bundle);

        assertNotNull(info);
        assertEquals(DemoFragment.class, info.fragment);
        assertNotNull(info.data);
    }

    @Test
    public void testMapLinkNoMappingFound() {
        when(uri.getPath()).thenReturn("/m/123");
        when(uri.toString()).thenReturn("http://fueled.com/m/123");

        deepLinkHandler.addFragment("/hi", DemoFragment.class);
        deepLinkHandler.addFragment("/hello", SampleFragment.class);

        FlowrDeepLinkInfo<AbstractFlowrFragment> info = deepLinkHandler.getDeepLinkInfoForIntent(intent);

        verifyZeroInteractions(bundle);

        assertNull(info);
    }

    public static class SampleFragment extends AbstractFlowrFragment {

    }

    public static class DemoFragment extends AbstractFlowrFragment {

    }

}
