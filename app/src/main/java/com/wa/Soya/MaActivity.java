package com.wa.Soya;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.wa.Soya.sqlite.DbHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaActivity extends AppCompatActivity {
    private Button back;
    private SimpleAdapter sa; // 是android中一个列表适配器，做一些简单的列表适配
    private ListView lv;
    private List<Map<String, Object>> messageList2 = new ArrayList<Map<String, Object>>();
    private String sql = "SELECT * FROM Soya";

    private static final String PATH = Environment.getExternalStorageDirectory() + "/SoyaPic/";
    private Button ok;//确定按钮
    private TextView date, ps, loc;
    private String rdate, rps, rloc, rpic;
    private ImageView pic;
    private Context context = MaActivity.this;
    private Dialog dia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ma);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        lv = findViewById(R.id.lv);
        back = findViewById(R.id.back);


        dia = new Dialog(context, R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.activity_result);
        date = dia.findViewById(R.id.rdate);
        ps = dia.findViewById(R.id.rps);
        loc = dia.findViewById(R.id.rloc);
        ok = dia.findViewById(R.id.ok);
        pic = (ImageView) dia.findViewById(R.id.rpic);




        dia.setCanceledOnTouchOutside(false); // Sets whether this dialog is
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 40;
        dia.onWindowAttributesChanged(lp);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.dismiss();
            }
        });

//获取筛选模式与内容
        Intent intent = getIntent();
        int flag = intent.getIntExtra("mod", 3);
        String ct = intent.getStringExtra("ct");

        switch (flag) {
            case 0:
                sql = "SELECT * FROM Soya";
                break;
            case 1:
                sql = "SELECT * FROM Soya WHERE name LIKE '%" + ct + "%' ORDER BY id ASC";
                break;
            case 2:
                sql = "SELECT * FROM Soya WHERE age" + ct + " ORDER BY age ASC";
                break;
            case 3:
                sql = "SELECT * FROM Soya WHERE height" + ct + " ORDER BY height ASC";
                break;
            case 4:
                sql = "SELECT * FROM Soya WHERE date LIKE '%" + ct + "%' ORDER BY id ASC";
                break;

        }
        final DbHelper mySql = new DbHelper(this, "Soya", null, 1);
        SQLiteDatabase db = mySql.getWritableDatabase();

        Cursor c = db.rawQuery(sql, null);


        while (c.moveToNext()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getInt(c.getColumnIndex("id")));
            map.put("name", c.getString(c.getColumnIndex("name")));
            map.put("age", c.getInt(c.getColumnIndex("age")));
            map.put("height", c.getString(c.getColumnIndex("height")));
            map.put("date", c.getString(c.getColumnIndex("date")));
            map.put("loc", c.getString(c.getColumnIndex("loc")));
            map.put("ps", c.getString(c.getColumnIndex("ps")));
            map.put("pic", c.getString(c.getColumnIndex("pic")));
            messageList2.add(map);
        }
        c.close();

        sa = new SimpleAdapter(this, messageList2, R.layout.listview_item_layout, new String[]{"name", "height", "age", "date", "ps"},// from 从来来
                new int[]{R.id.user, R.id.height, R.id.age, R.id.date, R.id.ps}// to 到那里去
        );
        lv.setAdapter(sa);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = messageList2.get(position);
                rdate = "采集日期："+(String) map.get("date");
                rps = "　　"+(String) map.get("ps");
                rloc = "采集位置："+(String) map.get("loc");
                rpic = PATH + (String) map.get("pic");

                File mFile=new File(rpic);
                date.setText(rdate);
                ps.setText(rps);
                loc.setText(rloc);

                if (mFile.exists()) {
                    Bitmap bitmap= BitmapFactory.decodeFile(rpic);
                    pic.setImageBitmap(bitmap);
                } else pic.setImageResource(R.drawable.ic_wujiedu);

                dia.show();
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
