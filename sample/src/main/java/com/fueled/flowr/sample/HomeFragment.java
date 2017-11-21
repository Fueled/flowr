package com.fueled.flowr.sample;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.fueled.flowr.NavigationIconType;
import com.fueled.flowr.internal.TransitionConfig;
import com.fueled.flowr.sample.core.AbstractFragment;
import com.fueled.flowr.sample.databinding.FragmentHomeBinding;

/**
 * Created by hussein@fueled.com on 13/02/2017.
 * Copyright (c) 2017 Fueled. All rights reserved.
 */
public class HomeFragment extends AbstractFragment implements View.OnClickListener {

    public static final int RC_STACK = 101;
    public static int backStackIdentifier;
    public static String targetFragmentId;

    private FragmentHomeBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setupView(View view) {
        binding = DataBindingUtil.bind(view);
        binding.homeOpenViewButton.setOnClickListener(this);
        binding.homeOpenLinkButton.setOnClickListener(this);
        binding.homeOpenFirstButton.setOnClickListener(this);
        binding.homeOpenTransitionButton.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationIconClick() {
        return true;
    }

    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.HAMBURGER;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_open_view_button:
                displayViewFragment();
                break;
            case R.id.home_open_link_button:
                displayLinkFragment();
                break;
            case R.id.home_open_transition_button:
                displayTransitionFragment();
                break;
            case R.id.home_open_first_button:
                displayFirstFragment();
                break;
            default:
                break;
        }
    }

    private void displayViewFragment() {
        getFlowr().open("/m/From%20HomeFragment")
                .setCustomTransactionAnimation(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .displayFragment();
    }

    private void displayLinkFragment() {
        getFlowr().open("/hello")
                .displayFragment();
    }

    private void displayFirstFragment() {
        targetFragmentId = getFragmentId();
        backStackIdentifier = getFlowr().open("/first")
                .displayFragmentForResults(targetFragmentId, RC_STACK);
    }

    @Override
    protected void onFragmentResults(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResults(requestCode, resultCode, data);
        if (requestCode == RC_STACK && data.containsKey(SecondFragment.RESULT_FROM_SECOND)) {
            String resultFromStack = data.getString(SecondFragment.RESULT_FROM_SECOND);
            Toast.makeText(getContext(), resultFromStack, Toast.LENGTH_SHORT).show();
        }
    }

    private void displayTransitionFragment() {
        getFlowr().open("/transition")
                .replaceCurrentFragment(true)
                .setTransition(getTransitionConfig(), binding.flowrTextView)
                .displayFragment();
    }

    @NonNull
    private TransitionConfig getTransitionConfig() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new TransitionConfig.Builder()
                    .sharedElementEnter(TransitionConfig.Provider.changeBounds)
                    .sharedElementExit(TransitionConfig.Provider.changeBounds)
                    .enter(TransitionConfig.Provider.fade)
                    .exit(TransitionConfig.Provider.fade)
                    .build();
        }
        return null;
    }
}
