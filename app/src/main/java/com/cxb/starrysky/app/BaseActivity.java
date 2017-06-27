package com.cxb.starrysky.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.cxb.starrysky.utils.DisplayUtil;
import com.cxb.starrysky.widget.DefaultProgressDialog;
import com.orhanobut.logger.Logger;


/**
 * 基类
 */

public class BaseActivity extends Activity implements ActivityListener {

    private InputMethodManager manager;

    private boolean curIsShow = false;

    private DefaultProgressDialog progressDialog;//等待对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(this.getLocalClassName());

        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        setStatusBar();
    }

    @Override
    protected void onDestroy() {
        //移除布局变化监听
        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutChangeListener);
        hideLoading();
        super.onDestroy();
    }

    //沉浸式
    private void setStatusBar() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);// 状态栏透明
//        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);// 导航栏透明
        window.getDecorView().setFitsSystemWindows(true);
    }

    protected void showLoading(String content) {
        if (progressDialog == null) {
            progressDialog = new DefaultProgressDialog(this);
        }
        progressDialog.setMessage(content);
        progressDialog.show();
    }

    protected void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void hideKeyboard() {
        if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //隐藏虚拟按键，并且全屏
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    //键盘显示隐藏回调
    protected void setOnKeyboardChangeListener() {
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mLayoutChangeListener);
    }

    //layout改变监听
    private ViewTreeObserver.OnGlobalLayoutListener mLayoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

            int screenHeight = DisplayUtil.getScreenHeight();
            int heightDifference = screenHeight - (r.bottom - r.top);

            boolean isShow = heightDifference > screenHeight / 3;

            if (!curIsShow && isShow || curIsShow && !isShow) {
                onkeyboardChange(isShow);
                curIsShow = isShow;
            }
        }
    };

    @Override
    public void onkeyboardChange(boolean isShow) {
        //键盘显示隐藏后的操作
    }

}
