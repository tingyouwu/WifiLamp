package com.wcolorpicker.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * 选择颜色值的调色板。
 * @author wuzhen
 */
public class WCircleColorPicker extends View {

    //把HSV的内容转化成color,其中alpha设置成0xff,hsv有三个成员，hsv[0]的范围是[0,360),表示色彩，hsv[1]范围[0,1]表示饱和度，
    // hsv[2]范围[0,1]表示值，如果它们的值超出范围，那么它们会被截断成范围内的值
    private final float[] colorHsv = {0f, 0f, 1f};

    private int innerPadding = 0;
    private int lastSelectedColor;//最近选择的颜色
    private float circleRadius;//圆半径
    private int[] pixels;

    private Rect rect;
    private Bitmap bitmap;
    boolean init;

    private int old_red,old_green,old_blue;

    private Paint mCirclePaint = new Paint();

    private IOnColorSelectedListener mOnColorSelectedListener;

    private IOnColorChangeListener mOnColorChangeListener;

    public WCircleColorPicker(Context context) {
        super(context);
        init();
    }

    public WCircleColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setAntiAlias(true);
        setColor(Color.HSVToColor(colorHsv));
    }

    /**
     * 设置当前选中的颜色。
     *
     * @param color 颜色值
     */
    public void setColor(int color) {
        if (mOnColorSelectedListener != null) {
            mOnColorSelectedListener.onColorSelected(color, lastSelectedColor);
        }
        lastSelectedColor = color;

        Color.colorToHSV(color, colorHsv);
        invalidate();
    }

    /**
     * 设置颜色选中的监听事件。
     *
     * @param listener 监听事件
     */
    public void setOnColorSelectedListener(IOnColorSelectedListener listener) {
        this.mOnColorSelectedListener = listener;
    }

    /**
     * 设置颜色选中的监听事件。
     *
     * @param listener 监听事件
     */
    public void setOnColorChangedListener(IOnColorChangeListener listener) {
        this.mOnColorChangeListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, null, rect, null);
            // TODO 防止边缘锯齿的更好方法
            Drawable drawable = getBackground();
            if (drawable instanceof ColorDrawable) {
                int color = ((ColorDrawable) drawable).getColor();
                mCirclePaint.setColor(color);
                canvas.drawCircle(getWidth() / 2.f, getHeight() / 2.f, getWidth() / 2.f - innerPadding - 1, mCirclePaint); //画出圆环
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(init)return;
        init = true;
        rect = new Rect(innerPadding, innerPadding, w - innerPadding, h - innerPadding);
        bitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        circleRadius = Math.min(rect.width(), rect.height()) / 2;
        pixels = new int[rect.width() * rect.height()];
        createBitmap();
    }

    private void createBitmap() {
        int width = rect.width();
        int height = rect.height();
        int[] tempPixels = new int[width * height];

        float[] hsv = new float[]{0f, 0f, 1f};

        int x = (int) -circleRadius, y = (int) -circleRadius;
        for (int i = 0; i < tempPixels.length; i++) {
            if (i % width == 0) {
                x = (int) -circleRadius;
                y++;
            } else {
                x++;
            }

            double centerDist = Math.sqrt(x * x + y * y);
            if (centerDist <= circleRadius) {
                hsv[0] = (float) (Math.atan2(y, x) / Math.PI * 180f) + 180;
                hsv[1] = (float) (centerDist / circleRadius);
                tempPixels[i] = Color.HSVToColor(255, hsv);
            } else {
                tempPixels[i] = 0x00000000;
            }
        }

        for (x = 0; x < width; x++) {
            for (y = 0; y < height; y++) {
                pixels[x * height + y] = tempPixels[x * height + y];
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;
        width = height = Math.min(maxWidth, maxHeight);

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            if(!isOutOfCircle((int) event.getX(),(int) event.getY())){
                int newColor = getColorForPoint((int) event.getX(), (int) event.getY(), colorHsv);
                if (mOnColorSelectedListener != null) {
                    mOnColorSelectedListener.onColorSelected(newColor, lastSelectedColor);
                }

                if (mOnColorChangeListener != null){
                    int red = getRed(newColor);
                    int green = getGreen(newColor);
                    int blue = getBlue(newColor);

                    if(Math.abs(red - old_red)<=3 && Math.abs(green-old_green)<=3 && Math.abs(blue-old_blue)<=3)return true;

                    old_red = red;
                    old_blue =blue;
                    old_green = green;

                    mOnColorChangeListener.onColorSelected(red,green,blue);
                }
                lastSelectedColor = newColor;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    private int getColorForPoint(int x, int y, float[] hsv) {
        x -= circleRadius;
        y -= circleRadius;
        double centerDist = Math.sqrt(x * x + y * y);
        //色彩
        hsv[0] = (float) (Math.atan2(y, x) / Math.PI * 180f) + 180;
        hsv[1] = Math.max(0f, Math.min(1f, (float) (centerDist / circleRadius)));
        return Color.HSVToColor(255, hsv);
    }

    private boolean isOutOfCircle(int x, int y){
        x -= circleRadius;
        y -= circleRadius;
        double centerDist = Math.sqrt(x * x + y * y);
        if(centerDist>circleRadius)
            return true;
        return false;
    }

    private int getBlue(int color){
        return Color.blue(color);
    }

    private int getRed(int color){
        return Color.red(color);
    }

    private int getGreen(int color){
        return Color.green(color);
    }
}
