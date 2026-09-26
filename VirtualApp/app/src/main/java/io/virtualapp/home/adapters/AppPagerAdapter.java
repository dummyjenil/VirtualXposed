package io.virtualapp.home.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.virtualapp.R;
import io.virtualapp.XApp;
import io.virtualapp.home.InstalledAppFragment;
import io.virtualapp.home.ListAppFragment;

/**
 * @author Lody
 */
public class AppPagerAdapter extends FragmentPagerAdapter {
    private List<String> titles = new ArrayList<>();

    public AppPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        titles.add("Installed");
        titles.add(XApp.getApp().getResources().getString(R.string.clone_apps));
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return InstalledAppFragment.newInstance();
        } else {
            return ListAppFragment.newInstance(null);
        }
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
