package com.alexbros.opidlubnyi.allfootball.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alexbros.opidlubnyi.allfootball.R;

public class CustomDialogBuilder extends AlertDialog.Builder {

    /**
     * The custom_body layout
     */
    private View mDialogView;

    /**
     * optional dialog title layout
     */
    private TextView mTitle;

    /**
     * The colored holo divider. You can set its color with the setDividerColor method
     */
    //private View mDivider;
    public CustomDialogBuilder(Context context) {
        super(context);

        mDialogView = View.inflate(context, R.layout.dialog_custom, null);
        setView(mDialogView);

        mTitle = (TextView) mDialogView.findViewById(R.id.alertTitle);
        //mDivider = mDialogView.findViewById(R.id.titleDivider);
    }

    /**
     * Use this method to color the divider between the title and content.
     * Will not display if no title is set.
     *
     * @param colorString for passing "#ffffff"
     */
//    public CustomDialogBuilder setDividerColor(int color) {
//    	mDivider.setBackgroundColor(color);
//    	return this;
//    }
    @Override
    public CustomDialogBuilder setTitle(CharSequence text) {
        mTitle.setText(text);
        return this;
    }

    @Override
    public CustomDialogBuilder setTitle(int resId) {
        mTitle.setText(resId);
        return this;
    }

    public CustomDialogBuilder setTitleColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }

    /**
     * This allows you to specify a custom layout for the area below the title divider bar
     * in the dialog. As an example you can look at example_ip_address_layout.xml and how
     * I added it in TestDialogActivity.java
     *
     * @param resId   of the layout you would like to add
     * @param context
     */
    public CustomDialogBuilder setCustomView(int resId, Context context) {
        View customView = View.inflate(context, resId, null);
        ((FrameLayout) mDialogView.findViewById(R.id.customPanel)).addView(customView);
        return this;
    }

    public CustomDialogBuilder setCustomView(View view) {
        //View customView = View.inflate(context, resId, null);
        ((FrameLayout) mDialogView.findViewById(R.id.customPanel)).addView(view);
        return this;
    }

    @Override
    public AlertDialog show() {
        if (mTitle.getText().equals(""))
            mDialogView.findViewById(R.id.topPanel).setVisibility(View.GONE);
        return super.show();
    }
}
