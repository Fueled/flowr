package com.fueled.flowr;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.fueled.flowr.internal.TransactionData;


/**
 * Created by hussein@fueled.com on 31/05/2016.
 * Copyright (c) 2016 Fueled. All rights reserved.
 */
public class Flowr implements FragmentManager.OnBackStackChangedListener,
        View.OnClickListener {

    private final static String KEY_REQUEST_BUNDLE = "KEY_REQUEST_BUNDLE";
    private final static String KEY_FRAGMENT_ID = "KEY_FRAGMENT_ID";
    private final static String KEY_REQUEST_CODE = "KEY_REQUEST_CODE";

    private final static String TAG = Flowr.class.getSimpleName();

    private FlowrScreen screen;
    private ToolbarHandler toolbarHandler;
    private DrawerHandler drawerHandler;

    private final FragmentsResultPublisher resultPublisher;
    private final int mainContainerId;

    private Fragment currentFragment;

    private boolean overrideBack;
    private String tagPrefix;

    /**
     * Constructor to use when creating a new router for an activity
     * that has no toolbar.
     *
     * @param mainContainerId the id of the container where the fragments should be displayed
     * @param screen          the fragment's parent screen
     */
    public Flowr(@IdRes int mainContainerId, FlowrScreen screen,
                 FragmentsResultPublisher resultPublisher) {
        this(mainContainerId, screen, null, null, resultPublisher);
    }

    /**
     * Constructor to use when creating a new router for an activity
     * that has no toolbar.
     *
     * @param mainContainerId the id of the container where the fragments should be displayed
     * @param screen          the fragment's parent screen
     * @param tagPrefix       a custom prefix for the tags to be used for fragments that will be added to
     *                        the backstack.
     * @param resultPublisher the result publish to be used to publish results from fragments
     *                        that where opened for results.
     */
    public Flowr(@IdRes int mainContainerId, FlowrScreen screen, String tagPrefix,
                 FragmentsResultPublisher resultPublisher) {
        this(mainContainerId, screen, null, null, tagPrefix, resultPublisher);
    }

    /**
     * Constructor to use when creating a new router for an activity
     * that has toolbar and a drawer.
     *
     * @param mainContainerId the id of the container where the fragments should be displayed
     * @param screen          the fragment's parent screen
     * @param toolbarHandler  the {@link ToolbarHandler} to be used to sync toolbar state
     * @param drawerHandler   the {@link DrawerHandler} to be used to sync drawer state
     * @param resultPublisher the result publish to be used to publish results from fragments
     *                        that where opened for results.
     */
    public Flowr(@IdRes int mainContainerId, FlowrScreen screen, ToolbarHandler toolbarHandler,
                 DrawerHandler drawerHandler, FragmentsResultPublisher resultPublisher) {
        this(mainContainerId, screen, toolbarHandler, drawerHandler, "#id-", resultPublisher);
    }

    /**
     * Constructor to use when creating a new router for an activity
     * that has toolbar and a drawer.
     *
     * @param mainContainerId the id of the container where the fragments should be displayed
     * @param screen          the fragment's parent screen
     * @param toolbarHandler  the {@link ToolbarHandler} to be used to sync toolbar state
     * @param drawerHandler   the {@link DrawerHandler} to be used to sync drawer state
     * @param tagPrefix       a custom prefix for the tags to be used for fragments that will be added to
     *                        the backstack.
     * @param resultPublisher the result publish to be used to publish results from fragments
     *                        that where opened for results.
     */
    public Flowr(@IdRes int mainContainerId, FlowrScreen screen, ToolbarHandler toolbarHandler,
                 DrawerHandler drawerHandler, String tagPrefix,
                 FragmentsResultPublisher resultPublisher) {
        this.resultPublisher = resultPublisher;
        this.mainContainerId = mainContainerId;
        this.tagPrefix = tagPrefix;
        this.overrideBack = false;

        setRouterScreen(screen);
        setToolbarHandler(toolbarHandler);
        setDrawerHandler(drawerHandler);

        syncScreenState();
    }

    /**
     * Returns the {@link FlowrScreen} used for this router.
     *
     * @return the router screen for this router
     */
    protected FlowrScreen getRouterScreen() {
        return screen;
    }

    /**
     * Sets the {@link FlowrScreen} to be used for this router.
     *
     * @param flowrScreen the router screen to be used
     */
    protected void setRouterScreen(FlowrScreen flowrScreen) {
        removeCurrentRouterScreen();
        if (flowrScreen != null) {
            this.screen = flowrScreen;

            if (flowrScreen.getScreenFragmentManager() != null) {
                screen.getScreenFragmentManager().addOnBackStackChangedListener(this);
                currentFragment = retrieveCurrentFragment();
            }
        }
    }

    private void removeCurrentRouterScreen() {
        if (screen != null) {
            screen.getScreenFragmentManager().removeOnBackStackChangedListener(this);
            screen = null;
            currentFragment = null;
        }
    }

    /**
     * Sets the {@link ToolbarHandler} to be used to sync toolbar state.
     *
     * @param toolbarHandler the toolbar handler to be used.
     */
    public void setToolbarHandler(ToolbarHandler toolbarHandler) {
        removeCurrentToolbarHandler();

        if (toolbarHandler != null) {
            this.toolbarHandler = toolbarHandler;
            toolbarHandler.setToolbarNavigationButtonListener(this);
        }
    }

    private void removeCurrentToolbarHandler() {
        if (toolbarHandler != null) {
            toolbarHandler.setToolbarNavigationButtonListener(null);
            toolbarHandler = null;
        }
    }

    /**
     * Sets the {@link DrawerHandler} to be used to sync drawer state.
     *
     * @param drawerHandler the drawer handler to be used.
     */
    public void setDrawerHandler(DrawerHandler drawerHandler) {
        this.drawerHandler = drawerHandler;
    }

    /**
     * Returns the prefix used for the backstack fragments tag
     *
     * @return the prefix used for the backstack fragments tag
     */
    protected final String getTagPrefix() {
        return tagPrefix;
    }

    protected <T extends Fragment & FlowrFragment> void displayFragment(TransactionData<T> data) {
        try {
            if (data.isClearBackStack()) {
                clearBackStack();
            }

            FragmentTransaction transaction = screen.getScreenFragmentManager().beginTransaction();

            currentFragment = retrieveCurrentFragment();

            if (data.isReplaceCurrentFragment() && data.isSkipBackStack() && currentFragment != null) {
                transaction.remove(currentFragment).commit();
            }

            Fragment fragment = data.getFragmentClass().newInstance();
            fragment.setArguments(data.getArgs());

            transaction = screen.getScreenFragmentManager().beginTransaction();

            if (!data.isSkipBackStack()) {
                String id = tagPrefix + screen.getScreenFragmentManager().getBackStackEntryCount();
                transaction.addToBackStack(id);
            }

            setCustomAnimations(transaction, data.getEnterAnim(), data.getExitAnim());

            if (data.isReplaceCurrentFragment() && !data.isSkipBackStack()) {
                transaction.replace(mainContainerId, fragment);
            } else {
                transaction.add(mainContainerId, fragment);
            }

            transaction.commit();

            currentFragment = fragment;

            syncScreenState();
        } catch (Exception e) {
            Log.e(TAG, "Error while displaying fragment.", e);
        }
    }

    private void setCustomAnimations(FragmentTransaction transaction, @AnimRes int enterAnim,
                                     @AnimRes int exitAnim) {
        transaction.setCustomAnimations(
                enterAnim,
                FragmentTransaction.TRANSIT_NONE,
                FragmentTransaction.TRANSIT_NONE,
                exitAnim);
    }

    private Fragment retrieveCurrentFragment() {
        Fragment fragment = null;

        if (screen != null) {
            fragment = screen.getScreenFragmentManager()
                    .findFragmentById(mainContainerId);
        }

        return fragment;
    }

    @Override
    public void onBackStackChanged() {
        currentFragment = retrieveCurrentFragment();

        if (currentFragment instanceof FlowrFragment) {
            ((FlowrFragment) currentFragment).onPoppedBackFromStack();
        }

        syncScreenState();
    }

    /**
     * Closes the current activity if the fragments back stack is empty,
     * otherwise pop the top fragment from the stack.
     */
    public void close() {
        overrideBack = true;
        screen.invokeOnBackPressed();
    }

    /**
     * Closes the current activity if the fragments back stack is empty,
     * otherwise pop the top n fragments from the stack.
     *
     * @param n the number of fragments to remove from the back stack
     */
    public void close(int n) {
        int count = screen.getScreenFragmentManager().getBackStackEntryCount();
        if (count > 1) {
            String id = tagPrefix + (screen.getScreenFragmentManager().getBackStackEntryCount() - n);
            screen.getScreenFragmentManager()
                    .popBackStackImmediate(id, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            close();
        }
    }

    /**
     * Closes the current activity if the fragments back stack is empty,
     * otherwise pop the top fragment from the stack and publish the results response.
     *
     * @param resultResponse the results response to be published
     */
    public void closeWithResults(ResultResponse resultResponse) {
        closeWithResults(resultResponse, 1);
    }

    /**
     * Closes the current activity if the fragments back stack is empty,
     * otherwise pop the top n fragments from the stack and publish the results response.
     *
     * @param resultResponse the results response to be published
     * @param n              the number of fragments to remove from the back stack
     */
    public void closeWithResults(ResultResponse resultResponse, int n) {
        close(n);

        if (resultResponse != null) {
            resultPublisher.publishResult(resultResponse);
        }
    }

    /**
     * Clears the fragments back stack.
     */
    public void clearBackStack() {
        screen.getScreenFragmentManager()
                .popBackStackImmediate(tagPrefix + "0", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        currentFragment = null;
    }

    /**
     * Notify the current fragment of the back press event
     * and see if the fragment will handle it.
     *
     * @return true if the event was handled by the fragment
     */
    public boolean onBackPressed() {
        if (!overrideBack && currentFragment instanceof FlowrFragment &&
                ((FlowrFragment) currentFragment).onBackPressed()) {
            return true;
        }

        overrideBack = false;
        return false;
    }

    public void onNavigationIconClicked() {
        if (!(currentFragment instanceof FlowrFragment &&
                ((FlowrFragment) currentFragment).onNavigationIconClick())) {
            close();
        }
    }

    /**
     * Checks if the current fragment is the home fragment.
     *
     * @return true if the current fragment is the home fragment
     */
    public boolean isHomeFragment() {
        return screen.getScreenFragmentManager().getBackStackEntryCount() == 0;
    }

    /**
     * Returns the fragment currently being displayed for this screen,
     *
     * @return the fragment currently being displayed
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    /**
     * Called by the {@link android.app.Activity#onPostCreate(Bundle)} to update
     * the state of the container screen.
     */
    public void syncScreenState() {
        int screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
        int navigationBarColor = -1;

        if (currentFragment instanceof FlowrFragment) {
            screenOrientation = ((FlowrFragment) currentFragment).getScreenOrientation();
            navigationBarColor = ((FlowrFragment) currentFragment).getNavigationBarColor();
        }

        screen.setScreenOrientation(screenOrientation);
        screen.setNavigationBarColor(navigationBarColor);

        syncToolbarState();
        syncDrawerState();
    }

    private void syncToolbarState() {
        if (toolbarHandler == null) {
            return;
        }

        NavigationIconType iconType = NavigationIconType.HIDDEN;
        String title = null;

        if (currentFragment instanceof FlowrFragment) {
            iconType = ((FlowrFragment) currentFragment).getNavigationIconType();
            title = ((FlowrFragment) currentFragment).getTitle();
        }

        if (iconType == NavigationIconType.CUSTOM) {
            toolbarHandler.setCustomNavigationIcon(((FlowrFragment) currentFragment).getNavigationIcon());
        } else {
            toolbarHandler.setNavigationIcon(iconType);
        }

        toolbarHandler.setToolbarVisible(!(currentFragment instanceof FlowrFragment) ||
                        ((FlowrFragment) currentFragment).isToolbarVisible());

        toolbarHandler.setToolbarTitle(title != null ? title : "");
    }

    private void syncDrawerState() {
        if (drawerHandler == null) {
            return;
        }

        drawerHandler.setDrawerEnabled(!(currentFragment instanceof FlowrFragment) ||
                ((FlowrFragment) currentFragment).isDrawerEnabled());
    }

    /**
     * Creates a new {@link Builder} instance to be used to display a fragment
     *
     * @param fragmentClass the class for the fragment to be displayed
     * @return a new {@link Builder} instance
     */
    public <T extends Fragment & FlowrFragment> Builder open(Class<? extends T> fragmentClass) {
        return new Builder<>(fragmentClass);
    }

    /**
     * Build and return a new ResultResponse instant using the arguments passed in.
     *
     * @param arguments  Used to retrieve the ID and request code for the fragment
     *                   requesting the results.
     * @param resultCode The results code to be returned.
     * @param data       Used to return extra data that might be required.
     * @return a new {@link ResultResponse} instance
     */
    public static ResultResponse getResultsResponse(Bundle arguments, int resultCode, Bundle data) {
        if (arguments == null || !arguments.containsKey(KEY_REQUEST_BUNDLE)) {
            return null;
        }

        ResultResponse resultResponse = new ResultResponse();
        resultResponse.resultCode = resultCode;
        resultResponse.data = data;

        Bundle requestBundle = arguments.getBundle(KEY_REQUEST_BUNDLE);

        resultResponse.fragmentId = requestBundle.getString(KEY_FRAGMENT_ID);
        resultResponse.requestCode = requestBundle.getInt(KEY_REQUEST_CODE);

        return resultResponse;
    }

    /**
     * The default enter animation to be used for fragment transactions
     *
     * @return the default fragment enter animation
     */
    protected int getDefaultEnterAnimation() {
        return FragmentTransaction.TRANSIT_NONE;
    }

    /**
     * The default exit animation to be used for fragment transactions
     *
     * @return the default fragment exit animation
     */
    protected int getDefaultExitAnimation() {
        return FragmentTransaction.TRANSIT_NONE;
    }

    @Override
    public void onClick(View view) {
        onNavigationIconClicked();
    }

    public void onDestroy() {
        setRouterScreen(null);
        setToolbarHandler(null);
        setDrawerHandler(null);
    }

    /**
     * This builder class is used to show a new fragment inside the current activity
     */
    public class Builder<T extends Fragment & FlowrFragment> {

        private TransactionData<T> data;

        public Builder(Class<? extends T> fragmentClass) {
            data = new TransactionData<>(fragmentClass, getDefaultEnterAnimation(),
                    getDefaultExitAnimation());
        }

        /**
         * Sets the construction arguments for fragment to be displayed.
         *
         * @param args the construction arguments for this fragment.
         */
        public Builder setData(Bundle args) {
            data.setArgs(args);
            return this;
        }

        /**
         * Specifies if this fragment should not be added to the back stack.
         */
        public Builder skipBackStack(boolean skipBackStack) {
            data.setSkipBackStack(skipBackStack);
            return this;
        }

        /**
         * Specifies if the fragment manager back stack should be cleared.
         */
        public Builder clearBackStack(boolean clearBackStack) {
            data.setClearBackStack(clearBackStack);
            return this;
        }

        /**
         * Specifies if this fragment should replace the current fragment.
         */
        public Builder replaceCurrentFragment(boolean replaceCurrentFragment) {
            data.setReplaceCurrentFragment(replaceCurrentFragment);
            return this;
        }

        /**
         * Specifies the animations to be used for this transaction.
         *
         * @param enterAnim the fragment enter animation.
         * @param exitAnim  the fragment exit animation.
         */
        public Builder setCustomTransactionAnimation(@AnimRes int enterAnim, @AnimRes int exitAnim) {
            data.setEnterAnim(enterAnim);
            data.setExitAnim(exitAnim);
            return this;
        }

        /**
         * Don't use any animations for this transaction
         */
        public Builder noTransactionAnimation() {
            return setCustomTransactionAnimation(FragmentTransaction.TRANSIT_NONE,
                    FragmentTransaction.TRANSIT_NONE);
        }

        /**
         * Displays the fragment using this builder configurations.
         */
        public void displayFragment() {
            Flowr.this.displayFragment(data);
        }

        /**
         * Displays the fragment for results using this builder configurations.
         *
         * @param fragmentId  a unique ID that the fragment requesting the results can be identified by,
         *                    it will be later used to deliver the results to the correct fragment instance.
         * @param requestCode this code will be returned in {@link ResultResponse} when the fragment is closed,
         *                    and it can be used to identify the request from which the results were returned.
         */
        public void displayFragmentForResults(String fragmentId, int requestCode) {
            if (!TextUtils.isEmpty(fragmentId)) {
                if (data.getArgs() == null) {
                    data.setArgs(new Bundle());
                }

                data.getArgs().putBundle(KEY_REQUEST_BUNDLE,
                        getResultRequestBundle(fragmentId, requestCode));
            }

            Flowr.this.displayFragment(data);
        }

        private Bundle getResultRequestBundle(String fragmentId, int requestCode) {
            Bundle request = new Bundle();
            request.putString(KEY_FRAGMENT_ID, fragmentId);
            request.putInt(KEY_REQUEST_CODE, requestCode);
            return request;
        }
    }

}