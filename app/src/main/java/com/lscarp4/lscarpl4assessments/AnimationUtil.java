package com.lscarp4.lscarpl4assessments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import androidx.recyclerview.widget.RecyclerView;

// This class plays a set of Animator objects in the specified order.
// Animations can be set up to play together, in sequence, or after a specified delay.

public class AnimationUtil {

    public static void animate(RecyclerView.ViewHolder holder, boolean goesDown){

        //Public Constructor
        AnimatorSet animatorSet = new AnimatorSet();

        //ObjectAnimator is a subclass of ValueAnimator that allows to set a target object and object property to animate
        ObjectAnimator animatorTranslateY = ObjectAnimator.ofFloat(holder.itemView,"translationY", goesDown == true ? 200:-200,0);
        animatorTranslateY.setDuration(1000);

        //Calling playTogether() to add a set of animations all at once
        animatorSet.playTogether(animatorTranslateY);
        animatorSet.start();
    }
}
