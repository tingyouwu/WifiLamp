package com.wcolorpicker.android;

/**
 * 颜色选中的回调接口。
 *
 * @author wuzhen
 * @version Version 1.0, 2016-09-18
 */
public interface IOnColorSelectedListener {

    /**
     * 颜色选中的监听事件。
     *
     * @param newColor 新颜色
     * @param oldColor 原来的颜色
     */
    void onColorSelected(int newColor, int oldColor);
}
