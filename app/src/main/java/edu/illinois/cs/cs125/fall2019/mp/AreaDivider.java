package edu.illinois.cs.cs125.fall2019.mp;

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
    }

    /**
     * @param x is the cell's X coordinate
     * @param y is the cell's Y coordinate
     * @return the boundaries of the cell
     */
    public com.google.android.gms.maps.model.LatLngBounds getCellBounds(final int x, final int y) {
        return null;
    }

    /**
     * getXCells gets the number of cells between the west and east boundaries.
     *
     * @return the number of Cells in the X direction
     */
    public int getXCells() {

        double distance = (LatLngUtils.distance(0, west, 0, east));
        System.out.println(distance);
        double numberOfCells = distance / cellSize;
        System.out.println(cellSize);
        // return (int) java.lang.Math.ceil(numberOfCells);
        return 0;
    }

    /**
     * @param location gets the x coordinate of the cell containing of the cell containing the specified location
     * @return the X coordinate of the cell containing the specified latitude-longitude point
     */
    public int getXCoordinate(final com.google.android.gms.maps.model.LatLng location) {
        return 0;
    }

    /**
     * getYCells Gets the number of cells between the south and north boundaries.
     *
     * @return the number of cells in the Y direction
     */
    public int getYCells() {
        double distance = (LatLngUtils.distance(north, 0, south, 0));
        System.out.println(distance);
        double numberOfCells = distance / cellSize;
        System.out.println(cellSize);
        // return (int) java.lang.Math.ceil(numberOfCells);
        return 0;
    }

    /**
     * @param location the location
     * @return the Y coordinate of the cell containing the specified latitude-longitude point
     */
    public int getYCoordinate(final com.google.android.gms.maps.model.LatLng location) {
        return 0;
    }

    /**
     * Draws the grid to a map using solid black polylines.
     *
     * @param map the Google map to draw on
     */
    public void renderGrid(final com.google.android.gms.maps.GoogleMap map) {
    }

}
