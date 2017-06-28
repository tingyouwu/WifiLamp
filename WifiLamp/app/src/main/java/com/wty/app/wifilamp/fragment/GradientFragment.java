package com.wty.app.wifilamp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.wty.app.wifilamp.R;
import com.wty.app.wifilamp.eventbus.WifiEvent;
import com.wty.app.wifilamp.wifi.LightCode;

import org.greenrobot.eventbus.EventBus;


/**
 * 描述：渐变呼吸
 */
public class GradientFragment extends Fragment {
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_gradient, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        SeekBar seekBarSpeed = (SeekBar) rootView.findViewById(R.id.seekBar_speed);
        seekBarSpeed.setProgress(50);
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int progress = seekBar.getProgress();
                int state;
                if (progress < 25) {
                    seekBar.setProgress(0);
                    state = LightCode.Frequency_Slow;
                } else if (progress < 75) {
                    seekBar.setProgress(50);
                    state = LightCode.Frequency_Normal;
                } else {
                    seekBar.setProgress(100);
                    state = LightCode.Frequency_Fast;
                }
                WifiEvent event = new WifiEvent(LightCode.Type_Gradien);
                event.appendHashParam(LightCode.Gradien_Frequency,state);
                EventBus.getDefault().post(event);
            }
        });
    }
}
