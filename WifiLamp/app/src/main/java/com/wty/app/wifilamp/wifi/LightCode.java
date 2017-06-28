package com.wty.app.wifilamp.wifi;

/**
 * 描述：wifi灯协议编码
 */

public class LightCode {

    public static final String Type = "t";//类型
    public static final int Type_Bright = 1;//亮度
    public static final int Type_Color = 2;//颜色
    public static final int Type_Gradien = 3;//呼吸
    public static final int Type_Switch = 4;//开关

    public static final String Color_Red = "cr";//红色
    public static final String Color_Green = "cg";//绿色
    public static final String Color_Blue = "cb";//蓝色

    public static final String Gradien_Frequency = "gf";//呼吸灯频率
    public static final int Frequency_Slow = 0;//慢呼吸
    public static final int Frequency_Normal = 1;//正常呼吸
    public static final int Frequency_Fast = 2;//快呼吸

    public static final String Switch = "ss";//开关
    public static final int Switch_Off = 0;//关闭
    public static final int Switch_On = 1;//开启

    public static final String Bright = "bb";//亮度
    public static final int Bright_Dark = 0;//暗
    public static final int Bright_Normal = 1;//正常
    public static final int Bright_Bright = 2;//亮

}
