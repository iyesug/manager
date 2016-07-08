/*
 * Copyright 2015 Yan Zhenjie
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
package com.vis.custom.customersmanage.util;

import android.content.DialogInterface;
import android.graphics.Bitmap;

import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.vis.custom.customersmanage.R;
import com.vis.custom.customersmanage.view.ImageDialog;
import com.vis.custom.customersmanage.view.WaitDialog;
import com.vis.custom.customersmanage.view.WebDialog;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.NotFoundCacheError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;
import com.yolanda.nohttp.tools.HeaderParser;

import java.net.ProtocolException;

/**
 * Created in Nov 4, 2015 12:02:55 PM.
 *
 * @author Yan Zhenjie.
 */
public class HttpResponseListener<T> implements OnResponseListener<T> {

    private Fragment mFragment;
    private AppCompatActivity mActivity;
    /**
     * Dialog.
     */
    private WaitDialog mWaitDialog;
    /**
     * Request.
     */
    private Request<?> mRequest;
    /**
     * 结果回调.
     */
    private HttpListener<T> callback;

    /**
     * @param activity     context用来实例化dialog.
     * @param request      请求对象.
     * @param httpCallback 回调对象.
     * @param canCancel    是否允许用户取消请求.
     * @param isLoading    是否显示dialog.
     */

    public HttpResponseListener(AppCompatActivity activity, Request<?> request, HttpListener<T> httpCallback, boolean canCancel, boolean isLoading) {
        this.mActivity = activity;
        this.mRequest = request;
        if (activity != null && isLoading) {
            mWaitDialog = new WaitDialog(activity);
            mWaitDialog.setCancelable(canCancel);
            mWaitDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mRequest.cancel();
                }
            });
        }
        this.callback = httpCallback;
    }
    public HttpResponseListener(Fragment activity, Request<?> request, HttpListener<T> httpCallback, boolean canCancel, boolean isLoading) {
        this.mFragment = activity;
        this.mRequest = request;
//        if (activity != null && isLoading) {
//
//
//        }
        this.callback = httpCallback;
    }
    /**
     * 开始请求, 这里显示一个dialog.
     */
    @Override
    public void onStart(int what) {
        if (mWaitDialog != null && !mActivity.isFinishing() && !mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    /**
     * 结束请求, 这里关闭dialog.
     */
    @Override
    public void onFinish(int what) {
        if (mWaitDialog != null && mWaitDialog.isShowing())
            mWaitDialog.dismiss();
    }

    /**
     * 成功回调.
     */
    @Override
    public void onSucceed(int what, Response<T> response) {
        int responseCode = response.getHeaders().getResponseCode();
        if (responseCode > 400 && mActivity != null) {
            if (responseCode == 405) {// 405表示服务器不支持这种请求方法，比如GET、POST、TRACE中的TRACE就很少有服务器支持。

                showMessageDialog(R.string.request_succeed, R.string.request_method_not_allow);
            } else {// 但是其它400+的响应码服务器一般会有流输出。
                showWebDialog(response);
            }
        }
        if (callback != null) {
            callback.onSucceed(what, response);
        }
    }

    /**
     * 失败回调.
     */
    @Override
    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
        if (exception instanceof NetworkError) {// 网络不好

            Toast.makeText((mActivity!=null?mActivity:mFragment.getActivity()), "请检查网络。", Toast.LENGTH_SHORT).show();

        } else if (exception instanceof TimeoutError) {// 请求超时
            Toast.makeText((mActivity!=null?mActivity:mFragment.getActivity()), "请求超时，网络不好或者服务器不稳定。", Toast.LENGTH_SHORT).show();

        } else if (exception instanceof UnKnownHostError) {// 找不到服务器
            Toast.makeText((mActivity!=null?mActivity:mFragment.getActivity()), "未发现指定服务器。", Toast.LENGTH_SHORT).show();

        } else if (exception instanceof URLError) {// URL是错的
            Toast.makeText((mActivity!=null?mActivity:mFragment.getActivity()), "URL错误。", Toast.LENGTH_SHORT).show();

        } else if (exception instanceof NotFoundCacheError) {
            // 这个异常只会在仅仅查找缓存时没有找到缓存时返回
            Toast.makeText((mActivity!=null?mActivity:mFragment.getActivity()), "没有发现缓存。", Toast.LENGTH_SHORT).show();

        } else if (exception instanceof ProtocolException) {
            Toast.makeText((mActivity!=null?mActivity:mFragment.getActivity()), "系统不支持的请求方式。", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText((mActivity!=null?mActivity:mFragment.getActivity()), "未知错误。", Toast.LENGTH_SHORT).show();

        }
        Logger.e("错误：" + exception.getMessage());
        if (callback != null)
            callback.onFailed(what, url, tag, exception, responseCode, networkMillis);
    }



    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(int title, int message) {
        showMessageDialog(mActivity.getText(title), mActivity.getText(message));
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(int title, CharSequence message) {
        showMessageDialog(mActivity.getText(title), message);
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(CharSequence title, int message) {
        showMessageDialog(title, mActivity.getText(message));
    }

    /**
     * Show message dialog.
     *
     * @param title   title.
     * @param message message.
     */
    public void showMessageDialog(CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * 显示图片dialog。
     *
     * @param title  标题。
     * @param bitmap 图片。
     */
    public void showImageDialog(CharSequence title, Bitmap bitmap) {
        ImageDialog imageDialog = new ImageDialog(mActivity);
        imageDialog.setTitle(title);
        imageDialog.setImage(bitmap);
        imageDialog.show();
    }

    /**
     * 显示一个WebDialog。
     *
     * @param response 响应。
     */
    public void showWebDialog(Response<?> response) {
        String result = StringRequest.parseResponseString(response.url(), response.getHeaders(), response.getByteArray());
        WebDialog webDialog = new WebDialog(mActivity);
        String contentType = response.getHeaders().getContentType();
        webDialog.loadUrl(result, contentType, HeaderParser.parseHeadValue(contentType, "charset", "utf-8"));
        webDialog.show();
    }


}
