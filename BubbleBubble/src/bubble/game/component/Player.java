package bubble.game.component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import bubble.game.BubbleFrame;
import bubble.game.Moveable;
import bubble.game.service.BackgroundPlayerService;
import bubble.game.state.PlayerWay;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends JLabel implements Moveable {

	private BubbleFrame mContext;
	private List<Bubble> bubbleList;

	// ��ġ����
	private int x;
	private int y;

	// player�� ����
	private PlayerWay playerWay;

	// ������ ����
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	// player �ӵ�
	private final int SPEED = 4; // ����� �빮�ڷ�!!
	private final int JUMP = 4; // UP,DOWN���ǵ�

	private int state = 0;

	// ���浹����
	private boolean leftWallCrash;
	private boolean rightWallCrash;

	private ImageIcon playerR, playerL, playerRdie, playerLdie;

	public Player(BubbleFrame mContext) {
		this.mContext = mContext;
		initObject();
		initSetting();
		initBackgroundPlayerService();
	}

	private void initObject() {

		playerR = new ImageIcon("image/playerR.png");
		playerL = new ImageIcon("image/playerL.png");
		playerRdie = new ImageIcon("image/playerRdie.png");
		playerLdie = new ImageIcon("image/playerLdie.png");
		bubbleList = new ArrayList<>();

	}

	private void initSetting() {
		x = 80;
		y = 535;

		left = false;
		right = false;
		up = false;
		down = false;
		leftWallCrash = false;
		rightWallCrash = false;

		playerWay = PlayerWay.RIGHT;

		setIcon(playerR);

		setSize(50, 50);
		setLocation(x, y);
	}

	private void initBackgroundPlayerService() {
		new Thread(new BackgroundPlayerService(this)).start();
	}

	@Override
	public void attack() {
		new Thread(() -> {
			Bubble bubble = new Bubble(mContext);
			mContext.add(bubble);
			bubbleList.add(bubble);
			if (playerWay == PlayerWay.LEFT) {
				bubble.left();
			} else {
				bubble.right();
			}
		}).start();
	}

	@Override
	public void left() {
		playerWay = PlayerWay.LEFT;

		left = true;
		new Thread(() -> {
			while (left && getState() == 0) {
				setIcon(playerL);
				x = x - SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void right() {
		playerWay = PlayerWay.RIGHT;

		right = true;
		new Thread(() -> {
			while (right && getState() == 0) {
				setIcon(playerR);
				x = x + SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public void up() {
		// System.out.println("����!");
		up = true;
		new Thread(() -> {
			for (int i = 0; i < 130 / JUMP; i++) {
				y = y - JUMP;
				setLocation(x, y);
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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
			// System.out.println("�ٿ�");
			down = true;
			new Thread(() -> {
				while (down) {
					y = y + JUMP;
					setLocation(x, y);
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
						// TODO Auto)-generated catch block
						e.printStackTrace();
					}
				}
				down = false;
			}).start();
		}
	}

	public void die() {
		new Thread(() -> {
			setState(1);
			setIcon(PlayerWay.RIGHT == playerWay ? playerRdie : playerLdie);
			try {
				if (!isUp() && isDown())
					up();
				Thread.sleep(2000);
				mContext.remove(this);
				mContext.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("player ����");
		}).start();
	}
	
	public int getState() {
	    return state;
	}

	public void setState(int state) {
	    this.state = state;
	}

	public boolean isUp() {
	    return up;
	}

	public boolean isDown() {
	    return down;
	}

}
