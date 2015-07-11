import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
/*
 * store information about all our UI components
 * and have a method which initiates them.
 */
public class Assets {
	//connect
	static JLabel joinGame = new JLabel();
	static JLabel connectBackground = new JLabel();
	static JLabel ipText = new JLabel("Ip: ");
	static JLabel portText = new JLabel("Port: ");
	static JLabel connectError = new JLabel();
	static JTextField ip = new JTextField("127.0.0.1");
	static JTextField port = new JTextField("50010");
	//game
	static JLabel levelLabel = new JLabel("Level: 1");
	static JLabel goldCount = new JLabel("Gold: 0");
	static JLabel health = new JLabel("Wall Health: 100");
	static JLabel gameBackground = new JLabel();
	static JLabel gameWalls = new JLabel();
	//city	
	static JLabel levelClearedCity = new JLabel();
	static JLabel goldCountCity = new JLabel("Gold: 0");
	static JLabel ready = new JLabel();
	static JLabel buildUI = new JLabel();
	static JLabel buildWalls = new JLabel();
	static JLabel buildMasonry = new JLabel();
	static JLabel buildTavern = new JLabel();
	static JLabel buildBlacksmith = new JLabel();
	static JLabel buildLaboratory = new JLabel();
	static JLabel buildWizardTower = new JLabel();
	static JLabel buildBarracks = new JLabel();
	static JLabel buildTownhall = new JLabel();
	static JLabel cityBackground = new JLabel();
	static JLabel walls = new JLabel();
	static JLabel masonry = new JLabel();
	static JLabel tavern = new JLabel();
	static JLabel blacksmith = new JLabel();
	static JLabel laboratory = new JLabel();
	static JLabel wizardTower = new JLabel();
	static JLabel barracks = new JLabel();
	static JLabel townhall = new JLabel();
	//victory
	static JLabel victoryScreen = new JLabel();
	//city buildings
	static boolean wallsBought = false;
	static boolean walls1Bought = false;
	static boolean walls2Bought = false;
	static boolean masonryBought = false;
	static boolean masonry1Bought = false;
	static boolean masonry2Bought = false;
	static boolean tavernBought = false;
	static boolean tavern1Bought = false;
	static boolean tavern2Bought = false;
	static boolean blacksmithBought = false;
	static boolean blacksmith1Bought = false;
	static boolean blacksmith2Bought = false;
	static boolean laboratoryBought = false;
	static boolean laboratory1Bought = false;
	static boolean laboratory2Bought = false;
	static boolean wizardTowerBought = false;
	static boolean wizardTower1Bought = false;
	static boolean wizardTower2Bought = false;
	static boolean barracksBought = false;
	static boolean barracks1Bought = false;
	static boolean barracks2Bought = false;
	static boolean townhallBought = false;
	static boolean townhall1Bought = false;
	static boolean townhall2Bought = false;
	//connect
	static ImageIcon imageConnectBackground = new ImageIcon("imagesKevin/startScreen.png");
	static ImageIcon imageJoinGame = new ImageIcon("imagesKevin/joinGame.png");
	//city images
	static ImageIcon imageCityBackground = new ImageIcon("imagesKevin/cityBackground.png");
	static ImageIcon imageWalls = new ImageIcon("imagesKevin/cityWall.png");
	static ImageIcon imageWalls1 = new ImageIcon("imagesKevin/cityWall2.png");
	static ImageIcon imageWalls2 = new ImageIcon("imagesKevin/cityWall3.png");
	static ImageIcon imageMasonry = new ImageIcon("imagesKevin/cityMasonry.png");
	static ImageIcon imageMasonry1= new ImageIcon("imagesKevin/cityMasonry2.png");
	static ImageIcon imageMasonry2 = new ImageIcon("imagesKevin/cityMasonry3.png");
	static ImageIcon imageTavern = new ImageIcon("imagesKevin/cityTavern.png");
	static ImageIcon imageTavern1 = new ImageIcon("imagesKevin/cityTavern2.png");
	static ImageIcon imageTavern2 = new ImageIcon("imagesKevin/cityTavern3.png");
	static ImageIcon imageBlacksmith = new ImageIcon("imagesKevin/cityBlacksmith.png");
	static ImageIcon imageBlacksmith1 = new ImageIcon("imagesKevin/cityBlacksmith2.png");
	static ImageIcon imageBlacksmith2 = new ImageIcon("imagesKevin/cityBlacksmith3.png");
	static ImageIcon imageLaboratory = new ImageIcon("imagesKevin/cityLaboratory.png");
	static ImageIcon imageLaboratory1 = new ImageIcon("imagesKevin/cityLaboratory2.png");
	static ImageIcon imageLaboratory2 = new ImageIcon("imagesKevin/cityLaboratory3.png");
	static ImageIcon imageWizardTower = new ImageIcon("imagesKevin/cityWizardTower.png");
	static ImageIcon imageWizardTower1 = new ImageIcon("imagesKevin/cityWizardTower2.png");
	static ImageIcon imageWizardTower2 = new ImageIcon("imagesKevin/cityWizardTower3.png");
	static ImageIcon imageBarracks = new ImageIcon("imagesKevin/cityBarracks.png");
	static ImageIcon imageBarracks1 = new ImageIcon("imagesKevin/cityBarracks2.png");
	static ImageIcon imageBarracks2 = new ImageIcon("imagesKevin/cityBarracks3.png");
	static ImageIcon imageTownhall = new ImageIcon("imagesKevin/cityTownhall2.png");
	static ImageIcon imageTownhall1 = new ImageIcon("imagesKevin/cityTownhall3.png");
	static ImageIcon imageTownhall2 = new ImageIcon("imagesKevin/cityTownhall4.png");
	//city UI images
	static ImageIcon imageBuyUI = new ImageIcon("imagesKevin/buyUI.png");
	static ImageIcon imageWallsUI = new ImageIcon("imagesKevin/buyWalls.png");
	static ImageIcon imageWalls1UI = new ImageIcon("imagesKevin/buyWalls2.png");
	static ImageIcon imageWalls2UI = new ImageIcon("imagesKevin/buyWalls3.png");
	static ImageIcon imageMasonryUI = new ImageIcon("imagesKevin/buyMasonry.png");
	static ImageIcon imageMasonry1UI= new ImageIcon("imagesKevin/buyMasonry2.png");
	static ImageIcon imageMasonry2UI = new ImageIcon("imagesKevin/buyMasonry3.png");
	static ImageIcon imageTavernUI = new ImageIcon("imagesKevin/buyTavern.png");
	static ImageIcon imageTavern1UI = new ImageIcon("imagesKevin/buyTavern2.png");
	static ImageIcon imageTavern2UI = new ImageIcon("imagesKevin/buyTavern3.png");
	static ImageIcon imageBlacksmithUI = new ImageIcon("imagesKevin/buyBlacksmith.png");
	static ImageIcon imageBlacksmith1UI = new ImageIcon("imagesKevin/buyBlacksmith2.png");
	static ImageIcon imageBlacksmith2UI = new ImageIcon("imagesKevin/buyBlacksmith3.png");
	static ImageIcon imageLaboratoryUI = new ImageIcon("imagesKevin/buyLaboratory.png");
	static ImageIcon imageLaboratory1UI = new ImageIcon("imagesKevin/buyLaboratory2.png");
	static ImageIcon imageLaboratory2UI = new ImageIcon("imagesKevin/buyLaboratory3.png");
	static ImageIcon imageWizardTowerUI = new ImageIcon("imagesKevin/buyWizardTower.png");
	static ImageIcon imageWizardTower1UI = new ImageIcon("imagesKevin/buyWizardTower2.png");
	static ImageIcon imageWizardTower2UI = new ImageIcon("imagesKevin/buyWizardTower3.png");
	static ImageIcon imageBarracksUI = new ImageIcon("imagesKevin/buyBarracks.png");
	static ImageIcon imageBarracks1UI = new ImageIcon("imagesKevin/buyBarracks2.png");
	static ImageIcon imageBarracks2UI = new ImageIcon("imagesKevin/buyBarracks3.png");
	static ImageIcon imageTownhallUI = new ImageIcon("imagesKevin/buyTownhall.png");
	static ImageIcon imageTownhall1UI = new ImageIcon("imagesKevin/buyTownhall2.png");
	static ImageIcon imageTownhall2UI = new ImageIcon("imagesKevin/buyTownhall3.png");
	static ImageIcon readyUI = new ImageIcon("imagesKevin/ready.png");
	static ImageIcon notReadyUI = new ImageIcon("imagesKevin/notReady.png");
	//game
	static ImageIcon imageGameBackground = new ImageIcon("imagesKevin/gameBackground.png");
	static ImageIcon imageGameWall = new ImageIcon("imagesKevin/gameWall1.png");
	static ImageIcon imageGameWall1 = new ImageIcon("imagesKevin/gameWall2.png");
	static ImageIcon imageGameWall2 = new ImageIcon("imagesKevin/gameWall3.png");
	static ImageIcon imageGoblin1 = new ImageIcon("imagesKevin/goblin1.png");
	static ImageIcon imageGoblin2 = new ImageIcon("imagesKevin/goblin2.png");
	static ImageIcon imageGoblin3 = new ImageIcon("imagesKevin/goblin3.png");
	static ImageIcon imageGoblin4 = new ImageIcon("imagesKevin/goblin4.png");
	static ImageIcon imageGoblin1Running = new ImageIcon("imagesKevin/goblin1running.png");
	static ImageIcon imageGoblin2Running = new ImageIcon("imagesKevin/goblin2running.png");
	static ImageIcon imageGoblin3Running = new ImageIcon("imagesKevin/goblin3running.png");
	static ImageIcon imageGoblin4Running = new ImageIcon("imagesKevin/goblin4running.png");
	static ImageIcon imageGoblin1RunningLoading = new ImageIcon("imagesKevin/goblin1runningLoading.png");
	static ImageIcon imageGoblin2RunningLoading = new ImageIcon("imagesKevin/goblin2runningLoading.png");
	static ImageIcon imageGoblin3RunningLoading = new ImageIcon("imagesKevin/goblin3runningLoading.png");
	static ImageIcon imageGoblin4RunningLoading = new ImageIcon("imagesKevin/goblin4runningLoading.png");
	static ImageIcon imageGoblin1Loading = new ImageIcon("imagesKevin/goblin1Loading.png");
	static ImageIcon imageGoblin2Loading = new ImageIcon("imagesKevin/goblin2Loading.png");
	static ImageIcon imageGoblin3Loading = new ImageIcon("imagesKevin/goblin3Loading.png");
	static ImageIcon imageGoblin4Loading = new ImageIcon("imagesKevin/goblin4Loading.png");
	
	static ImageIcon imageHumanWizard = new ImageIcon("imagesKevin/humanWizard.png");
	static ImageIcon imageHumanWizardHurt = new ImageIcon("imagesKevin/humanWizardHurt.png");
	static ImageIcon imageHumanWizardDead = new ImageIcon("imagesKevin/humanWizardDead.png");
	static ImageIcon imageHumanKnight = new ImageIcon("imagesKevin/humanKnight.png");
	static ImageIcon imageHumanKnightHurt = new ImageIcon("imagesKevin/humanKnightHurt.png");
	static ImageIcon imageHumanKnightDead = new ImageIcon("imagesKevin/humanKnightDead.png");

	static String imageBlood1 = "imagesKevin/blood1.gif";
	static String imageBlood2 = "imagesKevin/blood2.gif";
	
	static String strBuildWalls1 = "Build";
	static String strBuildWalls2 = "";
	
	//victory
	static ImageIcon imageVictoryScreen = new ImageIcon("imagesKevin/victoryScreen.png");
	
	static ImageIcon getGoblinStanding(int id){
		switch(id){
		default:
		case 1:
			return imageGoblin1;
		case 2:
			return imageGoblin2;
		case 3:
			return imageGoblin3;
		case 4:
			return imageGoblin4;
		}
	}
	
	static ImageIcon getGoblinLoading(int id){
		switch(id){
		default:
		case 1:
			return imageGoblin1Loading;
		case 2:
			return imageGoblin2Loading;
		case 3:
			return imageGoblin3Loading;
		case 4:
			return imageGoblin4Loading;
		}
	}
	
	static ImageIcon getGoblinRunning(int id){
		switch(id){
		default:
		case 1:
			return imageGoblin1Running;
		case 2:
			return imageGoblin2Running;
		case 3:
			return imageGoblin3Running;
		case 4:
			return imageGoblin4Running;
		}
	}
	
	static ImageIcon getGoblinRunningLoading(int id){
		switch(id){
		default:
		case 1:
			return imageGoblin1RunningLoading;
		case 2:
			return imageGoblin2RunningLoading;
		case 3:
			return imageGoblin3RunningLoading;
		case 4:
			return imageGoblin4RunningLoading;
		}
	}

	static void init(){
		joinGame.setBounds(384,500,256,128);
		joinGame.setIcon(imageJoinGame);
		connectBackground.setBounds(0,0,1024,768);
		connectBackground.setIcon(imageConnectBackground);
		ip.setBounds(384,300,256,50);
		ip.setFont(new Font("Serif", Font.PLAIN, 24));
		ipText.setBounds(384,250,256,50);
		ipText.setFont(new Font("Serif", Font.PLAIN, 24));
		port.setBounds(384,400,256,50);
		port.setFont(new Font("Serif", Font.PLAIN, 24));
		portText.setBounds(384,350,256,50);
		portText.setFont(new Font("Serif", Font.PLAIN, 24));
		connectError.setBounds(384, 650, 256, 50);
		connectError.setFont(new Font("Serif", Font.PLAIN, 24));
		///game
		goldCount.setBounds(10, 10, 200, 20);
		goldCount.setFont(new Font("Serif", Font.PLAIN, 24));
		health.setBounds(10, 35, 200, 20);
		health.setFont(new Font("Serif", Font.PLAIN, 24));
		levelLabel.setBounds(10, 60, 200, 20);
		levelLabel.setFont(new Font("Serif", Font.PLAIN, 24));
		gameBackground.setBounds(0, 0, 1024, 768);
		gameBackground.setIcon(imageGameBackground);
		gameWalls.setBounds(0, 0, 79, 768);
		gameWalls.setIcon(imageGameWall);
		//city
		ready.setBounds(910, 636, 96, 96);
		ready.setIcon(notReadyUI);
		goldCountCity.setBounds(412,20,200,20);
		goldCountCity.setFont(new Font("Serif", Font.PLAIN, 24));
		levelClearedCity.setBounds(412,50,200,20);
		levelClearedCity.setFont(new Font("Serif", Font.PLAIN, 24));
		cityBackground.setBounds(0, 0, 1024, 640);
		cityBackground.setIcon(imageCityBackground);
		buildUI.setBounds(0, 620, 1024, 128);
		buildUI.setIcon(imageBuyUI);
		buildWalls.setBounds(32, 636, 96, 96);
		buildWalls.setIcon(imageWallsUI);
		buildMasonry.setBounds(138, 636, 96, 96);
		buildMasonry.setIcon(imageMasonryUI);
		buildTavern.setBounds(244, 636, 96, 96);
		buildTavern.setIcon(imageTavernUI);
		buildBlacksmith.setBounds(350, 636, 96, 96);
		buildBlacksmith.setIcon(imageBlacksmithUI);
		buildLaboratory.setBounds(456, 636, 96, 96);
		buildLaboratory.setIcon(imageLaboratoryUI);
		buildWizardTower.setBounds(562, 636, 96, 96);
		buildWizardTower.setIcon(imageWizardTowerUI);
		buildBarracks.setBounds(668, 636, 96, 96);
		buildBarracks.setIcon(imageBarracksUI);
		buildTownhall.setBounds(774, 636, 96, 96);
		buildTownhall.setIcon(imageTownhallUI);
		walls.setBounds(250, 100, 570, 402);
		walls.setIcon(imageWalls);
		masonry.setBounds(450, 250, 86, 60);
		masonry.setIcon(imageMasonry);
		tavern.setBounds(400, 400, 69, 66);
		tavern.setIcon(imageTavern);
		blacksmith.setBounds(650, 400, 55, 39);
		blacksmith.setIcon(imageBlacksmith);
		laboratory.setBounds(600, 150, 51, 49);
		laboratory.setIcon(imageLaboratory);
		wizardTower.setBounds(300, 200, 55, 65);
		wizardTower.setIcon(imageWizardTower);
		barracks.setBounds(500, 200, 68, 47);
		barracks.setIcon(imageBarracks);
		townhall.setBounds(500, 275, 143, 78);
		townhall.setIcon(imageTownhall);
		victoryScreen.setBounds(0,0,1024,768);
		victoryScreen.setIcon(imageVictoryScreen);
	}
}
