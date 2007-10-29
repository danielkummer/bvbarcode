package bvbarcode;

/**
 *
 * @author kummedan
 */
public class Coordinate {
    
    public int X;
    public int Y;
    
    public Coordinate() {
        X = 0;
        Y = 0;
    }
    
    public Coordinate(int x, int y) {
        X = x;
        Y = y;
    }
    
    public void set(int x, int y) {
        this.X = x;
        this.Y = y;
    }
}
