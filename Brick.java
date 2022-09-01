import javax.swing.ImageIcon;

public class Brick extends Sprite {
    private boolean isBroken;

    public Brick(int x, int y) {
        initBrick(x, y);
    }
    
    private void initBrick(int x, int y) {
        this.x = x;
        this.y = y;
        
        isBroken = false;

        loadImage();
        getImageDimensions();
    }
    
    private void loadImage() {
        var ii = new ImageIcon("never brick.jpg");
        image = ii.getImage();        
    }

    boolean getIsBroken() {
        return isBroken;
    }

    void setBroken(boolean val) {
        isBroken = val;
    }
}