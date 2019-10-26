package edu.illinois.cs.cs125.fall2019.mp;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

/**
 * Represents a target in an ongoing target-mode game and manages the marker displaying it.
 */
public class Target {
    /**
     * An icon placed at a particular point on the map's surface.
     * A marker icon is drawn oriented against the device's screen rather than the map's surface;
     * i.e., it will not necessarily change orientation due to map rotations, tilting, or zooming.
     */
    private Marker marker;

    /**
     * Setting any options for the created Target.
     */
    private MarkerOptions options;

    /**
     * The position of the Target Object.
     */
    private LatLng position;

    /**
     * the ID of the team currently owning the Target Object.
     */
    private int team;

    /**
     * constructor for the Target class.
     * @param setMap - the map to render to
     * @param setPosition - the position of the target
     * @param setTeam - the TeamID code of the team currently owning the target
     */
    public Target(final com.google.android.gms.maps.GoogleMap setMap,
                  final com.google.android.gms.maps.model.LatLng setPosition,
                  final int setTeam) {
        position = setPosition;
        // Suppose position is a LatLng variable
        options = new MarkerOptions().position(position);
        // Set any other options you like?
        marker = setMap.addMarker(options);
        if (setTeam == TeamID.TEAM_RED) {
            team = TeamID.TEAM_RED;
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_RED));
        } else if (setTeam == TeamID.TEAM_YELLOW) {
            team = TeamID.TEAM_YELLOW;
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_YELLOW));
        } else if (setTeam == TeamID.TEAM_GREEN) {
            team = TeamID.TEAM_GREEN;
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_GREEN));
        } else if (setTeam == TeamID.TEAM_BLUE) {
            team = TeamID.TEAM_BLUE;
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_BLUE));
        } else {
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_VIOLET));
        }
    }

    /**
     * @return the postion of the Target
     */
    public com.google.android.gms.maps.model.LatLng getPosition() {
        return position;
    }

    /**
     * @return the current Team ID of the team owning the Target.
     */
    public int getTeam() {
        return team;
    }

    /**
     * @param newTeam the new team that captured the target.
     */
    public void setTeam(final int newTeam) {
        team = newTeam;
        if (team == TeamID.TEAM_RED) {
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_RED));
        } else if (team == TeamID.TEAM_YELLOW) {
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_YELLOW));
        } else if (team == TeamID.TEAM_GREEN) {
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_GREEN));
        } else if (team == TeamID.TEAM_BLUE) {
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_BLUE));
        } else {
            options.icon(BitmapDescriptorFactory.defaultMarker(HUE_VIOLET));
        }
    }
}
