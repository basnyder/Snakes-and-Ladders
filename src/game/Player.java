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
public class Player {
	public int pos;
	public int id;
	
	public Player() {
		this(0);
	}
	
	public Player(int id) {
		this.pos = 0;
		this.id = id;
	}
	
	public String toString() {
		return "player " + id + ",\tpos: " + pos;
	}
	
	public void move(int spaces) {
		pos += spaces;
	}
	
	public void moveTo(int space) {
		pos = space;
	}
	
	public int getBox() {
		return pos+1;
	}
	
}
