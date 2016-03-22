package com.choinisae.donghaechoi.change_background_1;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.choinisae.donghaechoi.change_background_1.facade.ImageFacade;
import com.mocoplex.adlib.AdlibActivity;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AdlibActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private final int GALLERY_PICK = 1000;
    private ImageFacade mFacade;
    private MyCursorAdapter mMyCursorAdapter;
    public static final int MY_REQUEST_CODE = 100;
    private String TAG = "TAG";
    private static final String TEMP_PHOTO_FILE = "temp.jpg";
    private final int IMAGE_CROP_REQUESTCODE = 2000;
    private String mFilePath;
    private String mCropFile;


    protected void initAds() {
        setAdlibKey("56e6bfe60cf27038eed01b0d");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // android.permission.WRITE_EXTERNAL_STORAGE
        // 권한 확인 메소드

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
            }
        }




        startService(new Intent(this, MyService.class));
        initAds();
        this.setAdsContainer(R.id.ads);
        findViewById(R.id.add_btn).setOnClickListener(this);
        findViewById(R.id.comp_btn).setOnClickListener(this);
        mFacade = new ImageFacade(getApplicationContext());
        GridView mGridView = (GridView) findViewById(R.id.grid_view);
        Cursor cursor = mFacade.queryAllpaths();
        mMyCursorAdapter = new MyCursorAdapter(getApplicationContext(), cursor);
        mGridView.setAdapter(mMyCursorAdapter);
        mGridView.setOnItemLongClickListener(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(MainActivity.this, "권한 동의를 해주셔야 사용가능합니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add_btn: {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_PICK);
                break;
            }
            case R.id.comp_btn: {
                Cursor cursor = mFacade.queryAllpaths();
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                double random = Math.random() * cursor.getCount() + 1;
                cursor.move((int) random);
                // 디바이스 사이즈 확인
                DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                int height = dm.heightPixels;

                // 옵션설정
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                float widthScale = options.outWidth / width;
                float heightScale = options.outHeight / height;
                float scale = widthScale > heightScale ? widthScale : heightScale;

                // 스케일에 따라 inSampleSize 재정의
                if (scale >= 8) {
                    options.inSampleSize = 8;
                } else if (scale >= 6) {
                    options.inSampleSize = 6;
                } else if (scale >= 4) {
                    options.inSampleSize = 4;
                }

                if (cursor.getCount() != 0) {
                    Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(1), options);
                    try {
                        if (bitmap != null) {
                            wallpaperManager.setBitmap(bitmap);
                            Toast.makeText(MainActivity.this, "세팅이 완료 됐습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {

                    }
                } else {
                    Toast.makeText(MainActivity.this, "사진을 불러오지 못했습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectImage,
                    filePathColumn, null, null, null);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
                if (bitmap.getWidth() > bitmap.getHeight()) {
                    // TODO 가로사진 세로로 crop code 추가
                    Toast.makeText(MainActivity.this, "잘라 낸 후 다시 등록을 시도해주세요", Toast.LENGTH_SHORT).show();
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    cropIntent.setDataAndType(selectImage, "image/*");
                    cropIntent.putExtra("crop", "true");
                    // x축과 y축의 비율이 3:4
                    cropIntent.putExtra("aspectX", 3);
                    cropIntent.putExtra("aspectY", 4);
                    // 리턴 데이터를 받지 않고 파일로 저장
                    cropIntent.putExtra("return-data", false);

                    // 저장될 파일명
                    mCropFile = System.currentTimeMillis() + ".jpg";
                    // jpg로 저장
                    cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    // 새로운 파일 생성
                    File file = new File(Environment.getExternalStorageDirectory(), mCropFile);
                    // 생성된 파일의 path얻기
                    mFilePath = file.getPath();
                    // 파일 저장
                    Uri uri = Uri.fromFile(file);
                    cropIntent.putExtra("output", uri);
                    // 크롭 시작
                    startActivityForResult(cropIntent, IMAGE_CROP_REQUESTCODE);
                } else {
                    mFacade.insertPath(picturePath);
                    mMyCursorAdapter.swapCursor(mFacade.queryAllpaths());
                }
            }
            // TODO 크롭시 받아오는 곳
        } else {
            if (requestCode == IMAGE_CROP_REQUESTCODE && resultCode == RESULT_OK && data != null) {
                mFacade.insertPath(mFilePath);
                mMyCursorAdapter.swapCursor(mFacade.queryAllpaths());
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = mFacade.queryAllpaths();
        cursor.moveToPosition(position);
        int deleted = mFacade.deleteImage("_id=" + cursor.getLong(0), null);
        if (deleted > 0) {
            Toast.makeText(getApplicationContext(), "삭제 되었습니다", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onItemLongClick: " + cursor.getPosition());
        } else {
            Toast.makeText(MainActivity.this, "삭제 실패", Toast.LENGTH_SHORT).show();
        }
        mMyCursorAdapter.swapCursor(mFacade.queryAllpaths());
        return true;
    }
}
