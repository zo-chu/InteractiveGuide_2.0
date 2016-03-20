package com.kitanasoftware.interactiveguide;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kitanasoftware.interactiveguide.dataTransfer.WifiUtility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BroadCastScreen_6 extends AppCompatActivity {

    Timer broadcast_timer = new Timer();
    private final long BROADCAST_TIME = 5000;//5 seconds
    Thread broadcastIpThread = null;

    private double OLD_PLAY_RANDOM = 0.0;//monitor stream play
    private double LASTEST_PLAY_RANDOM = 0.0;//monitor stream play
    private AudioTrack speaker = null;

    public static DatagramSocket socket;
    public byte[] buffer;

    PlayStream playStream = null;

    private int VOICE_STREAM_PORT = 50005;
    private int BROADCAST_IP_PORT = 36367;

    Thread listenForBroadcastsThread = null;

    //Audio Configuration.
    private int RECORDER_SAMPLE_RATE = 44100;
    @SuppressWarnings("deprecation")
    private int RECORDER_CHANNELS = AudioFormat.CHANNEL_CONFIGURATION_STEREO;
    private int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private boolean status_sending = false;
    private boolean status_receiving = true;

    ArrayList<String> devices = new ArrayList<String>();
    ArrayList<AudioRecord> recorders = new ArrayList<AudioRecord>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.broad_cast_screen_6);

        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#f8bfd8"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);

    }

    //Listen for voice streams and play.
    public class PlayStream extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                @SuppressWarnings("resource")
//Socket Created
                        DatagramSocket socket = new DatagramSocket(VOICE_STREAM_PORT);

                int MIN_BUFFER_SIZE = 8192;

                byte[] buffer = new byte[MIN_BUFFER_SIZE];

                speaker = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
                        RECORDER_SAMPLE_RATE,
                        RECORDER_CHANNELS,
                        RECORDER_AUDIO_ENCODING,
                        MIN_BUFFER_SIZE * 5,
                        AudioTrack.MODE_STREAM);
                speaker.play();

                while(status_receiving == true) {
                    try {

                        DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
//Packet Received
                        socket.receive(packet);

//reading content from packet
                        buffer=packet.getData();
                        String ip = packet.getAddress().toString();
                        System.out.println();

//Writing buffer content to Audiotrack (speaker)
                        speaker.write(buffer, 0, MIN_BUFFER_SIZE);

                        OLD_PLAY_RANDOM = LASTEST_PLAY_RANDOM;
                        LASTEST_PLAY_RANDOM = Math.random();

                    } catch(IOException e) {
                        e.printStackTrace();
                    }
                }

            } catch (SocketException e) {
                e.printStackTrace();
            }

            return "";
        }
        @Override
        protected void onPostExecute(String value) {
            super.onPostExecute(value);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!WifiUtility.isWifiConnected(this)){
            if (!WifiUtility.isHotSpot(this)){
                return;
            }
        }

        if (playStream ==null){
            playStream = (PlayStream) new PlayStream().execute();
        }
        if (broadcastIpThread==null){
            sendBroadcast();
        }
        if (listenForBroadcastsThread ==null){
            ListenForBroadcasts();
        }

    }

    public void startClick(final View v) {

        v.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                v.setVisibility(View.VISIBLE);
                sendStream();
            }
        }, 50);

    }

    public void sendStream(){
        status_sending = true;
        for (int i=0; i<devices.size(); i++){
            String device_ip = devices.get(i);
            System.out.println(devices.get(i));
            sendStream(device_ip);
        }
    }

    public void broadcastIp(final String message) {
        broadcastIpThread = new Thread (new Runnable() {
            @Override
            public void run() {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                try {
                    DatagramSocket socket = new DatagramSocket();
                    socket.setBroadcast(true);
                    byte[] sendData = message.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData,
                            sendData.length,
                            WifiUtility.getBroadcastAddress(),
                            BROADCAST_IP_PORT);
                    socket.send(sendPacket);
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        broadcastIpThread.start();
    }

    public void sendBroadcast(){
        broadcast_timer = new Timer();
        broadcast_timer.schedule(new TimerTask(){
            public void run() {
                broadcastIp("Ping");
            }}, 0, BROADCAST_TIME);
    }

    public void ListenForBroadcasts(){
        listenForBroadcastsThread = new Thread (new Runnable() {
            @Override
            public void run() {
                try {

//Keep socket open to listen to all the UDP traffic that is destined for this port.
                    socket = new DatagramSocket(BROADCAST_IP_PORT, InetAddress.getByName("0.0.0.0"));
                    socket.setBroadcast(true);

                    while (true) {

//Receive a packet
                        byte[] buffer = new byte[15000];
                        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);

                        final String sender_ip = packet.getAddress().getHostAddress();
                        String device_ip = WifiUtility.getIpAddress();

//if ping is coming from this device, ignore.
                        if (!sender_ip.equalsIgnoreCase(device_ip)){

                            boolean ip_address_already_exist = false;
//Check if devices exists on the list
                            for (int i=0; i<devices.size();i++){
                                if (devices.get(i).equalsIgnoreCase(sender_ip)){
                                    ip_address_already_exist = true;
                                }
                            }

                            if (!ip_address_already_exist){

//Update devices list
                                devices.add(sender_ip);

//if device is streaming voice
                                if (status_sending){//stream to the new device
                                    sendStream(device_ip);
                                }

                            }

                        }

                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        listenForBroadcastsThread.start();
    }

    //Send voice stream to a specific device on the network.
    public void sendStream(final String device_ip) {

        Thread sendStreamThread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    int MIN_BUFFER_SIZE = 8192;

                    @SuppressWarnings("resource")
//Socket Created
                            DatagramSocket socket = new DatagramSocket();

                    byte[] buffer = new byte[MIN_BUFFER_SIZE];

                    DatagramPacket packet;

//Destination Address retrieved.
                    final InetAddress destination = InetAddress.getByName(device_ip);

//Initialize Recorder
                    AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            RECORDER_SAMPLE_RATE,
                            RECORDER_CHANNELS,
                            RECORDER_AUDIO_ENCODING,
                            MIN_BUFFER_SIZE * 5);

//add to array
                    recorders.add(recorder);
                    recorder.startRecording();

                    while (status_sending == true) {

//reading data from MIC into buffer
                        MIN_BUFFER_SIZE = recorder.read(buffer, 0, buffer.length);

//putting buffer in the packet
                        packet = new DatagramPacket (buffer, buffer.length, destination, VOICE_STREAM_PORT);

//Send packet
                        socket.send(packet);
                    }

                } catch(UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
//socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        sendStreamThread.start();

    }
}
