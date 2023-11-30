package bubble.test.ex18;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

//���ν����� �ٻ� - Ű���� �̺�Ʈ�� ó���ϱ� �ٻڴ�. 
//background���� ��� ������!!
public class BackgroundEnemyService implements Runnable {

	private BufferedImage image;
	private Enemy enemy;

	// player, bubble
	public BackgroundEnemyService(Enemy enemy) {
		this.enemy = enemy;
		try {
			image = ImageIO.read(new File("image/backgroundMapService.png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
		while (enemy.getState() == 0) {

			// ���� Ȯ��
			Color leftcolor = new Color(image.getRGB(enemy.getX() - 10, enemy.getY() + 25));
			Color rightcolor = new Color(image.getRGB(enemy.getX() + 50 + 15, enemy.getY() + 25));

			// -2�� ���´ٴ� ���� �ٴڿ� ������ ���� ����̶�� ��!!
			int bottomcolor = image.getRGB(enemy.getX() + 10, enemy.getY() + 50 + 5)
					+ image.getRGB(enemy.getX() + 50 - 10, enemy.getY() + 50 + 5);

			// �ٴ� �浹 Ȯ��
			if (bottomcolor != -2) {
				// System.out.println("bottom" + bottomcolor);
				// System.out.println("�ٴ��浹��");
				enemy.setDown(false);
			} else { // ������ �������θ� �̵��� ��,,!! (bottomcolor�� -2�� ��!)
				if (!enemy.isUp() && !enemy.isDown()) {
					enemy.down();
				}
			}

			// �ܺ� �浹 Ȯ��
			if (leftcolor.getRed() == 255 && leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				enemy.setLeft(false);
				if (!enemy.isRight()) {
					enemy.right();
				}
			} else if (rightcolor.getRed() == 255 && rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				enemy.setRight(false);
				if (!enemy.isLeft()) {
					enemy.left();
				}
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

}
