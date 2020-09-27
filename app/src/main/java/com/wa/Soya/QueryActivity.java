package com.wa.Soya;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wa.Soya.ui.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class QueryActivity extends AppCompatActivity {

    String dbpath = "/data/data/com.wa.Soya/databases/"
            +"Soya";
    boolean success=copyFile(dbpath, Environment.getExternalStorageDirectory() + "/SoyaDb/"
            + "Soya.db");
    private SharedPreferences userSP;
    private Button out,query,back;
    private EditText et1;
    public static final int TYPE_1 = -1;
    public static final String TYPE_1_String = "1234567890.<>=";// 限制只能输入此字符串中的字符
    public static final String TYPE_2_String = "1234567890/ :";// 限制日期只能输入此字符串中的字符
    private TextView uu;
    private RadioGroup rdg1;
    private int flag = 0;
    private RadioButton rdb1,rdb2,rdb3;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        query = findViewById(R.id.query);
        back = findViewById(R.id.back);
        out = findViewById(R.id.out);
        rdg1 = findViewById(R.id.rdg1);
        et1 = findViewById(R.id.et1);
        uu = findViewById(R.id.uu);

        String key="";
        userSP = this.getApplicationContext().getSharedPreferences("userSP",Context.MODE_APPEND);
        Map<String,String> map = (Map<String, String>) userSP.getAll();
        Set<Map.Entry<String,String>> set = map.entrySet();
        Iterator<Map.Entry<String,String>> it = set.iterator();
        while (it.hasNext()){
            Map.Entry<String,String> entry = it.next();
            key += "   "+entry.getKey();
        }

        uu.setText("现有采集人:\n"+key);

        rdg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdb1){
                    flag = 1;
                    et1.setEnabled(true);
                    et1.setText("");
                    et1.clearComposingText();
                    et1.setInputType(InputType.TYPE_CLASS_TEXT);
                } else if (checkedId == R.id.rdb2){
                    flag = 2;
                    et1.setEnabled(true);
                    et1.setText("");
                    et1.setKeyListener(new myInputListener(TYPE_1, TYPE_1_String));
                } else if (checkedId == R.id.rdb3){
                    flag = 3;
                    et1.setEnabled(true);
                    et1.setText("");
                    et1.setKeyListener(new myInputListener(TYPE_1, TYPE_1_String));
                } else if (checkedId == R.id.rdb4){
                    flag = 4;
                    et1.setEnabled(true);
                    et1.setText("");
                    et1.setKeyListener(new myInputListener(TYPE_1, TYPE_2_String));
                } else if (checkedId == R.id.rdb0){
                    flag = 0;
                    et1.setText("");
                    et1.setEnabled(false);
                }
            }
        });
//查询
        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QueryActivity.this,MaActivity.class);
                intent.putExtra("ct",et1.getText().toString());
                intent.putExtra("mod",flag);
                startActivity(intent);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//导出数据库
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                out.setEnabled(false);
                File file = new File(Environment.getExternalStorageDirectory() + "/SoyaDb/");
                if (!file.exists()) {
                    /**  注意这里是 mkdirs()方法  可以创建多个文件夹 */
                    file.mkdirs();
                }
                if (success) ToastUtil.showShort(QueryActivity.this,"采集数据导出成功\n文件保存在根目录SoyaDb文件夹下");
                else ToastUtil.showLong(QueryActivity.this,"      数据库文件导出失败\n可能尚未录入，数据库为空");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        out.setEnabled(true);
                    }
                }, 1000);
            }
        });

    }



    //自定义输入监听器
    private class myInputListener extends NumberKeyListener {

        private int type;
        private String chars;

        public myInputListener(int type, String chars) {
            this.type = type;
            this.chars = chars;
        }

        @Override
        public int getInputType() {
            return type;
        }

        @Override
        protected char[] getAcceptedChars() {
            return chars.toCharArray();
        }

    }
    //复制数据库文件
    public static boolean copyFile(String source, String dest) {
        try {
            File f1 = new File(source);
            File f2 = new File(dest);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0)
                out.write(buf, 0, len);

            in.close();
            out.close();
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }


}
