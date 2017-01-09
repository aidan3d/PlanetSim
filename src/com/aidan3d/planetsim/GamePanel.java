/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aidan3d.planetsim;

import javax.swing.JPanel;


public class GamePanel extends JPanel implements Runnable {
    
    private static final int PWIDTH = 500;      // the size of the panel
    private static final int PHEIGHT = 400;
    
    private Thread animator;                    // for the animation
    private boolean running = false;            // stops the animation
    
    private boolean gameOver = false;           // for game termination
    
    // more variables, explained later
    //    .
    //    .
    
    
    public GamePanel() {
    }
    
    
    @Override
    public void run() {
        // to change body of generated methods, choose Tools | Templates
        throw new UnsupportedOperationException("Not supported yet."); 
    }    
}
