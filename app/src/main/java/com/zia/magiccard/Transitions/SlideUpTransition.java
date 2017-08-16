package com.zia.magiccard.Transitions;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zia on 17-8-16.
 */

public class SlideUpTransition extends Visibility {

    private View view;
    private Context context;
    private static final String key = "view";
    private static final String TAG = "SlideUpTransition";

    public SlideUpTransition(Context context,View view){
        this.view = view;
        this.context = context;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        view.measure(0,0);
        int transY = view.getMeasuredHeight();
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view
                .getLayoutParams();
        int marginTop = lp.topMargin;
        int marginBottom = lp.bottomMargin;
        Log.d(TAG,"y:"+transY+"marginTop:"+marginTop+"marginBottom"+marginBottom);
        transitionValues.values.put(key,transY + marginTop + marginBottom);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        super.captureEndValues(transitionValues);
        transitionValues.values.put(key,0);
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {
        if (null == startValues || null == endValues) {
            return null;
        }
        if(view == this.view){
            int startTransY = (int) startValues.values.get(key);
            int endTransY = (int) endValues.values.get(key);
            if(startTransY != endTransY){
                final ValueAnimator animator = ValueAnimator.ofInt(startTransY, endTransY);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        Object value = animator.getAnimatedValue();
                        if(value != null){
                            view.setTranslationY((Integer) value);
                        }
                    }
                });
                return animator;
            }
        }
        return null;

    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues, TransitionValues endValues) {
        if (null == startValues || null == endValues) {
            return null;
        }
        if (view == this.view) {
            int startTransY = (int) endValues.values.get(key);
            int endTransY = (int) startValues.values.get(key);

            if (startTransY != endTransY) {
                ValueAnimator animator = ValueAnimator.ofInt(startTransY, endTransY);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Object value = animation.getAnimatedValue();
                        if (null != value) {
                            view.setTranslationY((Integer) value);
                        }
                    }
                });
                return animator;
            }
        }
        return null;
    }
}
