package limitless.android.androiddevelopment.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import limitless.android.androiddevelopment.Model.Song;
import limitless.android.androiddevelopment.Model.Photo;
import limitless.android.androiddevelopment.Model.Video;

public class Data {

    public static List<Photo> getAllPhotos(Context context, String sort){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.DESCRIPTION,
                MediaStore.Images.Media.LATITUDE,
                MediaStore.Images.Media.LONGITUDE
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, sort);
        if (cursor != null){
            if (cursor.getCount() > 0 && cursor.moveToFirst()){
                List<Photo> list = new ArrayList<>();
                int id = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                int height = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT);
                int width = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH);
                int size = cursor.getColumnIndex(MediaStore.Images.Media.SIZE);
                int dateAdded = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);
                int title = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
                int des = cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION);
                int latitude = cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE);
                int longitude = cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE);
                do {
                    list.add(new Photo(
                            cursor.getInt(id),
                            cursor.getInt(height),
                            cursor.getInt(width),
                            cursor.getLong(size),
                            cursor.getLong(dateAdded),
                            cursor.getString(title),
                            ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getInt(id)),
                            cursor.getString(des),
                            cursor.getString(latitude),
                            cursor.getString(longitude)
                    ));
                }while (cursor.moveToNext());
                cursor.close();
                return list;
            }
            cursor.close();
        }
        return null;
    }

    public static List<Song> getAllSongs(Context context, String sort){
        if (context == null)
            return null;
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.SIZE,
                MediaStore.Audio.AudioColumns.DATE_ADDED
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, sort);
        if (cursor == null)
            return null;
        if (cursor.getCount() <= 0 || ! cursor.moveToFirst())
            return null;
        List<Song> list = new ArrayList<>();
        int title = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
        int album = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
        int artist = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
        int id = cursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID);
        int albumId = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID);
        int artistId = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST_ID);
        int duration = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION);
        int size = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE);
        int dateAdded = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED);
        do {
            list.add(new Song(
                    cursor.getString(title),
                    ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, cursor.getInt(id)),
                    cursor.getString(album),
                    cursor.getString(artist),
                    cursor.getInt(id),
                    cursor.getInt(albumId),
                    cursor.getInt(artistId),
                    cursor.getInt(duration),
                    cursor.getLong(size),
                    cursor.getLong(dateAdded)
            ));
        }while (cursor.moveToNext());
        cursor.close();
        return list;
    }

    public static List<Video> getAllVideos(ContentResolver cr, String sort){
        if (cr == null)
            return null;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Video.VideoColumns.TITLE,
                MediaStore.Video.VideoColumns.ALBUM,
                MediaStore.Video.VideoColumns.ARTIST,
                MediaStore.Video.VideoColumns.CATEGORY,
                MediaStore.Video.VideoColumns.RESOLUTION,
                MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.WIDTH,
                MediaStore.Video.VideoColumns.HEIGHT,
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.SIZE,
                MediaStore.Video.VideoColumns.DATE_ADDED
        };
        Cursor cursor = cr.query(uri, projection, null, null, sort);
        if (cursor == null)
            return null;
        if (cursor.getCount() <= 0 || ! cursor.moveToFirst())
            return null;
        List<Video> list = new ArrayList<>();
        int title = cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE);
        int album = cursor.getColumnIndex(MediaStore.Video.VideoColumns.ALBUM);
        int artist = cursor.getColumnIndex(MediaStore.Video.VideoColumns.ARTIST);
        int category = cursor.getColumnIndex(MediaStore.Video.VideoColumns.CATEGORY);
        int resolution = cursor.getColumnIndex(MediaStore.Video.VideoColumns.RESOLUTION);
        int id = cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID);
        int width = cursor.getColumnIndex(MediaStore.Video.VideoColumns.WIDTH);
        int height = cursor.getColumnIndex(MediaStore.Video.VideoColumns.HEIGHT);
        int duration = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION);
        int size = cursor.getColumnIndex(MediaStore.Video.VideoColumns.SIZE);
        int dataAdded = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED);
        do {
            list.add(new Video(
                    cursor.getString(title),
                    ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, cursor.getInt(id)),
                    cursor.getString(album),
                    cursor.getString(artist),
                    cursor.getString(category),
                    cursor.getString(resolution),
                    cursor.getInt(id),
                    cursor.getInt(width),
                    cursor.getInt(height),
                    cursor.getInt(duration),
                    cursor.getLong(size),
                    cursor.getLong(dataAdded)
            ));
        }while (cursor.moveToNext());
        cursor.close();
        return list;
    }

    public static Bitmap getAudioCoverPhoto(Context context, Uri uri) {
        if (context == null || uri == null)
            return null;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
        byte[] bytes = mmr.getEmbeddedPicture();
        if (bytes != null)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    public static Bitmap getVideoCoverPhoto(ContentResolver contentResolver, int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = true;
        options.inDensity = 95;
        options.inSampleSize = 1;
        return MediaStore.Video.Thumbnails.getThumbnail(contentResolver, i, MediaStore.Video.Thumbnails.MINI_KIND, options);
    }

}
