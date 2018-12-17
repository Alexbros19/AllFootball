package com.alexbros.opidlubnyi.allfootball.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.Constants;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.config.ConfigManager;
import com.alexbros.opidlubnyi.allfootball.helpers.MemoryCacheCleaner;
import com.alexbros.opidlubnyi.allfootball.picasso.PicassoImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

public class UserImageView extends PicassoImageView {
    public static void setImage(Context context, String userHash, int userStatus, int fallback, ImageView view) {
        if (userHash == null)
            return;

        if (view.getTag() != null && view.getTag().equals(Constants.BACKGROUND_IMAGEVIEW_TAG))
            setBackground(context, userHash, view, getNetworkPolicy());
        else
            setPicture(context, userHash, userStatus, view, fallback, null);
    }

    static void setImageAndBackground(final Context context, final String userHash, int userStatus, ImageView imageView, int imageFallback, final ImageView backgroundView) {
        if (userHash == null)
            return;

        setPicture(context, userHash, userStatus, imageView, imageFallback, new Callback() {
            @Override
            public void onSuccess() {
                setBackground(context, userHash, backgroundView, NetworkPolicy.OFFLINE);
            }

            @Override
            public void onError() {
            }
        });

        // if URL is empty then Callback doesn't trigger
        if (userHash.isEmpty())
            setBackground(context, userHash, backgroundView, NetworkPolicy.OFFLINE);
    }

    private static void setPicture(Context context, String userHash, int userStatus, ImageView view, int fallback, final Callback callback) {
        try {
            RequestCreator requestCreator;

            if (userHash.isEmpty())
                requestCreator = getPicasso(context)
                        .load(fallback);
            else
                requestCreator = getPicasso(context)
                        .load(getUrl(userHash));

            requestCreator.networkPolicy(getNetworkPolicy())
                    .priority(Picasso.Priority.LOW)
                    .placeholder(fallback)
                    .error(fallback)
                    .transform(new CircleTransform(userStatus))
                    .into(view, callback);
        } catch (OutOfMemoryError e) {
            MemoryCacheCleaner.clean();

            if (callback != null)
                callback.onError();
        }
    }

    private static void setBackground(Context context, String userHash, ImageView view, NetworkPolicy networkPolicy) {
        try {
            getPicasso(context)
                    .load(getUrl(userHash))
                    .networkPolicy(networkPolicy)
                    .fit()
                    .priority(Picasso.Priority.LOW)
                    .placeholder(new ColorDrawable(Colors.activityBackground))
                    .centerCrop()
                    .transform(new BlurTransform(context, view))
                    .into(view);
        } catch (OutOfMemoryError e) {
            MemoryCacheCleaner.clean();
        }
    }

    public static void setImageEdit(final Context context, String userHash, int userStatus, int fallback, final ImageView view) {
        if (userHash == null)
            return;

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                view.setImageBitmap(bitmap);
                addLayerEdit(context, view);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                view.setImageDrawable(errorDrawable);
                addLayerEdit(context, view);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                view.setImageDrawable(placeHolderDrawable);
                addLayerEdit(context, view);
            }
        };

        view.setTag(target);

        getPicasso(context)
                .load(getUrl(userHash))
                .networkPolicy(getNetworkPolicy())
                .placeholder(fallback)
                .error(fallback)
                .transform(new CircleTransform(userStatus))
                .into(target);
    }

    private static NetworkPolicy getNetworkPolicy() {
        return ConfigManager.getInstance().config().features.logos.getUserPics ? NetworkPolicy.NO_CACHE : NetworkPolicy.OFFLINE;
    }

    public static String getUrl(String userHash) {
        if (userHash.startsWith("http")) {
            return userHash;
        } else if (userHash.startsWith("/")) {
            return "file:" + userHash;
        } else if (!userHash.equals(""))
            return ConfigManager.getInstance().config().baseUserPictureURL + "userpicture/1?id=" + userHash;
        return null;
    }

    public static void addLayerEdit(Context context, ImageView imageView) {
        Drawable[] layers = new Drawable[2];
        layers[0] = imageView.getDrawable();
        layers[1] = ContextCompat.getDrawable(context, R.drawable.edit_user_photo_overlay);
        LayerDrawable layerDrawable = new LayerDrawable(layers);

        Bitmap bitmap = Bitmap.createBitmap(layerDrawable.getIntrinsicWidth(), layerDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        layerDrawable.draw(canvas);
        imageView.setImageBitmap(getBitmapClippedCircle(bitmap));
    }

    private static Bitmap getBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float) (width / 2)
                , (float) (height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }
}
