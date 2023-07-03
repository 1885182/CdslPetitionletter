package com.sszt.cdslpetitionletter.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.sszt.basis.base.activity.BaseActivity;
import com.sszt.basis.base.viewmodel.PublicViewModel;
import com.sszt.cdslpetitionletter.R;
import com.sszt.cdslpetitionletter.adapter.AddressListAdapter;
import com.sszt.cdslpetitionletter.adapter.PagerViewAdapter;
import com.sszt.cdslpetitionletter.bean.CityBean;
import com.sszt.cdslpetitionletter.databinding.ActivityAddressCheckBinding;
import com.sszt.cdslpetitionletter.network.RequestCityListTask;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址选择器
 * */
public class AddressCheckActivity extends BaseActivity<PublicViewModel,ActivityAddressCheckBinding> {

    private static final String KEY_OUTPUT_PROVINCE_CITY_DISTRICT = "KEY_OUTPUT_PROVINCE_CITY_DISTRICT";

    //省
    AddressListAdapter mOneListAdapter;
    List<CityBean> mOneList;
    int mCurrentOneSelect = -1;

    //市
    AddressListAdapter mTwoListAdapter;
    List<CityBean> mTwoList;
    int mCurrentTwoSelect = -1;

    //区
    AddressListAdapter mThreeListAdapter;
    List<CityBean> mThreeList;
    int mCurrentThreeSelect = -1;

    //街镇
    AddressListAdapter mFourListAdapter;
    List<CityBean> mFourList;

    public static ArrayList<CityBean> parse(Intent data) {
        return data.getParcelableArrayListExtra(KEY_OUTPUT_PROVINCE_CITY_DISTRICT);
    }




    /**
     * 请求服务器数据回来。(我这里是从asset中的json中读取的，模拟从服务器请求。)
     */
    private RequestCityListTask.Callback callback = new RequestCityListTask.Callback() {
        @Override
        public void callback(List<CityBean> cities) {
            mOneList = cities;
            mOneListAdapter.notifyDataSetChanged(mOneList);
        }
    };

    /**
     * 省的item被点击。
     */
    private AddressListAdapter.OnCompatItemClickListener mProvinceItemClickListener = new AddressListAdapter.OnCompatItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mCurrentOneSelect == position) {
                bind.viewPagerAddress.setCurrentItem(1, true);
                return;
            }
            if (mCurrentOneSelect != -1) {
                mOneList.get(mCurrentOneSelect).setSelect(false);
                mOneListAdapter.notifyItemChanged(mCurrentOneSelect);
            }

            mCurrentOneSelect = position;
            mOneList.get(mCurrentOneSelect).setSelect(true);
            mOneListAdapter.notifyItemChanged(mCurrentOneSelect);

            CityBean one = mOneList.get(mCurrentOneSelect);
            mTwoList = one.getCityList();
            if (mTwoList == null || mTwoList.size() == 0) { // 选定一级。
                setResultFinish(one, null, null,null);
            } else {
                // 更新二级的content和title。
                mTwoListAdapter.notifyDataSetChanged(mTwoList);
                bind.tabLayoutAddress.getTabAt(1).setText(one.getName());
                bind.viewPagerAddress.setCurrentItem(1, true);

                // 三级置空。
                bind.tabLayoutAddress.getTabAt(2).setText(null);
                mThreeList = null;
                mCurrentTwoSelect = -1;
                mCurrentThreeSelect = -1;

                // 四级置空。
                bind.tabLayoutAddress.getTabAt(3).setText(null);
                mFourList = null;
            }
        }
    };

    /**
     * 市的item被点击。
     */
    private AddressListAdapter.OnCompatItemClickListener mCityItemClickListener = new AddressListAdapter.OnCompatItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            if (mCurrentTwoSelect == position) {
                bind.viewPagerAddress.setCurrentItem(2, true);
                return;
            }

            if (mCurrentTwoSelect != -1) {
                mTwoList.get(mCurrentTwoSelect).setSelect(false);
                mTwoListAdapter.notifyItemChanged(mCurrentTwoSelect);
            }

            mCurrentTwoSelect = position;
            mTwoList.get(mCurrentTwoSelect).setSelect(true);
            mTwoListAdapter.notifyItemChanged(mCurrentTwoSelect);

            CityBean two = mTwoList.get(mCurrentTwoSelect);
            mThreeList = two.getCityList();
            if (mThreeList == null || mThreeList.size() == 0) { // 选定二级。
                setResultFinish(mOneList.get(mCurrentOneSelect), two, null,null);
            } else {
                mThreeListAdapter.notifyDataSetChanged(mThreeList);
                bind.tabLayoutAddress.getTabAt(2).setText(two.getName());
                bind.viewPagerAddress.setCurrentItem(2, true);


                // 四级置空。
                bind.tabLayoutAddress.getTabAt(3).setText(null);
                mFourList = null;
                mCurrentThreeSelect = -1;
            }
        }
    };

    /**
     * 区的item被点击。
     */
    private final AddressListAdapter.OnCompatItemClickListener mDistrictItemClickListener = new AddressListAdapter.OnCompatItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mCurrentThreeSelect == position) {
                bind.viewPagerAddress.setCurrentItem(2, true);
                return;
            }

            if (mCurrentThreeSelect != -1) {
                mThreeList.get(mCurrentThreeSelect).setSelect(false);
                mThreeListAdapter.notifyItemChanged(mCurrentThreeSelect);
            }

            mCurrentThreeSelect = position;
            mThreeList.get(mCurrentThreeSelect).setSelect(true);
            mThreeListAdapter.notifyItemChanged(mCurrentThreeSelect);

            CityBean three = mThreeList.get(mCurrentThreeSelect);
            mFourList = three.getCityList();
            if (mFourList == null || mFourList.size() == 0) { // 选定二级。
                setResultFinish(mOneList.get(mCurrentOneSelect), mTwoList.get(mCurrentTwoSelect), three,null);
            } else {
                mFourListAdapter.notifyDataSetChanged(mFourList);
                bind.tabLayoutAddress.getTabAt(3).setText(three.getName());
                bind.viewPagerAddress.setCurrentItem(3, true);
            }
        }
    };

    /**
     * 四级的item被点击。
     */
    private final AddressListAdapter.OnCompatItemClickListener mFourItemClickListener = new AddressListAdapter.OnCompatItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            setResultFinish(mOneList.get(mCurrentOneSelect), mTwoList.get(mCurrentTwoSelect), mThreeList.get(mCurrentThreeSelect),mFourList.get(position));
        }
    };

    /**
     * 选中。
     */
    private void setResultFinish(CityBean province, CityBean city, CityBean district, CityBean four) {
        Intent intent = new Intent();
        intent.putExtra("receiverState",province.getName()+"");

        if (city != null && !city.equals("")) {
            intent.putExtra("receiverCity", city.getName() + "");
        }else {
            intent.putExtra("receiverCity", "");
        }
        if (district != null && !district.equals("")) {
            intent.putExtra("receiverDistrict", district.getName() + "");
        }else {
            intent.putExtra("receiverDistrict", "");
        }
        if (four != null && !four.equals("")) {
            intent.putExtra("receiverFour", four.getName() + "");
        }else {
            intent.putExtra("receiverFour", "");
        }
        setResult(999, intent);
        finish();
    }


    private TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {

        private int mCurrentPosition;

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int newPosition = tab.getPosition();
            switch (newPosition) {
                case 1: {
                    if (mTwoList == null) {
                        bind.tabLayoutAddress.getTabAt(mCurrentPosition).select();
                        return;
                    }
                    break;
                }
                case 2: {
                    if (mThreeList == null) {
                        bind.tabLayoutAddress.getTabAt(mCurrentPosition).select();
                        return;
                    }
                    break;
                }
            }
            this.mCurrentPosition = tab.getPosition();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    @Override
    public int layoutId() {
        return R.layout.activity_address_check;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        bind.tabLayoutAddress.addOnTabSelectedListener(tabSelectedListener);

        RecyclerView oneView = new RecyclerView(this);
        RecyclerView twoView = new RecyclerView(this);
        RecyclerView threeView = new RecyclerView(this);
        RecyclerView fourView = new RecyclerView(this);

        List<RecyclerView> recyclerViewList = new ArrayList<>();

        recyclerViewList.add(oneView);
        recyclerViewList.add(twoView);
        recyclerViewList.add(threeView);
        recyclerViewList.add(fourView);
        PagerViewAdapter<RecyclerView> pagerViewAdapter = new PagerViewAdapter<>(recyclerViewList);
        bind.viewPagerAddress.setAdapter(pagerViewAdapter);
        bind.tabLayoutAddress.setupWithViewPager(bind.viewPagerAddress);
        bind.tabLayoutAddress.getTabAt(0).setText("请选择");

        oneView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        oneView.setLayoutManager(new LinearLayoutManager(this));

        mOneListAdapter = new AddressListAdapter(getLayoutInflater(), mProvinceItemClickListener);
        oneView.setAdapter(mOneListAdapter);

        twoView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        twoView.setLayoutManager(new LinearLayoutManager(this));
        mTwoListAdapter = new AddressListAdapter(getLayoutInflater(), mCityItemClickListener);
        twoView.setAdapter(mTwoListAdapter);

        threeView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        threeView.setLayoutManager(new LinearLayoutManager(this));
        mThreeListAdapter = new AddressListAdapter(getLayoutInflater(), mDistrictItemClickListener);
        threeView.setAdapter(mThreeListAdapter);

        fourView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        fourView.setLayoutManager(new LinearLayoutManager(this));
        mFourListAdapter = new AddressListAdapter(getLayoutInflater(), mFourItemClickListener);
        fourView.setAdapter(mFourListAdapter);


        RequestCityListTask requestCityTask = new RequestCityListTask(this, callback);
        requestCityTask.execute();
    }
}
