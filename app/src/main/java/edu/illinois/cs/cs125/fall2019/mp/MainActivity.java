package edu.illinois.cs.cs125.fall2019.mp;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Represents the main screen of the app, where the user will be able to view invitations and enter games.
 */
public final class MainActivity extends AppCompatActivity {

    /**
     * the email of the user.
     */
    private String myEmail;

    /**
     * Called by the Android system when the activity is created.
     *
     * @param savedInstanceState saved state from the previously terminated instance of this activity (unused)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        // This "super" call is required for all activities
        super.onCreate(savedInstanceState);
        // Create the UI from a layout resource
        setContentView(R.layout.activity_main);
        Button createGame = findViewById(R.id.createGame);
        createGame.setOnClickListener(unused -> createGameWhenClicked());
        myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        connect();
        // This activity doesn't do anything yet - it immediately launches the game activity
        // Work on it will start in Checkpoint 1
        // Intents are Android's way of specifying what to do/launch
        // Here we create an Intent for launching GameActivity and act on it with startActivity
        // End this activity so that it's removed from the history
        // Otherwise pressing the back button in the game would come back to a blank screen here
        finish();
    }

    // The functions below are stubs that will be filled out in Checkpoint 2

    /**
     * Starts an attempt to connect to the server to fetch/refresh games.
     */
    private void connect() {
        WebApi.startRequest(this, WebApi.API_BASE + "/games", response -> {
            // Code in this handler will run when the request completes successfully
            // Do something with the response?
            JsonArray gamesArray = response.get("games").getAsJsonArray();
            LinearLayout parentongoingGame = findViewById(R.id.ongoingGamesList);
            LinearLayout parentinvitations = findViewById(R.id.invitationsList);
            String[] teamName = getResources().getStringArray(R.array.team_choices);
            for (JsonElement y : gamesArray) {
                JsonObject asObject = y.getAsJsonObject();
                String ownerEmail  = asObject.get("owner").getAsString();
                int gameState = asObject.get("state").getAsInt();
                String gameMode = asObject.get("mode").getAsString();

                JsonArray playersArray = asObject.get("players").getAsJsonArray();

                for (JsonElement d : playersArray) {
                    JsonObject dAsObject = d.getAsJsonObject();
                    String playerEmail = dAsObject.get("email").getAsString();
                    int playerTeam = dAsObject.get("team").getAsInt();
                    int playerState = dAsObject.get("state").getAsInt();

                    if (gameState == GameStateID.RUNNING || gameState == GameStateID.PAUSED) {
                        if (playerState != PlayerStateID.REMOVED) {
                            if (myEmail.equals(playerEmail)) {
                                if (playerState == PlayerStateID.INVITED) {
                                    findViewById(R.id.invitationsGroup).setVisibility(View.VISIBLE);
                                    View invitationsChunk = getLayoutInflater().inflate(
                                            R.layout.chunk_invitations, parentinvitations,
                                            false);
                                    TextView senderEmail = invitationsChunk.findViewById(R.id.owner);
                                    senderEmail.setText("Created by: " + ownerEmail);
                                    TextView senderGameMode = invitationsChunk.findViewById(R.id.gamemode);
                                    senderGameMode.setText(gameMode + " mode");
                                    TextView senderRoleTeam = invitationsChunk.findViewById(R.id.roleteam);
                                    senderRoleTeam.setText(teamName[playerTeam]);
                                    // Do something with any other views in the chunk...
                                    parentinvitations.addView(invitationsChunk);
                                }
                                if (playerState == PlayerStateID.PLAYING || playerState == PlayerStateID.ACCEPTED) {
                                    findViewById(R.id.ongoingGamesGroup).setVisibility(View.VISIBLE);
                                    View ongoingGameChunk = getLayoutInflater().inflate(
                                            R.layout.chunk_ongoing_game, parentongoingGame,
                                            false);
                                    TextView playingEmail = ongoingGameChunk.findViewById(R.id.OGemail);
                                    playingEmail.setText("Created by " + ownerEmail);
                                    TextView playingTeam = ongoingGameChunk.findViewById(R.id.OGteam);
                                    playingTeam.setText(teamName[playerTeam]);
                                    TextView playingMode = ongoingGameChunk.findViewById(R.id.OGgamemode);
                                    playingMode.setText(gameMode + " mode");
                                    parentongoingGame.addView(ongoingGameChunk);
                                }
                            }
                        }
                    }
                }
            }

        }, error -> {
                System.out.println("An Error has Occurred.");
                // Code in this handler will run if the request fails
                // Maybe notify the user of the error?
                Toast.makeText(this, "Oh no!", Toast.LENGTH_LONG).show();
            });
        // Make any "loading" UI adjustments you like
        // Use WebApi.startRequest to fetch the games lists
        // In the response callback, call setUpUi with the received data
    }

    /**
     * Populates the games lists UI with data retrieved from the server.
     *
     * @param result parsed JSON from the server
     */
    private void setUpUi(final JsonObject result) {

        // View ongoingGameChunk = getLayoutInflater().inflate(R.layout.chunk_ongoing_game, parentongoingGame, false);
        // View invitationsChunk = getLayoutInflater().inflate(R.layout.chunk_invitations, parent, false);
        // Hide any optional "loading" UI you added
        // Clear the games lists
        // Add UI chunks to the lists based on the result data
    }

    /**
     * Enters a game (shows the map).
     *
     * @param gameId the ID of the game to enter
     */
    private void enterGame(final String gameId) {
        // Launch GameActivity with the game ID in an intent extra
        // Do not finish - the user should be able to come back here
    }

    /**
     * Creates intent to open up NewGameActivity.
     */
    private void createGameWhenClicked() {
        startActivity(new Intent(this, NewGameActivity.class));
    }
}
