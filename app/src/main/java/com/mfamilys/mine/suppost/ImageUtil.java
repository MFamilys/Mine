package com.mfamilys.mine.suppost;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.graphics.Palette;

/**
 * Created by mfamilys on 16-4-8.
 */
public class ImageUtil {
    public static int getImageColor(Bitmap bitmap) {
        Palette palette = Palette.from(bitmap).generate();
        if (palette == null || palette.getDarkMutedSwatch() == null) {
            return Color.LTGRAY;
        }
        return palette.getDarkMutedSwatch().getRgb();
    }
}
