package hkucs.freeden.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by lshung on 22/11/2017.
 */

public abstract class DrawableUtils {
    public static Drawable tintDrawable(final Activity a, int id, int color ) {
        Drawable wrapped_drawable = DrawableCompat.wrap(
                a.getResources().getDrawable( id )
        ).mutate();
        DrawableCompat.setTint( wrapped_drawable, color );
        return wrapped_drawable;
    }

    public static Drawable tintDrawable(final Context c, int id, int color) {
        Drawable wrapped_drawable = DrawableCompat.wrap(
                c.getResources().getDrawable( id )
        ).mutate();
        DrawableCompat.setTint( wrapped_drawable, color );
        return wrapped_drawable;
    }
}
