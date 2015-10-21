package pro.artwave.spaceshooter.view.manipulator;

public interface InterfaceFireController {
	int getFire();
	void setFire(int slotId);
	void setReady(int slotId,float ready);
	//void setOffFire();
	void setVisible(boolean visible);
	boolean fireEnemy();
	void resetFireEnemy();
}
