package com.zia.magiccard.Util;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.zia.magiccard.R;

/**
 * Created by zia on 17-8-15.
 */

public class MyCard extends CardView {

    private static final String TAG = "MyCard";
    private int lastX,lastY;//最后一次按下的坐标
    private int widgetsWidth = 0;//所有空间总共的宽度
    private int mDeleteBtnState = 0;//0：关闭，1：将要关闭，2：将要打开，3：打开
    private TextView mDelete;
    private Scroller mScroller;
    private boolean isItemMoving = false,isStartScroll = false;
    private VelocityTracker mVelocityTracker;

    public MyCard(Context context) {
        super(context);
    }

    public MyCard(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        mVelocityTracker.addMovement(e);
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG,"MotionEvent.ACTION_DOWN");
                if (mDeleteBtnState == 0) {
//                    mDelete = findViewById(R.id.item_delete);
//                    mDelete.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            scrollTo(0, 0);
//                            mDeleteBtnState = 0;
//                        }
//                    });
                } else if (mDeleteBtnState == 3) {
                    mScroller.startScroll(getScrollX(), 0, -(widgetsWidth-getWidth()), 0, 200);
                    invalidate();
                    mDeleteBtnState = 0;
                    return false;
                } else {
                    return false;
                }
                return true;

            case MotionEvent.ACTION_UP:
                Log.d(TAG,"MotionEvent.ACTION_UP");
//                if (!isItemMoving && !isDragging && mListener != null) {
//                    mListener.onItemClick(mItemLayout, mPosition);
//                }
                isItemMoving = false;
                mVelocityTracker.computeCurrentVelocity(1000);//计算手指滑动的速度
                float xVelocity = mVelocityTracker.getXVelocity();//水平方向速度（向左为负）
                float yVelocity = mVelocityTracker.getYVelocity();//垂直方向速度

                int deltaX = 0;
                int upScrollX = getScrollX();

                if (Math.abs(xVelocity) > 100 && Math.abs(xVelocity) > Math.abs(yVelocity)) {
                    if (xVelocity <= -100) {//左滑速度大于100，则删除按钮显示
                        deltaX = widgetsWidth - getWidth() - upScrollX;
                        mDeleteBtnState = 2;
                    } else if (xVelocity > 100) {//右滑速度大于100，则删除按钮隐藏
                        deltaX = -upScrollX;
                        mDeleteBtnState = 1;
                    }
                } else {
                    if (upScrollX >= (widgetsWidth - getWidth()) / 2) {//item的左滑动距离大于删除按钮宽度的一半，则则显示删除按钮
                        deltaX = widgetsWidth - getWidth() - upScrollX;
                        mDeleteBtnState = 2;
                    } else if (upScrollX < (widgetsWidth - getWidth()) / 2) {//否则隐藏
                        deltaX = -upScrollX;
                        mDeleteBtnState = 1;
                    }
                }

                //item自动滑动到指定位置
                mScroller.startScroll(upScrollX, 0, deltaX, 0, 200);
                isStartScroll = true;
                invalidate();

                mVelocityTracker.clear();
                break;

            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"MotionEvent.ACTION_MOVE");
                int dx = lastX - x;
                int dy = lastY - y;

                int scrollX = getScrollX();
                if (Math.abs(dx) > Math.abs(dy)) {
                    isItemMoving = true;
                    if (scrollX + dx <= 0) {//左边界检测
                        scrollTo(0, 0);
                        return true;
                    } else if (scrollX + dx >= widgetsWidth-getWidth()) {//右边界检测
                        scrollTo(widgetsWidth-getWidth(), 0);
                        return true;
                    }
                    this.scrollBy(dx, 0);//item跟随手指滑动
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else if (isStartScroll) {
            isStartScroll = false;
            if (mDeleteBtnState == 1) {
                mDeleteBtnState = 0;
            }
            if (mDeleteBtnState == 2) {
                mDeleteBtnState = 3;
            }
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //final int width = getWidth();
        final int height = getHeight();
        int currentWidth = 0;
        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            //int childHeight = child.getMeasuredHeight();
            int left = currentWidth + lp.leftMargin;
            int top = lp.topMargin;
            int right = currentWidth + childWidth + lp.rightMargin;
            int bottom = height - lp.bottomMargin;
            child.layout(left,top,right,bottom);
            currentWidth = currentWidth + childWidth + lp.leftMargin + lp.rightMargin;
        }
        widgetsWidth = currentWidth;
    }
}
