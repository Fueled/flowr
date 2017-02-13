package com.fueled.router.sample;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.fueled.router.NavigationIconType;
import com.fueled.router.ToolbarHandler;
import com.fueled.router.sample.core.AbstractActivity;
import com.fueled.router.sample.core.FragmentResultPublisherImpl;
import com.fueled.router.sample.core.Router;
import com.fueled.router.sample.databinding.ActivityMainBinding;


public class MainActivity extends AbstractActivity implements ToolbarHandler {

    private Router router;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        if (getRouter().getCurrentFragment() == null) {
            getRouter().open(HomeFragment.class)
                    .skipBackStack(true)
                    .displayFragment();
        }
    }

    @Override
    public Router getRouter() {
        if (router == null) {
            router = new Router(R.id.main_container,
                    this,
                    this,
                    FragmentResultPublisherImpl.getInstance());
        }

        return router;
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
        binding.toolbar.setNavigationOnClickListener(onClickListener);
    }
}
