package com.zxy.and2;

import android.app.Activity;

import androidx.core.content.res.ResourcesCompat;

import site.gemus.openingstartanimation.OpeningStartAnimation;

public class AppStartAnimation {
    public static void appStartAnimation(Activity activity){
        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(activity)
                .setAppIcon(ResourcesCompat.getDrawable(activity.getResources(),R.drawable.ic_launcher_foreground2,null)) //设置图标
//                .setColorOfAppIcon(getColor(R.color.teal_200)) //设置绘制图标线条的颜色
                .setAppName(activity.getString(R.string.app_name)) //设置app名称
//                .setColorOfAppName() //设置app名称颜色
                .setAppStatement("关爱乡村，关爱留守儿童") //设置一句话描述
//                .setColorOfAppStatement() // 设置一句话描述的颜色
                .setAnimationInterval(2000) // 设置动画时间间隔
//                .setAnimationFinishTime() // 设置动画的消失时长
                .create();
        openingStartAnimation.show(activity);


// 开屏动画方法1
//        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
//                .setDrawStategy(new NormalDrawStrategy()) //设置动画效果
//                .create();
//        openingStartAnimation.show(this);
// 开屏动画方法2
//        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
//                .setDrawStategy(new DrawStrategy() {
//                    @Override
//                    public void drawAppName(Canvas canvas, float fraction, String name, int colorOfAppName, WidthAndHeightOfView widthAndHeightOfView) {
//
//                    }
//                    @Override
//                    public void drawAppIcon(Canvas canvas, float fraction, Drawable icon, int colorOfIcon, WidthAndHeightOfView widthAndHeightOfView) {
//                    }
//                    @Override
//                    public void drawAppStatement(Canvas canvas, float fraction, String statement, int colorOfStatement, WidthAndHeightOfView widthAndHeightOfView) {
//                    }
//                })
//                .create();
//        openingStartAnimation.show(this);
// 开屏动画方法3
//        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
////                .setAppIcon(getDrawable(R.drawable.ic_launcher_foreground)) //设置图标
////                .setColorOfAppIcon(getColor(R.color.teal_200)) //设置绘制图标线条的颜色
//                .setAppName("蓝信封") //设置app名称
////                .setColorOfAppName() //设置app名称颜色
//                .setAppStatement("关爱乡村，关爱留守儿童") //设置一句话描述
////                .setColorOfAppStatement() // 设置一句话描述的颜色
//                .setAnimationInterval(2000) // 设置动画时间间隔
////                .setAnimationFinishTime() // 设置动画的消失时长
//                .create();
//        openingStartAnimation.show(this);
    }
}
