package com.vis.custom.customersmanage.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vis.custom.customersmanage.R;
import com.vis.custom.customersmanage.presenter.RecyclerViewAdapter;
import com.vis.custom.customersmanage.presenter.StaggeredViewAdapter;
import com.vis.custom.customersmanage.util.CallServer;
import com.vis.custom.customersmanage.util.DataKeeper;
import com.vis.custom.customersmanage.util.HttpListener;
import com.vis.custom.customersmanage.util.MyPattern;
import com.vis.custom.customersmanage.util.SnackbarUtil;
import com.vis.custom.customersmanage.util.Util;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RecyclerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, RecyclerViewAdapter.OnItemClickListener,
        StaggeredViewAdapter.OnItemClickListener, View.OnClickListener{
    private View mView;
    private SwipeRefreshLayout mSwipeRefreshl;
    private RecyclerView mRecyclerview;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mRecyclerviewadapter;
    private StaggeredViewAdapter mStaggeredadapter;
    private int flag=0;
    private Context context;
    private List<String> mData;
    private List<String> mId;
    private int nid = 000;
    private DataKeeper dk;

    private FloatingActionButton mFloating;


    private String messages;
    private View myView=null;
    private TextView id = null,name= null,adr= null,tel= null,mail= null,fax= null,qq= null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                                 Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dk=new DataKeeper(getActivity(),DataKeeper.NAME);
        mSwipeRefreshl = (SwipeRefreshLayout) mView.findViewById(R.id.id_swiperefreshlayout);
        mRecyclerview = (RecyclerView) mView.findViewById(R.id.id_recyclerview);
        mFloating= (FloatingActionButton) mView.findViewById(R.id.id_floatingactionbutton);
        mFloating.setOnClickListener(this);
        flag = (int) getArguments().get("flag");

        if (flag != Util.STAGGERED_GRID) {


            request(0,-1,"c", null);
        } else {
            request(0,-1,"d", null);
        }

        // 指示器旋转颜色
        mSwipeRefreshl.setColorSchemeResources(R.color.main, R.color.main_dark);
        mSwipeRefreshl.setOnRefreshListener(this);
    }



    private void configRecyclerView() {

        switch (flag) {
            case Util.VERTICAL_LIST:
                mLayoutManager =
                        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                break;
            case Util.HORIZONTAL_LIST:
                mLayoutManager =
                        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                break;
            case Util.VERTICAL_GRID:
                mLayoutManager =
                        new GridLayoutManager(getActivity(), Util.SPAN_COUNT, GridLayoutManager.VERTICAL, false);
                break;
            case Util.HORIZONTAL_GRID:
                mLayoutManager =
                        new GridLayoutManager(getActivity(), Util.SPAN_COUNT, GridLayoutManager.HORIZONTAL, false);
                break;
            case Util.STAGGERED_GRID:
                mLayoutManager =
                        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//                        new StaggeredGridLayoutManager(Util.SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
                break;
        }

        if (flag != Util.STAGGERED_GRID) {


            mRecyclerviewadapter = new RecyclerViewAdapter(context,mData,mId);
            mRecyclerviewadapter.setOnItemClickListener(this);
            mRecyclerview.setAdapter(mRecyclerviewadapter);
        } else {
            mRecyclerviewadapter = new RecyclerViewAdapter(getActivity(),mData,mId);
            mRecyclerviewadapter.setOnItemClickListener(this);
            mRecyclerview.setAdapter(mRecyclerviewadapter);
//            mStaggeredadapter = new StaggeredViewAdapter(getActivity(),mData,mId);
//            mStaggeredadapter.setOnItemClickListener(this);
//            mRecyclerview.setAdapter(mStaggeredadapter);
        }

        mRecyclerview.setLayoutManager(mLayoutManager);

    }


    //刷新数据
    @Override
    public void onRefresh() {


        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                mSwipeRefreshl.setRefreshing(false);


                if (flag != Util.STAGGERED_GRID) {


                    request(0,-1,"c", null);
                } else {
                    request(0,-1,"d", null);
                }


            }
        }, 1000);
        if(mRecyclerviewadapter!=null) {

            mRecyclerviewadapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(View view, int position) {


        if (flag != Util.STAGGERED_GRID) {request(3,Integer.parseInt(mId.get(position)),"c", null);}
        else {request(3,Integer.parseInt(mId.get(position)),"d", null);}



    }

    private void request(int doo, int id, String what, List<String> list) {
        // 创建请求对象。
        Request<String> request = NoHttp.createStringRequest(Util.URL, RequestMethod.POST);

        // 添加请求参数。
        request.add("do", doo);
        request.add("what", what);
        request.add("id", id);
        if(list!=null){

            request.add("name", list.get(1));
            request.add("adr", list.get(2));
            request.add("tel", list.get(3));
            request.add("mail", list.get(4));
            request.add("fax", list.get(5));
            request.add("qq", list.get(6));
        }

        CallServer.getRequestInstance().add(this, doo, request, httpListener, true, true);
    }

    @Override
    public void onItemLongClick(View view, final int position) {
       String[] option=getResources().getStringArray(R.array.what);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle(R.string.choose);
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(which==0){
                    if (flag != Util.STAGGERED_GRID) {
                        request(3,Integer.parseInt(mId.get(position)),"c", null);}
                    else {
                        request(3,Integer.parseInt(mId.get(position)),"d", null);}
                }
                if(which==1){
                    if (flag != Util.STAGGERED_GRID) {
                        request(21,Integer.parseInt(mId.get(position)),"c", null);}
                    else {
                        request(21,Integer.parseInt(mId.get(position)),"d", null);}

                }
                if(which==2){


                    showConfirmDialog("确认","确认删除吗？",position);


                }
                if(which==3){
                    if (flag != Util.STAGGERED_GRID) {
                        showMessageDialog("新增资料", null,1);
                       }
                    else {
                        showMessageDialog("新增资料", null,1);
                       }

                }

            }
        });
        builder.create();
        builder.show();

    }




    private HttpListener<String> httpListener = new HttpListener<String>() {

        @Override
        public void onSucceed(int what, Response<String> response) {
            int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
            if (responseCode == 200) {
                if (RequestMethod.HEAD == response.getRequestMethod())// 请求方法为HEAD时没有响应内容

                SnackbarUtil.show(mView, context.getString(R.string.request_succeed), 0);
                else{
                    String res=response.get();
                  // Toast.makeText(context, res, Toast.LENGTH_SHORT).show();

                    Log.i("Json",res);
                    try {
                        if(what==0) {
                            JSONArray js = new JSONArray(res);
                            mData = new ArrayList<>();
                            mId = new ArrayList<>();
                            for (int i = 0; i < js.length(); i++) {
                                JSONObject jo = js.getJSONObject(i);
                                String data = jo.toString();
                                int id;
                                if(jo.has("c_id")){
                                id = jo.getInt("c_id");}
                                else{
                                   id = jo.getInt("d_id");
                                }
                                mData.add(data);
                                mId.add(id + "");
                                configRecyclerView();
                            }
                        }
                        if(what==3){
                            showMessageDialog("详细资料", res,what);
                        }
                        if(what==21){
                            showMessageDialog("修改资料", res,what);
                        }
                        if(what==2){
                            messages=res.substring(1,res.length()-1);
                          Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();
                          //  SnackbarUtil.show(mView,messages, 0);

                        }
                        if(what==4){
                            messages=res.substring(1,res.length()-1);
                            Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();
                          //  SnackbarUtil.show(mView,messages, 0);

                            if (flag != Util.STAGGERED_GRID) {


                                request(0,-1,"c", null);
                            } else {
                                request(0,-1,"d", null);
                            }

                        }
                        if(what==1){
                            messages=res.substring(1,res.length()-1);
                            Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();
                          //  SnackbarUtil.show(mView,messages, 0);
                            if (flag != Util.STAGGERED_GRID) {


                                request(0,-1,"c", null);
                            } else {
                                request(0,-1,"d", null);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

            SnackbarUtil.show(mView, context.getString(R.string.request_failed) + exception.getMessage(), 0);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void showMessageDialog(String title, final String message, final int what) {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

        if(what==3){
           myView = layoutInflater.inflate(R.layout.mydialog, null);


            id= (TextView) myView.findViewById(R.id.id_textview_id);
            name= (TextView) myView.findViewById(R.id.id_textview_name);
            adr= (TextView) myView.findViewById(R.id.id_textview_adr);
            tel= (TextView) myView.findViewById(R.id.id_textview_tel);
            mail= (TextView) myView.findViewById(R.id.id_textview_mail);
            fax= (TextView) myView.findViewById(R.id.id_textview_fax);
            qq= (TextView) myView.findViewById(R.id.id_textview_qq);
            }
        if(what==21||what==1){
           myView = layoutInflater.inflate(R.layout.myeditdialog, null);


            id= (TextView) myView.findViewById(R.id.id_textview_id);
            name= (EditText) myView.findViewById(R.id.id_textview_name);
            adr= (EditText) myView.findViewById(R.id.id_textview_adr);
            tel= (EditText) myView.findViewById(R.id.id_textview_tel);
            mail= (EditText) myView.findViewById(R.id.id_textview_mail);
            fax= (EditText) myView.findViewById(R.id.id_textview_fax);
            qq= (EditText) myView.findViewById(R.id.id_textview_qq);
        }

        String nname = null,nadr = null,ntel = null,nmail = null,nfax = null,nqq = null;
        try {
            if(what!=1){
            JSONArray js = new JSONArray(message);
            JSONObject jo = js.getJSONObject(0);

            if(jo.has("c_id")){
                nid = jo.getInt("c_id");
                nname = jo.getString("c_name");
                nadr = jo.getString("c_adr");
                ntel = jo.getString("c_tel");
                nmail = jo.getString("c_mail");
                nfax = jo.getString("c_fax");
                nqq = jo.getString("c_qq");
            }else{
                nid = jo.getInt("d_id");
                nname = jo.getString("d_name");
                nadr = jo.getString("d_adr");
                ntel = jo.getString("d_tel");
                nmail = jo.getString("d_mail");
                nfax = jo.getString("d_fax");
                nqq = jo.getString("d_qq");

            }


            id.setText(""+nid);
            name.setText(""+nname);
            adr.setText(""+nadr);
            tel.setText(""+ntel);
            mail.setText(""+nmail);
            fax.setText(""+nfax);
            qq.setText(""+nqq);
            }
            if(what==1){

                id.setText("");
                name.setText(dk.get("mname",""));
                adr.setText(dk.get("madr",""));
                tel.setText(dk.get("mtel",""));
                mail.setText(dk.get("mmail",""));
                fax.setText(dk.get("mfax",""));
                qq.setText(dk.get("mqq",""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(title);

        builder.setIcon(R.mipmap.ic_launcher);
        if(what==21||what==1){
            builder.setPositiveButton(R.string.commit, new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {


                    String mid=id.getText().toString().trim();
                    String mname=name.getText().toString().trim();
                    String madr=adr.getText().toString().trim();
                    String mtel=tel.getText().toString().trim();
                    String mmail=mail.getText().toString().trim();
                    String mfax=fax.getText().toString().trim();
                    String mqq=qq.getText().toString().trim();
                    dk.put("mname",mname);
                    dk.put("madr",madr);
                    dk.put("mtel",mtel);
                    dk.put("mmail",mmail);
                    dk.put("mfax",mfax);
                    dk.put("mqq",mqq);

                    if("".equals(mname)||"".equals(madr)||"".equals(mtel)||"".equals(mmail)||"".equals(mfax)||"".equals(mqq)){

                        Toast.makeText(context, "失败，字段不能为空！", Toast.LENGTH_SHORT).show();

                    }

                    else if(!MyPattern.isEmail(mmail)){
                        Toast.makeText(context, "邮箱格式错误！！！", Toast.LENGTH_SHORT).show();
                    }

                    else if(!MyPattern.isQq(mqq)){
                        Toast.makeText(context, "qq格式错误！！！", Toast.LENGTH_SHORT).show();
                    }

                     else {

                        List <String> list=new ArrayList<>();
                        list.add(mid);list.add(mname);list.add(madr);list.add(mtel);list.add(mmail);list.add(mfax);list.add(mqq);
                        String doo=null;
                        if (flag != Util.STAGGERED_GRID) {doo="c";
                        }else{doo="d";}
                        if(what==21){
                            request(2, Integer.parseInt(mid),doo,list);
                            Log.w("log","request(2___________________________________");
                        }else{
                            request(1, -1,doo,list);
                            Log.w("log","request(1___________________________________");
                        }
                        dk.put("mname",null);
                        dk.put("madr",null);
                        dk.put("mtel",null);
                        dk.put("mmail",null);
                        dk.put("mfax",null);
                        dk.put("mqq",null);

                    }
                    if (flag != Util.STAGGERED_GRID) {


                        request(0,-1,"c", null);
                    } else {
                        request(0,-1,"d", null);
                    }

                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
        }
        if(what==3) {

            builder.setNegativeButton(R.string.edit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (flag != Util.STAGGERED_GRID) {
                        request(21,nid,"c", null);}
                    else {
                        request(21,nid,"d", null);}
                }
            });


            builder.setPositiveButton(R.string.know, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }


        builder.setView(myView);
        builder.setCancelable(false);
        builder.show();




    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // FloatingActionButton的点击事件
            case R.id.id_floatingactionbutton:
                if (flag != Util.STAGGERED_GRID) {
                    showMessageDialog("新增资料", null,1);
                }
                else {
                    showMessageDialog("新增资料", null,1);
                }

                break;
        }
    }




    public void showConfirmDialog(CharSequence title, CharSequence message, final int position) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(R.string.commit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (flag != Util.STAGGERED_GRID) {
                    request(4,Integer.parseInt(mId.get(position)),"c", null);}
                else {
                    request(4,Integer.parseInt(mId.get(position)),"d",null);}

            }
        });
        builder.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
