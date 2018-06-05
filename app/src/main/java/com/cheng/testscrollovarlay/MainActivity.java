package com.cheng.testscrollovarlay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ScrollLayout mScrollLayout;//滑动抽屉
    private View bottom_ll_content;//底部布局
    private TextView text_view_1;//底部默认显示的布局
    private TextView text_view_2;//底部布局最后一个控件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mScrollLayout = findViewById(R.id.scroll_down_layout);
        bottom_ll_content = findViewById(R.id.bottom_ll_content);
        text_view_1 = findViewById(R.id.text_view_1);
        text_view_2 = findViewById(R.id.text_view_2);
        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollLayout.scrollToExit();
            }
        });
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new ListviewAdapter(this));
    }

    private float curProgress = 0;

    private ScrollLayout.OnScrollChangedListener mOnScrollChangedListener = new ScrollLayout.OnScrollChangedListener() {
        @Override
        public void onScrollProgressChanged(float currentProgress) {
            if (currentProgress >= -0.5) {
                mScrollLayout.getBackground().setAlpha(50);
            }else{
                mScrollLayout.getBackground().setAlpha(0);
            }
            curProgress = currentProgress;
        }
        @Override
        public void onScrollFinished(ScrollLayout.Status currentStatus) {
            if(currentStatus==ScrollLayout.Status.CLOSED && curProgress<0 && curProgress>-0.85){
                mScrollLayout.setToOpen();
            }
        }
        @Override
        public void onChildScroll(int top) {

        }
    };

    boolean initScrollLayout = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!initScrollLayout){
            initScrollLayout = true;
            int total_height = AppUtils.getScreenDispaly(this)[1];
            int bottom_height = bottom_ll_content.getHeight();
            int visi_height = text_view_1.getHeight();
            Log.i("111", "total_height = " + total_height);
            Log.i("111", "bottom_height = " + bottom_height);
            Log.i("111", "visi_height = " + visi_height);
            if(total_height > bottom_height + AbViewUtil.dip2px(this, 50)){
                Log.i("111", "total_height > bottom_height + AbViewUtil.dip2px(this, 50) --- ");
                mScrollLayout.setMinOffset(total_height - bottom_height + visi_height);
                mScrollLayout.setMaxOffset(bottom_height);
            }else{
                Log.i("111", "total_height <= bottom_height + AbViewUtil.dip2px(this, 50) --- ");
                mScrollLayout.setMinOffset(AbViewUtil.dip2px(this, 50));
                mScrollLayout.setMaxOffset((int)(total_height * 0.5));
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) text_view_2.getLayoutParams();
                layoutParams.bottomMargin = visi_height;
                text_view_2.setLayoutParams(layoutParams);
            }
            mScrollLayout.setExitOffset(visi_height);
            mScrollLayout.setIsSupportExit(true);
            mScrollLayout.setAllowHorizontalScroll(true);
            mScrollLayout.setOnScrollChangedListener(mOnScrollChangedListener);
            mScrollLayout.setToExit();
            mScrollLayout.getBackground().setAlpha(0);
        }
    }

}