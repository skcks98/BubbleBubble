package bubble.test.ex18;

public interface Moveable {

	public abstract void left();

	public abstract void right();

	public abstract void up();

	default public void down() {
	}; // default �� ����ϸ� �������̽��� ��ü�� �ִ� �޼ҵ带 ���� �� �ִ�. (���߻���� �ȵǴ� ���� ���� ������)

	default public void attack() {
	};
}
