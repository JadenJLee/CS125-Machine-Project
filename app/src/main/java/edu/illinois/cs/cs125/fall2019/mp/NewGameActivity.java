package edu.illinois.cs.cs125.fall2019.mp;

import android.content.Intent;
import android.graphics.Point;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game creation screen, where the user configures a new game.
 */
public final class NewGameActivity extends AppCompatActivity {

    // This activity doesn't do much at first - it'll be worked on in Checkpoints 1 and 3

    /**
     * The Google Maps view used to set the area for area mode. Null until getMapAsync finishes.
     */
    private GoogleMap areaMap;

    /**
     * The Google Maps view used to set the area for target mode. Null until getMapAsync finishes.
     */
    private GoogleMap targetMap;

    /**
     * List of Markers for the various targets.
     */
    private List<Marker> targets = new ArrayList<>();

    /**
     * List of invitees invited to a certain game.
     */
    private List<Invitee> invitees = new ArrayList<>();

    /**
     * the mode selected on the radiogroup.
     */
    private RadioGroup selectedMode;

    /**
     * Called by the Android system when the activity is created.
     *
     * @param savedInstanceState state from the previously terminated instance (unused)
     */
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game); // app/src/main/res/layout/activity_new_game.xml
        setTitle(R.string.create_game); // Change the title in the top bar
        // Now that setContentView has been called, findViewById and findFragmentById work

        // Find the Google Maps component for the area map
        SupportMapFragment areaMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.areaSizeMap);
        // Start the process of getting a Google Maps object
        areaMapFragment.getMapAsync(newMap -> {
            // NONLINEAR CONTROL FLOW: Code in this block is called later, after onCreate ends
            // It's a "callback" - it will be called eventually when the map is ready

            // Set the map variable so it can be used by other functions
            areaMap = newMap;
            // Center it on campustown
            centerMap(areaMap);
        });

        SupportMapFragment targetsMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.targetsMap);
        targetsMapFragment.getMapAsync(newMap -> {
            targetMap = newMap;
            centerMap(targetMap);

            targetMap.setOnMapLongClickListener(location -> {
                MarkerOptions options = new MarkerOptions().position(location);
                // Add it to the map - Google Maps gives us the created Marker
                Marker marker = targetMap.addMarker(options);
                targets.add(marker);
                // Code here runs whenever the user presses on the map.
                // location is the LatLng position where the user pressed.
                // 1. Create a Google Maps Marker at the provided coordinates.
                // 2. Add it to your targets list instance variable.
            });

            targetMap.setOnMarkerClickListener(clickedMarker -> {
                clickedMarker.remove();
                targets.remove(clickedMarker);
                // Code here runs whenever the user taps a marker.
                // clickedMarker is the Marker object the user clicked.
                // 1. Remove the marker from the map with its remove function.
                // 2. Remove it from your targets list.
                return true; // This makes Google Maps not pan the map again
            });
        });

        Invitee user = new Invitee(FirebaseAuth.getInstance().getCurrentUser().getEmail(), TeamID.OBSERVER);
        invitees.add(user);
        updatePlayersUI();
        System.out.println(user);
        System.out.println(invitees.get(0));

        /*
         * Setting an ID for a control in the UI designer produces a constant on R.id
         * that can be passed to findViewById to get a reference to that control.
         * Here we get a reference to the Create Game button.
         */
        Button createGame = findViewById(R.id.createGame);
        /*
         * Now that we have a reference to the control, we can use its setOnClickListener
         * method to set the handler to run when the user clicks the button. That function
         * takes an OnClickListener instance. OnClickListener, like many types in Android,
         * has exactly one function which must be filled out, so Java allows instances of it
         * to be written as "lambdas", which are like small functions that can be passed around.
         * The part before the arrow is the argument list (Java infers the types); the part
         * after is the statement to run. Here we don't care about the argument, but it must
         * be there for the signature to match.
         */
        createGame.setOnClickListener(unused -> createGameClicked());
        /*
         * It's also possible to make lambdas for functions that take zero or multiple parameters.
         * In those cases, the parameter list needs to be wrapped in parentheses, like () for a
         * zero-argument lambda or (someArg, anotherArg) for a two-argument lambda. Lambdas that
         * run multiple statements, like the one passed to getMapAsync above, look more like
         * normal functions in that they need their body wrapped in curly braces. Multi-statement
         * lambdas for functions with a non-void return type need return statements, again like
         * normal functions.
         */
//        finish();

        // Suppose modeGroup is a RadioGroup variable (maybe an instance variable?)
        RadioGroup modeGroup = findViewById(R.id.gameModeGroup);
        modeGroup.setOnCheckedChangeListener((unused, checkedId) -> {
            if (checkedId == R.id.targetModeOption) {
                LinearLayout targetSettings = findViewById(R.id.targetSettings);
                LinearLayout areaSettings = findViewById(R.id.areaSettings);
                targetSettings.setVisibility(View.VISIBLE);
                areaSettings.setVisibility(View.GONE);

            } else if (checkedId == R.id.areaModeOption) {
                LinearLayout areaSettings = findViewById(R.id.areaSettings);
                LinearLayout targetSettings = findViewById(R.id.targetSettings);
                areaSettings.setVisibility(View.VISIBLE);
                targetSettings.setVisibility(View.GONE);
            }
            // checkedId is the R.id constant of the currently checked RadioButton
            // Your code here: make only the selected mode's settings group visible
        });
        Button addButton = findViewById(R.id.addInvitee);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                addInvitee();
                // Code here executes on main thread after user presses button
            }
        });
    }

    /**
     * updates the players list UI.
     */
    private void updatePlayersUI() {
        LinearLayout playersLayout = findViewById(R.id.playersList);
        playersLayout.removeAllViews();
        for (Invitee player : invitees) {
            View playersChunk = getLayoutInflater().inflate(
                    R.layout.chunk_invitee, playersLayout, false);
            TextView emailLabel = playersChunk.findViewById(R.id.inviteeEmail);
            Spinner inviteesTeam = playersChunk.findViewById(R.id.inviteeTeam);
            Button removeButton = playersChunk.findViewById(R.id.removeInvitee);

            emailLabel.setText(player.getEmail());

            inviteesTeam.setSelection(player.getTeamId());

            if (player.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                removeButton.setVisibility(View.GONE);
            }

            inviteesTeam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(final AdapterView<?> parent, final View view,
                                           final int position, final long id) {
                    // Called when the user selects a different item in the dropdown
                    // The position parameter is the selected index
                    // The other parameters can be ignored
                    player.setTeamId(position);
                }
                @Override
                public void onNothingSelected(final AdapterView<?> parent) {
                    // Called when the selection becomes empty
                    // Not relevant to the MP - can be left blank
                }
            });
            removeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    invitees.remove(player);
                    updatePlayersUI();
                    // Code here executes on main thread after user presses button
                }
            });
            playersLayout.addView(playersChunk);
        }
    }

    /**
     * adding and invitee.
     */
    private void addInvitee() {
        TextView inviteeEmail = findViewById(R.id.newInviteeEmail);
        String invitedEmail = inviteeEmail.getText().toString();

        if (invitedEmail.length() != 0) {
            Invitee newInvite = new Invitee(invitedEmail, TeamID.OBSERVER);
            invitees.add(newInvite);
            updatePlayersUI();
            inviteeEmail.setText("");
        }
    }



    /**
     * Sets up the area sizing map with initial settings: centering on campustown.
     * <p>
     * You don't need to alter or understand this function, but you will want to use it when
     * you add another map control in Checkpoint 3.
     *
     * @param map the map to center
     */
    private void centerMap(final GoogleMap map) {
        // Bounds of campustown and some surroundings
        final double swLatitude = 40.098331;
        final double swLongitude = -88.246065;
        final double neLatitude = 40.116601;
        final double neLongitude = -88.213077;

        // Get the window dimensions (for the width)
        Point windowSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(windowSize);

        // Convert 300dp (height of map control) to pixels
        final int mapHeightDp = 300;
        float heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mapHeightDp,
                getResources().getDisplayMetrics());

        // Submit the camera update
        final int paddingPx = 10;
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds(
                new LatLng(swLatitude, swLongitude),
                new LatLng(neLatitude, neLongitude)), windowSize.x, (int) heightPx, paddingPx));
    }

    /**
     * Code to run when the Create Game button is clicked.
     */
    private void createGameClicked() {

        JsonObject gameCreation = new JsonObject();

        RadioGroup gameModeGroup = findViewById(R.id.gameModeGroup);

        //int gameID = selectedMode.getCheckedRadioButtonId();

        Intent intent = new Intent(this, GameActivity.class);
        if (gameModeGroup.getCheckedRadioButtonId() == R.id.targetModeOption) {
            intent.putExtra("mode", "target");
            EditText proximityThresh = findViewById(R.id.proximityThreshold);
            String proximityThreshasText = proximityThresh.getText().toString();
            gameCreation.addProperty("mode", "target");
            JsonArray targetArrays = new JsonArray();

            for (int i = 0; i < targets.size(); i++) {
                JsonObject target = new JsonObject();
                target.addProperty("longitude", targets.get(i).getPosition().longitude);
                target.addProperty("latitude", targets.get(i).getPosition().latitude);
                targetArrays.add(target);
            }
            gameCreation.add("targets", targetArrays);

            if (!proximityThresh.equals("")) {
                int proximity = Integer.parseInt(proximityThreshasText);
                intent.putExtra("proximityThreshold", proximity);

                gameCreation.addProperty("proximityThreshold", proximity);
            }
        }
        if (gameModeGroup.getCheckedRadioButtonId() == R.id.areaModeOption) {
            intent.putExtra("mode", "area");
            EditText mCellSize = findViewById(R.id.cellSize);
            String cellSizeasText = mCellSize.getText().toString();
            if (!cellSizeasText.equals("")) {
                int number = Integer.parseInt(cellSizeasText);
                intent.putExtra("cellSize", number);
                gameCreation.addProperty("cellSize", number);
                LatLngBounds bounds = areaMap.getProjection().getVisibleRegion().latLngBounds;
                intent.putExtra("areaNorth", bounds.northeast.latitude);
                intent.putExtra("areaEast", bounds.northeast.longitude);
                intent.putExtra("areaSouth", bounds.southwest.latitude);
                intent.putExtra("areaWest", bounds.southwest.longitude);

                gameCreation.addProperty("areaNorth", bounds.northeast.latitude);
                gameCreation.addProperty("areaEast", bounds.northeast.longitude);
                gameCreation.addProperty("areaSouth", bounds.southwest.latitude);
                gameCreation.addProperty("areaWest", bounds.southwest.longitude);
            }
            gameCreation.addProperty("mode", "area");
        }

        JsonArray invitedList = new JsonArray();

        for (int i = 0; i < invitees.size(); i++) {
            JsonObject invitedPlayer = new JsonObject();
            invitedPlayer.addProperty("team", invitees.get(i).getTeamId());
            invitedPlayer.addProperty("email", invitees.get(i).getEmail());
            invitedList.add(invitedPlayer);
        }
        gameCreation.add("invitees", invitedList);




        //Intent intent = new Intent(this, GameActivity.class);

        // Set up an Intent that will launch GameActivity
        boolean clicked = (gameModeGroup.getCheckedRadioButtonId() != -1);

        if (!clicked) {
            return;
        }

        if (gameModeGroup.getCheckedRadioButtonId() == R.id.targetModeOption) {
            System.out.println("target mode button was clicked!");
            intent.putExtra("mode", "target");
        } else if (gameModeGroup.getCheckedRadioButtonId() == R.id.areaModeOption) {
            System.out.println("area mode button was clicked!");
            intent.putExtra("mode", "area");
        }
        if (intent.getStringExtra("mode").equals("target")) {
            EditText mProximityThreshold = findViewById(R.id.proximityThreshold);
            String text = mProximityThreshold.getText().toString();
            int number;
            try {
                number = Integer.parseInt(text); // This will crash if text isn't a number
                intent.putExtra("proximityThreshold", number);
                System.out.println(intent.getIntExtra("proximityThreshold", 0));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number!");
                return;
            }
        } else if (intent.getStringExtra("mode").equals("area")) {
            EditText mCellSize = findViewById(R.id.cellSize);
            String text = mCellSize.getText().toString();
            int number;
            try {
                number = Integer.parseInt(text); // This will crash if text isn't a number
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number!");
                return;
            }
            intent.putExtra("cellSize", number);
            LatLngBounds bounds = areaMap.getProjection().getVisibleRegion().latLngBounds;
            intent.putExtra("areaNorth", bounds.northeast.latitude);
            intent.putExtra("areaEast", bounds.northeast.longitude);
            intent.putExtra("areaSouth", bounds.southwest.latitude);
            intent.putExtra("areaWest", bounds.southwest.longitude);
        }
        //startActivity(intent);
        //finish();
        // Complete this function so that it populates the Intent with the user's settings (using putExtra)
        // If the user has set all necessary settings, launch the GameActivity and finish this activity
        WebApi.startRequest(this, WebApi.API_BASE + "/games/create", Request.Method.POST, gameCreation, response -> {
            intent.putExtra("game", response.get("game").getAsString());
            startActivity(intent);
            finish();
            // Code in this handler will run when the request completes successfully
            // Do something with the response?
        }, error -> {
            // Code in this handler will run if the request fails
            // Maybe notify the user of the error?
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
            });
    }
}
