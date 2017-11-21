/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import java.util.List;
import processing.core.PApplet;
/**
 *
 * @author Brett
 */
public class Board {
	public BoxType[] board;
	private final Player player1;
	private final Player player2;
	public Player currentPlayer;
	PApplet p;
	String printout;
	
	public Board(PApplet p) {
		this(p, 0.09, 0.08);
	}
	
	public Board(PApplet p, double d) {
		this(p, d, d);
	}
	
	public Board(PApplet p, double pSnake, double pLadder) {
		printout = "press space to take your turn";
		this.p = p;
		player1 = new Player(1);
		player2 = new Player(2);
		startGame();
		
		List<Integer> ends = new ArrayList<>();		
		
		board = new BoxType[100];
		for (int i = 0; i < 100; i++) {
			if (board[i] != null) {
				continue;
			}
			double rand = Math.random();
			if (rand >= pSnake+pLadder || //normal box
					(rand < pSnake && (i<15 || i==99)) || //wrong spot for snake
					(rand >= pSnake && rand < pSnake+pLadder && (i>85 || i==0)) //wrong spot for ladder
				) {//normal box
				board[i] = new NormalBox(i);
			} else {//special box
				int dist;
				if (rand < pSnake) {//snake
					do {
						dist = (int)(Math.random()*i);
					} while (dist < 10 || ends.contains(i-dist));
					ends.add(i-dist);
					board[i] = new SnakeBox(i, dist);
					board[i-dist] = new NormalBox(i-dist);
				} else {//ladder
					do {
						dist = (int)(Math.random()*(99-i));
					} while (dist < 10 || ends.contains(i+dist));
					ends.add(i+dist);
					board[i] = new LadderBox(i, dist);
					board[i+dist] = new NormalBox(i+dist);
				}
			}
			
		}
	}
	
	public final void startGame() {
		player1.pos=0;
		player2.pos=0;
		currentPlayer = player1;
	}
	
	public int getSnakes () {
		int n = 0;
		for (int i = 0; i < 100; i++) {
			if (board[i] instanceof SnakeBox) {
				n++;
			}
		}
		return n;
	}
	
	public int getLadders () {
		int n = 0;
		for (int i = 0; i < 100; i++) {
			if (board[i] instanceof LadderBox) {
				n++;
			}
		}
		return n;
	}
	
	public String toString() {
		String s = "";
		s += player1.toString() + "\n" + player2.toString() + "\n";
		s += "current player:" + ((currentPlayer==player1) ? "player 1" : "player 2") + "\n";
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				s += board[i*10+j].toString()+"\t";
			}
			s += "\n";
		}
		return s;
	}
	
	public void takeTurn(int spaces) {
		printout = "player"+((currentPlayer==player1) ? "1" : "2");
		if (currentPlayer.getBox() == 100) {
			//already won
			printout += " WINS!!!";
		} else if (currentPlayer.getBox() + spaces == 100) {
			//winner
//			currentPlayer.move(spaces);
			board[99].landedOn(currentPlayer);
			printout += " rolled " + spaces +  " WINS!!!";
		} else {
			if (currentPlayer.getBox() + spaces > 100) {
				//error, cant move
				printout += " rolled " + spaces +  " cant move above past 100";
			} else {
				//move spaces
//				currentPlayer.move(spaces);
				printout += ":\t moved "+spaces + " spaces" ;//+ currentPlayer.getBox();

				board[currentPlayer.pos+spaces].landedOn(currentPlayer);
//				printout += ":\t moved "+spaces + " spaces " ;//+ currentPlayer.getBox();
			}
			//switch players
			currentPlayer = ((currentPlayer==player1) ? player2 : player1);
		}
		
		
	}
	
	public abstract class BoxType implements Box {
		public int pos;
		public float x,y;//center of box
		
		public void landedOn(Player p) {
			p.moveTo(pos);
			printout += " to " + p.getBox();
		}
		
		public String toString() {
			return ""+(pos+1);
		}
		
		protected final void setXY() {
			x = posToX(pos);
			y = posToY(pos);
		}
		
		public void draw() {
			p.fill(127);
			p.strokeWeight(1);
			p.noStroke();
			if (this instanceof NormalBox) {//normal

			} else {
				if (this instanceof LadderBox) {//ladder
					p.stroke(0, 255, 0);
				} else {//snake
					p.stroke(255, 0, 0);
				}
			}
			p.rect(x-p.width*7f/8f/20f, y-p.height*7f/8f/20f, p.width*7f/8f/10f-1, p.height*7f/8f/10f-1);
			p.stroke(255);
			p.fill(255);
			p.textAlign(PApplet.CENTER, PApplet.CENTER);
			p.text(pos+1, x, y-1.5f);
		}
	}
	
	
	
	private class NormalBox extends BoxType {
		public NormalBox(int pos) {
			this.pos = pos;
			setXY();
		}
	}
	
	private abstract class SpecialBox extends BoxType {
		public int endPos;
		public float endX, endY;
		
		protected final void setEndXY() {
			endX = posToX(endPos);
			endY = posToY(endPos);
		}
		
		public void landedOn(Player p) {
			p.moveTo(endPos);
		}
	}
	
	private class SnakeBox extends SpecialBox {
		public SnakeBox(int pos, int dist) {
			this.pos = pos;
			this.endPos = pos-dist;
			setXY();
			setEndXY();
		}
		
		public String toString() {
			return super.toString() + " S " + (endPos+1);
		}
		
		public void landedOn(Player p) {
			p.moveTo(endPos);
			printout += "\nsnaked from " + (pos+1) + " down to " + p.getBox();
			System.out.println("snaked from " + (pos+1) + " down to " + p.getBox());
		}
	}
	
	private class LadderBox extends SpecialBox {
		public LadderBox(int pos, int dist) {
			this.pos = pos;
			this.endPos = pos+dist;
			setXY();
			setEndXY();
		}
		
		public String toString() {
			return super.toString() + " L " + (endPos+1);
		}
		
		public void landedOn(Player p) {
			p.moveTo(endPos);
			printout += "\nladdered from " + (pos+1) + " up to " + p.getBox();
			System.out.println("laddered from " + (pos+1) + " up to " + p.getBox());
		}
	}
	
	public void draw(PApplet p) {
		//draw boxes
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				BoxType b = board[i*10+j];
				b.draw();
			}
		}
		//draw snakes and ladders
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				BoxType b = board[i*10+j];
				if (b instanceof SpecialBox) {
					p.strokeWeight(3);
					if (b instanceof LadderBox) {//ladder
						p.stroke(0, 255, 0);
					} else {//snake
						p.stroke(255, 0, 0);
					}
					drawRoute((SpecialBox)b);
				}
			}
		}
		//draw players
		p.noStroke();
		//p1 up and right
		p.fill(255,127,0);
		p.ellipse(posToX(player1.pos)+p.width*7f/8f/40f, posToY(player1.pos)-p.height*7f/8f/40f, p.width*7f/8f/30f, p.height*7f/8f/30f);
		//p2 down and right
		p.fill(0,0,255);
		p.ellipse(posToX(player2.pos)+p.width*7f/8f/40f, posToY(player2.pos)+p.height*7f/8f/40f, p.width*7f/8f/30f, p.height*7f/8f/30f);
		
		p.fill(255);
		p.textAlign(p.LEFT, p.TOP);
		p.text(printout, 0*p.width/2, p.height*7/8);
	}
	
		
	private float posToX(int pos) {
		return (((pos/10)&1) == 1) ? p.width*7f/8f*(19f/20f-((pos%10)/10f)) : (pos%10)*p.width*7f/8f/10f+p.width*7f/8f/20f;
	}
	
	private float posToY(int pos) {
		return  p.height*7f/8f-((pos/10)+1)*p.height*7f/8f/10f+p.height*7f/8f/20f;
	}
	
	private void drawRoute(SpecialBox from) {
		p.line(from.x, from.y, from.endX, from.endY);
		
		float len = PApplet.sqrt((from.x-from.endX)*((from.x-from.endX)) + (from.y-from.endY)*(from.y-from.endY));
		float ang = PApplet.atan((from.y-from.endY)/(from.x-from.endX));
		if (from.endX<=from.x) {
			ang+=PApplet.PI;
		}
		
		p.pushMatrix();
		p.translate(from.x, from.y);
		p.rotate(ang);
		p.line(0,0,len, 0);
		p.line(len, 0, len - 8, -8);
		p.line(len, 0, len - 8, 8);
		p.popMatrix();
	}
	
	public static void main(String[] args) {
        Visualize.main(args);
    }
}
