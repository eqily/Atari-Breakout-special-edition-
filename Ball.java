import javax.swing.ImageIcon;

public class Ball extends Sprite {
    private int xVal;
    private int yVal;

    public Ball() {
        initBall();
    }

    private void initBall() {
        xVal = 1;
        yVal = -1;

        loadImage();
        getImageDimensions();
        resetState();
    }

    private void loadImage() {
        var ii = new ImageIcon("heart.png");
        image = ii.getImage();
    }

    void move() {
      //give ball x and y velocity components
        x += xVal;
        y += yVal;

        if (x == 0) {
            setXVal(1);
        }

        if (x == Commons.WIDTH - imageWidth) {
            setXVal(-1);
        }

        if (y == 0) {
            setYVal(1);
        }
    }

    private void resetState(){
        x = Commons.INIT_BALL_X;
        y = Commons.INIT_BALL_Y;
    }

    public void setXVal(int x){
        xVal = x;
    }

    public void setYVal(int y){
        yVal = y;
    }

    public int getYVal(){
        return yVal;
    }

    public int getXVal(){
      return xVal;
    }

}