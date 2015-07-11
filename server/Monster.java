import java.awt.Rectangle;
/*
 * Simple class to contain information about different monsters.
 * Create a different monster through the short t argument 
 * in the constructor.
 */
public class Monster{
	Rectangle r;
	short id;
	int hp;
	short wealth;
	short type;
	
	Monster(int x, int y, short c, short t){
		id = c;
		int he, wi;
		switch(t){
			default:
			case 1: // human Wizard
				he = 50;
				wi = 50;
				hp = 50;
				wealth = 20;
				break;
			case 2: // human knight
				he = 50;
				wi = 50;
				hp = 100;
				wealth = 35;
				break;
		}
		type = t;
		r = new Rectangle(x,y,he,wi);
	}
	public boolean hit(int dmg){
		hp -= dmg;
		if(hp <= 0){
			return true;
		}
		return false;
	}
}
