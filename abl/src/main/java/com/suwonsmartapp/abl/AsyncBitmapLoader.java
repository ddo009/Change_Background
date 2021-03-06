
package com.suwonsmartapp.abl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by junsuk on 15. 4. 8..
 *
 * 비동기로 Bitmap 을 로드하는 클래스
 * 메모리 캐시를 사용
 */
public class AsyncBitmapLoader {

    private ImageCache mImageCache;

    private BitmapLoadListener mBitmapLoadListener;

    private Context mContext;

    private ColorDrawable mTransparentColorDrawable;

    private TransitionDrawable mTransitionDrawable;

    /**
     * AsyncBitmapLoader 사용시 setBitmapLoadListener 에 설정 하는 리스너
     */
    public interface BitmapLoadListener {
        Bitmap getBitmap(String path);
    }

    public void setBitmapLoadListener(BitmapLoadListener listener) {
        mBitmapLoadListener = listener;
    }

    public AsyncBitmapLoader(Context context) {
        mContext = context;

        final int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = 1024 * 1024 * memClass / 8;

        mImageCache = new MemoryImageCache(cacheSize);

        mTransparentColorDrawable = new ColorDrawable(Color.TRANSPARENT);
    }

    /**
     * 어댑터의 getView에서 이미지뷰에 동적 로딩
     *
     * @param position getView 의 position
     * @param imageView 이미지를 설정 할 이미지뷰
     */
    public void loadBitmap(String position, ImageView imageView) {
        final String imageKey = String.valueOf(position);

        final Bitmap bitmap = getBitmapFromCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            if (cancelTask(position, imageView)) {
                final AsyncBitmapLoaderTask task = new AsyncBitmapLoaderTask(position, imageView);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), null, task);
                imageView.setImageDrawable(asyncDrawable);

                task.execute(position);
            }
        }
    }

    private void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mImageCache.addBitmap(key, bitmap);
        }
    }

    private Bitmap getBitmapFromCache(String key) {
        return mImageCache.getBitmap(key);
    }

    class AsyncBitmapLoaderTask extends AsyncTask<String, Void, Bitmap> {
        private String position = "";

        private final WeakReference<ImageView> mImageViewReference;

        public AsyncBitmapLoaderTask(String position, ImageView imageView) {
            mImageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            if (mBitmapLoadListener == null) {
                throw new NullPointerException("BitmapLoadListener is null");
            }

            position = params[0];
//            Log.d("AsyncBitmapLoader", "position : " + position);

            final Bitmap bitmap = mBitmapLoadListener.getBitmap(position);

            String key = position;
            addBitmapToCache(key, bitmap);

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (mImageViewReference != null && bitmap != null) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), bitmap);
                Drawable[] drawables = new Drawable[] { mTransparentColorDrawable, bitmapDrawable};
                mTransitionDrawable = new TransitionDrawable(drawables);

                final ImageView imageView = mImageViewReference.get();
                if (imageView != null) {
                    // ImageView 에서 Task를 얻음
                    final AsyncBitmapLoaderTask bitmapLoaderTask = getAsyncBitmapLoaderTask(imageView);

                    // 같은 Task이면 비트맵을 설정
                    if (this == bitmapLoaderTask && imageView != null) {
                        mTransitionDrawable.startTransition(500);
                        imageView.setImageDrawable(mTransitionDrawable);
                    }
                }
            }
        }

    }

    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<AsyncBitmapLoaderTask> asyncBitmapLoaderTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, AsyncBitmapLoaderTask asyncBitmapLoaderTask) {
            super(res, bitmap);
            this.asyncBitmapLoaderTaskReference = new WeakReference<AsyncBitmapLoaderTask>(asyncBitmapLoaderTask);
        }

        public AsyncBitmapLoaderTask getAsyncBitmapLoaderTask() {
            return asyncBitmapLoaderTaskReference.get();
        }
    }

    private static AsyncBitmapLoaderTask getAsyncBitmapLoaderTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getAsyncBitmapLoaderTask();
            }
        }
        return null;
    }

    public static boolean cancelTask(String position, ImageView imageView) {
        final AsyncBitmapLoaderTask task = getAsyncBitmapLoaderTask(imageView);

        if (task != null) {
            final String taskPosition = task.position;
            if (!taskPosition.isEmpty()) {
                // 이전 Task 를 캔슬
                task.cancel(true);
                Log.d("AsyncBitmapLoader", "cancel : " + position);
            } else {
                // 같은 Task 일 경우, 실행 하지 않음
                Log.d("AsyncBitmapLoader", "false");
                return false;
            }
        }
        // 새로운 Task 실행
        return true;
    }

    public void destroy() {
        if (mImageCache != null) {
            mImageCache.clear();
            mImageCache = null;
        }
    }

}