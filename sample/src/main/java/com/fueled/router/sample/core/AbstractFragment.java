package com.fueled.router.sample.core;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fueled.router.AbstractRouterFragment;
import com.fueled.router.ResultResponse;
import com.fueled.router.sample.R;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by hussein@fueled.com on 13/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public abstract class AbstractFragment extends AbstractRouterFragment {

    private Disposable fragmentResultSubscription;

    public abstract @LayoutRes int getLayoutId();

    protected abstract void setupView(View view);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        view.setClickable(true);
        setupView(view);
        return view;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listenToResults();
    }

    protected Router getRouter() {
        if (getActivity() != null && getActivity() instanceof AbstractActivity) {
            return ((AbstractActivity) getActivity()).getRouter();
        }

        return null;
    }

    private void listenToResults() {
        removeFragmentResultSubscription();
        fragmentResultSubscription = FragmentResultPublisherImpl.getInstance()
                .observeResultsForFragment(getFragmentId(), new Consumer<ResultResponse>() {
                    @Override
                    public void accept(ResultResponse resultResponse) throws Exception {
                        onFragmentResults(resultResponse.requestCode, resultResponse.resultCode,
                                resultResponse.data);
                    }
                });
    }

    private void removeFragmentResultSubscription() {
        if (fragmentResultSubscription != null && !fragmentResultSubscription.isDisposed()) {
            fragmentResultSubscription.dispose();
        }
    }

    @Override
    public String getTitle() {
        return getString(R.string.app_name);
    }
}
