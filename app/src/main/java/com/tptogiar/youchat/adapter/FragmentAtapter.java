package com.tptogiar.youchat.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class FragmentAtapter extends FragmentStateAdapter {
    ArrayList<Fragment> fragments=new ArrayList<>();

    public FragmentAtapter(@NonNull FragmentActivity fragmentActivity,ArrayList<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments=fragments;
    }

    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }
    @Override
    public int getItemCount() {
        return fragments.size();
    }




}
