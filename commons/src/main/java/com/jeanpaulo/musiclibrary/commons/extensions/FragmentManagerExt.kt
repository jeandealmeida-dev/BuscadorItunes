package com.jeanpaulo.musiclibrary.commons.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.jeanpaulo.musiclibrary.commons.R

fun FragmentManager.replace(
    id: Int,
    fragment: Fragment,
    tag: String? = null,
    addStack: Boolean = false,
    transitionAnimation: TransitionAnimation = TransitionAnimation.NoAnim,
    commitAllow: Boolean = false
) {
    val fragmentTransaction = this.beginTransaction()
    fragmentTransaction.setCustomAnimations(
        transitionAnimation.enterIn,
        transitionAnimation.exitOut,
        transitionAnimation.enterOut,
        transitionAnimation.exitIn
    )
    fragmentTransaction.replace(id, fragment, tag)
    if (addStack) {
        fragmentTransaction.addToBackStack(tag)
    }
    if (commitAllow) {
        fragmentTransaction.commitAllowingStateLoss()
    } else {
        fragmentTransaction.commit()
    }
}

fun FragmentManager.add(
    id: Int,
    fragment: Fragment,
    tag: String? = null,
    addStack: Boolean = false,
    transitionAnimation: TransitionAnimation = TransitionAnimation.NoAnim
) {
    val fragmentTransaction = this.beginTransaction()

    fragmentTransaction.setCustomAnimations(
        transitionAnimation.enterIn,
        transitionAnimation.exitOut,
        transitionAnimation.enterOut,
        transitionAnimation.exitIn
    )

    fragmentTransaction.add(id, fragment, tag)

    if (addStack) {
        fragmentTransaction.addToBackStack(tag)
    }

    fragmentTransaction.commit()
}

sealed class TransitionAnimation(val enterIn: Int, val exitOut: Int, val enterOut: Int, val exitIn: Int) {

    object RightEnterLeftOutAnim : TransitionAnimation(
        R.anim.enter_from_right,
        R.anim.exit_to_left,
        R.anim.enter_from_left,
        R.anim.exit_to_right
    )

    object RightEnterExitRightAnim : TransitionAnimation(
        R.anim.enter_from_right,
        R.anim.exit_to_left,
        0,
        0
    )

    object FadeAnim : TransitionAnimation(
        android.R.anim.fade_in,
        android.R.anim.fade_out,
        0,
        0
    )

    object NoAnim : TransitionAnimation(0, 0, 0, 0)
}