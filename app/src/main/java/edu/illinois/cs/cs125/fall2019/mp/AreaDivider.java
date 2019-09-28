package edu.illinois.cs.cs125.fall2019.mp;

import com.google.android.gms.maps.model.LatLng;

/**
 * Divides a rectangular area into identically sized, roughly square cells.
 */
public class AreaDivider extends java.lang.Object {
    /**
     * north is the latitude of the north boundary.
     */
    private double north;
    /**
     * east is the longitude of the east boundary.
     */
    private double east;
    /**
     * south is the latitude of the south boundary.
     */
    private double south;
    /**
     * west is the longitude of the west boundary.
     */
    private double west;
    /**
     * cellSize is the requested side length of each cell in meters.
     */
    private double cellSize;
    /**
     * southWest is the latitude and Longitude of the southwest corner.
     */
    private LatLng southWest;
    /**
     * northWest is the latitude and Longitude of the northwest corner.
     */
    private LatLng northWest;
    /**
     * northEast is the latitude and Longitude of the northeast corner.
     */
    private LatLng northEast;
    /**
     * southEast is the latitude and Longitude of the southeast corner.
     */
    private LatLng southEast;

    /**
     * gets the width of the cell.
     */
    private double cellWidth;

    /**
     * gets thje height of the cell.
     */
    private double cellHeight;



    /**
     * @param setNorth    sets the latitude of the north boundary
     * @param setEast     sets the longitude of the east boundary
     * @param setSouth    latitude of the south boundary
     * @param setWest     longitude of the west boundary
     * @param setCellSize the requested side length of each cell, in meters
     *                    creates an areaDivider for an area
     */

    AreaDivider(final double setNorth, final double setEast, final double setSouth,
                final double setWest, final double setCellSize) {
        north = setNorth;
        east = setEast;
        south = setSouth;
        west = setWest;
        cellSize = setCellSize;

        southWest = new LatLng(setSouth, setWest);
        northWest = new LatLng(setNorth, setWest);
        northEast = new LatLng(setNorth, setEast);
        southEast = new LatLng(setSouth, setEast);
        cellWidth = (setEast - setWest) / getXCells();
        cellHeight = (setNorth - setSouth) / getYCells();


    }

    /**
     * @param x is the cell's X coordinate
     * @param y is the cell's Y coordinate
     * @return the boundaries of the cell
     */
    public com.google.android.gms.maps.model.LatLngBounds getCellBounds(final int x,
                                                                        final int y) {

    }

    /**
     * getXCells gets the number of cells between the west and east boundaries.
     *
     * @return the number of Cells in the X direction
     */
    public int getXCells() {

        double distance = (LatLngUtils.distance(northWest, northEast));
        int newDistance = (int) java.lang.Math.ceil(distance);
        double numberOfCells = newDistance / cellSize;
        return (int) java.lang.Math.ceil(numberOfCells);
    }

    /**
     * @param location gets the x coordinate of the cell containing of the cell containing the specified location
     * @return the X coordinate of the cell containing the specified latitude-longitude point
     */
    public int getXCoordinate(final com.google.android.gms.maps.model.LatLng location) {
        double distance = (LatLngUtils.distance(location.latitude, location.longitude, location.latitude, west));
        System.out.println(distance);
        double output = distance / cellSize;
        return (int) output;

    }

    /**
     * getYCells Gets the number of cells between the south and north boundaries.
     *
     * @return the number of cells in the Y direction
     */
    public int getYCells() {
        double distance = (LatLngUtils.distance(southWest, northWest));
        int newDistance = (int) java.lang.Math.ceil(distance);
        double numberOfCells = newDistance / cellSize;
        return (int) java.lang.Math.ceil(numberOfCells);
    }

    /**
     * @param location the location
     * @return the Y coordinate of the cell containing the specified latitude-longitude point
     */
    public int getYCoordinate(final com.google.android.gms.maps.model.LatLng location) {
        double distance = (LatLngUtils.distance(location.latitude, location.longitude, south, location.longitude));
        System.out.println(distance);
        double output = distance / cellSize;
        return (int) output;
    }

    /**
     * Draws the grid to a map using solid black polylines.
     *
     * @param map the Google map to draw on
     */
    public void renderGrid(final com.google.android.gms.maps.GoogleMap map) {
    }

}
