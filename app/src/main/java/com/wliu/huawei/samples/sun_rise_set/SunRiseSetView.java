package com.wliu.huawei.samples.sun_rise_set;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.wliu.huawei.samples.Util;

import java.util.Calendar;

/**
 * Created by wliu on 10/07/2018.
 */

public class SunRiseSetView extends View {
    private Paint mp = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint sunPaint = new Paint((Paint.ANTI_ALIAS_FLAG));
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // margin left / right of the base line
    private int STD_PADDING = Util.getPixelByDp(this.getContext(), 20);

    // screen width & height
    private int SCREEN_WIDTH, SCREEN_HEIGHT = 0;

    // The base line params
    private int lineStartX, lineY, lineEndX;

    // The arc angle offset, if it is 0, then it is half circle
    // here sets it as 10, then it will be smaller than half circle
    private float angle = 10;

    // The angle during the move, being used to position the sun
    // it is an offset to the `angle`
    private int sunAngle = 0;

    // The target angle of the sun movement, it should be calculated
    // by current time, here is a hard code
//    private float targetSunAngle = 0;

    // The radius of the sun
    private int sunRadius = 30;

    // The color of the sun and its sunshine
    private int sunColor = Color.parseColor("#FFD947");

    // The width of the base line
    private int bottomLineWidth = Util.getPixelByDp(this.getContext(), 2);

    // The start time and end time of the sun
    private String sunriseTimeStr = "凌晨4:55", sunsetTimeStr = "晚上7:44", sunriseSunset = "日出日落";

    // The text size of sunrise/sunset time
    private int timeTextSize = Util.getPixelBySp(this.getContext(), 14);

    // The gap between base line and the time text
    private int timeTextOffsetTop = Util.getPixelByDp(this.getContext(), 10);

    // The animator object for the sun movement
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "sunAngle", 0, 0);

    public void animateSun(float angle) {
        animator.end();
        animator.setIntValues((int) angle);
        animator.setupEndValues();
        animator.setStartDelay(100);
        animator.setDuration((int) (angle/ 90 * 2000));
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    public void setAngle(int angle) {
        this.angle = angle;
        this.invalidate();
    }

    /**
     * Sets the sun angle, will be called by object animator
     * @param angle
     */
    private void setSunAngle(int angle) {
        this.sunAngle = angle;
        this.invalidate();
    }

    /**
     * Sets sunrise time str
     *
     * @param sunriseTimeStr
     */
    public void setSunriseTimeStr(String sunriseTimeStr) {
        this.sunriseTimeStr = sunriseTimeStr;
    }

    /**
     * Sets sunset time str
     * @param sunsetTimeStr
     */
    public void setSunsetTimeStr(String sunsetTimeStr) {
        this.sunsetTimeStr = sunsetTimeStr;
    }

    public SunRiseSetView(Context context) {
        this(context, null);
    }

    public SunRiseSetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SunRiseSetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SunRiseSetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        SCREEN_WIDTH = Util.getScreenWidthAndHeight(this.getContext())[0];

        SCREEN_HEIGHT = Util.getScreenWidthAndHeight(this.getContext())[1];

        lineStartX = STD_PADDING;
        lineEndX = SCREEN_WIDTH - STD_PADDING;
        lineY = SCREEN_HEIGHT / 3;


        mp.setStyle(Paint.Style.STROKE);
        mp.setStrokeWidth(bottomLineWidth);
        mp.setColor(Color.WHITE);

        PathEffect pathEffect = new DashPathEffect(new float[]{20, 15}, 0);
        arcPaint.setPathEffect(pathEffect);
        arcPaint.setColor(Color.WHITE);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(bottomLineWidth);

        sunPaint.setColor(sunColor);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(timeTextSize);

//        animator.setStartDelay(100);
//        animator.setDuration((int) (targetSunAngle / 90 * 2000));
//        animator.setInterpolator(new LinearInterpolator());
//        animator.setRepeatMode(ValueAnimator.RESTART);
//        animator.setRepeatCount(ValueAnimator.INFINITE);
//        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, SCREEN_HEIGHT / 2);
    }

    /**
     * Draw text on the specified position specified by px and py, with offset top to py.
     *
     * @param text
     * @param px
     * @param py
     * @param offsetTop
     * @param canvas
     */
    private void drawText(String text, int px, int py, int offsetTop, Canvas canvas) {
        Rect textBounds = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), textBounds);
        int textHeight = textBounds.height();
        int textWidth = textBounds.width();

        int textY = py + offsetTop + textHeight;
        if (offsetTop < 0) {
            textY = py + offsetTop;
        }
        int textX = px - textWidth / 2;
        canvas.drawText(text, textX, textY, textPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Sun move path base line
        canvas.drawLine(lineStartX, lineY, lineEndX, lineY, mp);


        // Calculations
        int circleMargin = Util.getPixelByDp(this.getContext(), 60);
        int centerX = SCREEN_WIDTH / 2;
        int radius = centerX - circleMargin;
        int cx = centerX;
        double offsetAngle = (angle / 180) * Math.PI;
        double offsetY = Math.sin(offsetAngle) * radius;
        int cy = lineY + (int) offsetY;

        // The cross point of sun move path and the base line
        double circleCrossStartX = centerX - Math.cos(offsetAngle) * radius;
        double circleCrossEndX = SCREEN_WIDTH - circleCrossStartX;

        // Draw start time str
        drawText(sunriseTimeStr, (int) circleCrossStartX, lineY, timeTextOffsetTop, canvas);
        // Draw end time str
        drawText(sunsetTimeStr, (int) circleCrossEndX, lineY, timeTextOffsetTop, canvas);
        // Draw sunrise sunset str
        drawText(sunriseSunset, cx, lineY, -timeTextOffsetTop, canvas);

        // Draw sun and sunshine
        sunPaint.setStrokeWidth(bottomLineWidth);
        double sunOffsetAngle = (sunAngle + angle) / 180 * Math.PI;
        double sunOffsetX = Math.cos(sunOffsetAngle) * radius;
        double sunOffsetY = Math.sin(sunOffsetAngle) * radius;
        int sunX = cx - (int) sunOffsetX;
        int sunY = cy - (int) sunOffsetY;
        canvas.save();
        canvas.clipRect(0, 0, lineEndX, lineY);
        canvas.rotate(sunAngle + angle, sunX, sunY);
        int sunShineCount = 8;
        for (int i = 0; i < sunShineCount; i++) {
            float sunshineAngle = 360 / sunShineCount * i;
            int sunshineRadiusStart = sunRadius + 5;
            int sunshineRadiusEnd = sunshineRadiusStart + 20;

            double sunshineOffsetAngle = sunshineAngle / 180 * Math.PI;
            double ssOffsetX = Math.cos(sunshineOffsetAngle) * sunshineRadiusStart;
            double ssOffsetY = Math.sin(sunshineOffsetAngle) * sunshineRadiusStart;
            int sunshineStartX = sunX - (int) ssOffsetX;
            int sunshineStartY = sunY - (int) ssOffsetY;

            ssOffsetX = Math.cos(sunshineOffsetAngle) * sunshineRadiusEnd;
            ssOffsetY = Math.sin(sunshineOffsetAngle) * sunshineRadiusEnd;
            int sunshineEndX = sunX - (int) ssOffsetX;
            int sunshineEndY = sunY - (int) ssOffsetY;
            canvas.drawLine(sunshineStartX, sunshineStartY, sunshineEndX, sunshineEndY, sunPaint);
        }

        canvas.drawCircle(sunX, sunY, sunRadius, sunPaint);
        canvas.restore();

        // Draw pass over move path
        arcPaint.setColor(sunColor);
        canvas.save();
        canvas.clipRect(0, 0, sunX, lineY);
        canvas.drawCircle(cx, cy, radius, arcPaint);
        canvas.restore();

        // Draw sun move path
        arcPaint.setColor(Color.WHITE);
        canvas.save();
        canvas.clipRect(sunX, 0, lineEndX, lineY);
        canvas.drawCircle(cx, cy, radius, arcPaint);
        canvas.restore();
    }
}
