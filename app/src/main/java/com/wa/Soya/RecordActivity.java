package com.wa.Soya;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.wa.Soya.sqlite.DbHelper;
import com.wa.Soya.ui.PermissionUtils;
import com.wa.Soya.ui.ToastUtil;
import com.wa.Soya.ui.XPermissionUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    private EditText height, age, ps;
    private Button commit, back, camera, fresh;
    private Bitmap mBitmap=null;
    private TextView name, loca;
    private Date date;
    private String locationProvider, loc = "暂无位置信息，请稍等片刻后刷新…";
    private LocationManager locationManager;
    private Location location;
    private ImageView iv;
    private static final String PATH = Environment.getExternalStorageDirectory() + "/SoyaPic/";

//savephoto
    static void savePic(String path, String picName, Bitmap photo) throws IOException {

        if (android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            File photoFile = new File(path, picName);
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photo != null) {
                    if (photo.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        final DbHelper mySql = new DbHelper(this, "Soya", null, 1);
        height = findViewById(R.id.soyhigh);
        age = findViewById(R.id.soyage);
        ps = findViewById(R.id.ps);
        commit = findViewById(R.id.commit);
        back = findViewById(R.id.back);
        name = findViewById(R.id.uid);
        camera = findViewById(R.id.pic);
        loca = findViewById(R.id.location);
        fresh = findViewById(R.id.fresh);
        iv = findViewById(R.id.image);
        PermissionUtils.verifyStoragePermissions(this);

        File file2 = new File(Environment.getExternalStorageDirectory() + "/SoyaPic/");
        if (!file2.exists()) file2.mkdirs();

        Intent intent = getIntent();
        final String uid = intent.getStringExtra("uid");
//获取当前时间
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final SimpleDateFormat fileName = new SimpleDateFormat("yyyy-MM-dd HHmmss");
        date = new Date(System.currentTimeMillis());
        name.setText("采集人: " + uid + "  记录时间：" + simpleDateFormat.format(date));

//位置数据
        XPermissionUtils.requestPermissions(this, 1, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, new XPermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {
                //权限获取成功，进行你需要的操作
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = locationManager.getProviders(true);
                if (providers.contains(LocationManager.GPS_PROVIDER)) {
                    //如果是GPS
                    locationProvider = LocationManager.GPS_PROVIDER;
                } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    //如果是Network
                    locationProvider = LocationManager.NETWORK_PROVIDER;
                } else {
                    ToastUtil.showShort(RecordActivity.this, "没有可用的位置提供器");
                }
                location = locationManager.getLastKnownLocation(locationProvider);
                if (location != null) {
                    //不为空,显示地理位置经纬度
                    loc = "纬度：" + location.getLatitude() + " "
                            + "\n　　　　　经度：" + location.getLongitude();
                    loca.setText("当前位置　" + loc);
                }
                locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

            }

            @Override
            public void onPermissionDenied() {
                //权限获取失败，进行你需要的操作
                ToastUtil.showShort(RecordActivity.this, "请授予定位权限");
                finish();
            }
        });


//相机授权
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                XPermissionUtils.requestPermissions(RecordActivity.this, 2, new String[]{Manifest.permission.CAMERA}, new XPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //权限获取成功，进行你需要的操作

                        try {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 0);
                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }


                    }

                    @Override
                    public void onPermissionDenied() {
                        //权限获取失败，进行你需要的操作
                        ToastUtil.showShort(RecordActivity.this, "请授予照相权限");
                    }
                });

            }
        });

//刷新数据
        fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date(System.currentTimeMillis());
                name.setText("采集人: " + uid + "  记录时间：" + simpleDateFormat.format(date));
                List<String> providers = locationManager.getProviders(true);
                if (providers.contains(LocationManager.GPS_PROVIDER)) {
                    //如果是GPS
                    locationProvider = LocationManager.GPS_PROVIDER;
                } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
                    //如果是Network
                    locationProvider = LocationManager.NETWORK_PROVIDER;
                } else {
                    ToastUtil.showShort(RecordActivity.this, "没有可用的位置提供器");
                }
                location = locationManager.getLastKnownLocation(locationProvider);
                if (location != null) {
                    //不为空,显示地理位置经纬度
                    loc = "纬度：" + location.getLatitude() + " "
                            + "\n　　　　　经度：" + location.getLongitude();
                    loca.setText("当前位置　" + loc);
                }
            }
        });


//提交数据库
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String gao = height.getText().toString();
                String sui = age.getText().toString();
                String beizhu = ps.getText().toString();
                if (!"".equals(gao)) {
                    if (!"".equals(sui)) {

                        commit.setEnabled(false);
                        SQLiteDatabase db = mySql.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        Integer zg = Integer.parseInt(gao);
                        Integer syq = Integer.parseInt(sui);
                        values.put("name", uid);
                        values.put("height", zg);
                        values.put("age", syq);
                        values.put("date", simpleDateFormat.format(date));
                        values.put("ps", beizhu);
                        values.put("pic", fileName.format(date) + ".jpg");
                        values.put("loc", loc);
                        db.insert("Soya", null, values);
                        values.clear();
                        if (mBitmap != null) {
                            try {
                                savePic(PATH, fileName.format(date) + ".jpg", mBitmap);
                            } catch (IOException e) {
                                ToastUtil.showShort(RecordActivity.this, "照片保存失败");
                                e.printStackTrace();
                            }
                        }

                        /*try {
                            FileOutputStream out = new FileOutputStream( Environment.getExternalStorageDirectory()+ "/SoyaPic/"+simpleDateFormat.format(date)+".jpg");

                            mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                            out.flush();
                            out.close();
                            ToastUtil.showShort(RecordActivity.this, Environment.getExternalStorageDirectory().toString());
                        } catch (Exception e) {
                            ToastUtil.showShort(RecordActivity.this, "照片保存失败");
                        }*/

                        ToastUtil.showShort(RecordActivity.this, "记录保存成功，2秒后返回上一级界面");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);    //延时2s执行new Handler().postDelayed(new Runnable() {
                    } else ToastUtil.showShort(RecordActivity.this, "请输入生育期数据");
                } else ToastUtil.showShort(RecordActivity.this, "请输入株高数据");

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            super.onActivityResult(requestCode, resultCode, data);
            Bundle Extras = data.getExtras();
            mBitmap = (Bitmap) Extras.get("data");
            iv.setImageBitmap(mBitmap);
        } else {
            ToastUtil.showShort(this, "您没有拍摄照片！");

        }
    }


    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle arg2) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            //如果位置发生变化,重新显示
            loc = "纬度：" + location.getLatitude() + " " + "\n　　　　　经度：" + location.getLongitude();

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //移除监听器
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        XPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
