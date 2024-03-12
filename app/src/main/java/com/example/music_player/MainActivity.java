package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView play,next,previous,headphone,text;
    TextView songtitle;
    SeekBar mseekbarTime,mseekbarvol;
    static MediaPlayer mMediaplayer;
    private Runnable runnable;
    private AudioManager mAudiomanager;
    int currentIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAudiomanager=(AudioManager) getSystemService(Context.AUDIO_SERVICE);

        play=findViewById(R.id.pause);
                previous=findViewById(R.id.previous);
                        next=findViewById(R.id.next);
                                songtitle=findViewById(R.id.Text);
                                headphone=findViewById(R.id.headphone);
                                mseekbarTime=findViewById(R.id.seekbar1);
                                        mseekbarvol=findViewById(R.id.seekbarvol);
        final ArrayList<Integer> songs =new ArrayList<>();
        songs.add(0,R.raw.doraemon);
        songs.add(1,R.raw.shinchan);
        songs.add(2,R.raw.kiteretsu);
        songs.add(3,R.raw.ninja_hattori);
        songs.add(4,R.raw.pokemon);

//        initializing mediaplayer

        mMediaplayer=MediaPlayer.create(getApplicationContext(),
                songs.get(currentIndex));

        int maxv=mAudiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curv=mAudiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mseekbarvol.setMax(maxv);
        mseekbarvol.setProgress(curv);

        mseekbarvol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mAudiomanager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        i,0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mseekbarTime.setMax(mMediaplayer.getDuration());
                if(mMediaplayer !=null &&mMediaplayer.isPlaying())
                {
                    mMediaplayer.pause();
                    play.setImageResource(R.drawable.baseline_play_circle_24);

                }
                else {
                    mMediaplayer.start();
                    play.setImageResource(R.drawable.baseline_pause_circle_filled_24);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaplayer != null) {
                    play.setImageResource(R.drawable.baseline_pause_circle_filled_24);
                }

                if (currentIndex < songs.size() - 1) {
                    currentIndex++;
                } else {
                    currentIndex = 0;
                }

                if (mMediaplayer.isPlaying()) {
                    mMediaplayer.stop();
                }

                mMediaplayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));

               mMediaplayer.start();
                songNames();

            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMediaplayer != null) {
                    play.setImageResource(R.drawable.baseline_pause_circle_filled_24);
                }

                if (currentIndex > 0) {
                    currentIndex--;
                } else {
                    currentIndex = songs.size() - 1;
                }
                if (mMediaplayer.isPlaying()) {
                    mMediaplayer.stop();
                }

                mMediaplayer = MediaPlayer.create(getApplicationContext(), songs.get(currentIndex));
                mMediaplayer.start();
                songNames();

            }
        });

    }

    private void songNames(){
        if(currentIndex==0){
            songtitle.setText("Doremon-ek hassen zindagi");
            headphone.setImageResource(R.drawable.doraemon_img);
        }
        if(currentIndex==1){
            songtitle.setText("Shinchan-ek hassen zindagi");
            headphone.setImageResource(R.drawable.shinchen_img);
        }
        if(currentIndex==2){
            songtitle.setText("Ninja-ek hassen zindagi");
            headphone.setImageResource(R.drawable.ninjahattori_img);
        }
        if(currentIndex==3){
            songtitle.setText("kiteretsu-ek hassen zindagi");
            headphone.setImageResource(R.drawable.kiteretsu_img);
        }
        if(currentIndex==4){
            songtitle.setText("pokemon-ek hassen zindagi");
            headphone.setImageResource(R.drawable.pokemon_img);
        }

        // seekbar duration
        mMediaplayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
               mseekbarTime.setMax(mMediaplayer.getDuration());
                mMediaplayer.start();
            }
        });

        mseekbarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mMediaplayer.seekTo(progress);
                   mseekbarTime.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mMediaplayer  != null) {
                    try {
                        if (mMediaplayer.isPlaying()) {
                            Message message = new Message();
                            message.what = mMediaplayer.getCurrentPosition();
                            handler.sendMessage(message);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @SuppressLint("Handler Leak")
    Handler handler = new Handler () {
        @Override
        public void handleMessage  (Message msg) {
            mseekbarTime.setProgress(msg.what);
        }
    };

    }

