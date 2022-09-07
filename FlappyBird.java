package com.company;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, KeyListener {

    public static FlappyBird flappyBird;
    public Renderer renderer;
    public Rectangle bird;
    public ArrayList<Rectangle> obstacles;
    public int yMotion, score;
    public boolean end = false;
    public boolean start;
    public Random rand;
    public Timer timer;

    public FlappyBird() {
        JFrame jframe = new JFrame();

        renderer = new Renderer();
        rand = new Random();
        bird = new Rectangle(375,375, 20, 20);
        obstacles = new ArrayList<>();


        jframe.add(renderer);
        jframe.setTitle("CS Final");
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(800, 800);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setVisible(true);

        addObstacle(true);
        addObstacle(true);
        addObstacle(true);
        addObstacle(true);

        timer = new Timer(20, this);
        timer.start();
    }

    public static void main(String[] args) {
        flappyBird = new FlappyBird();
    }

    public void addObstacle(boolean start) {
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);

        if (start) {
            obstacles.add(new Rectangle(900 + obstacles.size() * 300, 800 - height - 120, width, height));
            obstacles.add(new Rectangle(900 + (obstacles.size() - 1) * 300, 0, width, 800 - height - space));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 10;

        if (start) {
            for (Rectangle column : obstacles) {
                column.x -= speed;
            }
            if (yMotion < 15) {
                yMotion += 2;
            }

            for (int i = 0; i < obstacles.size(); i++) {
                Rectangle column = obstacles.get(i);
                if (column.x + column.width < 0) {
                    obstacles.remove(column);
                    if (column.y == 0) {
                        addObstacle(false);
                    }
                }
            }

            bird.y += yMotion;

            for (Rectangle column : obstacles) {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) {
                    score++;
                }

                if (column.intersects(bird)) {
                    end = true;

                    if (bird.x <= column.x) {
                        bird.x = column.x - bird.width;

                    }
                    else {
                        if (column.y != 0) {
                            bird.y = column.y - bird.height;
                        }
                        else if (bird.y < column.height) {
                            bird.y = column.height;
                        }
                    }
                }
            }

            if (bird.y > 680 || bird.y < 0) {
                end = true;
            }

            if (bird.y + yMotion >= 680) {
                bird.y = 680 - bird.height;
                end = true;
            }
        }
        renderer.repaint();
    }

    public void paintColumn(Graphics g, Rectangle obstacles) {
        g.setColor(Color.black);
        g.fillRect(obstacles.x, obstacles.y, obstacles.width, obstacles.height);
    }

    public void repaint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, 800, 800);

        g.setColor(Color.gray);
        g.fillRect(0, 680, 800, 200);

        g.setColor(Color.red);
        g.fillOval(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : obstacles) {
            paintColumn(g, column);
        }

        g.setColor(Color.black);

        if (!start) {
            g.drawString("Start", 375, 100);
        }

        if (end) {
            g.drawString("Game Over!", 375, 100);
            obstacles.clear();
        }

        if (!end && start) {
            g.drawString(String.valueOf(score), 375, 100);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!start) {
                start = true;
            }
            else {
                if (yMotion > 0) {
                    yMotion = 0;
                }

                yMotion -= 10;
            }
            if (end){
                boolean b = e.getKeyCode() == KeyEvent.VK_BACK_SPACE;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (end) {

                bird = new Rectangle(375,375, 20, 20);
                obstacles.clear();
                yMotion = 0;
                score = 0;

                addObstacle(true);
                addObstacle(true);
                addObstacle(true);
                addObstacle(true);

                end = false;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }
}