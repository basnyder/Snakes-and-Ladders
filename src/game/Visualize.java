
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import processing.core.*;
/**
 *
 * @author Brett
 */
public class Visualize extends PApplet {
	Board b;
	
	public void settings() {
        size(600, 600);
    }
    
    public void setup() {
        background(0);
        frameRate(100);
		b = new Board(this);
		init();
		drawBoard();
    }
    
    public void draw() {
		background(0);
		drawBoard();
    }
	
	public void init() {
		b.startGame();
//		System.out.println(b.toString());
	}
	
	public void drawBoard() {
		b.draw(this);
	}
	
	public void rollDice() {
		b.takeTurn((int)(Math.random()*10)+1);
	}
    
    public void keyPressed() {
        /*if (Character.isDigit(key)) {
			System.out.println();
			b.takeTurn(key-'0');
        }*/
        switch (key) {
            case 'r':
            case 'R':
				//restart
                init();
                break;
			case 'n':
            case 'N':
				//new game
                setup();
                break;
            case ' ':
				//role dice
                rollDice();
                break;
            /*
            case CODED:
                switch (keyCode) {
                    case DOWN:
                        
                        break;
                    case UP:
                        
                        break;
                    case LEFT:
						
                        break;
                    case RIGHT:
						
                        break;
                    case RETURN:
                    case ENTER:
                        
                        break;
                }
                break;
			*/
        }
    }
    
    
    public static void main(String[] args) {
        PApplet.main(new String[]{game.Visualize.class.getName()});
    }
}
