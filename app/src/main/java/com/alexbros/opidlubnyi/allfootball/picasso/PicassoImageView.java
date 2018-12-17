package com.alexbros.opidlubnyi.allfootball.picasso;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.alexbros.opidlubnyi.allfootball.Colors;
import com.alexbros.opidlubnyi.allfootball.R;
import com.alexbros.opidlubnyi.allfootball.models.UserProfile;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoImageView {
    private static final float CIRCLE_SIZE_IN_PERCENT = 0.05f;

    public static class BlurTransform implements Transformation {
        RenderScript rs;
        private Context mContext;
        View view;

        public BlurTransform(Context context, View view) {
            super();
            this.view = view;
            rs = RenderScript.create(context);
            mContext = context;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        public Bitmap transform(Bitmap bitmap) {
            Bitmap smallBitmap = Bitmap.createScaledBitmap(bitmap, view.getWidth(), view.getHeight(), false);

            //create a small bitmap and make it darker
            ColorMatrix cm = new ColorMatrix(new float[]
                    {
                            1, 0, 0, 0, -20,
                            0, 1, 0, 0, -20,
                            0, 0, 1, 0, -20,
                            0, 0, 0, 1, 0
                    });

            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(cm));

            Canvas canvas1 = new Canvas(smallBitmap);
            GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{
                    0xBF000000,
                    0xBF000000,
                    0xBF000000,
                    ContextCompat.getColor(mContext, R.color.color_activity_background_black),
                    ContextCompat.getColor(mContext, R.color.color_activity_background_black)});
            gradientDrawable.setBounds(0, 0, canvas1.getWidth(), canvas1.getHeight());
            gradientDrawable.draw(canvas1);
            Bitmap bitmapGo = blurBitmap(smallBitmap, mContext);

            bitmap.recycle();
            return bitmapGo;
        }

        @Override
        public String key() {
            return "blur";
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static Bitmap blurBitmap(Bitmap bitmap, Context context) {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);
        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        //Create the in/out Allocations with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        //Set the radius of the blur
        blurScript.setRadius(25.f);
        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        //recycle the original bitmap
        bitmap.recycle();
        //After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }

    public static class CircleTransform implements Transformation {
        private int userStatus = UserProfile.STATUS_TYPE_NONE;

        public CircleTransform(int userStatus) {
            this.userStatus = userStatus;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            if (UserProfile.STATUS_TYPE_NONE != userStatus) {
                Paint paintStroke = new Paint();
                paintStroke.setAntiAlias(true);
                if (UserProfile.STATUS_TYPE_BRONZE == userStatus)
                    paintStroke.setColor(Colors.bronzeColor);
                else if (UserProfile.STATUS_TYPE_SILVER == userStatus)
                    paintStroke.setColor(Colors.silverColor);
                else if (UserProfile.STATUS_TYPE_GOLD == userStatus)
                    paintStroke.setColor(Colors.goldColor);
                else if (UserProfile.STATUS_TYPE_PLATINUM == userStatus)
                    paintStroke.setColor(Colors.platinumColor);
                else if (UserProfile.STATUS_TYPE_DIAMOND == userStatus)
                    paintStroke.setColor(Colors.diamondColor);

                paintStroke.setStyle(Paint.Style.STROKE);
                float stroke = size * CIRCLE_SIZE_IN_PERCENT;
                paintStroke.setStrokeWidth(stroke);
                canvas.drawCircle(r, r, r - (stroke / 2), paintStroke);
            }
            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle" + userStatus;
        }
    }


    public static Picasso getPicasso(Context context) {
        return PicassoHolder.getInstance().getPicasso(context);
    }
}
