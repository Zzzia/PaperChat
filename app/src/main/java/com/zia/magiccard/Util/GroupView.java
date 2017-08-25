package com.zia.magiccard.Util;

/**
 * Created by zia on 17-8-25.
 */
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.zia.magiccard.R;

import java.util.Arrays;
import java.util.List;

public class GroupView extends android.support.v7.widget.AppCompatImageView{

    /** 图片之间的距离 */
    private  int padding = 2;
    /** 圆角值 */
    private  int cornor = 0;

    private  int width,height;;
    /** 头像模式 圆的 */
    public static final int FACETYPE_CIRCLE = 1;
    /** 头像模式 方的 最多9个 */
    public static final int FACETYPE_SQUARE = 2;

    private Context mContext;

    private int mViewType;//默认圆形
    private Bitmap[] mBitmaps ;
    private int background;
    public GroupView(Context context,Bitmap[] bitmaps) {
        super(context);
        this.mBitmaps = bitmaps;
    }

    public GroupView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GroupView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GroupView_type:
                    mViewType = a.getInt(R.styleable.GroupView_type, FACETYPE_CIRCLE);
                    break;
                case R.styleable.GroupView_cornor:
                    cornor = (int) a.getDimension(R.styleable.GroupView_cornor,0);
                    break;
                case R.styleable.GroupView_padding:
                    padding = (int) a.getDimension(R.styleable.GroupView_padding,3);
                case R.styleable.GroupView_backgroundGP:
                    background = a.getColor(R.styleable.GroupView_backgroundGP,Color.parseColor("#DDDFD4"));
                    break;
                default:
                    break;
            }
        }

        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec){
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        } else {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        return width;
    }

    private int measureHeight(int heightMeasureSpec){
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        return height;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        if(mBitmaps == null) return;
        Paint paint = new Paint();
        canvas.drawBitmap(createGroupFace(mViewType, mContext, mBitmaps),0,0,paint);
    }

    public void setImageBitmaps(Bitmap[] bitmaps) {
        mBitmaps = bitmaps;
    }

    public void setImageBitmaps(List<Bitmap> bitmaps) {
        if(bitmaps == null) return;
        mBitmaps = bitmaps.toArray(new Bitmap[bitmaps.size()]);
    }

    public  Bitmap createGroupFace(int type, Context context,
                                   Bitmap[] bitmapArray) {
        System.out.println("hiwhitley"+"type"+type);
        if (type == FACETYPE_CIRCLE) {
            return createGroupBitCircle(bitmapArray, context);
        }
        return createTogetherBit(bitmapArray, context);
    }

    private static Bitmap scaleBitmap(float paramFloat, Bitmap paramBitmap) {
        Matrix localMatrix = new Matrix();
        localMatrix.postScale(paramFloat, paramFloat);
        return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(),
                paramBitmap.getHeight(), localMatrix, true);
    }

    /**
     * 拼接群头像 圆形版的
     *
     * @param bitmapArray
     * @param context
     * @return
     */
    private  Bitmap createGroupBitCircle(Bitmap[] bitmapArray,
                                         Context context) {
        if (bitmapArray.length < 1 && bitmapArray.length > 9) {
            return null;
        }
        // 先取一个获取宽和高
        Bitmap tempBitmap = (Bitmap) bitmapArray[0];
        if (tempBitmap == null) {
            return null;
        }
        // 画布的宽
        int tempWidth = width;
        // 画布的高
        int tempHeight = height;
        Bitmap canvasBitmap = Bitmap.createBitmap(tempWidth, tempHeight,
                Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(canvasBitmap);
        localCanvas.drawColor(background);
        JoinBitmaps.join(localCanvas, Math.min(tempWidth, tempHeight),
                Arrays.asList(bitmapArray));
        return canvasBitmap;
    }

    /**
     * 拼接群头像
     *
     * @param paramList
     * @param context
     * @return 头像本地路径
     */
    @SuppressWarnings("unused")
    private  Bitmap createTogetherBit(Bitmap[] paramList,
                                      final Context context) {
        if (paramList.length < 1 && paramList.length > 9) {
            return null;
        }
        // 先取一个获取宽和高
        Bitmap tempBitmap = (Bitmap) paramList[0];
        if (tempBitmap == null) {
            return null;
        }
        // 画布的宽
        int tempWidth = width;
        // 画布的高
        int tempHeight = height;
        // 创建一个空格的bitmap
        Bitmap canvasBitmap = Bitmap.createBitmap(tempWidth, tempHeight,
                Bitmap.Config.ARGB_8888);
        // 头像的数量
        int bitmapCount = paramList.length;
        Canvas localCanvas = new Canvas(canvasBitmap);
        localCanvas.drawColor(background);
        int colum = 0;

        if (bitmapCount > 1 && bitmapCount < 5) {
            colum = 2;
        } else if (bitmapCount > 4 && bitmapCount < 10) {
            colum = 3;
        } else {
            colum = 1;
        }
        float scale = 1.0F / colum;
        // 根据列数缩小
        Bitmap scaledBitmap = scaleBitmap(scale, tempBitmap);
        if (padding > 0) {
            padding = dip2px(context, padding);
            // 如果有内边距 再次缩小
            float paddingScale = (float) (tempWidth - (colum + 1) * padding)
                    / colum / scaledBitmap.getWidth();
            scaledBitmap = scaleBitmap(paddingScale, scaledBitmap);
            scale = scale * paddingScale;
        }
        // 第一行的 头像个数
        int topRowCount = bitmapCount % colum;
        // 满行的行数
        int rowCount = bitmapCount / colum;
        if (topRowCount > 0) {
            // 如果第一行头像个数大于零 行数加1
            rowCount++;
        } else if (topRowCount == 0) {
            // 6 或者 9 第一行头像个数和列数一致
            topRowCount = colum;
        }
        // 缩小后头像的宽
        int scaledWidth = scaledBitmap.getWidth();
        // 缩小后头像的高
        int scaledHeight = scaledBitmap.getHeight();
        // 第一个头像与画布顶部的距离
        int firstTop = ((tempHeight - (rowCount * scaledHeight + (rowCount + 1)
                * padding)) / 2)
                + padding;
        // 第一个头像与画布左部的距离
        int firstLeft = ((tempWidth - (topRowCount * scaledWidth + (topRowCount + 1)
                * padding)) / 2)
                + padding;
        for (int i = 0; i < paramList.length; i++) {
            if (i == 9) {// 达到上限 停止
                break;
            }
            // 按照最终压缩比例压缩
            Bitmap bit = scaleBitmap(scale, (Bitmap) paramList[i]);
            if (cornor > 0) {
                // 圆角化
                bit = GetRoundedCornerBitmap(bit);
            }
            localCanvas.drawBitmap(bit, firstLeft, firstTop, null);
            firstLeft += (scaledWidth + padding);
            if (i == topRowCount - 1 | tempWidth - firstLeft < scaledWidth) {
                firstTop += (scaledHeight + padding);
                firstLeft = padding;
            }
            bit.recycle();
        }
        // 重置padding
        //padding = 2;
        localCanvas.save(Canvas.ALL_SAVE_FLAG);
        localCanvas.restore();
        return canvasBitmap;
    }

    /**
     * 圆角
     *
     * @param bitmap
     * @return
     */
    private  Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight()));
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(Color.BLACK);
            canvas.drawRoundRect(rectF, cornor, cornor, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),
                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        } catch (Exception e) {
            return bitmap;
        }
    }

    private  int dip2px(Context context, float value) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value, context.getResources().getDisplayMetrics()) + 0.5f);
    }

}

class JoinBitmaps {

    public static final void join(Canvas canvas, int dimension, List<Bitmap> bitmaps) {
        if (bitmaps == null)
            return;
        int count = Math.min(bitmaps.size(), JoinLayout.max());
        float[] size = JoinLayout.size(count);
        join(canvas, dimension, bitmaps, count, size);
    }

    public static final void join(Canvas canvas, int dimension, List<Bitmap> bitmaps, int count,
                                  float[] size) {
        join(canvas, dimension, bitmaps, count, size, 0.15f);
    }

    public static final void join(Canvas canvas, int dimension, List<Bitmap> bitmaps,
                                  float gapSize) {
        if (bitmaps == null)
            return;
        int count = Math.min(bitmaps.size(), JoinLayout.max());
        float[] size = JoinLayout.size(count);
        join(canvas, dimension, bitmaps, count, size, gapSize);
    }

    public static final void join(Canvas canvas, int dimension, List<Bitmap> bitmaps, int count,
                                  float[] size, float gapSize) {
        if (bitmaps == null)
            return;
        // 旋转角度
        float[] rotation = JoinLayout.rotation(count);
        // paint
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Matrix matrixJoin = new Matrix();
        // scale as join size
        matrixJoin.postScale(size[0], size[0]);

        canvas.save();
        // canvas.drawColor(Color.RED);
        // index<count bitmaps.size 可能会越界
        for (int index = 0; index < count; index++) {
            Bitmap bitmap = bitmaps.get(index);

            // MATRIX
            Matrix matrix = new Matrix();
            // scale as destination
            matrix.postScale((float) dimension / bitmap.getWidth(),
                    (float) dimension / bitmap.getHeight());

            canvas.save();

            matrix.postConcat(matrixJoin);

            float[] offset = JoinLayout.offset(count, index, dimension, size);
            canvas.translate(offset[0], offset[1]);

            // 缩放
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            // 裁剪
            Bitmap bitmapOk = createMaskBitmap(newBitmap, newBitmap.getWidth(),
                    newBitmap.getHeight(), (int) rotation[index], gapSize);

            canvas.drawBitmap(bitmapOk, 0, 0, paint);
            canvas.restore();
        }

        canvas.restore();
    }

    public static final Bitmap createMaskBitmap(Bitmap bitmap, int viewBoxW, int viewBoxH,
                                                int rotation, float gapSize) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);// 抗锯齿
        paint.setFilterBitmap(true);
        int center = Math.round(viewBoxW / 2f);
        canvas.drawCircle(center, center, center, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        if (rotation != 360) {
            Matrix matrix = new Matrix();
            // 根据原图的中心位置旋转
            matrix.setRotate(rotation, viewBoxW / 2, viewBoxH / 2);
            canvas.setMatrix(matrix);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawCircle(viewBoxW * (1.5f - gapSize), center, center, paint);
        }
        return output;
    }

    public static final Bitmap createBitmap(int width, int height, List<Bitmap> bitmaps) {
        int count = Math.min(bitmaps.size(), JoinLayout.max());
        float[] size = JoinLayout.size(count);
        return createBitmap(width, height, bitmaps, count, size, 0.15f);
    }

    public static final Bitmap createBitmap(int width, int height, List<Bitmap> bitmaps,
                                            int count, float[] size) {
        return createBitmap(width, height, bitmaps, count, size, 0.15f);
    }

    public static final Bitmap createBitmap(int width, int height, List<Bitmap> bitmaps,
                                            int count, float[] size, float gapSize) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int dimen = Math.min(width, height);
        join(canvas, dimen, bitmaps, count, size, gapSize);
        return output;
    }

}

class JoinLayout {

    public static final String TAG = JoinLayout.class.getSimpleName();

    public static int max() {
        return 5;
    }

    private static final float[][] rotations = { new float[] { 360.0f }, new float[] { 45.0f, 360.0f },
            new float[] { 120.0f, 0.0f, -120.0f }, new float[] { 90.0f, 180.0f, -90.0f, 0.0f },
            new float[] { 144.0f, 72.0f, 0.0f, -72.0f, -144.0f }, };

    public static float[] rotation(int count) {
        return count > 0 && count <= rotations.length ? rotations[count - 1] : null;
    }

    private static final float[][] sizes = { new float[] { 0.9f, 0.9f },
            new float[] { 0.5f, 0.65f }, new float[] { 0.45f, 0.8f },
            new float[] { 0.45f, 0.91f }, new float[] { 0.38f, 0.80f } };

    public static float[] size(int count) {
        return count > 0 && count <= sizes.length ? sizes[count - 1] : null;
    }

    public static float[] offset(int count, int index, float dimension, float[] size) {
        switch (count) {
            case 1:
                return offset1(index, dimension, size);
            case 2:
                return offset2(index, dimension, size);
            case 3:
                return offset3(index, dimension, size);
            case 4:
                return offset4(index, dimension, size);
            case 5:
                return offset5(index, dimension, size);
            default:
                break;
        }
        return new float[] { 0f, 0f };
    }

    /**
     * 5个头像
     *
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    private static float[] offset5(int index, float dimension, float[] size) {
        // 圆的直径
        float cd = (float) dimension * size[0];
        // 边距
        float s1 = -cd * size[1];

        float x1 = 0;
        float y1 = s1;

        float x2 = (float) (s1 * Math.cos(19 * Math.PI / 180));
        float y2 = (float) (s1 * Math.sin(18 * Math.PI / 180));

        float x3 = (float) (s1 * Math.cos(54 * Math.PI / 180));
        float y3 = (float) (-s1 * Math.sin(54 * Math.PI / 180));

        float x4 = (float) (-s1 * Math.cos(54 * Math.PI / 180));
        float y4 = (float) (-s1 * Math.sin(54 * Math.PI / 180));

        float x5 = (float) (-s1 * Math.cos(19 * Math.PI / 180));
        float y5 = (float) (s1 * Math.sin(18 * Math.PI / 180));

        // Log.d(TAG, "x1:" + x1 + "/y1:" + y1);
        // Log.d(TAG, "x2:" + x2 + "/y2:" + y2);
        // Log.d(TAG, "x3:" + x3 + "/y3:" + y3);
        // Log.d(TAG, "x4:" + x4 + "/y4:" + y4);
        // Log.d(TAG, "x5:" + x5 + "/y5:" + y5);

        // 居中 Y轴偏移量
        float xx1 = (dimension - cd - y3 - s1) / 2;
        // 居中 X轴偏移量
        float xxc1 = (dimension - cd) / 2;
        // xx1 = xxc1 = -s1;
        // xx1 = xxc1 = 0;
        switch (index) {
            case 0:
                // return new float[] { s1 + xxc1, xx1 };
                return new float[] { x1 + xxc1, y1 + xx1 };
            case 1:
                return new float[] { x2 + xxc1, y2 + xx1 };
            case 2:
                return new float[] { x3 + xxc1, y3 + xx1 };
            case 3:
                return new float[] { x4 + xxc1, y4 + xx1 };
            case 4:
                return new float[] { x5 + xxc1, y5 + xx1 };
            default:
                break;
        }
        return new float[] { 0f, 0f };
    }

    /**
     * 4个头像
     *
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    private static float[] offset4(int index, float dimension, float[] size) {
        // 圆的直径
        float cd = (float) dimension * size[0];
        // 边距
        float s1 = cd * size[1];

        float x1 = 0;
        float y1 = 0;

        float x2 = s1;
        float y2 = y1;

        float x3 = s1;
        float y3 = s1;

        float x4 = x1;
        float y4 = y3;

        // Log.d(TAG, "x1:" + x1 + "/y1:" + y1);
        // Log.d(TAG, "x2:" + x2 + "/y2:" + y2);
        // Log.d(TAG, "x3:" + x3 + "/y3:" + y3);
        // Log.d(TAG, "x4:" + x4 + "/y4:" + y4);

        // 居中 X轴偏移量
        float xx1 = (dimension - cd - s1) / 2;
        switch (index) {
            case 0:
                return new float[] { x1 + xx1, y1 + xx1 };
            case 1:
                return new float[] { x2 + xx1, y2 + xx1 };
            case 2:
                return new float[] { x3 + xx1, y3 + xx1 };
            case 3:
                return new float[] { x4 + xx1, y4 + xx1 };
            default:
                break;
        }
        return new float[] { 0f, 0f };
    }

    /**
     * 3个头像
     *
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    private static float[] offset3(int index, float dimension, float[] size) {
        // 圆的直径
        float cd = (float) dimension * size[0];
        // 边距
        float s1 = cd * size[1];
        // 第二个圆的 Y坐标
        float y2 = s1 * (3 / 2);
        // 第二个圆的 X坐标
        float x2 = s1 - y2 / 1.73205f;
        // 第三个圆的 X坐标
        float x3 = s1 * 2 - x2;
        // 居中 Y轴偏移量
        float xx1 = (dimension - cd - y2) / 2;
        // 居中 X轴偏移量
        float xxc1 = (dimension - cd) / 2 - s1;
        // xx1 = xxc1 = 0;
        switch (index) {
            case 0:
                return new float[] { s1 + xxc1, xx1 };
            case 1:
                return new float[] { x2 + xxc1, y2 + xx1 };
            case 2:
                return new float[] { x3 + xxc1, y2 + xx1 };
            default:
                break;
        }
        return new float[] { 0f, 0f };
    }

    /**
     * 2个头像
     *
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    private static float[] offset2(int index, float dimension, float[] size) {
        // 圆的直径
        float cd = (float) dimension * size[0];
        // 边距
        float s1 = cd * size[1];

        float x1 = 0;
        float y1 = 0;

        float x2 = s1;
        float y2 = s1;

        // Log.d(TAG, "x1:" + x1 + "/y1:" + y1);
        // Log.d(TAG, "x2:" + x2 + "/y2:" + y2);

        // 居中 X轴偏移量
        float xx1 = (dimension - cd - s1) / 2;
        switch (index) {
            case 0:
                return new float[] { x1 + xx1, y1 + xx1 };
            case 1:
                return new float[] { x2 + xx1, y2 + xx1 };
            default:
                break;
        }
        return new float[] { 0f, 0f };
    }

    /**
     * 1个头像
     *
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    private static float[] offset1(int index, float dimension, float[] size) {
        // 圆的直径
        float cd = (float) dimension * size[0];
        float offset = (dimension - cd) / 2;
        return new float[] { offset, offset };
    }
}
