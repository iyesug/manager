package com.vis.custom.customersmanage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.vis.custom.customersmanage.presenter.ViewPagerAdapter;
import com.vis.custom.customersmanage.util.DataKeeper;
import com.vis.custom.customersmanage.util.HttpListener;
import com.vis.custom.customersmanage.util.SnackbarUtil;
import com.vis.custom.customersmanage.view.RecyclerFragment;
import com.vis.custom.customersmanage.view.WaitDialog;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private DrawerLayout mDrawerl;
    private CoordinatorLayout mCoordinatorl;
    private AppBarLayout mAppbarl;
    private Toolbar mToolbar;
    private TabLayout mTabl;
    private ViewPager mViewpager;
    private FloatingActionButton mFloating;
    private NavigationView mNavigation;
    private DataKeeper dk;
    private String [] mTitles;
    private List<Fragment> mFragments;
    private ViewPagerAdapter mViewpageradapter;
    private WaitDialog mWaitDialog;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dk=new DataKeeper(this,DataKeeper.NAME);
        String theme=dk.get("theme","0");
        if(!"0".equals(theme)){
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        initView();
        initdata();
        setView();
        System.out.println("________________MainActivity");

    }


    private void initView() {
        mDrawerl= (DrawerLayout) findViewById(R.id.id_drawerlayout);
        mCoordinatorl= (CoordinatorLayout) findViewById(R.id.id_coordinatorlayout);
        mAppbarl= (AppBarLayout) findViewById(R.id.id_appbarlayout);
        mTabl= (TabLayout) findViewById(R.id.id_tablayout);
        mToolbar= (Toolbar) findViewById(R.id.id_toolbar);
        mViewpager= (ViewPager) findViewById(R.id.id_viewpager);
       // mFloating= (FloatingActionButton) findViewById(R.id.id_floatingactionbutton);
        mNavigation= (NavigationView) findViewById(R.id.id_navigationview);
        mWaitDialog = new WaitDialog(this);
    }


    private void initdata() {
        mTitles=getResources().getStringArray(R.array.titles);
        mFragments=new ArrayList<>();
        for(int i=0;i<mTitles.length;i++){
            Bundle mBundle=new Bundle();
            mBundle.putInt("flag",i);
            RecyclerFragment fragment=new RecyclerFragment();
            fragment.setArguments(mBundle);
            mFragments.add(i,fragment);
        }
    }



    private void setView() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,mDrawerl,mToolbar,R.string.open,R.string.close);
        toggle.syncState();
        mDrawerl.setDrawerListener(toggle);

        mNavigation.inflateHeaderView(R.layout.header);
        mNavigation.inflateMenu(R.menu.menu);
        itemSelected(mNavigation);

        mViewpageradapter=new ViewPagerAdapter(getSupportFragmentManager(),mFragments,mTitles);

        mViewpager.setAdapter(mViewpageradapter);
        mViewpager.setOffscreenPageLimit(6);
        mViewpager.addOnPageChangeListener(this);

        mTabl.setTabMode(TabLayout.MODE_FIXED);
        mTabl.setupWithViewPager(mViewpager);
       // mTabl.setTabsFromPagerAdapter(mViewpageradapter);
       // mFloating.setOnClickListener(this);

    }
    private void itemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {

                String msgString = "";

                switch (menuItem.getItemId()) {
                    case R.id.nav_menu_home:
                        msgString = (String) menuItem.getTitle();
                        break;
//                    case R.id.nav_menu_categories:
//
//
//                        // 创建请求对象。
//                        Request<String> request = NoHttp.createStringRequest(Util.URL, RequestMethod.POST);
//
//                        // 添加请求参数。
//                        request.add("do", 3);
//                        request.add("what", "c");
//
//                        CallServer.getRequestInstance().add(MainActivity.this, 0, request, httpListener, true, true);
//
//
//
//                        break;
//                    case R.id.nav_menu_feedback:
//
//                        Request<String> request1 = NoHttp.createStringRequest(Util.URL, RequestMethod.POST);
//                        // 添加请求参数。
//                        request1.add("do", 3);
//                        request1.add("what", "c");
//                        // 添加到请求队列
//                        CallServer.getRequestInstance().add(MainActivity.this, 0, request1, httpListener, true, true);
//
//
//
//                        break;
                    case R.id.nav_menu_skin:
                        String theme=dk.get("theme","0");
                        Log.e("theme",theme+"_______________________________________________________________");
                        if("0".equals(theme)){
                            setTheme(R.style.AppTheme1);
                            dk.put("theme","1");
                        }else{
                        setTheme(R.style.AppTheme);
                            dk.put("theme","0");
                        }
                        MainActivity.this.recreate();
                        break;
                }


                menuItem.setChecked(true);
                mDrawerl.closeDrawers();

                SnackbarUtil.show(mViewpager, msgString, 0);

                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // FloatingActionButton的点击事件
            case R.id.id_floatingactionbutton:
                SnackbarUtil.show(v, getString(R.string.dot), 0);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    private HttpListener<String> httpListener = new HttpListener<String>() {

        @Override
        public void onSucceed(int what, Response<String> response) {
            int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
            if (responseCode == 200) {
                if (RequestMethod.HEAD == response.getRequestMethod())// 请求方法为HEAD时没有响应内容
                    showMessageDialog(R.string.request_succeed, R.string.request_method_head);
                else{
                    String res=response.get();
                    showMessageDialog(R.string.request_succeed, res);
                    System.out.print(res);
                    Log.i("Json",res);
                    try {
                        JSONArray js=new JSONArray(res);
                        JSONObject jo=js.getJSONObject(0);
                        res=jo.getString("c_adr");
                        Log.i("Json",res);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            showMessageDialog(R.string.request_failed, exception.getMessage());
            SnackbarUtil.show(mViewpager, "请求失败: " + exception.getMessage(), 0);
        }
    };



    public void showMessageDialog(int title, int message) {
        showMessageDialog(getText(title), getText(message));
    }

    public void showMessageDialog(int title, CharSequence message) {
        showMessageDialog(getText(title), message);
    }

    public void showMessageDialog(CharSequence title, int message) {
        showMessageDialog(title, getText(message));
    }

    public void showMessageDialog(CharSequence title, CharSequence message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
     * 回调对象，接受请求结果.
     */
    private OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {
        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == what) {// 根据what判断是哪个请求的返回，这样就可以用一个OnResponseListener来接受多个请求的结果。
                int responseCode = response.getHeaders().getResponseCode();// 服务器响应码。

                if (responseCode == 200) {// 如果是是用NoHttp的默认的请求或者自己没有对NoHttp做封装，这里最好判断下Http状态码。
                    String result = response.get();// 响应结果。


                    Object tag = response.getTag();// 拿到请求时设置的tag。
                    byte[] responseBody = response.getByteArray();// 如果需要byteArray自己解析的话。

                    // 响应头
                    Headers headers = response.getHeaders();
                    String headResult = getString(R.string.request_original_result);
                    headResult = String.format(Locale.getDefault(), headResult, headers.getResponseCode(), response.getNetworkMillis());
                    SnackbarUtil.show(mViewpager, result, 0);
             //       SnackbarUtil.show(mViewpager, headResult, 0);

                }
            }
        }

        @Override
        public void onStart(int what) {
            // 请求开始，这里可以显示一个dialog
            if (mWaitDialog != null && !mWaitDialog.isShowing())
                mWaitDialog.show();
        }

        @Override
        public void onFinish(int what) {
            // 请求结束，这里关闭dialog
            if (mWaitDialog != null && mWaitDialog.isShowing())
                mWaitDialog.dismiss();
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            // 请求失败

            SnackbarUtil.show(mViewpager, "请求失败: " + exception.getMessage(), 0);
        }
    };




}
