package com.bytedance.camera.demo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;
import com.bytedance.camera.demo.utils.Utils;

import java.io.File;

public class TakePictureActivity extends AppCompatActivity {

	private ImageView imageView;
	private static final int REQUEST_IMAGE_CAPTURE = 1;

	private static final int REQUEST_EXTERNAL_STORAGE = 101;
	private File mimageFile;

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_picture);

		imageView = findViewById(R.id.img);
		findViewById(R.id.btn_picture).setOnClickListener(v -> {
			if (ContextCompat.checkSelfPermission(TakePictureActivity.this,
					Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
					|| ContextCompat.checkSelfPermission(TakePictureActivity.this,
					Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				//todo 在这里申请相机、存储的权限
				requestPermissions(
						new String[]{Manifest.permission.CAMERA,
								Manifest.permission.WRITE_EXTERNAL_STORAGE,
								Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
			} else {
				takePicture();
			}
		});

	}

	private void takePicture() {
		//todo 打开相机
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		mimageFile = Utils.getOutputMediaFile(Utils.MEDIA_TYPE_IMAGE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mimageFile));
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			setPic();
		}
	}

	private void setPic() {
		//todo 展示图片
		//todo 根据imageView裁剪
		//todo 根据缩放比例读取文件，生成Bitmap

		//todo 如果存在预览方向改变，进行图片旋转

		//todo 如果存在预览方向改变，进行图片旋转
		// Get the dimensions of the View
		int targetW = imageView.getWidth();
		int targetH = imageView.getHeight();
		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		// Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		if (mimageFile != null) {
			Bitmap bitmap = BitmapFactory.decodeFile(mimageFile.getPath(), bmOptions);
			Utils.rotateImage(bitmap, mimageFile.getPath());
			imageView.setImageBitmap(bitmap);
		} else {
			Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_EXTERNAL_STORAGE: {
				if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
					Toast.makeText(this, "没有存储权限！\n请接收权限申请或前往设置添加权限！", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case REQUEST_IMAGE_CAPTURE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					takePicture();
				} else {
					Toast.makeText(this, "没有相机权限！\n请接收权限申请或前往设置添加权限！", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
}
