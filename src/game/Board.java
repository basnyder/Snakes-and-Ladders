/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author Brett
 */
public class Board {
	public Box[] board;
	Player player1;
	Player player2;
	Player currentPlayer;
	
	public Board() {
		this(0.09, 0.08);
	}
	
	public Board(double pSnake, double pLadder) {
		player1 = new Player();
		player2 = new Player();
		currentPlayer = player1;
		
		board = new Box[100];
		for (int i = 0; i < 100; i++) {
			double rand = Math.random();
			if (rand < pSnake) {//snake
				board[i] = new SpecialBox(i, -(int)(Math.random()*(i-10)));
			} else if (rand < pSnake + pLadder) {//ladder
				board[i] = new SpecialBox(i, (int)(Math.random()*(90-i)));
			} else {//normal
				board[i] = new NormalBox(i);
			}
		}
	}
	
	
	private abstract class BoxType implements Box {
		public int pos;
		public void landedOn() {}
	}
	
	private class SpecialBox extends BoxType {
		public int endPos;
		
		public SpecialBox(int pos, int dist) {
			this.pos = pos;
			this.endPos = pos + dist;
		}
		
		public void landedOn() {
			currentPlayer.pos = endPos;
		}
	}
	
	private class NormalBox extends BoxType {
		public NormalBox(int pos) {
			this.pos = pos;
		}
	}
}
