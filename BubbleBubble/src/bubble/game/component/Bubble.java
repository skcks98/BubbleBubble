package bubble.game.component;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import bubble.game.BubbleFrame;
import bubble.game.Moveable;
import bubble.game.service.BackgroundBubbleService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bubble extends JLabel implements Moveable {

	private Player player;
	private BackgroundBubbleService backgroundBubbleService;
	private BubbleFrame mContext;
	private List<Enemy> enemy;
	private Enemy removeEnemy = null;

	private int x;
	private int y;

	private boolean left;
	private boolean right;
	private boolean up;

	private int state; 
	private ImageIcon bubble; 
	private ImageIcon bubbled; 
	private ImageIcon bomb; 

	public Bubble(BubbleFrame mContext) {
		this.mContext = mContext;
		this.player = mContext.getPlayer();
		this.enemy = mContext.getEnemy();
		initObject();
		initSetting();
	}

	private void initObject() {
		bubble = new ImageIcon("image/bubble.png");
		bubbled = new ImageIcon("image/bubbled.png");
		bomb = new ImageIcon("image/bomb.png");

		backgroundBubbleService = new BackgroundBubbleService(this);
	}

	private void initSetting() {
		up = false;
		left = false;
		right = false;

		x = player.getX();
		y = player.getY();

		setIcon(bubble);
		setSize(50, 50);

		state = 0;
	}
	

	@Override
	public void left() {
		left = true;
		for (int i = 0; i < 400; i++) {
			x--;
			setLocation(x, y);

			if (backgroundBubbleService.leftWall()) {
				left = false;
				break;
			}

			for (Enemy e : enemy) {
				if (Math.abs(x - e.getX()) < 10 && Math.abs(y - e.getY()) > 0 && Math.abs(y - e.getY()) < 50) {
					if (e.getState() == 0) {
						attack(e);
						break;
					}
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void right() {
		right = true;
		for (int i = 0; i < 400; i++) {
			x++;
			setLocation(x, y);

			if (backgroundBubbleService.rightWall()) {
				right = false;
				break;
			}

			for (Enemy e : enemy) {
				if (Math.abs(x - e.getX()) < 10 && Math.abs(y - e.getY()) > 0 && Math.abs(y - e.getY()) < 50) {
					if (e.getState() == 0) {
						attack(e);
						break;
					}
				}
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void up() {
		up = true;
		while (up) {
			y--;
			setLocation(x, y);

			if (backgroundBubbleService.topWall()) {
				up = false;
				break;
			}

			try {
				if (state == 0) {
					Thread.sleep(1);
				} else { 
					Thread.sleep(10);
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (state == 0)
			clearBubble();
	}

	@Override
	public void attack(Enemy e) {
		state = 1;
		e.setState(1);
		setIcon(bubbled);
		removeEnemy = e;
		mContext.remove(e);
		mContext.repaint();
	}

	private void clearBubble() {
		try {
			Thread.sleep(3000);
			setIcon(bomb);
			Thread.sleep(2000);
			
			mContext.getPlayer().getBubbleList().remove(this);
			mContext.remove(this); 
			mContext.repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearBubbled() {
		new Thread(() -> {
			System.out.println("clearBubbled");
			try {
				up = false;
				setIcon(bomb);
				Thread.sleep(1000);
				mContext.getPlayer().getBubbleList().remove(this);
				mContext.getEnemy().remove(removeEnemy);
				mContext.remove(this);
				mContext.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}
