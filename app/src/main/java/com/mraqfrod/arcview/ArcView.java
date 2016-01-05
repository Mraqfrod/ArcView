package com.mraqfrod.arcview;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class ArcView extends View {

    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;




    public ArcView(Context context) {
        super(context,null);
    }

    public ArcView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public ArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mArcPaint = new Paint();
        mArcPaint.setStyle(Paint.Style.STROKE);  //画笔空心
        mArcPaint.setAntiAlias(true);//抗齿距

        mLinePaint =new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(0xffdddddd);

        mTextPaint =new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(0xff64646f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desireWidth = Integer.MAX_VALUE;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize =MeasureSpec.getSize(heightMeasureSpec);
        Log.i("aaa","widthMode = "+widthMode);
        Log.i("aaa","widthSize = "+widthSize);
        Log.i("aaa","heightMode = "+heightMode);
        Log.i("aaa","heightSize = "+heightSize);
        int width ;
        int height ;
        // EX和 AT 可以直接用测量值
        if(widthMode == MeasureSpec.EXACTLY){ // 对应match_parent
            width = widthSize;
        }else if(widthMode == MeasureSpec.AT_MOST){//对应wrap_content
            width = Math.min(desireWidth,widthSize);
        }else {
            //未知大小 直接用最大值
            width = desireWidth;
        }

        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else if(heightMode == MeasureSpec.AT_MOST){
            height = Math.min(desireWidth,heightSize);
        }else {
            height = width;
        }

        setMeasuredDimension(width,height);

    }
}
