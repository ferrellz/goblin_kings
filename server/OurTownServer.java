import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
/*
 * Our huge server class. Allows and waits for two players to connect
 * to the game before launching.
 */
public class OurTownServer {
	Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();
	ArrayList<Monster> monsters = new ArrayList<Monster>();
	ServerSocket ss;
	ArrayList<ServerThread> playerThreads = new ArrayList<ServerThread>();
	Random rnd = new Random();
	final int SCREEN_WIDTH = 1024;
	final int SCREEN_HEIGHT = 768;
	short monsterIds = 0;
	
	int level = 1, gold = 0, maxWallHealth = 100, wallHealth = 100, waveCount = 0,
		monstersKilled = 0, monsterMoveSpeed = 1;
	
	int wallLevel = 0, masonryLevel = 0, tavernLevel = 0, blacksmithLevel = 0, 
		laboratoryLevel = 0, wizardTowerLevel = 0, barracksLevel = 0, townhallLevel = 0;
	
	boolean crazyLevels = false, allReady = false;;
	
	long monsterSpawnTimer, MoveTimer, pMoveTimer, wizardTimer; 

	public OurTownServer(int port) throws IOException {
		listen(port);
	}
	
	public static void main(String args[]) throws Exception{
		int port = 50010;
		if(args.length > 0)
			port = Integer.valueOf(args[0]);
		new OurTownServer(port);
	}
	
	private void listen(int port) throws IOException{
		ss = new ServerSocket(port);
		byte playerCount = 0;
		while(true){
			Socket newSocket = ss.accept();
			newSocket.setTcpNoDelay(true);
			System.out.println("New connection from: " + newSocket);
			DataOutputStream dout = new DataOutputStream(newSocket.getOutputStream());
			outputStreams.put(newSocket, dout);
			playerCount++;
			playerThreads.add(new ServerThread(this, newSocket, playerCount));
			if(playerCount >= 2) break;//break when two players have joined
		}
		while(true){
			allReady = true;
			for(ServerThread s: playerThreads){
				if(!s.ready){
					allReady = false;//wait until all the players are ready to begin from city screen
				}
			}
			if(allReady){//if they all are ready, assign some stuff once and start the game
				System.out.println("All players ready, game starting!");
				sendToAll((short)9, (short)1);
				monsterSpawnTimer = System.currentTimeMillis();
				MoveTimer = System.currentTimeMillis();
				wizardTimer = System.currentTimeMillis();
				for(ServerThread s2: playerThreads){
					s2.y = 100+100*s2.player;
					s2.ready = false;
				}
				monsters.clear();
				newPlayerJoined(playerCount);
				roundBuildingsBonus();
				gameLoop:
				while(true){//actual game loop
					spawnMonsters();
					moveMonsters();
					movePlayers();
					if (checkGameWon()){
						if(crazyLevels && level == 11)
							sendToAll((short)9, (short)3);
						sendToAll((short)9, (short)2);//send state change
						break gameLoop;
					}
				}
			}
		}
	}
	
	Enumeration getOutputStreams() {
		return outputStreams.elements();
	}
	
	/*
	 * Below is our send to player methods.
	 * We always send a short first which indicates what kind of action
	 * we want to do. The following shorts are information about that
	 * action.
	 */
	void sendToAll(short player, short action){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					dout.writeShort(player);
					dout.writeShort(action);
				} catch(IOException ie){}
			}
		}
	}
	
	void newPlayerJoined(short player){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					dout.writeShort(6);//6=new player
					dout.writeShort(playerThreads.size());
					for(int i = 0; i < playerThreads.size(); i++)
						dout.writeShort(playerThreads.get(i).player);
				} catch(IOException ie){}
			}
		}
	}
	
	void sendToAllMonsterSpawn(short x, short y, short id, short m){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					dout.writeShort(3);//3=monsterSpawn
					dout.writeShort(id);
					dout.writeShort(x);
					dout.writeShort(y);
					dout.writeShort(m);
				} catch(IOException ie){}
			}
		}
	}
	
	void sendToAllMonsterMoved(short m, short x){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					dout.writeShort(4);//4=monsterMove
					dout.writeShort(m);
					dout.writeShort(x);
				} catch(IOException ie){}
			}
		}
	}
	
	void sendToAllMonsterMovedClose(short m, short remHp){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					dout.writeShort(8);//8=monsterMoveCLOSED
					dout.writeShort(m);
					dout.writeShort(remHp);
				} catch(IOException ie){}
			}
		}
	}
	
	void removeMonsterFromPlayers(short id, short w, short t){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					dout.writeShort(5);//5=monsterRemove
					dout.writeShort(id);
					dout.writeShort(w);
					dout.writeShort(t);
				} catch(IOException ie){}
			}
		}
	}
	
	void monsterHitToPlayers(short id, short t){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					dout.writeShort(7);//7=monster hit
					dout.writeShort(id);
					dout.writeShort(t);
				} catch(IOException ie){}
			}
		}
	}
	
	void sendToAllPlayerShoots(short id){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					dout.writeShort(11);//11=player shoots
					dout.writeShort(id);
				} catch(IOException ie){}
			}
		}
	}
	
	void sendToAllBought(int b){
		synchronized(outputStreams){
			for(Enumeration e = getOutputStreams(); e.hasMoreElements();){
				DataOutputStream dout = (DataOutputStream)e.nextElement();
				try{
					short bx = (short)b;
					dout.writeShort(12);//12=bought building
					dout.writeShort(bx);
				} catch(IOException ie){}
			}
		}
	}
	
	void removeDeadConnection(Socket socket){
		synchronized(outputStreams){
			System.out.println("Removing connection: "+socket);
			outputStreams.remove(socket);
			try{
				socket.close();
			} catch (IOException ie){	
			}
		}
	}
	
	synchronized void moveMonsters(){
		if(System.currentTimeMillis() - MoveTimer > 100+monsterMoveSpeed){
			for(Iterator<Monster> i = monsters.iterator(); i.hasNext(); ){
				Monster m = i.next();
				if(crazyLevels)
					m.r.x -= 10;
				m.r.x -= 5;
				sendToAllMonsterMoved((short)m.id,(short)m.r.x);
				if(m.r.x <= 50){
					wallHealth -= 20;
					sendToAllMonsterMovedClose((short)m.id, (short)wallHealth);
					i.remove();
					if(wallHealth <= 0){
						gameOver();
					}
				}
			}
			MoveTimer= System.currentTimeMillis();
		}
	}
	
	void gameOver(){
		sendToAll((short)0, (short)0);
		try {
			ss.close();
		} catch (IOException e) {}
		System.exit(0);
	}
	
	void movePlayers(){
		if(System.currentTimeMillis() - pMoveTimer > 25){
			for(ServerThread s : playerThreads){
				if(s.movingDown){
					s.y += 5;
					sendToAll(s.player, (short)s.y);
				}
				if(s.movingUp){
					s.y -= 5;
					sendToAll(s.player, (short)s.y);
				}
				if(!s.movingUp && !s.movingDown && !s.stoppedMoving){
					sendToAll((short)10, s.player);
					s.stoppedMoving = true;
				}
			}
			pMoveTimer = System.currentTimeMillis();
		}
	}

	void spawnMonsters(){
		switch(level){
			default:
			case 1:
				if(System.currentTimeMillis() - monsterSpawnTimer > 5000 && waveCount <= 5){
					doSpawn(0);
					if(crazyLevels) doSpawn(1);
				}
				break;
			case 2:
				if(System.currentTimeMillis() - monsterSpawnTimer > 5000 && waveCount <= 10){
					doSpawn(0);
					if(crazyLevels) doSpawn(1);
				}
				break;
			case 3:
				if(System.currentTimeMillis() - monsterSpawnTimer > 4000 && waveCount <= 10){
					doSpawn(1);
					if(crazyLevels) doSpawn(2);
				}
				break;
			case 4:
				if(System.currentTimeMillis() - monsterSpawnTimer > 4000 && waveCount <= 10){
					doSpawn(2);
					if(crazyLevels) doSpawn(3);
				}
				break;
			case 5:
				if(System.currentTimeMillis() - monsterSpawnTimer > 3000 && waveCount <= 10){
					doSpawn(1);
					if(crazyLevels) doSpawn(2);
				}
				break;
			case 6:
				if(System.currentTimeMillis() - monsterSpawnTimer > 3000 && waveCount <= 10){
					doSpawn(2);
					if(crazyLevels) doSpawn(3);
				}
				break;
			case 7:
				if(System.currentTimeMillis() - monsterSpawnTimer > 3000 && waveCount <= 10){
					doSpawn(2);
					if(crazyLevels) doSpawn(3);
				}
				break;
			case 8:
				if(System.currentTimeMillis() - monsterSpawnTimer > 2000 && waveCount <= 10){
					doSpawn(2);
					if(crazyLevels) doSpawn(3);
				}
				break;
			case 9:
				if(System.currentTimeMillis() - monsterSpawnTimer > 2000 && waveCount <= 10){
					doSpawn(1);
					if(crazyLevels) doSpawn(2);
				}
				break;
			case 10:
				if(System.currentTimeMillis() - monsterSpawnTimer > 2000 && waveCount <= 10){
					doSpawn(1);
					if(crazyLevels) doSpawn(2);
				}
				break;
		}

	}
	//0 = wizard, 1 = knight, 2 = random
	void doSpawn(int i){
		++monsterIds;
		short x = 900;
		short y =  (short) (85 + (short)rnd.nextInt(593));
		short mRes = 1;
		switch(i){
		default:
		case 1: mRes = 1; break;
		case 2: mRes = 2; break;
		case 3: 
			if(rnd.nextInt(2)+1 == 1)
				mRes = 2;
			break;
		}
		monsters.add(new Monster(x, y, monsterIds, mRes));
		sendToAllMonsterSpawn(x, y, monsterIds, mRes);
		monsterSpawnTimer = System.currentTimeMillis();
		waveCount++;
	}
	
	boolean checkGameWon(){
		switch(level){
			default:
			case 1:
				 return helpCheckWon(5);
			case 2://levels 2-10 needs 10 monsters killed
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
				 return helpCheckWon(10);
		}
	}
	
	boolean helpCheckWon(int kills){
		if (monstersKilled >= kills){
			monsterIds = 0;
			monstersKilled = 0;
			waveCount = 0;
			level++;
			if(level > 10){
				if(!crazyLevels){
					level = 1;
					crazyLevels = true;
				}
			}
			return true;
		} else return false;
	}
	
	/*
	 * Our shoot action. 
	 */
	synchronized void shoot(int pY, int dmg){
		for(Iterator<Monster> it = monsters.iterator(); it.hasNext();){
			Monster m = it.next();
			if(pY+25 >= m.r.y && pY+25 <= m.r.y+55){//check for hit
				if(pY+25 >= m.r.y && pY+25 <= m.r.y+20){//check for headshot(will do critical damage)
					if(m.hit(dmg*2)){
						removeMonsterFromPlayers(m.id, getWizardBonus(m.wealth), (short)(m.type+10));
						gold += getWizardBonus(m.wealth);
						it.remove();
						monstersKilled++;
						break;
					} else{
						monsterHitToPlayers(m.id, (short)(m.type+10));
						break;
					}
				} else {
					if(m.hit(dmg)){
						removeMonsterFromPlayers(m.id, getWizardBonus(m.wealth), m.type);
						gold += getWizardBonus(m.wealth);
						it.remove();
						monstersKilled++;
						break;
					} else{
						monsterHitToPlayers(m.id, m.type);
						break;
					}
				}
			}
		}
	}
	
	void roundBuildingsBonus(){
		switch(masonryLevel){
		default:
		case 0: break;
		case 1: if(wallHealth > maxWallHealth) wallHealth = maxWallHealth; else wallHealth += 10; break;
		case 2: if(wallHealth > maxWallHealth) wallHealth = maxWallHealth; else wallHealth += 15; break;
		case 3: if(wallHealth > maxWallHealth) wallHealth = maxWallHealth; else wallHealth += 20; break;
		}
		switch(townhallLevel){
		default:
		case 0: break;
		case 1: if(wallHealth > maxWallHealth) wallHealth = maxWallHealth; else wallHealth += 5; gold += 50; break;
		case 2: if(wallHealth > maxWallHealth) wallHealth = maxWallHealth; else wallHealth += 8; gold += 100; break;
		case 3: if(wallHealth > maxWallHealth) wallHealth = maxWallHealth; else wallHealth += 50; gold += 800; break;
		}
		switch(tavernLevel){
		default:
		case 0: break;
		case 1: gold += 100; break;
		case 2: gold += 200; break;
		case 3: gold += 300; break;
		}
		switch(barracksLevel){
		default:
		case 0: monsterMoveSpeed = 0; break;
		case 1: monsterMoveSpeed = 30; break;
		case 2: monsterMoveSpeed = 50; break;
		case 3: monsterMoveSpeed = 100; break;
		}
	}
	
	short getWizardBonus(short base){
		switch(wizardTowerLevel){
		default:
		case 0: return base;
		case 1: return (short) (base*2);
		case 2: return (short) (base*3);
		case 3: return (short) (base*4);
		}
	}
	
	/*
	 * Take into effect the buying of a building and
	 * then send back a confirmation that it has been bought.
	 */
	void buyBuilding(int b){
		switch(b){
			case 5://walls
				if(wallLevel == 0 && gold >= 100){
					maxWallHealth = 150;
					gold -= 100;
					sendToAllBought(5);
					wallLevel++;
				}else if(wallLevel == 1 && gold >= 250){
					maxWallHealth = 200;
					gold -= 250;
					sendToAllBought(5);
					wallLevel++;
				}else if(wallLevel == 2 && gold >= 500){
					maxWallHealth = 250;
					gold -= 500;
					sendToAllBought(5);
					wallLevel++;
				}
			break;
			case 6://masonry
				if(masonryLevel == 0 && gold >= 150){
					gold -= 150;
					sendToAllBought(6);
					masonryLevel++;
				}else if(masonryLevel == 1 && gold >= 250){
					gold -= 250;
					sendToAllBought(6);
					masonryLevel++;
				}else if(masonryLevel == 2 && gold >= 450){
					gold -= 450;
					sendToAllBought(6);
					masonryLevel++;
				}
			break;
			case 7://tavern
				if(tavernLevel == 0 && gold >= 250){
					gold -= 250;
					sendToAllBought(7);
					tavernLevel++;
				}else if(tavernLevel == 1 && gold >= 500){
					gold -= 500;
					sendToAllBought(7);
					tavernLevel++;
				}else if(tavernLevel == 2 && gold >= 700){
					gold -= 700;
					sendToAllBought(7);
				}
			break;
			case 8://blacksmith
				if(blacksmithLevel == 0 && gold >= 200){
					for(ServerThread s : playerThreads)
						s.damage = 35;
					gold -= 200;
					sendToAllBought(8);
					blacksmithLevel++;
				}else if(blacksmithLevel == 1 && gold >= 400){
					for(ServerThread s : playerThreads)
						s.damage = 50;
					gold -= 400;
					sendToAllBought(8);
					blacksmithLevel++;
				}else if(blacksmithLevel == 2 && gold >= 800){
					for(ServerThread s : playerThreads)
						s.damage = 65;
					gold -= 800;
					sendToAllBought(8);
					blacksmithLevel++;
				}
			break;
			case 9://laboratory
				if(laboratoryLevel == 0 && gold >= 300){
					for(ServerThread s : playerThreads)
						s.reloadTime = 800;
					gold -= 300;
					sendToAllBought(9);
					laboratoryLevel++;
				}else if(laboratoryLevel == 1 && gold >= 700){
					for(ServerThread s : playerThreads)
						s.reloadTime = 500;
					gold -= 700;
					sendToAllBought(9);
					laboratoryLevel++;
				}else if(laboratoryLevel == 2 && gold >= 1000){
					for(ServerThread s : playerThreads)
						s.reloadTime = 250;
					gold -= 1000;
					sendToAllBought(9);
					laboratoryLevel++;
				}
			break;
			case 10://wizard tower
				if(wizardTowerLevel == 0 && gold >= 400){
					gold -= 400;
					sendToAllBought(10);
					wizardTowerLevel++;
				}else if(wizardTowerLevel == 1 && gold >= 800){
					gold -= 800;
					sendToAllBought(10);
					wizardTowerLevel++;
				}else if(wizardTowerLevel == 2 && gold >= 1200){
					gold -= 1200;
					sendToAllBought(10);
					wizardTowerLevel++;
				}
			break;
			case 11://barracks
				if(barracksLevel == 0 && gold >= 350){
					gold -= 350;
					sendToAllBought(11);
					barracksLevel++;
				}else if(barracksLevel == 1 && gold >= 600){
					gold -= 600;
					sendToAllBought(11);
					barracksLevel++;
				}else if(barracksLevel == 2 && gold >= 800){
					gold -= 800;
					sendToAllBought(11);
					barracksLevel++;
				}
			break;
			case 12://townhall
				if(townhallLevel == 0 && gold >= 250){
					gold -= 250;
					sendToAllBought(12);
					townhallLevel++;
				}else if(townhallLevel == 1 && gold >= 400){
					gold -= 400;
					sendToAllBought(12);
					townhallLevel++;
				}else if(townhallLevel == 2 && gold >= 1000){
					gold -= 1000;
					sendToAllBought(12);
					townhallLevel++;
				}
			break;
			default:break;
		}
	}

}
