package com.jeanpaulo.musiclibrary.commons.extensions.ui

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.annotation.StyleRes

@ColorInt
fun Context.getThemeColor(@AttrRes attribute: Int) =
    TypedValue().let {
        theme.resolveAttribute(attribute, it, true);
        it.data
    }

@ColorRes
fun Context.getThemeReferenceColor(@AttrRes attribute: Int) =
    TypedValue().let {
        theme.resolveAttribute(attribute, it, true);
        it.resourceId
    }

@StyleRes
fun Context.getThemeStyle(@AttrRes attribute: Int) =
    TypedValue().let {
        theme.resolveAttribute(attribute, it, true)
        it.data
    }

@DimenRes
fun Context.getDimenAttr(@AttrRes attribute: Int) =
    TypedValue().let {
        theme.resolveAttribute(attribute, it, true)
        it.resourceId
    }