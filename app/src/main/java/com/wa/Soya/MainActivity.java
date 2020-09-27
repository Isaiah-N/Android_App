package com.wa.Soya;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wa.Soya.sqlite.DbHelper;
import com.wa.Soya.ui.PermissionUtils;
import com.wa.Soya.ui.ToastUtil;
import com.wa.Soya.ui.XPermissionUtils;

import java.io.File;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Button fBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
//初始化用户表
        preferences = getSharedPreferences("userSP", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("admin","admin");
        editor.commit();
//初始化数据表
        final DbHelper mySql = new DbHelper(this, "Soya", null, 1);
        SQLiteDatabase db = mySql.getWritableDatabase();

//创建应用文件夹
        File file = new File(Environment.getExternalStorageDirectory() + "/SoyaDb/");
        File file2 = new File(Environment.getExternalStorageDirectory() + "/SoyaPic/");
        if (!file.exists()) {
            /**  注意这里是 mkdirs()方法  可以创建多个文件夹 */
            file.mkdirs();
        } else if (!file2.exists()) file2.mkdirs();

//权限
        PermissionUtils.verifyStoragePermissions(this);
        XPermissionUtils.requestPermissions(this, 1, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                //权限获取成功，进行你需要的操作
            }

            @Override
            public void onPermissionDenied() {
                //权限获取失败，进行你需要的操作
                ToastUtil.showShort(MainActivity.this, "请授予定位权限");
            }
        });
        XPermissionUtils.requestPermissions(MainActivity.this, 2, new String[]{Manifest.permission.CAMERA}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                //权限获取成功，进行你需要的操作
            }

            @Override
            public void onPermissionDenied() {
                //权限获取失败，进行你需要的操作
                ToastUtil.showShort(MainActivity.this, "请授予照相权限");
            }
        });


        //已阅读引导页，跳转到登录页
        fBtn = findViewById(R.id.btn_1);
        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
