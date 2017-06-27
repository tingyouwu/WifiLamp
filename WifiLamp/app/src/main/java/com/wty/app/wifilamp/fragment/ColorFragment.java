package com.wty.app.wifilamp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcolorpicker.android.IOnColorChangeListener;
import com.wcolorpicker.android.IOnColorSelectedListener;
import com.wcolorpicker.android.WCircleColorPicker;
import com.wty.app.wifilamp.R;

/**
 * 描述：颜色
 */
public class ColorFragment extends Fragment implements IOnColorChangeListener,IOnColorSelectedListener {
    private View rootView;
    WCircleColorPicker colorPicker;
    TextView tv_color;
    TextView tv_data;
    View view_color;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_color, container, false);
        colorPicker = (WCircleColorPicker) rootView.findViewById(R.id.color_picker);
        colorPicker.setOnColorChangedListener(this);
        colorPicker.setOnColorSelectedListener(this);
        view_color = rootView.findViewById(R.id.color_view);
        tv_color = (TextView) rootView.findViewById(R.id.color_tv);
        tv_data = (TextView) rootView.findViewById(R.id.tv_send);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onColorSelected(int red, int green, int blue) {
        tv_color.setText("R:"+red+",G:"+green+",B:"+blue);
    }

    @Override
    public void onColorSelected(int newColor, int oldColor) {
        view_color.setBackgroundColor(newColor);
    }
}
