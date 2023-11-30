package bubble.test.ex06;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

//���ν����� �ٻ� - Ű���� �̺�Ʈ�� ó���ϱ� �ٻڴ�. 
//background���� ��� ������!!
public class BackgroundPlayerService implements Runnable {

	private BufferedImage image;
	private Player player;

	public BackgroundPlayerService(Player player) {
		this.player = player;

		try {
			image = ImageIO.read(new File("image/backgroundMapService.png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {

		while (true) {
			Color leftcolor = new Color(image.getRGB(player.getX() - 10, player.getY() + 25));
			Color rightcolor = new Color(image.getRGB(player.getX() + 50 + 15, player.getY() + 25));

			// System.out.println("left���� = " + leftcolor);
			// System.out.println("rigth���� = " + rightcolor);

			if (leftcolor.getRed() == 255 && leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				System.out.println("���ʺ� �浹");
			} else if (rightcolor.getRed() == 255 && rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				System.out.println("�����ʺ� �浹");
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

}
