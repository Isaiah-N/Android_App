package com.wa.Soya;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wa.Soya.ui.ToastUtil;

public class ResignActivity extends AppCompatActivity {

    private SharedPreferences userSP;
    private SharedPreferences.Editor editor;
    private Button resign,back;
    private EditText name,pwd,rePwd;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        resign = findViewById(R.id.resign);
        name = findViewById(R.id.userName);
        pwd = findViewById(R.id.pwd);
        rePwd = findViewById(R.id.rePwd);
        back = findViewById(R.id.back);

        userSP = getSharedPreferences("userSP", Context.MODE_APPEND);



        resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n,p,re;
                n = name.getText().toString();
                p = pwd.getText().toString();
                re = rePwd.getText().toString();
                if ("".equals(n)) {
                    ToastUtil.showLong(ResignActivity.this,"用户名不能为空");
                } else if ("".equals(p)) {
                    ToastUtil.showLong(ResignActivity.this,"密码不能为空");
                } else if ("".equals(re)) {
                    ToastUtil.showLong(ResignActivity.this,"请输入确认密码");
                } else if (userSP.contains(n)) {
                    ToastUtil.showShort(ResignActivity.this,"用户名已存在，请输入新的用户名");
                } else if (p.equals(re)) {
                    editor = userSP.edit();
                    editor.putString(n,p);
                    editor.commit();
                    ToastUtil.showShort(ResignActivity.this,"注册成功");
                    finish();
                } else ToastUtil.showShort(ResignActivity.this,"两次输入的密码不一致");
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
