package com.cxb.starrysky.ui;

import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.cxb.starrysky.R;
import com.cxb.starrysky.app.BaseActivity;
import com.cxb.starrysky.model.PersonInfo;
import com.cxb.starrysky.utils.AssetsUtil;
import com.cxb.starrysky.utils.ToastMaster;
import com.cxb.starrysky.widget.starrysky.OnStarSelectListener;
import com.cxb.starrysky.widget.starrysky.StarrySkyView2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 星空
 */

public class StarrySkyActivity extends BaseActivity {

    private StarrySkyView2 ssvStar;
    private List<PersonInfo> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starry_sky);

        initView();
        setData();

    }

    private void initView() {
        ssvStar = (StarrySkyView2) findViewById(R.id.ssv_star);
        ssvStar.setOnStarSelectListener(starListener);
    }

    private void setData() {
        mList = new ArrayList<>();

        Observable.create(new ObservableOnSubscribe<List<PersonInfo>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<PersonInfo>> e) throws Exception {
                String json = AssetsUtil.getAssetsTxtByName(StarrySkyActivity.this, "family_tree.txt");
                List<PersonInfo> temp = JSONObject.parseArray(json, PersonInfo.class);
                if (!e.isDisposed()) {
                    e.onNext(temp);
                    e.onComplete();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<PersonInfo>>() {
                    @Override
                    public void accept(@NonNull List<PersonInfo> personList) throws Exception {
                        if (personList != null) {
                            mList.clear();
                            mList.addAll(personList);
                        }
                        setPersonAvatar();
                    }
                });
    }

    private void setPersonAvatar() {
        if (mList != null) {
            Collections.shuffle(mList);
            ssvStar.setPersonList(mList);
        }
    }

    private OnStarSelectListener starListener = new OnStarSelectListener() {
        @Override
        public void onStarSelect(int position) {
            ToastMaster.toast(mList.get(position).getMemberName());
        }

        @Override
        public void onSliding() {
            setPersonAvatar();
        }
    };

}
