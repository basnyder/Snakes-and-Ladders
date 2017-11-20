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
	
	public Board(PApplet p) {
		this(p, 0.09, 0.08);
	}
	
	public Board(PApplet p, double pSnake, double pLadder) {
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
					(rand >= pSnake && rand < pSnake+pLadder && (i>85 || i==0)) || //wrong spot for ladder
					ends.contains(i)) {//normal box
				board[i] = new NormalBox(i);
			} else {//special box
				int dist;
				if (rand < pSnake) {//snake
					do {
						dist = (int)(Math.random()*i);
					} while (dist < 10 || ends.contains(i-dist));
					ends.add(i-dist);
					board[i] = new SnakeBox(i, dist);
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
		if (currentPlayer.pos + spaces > 100) {
			//error, cant move
			
		} else if (currentPlayer.pos + spaces == 100) {
			//winner!
			
		} else {
			//move spaces
			currentPlayer.move(spaces);
		}
		
		
		//switch players
		currentPlayer = ((currentPlayer==player1) ? player2 : player1);
	}
	
	public abstract class BoxType implements Box {
		public int pos;
		public float x,y;//center of box
		
		public void landedOn() {}
		
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
			p.rect(x-p.width/20f, y-p.height/20f, p.width/10f-1, p.height/10f-1);
			p.stroke(255);
			p.fill(255);
			p.textAlign(PApplet.CENTER, PApplet.CENTER);
			p.text(toString(), x, y-1.5f);
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
	}
	
	private class SnakeBox extends SpecialBox {
		public SnakeBox(int pos, int dist) {
			this.pos = pos;
			this.endPos = pos-dist;
			setXY();
			setEndXY();
		}
		
		public void landedOn() {
			currentPlayer.moveTo(endPos);
		}
		
		public String toString() {
			return super.toString() + " S " + (endPos+1);
		}
	}
	
	private class LadderBox extends SpecialBox {
		public LadderBox(int pos, int dist) {
			this.pos = pos;
			this.endPos = pos+dist;
			setXY();
			setEndXY();
		}
		
		public void landedOn() {
			currentPlayer.moveTo(endPos);
		}
		
		public String toString() {
			return super.toString() + " L " + (endPos+1);
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
					p.strokeWeight(5);
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
		p.ellipse(posToX(player1.pos)+p.width/40f, posToY(player1.pos)-p.height/40f, p.width/30f, p.height/30f);
		//p2 down and right
		p.fill(0,0,255);
		p.ellipse(posToX(player2.pos)+p.width/40f, posToY(player2.pos)+p.height/40f, p.width/30f, p.height/30f);
	}
	
	
	
	
	private float posToX(int pos) {
		return (((pos/10)&1) == 1) ? p.width*(19f/20f-((pos%10)/10f)) : (pos%10)*p.width/10f+p.width/20f;
	}
	
	private float posToY(int pos) {
		return  p.height-((pos/10)+1)*p.height/10f+p.height/20f;
	}
	
	private void drawRoute(SpecialBox from) {
		p.line(from.x, from.y, from.endX, from.endY);
	}
	
	public static void main(String[] args) {
        Visualize.main(args);
    }
}
