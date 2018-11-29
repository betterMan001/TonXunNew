package tx.a316.com.tx_teacher.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.anlia.photofactory.factory.PhotoFactory;
import com.anlia.photofactory.result.ResultData;
import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import tx.a316.com.tx_teacher.MyApplication;
import tx.a316.com.tx_teacher.R;
import tx.a316.com.tx_teacher.Utils.CleanCacheUtil;
import tx.a316.com.tx_teacher.Utils.MyHttpUtil;
import tx.a316.com.tx_teacher.Utils.Net;
import tx.a316.com.tx_teacher.activites.ChangePwActivity;
import tx.a316.com.tx_teacher.activites.MainActivity;
import tx.a316.com.tx_teacher.activites.MessageActivity;
import tx.a316.com.tx_teacher.activites.welcomeActivity;
import tx.a316.com.tx_teacher.services.serviceImp.TeacherInformationServiceImp;

import static android.app.Activity.RESULT_OK;
import static tx.a316.com.tx_teacher.activites.MainActivity.mineModel;

public class MineFragment extends Fragment {
    Unbinder unbinder;
    @BindView(R.id.mine_swf)
    SwipeRefreshLayout mineSwf;
    @BindView(R.id.my_name_tv)
    TextView myNameTv;
    @BindView(R.id.my_picture_url_iv)
    ImageView myPictureUrlIv;
    private View view;
    private SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    //老师的头像
    public Bitmap teacherHead;
     /*上传头像相关变量*/
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;
    //调用照相机返回的图片
    Bitmap modifyHead;
    //调用照相机返回图片文件
    private File tempFile;
    //裁剪用工厂
   private  PhotoFactory photoFactory;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplication());
        editor = pref.edit();
        //从网络获取老师头像
        getImage();
        return view;
    }

    private void initView() {
        if (mineModel != null) {
            myNameTv.setText(mineModel.getName());
        }
        mineSwf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       updateUserInfo();
                   }
               });
            }
        });
         photoFactory=new PhotoFactory(getActivity());
    }
private void updateUserInfo(){
        //重新获得老师数据
    new TeacherInformationServiceImp(this).getTeacherInfo(MyApplication.getT_tid());

}
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.my_exit_ll)
    public void exit_Click() {
        //新建exit类型对话框设置边距加载到Fragment
        ConfirmDialog.newInstance("exit").setMargin(60).setOutCancel(false).show(getFragmentManager());
    }

    public static void exit() {
        //1.将Application类的登录信息清空
        MyApplication.setT_tid("");
        MyApplication.setToken("");
        //2.将本地存储的数据清空
        editor.clear();
        editor.apply();
        //退出环信账号
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("lzan13", "logout success");
            }

            @Override
            public void onError(int i, String s) {
                Log.i("lzan13", "logout error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        //3.完成后跳转至登录界面

        Intent intent = new Intent(MyApplication.getContext(), welcomeActivity.class);
        //4.将loginActivity放入新的task
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //5.跳转
        MyApplication.getContext().startActivity(intent);
    }

    @OnClick(R.id.my_message_ll)
    public void message_Click() {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }

    @OnClick(R.id.my_clean)
    public void clean_Click() {
        ConfirmDialog.newInstance("clean")
                .setMargin(60)
                .setOutCancel(false)
                .show(getFragmentManager());
    }

    @OnClick(R.id.my_modifypwd_ll)
    public void modifyPwd_onclick() {
        Intent intent = new Intent(getActivity(), ChangePwActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getContext().startActivity(intent);
    }

    @OnClick(R.id.my_modifyhead_ll)
    public void modifyhead_click() {
       //上传图像
        uploadHeadImage();
    }
    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_popupwindow, null);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        //将要加载的布局绑定到popupWindow
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        //要加载到的视图
        View parent = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.5f;
        getActivity().getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getActivity().getWindow().setAttributes(params);
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    getPicFromAlbm();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    public static class ConfirmDialog extends BaseNiceDialog {
        private String type;

        //以bundle的形式传递参数，可以保证Activity重建Fragment数据不变
        public static ConfirmDialog newInstance(String type) {
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            ConfirmDialog dialog = new ConfirmDialog();
            dialog.setArguments(bundle);
            return dialog;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle bundle = getArguments();
            if (bundle == null) {
                return;
            }
            type = bundle.getString("type");
        }

        @Override
        public int intLayoutId() {
            return R.layout.confirm_layout;
        }

        //重载方法画对话框,用type来判断对话框的种类
        @Override
        public void convertView(ViewHolder holder, final BaseNiceDialog dialog) {

            if ("exit".equals(type)) {
                holder.setText(R.id.title, "提示");
                holder.setText(R.id.message, "是否退出登陆?");

                holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        exit();
                    }
                });

            } else if ("clean".equals(type)) {
                holder.setText(R.id.title, "提示");
                holder.setText(R.id.message, "是否清除缓存?");

                holder.setOnClickListener(R.id.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                holder.setOnClickListener(R.id.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        CleanCacheUtil.clearAllCache(getContext());
                        Toast.makeText(getContext(), "缓存已清理完成", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    }
    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
    }
    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        photoFactory.FromCrop(uri).StartForResult(new PhotoFactory.OnResultListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onSuccess(ResultData resultData) {
                modifyHead=resultData.GetBitmap();
                updateImageService();
            }

            @Override
            public void onError(String s) {
                  getActivity().runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Toast.makeText(getActivity(),"裁图片失败了",Toast.LENGTH_LONG).show();
                      }
                  });
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    cropPhoto(uri);
                }
                break;
        }
    }
    private void getImage() {
        String URL = mineModel.getPicture_url();
        //老师没有原始头像
        if (URL == null || URL.equals(""))
            initView();
        else {
            //使用后台服务器传来的url获取图片
            MyHttpUtil.sendOkhttpGetRequest(URL, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("image", "fail");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //接受图片比特流
                    byte[] pi = response.body().bytes();
                    //转化为位图
                    teacherHead = BitmapFactory.decodeByteArray(pi, 0, pi.length);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myPictureUrlIv.setImageBitmap(teacherHead);
                            initView();
                        }
                    });
                }
            });
        }


    }
    private void updateImageService(){
        //将获得的BitMap传到服务器
        String URL= Net.updateTeacherIma;
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        modifyHead.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
         //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("t_tid",MyApplication.getT_tid());
        builder.add("image",imageString);
        RequestBody formBody = builder.build();
        MyHttpUtil.sendOkhttpPostRequest(URL, formBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("imageService","上传图片失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //修改头像成功更新视图
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myPictureUrlIv.setImageBitmap(modifyHead);
                    }
                });
            }
        });
    }
    public void callBack(){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //防止空指针或者断网带啦的闪退
                if (mineModel != null) {
                    if(mineModel.getName()!=null)
                        myNameTv.setText(mineModel.getName());
                    if(mineModel.getPicture_url()!=null){
                        Glide.with(getActivity()).load(mineModel.getPicture_url()).placeholder(R.drawable.head).into(myPictureUrlIv);
                    }
                }
                //关闭下拉加载动画
               mineSwf.setRefreshing(false);
            }
        });
    }
}
