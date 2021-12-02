package com.sconti.studentcontinent.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.sconti.studentcontinent.R;

public class SlideAnimationUtil {

    /**
     * Animates outdooricon view so that it slides in from the left of it's container.
     *
     * @param context
     * @param view
     */
    public static void slideInFromLeft(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.left_right);
    }

    /**
     * Animates outdooricon view so that it slides from its current position, out of view to the left.
     *
     * @param context
     * @param view
     */
    public static void slideOutToLeft(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.right_left);
    }

    /**
     * Animates outdooricon view so that it slides in the from the right of it's container.
     *
     * @param context
     * @param view
     */
    public static void slideInFromRight(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.right_left);
    }

    /**
     * Animates outdooricon view so that it slides from its current position, out of view to the right.
     *
     * @param context
     * @param view
     */
    public static void slideOutToRight(Context context, View view) {
        runSimpleAnimation(context, view, R.anim.enter);
    }

    /**
     * Runs outdooricon simple animation on outdooricon View with no extra parameters.
     *
     * @param context
     * @param view
     * @param animationId
     */
    private static void runSimpleAnimation(Context context, View view, int animationId) {
        view.startAnimation(AnimationUtils.loadAnimation(
                context, animationId
        ));
    }

}
