package com.zxy.and2;

import static android.view.View.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zxy.and2.placeholder.DakaPlaceholderContent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DakaFragment extends Fragment {

    private int mColumnCount = 1;
    private InputMethodManager inputMethodManager;//键盘弹出收回管理器
    private File file;
    private MyDakaItemRecyclerViewAdapter dakaAdapter;
    private ConstraintLayout dakaConstraint;

    public DakaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daka, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.daka_recyclerview);
        // Set the adapter
        if (recyclerView != null) {
            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            DakaPlaceholderContent.init();
            dakaAdapter = new MyDakaItemRecyclerViewAdapter(DakaPlaceholderContent.ITEMS);
            dakaAdapter.setOnRecyclerViewItemClickListener(new MyDakaItemRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(RecyclerView recyclerView, View view, int position) {
                    Intent intent = new Intent(getActivity(), DakaDetailActivity.class);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(dakaAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setItemViewCacheSize(20);
//            recyclerView.setDrawingCacheEnabled(true);
//            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        }

        dakaConstraint = view.findViewById(R.id.daka_constraint_edit);
        EditText dakaText = view.findViewById(R.id.daka_edit_text);
        Button dakaUploadImage = view.findViewById(R.id.daka_upload_image);
        ImageView photoImageView = view.findViewById(R.id.daka_tmp_image);
        inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        photoImageView.setVisibility(GONE);

//        dakaConstraint.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                dakaText.requestFocus();
//                return true;
//            }
//        });


//        dakaText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    if (dakaText.getHeight() < 320) {
//                        ConstraintLayout.LayoutParams lp_dakaText = (ConstraintLayout.LayoutParams) dakaText.getLayoutParams();
//                        lp_dakaText.height = 320;
//                        dakaText.setLayoutParams(lp_dakaText);
//                        dakaUploadImage.setVisibility(View.VISIBLE);
//                        photoImageView.setVisibility(View.VISIBLE);
//                    }
//                } else {
//                    ConstraintLayout.LayoutParams lp_dakaText = (ConstraintLayout.LayoutParams) dakaText.getLayoutParams();
//                    lp_dakaText.height = 65;
//                    dakaText.setLayoutParams(lp_dakaText);
//
////                ConstraintLayout.LayoutParams lp_dakaUploadImage=(ConstraintLayout.LayoutParams)dakaUploadImage.getLayoutParams();
////                lp_dakaUploadImage.height=30;
////                lp_dakaUploadImage.width=30;
////                dakaUploadImage.setLayoutParams(lp_dakaUploadImage);
////
////                ConstraintLayout.LayoutParams lp_photoImageView=(ConstraintLayout.LayoutParams)photoImageView.getLayoutParams();
////                lp_photoImageView.height=30;
////                lp_photoImageView.width=30;
////                photoImageView.setLayoutParams(lp_dakaUploadImage);
//                    dakaUploadImage.setVisibility(View.GONE);
//                    photoImageView.setVisibility(View.GONE);
//
//                    inputMethodManager.hideSoftInputFromWindow(dakaText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//                }
//            }
//        });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                dakaText.clearFocus();
//            }
//        });


        SharedPreferences pref_reader = getContext().getSharedPreferences(AppControlor.id+"_photo", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor pref = pref_reader.edit();
        dakaUploadImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // 指定开启系统相机的Action
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                // 根据文件地址创建文件
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");// HH:mm:ss
                file = new File(requireContext().getFilesDir(), simpleDateFormat.format(new Date()));
                // 把文件地址转换成Uri格式
//                Uri uri=Uri.fromFile(file);
                Uri uri = FileProvider.getUriForFile(requireActivity(), requireActivity().getPackageName() + ".fileprovider", file);
                // 设置系统相机拍摄照片完成后图片文件的存放地址
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                startActivity(intent);
                startActivityForResult(intent,0);
            }
        });

        Button publish = view.findViewById(R.id.daka_publish);
        publish.setOnClickListener(new OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                if(file==null||!file.exists()){
                    Toast.makeText(requireContext(),"请拍摄照片，完成打卡",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(dakaText.getText().toString().length()==0){
                    Toast.makeText(requireContext(),"请填写文字，完成打卡",Toast.LENGTH_SHORT).show();
                    return;
                }
                pref.putInt("sum", pref_reader.getInt("sum", 0) + 1);
                pref.apply();
                pref.putString("image" + pref_reader.getInt("sum", 1), file.getAbsolutePath());
                pref.putString("cache" + pref_reader.getInt("sum", 1), file.getAbsolutePath() + "_cache");
                pref.putString("content" + pref_reader.getInt("sum", 1), dakaText.getText().toString());
                pref.putString("date" + pref_reader.getInt("sum", 1), new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date()));
                pref.apply();
                int i = pref_reader.getInt("sum", 1);
                DakaPlaceholderContent.ITEMS.add(0, DakaPlaceholderContent.createPlaceholderItem(i, pref_reader.getString("image" + i, null), pref_reader.getString("content" + i, null), pref_reader.getString("date" + i, null), pref_reader.getString("cache" + i, null)));
                dakaAdapter.notifyItemInserted(0);
                recyclerView.scrollToPosition(0);
            }
        });
        return view;
    }


    public void onFragmentResult() {
//            Bundle bundle = data.getExtras();
//            //intent跳转携带参数返回，data转化成Bitmap，获得的是个略缩图
//            Bitmap photo = (Bitmap) bundle.get("data");
        if(file==null||!file.exists())return;
        ImageView photoImageView = dakaConstraint.findViewById(R.id.daka_tmp_image);
        photoImageView.setVisibility(VISIBLE);
        //将预览图放进预览框
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        Matrix matrix=new Matrix();
        if(bitmap.getByteCount()>=1024*1024*17)matrix.setScale(0.08f,0.08f);
        else if(bitmap.getByteCount()>=1024*1024*12)matrix.setScale(0.09f,0.09f);
        else if(bitmap.getByteCount()>=1024*1024*9)matrix.setScale(0.1f,0.1f);
        else if(bitmap.getByteCount()>=1024*1024*3)matrix.setScale(0.2f,0.2f);
        else if(bitmap.getByteCount()>=1024*1024)matrix.setScale(0.3f,0.3f);
        else if(bitmap.getByteCount()>=1024*512)matrix.setScale(0.4f,0.4f);
        else if(bitmap.getByteCount()>=1024*256)matrix.setScale(0.7f,0.7f);
        else matrix.setScale(0.9f,0.9f);
        Bitmap cache=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        File cacheFile=new File(file.getAbsolutePath()+"_cache");
        try {
            if(cacheFile.exists())cacheFile.delete();
            cacheFile.createNewFile();
            FileOutputStream fos=new FileOutputStream(cacheFile);
            cache.compress(Bitmap.CompressFormat.JPEG,100,fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoImageView.setImageBitmap(cache);
//            photoBytes = CommonCode.bitmapToByte(photo);
        Toast.makeText(requireContext(), "提示：一次只能上传一张图片哦~", Toast.LENGTH_SHORT).show();
    }
}