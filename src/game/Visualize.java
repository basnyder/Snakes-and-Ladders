
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
		
	}
    
    public void keyPressed() {
        if (key == 'n' || key == 'N') {
            setup();
        }
        switch (key) {
            case 'r':
            case 'R':
                init();
                break;
            case ' ':
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
