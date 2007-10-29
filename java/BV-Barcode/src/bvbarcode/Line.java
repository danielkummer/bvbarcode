package bvbarcode;

/**
 *
 * @author kummedan
 */
public class Line {
    
    public Coordinate Start, End;
    
    public Line() {
        Start = new Coordinate();
        End   = new Coordinate();
    }
    
    public Line(int startx, int starty, int endx, int endy) {
        Start = new Coordinate(startx, starty);
        End = new Coordinate(endx, endy);
    }
}
