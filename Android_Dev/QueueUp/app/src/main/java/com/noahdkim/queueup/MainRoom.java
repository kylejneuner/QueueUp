package com.noahdkim.queueup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlaybackState;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainRoom extends AppCompatActivity implements AsyncResponse{

    private boolean endQueue=false;
    private boolean isPaused=false;
    private boolean repeat = false;
    private boolean shuffle = false;
    private String songsToQueue;
    private String arraySongsToQueue[];
    protected static SpotifyPlayer qPlayer = LogOn.getPlayer();
    ArrayList<String> queue;
    ArrayList<String> playedSongs;
    private final Player.OperationCallback mOperationCallback = new Player.OperationCallback() {
        @Override
        public void onSuccess() {
            Log.d("Queue:", "OK!");
        }

        @Override
        public void onError(Error error) {
            Log.d("Queue: " ,"error");
        }
    };
    private Timer checkDatabase = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent enterRoom = getIntent();
        setContentView(R.layout.activity_main_room);
        final TextView roomID = (TextView)findViewById(R.id.roomID);
        String idNumb = enterRoom.getStringExtra("id");
        final String idNumber = idNumb.substring(1,idNumb.length()-1);
        roomID.setText(idNumber);

        // Sets actions for play/pause and skip song buttons

        checkDatabase.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                PlaybackState playbackState = qPlayer.getPlaybackState();
                if(queue!=null){
                    endQueue = false;
                }
                if(playbackState.isPlaying) {

                }
                else if(endQueue){
                    System.out.println("endQueue");
                    //addSongsToQueue();
                }else if(!isPaused){
                    System.out.println("a");
                    playNextSong(queue, playedSongs);
                }

            }
        }, 0, 1000);
        // The array lists to keep track of the songs
        queue = new ArrayList<String>();
        playedSongs = new ArrayList<String>();
        //checkDatabase.run();
        final Button pp = (Button)findViewById(R.id.PlPa);
        final Button nextSong = (Button)findViewById(R.id.nextSong);
        pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrPause(qPlayer, pp);
            }
        });
        nextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextSong(queue, playedSongs);
            }
        });
        final Button retrieve = (Button)findViewById(R.id.Retrieve);
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSongsToQueue(idNumber);
            }
        });

    }

    public void playNextSong(ArrayList<String> queue, ArrayList<String> playedSongs) {
        System.out.println((queue!=null && playedSongs!=null));
        if(queue!=null && playedSongs!=null && (queue.size()>0 || playedSongs.size()>0)) {
            if (queue.size() == 0 & !endQueue) {
                // If queue is empty and the
                qPlayer.pause(null);
                endQueue = true;
            } else if (queue.size() == 0 & endQueue) {
                int count = playedSongs.size();
                for (int i = 0; i <= playedSongs.size(); i++) {
                    String song = playedSongs.get(0);
                    queue.add(song);
                    playedSongs.remove(0);
                }
                endQueue = false;
                //playNextSong(queue, playedSongs);
                System.out.println("playnext");
            } else if (queue.size() == 0 && playedSongs.size() ==0){
                System.out.println("No songs");
            }
            else{
                String nextSong = queue.get(0);
                playedSongs.add(nextSong);
                queue.remove(0);
                qPlayer.playUri(null, nextSong, 0, 0);
            }
            System.out.println(queue.size() + " " + playedSongs.size());
        }else{

        }
    }


    public void playFirstSong(Player qPlayer){
        // TODO: This method should search the database and play the first song. Stay in function until song found.

        String INITIAL_PLAYLIST = "spotify:track:3n3Ppam7vgaVa1iaRUc9Lp";
        String INITIAL_SONG = "spotify:track:0FE9t6xYkqWXU2ahLh6D8X";
        qPlayer.playUri(null, INITIAL_SONG, 0, 0);
    }

    public void addSongsToQueue(String idNumb){
        // TODO: Every 10 seconds or so, search the database and add the songs to the queue. Loop continually
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate= this;
        String type = "addToQueue";
        backgroundWorker.execute(type, idNumb);
        System.out.println("addSongsToQueue");
        //qPlayer.queue(null, QUEUE_SONG);
    }

    public boolean songIsPlaying(){
        return qPlayer.getPlaybackState().isPlaying;
    }

    public void playOrPause(Player qPlayer, Button pp) {
        if (!endQueue) {
            if (!qPlayer.getPlaybackState().isPlaying) {
                qPlayer.resume(null);
                pp.setText("Pause");
            } else {
                qPlayer.pause(null);
                pp.setText("Resume");
            }
            isPaused = !isPaused;
        }



    }
    @Override
    protected void onDestroy() {
        // *** ULTRA-IMPORTANT ***
        // ALWAYS call this in your onDestroy() method, otherwise you will leak native resources!
        // This is an unfortunate necessity due to the different memory management models of
        // Java's garbage collector and C++ RAII.
        // For more information, see the documentation on Spotify.destroyPlayer().
        Spotify.destroyPlayer(this);
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("clearDB");
        super.onDestroy();
    }


    @Override
    public void processFinish(String output) {
        if(output.length()>2) {
            songsToQueue = output.substring(1, output.length() - 1);
            arraySongsToQueue = songsToQueue.split(",");
            for (int i = 0; i < arraySongsToQueue.length; i++) {
                String song = arraySongsToQueue[i].substring(1, arraySongsToQueue[i].length() - 1);
                queue.add(arraySongsToQueue[i].substring(1, arraySongsToQueue[i].length() - 1));
                System.out.println(song);
            }
        }
        arraySongsToQueue = null;
    }
}