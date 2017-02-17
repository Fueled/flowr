package com.fueled.flowr.sample.core;

import com.fueled.flowr.FragmentsResultPublisher;
import com.fueled.flowr.ResultResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by hussein@fueled.com on 13/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public class FragmentResultPublisherImpl implements FragmentsResultPublisher {

    private static FragmentResultPublisherImpl instance;

    private PublishSubject<ResultResponse> publishSubject;

    private FragmentResultPublisherImpl() {
        publishSubject = PublishSubject.create();
    }

    @Override
    public void publishResult(ResultResponse resultResponse) {

    }

    public Disposable observeResultsForFragment(final String fragmentId, Consumer<ResultResponse> consumer) {
        return publishSubject
                .filter(new Predicate<ResultResponse>() {
                    @Override
                    public boolean test(ResultResponse resultResponse) throws Exception {
                        return resultResponse.fragmentId.equals(fragmentId);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public static FragmentResultPublisherImpl getInstance() {
        if (instance == null) {
            instance = new FragmentResultPublisherImpl();
        }

        return instance;
    }

}
