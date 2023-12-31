package bubble.game.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import bubble.game.component.Bubble;
import bubble.game.component.Player;

//background에서 계속 관찰
public class BackgroundPlayerService implements Runnable {

	private BufferedImage image;
	private Player player;
	private List<Bubble> bubbleList;

	// player, bubble
	public BackgroundPlayerService(Player player) {
		this.player = player;
		this.bubbleList = player.getBubbleList();
		try {
			image = ImageIO.read(new File("image/backgroundMapService.png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {

		while (player.getState() == 0) {

			// 1. bubble 충돌 체크
			for (int i = 0; i < bubbleList.size(); i++) {
				Bubble bubble = bubbleList.get(i);
				if (bubble.getState() == 1) {

					if (Math.abs(player.getX() - bubble.getX()) < 10 && Math.abs(player.getY() - bubble.getY()) > 0
							&& Math.abs(player.getY() - bubble.getY()) < 50) {
						System.out.println("적군 사살 완료 ");

						bubble.clearBubbled();

						break;
					}
				}
			}

			// 2. 벽 충돌 체크
			// 색상 확인
			Color leftcolor = new Color(image.getRGB(player.getX() - 10, player.getY() + 25));
			Color rightcolor = new Color(image.getRGB(player.getX() + 50 + 15, player.getY() + 25));

			// -2가 나온다는 뜻은 바닥에 색깔이 없이 흰색이라는 뜻!!
			int bottomcolor = image.getRGB(player.getX() + 10, player.getY() + 50 + 5)
					+ image.getRGB(player.getX() + 50 - 10, player.getY() + 50 + 5);

			// 바닥 충돌 확인
			if (bottomcolor != -2) {
				// System.out.println("bottom" + bottomcolor);
				// System.out.println("바닥충돌함");
				player.setDown(false);
			} else { // 오른쪽 왼쪽으로만 이동할 때,,!! (bottomcolor가 -2일 때!)
				if (!player.isUp() && !player.isDown()) {
					player.down();
				}
			}

			// 외벽 충돌 확인
			if (leftcolor.getRed() == 255 && leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				// System.out.println("왼쪽벽 충돌");
				player.setLeftWallCrash(true);
				player.setLeft(false);
			} else if (rightcolor.getRed() == 255 && rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				// System.out.println("오른쪽벽 충돌");
				player.setRightWallCrash(true);
				player.setRight(false);
			} else {
				player.setLeftWallCrash(false);
				player.setRightWallCrash(false);
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

}
