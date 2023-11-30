package bubble.game;

import bubble.game.component.Enemy;

public interface Moveable {

	public abstract void left();

	public abstract void right();

	public abstract void up();

	default public void down() {
	}; // default �� ����ϸ� �������̽��� ��ü�� �ִ� �޼ҵ带 ���� �� �ִ�. (���߻���� �ȵǴ� ���� ���� ������)

	default public void attack() {
	};

	default public void attack(Enemy e) {
	};
}
