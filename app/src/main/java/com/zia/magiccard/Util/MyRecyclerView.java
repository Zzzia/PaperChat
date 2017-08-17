package com.zia.magiccard.Util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;
import android.widget.TextView;

import com.zia.magiccard.R;

/**
 * Created by zia on 17-8-17.
 */

public class MyRecyclerView extends RecyclerView {

    private static final String TAG = "MyCard";
    private int mDeleteBtnState = 0;//0：关闭，1：将要关闭，2：将要打开，3：打开
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private boolean isItemMoving = false,isStartScroll = false,isDragging = false;
    private int mLastX,mLastY,mPosition,mMaxLength;
    private View mItemLayout;
    private TextView mDelete;
    private MyListener mListener;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
    }

    public interface MyListener{
        void onItemClick(View view,int position);
        void onDeleteClick(int position);
    }

    public void setMyListener(MyListener listener){
        this.mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mDeleteBtnState == 0) {
                    View view = findChildViewUnder(x, y);
                    if (view == null) {
                        return false;
                    }

                    ViewHolder viewHolder = getChildViewHolder(view);

                    mItemLayout = viewHolder.itemView;
                    mPosition = viewHolder.getAdapterPosition();

                    mDelete = (TextView) mItemLayout.findViewById(R.id.item_message_delete);
                    mMaxLength = mDelete.getWidth();
                    mDelete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.onDeleteClick(mPosition);
                            mItemLayout.scrollTo(0, 0);
                            mDeleteBtnState = 0;
                        }
                    });
                } else if (mDeleteBtnState == 3) {
                    mScroller.startScroll(mItemLayout.getScrollX(), 0, -mMaxLength, 0, 200);
                    invalidate();
                    mDeleteBtnState = 0;
                    return false;
                } else {
                    return false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                int dy = mLastY - y;

                int scrollX = mItemLayout.getScrollX();
                if (Math.abs(dx) > Math.abs(dy)) {
                    isItemMoving = true;
                    if (scrollX + dx <= 0) {//左边界检测
                        mItemLayout.scrollTo(0, 0);
                        return true;
                    } else if (scrollX + dx >= mMaxLength) {//右边界检测
                        mItemLayout.scrollTo(mMaxLength, 0);
                        return true;
                    }
                    mItemLayout.scrollBy(dx, 0);//item跟随手指滑动
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isItemMoving && !isDragging && mListener != null) {
                    mListener.onItemClick(mItemLayout, mPosition);
                }
                isItemMoving = false;

                mVelocityTracker.computeCurrentVelocity(1000);//计算手指滑动的速度
                float xVelocity = mVelocityTracker.getXVelocity();//水平方向速度（向左为负）
                float yVelocity = mVelocityTracker.getYVelocity();//垂直方向速度

                int deltaX = 0;
                int upScrollX = mItemLayout.getScrollX();

                if (Math.abs(xVelocity) > 100 && Math.abs(xVelocity) > Math.abs(yVelocity)) {
                    if (xVelocity <= -100) {//左滑速度大于100，则删除按钮显示
                        deltaX = mMaxLength - upScrollX;
                        mDeleteBtnState = 2;
                    } else if (xVelocity > 100) {//右滑速度大于100，则删除按钮隐藏
                        deltaX = -upScrollX;
                        mDeleteBtnState = 1;
                    }
                } else {
                    if (upScrollX >= mMaxLength / 2) {//item的左滑动距离大于删除按钮宽度的一半，则则显示删除按钮
                        deltaX = mMaxLength - upScrollX;
                        mDeleteBtnState = 2;
                    } else if (upScrollX < mMaxLength / 2) {//否则隐藏
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
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(e);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mItemLayout.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
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
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        isDragging = state == SCROLL_STATE_DRAGGING;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(e);
    }
}
