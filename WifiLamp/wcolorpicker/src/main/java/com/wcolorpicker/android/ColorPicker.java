package com.wcolorpicker.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


public class ColorPicker extends ImageView{

    private Bitmap bp;//色轮图片
    private int bw, bh;//色轮图片的尺寸
    private float x, y, radio;
    private OnColorSelectListener onColorSelectListener;
    private Paint paint;
    private int old_red,old_green,old_blue;

    public ColorPicker(Context context) {
        this(context, null);
    }

    public ColorPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        bp = BitmapFactory.decodeResource(context.getResources(), R.drawable.circle);

        bw = bp.getWidth();
        bh = bp.getHeight();

        setImageBitmap(bp);

        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);

        setClickable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xx = event.getX();
        float yy = event.getY();
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            if (inCircle(xx, yy)) {
                if (onColorSelectListener != null) {
                    int color = getColor(xx, yy);
                    int red= Color.red(color);
                    int green=Color.green(color);
                    int blue=Color.blue(color);
                    if(Math.abs(red - old_red)<=3 && Math.abs(green-old_green)<=3 && Math.abs(blue-old_blue)<=3)return true;
                    old_red = red;
                    old_blue =blue;
                    old_green = green;
                    onColorSelectListener.onColorSelect(getColor(xx, yy));
                }
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        x = getWidth() / 2;
        y = getHeight() / 2;
        radio = x > y ? y : x;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPoint(x, y, paint);
    }

    private int getColor(float x, float y) {

        this.x = x;
        this.y = y;

        int w = getWidth();
        int h = getHeight();

        int dx = (int) ((x / w) * bw);
        int dy = (int) ((y / h) * bh);

        int color = Color.BLACK;

        try {
            color = bp.getPixel(dx, dy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return color;
    }

    private boolean inCircle(float x, float y) {
        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        float d = (float) Math.abs(Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy)));
        return d <= radio;
    }

    public static interface OnColorSelectListener {
        public void onColorSelect(int color);
    }

    public OnColorSelectListener getOnColorSelectListener() {
        return onColorSelectListener;
    }

    public void setOnColorSelectListener(OnColorSelectListener onColorSelectListener) {
        this.onColorSelectListener = onColorSelectListener;
    }

    /**
     * 回收Bitmap内存
     */
    public void recycle() {
        if (bp != null) {
            if (!bp.isRecycled()) {
                bp.recycle();
            }
            bp = null;
        }
    }

    public boolean isRecycled(){
        return bp == null || bp.isRecycled();
    }

}
