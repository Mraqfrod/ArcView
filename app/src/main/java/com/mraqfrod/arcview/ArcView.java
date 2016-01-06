package com.mraqfrod.arcview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/1/5 0005.
 */
public class ArcView extends View {

    private float mTemperature = 24f;
    
    private Paint mArcPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;

    private float mMinSize;
    private float mGapWidth;
    private float mInnerRadius;
    private float mCenter;
    private float mRadius;

    private RectF mArcRect ;
    private SweepGradient mSweepGradient;//扫描渲染 类似雷达扫描


    public ArcView(Context context) {
        super(context, null);
    }

    public ArcView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mArcPaint = new Paint();
        Log.i("aaa","初始化 " +mArcPaint);
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
//        Log.i("aaa","widthMode = "+widthMode);
//        Log.i("aaa", "widthSize = " + widthSize);
//        Log.i("aaa", "heightMode = " + heightMode);
//        Log.i("aaa","heightSize = "+heightSize);

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


        mMinSize = width / 600f ;

        int size = width;

        initSize();


        if(heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else if(heightMode == MeasureSpec.AT_MOST){
            height = Math.min(width,heightSize);
        }else {
            height = size;
        }

        setMeasuredDimension(width, height);

    }

    private void initSize() {
        // mMinSize 缩小比例
       mGapWidth = 56 * mMinSize;//内圆弧到外圆弧的长度
        mInnerRadius = 180 * mMinSize;//分割线到中心的长度
        mCenter = 300 * mMinSize ; //圆弧的中心
        mRadius = 208 * mMinSize; // 圆弧中心到 外圆弧半径
     //   Log.i("aaa"," mMinSize = "+mMinSize);
     //   Log.i("aaa","left ="+(mCenter -mRadius)+" top ="+ (mCenter-mRadius)+"right ="+(mCenter+mRadius)+" bootm ="+(mCenter+mRadius));
        //圆弧外轮廓矩形区域
        mArcRect = new RectF(mCenter -mRadius,mCenter-mRadius,mCenter+mRadius,mCenter+mRadius);
        int[] colors = {0xFFE5BD7D,0xFFFAAA64,
                0xFFFFFFFF, 0xFF6AE2FD,
                0xFF8CD0E5, 0xFFA3CBCB,
                0xFFBDC7B3, 0xFFD1C299, 0xFFE5BD7D };

        float [] positions = {0,1f/8,2f/8,3f/8,4f/8,5f/8,6f/8,7f/8,1};
        // 4个参数： 渲染中心x ，y 坐标， 颜色数组，相对位置颜色渐变，若为null颜色 沿渐变线 均匀分布
        mSweepGradient =new SweepGradient(mCenter,mCenter,colors,positions);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw arc
        Log.i("aaa", " mArc = " + mArcPaint);
        mArcPaint.setStrokeWidth(mGapWidth);//设置空心线宽
        int gapDegree =getDegree();
        //Shader类 专门用来渲染图像以及一些几何图形。
        mArcPaint.setShader(mSweepGradient);
        //外轮廓矩形区域，起始角度，扫过的角度，是否包括圆心，画笔
        //画进度 弧
        canvas.drawArc(mArcRect, -225, gapDegree + 225, false, mArcPaint);
        //画背景 弧
        mArcPaint.setShader(null);
        mArcPaint.setColor(Color.WHITE);
        canvas.drawArc(mArcRect, gapDegree, 45 - gapDegree, false, mArcPaint);

        //画 分割线
        mLinePaint.setStrokeWidth(mMinSize * 1.5f);
        // 一直在相同坐标画直线  canvas 在转动
        for(int i =0;i<120;i++){
            //判断是否在圆弧里 在外面就不画  135 - 225度之间在外面
            if(i<=45 || i>= 75 ){
                float top = mCenter - mInnerRadius - mGapWidth;
                if(i%15==0){
                    top =top - 20*mMinSize;
                }
        Log.i("aaa", "left =" + (mCenter) + " top =" + (mCenter - mInnerRadius) + "right =" + (mCenter) + " bootm =" +top);
                canvas.drawLine(mCenter, mCenter - mInnerRadius, mCenter, top, mLinePaint);
            }
            //每次旋转3度 120*3 正好一圈
            canvas.rotate(3,mCenter,mCenter);
        }
        //写字
        mTextPaint.setTextSize(mMinSize*30);
        //居中对齐
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        // 16度 到28度
            for(int i =16 ; i<29;i+=2){

            }

    }
        // 温度的角度
    private int getDegree(){
        checkTemperature(mTemperature);
        return -225 +(int)((mTemperature-16)/12*90+0.5f)*3;
    }
    //检查温度是否超过区间
    private void checkTemperature(float t){
        if(t<16 || t>28 ){
            throw new RuntimeException("Temperature out of range");
        }
    }

    public void setTemperature (float t){
        checkTemperature(t);
        mTemperature = t ;
        invalidate();
    }
}
