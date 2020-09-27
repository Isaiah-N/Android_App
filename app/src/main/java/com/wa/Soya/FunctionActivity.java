package com.wa.Soya;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wa.Soya.ui.ToastUtil;

public class FunctionActivity extends AppCompatActivity{
    private Button btn1,btn2,btn3,btn4,btn5;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }

        btn1 = findViewById(R.id.record);
        btn2 = findViewById(R.id.find);
        btn3 = findViewById(R.id.rePwd);
        btn4 = findViewById(R.id.logout);
        btn5 = findViewById(R.id.resign);

        Intent intent= getIntent();
        final String uid = intent.getStringExtra("uid");
        if (uid.equals("admin")) btn5.setVisibility(View.VISIBLE);
//数据登记
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,RecordActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

//数据查找
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,QueryActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

//更改密码
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FunctionActivity.this,ChangepwdActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);

            }
        });

//退出登录
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(FunctionActivity.this)
                        .setMessage("您确定要退出本系统吗？").setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //ToDo: 你想做的事情
                                ToastUtil.showShort(FunctionActivity.this,"成功退出");
                                finish();
                                System.exit(0);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //ToDo: 取消时想做的事
                                dialogInterface.dismiss();
                            }
                        }).setNeutralButton("切换用户", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(FunctionActivity.this,LoginActivity.class));
                                finish();
                            }
                        });
                builder.create().show();
            }
        });

//新用户注册
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FunctionActivity.this,ResignActivity.class));
            }
        });
    }


}
