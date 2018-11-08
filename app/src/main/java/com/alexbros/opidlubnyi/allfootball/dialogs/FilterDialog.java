package com.alexbros.opidlubnyi.allfootball.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.Constants;
import com.alexbros.opidlubnyi.allfootball.ModelData;
import com.alexbros.opidlubnyi.allfootball.R;

public class FilterDialog {
    public static AlertDialog createDialog(final Activity activity, final LayoutInflater layoutInflater, final ModelData model) {
        View dialogView = layoutInflater.inflate(R.layout.dialog_filter, null);
        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder = new CustomDialogBuilder(activity);
            ((CustomDialogBuilder) builder).setCustomView(dialogView);
            ((CustomDialogBuilder) builder).setTitleColor(Colors.activityBackground);
        } else {
            builder = new AlertDialog.Builder(activity);
            builder.setView(dialogView);
        }

        builder.setTitle(R.string.string_filter_title);

        final CheckBox liveCheckBox = dialogView.findViewById(R.id.filterdialog_liveCheckbox);
        liveCheckBox.setChecked(model.onlyLiveGamesFilterEnabled);

        RelativeLayout liveRelativeLayout = dialogView.findViewById(R.id.filterdialog_liveRelativeLayout);
        liveRelativeLayout.setOnClickListener(v -> liveCheckBox.setChecked(!liveCheckBox.isChecked()));

        builder.setPositiveButton(R.string.string_ok, (dialog, which) -> {

            model.onlyLiveGamesFilterEnabled = liveCheckBox.isChecked();

            dialog.dismiss();

            Intent scoreRedrawIntent = new Intent(Constants.BROADCAST_ACTION_REDRAW_SCORES);
            LocalBroadcastManager.getInstance(activity).sendBroadcast(scoreRedrawIntent);
        });

        builder.setNegativeButton(R.string.string_cancel, null);

        return builder.create();
    }
}
