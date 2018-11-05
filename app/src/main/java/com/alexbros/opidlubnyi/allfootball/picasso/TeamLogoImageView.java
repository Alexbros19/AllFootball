package com.alexbros.opidlubnyi.allfootball.picasso;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.Constants;
import com.alexbros.opidlubnyi.allfootball.helpers.MemoryCacheCleaner;
import com.alexbros.opidlubnyi.allfootball.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class TeamLogoImageView extends PicassoImageView {
    public static void setTeamImage(final Context context, final ImageView view, final String id, boolean cancelPending) {
        final boolean isBackground = (view.getTag() != null) && view.getTag().equals(Constants.BACKGROUND_IMAGEVIEW_TAG) && !id.equals("");

        try {
            final String urlTeam = getLogoUrl(id);

            if (cancelPending) {
                try {
                    getPicasso(context).cancelTag(System.identityHashCode(view));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (isBackground) {
                getPicasso(context)
                        .load(urlTeam)
                        .fit().centerCrop()
                        .priority(Picasso.Priority.LOW)
                        .placeholder(new ColorDrawable(Colors.activityBackground))
                        .transform(new PicassoImageView.BlurTransform(context, view))
                        .tag(System.identityHashCode(view))
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(view, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                try {
//                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                                    List<String> list = new ArrayList<>(preferences.getStringSet(Preferences.PREF_LIST_TEAM_LOGOS, new HashSet<String>()));
//                                    if (list.contains(id)) {
                                    getPicasso(context)
                                            .load(urlTeam)
                                            .fit().centerCrop()
                                            .priority(Picasso.Priority.LOW)
                                            .placeholder(new ColorDrawable(Colors.activityBackground))
                                            .transform(new PicassoImageView.BlurTransform(context, view))
                                            .tag(System.identityHashCode(view))
                                            .into(view);
//                                    }
                                } catch (OutOfMemoryError e) {
                                    MemoryCacheCleaner.clean();
                                }
                            }
                        });
            } else {
                getPicasso(context)
                        .load(urlTeam)
                        .priority(Picasso.Priority.HIGH)
                        .placeholder(R.drawable.team_logo_unknown)
                        .error(R.drawable.team_logo_unknown)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(view, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                try {
//                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//                            List<String> list = new ArrayList<>(preferences.getStringSet(Preferences.PREF_LIST_TEAM_LOGOS, new HashSet<String>()));
//                            if (list.contains(id)) {
                                    getPicasso(context)
                                            .load(urlTeam)
                                            .priority(Picasso.Priority.HIGH)
                                            .placeholder(R.drawable.team_logo_unknown)
                                            .error(R.drawable.team_logo_unknown)
                                            .into(view);
//                            }
                                } catch (OutOfMemoryError e) {
                                    MemoryCacheCleaner.clean();
                                }
                            }
                        });
            }
        } catch (OutOfMemoryError e) {
            MemoryCacheCleaner.clean();
        }
    }

    public static String getLogoUrl(String id) {
        return Constants.MEDIA_URL + "soccer/teamlogo/2?id" + "=" + id;
    }
}
