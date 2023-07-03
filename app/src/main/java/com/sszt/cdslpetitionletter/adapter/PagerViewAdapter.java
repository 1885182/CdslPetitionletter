package com.sszt.cdslpetitionletter.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

/**
 * @author:zcr
 * @date:2021/3/20
 * @description:地址选择viewPager适配器
 */
public class PagerViewAdapter<T extends View> extends PagerAdapter {

    private final List<T> mTList;

    public PagerViewAdapter(List<T> tList) {
        this.mTList = tList;
    }

    @Override
    public int getCount() {
        return mTList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        T t = mTList.get(position);
        container.addView(t);
        return t;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mTList.get(position));
    }
}
