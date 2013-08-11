package se.embargo.core.widget;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;

/**
 * For use with ActionBar tabs. 
 * @see			ActionBar.Tab.setTabListener()
 * @param	<T>	Fragment implementation class 
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private final Activity _activity;
    private final String _tag;
    private final Class<T> _class;
    private Fragment _fragment;

    /** 
     * Constructor used each time a new tab is created.
     * @param	activity	The host Activity, used to instantiate the fragment
     * @param	tag  		The identifier tag for the fragment
     * @param	clz  		The fragment's Class, used to instantiate the fragment
     */
    public TabListener(Activity activity, String tag, Class<T> clz) {
        _activity = activity;
        _tag = tag;
        _class = clz;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // Check if the fragment is already initialized
        if (_fragment == null) {
            // If not, instantiate and add it to the activity
            _fragment = Fragment.instantiate(_activity, _class.getName());
            ft.add(android.R.id.content, _fragment, _tag);
        } 
        else {
            // If it exists, simply attach it in order to show it
            ft.attach(_fragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (_fragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(_fragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}
