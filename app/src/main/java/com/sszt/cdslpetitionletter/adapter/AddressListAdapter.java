package com.sszt.cdslpetitionletter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sszt.cdslpetitionletter.R;
import com.sszt.cdslpetitionletter.bean.CityBean;

import java.util.List;


/**
 * @author:zcr
 * @date:2021/3/10
 * @description:地址列表适配器
 * */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<CityBean> mCityList;
    private final OnCompatItemClickListener mItemClickListener;



    public AddressListAdapter(LayoutInflater layoutInflater, OnCompatItemClickListener itemClickListener) {
        this.mLayoutInflater = layoutInflater;
        this.mItemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(List<CityBean> mCityList) {
        this.mCityList = mCityList;
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddressViewHolder viewHolder = new AddressViewHolder(mLayoutInflater.inflate(R.layout.adapter_address_select_item, parent, false));
        viewHolder.mItemClickListener = mItemClickListener;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        holder.setData(mCityList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCityList == null ? 0 : mCityList.size();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvAddress;
        RadioButton mRadioButton;

        OnCompatItemClickListener mItemClickListener;

        AddressViewHolder(View itemView) {
            super(itemView);
            mTvAddress = itemView.findViewById(R.id.tv_area_name);
            mRadioButton = itemView.findViewById(R.id.radio_btn);

            itemView.setOnClickListener(this);
        }

        public void setData(CityBean mData) {
            mTvAddress.setText(mData.getName());
            mRadioButton.setChecked(mData.isSelect());
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnCompatItemClickListener {

        void onItemClick(View view, int position);

    }

}
