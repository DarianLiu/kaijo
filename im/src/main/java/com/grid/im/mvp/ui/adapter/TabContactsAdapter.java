package com.grid.im.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by codeest on 16/8/11.
 */

public class TabContactsAdapter extends FragmentPagerAdapter {

    public String tag1;
    public String tag2;
    public String tag3;
    private FragmentManager fm;
    private List<Fragment> fragments;

    public TabContactsAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        if(position==0){
            tag1 = fragment.getTag();
        }else if(position==1){
            tag2 = fragment.getTag();
        }else if(position==2){
            tag3 = fragment.getTag();
        }
        return fragment;
    }
}
