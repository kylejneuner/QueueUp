package com.noahdkim.queueup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

public class LogOn extends AppCompatActivity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback, AsyncResponse{

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "d6e28e8ad47847239eaeb2916a7772cd";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "queueup://callback";


    private static SpotifyPlayer qPlayer;


    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1137;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_on);


        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


        Button createRoom = (Button) findViewById(R.id.create_room_button);
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Link button press with php so you enter a unique room
                CreateID(view);
                Log.d("LogOn", "create room button pressed");
//                Intent enterRoom = new Intent(LogOn.this, MainRoom.class);
//                startActivity(enterRoom);


            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        qPlayer = spotifyPlayer;
                        qPlayer.addConnectionStateCallback(LogOn.this);
                        qPlayer.addNotificationCallback(LogOn.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        // VERY IMPORTANT! This must always be called or else you will leak resources
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");

        //sPlayer.playUri(null, "spotify:track:3n3Ppam7vgaVa1iaRUc9Lp", 0, 0);
//        Intent intent = new Intent(this, createRoom.class);
//        startActivity(intent);
    }



    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    public static SpotifyPlayer getPlayer(){
        return qPlayer;
    }

    public void CreateID(View view){
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.delegate= this;
        String type = "createID";
        backgroundWorker.execute(type);
        System.out.println("HELLO");
    }

    @Override
    public void processFinish(String output) {

        System.out.print(output+"processFinish");
        Intent enterRoom = new Intent(LogOn.this, MainRoom.class);
        enterRoom.putExtra("id", output);
        startActivity(enterRoom);
    }
}