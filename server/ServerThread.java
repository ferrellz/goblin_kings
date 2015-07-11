import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

/*
 * Simple service thread for our players. 
 * We store some information about each player here in 
 * addition of just storing information about the connection.
 */
public class ServerThread extends Thread{
	private OurTownServer serv;
	private Socket sock;
	int y;
	short player;
	boolean movingUp;
	boolean movingDown;
	boolean stoppedMoving;
	long lastShot;
	int reloadTime = 1000;
	int damage = 25;
	boolean ready = false;
	
	public ServerThread(OurTownServer s, Socket ss, short p){
		serv = s;
		sock = ss;
		player = p;
		y = 100+100*p;
		start();
	}
	
	public void run(){
		try{
			DataInputStream din = new DataInputStream(sock.getInputStream());
			while(true){
				int message = din.readByte();
				switch(message){
					case 1: movingUp = !movingUp; stoppedMoving = false; break; //up
					case 2: movingDown = !movingDown; stoppedMoving = false; break; //down
					case 3://shoot
						if(System.currentTimeMillis() - lastShot >= reloadTime){
							serv.sendToAllPlayerShoots(player); 
							serv.shoot(y, damage);
							lastShot = System.currentTimeMillis();
						}
						break;
					case 4: ready = !ready; break; //clicked on ready button
					
					case 5: serv.buyBuilding(5); break;
					case 6: serv.buyBuilding(6); break;
					case 7: serv.buyBuilding(7); break;
					case 8: serv.buyBuilding(8); break;
					case 9: serv.buyBuilding(9); break;
					case 10: serv.buyBuilding(10); break;
					case 11: serv.buyBuilding(11); break;
					case 12: serv.buyBuilding(12); break;
					default: break;
				}
			}
		} catch(EOFException ie){

		}
		catch (IOException ie){
			ie.printStackTrace();
		} finally {
			serv.removeDeadConnection(sock);
		}
	}
}
