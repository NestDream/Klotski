package com.guoli.klotski;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//最后工作：添加玩法，多个地图选择；记录最高成绩；胜利后弹出提示框
//美化游戏主界面
//写好报告，开源github（6月5日）
//解决在不同的型号手机上会出现的bug
public class GameActivity extends AppCompatActivity {
    Button Qz[] = new Button[10];//总共10个棋子
    int BG[][] = new int[5][4];//总共五行四列
    TextView txt1;//用于显示文字的TextView，目前用于显示步数
    float SW;
    float x1, x2, y1, y2;
    int Step = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaming);

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

        txt1 = (TextView) findViewById(R.id.Text1);
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
        txt1.post(new Runnable() {
            @Override
            public void run() {
                txt1.setText("Screen Width:" + txt1.getWidth() + "; Qz Width" + Qz[1].getWidth());
                SW = txt1.getWidth();
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
        txt1.post(new Runnable() {
            @Override
            public void run() {
                txt1.setText("Screen Width:" + txt1.getWidth() + "; Qz Width" + Qz[1].getWidth());
                SW = txt1.getWidth();
                init();
            }
        });
    }

    //监听实现
    public class mTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int type; // 1 兵    2  张飞  3 关羽 4 曹操
            int r, c;
            if (v.getWidth() == v.getHeight()) {
                if (v.getHeight() > 300)
                    type = 4;//大的是曹操
                else
                    type = 1;//小的是小兵

            } else {
                if (v.getHeight() > v.getWidth())
                    type = 2;//高大于宽是竖的
                else
                    type = 3;//宽大于高是横的
            }

            r = (int) (v.getY() / 270f);//这里可能出问题
            c = (int) (v.getX() / 270f);
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
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 2:
                            if (r > 0 && BG[r - 1][c] == 0) {
                                SetPos(v, r - 1, c);
                                BG[r - 1][c] = 1;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 3:
                            if (r > 0 && BG[r - 1][c] == 0 && BG[r - 1][c + 1] == 0) {
                                SetPos(v, r - 1, c);
                                BG[r - 1][c] = BG[r - 1][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 4:
                            if (r > 0 && BG[r - 1][c] == 0 && BG[r - 1][c + 1] == 0) {
                                SetPos(v, r - 1, c);
                                BG[r - 1][c] = BG[r - 1][c + 1] = 1;
                                BG[r + 1][c] = BG[r + 1][c + 1] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
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
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 2:
                            if (r < 3 && BG[r + 2][c] == 0) {
                                SetPos(v, r + 1, c);
                                BG[r + 2][c] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }

                            break;
                        case 3:
                            if (r < 4 && BG[r + 1][c] == 0 && BG[r + 1][c + 1] == 0) {
                                SetPos(v, r + 1, c);
                                BG[r + 1][c] = BG[r + 1][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 4:
                            if (r < 3 && BG[r + 2][c] == 0 && BG[r + 2][c + 1] == 0) {
                                SetPos(v, r + 1, c);
                                BG[r + 2][c] = BG[r + 2][c + 1] = 1;
                                BG[r][c] = BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                                if (r + 1 == 3 && c == 1) {
                                    Log.d("onWinning", "From up to down");
                                    win();
//                                    txt1.setText("你赢了！共用" + Step + "步！");
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
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 2:
                            if (c > 0 && BG[r][c - 1] == 0 && BG[r + 1][c - 1] == 0) {
                                SetPos(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r + 1][c - 1] = 1;
                                BG[r][c] = 0;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 3:
                            if (c > 0 && BG[r][c - 1] == 0) {
                                SetPos(v, r, c - 1);
                                BG[r][c - 1] = 1;
                                BG[r][c + 1] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 4:
                            if (c > 0 && BG[r][c - 1] == 0 && BG[r + 1][c - 1] == 0) {
                                SetPos(v, r, c - 1);
                                BG[r][c - 1] = BG[r + 1][c - 1] = 1;
                                BG[r][c + 1] = BG[r + 1][c + 1] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
//                                if (r + 1 == 3 && c == 1) {
                                if (r == 3 && c == 2) {
                                    Log.d("onWinning", "From right to left");
                                    win();
//                                    txt1.setText("你赢了，共用" + Step + "步！");
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
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 2:
                            if (c < 3 & BG[r][c + 1] == 0 && BG[r + 1][c + 1] == 0) {
                                SetPos(v, r, c + 1);
                                BG[r][c + 1] = 1;
                                BG[r + 1][c + 1] = 1;
                                BG[r][c] = 0;
                                BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 3:
                            if (c < 2 && BG[r][c + 2] == 0) {
                                SetPos(v, r, c + 1);
                                BG[r][c + 2] = 1;
                                BG[r][c] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
                            }
                            break;
                        case 4:
                            if (c < 2 && BG[r][c + 2] == 0 && BG[r + 1][c + 2] == 0) {
                                SetPos(v, r, c + 1);
                                BG[r][c + 2] = BG[r + 1][c + 2] = 1;
                                BG[r][c] = BG[r + 1][c] = 0;
                                Step++;
                                txt1.setText("你已经走了：" + Step + "步！");
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
    }


    public void win() {
        //todo:记录最佳成绩
        txt1.setText("你赢了！共用" + Step + "步！");

        AlertDialog.Builder alertdialogbuilder =
                new AlertDialog.Builder(GameActivity.this);
        alertdialogbuilder.setTitle("胜利！");
        alertdialogbuilder.setMessage("恭喜你获得胜利！总步数为：" + Step);
        alertdialogbuilder.setPositiveButton("返回主界面", checkout);

        AlertDialog alertdialog = alertdialogbuilder.create();
        alertdialog.show();
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
        v.setWidth(w * dip2px(getApplicationContext(),SW/4));//(240);
        v.setHeight(h * dip2px(getApplicationContext(), SW / 4));
        v.setText(txt);
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
        //txt1.setText("SW：" +dip2px(getApplicationContext(), SW)+","+getApplicationContext().getResources().getDisplayMetrics().density);
    }


}

