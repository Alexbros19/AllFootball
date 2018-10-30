package com.alexbros.opidlubnyi.allfootball.picasso;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
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

import com.alexbros.opidlubnyi.allfootball.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoImageView {
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

    public static Picasso getPicasso(Context context) {
        return PicassoHolder.getInstance().getPicasso(context);
    }
}
