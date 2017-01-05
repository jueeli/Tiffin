package com.svmindlogic.tiffin.tiffinmap.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class CommonMethods {

    public static void setCustomFontCANADARA(Context context, View view) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/candara.ttf");
        if (view instanceof TextView) {
            ((TextView) view).setTypeface(tf);
        } else if (view instanceof Button) {
            ((Button) view).setTypeface(tf);
        } else if (view instanceof EditText) {
            ((EditText) view).setTypeface(tf);
        } else if (view instanceof CheckBox) {
            ((CheckBox) view).setTypeface(tf);
        }
    }


}
