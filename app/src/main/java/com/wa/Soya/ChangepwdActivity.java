package com.wa.Soya;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wa.Soya.ui.ToastUtil;

public class ChangepwdActivity extends AppCompatActivity {


    private EditText oPwd,pwd,rePwd;
    private Button change,back;
    private SharedPreferences userSP;
    private SharedPreferences.Editor editor;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        oPwd = findViewById(R.id.oPwd);
        pwd = findViewById(R.id.pwd);
        rePwd = findViewById(R.id.rePwd);
        change = findViewById(R.id.change);
        back = findViewById(R.id.back);

        userSP = getSharedPreferences("userSP", Context.MODE_APPEND);
        editor = userSP.edit();

        Intent intent= getIntent();
        final String uid = intent.getStringExtra("uid");

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String o,p,re;
                o = oPwd.getText().toString();
                p = pwd.getText().toString();
                re = rePwd.getText().toString();
                if ("".equals(o)) {
                    ToastUtil.showLong(ChangepwdActivity.this,"请输入原密码");
                } else if ("".equals(p)) {
                    ToastUtil.showLong(ChangepwdActivity.this,"新密码不能为空");
                } else if ("".equals(re)) {
                    ToastUtil.showLong(ChangepwdActivity.this,"请输入确认密码");
                } else if (p.equals(re)) {
                    if (userSP.getString(uid,"").equals(o)){
                        editor.putString(uid,p);
                        editor.commit();
                        ToastUtil.showShort(ChangepwdActivity.this,"修改密码成功");
                        finish();
                    }else ToastUtil.showShort(ChangepwdActivity.this,"原密码有误，请重新输入");
                } else ToastUtil.showShort(ChangepwdActivity.this,"两次输入的密码不一致");
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
