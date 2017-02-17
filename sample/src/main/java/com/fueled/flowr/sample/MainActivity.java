package com.fueled.flowr.sample;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.fueled.flowr.NavigationIconType;
import com.fueled.flowr.Flowr;
import com.fueled.flowr.ToolbarHandler;
import com.fueled.flowr.sample.core.AbstractActivity;
import com.fueled.flowr.sample.core.FragmentResultPublisherImpl;
import com.fueled.flowr.sample.databinding.ActivityMainBinding;


public class MainActivity extends AbstractActivity implements ToolbarHandler {

    private Flowr flowr;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        if (getFlowr().getCurrentFragment() == null) {
            getFlowr().open(HomeFragment.class)
                    .skipBackStack(true)
                    .displayFragment();
        }
    }

    public Flowr getFlowr() {
        if (flowr == null) {
            flowr = new Flowr(R.id.main_container, this, this, null,
                    FragmentResultPublisherImpl.getInstance());
        }

        return flowr;
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
