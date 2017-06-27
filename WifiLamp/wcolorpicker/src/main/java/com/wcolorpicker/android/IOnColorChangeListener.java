package com.wcolorpicker.android;

/**
 * 颜色选中的回调接口。
 */
public interface IOnColorChangeListener {

    /**
     * 颜色选中的监听事件。
     *
     * @param red 红色
     * @param green 绿色
     * @param blue 蓝色
     */
    void onColorSelected(int red, int green,int blue);
}
