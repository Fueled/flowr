package com.fueled.flowr.sample;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.fueled.flowr.DrawerHandler;
import com.fueled.flowr.Flowr;
import com.fueled.flowr.NavigationIconType;
import com.fueled.flowr.ToolbarHandler;
import com.fueled.flowr.sample.core.AbstractActivity;
import com.fueled.flowr.sample.core.AbstractFragment;
import com.fueled.flowr.sample.core.FragmentResultPublisherImpl;
import com.fueled.flowr.sample.databinding.ActivityMainBinding;


public class MainActivity extends AbstractActivity implements ToolbarHandler, DrawerHandler {

    private Flowr flowr;
    private ActivityMainBinding binding;

    private View.OnClickListener toolbarNavigationClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        if (getIntent().getAction().equals(Intent.ACTION_VIEW)) {
            FlowrDeepLinkHandler test = new FlowrDeepLinkHandler();
            test.routeIntentToScreen(getIntent(), getFlowr());
        } else if (getFlowr().getCurrentFragment() == null) {
            getFlowr().open(HomeFragment.class)
                    .skipBackStack(true)
                    .displayFragment();
        }

        setToolbarNavigationClickListener();
        setNavigationItemSelectedListener();
    }

    public Flowr getFlowr() {
        if (flowr == null) {
            flowr = new Flowr(R.id.main_container, this, this, this,
                    FragmentResultPublisherImpl.getInstance());
        }

        return flowr;
    }

    private void setToolbarNavigationClickListener() {
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFlowr() != null && getFlowr().isHomeFragment()) {
                    openDrawer();
                } else if (toolbarNavigationClickListener != null) {
                    toolbarNavigationClickListener.onClick(view);
                }
            }
        });
    }

    private void setNavigationItemSelectedListener() {
        binding.navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu_home:
                        displayNavigationScreen(HomeFragment.class);
                        break;
                    case R.id.navigation_menu_categories:
                        displayNavigationScreen(CategoriesFragment.class);
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    private void displayNavigationScreen(Class<? extends AbstractFragment> screen) {
        closeDrawer();

        if (!screen.equals(getFlowr().getCurrentFragment().getClass())) {
            getFlowr().open(screen)
                    .clearBackStack(true)
                    .skipBackStack(true)
                    .replaceCurrentFragment(true)
                    .displayFragment();
        }
    }

    @Override
    public void setNavigationIcon(NavigationIconType navigationIconType) {
        switch (navigationIconType) {
            case BACK:
                binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                break;
            case HAMBURGER:
                binding.toolbar.setNavigationIcon(R.drawable.ic_menu);
                break;
            default:
                binding.toolbar.setNavigationIcon(null);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(Gravity.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setCustomNavigationIcon(Drawable navigationIcon) {
        binding.toolbar.setNavigationIcon(navigationIcon);
    }

    @Override
    public void setToolbarVisible(boolean visible) {
        binding.toolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setToolbarTitle(String title) {
        binding.toolbar.setTitle(title);
    }

    @Override
    public void setToolbarNavigationButtonListener(View.OnClickListener onClickListener) {
        toolbarNavigationClickListener = onClickListener;
    }

    @Override
    public void openDrawer() {
        binding.drawerLayout.openDrawer(Gravity.START);
    }

    @Override
    public void closeDrawer() {
        binding.drawerLayout.closeDrawer(Gravity.START);
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        binding.drawerLayout.setDrawerLockMode(enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
