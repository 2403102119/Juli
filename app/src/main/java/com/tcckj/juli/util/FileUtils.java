package com.tcckj.juli.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
	
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/formats/";
	public static String SDPATH1 = Environment.getExternalStorageDirectory()
        + "/myimages/";
	public static void saveBitmap(Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".png");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将URL转化成bitmap形式
	 *
	 * @param url
	 * @return bitmap type
	 */
	public final static Bitmap returnBitMap(String url) {
		URL myFileUrl;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
			HttpURLConnection conn;
			conn = (HttpURLConnection) myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

//	保存文件的方法：
	public static Bitmap saveBitmapFromView(View view) {
		int w = view.getWidth();
		int h = view.getHeight();
		int[] l1 = {0, 0};
		view.getLocationInWindow(l1);
		Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmp);
		view.layout(l1[0], l1[1], w, h);
		view.draw(c);
		// 缩小图片
		Matrix matrix = new Matrix();
		matrix.postScale(0.5f,0.5f); //长和宽放大缩小的比例
		bmp = Bitmap.createBitmap(bmp,0,0,
				bmp.getWidth(),bmp.getHeight(),matrix,true);
			saveBitmap(bmp,TimeUtils.getTime()+".JPEG");
		return bmp;
	}

	/*
	 * 保存文件，文件名为当前日期
	 */
	public static void saveBitmap(Bitmap bitmap, String bitName, Context context){
		String fileName ;
		File file ;
		if(Build.BRAND .equals("Xiaomi") ){ // 小米手机
			fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/"+bitName ;
		}else{  // Meizu 、Oppo
			fileName = Environment.getExternalStorageDirectory().getPath()+"/DCIM/"+bitName ;
		}
		file = new File(fileName);

		if(file.exists()){
			file.delete();
		}
		FileOutputStream out;
		try{
			out = new FileOutputStream(file);
			// 格式为 JPEG，照相机拍出的图片为JPEG格式的，PNG格式的不能显示在相册中
			if(bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out))
			{
				out.flush();
				out.close();
// 插入图库
				MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), bitName, null);

			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();

		}

		Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
		// 发送广播，通知刷新图库的显示
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));

	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir(String path) {
		File dir = new File(path);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(path); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
