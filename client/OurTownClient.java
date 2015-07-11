import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/*
 * Our game client. Use GameScreen for rendering and
 * Assets for accessing UI components.
 */
public class OurTownClient extends JFrame implements Runnable{
	Socket socket;
	DataOutputStream dout;
	DataInputStream din;
	GameScreen gs = new GameScreen();
	final int SCREEN_WIDTH = 1024;
	final int SCREEN_HEIGHT = 768;
	short gold = 0;
	int wallHealth = 100;
	int maxWallHealth = 100;
	boolean movingUp = false;
	boolean movingDown = false;

	long lastLoopTime = System.nanoTime();
	int fpsCount = 0;
	long fpsTimer = System.nanoTime();
	
	int wallLevel = 0, masonryLevel = 0, tavernLevel = 0, blacksmithLevel = 0, 
		laboratoryLevel = 0, wizardTowerLevel = 0, barracksLevel = 0, townhallLevel = 0;


	public OurTownClient(String host, int port){

		setTitle("Goblin Kings");
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		setFocusable(true);
		add(gs);
		gs.setState(0);
		setIconImage(new ImageIcon("ImagesKevin/gameIcon.png").getImage());
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage("ImagesKevin/gameCursor.png");
		setCursor(toolkit.createCustomCursor(image, new Point(0,0), "Pencil"));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){
	        public void windowClosing(WindowEvent e) {
	            try {
	            	remove(gs);
	            	if(socket != null)
	            		socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	            System.exit(0);
	        }
		});
		
		addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent arg0) {
				if(gs.state == 1){
					switch(arg0.getKeyCode()){
						case(KeyEvent.VK_UP):
							if(!movingUp){
								movingUp = true;
								processMessage((byte)1);
							}
						break;
						case(KeyEvent.VK_DOWN): 
							if(!movingDown){
								movingDown = true;
								processMessage((byte)2);
							}
						break;
					
					}
				}
			}
			public void keyReleased(KeyEvent arg0) {
				if(gs.state == 1){
					switch(arg0.getKeyCode()){
						case(KeyEvent.VK_UP):
							if(movingUp){
								movingUp = false;
								processMessage((byte)1);
							}
						break;
						case(KeyEvent.VK_DOWN): 
							if(movingDown){
								movingDown = false;
								processMessage((byte)2);
							}
						break;
						case(KeyEvent.VK_SPACE): processMessage((byte)3); break;
					}
				}
			}
		});
		
		addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(gs.state == 0){
					if(pointInJLabel(e.getPoint(), Assets.joinGame)){
						if(!Assets.ip.getText().isEmpty() && !Assets.port.getText().isEmpty()){
							try{
								socket = new Socket(Assets.ip.getText(), Integer.parseInt(Assets.port.getText()));
								din = new DataInputStream(socket.getInputStream());
								dout = new DataOutputStream(socket.getOutputStream());
								startClientThread();
								gs.setState(2);
							} catch(IOException ie){
								Assets.connectError.setText("Error: Could not connect.");
							}
						}
					} 
				}
				/*
				 * We make a check to see if we have enough gold to buy a building
				 * just to avoid unneccessary calls to our server. Note that the
				 * server will still check if we actually have enough gold to buy
				 * a building.
				 */
				if(gs.state == 2){
					if(pointInJLabel(e.getPoint(), Assets.buildWalls)){
						if(e.getButton() == MouseEvent.BUTTON1){
							if(wallLevel == 0 && gold >= 100)
								processMessage((byte)5);
							else if(wallLevel == 1 && gold >= 250)
								processMessage((byte)5);
							else if(wallLevel == 2 && gold >= 500)
								processMessage((byte)5);
						}
					
					} else if (pointInJLabel(e.getPoint(), Assets.ready)){
						if(e.getButton() == MouseEvent.BUTTON1){
							gs.ready = !gs.ready;
							if(!gs.ready)
								Assets.ready.setIcon(Assets.notReadyUI);
							else Assets.ready.setIcon(Assets.readyUI);
							processMessage((byte)4);
						}
					} else if(pointInJLabel(e.getPoint(), Assets.buildMasonry)){
						if(e.getButton() == MouseEvent.BUTTON1){
							if(masonryLevel == 0 && gold >= 150)
								processMessage((byte)6);
							else if(masonryLevel == 1 && gold >= 250)
								processMessage((byte)6);
							else if(masonryLevel == 2 && gold >= 450)
								processMessage((byte)6);
						}
					} else if(pointInJLabel(e.getPoint(), Assets.buildTavern)){
						if(e.getButton() == MouseEvent.BUTTON1){
							if(tavernLevel == 0 && gold >= 250)
								processMessage((byte)7);
							else if(tavernLevel == 1 && gold >= 500)
								processMessage((byte)7);
							else if(tavernLevel == 2 && gold >= 700)
								processMessage((byte)7);
						}
					}else if(pointInJLabel(e.getPoint(), Assets.buildBlacksmith)){
						if(e.getButton() == MouseEvent.BUTTON1){
							if(blacksmithLevel == 0 && gold >= 200)
								processMessage((byte)8);
							else if(blacksmithLevel == 1 && gold >= 400)
								processMessage((byte)8);
							else if(blacksmithLevel == 2 && gold >= 800)
								processMessage((byte)8);
						}
					}else if(pointInJLabel(e.getPoint(), Assets.buildLaboratory)){
						if(e.getButton() == MouseEvent.BUTTON1){
							if(laboratoryLevel == 0 && gold >= 300)
								processMessage((byte)9);
							else if(laboratoryLevel == 1 && gold >= 700)
								processMessage((byte)9);
							else if(laboratoryLevel == 2 && gold >= 1000)
								processMessage((byte)9);
						}
					}else if(pointInJLabel(e.getPoint(), Assets.buildWizardTower)){
						if(e.getButton() == MouseEvent.BUTTON1){
							if(wizardTowerLevel == 0 && gold >= 400)
								processMessage((byte)10);
							else if(wizardTowerLevel == 1 && gold >= 800)
								processMessage((byte)10);
							else if(wizardTowerLevel == 2 && gold >= 1200)
								processMessage((byte)10);
						}
					}else if(pointInJLabel(e.getPoint(), Assets.buildBarracks)){
						if(e.getButton() == MouseEvent.BUTTON1){
							if(barracksLevel == 0 && gold >= 350)
								processMessage((byte)11);
							else if(barracksLevel == 1 && gold >= 600)
								processMessage((byte)11);
							else if(barracksLevel == 2 && gold >= 800)
								processMessage((byte)11);
						}
					}else if(pointInJLabel(e.getPoint(), Assets.buildTownhall)){
						if(e.getButton() == MouseEvent.BUTTON1){
							if(townhallLevel == 0 && gold >= 250)
								processMessage((byte)12);
							else if(townhallLevel == 1 && gold >= 400)
								processMessage((byte)12);
							else if(townhallLevel == 2 && gold >= 1000)
								processMessage((byte)12);
						}
					}
					
				}
			}
		});
	}
	
	public static void main(String args[]){
		new OurTownClient("127.0.0.1",50010);
	}
	
	public void startClientThread(){
		new Thread(this).start();
	}
	
	private boolean pointInJLabel(Point p, JLabel j){
		int jx = j.getLocation().x;
		int jy = j.getLocation().y+20;
		if(p.x >= jx && p.x <= jx+j.getWidth() && p.y >= jy && p.y <= jy+j.getHeight())
			return true;
		return false;
	}

	private void processMessage(byte message) {
		try {
			dout.writeByte(message);
		} catch( IOException ie ) {}
	}
	
	/*
	 * Listen for server messages
	 */
	public void run() {
		try{
			while(true){
				int command = din.readShort();
					if(command == 0){//game over - terminate connection
						dout.close();
						din.close();
						socket.close();
						System.exit(0);
					}else if(command == 3){//monster spawn
						int id = din.readShort();
						int x = din.readShort();
						int y = din.readShort();
						int m = din.readShort();
						JLabel j = newJLabel(x,y,m);
						gs.add(j);
						gs.monsters.put(id, j);
					} else if (command == 4){//monster move
						int id = din.readShort();
						int x = din.readShort();
						if(gs.monsters.containsKey(id))
							gs.monsters.get(id).setLocation(x, gs.monsters.get(id).getLocation().y);
					} else if (command == 5){//monster remove
						int id = din.readShort();
						int wealth = din.readShort();
						int type = din.readShort();
						boolean headshot = false;
						if(type - 10 > 0){
							headshot = true;
							type -= 10;
						}
						switch(type){
						default:
						case 1:
							gs.monsters.get(id).setIcon(Assets.imageHumanWizardDead);
							break;
						case 2:
							gs.monsters.get(id).setIcon(Assets.imageHumanKnightDead);
							break;
						}
						JLabel mx;
						if(headshot)
							mx = new JLabel(new ImageIcon(Assets.imageBlood2));
						else mx = new JLabel(new ImageIcon(Assets.imageBlood1));
						mx.setBounds(gs.monsters.get(id).getX()-64, gs.monsters.get(id).getY()-64, 128, 128);
						gs.add(mx);
						gs.animatedLabels.put(mx, System.currentTimeMillis());
						gold += wealth;
						gs.updateGold(gold);
					} else if (command == 6){//new player
						int amount = din.readShort();
						for(int i = 0;i<amount;i++){
							int player = din.readShort();
							JLabel j = new JLabel(Assets.getGoblinStanding(player));
							j.setBounds(100, 100+100*player, 50, 50);
							gs.add(j);
							gs.playerPositions.put(player, j);
						}
					} else if (command == 7){//monster hit
						int id = din.readShort();
						int type = din.readShort();
						boolean headshot = false;
						if(type - 10 > 0){
							headshot = true;
							type -= 10;
						}
						switch(type){
							default:
							case 1://human wizard
								gs.monsters.get(id).setIcon(Assets.imageHumanWizardHurt);
								break;
							case 2://human knight
								gs.monsters.get(id).setIcon(Assets.imageHumanKnightHurt);
								break;
						}
						JLabel mx;
						if(headshot)
							mx = new JLabel(new ImageIcon(Assets.imageBlood2));
						else mx = new JLabel(new ImageIcon(Assets.imageBlood1));
						mx.setBounds(gs.monsters.get(id).getX()-64, gs.monsters.get(id).getY()-64, 128, 128);
						gs.add(mx);
						gs.animatedLabels.put(mx, System.currentTimeMillis());
						gs.repaint();
					} else if (command == 8){//monster moved close
						int id = din.readShort();
						int remHp = din.readShort();
						if(gs.monsters.containsKey(id))
							gs.remove(gs.monsters.get(id));
						gs.monsters.remove(id);
						gs.updateWall(remHp);
					} else if (command == 9){//state changed
						int state = din.readShort();
						gs.setState(state);
						if(state == 1){
							gs.level++;
							gs.updateLevel(gs.level);
						} else if (state == 3){
							socket.close();
						}
					}else if (command == 10){//player stopped moving
						int player = din.readShort();
						if(gs.playerPositions.containsKey(player) && (!gs.playersShooting.containsKey(player)))//if shoot/reloading don't update moving gif yet)
							gs.playerPositions.get(player).setIcon(Assets.getGoblinStanding(player));
						gs.repaint();
					}else if (command == 11){//player shoots
						int player = din.readShort();
						if(gs.playerPositions.containsKey(player))
							gs.playerPositions.get(player).setIcon(Assets.getGoblinLoading(player));
						gs.playersShooting.put(player, System.currentTimeMillis());
						gs.repaint();
					}else if (command == 12){//building bought
						int building = din.readShort();
						boughtBuilding(building);
					} else {//playermove
						int player = command;
						int y = din.readShort();
						if(gs.playerPositions.containsKey(player)){
							gs.playerPositions.get(player).setLocation(100, y);
							if(gs.playersShooting.containsKey(player)){//if shooting do some awesome run-reload action
								gs.playerPositions.get(player).setIcon(Assets.getGoblinRunningLoading(player));
							} else gs.playerPositions.get(player).setIcon(Assets.getGoblinRunning(player));
						} else {
							JLabel j = new JLabel(Assets.getGoblinStanding(player));
							j.setBounds(100, 100+100*player, 50, 50);
							gs.playerPositions.put(player, j);
						}
					}
					requestRepaint();
			}
		} catch(IOException ie){}
	}
	
	public void requestRepaint(){
		long now = System.nanoTime();
	    long updateLength = now - lastLoopTime;
	    lastLoopTime = now;
	    if(updateLength > 16666666)
	    	gs.repaint();
	}
	
	public JLabel newJLabel(int x, int y, int m){
		JLabel j;
		switch(m){
			default:
			case 1://human wizard
				j = new JLabel(Assets.imageHumanWizard);
				j.setBounds(x, y, 41, 55);
				return j;
			case 2://human Knight
				j = new JLabel(Assets.imageHumanKnight);
				j.setBounds(x, y, 41, 55);
				return j;
		}
	}
	
	/*
	 * The method we run when the server confirms that we have bought a building.
	 */
	public void boughtBuilding(int building){
		switch(building){
		case 5:
			if(wallLevel == 0){
				gold -= 100;
				Assets.wallsBought = true;
				Assets.buildWalls.setIcon(Assets.imageWalls1UI);
				maxWallHealth = 150;
			} else if (wallLevel == 1){
				gold -= 250;
				Assets.buildWalls.setIcon(Assets.imageWalls2UI);
				Assets.walls.setIcon(Assets.imageWalls1);
				Assets.gameWalls.setIcon(Assets.imageGameWall1);
				maxWallHealth = 200;
			} else if (wallLevel == 2){
				gold -= 500;
				Assets.walls.setIcon(Assets.imageWalls2);
				Assets.gameWalls.setIcon(Assets.imageGameWall2);
				Assets.buildWalls.setIcon(null);
				maxWallHealth = 250;
			}
			wallLevel++;
			gs.updateGold(gold);
			gs.updateWall(wallHealth);
		break;
		case 6:
			if(masonryLevel == 0){
				gold -= 150;
				Assets.masonryBought = true;
				Assets.buildMasonry.setIcon(Assets.imageMasonry1UI);
			} else if (masonryLevel == 1){
				gold -= 250;
				Assets.buildMasonry.setIcon(Assets.imageMasonry2UI);
				Assets.masonry.setIcon(Assets.imageMasonry1);
			} else if (masonryLevel == 2){
				gold -= 450;
				Assets.buildMasonry.setIcon(null);
				Assets.masonry.setIcon(Assets.imageMasonry2);
			}
			masonryLevel++;
			gs.updateGold(gold);
		break;
		case 7:
			if(tavernLevel == 0){
				gold -= 250;
				Assets.tavernBought = true;
				Assets.buildTavern.setIcon(Assets.imageTavern1UI);
			} else if (tavernLevel == 1){
				gold -= 500;
				Assets.buildTavern.setIcon(Assets.imageTavern2UI);
				Assets.tavern.setIcon(Assets.imageTavern1);
				
			} else if (tavernLevel == 2){
				gold -= 700;
				Assets.tavern.setIcon(Assets.imageTavern2);
				Assets.buildTavern.setIcon(null);
			}
			tavernLevel++;
			gs.updateGold(gold);
		break;
		case 8:
			if(blacksmithLevel == 0){
				gold -= 200;
				Assets.blacksmithBought = true;
				Assets.buildBlacksmith.setIcon(Assets.imageBlacksmith1UI);
			} else if (blacksmithLevel == 1){
				gold -= 400;
				Assets.buildBlacksmith.setIcon(Assets.imageBlacksmith2UI);
				Assets.blacksmith.setIcon(Assets.imageBlacksmith1);
			} else if (blacksmithLevel == 2){
				gold -= 800;
				Assets.buildBlacksmith.setIcon(null);
				Assets.blacksmith.setIcon(Assets.imageBlacksmith2);
			}
			blacksmithLevel++;
			gs.updateGold(gold);
		break;
		case 9:
			if(laboratoryLevel == 0){
				gold -= 300;
				Assets.laboratoryBought = true;
				Assets.buildLaboratory.setIcon(Assets.imageLaboratory1UI);
				
			} else if (laboratoryLevel == 1){
				gold -= 700;
				Assets.buildLaboratory.setIcon(Assets.imageLaboratory2UI);
				Assets.laboratory.setIcon(Assets.imageLaboratory1);
			} else if (laboratoryLevel == 2){
				gold -= 1000;
				Assets.buildLaboratory.setIcon(null);
				Assets.laboratory.setIcon(Assets.imageLaboratory2);
			}
			laboratoryLevel++;
			gs.updateGold(gold);
		break;
		case 10:
			if(wizardTowerLevel == 0){
				gold -= 400;
				Assets.wizardTowerBought = true;
				Assets.buildWizardTower.setIcon(Assets.imageWizardTower1UI);
			} else if (wizardTowerLevel == 1){
				gold -= 800;
				Assets.wizardTower.setIcon(Assets.imageWizardTower1);
				Assets.buildWizardTower.setIcon(Assets.imageWizardTower2UI);
			} else if (wizardTowerLevel == 2){
				gold -= 1200;
				Assets.wizardTower.setIcon(Assets.imageWizardTower2);
				Assets.buildWizardTower.setIcon(null);
			}
			wizardTowerLevel++;
			gs.updateGold(gold);
		break;
		case 11:
			if(barracksLevel == 0){
				gold -= 350;
				Assets.barracksBought = true;
				Assets.buildBarracks.setIcon(Assets.imageBarracks1UI);
			} else if (barracksLevel == 1){
				gold -= 600;
				Assets.barracks.setIcon(Assets.imageBarracks1);
				Assets.buildBarracks.setIcon(Assets.imageBarracks2UI);
			} else if (barracksLevel == 2){
				gold -= 800;
				Assets.barracks.setIcon(Assets.imageBarracks2);
				Assets.buildBarracks.setIcon(null);
			}
			barracksLevel++;
			gs.updateGold(gold);
		break;
		case 12:
			if(townhallLevel == 0){
				gold -= 250;
				Assets.townhallBought = true;
				Assets.buildTownhall.setIcon(Assets.imageTownhall1UI);
			} else if (townhallLevel == 1){
				gold -= 400;
				Assets.townhall.setIcon(Assets.imageTownhall1);
				Assets.buildTownhall.setIcon(Assets.imageTownhall2UI);
			} else if (townhallLevel == 2){
				gold -= 1000;
				Assets.townhall.setIcon(Assets.imageTownhall2);
				Assets.buildTownhall.setIcon(null);
			}
			townhallLevel++;
			gs.updateGold(gold);
		break;
		default:break;
		}
		gs.setState(2);
	}
}