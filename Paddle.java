import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Paddle extends Sprite {
	private int dx;
	
	public Paddle() {
		initPaddle();
	}
	
	private void initPaddle() {
		loadImage();
		getImageDimensions();
		resetState();	
	}
	
	private void loadImage() {
		var ii = new ImageIcon("rick paddle yt.jpg");
		image = ii.getImage();
	}
	
	public void move() {
		x += dx;
		
		if (x <= 0) {
			x = 0;
		}
		
	if (x >= Commons.WIDTH - imageWidth) {
		x = Commons.WIDTH - imageWidth;	
	}
	
	}//end of move method
	
  //shifting paddle left and right with <> arrows
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT) {
			dx = -1;
		}
		
		if (key == KeyEvent.VK_RIGHT) {
			dx = 1;
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if (key == KeyEvent.VK_LEFT) {
			dx = 0;
		}
		
		if (key == KeyEvent.VK_RIGHT) {
			dx = 0;
		}
	}
	
	private void resetState() {
		x = Commons.INIT_PADDLE_X;
		y = Commons.INIT_PADDLE_Y;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}