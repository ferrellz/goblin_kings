import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JLabel;

/*
 * Our GameScreen which takes care of the rendering.
 * Switching a state through setState will take care
 * of loading the right assets into our screen.
 */
class GameScreen extends JLabel{
	Map<Integer, JLabel> playerPositions = new HashMap<Integer, JLabel>();
	Map<JLabel, Long> animatedLabels = new HashMap<JLabel, Long>();
	Hashtable<Integer, JLabel> monsters = new Hashtable <Integer, JLabel>();
	Hashtable<Integer, Long> playersShooting = new Hashtable <Integer, Long>();

	boolean ready;
	int state;//0= connect, 1= game, 2= city
	long fpsTimer = System.currentTimeMillis();
	int co;
	int reloadTime = 500;
	int weapon = 1;
	int level = 0;

	public GameScreen(){
		Assets.init();
		setVisible(true);
		setBackground(Color.white);
	}

	public void updateGold(int g){
		Assets.goldCount.setText("Gold: "+Integer.toString(g));
		Assets.goldCountCity.setText("Gold: "+Integer.toString(g));
	}

	public void updateWall(int h){
		Assets.health.setText("Wall Health: "+Integer.toString(h));
	}

	public void updateLevel(int l){
		Assets.levelLabel.setText("Level: "+Integer.toString(l));
	}

	public void setState(int s){
		switch(s){
		case 0:
			state = 0;
			removeAll();
			add(Assets.connectError);
			add(Assets.ipText);
			add(Assets.portText);
			add(Assets.joinGame);
			add(Assets.ip);
			add(Assets.port);
			add(Assets.connectBackground);
			break;
		case 1:
			state = 1;
			ready = false;
			Assets.ready.setIcon(Assets.notReadyUI);
			removeAll();
			add(Assets.goldCount);
			add(Assets.health);
			add(Assets.levelLabel);
			add(Assets.gameBackground);
			if(Assets.wallsBought) add(Assets.gameWalls);
			revalidate();
			repaint();
			break;
		case 2:
			state = 2;
			playersShooting.clear();
			playerPositions.clear();
			animatedLabels.clear();
			monsters.clear();
			removeAll();
			if(level > 0){
				Assets.levelClearedCity.setText("Level "+level+" cleared!");
				add(Assets.levelClearedCity);
			}
			add(Assets.ready);
			add(Assets.goldCountCity);
			add(Assets.buildWalls);
			add(Assets.buildMasonry);
			add(Assets.buildTavern);
			add(Assets.buildBlacksmith);
			add(Assets.buildLaboratory);
			add(Assets.buildWizardTower);
			add(Assets.buildBarracks);
			add(Assets.buildTownhall);
			add(Assets.buildUI);
			if(Assets.wallsBought) add(Assets.walls);
			if(Assets.masonryBought) add(Assets.masonry);
			if(Assets.tavernBought) add(Assets.tavern);
			if(Assets.blacksmithBought) add(Assets.blacksmith);
			if(Assets.laboratoryBought) add(Assets.laboratory);
			if(Assets.wizardTowerBought) add(Assets.wizardTower);
			if(Assets.barracksBought) add(Assets.barracks);
			if(Assets.townhallBought) add(Assets.townhall);
			add(Assets.cityBackground);
			revalidate();
			repaint();
			break;
		case 3:
			playersShooting.clear();
			playerPositions.clear();
			animatedLabels.clear();
			monsters.clear();
			removeAll();
			add(Assets.victoryScreen);
			revalidate();
			repaint();
			break;
		}
	}

	public synchronized void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(state == 1){
			//remove(ready);
			if(Assets.wallsBought)
				add(Assets.gameWalls);
			add(Assets.gameBackground);
			Iterator i3 = animatedLabels.entrySet().iterator();
			while(i3.hasNext()) {
				Map.Entry pairs = (Map.Entry)i3.next();
				if(System.currentTimeMillis() - (Long)pairs.getValue() >= 200){
					remove((JLabel)pairs.getKey());
					i3.remove();
				}
			}
			Iterator i = playersShooting.entrySet().iterator();
			while(i.hasNext()) {
				Map.Entry pairs = (Map.Entry)i.next();
				if(System.currentTimeMillis() - (Long)pairs.getValue() >= reloadTime){
					playerPositions.get((Integer)pairs.getKey()).setIcon(Assets.getGoblinStanding((Integer)pairs.getKey()));
					i.remove();
				}
			}
			co++;
			if(System.currentTimeMillis() - fpsTimer >= 1000){
				co = 0;
				fpsTimer = System.currentTimeMillis();
			}
		}

	}

}
