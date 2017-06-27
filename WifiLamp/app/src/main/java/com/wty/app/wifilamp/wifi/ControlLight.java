package com.wty.app.wifilamp.wifi;

import com.wty.app.wifilamp.util.PreferenceUtil;

/**
 * 描述：控制灯
 */

public class ControlLight {

    private static TcpClient tcpClient = null;
    private static ConnectStateListener listener;

    public static TcpClient newInstance() {
        if (null == tcpClient) {
            tcpClient = new TcpClient() {

                @Override
                public void onConnect(SocketTransceiver transceiver) {
                    if(listener != null)
                        listener.connectSuccess();
                }

                @Override
                public void onConnectFailed() {
                    if(listener != null)
                        listener.connectFailed();
                }

                @Override
                public void onReceive(SocketTransceiver transceiver, String s) {

                }

                @Override
                public void onDisconnect(SocketTransceiver transceiver) {

                }
            };
        }

        return tcpClient;
    }


    public static void connectLight() {
        try {
            if(listener != null){
                listener.startConnect();
            }
            ControlLight.newInstance().connect(PreferenceUtil.getInstance().getIP(), PreferenceUtil.getInstance().getPort());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public interface ConnectStateListener {
        void startConnect();
        void connectSuccess();
        void connectFailed();
    }

    public static void setOnConnectStateListener(ConnectStateListener listener) {
        ControlLight.listener = listener;
    }
}
