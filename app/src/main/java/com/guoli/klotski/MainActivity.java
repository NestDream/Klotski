package com.guoli.klotski;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.guoli.klotski.R;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gaming_dialog(View view) {
        final String[] items = {"游戏模式1", "游戏模式2", "游戏模式3", "游戏模式4"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(MainActivity.this);
        listDialog.setTitle("选择游戏模式");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                // which 下标从0开始
                switch (which) {
                    case 0:
                        Intent intent_0 = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(intent_0);
                        MainActivity.this.finish();
                        break;
                    case 1:
                        Intent intent_1 = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(intent_1);
                        MainActivity.this.finish();
                        break;
                    case 2:
                        Intent intent_2 = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(intent_2);
                        MainActivity.this.finish();
                        break;
                    case 3:
                        Intent intent_3 = new Intent(MainActivity.this, GameActivity.class);
                        startActivity(intent_3);
                        MainActivity.this.finish();
                        break;
                }
//                // ...To-do
//                Toast.makeText(MainActivity.this,
//                        "你点击了" + items[which],
//                        Toast.LENGTH_SHORT).show();
            }
        });
        listDialog.show();
    }

    public void gameing_dialog2(View view) {
        //定义一个新的对话框对象
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        //设置对话框提示内容
        alertdialogbuilder.setMessage("选择哪张地图？");
        //定义对话框2个按钮标题及接受事件的函数
        alertdialogbuilder.setNeutralButton("游戏地图1", game_1);
        alertdialogbuilder.setNeutralButton("游戏地图2", game_2);
        //创建并显示对话框
        AlertDialog alertdialog1 = alertdialogbuilder.create();
        alertdialog1.show();

    }

    private DialogInterface.OnClickListener game_1 = new DialogInterface.OnClickListener() {
        //使用该标记是为了增强程序在编译时候的检查，如果该方法并不是一个覆盖父类的方法，在编译时编译器就会报告错误。
        @Override

        public void onClick(DialogInterface arg0, int arg1) {
            //当按钮click1被按下时执行结束进程
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    };
    private DialogInterface.OnClickListener game_2 = new DialogInterface.OnClickListener() {
        //使用该标记是为了增强程序在编译时候的检查，如果该方法并不是一个覆盖父类的方法，在编译时编译器就会报告错误。
        @Override

        public void onClick(DialogInterface arg0, int arg1) {
            //当按钮click1被按下时执行结束进程
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    };

    public void exit_dialog(View view) {
        //定义一个新的对话框对象
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        //设置对话框提示内容
        alertdialogbuilder.setMessage("确定要退出程序吗？");
        //定义对话框2个按钮标题及接受事件的函数
        alertdialogbuilder.setPositiveButton("确定", click_confirm);
        alertdialogbuilder.setNegativeButton("取消", click_cancel);
        //创建并显示对话框
        AlertDialog alertdialog1 = alertdialogbuilder.create();
        alertdialog1.show();

    }

    private DialogInterface.OnClickListener click_confirm = new DialogInterface.OnClickListener() {
        //使用该标记是为了增强程序在编译时候的检查，如果该方法并不是一个覆盖父类的方法，在编译时编译器就会报告错误。
        @Override

        public void onClick(DialogInterface arg0, int arg1) {
            //当按钮click1被按下时执行结束进程
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    };

    private DialogInterface.OnClickListener click_cancel = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            //当按钮click2被按下时则取消操作
            arg0.cancel();
        }
    };
}
