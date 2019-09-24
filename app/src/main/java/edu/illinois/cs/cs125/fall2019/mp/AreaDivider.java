package edu.illinois.cs.cs125.fall2019.mp;

public class AreaDivider {
    /**
     * @param sNorth is the latitude of the north boundary
     */
    private double sNorth;
    /**
     * @param sEast is the longitude of the east boundary
     */
    private double sEast;
    /**
     * @param sSouth is the latitude of the south boundary
     */
    private double sSouth;
    /**
     * @param sWest is the longitude of the west boundary
     */
    private double sWest;
    /**
     * @param sCellSize is the requested side length of each cell in meters.
     */
    private double sCellSize;

        public AreaDivider(final double east,
                           final double north,
                           final double south,
                           final double west,
                           final double cellSize) {
            north = sNorth;
            east = sEast;
            south = sSouth;
            west = sWest;
            cellSize = sCellSize;

    }

    /**
     * @param x is the cell's X coordinate
     * @param y is the cell's Y coordinate
     * @return the boundaries of the cell
     */
    public com.google.android.gms.maps.model.LatLngBounds getCellBounds(final int x, final int y) {
            return null;
    }


}
