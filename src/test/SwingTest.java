import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class SwingTest implements ActionListener{

    JFrame frame;
    TestPanel testPanel;
    JButton start;
    Timer timer = new Timer(1000/60, this);
    Random rand = new Random();
    int value = 0;

    Rectangle ball = new Rectangle(32 * 2, 32 * 2);
    BufferedImage ballTexture;

    Rectangle barrier1 = new Rectangle(3 * 4,32 * 4);
    Rectangle barrier2 = new Rectangle(3 * 4,32 * 4);
    BufferedImage barrierTexture;

    int ballMotionX;
    int ballMotionY;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingTest::new);

    }

    public SwingTest() {
        timer.setActionCommand("timer");
        frame = new JFrame("SwingTest");
        frame.setSize(400, 400);

        try{
            barrierTexture = ImageIO.read(new File("src/test/bar.png"));
            ballTexture = ImageIO.read(new File("src/test/ball.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        testPanel = this.new TestPanel();
        testPanel.setPreferredSize(new Dimension(800,500));
        testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.PAGE_AXIS));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        start = new JButton("Start");
        start.addActionListener(this);
        start.setActionCommand("Start");
        start.setAlignmentX(Component.CENTER_ALIGNMENT);

        testPanel.add(Box.createRigidArea(new Dimension(40,20)));
        testPanel.add(start);
        testPanel.add(Box.createRigidArea(new Dimension(300, 425)));
        JLabel label = new JLabel("Start button can be reused to reroll ball velocity");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        testPanel.add(label);

        frame.setContentPane(testPanel);

        frame.pack();
        frame.setVisible(true);

    }

    boolean first = true;
    private class TestPanel extends JPanel{
        public TestPanel() {
            // TODO Auto-generated constructor stub
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.clearRect(this.getHeight()/2, this.getWidth()/2, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.fillRect(this.getWidth()/2 - 350, this.getHeight()/2 - 200, 700, 400);
            g.setColor(Color.black);
            g.drawRect(this.getWidth()/2 - 350, this.getHeight()/2- 200, 700, 400);

            if(first) {
                barrier1.setLocation(200, this.getHeight() / 2);
                barrier2.setLocation(getWidth() - 200, this.getHeight() / 2);
                ball.setLocation(this.getWidth() / 2 - ball.width, this.getHeight() / 2 - ball.height);
                first = false;
            }

            g.drawImage(barrierTexture, barrier1.x, barrier1.y, barrier2.width, barrier2.height, null);
            g.drawImage(barrierTexture, barrier2.x, barrier2.y, barrier2.width, barrier2.height, null);
            g.drawImage(ballTexture, ball.x, ball.y, ball.width, ball.height, null);

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Start")) {
            value = rand.nextInt(1,7);
            timer.start();
            ballMotionX = rand.nextInt(7) - 4;
            ballMotionY = rand.nextInt(7) - 4;
        }
        if(e.getActionCommand().equals("timer")){
            if(ball.x + ballMotionX < 700/2 - 300
                    || ball.x + ballMotionX > 700/2 + 300
                    || ball.intersects(barrier1)
                    || ball.intersects(barrier2))
                ballMotionX *= -1;
            if(ball.y + ballMotionY < 400/2 - 150
                    || ball.y + ballMotionY > 400/2 + 150
                    || ball.intersects(barrier1)
                    || ball.intersects(barrier2))
                ballMotionY *= -1;

            ball.x += ballMotionX;
            ball.y += ballMotionY;
            testPanel.repaint();
        }

    }


}