/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aidan3d.planetsim;

import java.awt.Color;
import java.awt.Graphics;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;


/**
 * This is the Planet class's default constructor.
 * This class is effectively the Planet "sprite."
 * 
 */
class Planet {
    private int dotSize;                             // The planet's visual
                                                     // radius (a few pixels)
    private String id;
    private double mass;
    private Vector2D displacement;
    private Vector2D velocity;
    private Color color;


/**
 * 
 * @param name A String holding the planet's human-readable identifier
 * @param m  the planet's mass, units: kg
 * @param d  the planets displacement, a Euclidan 2d vector, units: (meters, meters)
 * @param v   the planets velocity Euclidian 2d vector, ms^-1
 * @param c   the color used to key the planet on screen
 */
    public Planet(String name, double m, Point2D d, Point2D v, Color c, int ds) {
        dotSize = ds;
        id = name;
        mass = m;
        displacement = new Vector2D(d);
        velocity = new Vector2D(v);
        color = c;
    }


    /**
     * Setter for the Vector2D object
     * @param a Vector2D object
     */
    public void setVelocity(Vector2D v) {
        velocity = v;
    }


    /**
     * Getter for the displacement Vector2D object.
     * @return a Vector2D object
     */
    public Vector2D getDisplacement() {
        return displacement;
    }


    /**
     * Getter for the planet's name.
     * @return the planet's name
     */
    public String getId() {
        return id;
    }


    /**
     * Getter for the value stored in the instance
     * variable "mass."
     */
    public double getMass() {
        return mass;
    }


    /**
     * Getter for the Vector2D object velocity.
     */
    public Vector2D getVelocity() {
        return velocity;
    }


    /**
     * This method draws the planet to screen.
     * @param dbg the canvas to draw on 
     */
    public void draw(Graphics dbg) {
        dbg.setColor(color);            
        dbg.fillOval((int)displacement.getX(), (int)displacement.getY(), dotSize, dotSize);
    }


    /**
     * This is the traction method for the Planet object.
     */
    public void move() {
        // Velocity is effectively the number of pixels by which the Planet
        // object will move when this function is called for each gameUpdate
        // cycle (i.e., px is a great start).
        displacement = displacement.plus(velocity);
    }
}
