package com.cxb.starrysky.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 自定义loading对话框
 */
public class DefaultProgressDialog {

    private OnDismissListener mOnDismissListener;

    private ProgressDialog progressDialog;

    public DefaultProgressDialog(Context ctx) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ctx);
        }
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void setCancelable(boolean cancelable) {
        progressDialog.setCancelable(cancelable);
    }

    public void setMessage(String msg) {
        progressDialog.setMessage(msg);
    }

    public void show() {
        if (progressDialog != null && !progressDialog.isShowing())
            progressDialog.show();
    }

    public void dismiss() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    //设置取消按钮点击后的回调事件
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        progressDialog.setOnDismissListener(onDismissListener);
    }

    //对话框返回键监听
    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        progressDialog.setOnKeyListener(onKeyListener);
    }

    public interface OnDismissListener {
        void OnDismissListener();
    }
}
