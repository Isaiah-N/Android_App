package com.wa.Soya;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.wa.Soya.ui.ToastUtil;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences preferences,userSP;
    private SharedPreferences.Editor editor;
    private EditText userName,userPwd;
    private Button login;
    private ProgressBar load;

    private String user,pwd;
    @SuppressLint("WrongConstant")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        preferences = getSharedPreferences("guideActivity", MODE_PRIVATE);

    // 判断是不是首次登录
        if (preferences.getBoolean("firstStart", true)) {
            editor = preferences.edit();
            // 将登录标志位设置为false，下次登录时不在显示引导页
            editor.putBoolean("firstStart", false);
            editor.commit();

            //跳转到引导页
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        //如果不是首次登录 启动mainactivity加载项
        userSP = getSharedPreferences("userSP",Context.MODE_APPEND);
        userName = findViewById(R.id.username);
        userPwd = findViewById(R.id.password);
        login = findViewById(R.id.login);
        load = findViewById(R.id.loading);

        userPwd.setTransformationMethod( new PasswordTransformationMethod());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = userName.getText().toString();
                pwd = userPwd.getText().toString();
                if ("".equals(user)){
                    ToastUtil.showLong(LoginActivity.this,"请输入用户名");
                }else if ("".equals(pwd)){
                    ToastUtil.showLong(LoginActivity.this,"密码不能为空");
                } else {
                    login.setEnabled(false);
                    load.setVisibility(View.VISIBLE);


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {



                            if (userSP.contains(user)){
                                if (userSP.getString(user,"").equals(pwd)) {
                                    //登陆成功
                                    load.setVisibility(View.GONE);
                                    ToastUtil.showShort(LoginActivity.this,"欢迎使用~");
                                    Intent intent = new Intent(LoginActivity.this,FunctionActivity.class);
                                    intent.putExtra("uid",user);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    load.setVisibility(View.GONE);
                                    ToastUtil.showShort(LoginActivity.this,"密码错误,请查证后重新登陆");
                                    login.setEnabled(true);
                                }
                            }else {
                                load.setVisibility(View.GONE);
                                ToastUtil.showLong(LoginActivity.this,"用户不存在,请查证后重新登陆");
                                login.setEnabled(true);
                            }
                        }
                    }, 500);

                }

            }
        });



    }
}
