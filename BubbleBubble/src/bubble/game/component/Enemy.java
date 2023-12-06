package bubble.game.component;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import bubble.game.BubbleFrame;
import bubble.game.Moveable;
import bubble.game.service.BackgroundEnemyService;
import bubble.game.state.EnemyWay;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemy extends JLabel implements Moveable {

    private BubbleFrame mContext;
    private Player player;

    private int x;
    private int y;

    private EnemyWay enemyWay;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    private int state;

    private final int SPEED = 3;
    private final int JUMP = 1;

    private ImageIcon enemyR, enemyL;

    public Enemy(BubbleFrame mContext, EnemyWay enemyWay) {
        this.mContext = mContext;
        this.player = mContext.getPlayer();
        initObject();
        initSetting(enemyWay);
        initBackgroundEnemyService();
    }

    private void initObject() {
        enemyR = new ImageIcon("image/enemyR.png");
        enemyL = new ImageIcon("image/enemyL.png");
    }

    private void initSetting(EnemyWay enemyWay) {
        // �־��� ������ ������� ���� �ʱ� ������ �����մϴ�.
        x = 480;
        y = 178;
        left = false;
        right = false;
        up = false;
        setSize(50, 50);
        setLocation(x, y);
        state = 0;

        if (EnemyWay.RIGHT == enemyWay) {
            this.enemyWay = EnemyWay.RIGHT;
            setIcon(enemyR);
            right();
        } else {
            this.enemyWay = EnemyWay.LEFT;
            setIcon(enemyL);
            left();
        }
    }
    
    public void reset() {
        // ���⿡ ���� �ٽ� ������ �� �ʿ��� �ʱ�ȭ �ڵ� �ۼ�
        initSetting(enemyWay);  // �Ǵ� �ٸ� �ʱ�ȭ �۾��� ������ �� �ֽ��ϴ�.
    }

    private void initBackgroundEnemyService() {
        new Thread(new BackgroundEnemyService(this)).start();
    }

    @Override
    public void left() {
        enemyWay = EnemyWay.LEFT;
        left = true;
        new Thread(() -> {
            while (left) {
                setIcon(enemyL);
                x = x - SPEED;
                setLocation(x, y);
                if (Math.abs(x - player.getX()) < 50 && Math.abs(y - player.getY()) < 50) {
                    if (player.getState() == 0 && getState() == 0)
                        player.die();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void right() {
        enemyWay = EnemyWay.RIGHT;
        right = true;
        new Thread(() -> {
            while (right) {
                setIcon(enemyR);
                x = x + SPEED;
                setLocation(x, y);
                if (Math.abs(x - player.getX()) < 50 && Math.abs(y - player.getY()) < 50) {
                    if (player.getState() == 0 && getState() == 0)
                        player.die();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void up() {
        up = true;
        new Thread(() -> {
            for (int i = 0; i < 130 / JUMP; i++) {
                y = y - JUMP;
                setLocation(x, y);
                if (Math.abs(x - player.getX()) < 50 && Math.abs(y - player.getY()) < 50) {
                    if (player.getState() == 0 && getState() == 0)
                        player.die();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            up = false;
            down();
        }).start();
    }

    @Override
    public void down() {
        if (down == false) {
            down = true;
            new Thread(() -> {
                while (down) {
                    y = y + JUMP;
                    setLocation(x, y);
                    if (Math.abs(x - player.getX()) < 50 && Math.abs(y - player.getY()) < 50) {
                        if (player.getState() == 0 && getState() == 0)
                            player.die();
                    }
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                down = false;
            }).start();
        }
    }

    public int getState() { return state; }
    public boolean isUp() {return up; }
    public boolean isDown() { return down; }
}
