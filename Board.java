import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel {

    private Timer timer;
    private String message = "You have been given up :)";
    private String message1 = "press 'x' to doubt";
    private Ball ball;
    private Paddle paddle;
    private Brick[] bricks;
    private boolean inGame = true;

    public Board() {
      if(Math.random() > 0.5){
        message = "Never gonna win this game :)";
      }

        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));

        gameInit();
    }

    private void gameInit() {
        bricks = new Brick[Commons.N_OF_BRICKS];

        ball = new Ball();
        paddle = new Paddle();

        int k = 0;
//create bricks in an array & shift over
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                bricks[k] = new Brick(j * 40 + 30, i * 10 + 50);
                k++;
            }
        }

        timer = new Timer(Commons.PERIOD, new GameCycle());
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2d = (Graphics2D) g;

        if (inGame) {
            drawObjects(g2d);
        } else {
            gameFinished(g2d);
        }

        Toolkit.getDefaultToolkit().sync();//makes graphics run smoother
    }

    private void drawObjects(Graphics2D g2d) {
        g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(), ball.getImageWidth(), ball.getImageHeight(), this);
        g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(), paddle.getImageWidth(), paddle.getImageHeight(), this);

        for (int i = 0; i < Commons.N_OF_BRICKS; i++) {
            if (!bricks[i].getIsBroken()) {//places bricks that are not broken again
                g2d.drawImage(bricks[i].getImage(), bricks[i].getX(),
                        bricks[i].getY(), bricks[i].getImageWidth(),
                        bricks[i].getImageHeight(), this);
            }
        }
    }

    private void gameFinished(Graphics2D g2d) {
        var font = new Font("Comic Sans MS", Font.PLAIN, 18);
        FontMetrics fontMetrics = this.getFontMetrics(font);

        g2d.setColor(Color.BLACK);
        g2d.setFont(font);
        //print message (winning/losing option)
        g2d.drawString(message, (Commons.WIDTH - fontMetrics.stringWidth(message)) / 2, Commons.WIDTH / 2);

        //print replay game message
        font = new Font("Comic Sans MS", Font.ITALIC, 14);
        g2d.setFont(font);
        g2d.drawString(message1, (Commons.WIDTH - fontMetrics.stringWidth(message1)) / 2, ((Commons.WIDTH / 2) + 50));
    }

    class TAdapter extends KeyAdapter {
      //paddle movement key commands
        @Override
        public void keyReleased(KeyEvent e) {
            paddle.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            paddle.keyPressed(e);

      //reset game at end key command
              if(e.getKeyChar() == 'x'){
                if(!inGame){
                  inGame = true;
                  var game = new Breakout();
                  game.setVisible(true);
              }
            }
          }
    }

    class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private void doGameCycle() {
        ball.move();
        paddle.move();
        checkCollision();
        repaint();
    }

    private void stopGame() {
        inGame = false;
        timer.stop();
    }

    private void checkCollision() {
        if (ball.getRect().getMaxY() > Commons.BOTTOM_EDGE) {//if ball exits frame
            stopGame();
        }

        for (int i = 0, j = 0; i < Commons.N_OF_BRICKS; i++) {
            if (bricks[i].getIsBroken()) {//count number of broken bricks
                j++;
            }

            if (j == Commons.N_OF_BRICKS) {//if all bricks broken, set winning statement and stop game
                message = "You are winner!";
                stopGame();
            }
        }

        if ((ball.getRect()).intersects(paddle.getRect())) {//hitting the ball with paddle

            int paddleLPos = (int) paddle.getRect().getMinX();
            int ballLPos = (int) ball.getRect().getMinX();

            int first = paddleLPos + 8;
            int second = paddleLPos + 16;
            int third = paddleLPos + 24;
            int fourth = paddleLPos + 32;

            if (ballLPos < first) {
                ball.setXVal(-1);
                ball.setYVal(-1);
            }

            if (ballLPos >= first && ballLPos < second) {//rebound ball in y direction
                ball.setXVal(-1);
                ball.setYVal(-1 * ball.getYVal());
            }

            if (ballLPos >= second && ballLPos < third) {
                ball.setXVal(0);
                ball.setYVal(-1);
            }

            if (ballLPos >= third && ballLPos < fourth) {//rebound ball
                ball.setXVal(1);
                ball.setYVal(-1 * ball.getYVal());
            }

            if (ballLPos > fourth) {
                ball.setXVal(1);
                ball.setYVal(-1);
            }
        }

        for (int i = 0; i < Commons.N_OF_BRICKS; i++) {
            if ((ball.getRect()).intersects(bricks[i].getRect())) {

                int ballLeft = (int) ball.getRect().getMinX();
                int ballHeight = (int) ball.getRect().getHeight();
                int ballWidth = (int) ball.getRect().getWidth();
                int ballTop = (int) ball.getRect().getMinY();

                var pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
                var pointLeft = new Point(ballLeft - 1, ballTop);
                var pointTop = new Point(ballLeft, ballTop - 1);
                var pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);

                if (!bricks[i].getIsBroken()) {
                    if (bricks[i].getRect().contains(pointRight)) {//rebound ball off brick
                        ball.setXVal(-1);
                    } else if (bricks[i].getRect().contains(pointLeft)) {
                        ball.setXVal(1);
                    }

                    if (bricks[i].getRect().contains(pointTop)) {//rebound off top screen
                        ball.setYVal(1);
                    } else if (bricks[i].getRect().contains(pointBottom)) {
                        ball.setYVal(-1);
                    }

                    bricks[i].setBroken(true);
                }
            }
        }
    }
}