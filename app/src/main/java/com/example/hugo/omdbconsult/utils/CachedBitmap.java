package com.example.hugo.omdbconsult.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Hugo on 25/10/2015.
 */
public class CachedBitmap implements Serializable {

    private static final long serialVersionUID = -6298516694275121291L;

    transient Bitmap bitmap;
    String url;

    public CachedBitmap() {
    }

    ;

    public CachedBitmap(Bitmap b, String sourceUrl) {
        bitmap = b;
        url = sourceUrl;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        // This will serialize all fields that you did not mark with 'transient'
        // (Java's default behaviour)
        oos.defaultWriteObject();
        // Now, manually serialize all transient fields that you want to be serialized
        if (bitmap != null) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            boolean success = bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
            if (success) {
                oos.writeObject(byteStream.toByteArray());
            }
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        // Now, all again, deserializing - in the SAME ORDER!
        // All non-transient fields
        ois.defaultReadObject();
        // All other fields that you serialized
        byte[] image = (byte[]) ois.readObject();
        if (image != null && image.length > 0) {
            bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}