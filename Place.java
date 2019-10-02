package signpost;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

/** An (X, Y) position on a Signpost puzzle board.  We require that
 *  X, Y >= 0.  Each Place object is unique; no other has the same x and y
 *  values.  As a result, "==" may be used for comparisons.
 *  @author Vineet Vashist
 */
class Place {

    /** Convenience list-of-Place class.  (Defining this allows one to create
     *  arrays of lists without compiler warnings.) */
    static class PlaceList extends ArrayList<Place> {
        /** Initialize empty PlaceList. */
        PlaceList() {
        }

        /** Initialze PlaceList from a copy of INIT. */
        PlaceList(List<Place> init) {
            super(init);
        }
    }

    /** The position (X0, Y0), where X0, Y0 >= 0. */
    private Place(int x0, int y0) {
        x = x0; y = y0;
    }

    /** Return the position (X, Y).  This is a factory method that
     *  creates a new Place only if needed by caching those that are
     *  created. */
    static Place pl(int x, int y) {
        assert x >= 0 && y >= 0;
        int s = max(x, y);
        if (s >= _places.length) {
            Place[][] newPlaces = new Place[s + 1][s + 1];
            for (int i = 0; i < _places.length; i += 1) {
                System.arraycopy(_places[i], 0, newPlaces[i], 0,
                                 _places.length);
            }
            _places = newPlaces;
        }
        if (_places[x][y] == null) {
            _places[x][y] = new Place(x, y);
        }
        return _places[x][y];
    }

    /** Returns the direction from (X0, Y0) to (X1, Y1), if we are a queen
     *  move apart.  If not, returns 0. The direction returned (if not 0)
     *  will be an integer 1 <= dir <= 8 corresponding to the definitions
     *  in Model.java */
    static int dirOf(int x0, int y0, int x1, int y1) {
        int dx = x1 < x0 ? -1 : x0 == x1 ? 0 : 1;
        int dy = y1 < y0 ? -1 : y0 == y1 ? 0 : 1;
        if (dx == 0 && dy == 0) {
            return 0;
        }
        if (dx != 0 && dy != 0 && Math.abs(x0 - x1) != Math.abs(y0 - y1)) {
            return 0;
        }

        return dx > 0 ? 2 - dy : dx == 0 ? 6 + 2 * dy : 6 + dy;
    }

    /** Returns the direction from me to PLACE, if we are a queen
     *  move apart.  If not, returns 0. */
    int dirOf(Place place) {
        return dirOf(x, y, place.x, place.y);
    }

    /** If (x1, y1) is the adjacent square in  direction DIR from me, returns
     *  x1 - x. */
    static int dx(int dir) {
        return DX[dir];
    }

    /** If (x1, y1) is the adjacent square in  direction DIR from me, returns
     *  y1 - y. */
    static int dy(int dir) {
        return DY[dir];
    }

    /** Return an array, M, such that M[x][y][dir] is a list of Places that are
     *  one queen move away from square (x, y) in direction
     *  dir on a WIDTH x HEIGHT board.
     *  Additionally, M[x][y][0] is a list of all Places that
     *  are a queen move away from (x, y) in any direction
     *  (the union of the lists of queen moves in directions 1-8). */

    static PlaceList[][][] successorCells(int width, int height) {
        PlaceList[][][] M = new PlaceList[width][height][9];
        int lim = Math.max(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                PlaceList[] temp = new PlaceList[9];
                for (int i = 0; i < 9; i++) {
                    temp[i] = new PlaceList();
                }
                for (int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        int dir = dirOf(x, y, i, j);
                        switch (dir) {
                        case 1:
                            temp[1].add(new Place(i, j));
                            temp[0].add(new Place(i, j));
                            break;
                        case 2:
                            temp[2].add(new Place(i, j));
                            temp[0].add(new Place(i, j)); break;
                        case 3:
                            temp[3].add(new Place(i, j));
                            temp[0].add(new Place(i, j)); break;

                        case 4:
                            temp[4].add(new Place(i, j));
                            temp[0].add(new Place(i, j)); break;

                        case 5:
                            temp[5].add(new Place(i, j));
                            temp[0].add(new Place(i, j)); break;
                        case 6:
                            temp[6].add(new Place(i, j));
                            temp[0].add(new Place(i, j)); break;

                        case 7:
                            temp[7].add(new Place(i, j));
                            temp[0].add(new Place(i, j)); break;
                        case 8:
                            temp[8].add(new Place(i, j));
                            temp[0].add(new Place(i, j)); break;
                        default: break;
                        }
                    }
                }
                for (int i = 0; i < 9; i++) {
                    M[x][y][i] = temp[i];
                }
            }
        }
        return M;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Place)) {
            return false;
        }
        Place other = (Place) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return (x << 16) + y;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }

    /** X displacement of adjacent squares, indexed by direction. */
    static final int[] DX = { 0, 1, 1, 1, 0, -1, -1, -1, 0 };

    /** Y displacement of adjacent squares, indexed by direction. */
    static final int[] DY = { 0, 1, 0, -1, -1, -1, 0, 1, 1 };

    /** Coordinates of this Place. */
    protected final int x, y;

    /** Places already generated. */
    private static Place[][] _places = new Place[10][10];


}
