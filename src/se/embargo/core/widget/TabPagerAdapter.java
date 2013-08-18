package se.embargo.core.widget;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

/**
 * This is a helper class that implements the management of tabs and all
 * details of connecting a ViewPager with associated TabHost.  It relies on a
 * trick.  Normally a tab host has a simple API for supplying a View or
 * Intent that each tab will show.  This is not sufficient for switching
 * between pages.  So instead we make the content part of the tab host
 * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
 * view to show as the tab content.  It listens to changes in tabs, and takes
 * care of switch to the correct paged in the ViewPager whenever the selected
 * tab changes.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener, ActionBar.TabListener {
    private final Context _activity;
    private final ActionBar _actionbar;
    private final ViewPager _pager;
    private final ArrayList<String> _tabs = new ArrayList<String>();

    public TabPagerAdapter(FragmentActivity activity, ActionBar actionBar, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        _activity = activity;
        _actionbar = actionBar;
        _pager = pager;
        _pager.setAdapter(this);
        _pager.setOnPageChangeListener(this);
    }

    public void addTab(ActionBar.Tab tab, Class<?> clss) {
        _tabs.add(clss.getName());
        _actionbar.addTab(tab.setTabListener(this));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return _tabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment.instantiate(_activity, _tabs.get(position), null);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        _actionbar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		_pager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
}
