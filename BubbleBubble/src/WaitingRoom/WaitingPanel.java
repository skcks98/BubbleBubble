package WaitingRoom;
/*
import java.awt.Color;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JTextField;

import BubbleGame.BubbleBobbleGame;
import BubbleGame.GamePanel;
import BubbleGame.MainGamePanel;
import WaitingRoom.GameInPanel.JoinAction;
import utility.Settings;

public class WaitingPanel extends JLayeredPane {
	private GamePanel gamePanel;

	private String ip_addr = "127.0.0.1";
	private String port_no = "30000";

	private String roomNum;
	public static String userName;

	private static int myPlayerNum;

	private Image player1;
	private Image player2;

	private static JLabel p1NameLabel;
	private static JLabel p2NameLabel;
	private JLabel joinPlayer;
	private JButton startBtn;

	private static final int BUF_LEN = 128; // Windows 泥섎읆 BUF_LEN �쓣 �젙�쓽

	private Socket socket; // �뿰寃곗냼耳�
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;

	private ObjectInputStream ois;
	private static ObjectOutputStream oos;

	public WaitingPanel(String userName, String roomNum) {
		setLayout(null);
		this.userName = userName;
		this.roomNum = roomNum;

		Font font = new Font("HBIOS-SYS", Font.PLAIN, 90);

		joinPlayer = new JLabel("1 / 2");
		joinPlayer.setBounds((int) Settings.SCENE_WIDTH / 2 - (300 / 2), 200, 300, 100);
		joinPlayer.setFont(font);
		joinPlayer.setForeground(new Color(255, 186, 0));
		joinPlayer.setHorizontalAlignment(JTextField.CENTER);
		add(joinPlayer);

		font = new Font("HBIOS-SYS", Font.PLAIN, 30);
		p1NameLabel = new JLabel(userName);
		p1NameLabel.setBounds((int) Settings.SCENE_WIDTH / 2 - (200) - Settings.WAITING_SPACE, 400, 200, 40);
		p1NameLabel.setFont(font);
		p1NameLabel.setForeground(new Color(0, 255, 0));
		p1NameLabel.setHorizontalAlignment(JTextField.RIGHT);
		add(p1NameLabel);

		font = new Font("HBIOS-SYS", Font.PLAIN, 30);
		p2NameLabel = new JLabel("");
		p2NameLabel.setBounds((int) Settings.SCENE_WIDTH / 2 + Settings.WAITING_SPACE, 400, 200, 40);
		p2NameLabel.setFont(font);
		p2NameLabel.setForeground(new Color(0, 186, 255));
		p2NameLabel.setHorizontalAlignment(JTextField.LEFT);

		font = new Font("HBIOS-SYS", Font.PLAIN, 40);
		startBtn = new JButton("START");
		startBtn.setBounds((int) Settings.SCENE_WIDTH / 2 - (200 / 2), 450, 200, 40);
		startBtn.setFont(font);
		startBtn.setOpaque(true);
		startBtn.setBackground(Color.BLACK);
		startBtn.setForeground(Color.RED);
		startBtn.setBorderPainted(false);
		startBtn.setFocusPainted(false);

		StartAction startAction = new StartAction();
		startBtn.addActionListener(startAction);

		player1 = Toolkit.getDefaultToolkit().createImage("src/image/player1-waiting.gif");

		// 諛곌꼍 �깋 �꽕�젙
		setOpaque(true);
		this.setBackground(Color.BLACK);

		connectSet();
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	private void connectSet() {
		AppendText("User " + userName + " connecting " + ip_addr + " " + port_no);

		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));

			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			ois = new ObjectInputStream(socket.getInputStream());

			// SendMessage("/login " + userName);
			ChatMsg obcm = new ChatMsg(userName, "101", roomNum);
			SendObject(obcm);

			ListenNetwork net = new ListenNetwork();
			net.start();

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			AppendText("connect error");
		}
	}

	static public int getMyPlayerNum() {
		return myPlayerNum;
	}

	static public String getP1Name() {
		return p1NameLabel.getText();
	}

	static public String getP2Name() {
		return p2NameLabel.getText();
	}

	public void setAllJoinPlayer(String info[]) {
		joinPlayer.setText("2 / 2");
		player2 = Toolkit.getDefaultToolkit().createImage("src/image/player2-waiting.gif");
		add(p2NameLabel);
		if (info[0].trim().equals("player1")) {
			p2NameLabel.setText(info[1]);
			add(startBtn);
			myPlayerNum = 1;
		} else {
			p1NameLabel.setText(info[1]);
			p2NameLabel.setText(userName);
			myPlayerNum = 2;
		}
	}

	
	
	// Server Message瑜� �닔�떊�빐�꽌 �솕硫댁뿉 �몴�떆
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					Object obcm = null;
					String msg = null;
					ChatMsg cm;
					try {
						obcm = ois.readObject();
						if (obcm == null) break;
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						break;
					}
					if (obcm == null)
						break;
					if (obcm instanceof ChatMsg) {
						cm = (ChatMsg) obcm;
						msg = String.format("[%s] %s", cm.getId(), cm.getData());
					} else
						continue;
					switch (cm.getCode()) {
					case "102": // ��湲곗떎 2紐� �엯�옣
						setAllJoinPlayer(cm.getData().split("@@"));
						break;
					case "103": // 寃뚯엫 �떆�옉
						startGame();
						break;
					case "104": // �긽��諛� 諛� �굹媛�
						startGame();
						break;
					case "300": // Map change
						if (gamePanel != null) {
							gamePanel.SocketNextStge();
						}
						break;
					case "401": // 寃뚯엫 player ��吏곸엫
						if (gamePanel != null) {
							gamePanel.movePlayerTrue(cm.getData().split("@@"));
						}
						break;
					case "402": // 寃뚯엫 player ��吏곸엫
						if (gamePanel != null) {
							gamePanel.movePlayerFalse(cm.getData().split("@@"));
						}
						break;
					case "403":
						if (gamePanel != null) {
							gamePanel.movePlayerPosition(cm.getData().split("@@"));
							// ChatMsg(userName, "403", myPlayerNum+"@@" +x +"," +y);
						}
						break;
					case "501": // bubble�씠�옉 monster�씠�옉 留뚮궓
						if (gamePanel != null) {
							gamePanel.SocketMeetBubbleMonster(cm.getData().split(","));
						}
						break;
					case "502": // bubble 泥쒖옣 �옖�뜡 ��吏곸엫
						if (gamePanel != null) {
							gamePanel.SocketbubbleMove(cm.getData().split(","));
						}
						break;
					case "601": // bubble �꽣吏� > item create
						if (gamePanel != null) {
							gamePanel.SocketChangeItem(cm.getData().split(","));
						}
						break;
					case "602": // item �쐞移� 議곗젙
						if (gamePanel != null) {
							gamePanel.SocketItemLocation(cm.getData().split(","));
						}
						break;
					case "603": // item �젏�닔 利앷�
						if (gamePanel != null) {
							gamePanel.SocketIncrementScore(cm.getData().split(","));
						}
						break;
					}
				} catch (IOException e) {
					AppendText("ois.readObject() error");
					try {
//						dos.close();
//						dis.close();
						ois.close();
						oos.close();
						socket.close();

						break;
					} catch (Exception ee) {
						break;
					} // catch臾� �걹
				} // 諛붽묑 catch臾몃걹

			}
		}
	}

	public void AppendIcon(ImageIcon icon) {
//		int len = textArea.getDocument().getLength();
//		// �걹�쑝濡� �씠�룞
//		textArea.setCaretPosition(len);
//		textArea.insertIcon(icon);
	}

	// �솕硫댁뿉 異쒕젰
	public void AppendText(String msg) {
		// textArea.append(msg + "\n");
		// AppendIcon(icon1);
		msg = msg.trim(); // �븵�뮘 blank�� \n�쓣 �젣嫄고븳�떎.
//		int len = textArea.getDocument().getLength();
//		// �걹�쑝濡� �씠�룞
//		textArea.setCaretPosition(len);
//		textArea.replaceSelection(msg + "\n");
	}

	public void AppendImage(ImageIcon ori_icon) {
		Image ori_img = ori_icon.getImage();
		int width, height;
		double ratio;
		width = ori_icon.getIconWidth();
		height = ori_icon.getIconHeight();
		// Image媛� �꼫臾� �겕硫� 理쒕� 媛�濡� �삉�뒗 �꽭濡� 200 湲곗��쑝濡� 異뺤냼�떆�궓�떎.
		if (width > 200 || height > 200) {
			if (width > height) { // 媛�濡� �궗吏�
				ratio = (double) height / width;
				width = 200;
				height = (int) (width * ratio);
			} else { // �꽭濡� �궗吏�
				ratio = (double) width / height;
				height = 200;
				width = (int) (height * ratio);
			}
			Image new_img = ori_img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon new_icon = new ImageIcon(new_img);
		}
	}

	// Windows 泥섎읆 message �젣�쇅�븳 �굹癒몄� 遺�遺꾩� NULL 濡� 留뚮뱾湲� �쐞�븳 �븿�닔
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server�뿉寃� network�쑝濡� �쟾�넚
	public void SendMessage(String msg, String code) {
		try {
			// dos.writeUTF(msg);
//			byte[] bb;
//			bb = MakePacket(msg);
//			dos.write(bb, 0, bb.length);
			ChatMsg obcm = new ChatMsg(userName, code, msg);
			oos.writeObject(obcm);
		} catch (IOException e) {
			// AppendText("dos.write() error");
			AppendText("oos.writeObject() error");
			try {
//				dos.close();
//				dis.close();
				ois.close();
				oos.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}

	static public void SendObject(Object ob) { // �꽌踰꾨줈 硫붿꽭吏�瑜� 蹂대궡�뒗 硫붿냼�뱶
		try {
			oos.writeObject(ob);
		} catch (IOException e) {
			// textArea.append("硫붿꽭吏� �넚�떊 �뿉�윭!!\n");
			// AppendText("SendObject Error");
		}
	}

	public void startGame() {
		BubbleBobbleGame.isChange = true;
		BubbleBobbleGame.isGame = true;
		// bubbleGame.setPane(new MainGamePanel());

		setVisible(false);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (player1 != null) {
			g.drawImage(player1, (int) Settings.SCENE_WIDTH / 2 - 50 - Settings.WAITING_SPACE, 330, 50, 50, this);
		}
		if (player2 != null) {
			g.drawImage(player2, (int) Settings.SCENE_WIDTH / 2 + Settings.WAITING_SPACE, 330, 50, 50, this);
		}
	}

	class StartAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ChatMsg obcm = new ChatMsg(userName, "103", "start");
			SendObject(obcm);
			startGame();
		}
	}
}*/
