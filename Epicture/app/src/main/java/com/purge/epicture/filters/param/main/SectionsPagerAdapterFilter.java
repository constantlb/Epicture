package com.purge.epicture.filters.param.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.purge.epicture.Frag1_posts;
import com.purge.epicture.Frag2_favorites;
import com.purge.epicture.Frag_Hot;
import com.purge.epicture.Frag_Random;
import com.purge.epicture.Frag_Top;
import com.purge.epicture.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterFilter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapterFilter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Frag_Random();
                break;
            case 1:
                fragment = new Frag_Top();
                break;
            case 2:
                fragment = new Frag_Hot();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Random";
            case 1:
                return "Top";
            case 2:
                return "Hot";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}