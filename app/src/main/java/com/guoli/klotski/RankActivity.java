package com.guoli.klotski;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class RankActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SQLiteDatabase DB = SQLiteDatabase.openDatabase(getFilesDir() + "/rank.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        String createSql = "CREATE TABLE IF NOT EXISTS user_rank(_id INTEGER primary key autoincrement, username STRING NOT NULL, steps INTEGER NOT NULL)";
        DB.execSQL(createSql);

//        String insertSql = "insert into user_rank(username, steps) values(?,?)";
//        DB.execSQL(insertSql, new String[]{"tuonima", "123"});
//        DB.execSQL(insertSql, new String[]{"nimabi", "2"});
//        DB.execSQL(insertSql, new String[]{"asdfasdf", "23"});

        String selectSql = "select _id, username, steps from user_rank order by steps";
        Cursor cursor = DB.rawQuery(selectSql, null);
        String rank_string = "排名\t姓名\t步数\n";
        int rank = 1;
        while (cursor.moveToNext()) {
            String user_id = cursor.getString(cursor.getColumnIndex("_id"));
            String user_name = cursor.getString(cursor.getColumnIndex("username"));
            String user_steps = cursor.getString(cursor.getColumnIndex("steps"));
            Log.d("SQL Debug", user_id + "," + user_name + "," + user_steps);
            rank_string += rank + "\t" + user_name + "\t" + user_steps + '\n';
            rank++;
        }
        Log.d("Ranking Debug", rank_string);


        cursor.close();
        DB.close();

        setContentView(R.layout.activity_rank);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView t = (TextView) findViewById(R.id.rank_content);
        t.setText(rank_string);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(RankActivity.this, MainActivity.class);
                startActivity(intent);
                RankActivity.this.finish();
            }
        });


    }
}
