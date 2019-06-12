package com.guoli.klotski;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class GameActivity extends AppCompatActivity {
    Button Qz[] = new Button[10];//总共10个棋子
    int BG[][] = new int[5][4];//总共五行四列
    TextView gaming_text;//用于显示文字的TextView，目前用于显示步数
    float SW;
    float x1, x2, y1, y2;
    int Step = 0;
    float BingSize = 0;//小兵的大小，判断是哪种type的时候会用到
    SQLiteDatabase DB;
    String insertSql = "insert into user_rank(username, steps) values(?,?)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

        DB = SQLiteDatabase.openDatabase(getFilesDir() + "/rank.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);


        Qz[0] = (Button) findViewById(R.id.Qz1);
        Qz[1] = (Button) findViewById(R.id.Qz2);
        Qz[2] = (Button) findViewById(R.id.Qz3);
        Qz[3] = (Button) findViewById(R.id.Qz4);
        Qz[4] = (Button) findViewById(R.id.Qz5);
        Qz[5] = (Button) findViewById(R.id.Qz6);
        Qz[6] = (Button) findViewById(R.id.Qz7);
        Qz[7] = (Button) findViewById(R.id.Qz8);
        Qz[8] = (Button) findViewById(R.id.Qz9);
        Qz[9] = (Button) findViewById(R.id.Qz10);

        gaming_text = (TextView) findViewById(R.id.GamingText);
        //注册监听器
        for (int i = 0; i < 10; i++)
            Qz[i].setOnTouchListener(new mTouch());
        //背景数组对应填充 1-possessed 0-available
        //在这里可以修改游戏地图
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++)
                BG[i][j] = 1;
        BG[4][1] = 0;
        BG[4][2] = 0;
        //输出屏幕宽度和
        gaming_text.post(new Runnable() {
            @Override
            public void run() {
                gaming_text.setText("Screen Width:" + gaming_text.getWidth() + "; Qz Width" + Qz[1].getWidth());
                gaming_text.setText("欢迎来到剪纸华容道");
                SW = gaming_text.getWidth();
                init();
            }
        });

    }

    public void reset(View view) {
        //背景数组对应填充
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 4; j++)
                BG[i][j] = 1;
        BG[4][1] = 0;
        BG[4][2] = 0;
        //输出屏幕宽度和
        Step = 0;//步数归零
        gaming_text.post(new Runnable() {
            @Override
            public void run() {
//                gaming_text.setText("Screen Width:" + gaming_text.getWidth() + "; Qz Width" + Qz[1].getWidth());
                gaming_text.setText("欢迎来到剪纸华容道");
                SW = gaming_text.getWidth();

                init();
            }
        });
        Snackbar.make(view, getString(R.string.reset_toast), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }

    //监听实现
    public class mTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int type;
            int r, c;
            if (v.getWidth() == v.getHeight()) {
                Log.d("HeightWidth", v.getHeight() + "," + v.getWidth());
                if (v.getHeight() > BingSize)
                    type = 4;//大的是曹操
                else
                    type = 1;//小的是小兵

            } else {
                if (v.getHeight() > v.getWidth())
                    type = 2;//高大于宽是竖的
                else
                    type = 3;//宽大于高是横的
            }

            r = (int) (v.getY() / Qz[1].getWidth());//这里可能出问题 270f
            c = (int) (v.getX() / Qz[1].getWidth());
            Log.d("different screen", "y:" + v.getY() + "");
            Log.d("different screen", "x:" + v.getX() + "");
            Log.d("different screen", "qz1,w:" + Qz[1].getWidth() + "");
            Log.d("different screen", "sw" + SW);

            Log.d("mTouch", "User is touching at(" + r + "," + c + ")");//touch的时候的行列，row column

            //继承了Activity的onTouchEvent方法，直接监听点击事件
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                x1 = event.getX();
                y1 = event.getY();
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                //当手指离开的时候
                x2 = event.getX();
                y2 = event.getY();
                if (y1 - y2 > 50) //"向上滑:"
                {
                    switch (type) {
                        case 1:
                            if (r > 0 && BG[r - 1][c] == 0) {
                                SetPos(v, r - 1, c);
                                BG[r - 1][c] = 1;
                                BG[r][c] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 2:
                            if (r > 0 && BG[r - 1][c] == 0) {
                                SetPos(v, r - 1, c);
                                BG[r - 1][c] = 1;
                                BG[r + 1][c] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 3:
                            if (r > 0 && BG[r - 1][c] == 0 && BG[r - 1][c + 1] == 0) {
                                SetPos(v, r - 1, c);
                                BG[r - 1][c] = BG[r - 1][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 4:
                            if (r > 0 && BG[r - 1][c] == 0 && BG[r - 1][c + 1] == 0) {
                                SetPos(v, r - 1, c);
                                BG[r - 1][c] = BG[r - 1][c + 1] = 1;
                                BG[r + 1][c] = BG[r + 1][c + 1] = 0;
                                incr_and_show_step();
                            }
                            break;

                    }

                } else if (y2 - y1 > 50) { //向下滑
                    switch (type) {
                        case 1:
                            if (r < 4 && BG[r + 1][c] == 0) {
                                SetPos(v, r + 1, c);
                                BG[r + 1][c] = 1;
                                BG[r][c] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 2:
                            if (r < 3 && BG[r + 2][c] == 0) {
                                SetPos(v, r + 1, c);
                                BG[r + 2][c] = 1;
                                BG[r][c] = 0;
                                incr_and_show_step();
                            }

                            break;
                        case 3:
                            if (r < 4 && BG[r + 1][c] == 0 && BG[r + 1][c + 1] == 0) {
                                SetPos(v, r + 1, c);
                                BG[r + 1][c] = BG[r + 1][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 4:
                            if (r < 3 && BG[r + 2][c] == 0 && BG[r + 2][c + 1] == 0) {
                                SetPos(v, r + 1, c);
                                BG[r + 2][c] = BG[r + 2][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                incr_and_show_step();
                                if (r + 1 == 3 && c == 1) {
                                    Log.d("onWinning", "From up to down");
                                    win();
//                                    gaming_text.setText("你赢了！共用" + Step + "步！");
                                    //此处应该转到另一个activity，胜利用新的activity或者一个toast体现
                                    //记录用户时间和步数，如果top5，加入排行榜
                                    //注意析构
                                }
                            }
                            break;
                    }
                } else if (x1 - x2 > 50) //向左滑
                {
                    switch (type) {
                        case 1:
                            if (c > 0 && BG[r][c - 1] == 0) {
                                SetPos(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r][c] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 2:
                            if (c > 0 && BG[r][c - 1] == 0 && BG[r + 1][c - 1] == 0) {
                                SetPos(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r + 1][c - 1] = 1;
                                BG[r][c] = 0;
                                BG[r + 1][c] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 3:
                            if (c > 0 && BG[r][c - 1] == 0) {
                                SetPos(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r][c + 1] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 4:
                            if (c > 0 && BG[r][c - 1] == 0 && BG[r + 1][c - 1] == 0) {
                                SetPos(v, r, c - 1);
                                BG[r][c - 1] = BG[r + 1][c - 1] = 1;
                                BG[r][c + 1] = BG[r + 1][c + 1] = 0;
                                incr_and_show_step();
//                                if (r + 1 == 3 && c == 1) {
                                if (r == 3 && c == 2) {
                                    Log.d("onWinning", "From right to left");
                                    win();
//                                    gaming_text.setText("你赢了，共用" + Step + "步！");
                                }
                            }
                            break;
                    }
                } else if (x2 - x1 > 50) //向右滑
                {
                    switch (type) {
                        case 1:
                            if (c < 3 && BG[r][c + 1] == 0) {
                                SetPos(v, r, c + 1);
                                BG[r][c + 1] = 1;
                                BG[r][c] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 2:
                            if (c < 3 & BG[r][c + 1] == 0 && BG[r + 1][c + 1] == 0) {
                                SetPos(v, r, c + 1);
                                BG[r][c + 1] = 1;
                                BG[r + 1][c + 1] = 1;
                                BG[r][c] = 0;
                                BG[r + 1][c] = 0;
                                incr_and_show_step();
                            }
                            break;
                        case 3:
                            if (c < 2 && BG[r][c + 2] == 0) {
                                SetPos(v, r, c + 1);
                                BG[r][c + 2] = 1;
                                BG[r][c] = 0;

                            }
                            break;
                        case 4:
                            Log.d("onCase4", "c and r:(" + c + "," + r + "),and size of BG,BG[0]:" + BG.length + "," + BG[0].length);
                            if (c < 2 && BG[r][c + 2] == 0 && BG[r + 1][c + 2] == 0) {
                                SetPos(v, r, c + 1);
                                BG[r][c + 2] = BG[r + 1][c + 2] = 1;
                                BG[r][c] = BG[r + 1][c] = 0;
                                incr_and_show_step();
//                                if (r + 1 == 3 && c == 1) {
                                if (r == 3 && c == 0) {
//                                    游戏胜利
                                    Log.d("onWinning", "From left to right");
                                    win();
                                }
                            }
                            break;
                    }
                }
            }
            return true;
        }

        private void incr_and_show_step() {
            Step++;
            gaming_text.setText("当前步数：" + Step + "步");
        }
    }


    public void win() {
        gaming_text.setText("你赢了！共用" + Step + "步！");

        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setTitle("请输入您的名字：");
        builder.setMessage("恭喜你获得胜利！总步数为：" + Step);

        //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
        View view = LayoutInflater.from(GameActivity.this).inflate(R.layout.input_dialog, null);
        //    设置我们自己定义的布局文件作为弹出框的Content
        builder.setView(view);

        final EditText username = (EditText) view.findViewById(R.id.username);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = username.getText().toString().trim();
                //    将输入的用户名和密码打印出来
                String user_name = a;
                String str_step = Step + "";
                DB.execSQL(insertSql, new String[]{user_name, str_step});
                DB.close();
                Intent intent = new Intent(GameActivity.this, MainActivity.class);
                startActivity(intent);
                GameActivity.this.finish();
            }
        });
//            builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
//            {
//                @Override
//                public void onClick(DialogInterface dialog, int which)
//                {
//
//                }
//            });
        builder.show();


//        AlertDialog.Builder alertdialogbuilder =
//                new AlertDialog.Builder(GameActivity.this);
//        alertdialogbuilder.setTitle("胜利！");
//        alertdialogbuilder.setPositiveButton("返回主界面", checkout);
//
//        AlertDialog alertdialog = alertdialogbuilder.create();
//        alertdialog.show();


    }

    private DialogInterface.OnClickListener checkout = new DialogInterface.OnClickListener() {
        //胜利界面用于退出的Listener
        @Override

        public void onClick(DialogInterface arg0, int arg1) {
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            GameActivity.this.finish();
        }
    };

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    void SetSize(Button v, int w, int h, String txt) {
        int temp = (int) (SW / 4);

        String temp_w1 = "width" + v.getWidth();
        String temp_h1 = "height" + v.getHeight();
        Log.d("qz_init_before", v.getText() + temp_w1 + temp_h1 + "," + temp);

        v.getLayoutParams().width = 360;
//        v.setWidth(360);//(240);
        v.getLayoutParams().height = h * temp;
//                v.setHeight(h * temp);
//        v.setWidth(w * dip2px(getApplicationContext(), SW / 4));//(240);
//        v.setHeight(h * dip2px(getApplicationContext(), SW / 4));
        v.setText(txt);//用于调试
        v.setText("");
        String temp_w = "width" + v.getWidth();
        String temp_h = "height" + v.getHeight();
        Log.d("qz_init", v.getText() + temp_w + temp_h);
    }

    void SetPos(View v, int r, int c) {
        v.setX(c * SW / 4f);
        v.setY(r * SW / 4f);
    }

    void init() {
        //设置每个棋子的大小和位置
        SetSize(Qz[0], 1, 1, "兵");
        SetPos(Qz[0], 4, 0);
        SetSize(Qz[1], 1, 1, "兵");
        SetPos(Qz[1], 3, 1);
        SetSize(Qz[2], 1, 1, "兵");
        SetPos(Qz[2], 3, 2);
        SetSize(Qz[3], 1, 1, "兵");
        SetPos(Qz[3], 4, 3);

        SetSize(Qz[4], 1, 2, "张飞");
        SetPos(Qz[4], 0, 0);
        SetSize(Qz[5], 1, 2, "赵云");
        SetPos(Qz[5], 0, 3);
        SetSize(Qz[6], 1, 2, "马超");
        SetPos(Qz[6], 2, 0);
        SetSize(Qz[7], 1, 2, "黄忠");
        SetPos(Qz[7], 2, 3);

        SetSize(Qz[8], 2, 1, "关羽");
        SetPos(Qz[8], 2, 1);
        SetSize(Qz[9], 2, 2, "曹操");
        SetPos(Qz[9], 0, 1);
        //gaming_text.setText("SW：" +dip2px(getApplicationContext(), SW)+","+getApplicationContext().getResources().getDisplayMetrics().density);
        BingSize = Qz[0].getHeight();

    }


}

