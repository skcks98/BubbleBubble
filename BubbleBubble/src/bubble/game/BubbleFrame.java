package bubble.game;

import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import bubble.game.component.Enemy;
import bubble.game.component.Player;
import bubble.game.music.BGM;
import bubble.game.state.EnemyWay;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BubbleFrame extends JFrame {

    private BubbleFrame mContext = this;
    private JLabel backgroundMap;
    private Player player;
    private List<Enemy> enemy;

    public BubbleFrame() {
        initObject();
        initSetting();
        initListener();
        setVisible(true);
    }

    private void initObject() {
        backgroundMap = new JLabel(new ImageIcon("image/backgroundMap.png"));
        setContentPane(backgroundMap);
        player = new Player(mContext);
        add(player);
        enemy = new ArrayList<>();
        enemy.add(new Enemy(mContext, EnemyWay.RIGHT));
        enemy.add(new Enemy(mContext, EnemyWay.LEFT));
        for (Enemy e : enemy)
            add(e);
        new BGM();
    }

    private void initListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (!player.isLeft() && !player.isLeftWallCrash() && player.getState() == 0)
                            player.left();
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!player.isRight() && !player.isRightWallCrash() && player.getState() == 0)
                            player.right();
                        break;
                    case KeyEvent.VK_UP:
                        if (!player.isUp() && !player.isDown() && player.getState() == 0)
                            player.up();
                        break;
                    case KeyEvent.VK_SPACE:
                        if (player.getState() == 0) {
                            player.attack();
                            break;
                        }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        player.setLeft(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        player.setRight(false);
                        break;
                }
            }
        });
    }

    private void initSetting() {
        setSize(1000, 640);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // ���� ����ŸƮ�� ���� Ÿ�̸� ����
        Timer restartTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isGameOver()) {
                    showGameOverMessage();
                } else {
                    //restartGame();
                }
            }
        });
        restartTimer.setRepeats(false); // Ÿ�̸� �� ���� ����
        restartTimer.start();
    }

    private boolean isGameOver() {
        for (Enemy enemy : enemy) {
            if (enemy.getState() == 0) {
                return false; // ���� ������ ������ ����
            }
        }
        return true; // ��� ���� ���ŵǾ� ������ ����
    }

    private void showGameOverMessage() {
        JOptionPane.showMessageDialog(this, "������ ����Ǿ����ϴ�.", "���� ����", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("������ ����Ǿ����ϴ�."); // �ֿܼ��� �޽��� ���
        System.exit(0); // ���α׷� ����
    }

    /*private void restartGame() {
        for (Enemy enemy : enemy) {
            enemy.reset();
        }

        // �ٽ� ������ �� �÷��̾��� ���� �ʱ�ȭ �� �ʿ��� �ʱ�ȭ �۾� ����
        player.reset();

        // �������� �ٽ� �׸����� ����
        revalidate();
        repaint();
    }*/

    public static void main(String[] args) {
        new BubbleFrame();
    }
}
