/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sszt.cdslpetitionletter.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * <p>区域实体。</p>
 * Created by YanZhenjie on 2017/6/1.
 */
public class CityBean implements Parcelable {

    /**
     * id。
     */
    private String code;

    /**
     * 名称。
     */
    private String name;

    /**
     * 子项。
     */
    private List<CityBean> children;

    /**
     * 是否选中。
     */
    private boolean isSelect;

    public CityBean() {
    }

    protected CityBean(Parcel in) {
        code = in.readString();
        name = in.readString();
        children = in.createTypedArrayList(CityBean.CREATOR);
        isSelect = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeTypedList(children);
        dest.writeByte((byte) (isSelect ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {
        @Override
        public CityBean createFromParcel(Parcel in) {
            return new CityBean(in);
        }

        @Override
        public CityBean[] newArray(int size) {
            return new CityBean[size];
        }
    };

    public String getId() {
        return code;
    }

    public void setId(String mId) {
        code = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        name = mName;
    }

    public List<CityBean> getCityList() {
        return children;
    }

    public void setCityList(List<CityBean> mCityList) {
        this.children = mCityList;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean mSelect) {
        isSelect = mSelect;
    }

    public CityBean(String mId, String mName, List<CityBean> mCityList, boolean mIsSelect) {

        code = mId;
        name = mName;
        this.children = mCityList;
        isSelect = mIsSelect;
    }

}
