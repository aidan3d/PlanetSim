/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aidan3d.planetsim;

import GamePanel.GamePanel;             // a Swing JPanel-derived object

import java.awt.Color;                  // used for the color enumerations (e.g., .white)
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;


/**
 * A <b>PlanetSimPanel</b> is a <b>GamePanel</b> is a Swing <b>JPanel</b>.
 * <p> 
 * The <b>PlanetSimPanel</b> class inherits from a <b>JPanel</b> class, adding in a
 * few fields specific to <i>PlanetSim</i>; primarily, a <b>Planet</b>
 * ArrayListobject (named "planets").<br>
 * A <b>PlanetSimPanel</b> object's job is to set up the game components and initialize
 * the timing system.
 */
public class PlanetSimPanel extends GamePanel {
    private final Font wpfont;
    private final FontMetrics metrics;
    private ArrayList<Planet> planets;  // a string of orbs marching across space
    Vector2D force = new Vector2D();


    /**
     * The no-argument constructor. It calls the two-argument constructor, pas<br>
     * passing it a null reference to a <b>PlanetSim</b> object, and a long int,
     * initialized to 0.
     */
    public PlanetSimPanel() {
        // feed the two-argument constructor
        this(null, 0L);
    }


    /**
     * The two-argument constructor
     * @param egg  a reference to a <b>PlanetSim</b> object
     * @param p  long integer representing the number of seconds
     * per frame (i.e., 1fps => 1000 ms)
     */
    public PlanetSimPanel(PlanetSim egg, long p) {
        // initialize the "parent" class (a GamePanel object)
        super(p);  
        
        setBackground(Color.white);
        setPreferredSize(new java.awt.Dimension(PWIDTH, PHEIGHT));

        // create a sequence of four Planet objects
        planets = new ArrayList<>();

        // initialize the gravitational force accumulator
        force = new Vector2D();
        
        // set up the message font
        wpfont = new Font("SansSerif", Font.BOLD, 12);
        metrics = this.getFontMetrics(wpfont);
        
        // Build a solar system!
        createPlanets();
    }


    /**
     * This function crafts a few planets.
     */
    private void createPlanets() {
        // add the Sun (considered a Planet object for our purposes); radius: 30px
        planets.add(new Planet("sun", 6400, new Point2D(PWIDTH/2.0-32, PHEIGHT/2.0-32),
            new Point2D(0.0, 0.0), Color.yellow, 30));
 
        // add Mercury; radius: 20px
        planets.add(new Planet("mercury", 200, new Point2D(PWIDTH/2.0-10,
            PHEIGHT/2.0-160-10), new Point2D(-5.0, 0.0), Color.orange, 20));
        
         // add Venus; radius: 16px
        planets.add(new Planet("venus", 160, new Point2D(PWIDTH/2.0-5,
            PHEIGHT/2.0+175-5), new Point2D(6.0, 0.0), Color.red, 16));

        // add Earth; radius: 20px
        planets.add(new Planet("earth", 400, new Point2D(PWIDTH/2.0-45,
            PHEIGHT/2.0-240-45), new Point2D(-5.0, 0.0), Color.cyan, 40));
    }


    private void calcAcceleration(Planet obj) {
        // get the magnitude of the force to look at
        double magnitude = Math.pow(Math.pow(force.x(), 2.0) + Math.pow(force.y(), 2.0), 0.5);

        // display the force-on and velocity-of each planet
        System.out.printf("%s: force: %.2f @(%.2f, %.2f)%n", obj.getId(), magnitude,
            obj.getDisplacement().x(), obj.getDisplacement().x());

        // calculate the acceleration based on F = ma => a = F/m or a = f * m^-1
        Vector2D acceleration = force.times(Math.pow(obj.getMass(), -1.0));
        
        // increase the velocity by the direction and amount of the acceleration
        obj.setVelocity(obj.getVelocity().plus(acceleration));
    }


    private Vector2D calcGravForce(Planet obj1, Planet obj2) {
        // ignore G for the purposes of this program. it is of such a small value that we
        // round up to 1, to simplify the math.
        double G = 1.0F;

        // the distance between the two planets under the looking-glass
        Vector2D displacement = obj1.getDisplacement().minus(obj2.getDisplacement());

        // holds the magnitude of the distance betwen the two planets
        double magnitude = Math.pow((Math.pow(displacement.x(), 2.0) + Math.pow(displacement.y(), 2.0)),0.5);

        // use F = (G * m1 * m2) / d^2 in Euclidian vector form. thanks Cape Town U!
        // http://www.phy.uct.ac.za/courses/opencontent/phy1004w/week4.pdf 
        return displacement.times(-1*(G * obj1.getMass() * obj2.getMass()) / Math.pow(magnitude, 3.0));
    }


    /**
     * This is the initialization method that should be overridden 
     * by the derived class. This method will only be called once:
     * to set up game objects.
     */
    @Override
    public void customizeInit() {
    }


    /**
     * This method should be overridden during inheritance
     * and customized for your game to handle frame
     * rendering.
     */
    @Override
    public void customizeGameRender() {   
        // check whether the game has ended for each rendering cycle. This is effectively
        // the "Update" method. customizeGameRender is presumably called for each frame or
        // "game loop" run.
        if (!super.gameOver) {
            dbg.setColor(Color.black);
            dbg.fillRect(0, 0, super.getWidth(), super.getHeight());

            dbg.setColor(Color.green);
            dbg.setFont(wpfont);

            // report average FPS and UPS at top left
            dbg.drawString("Average FPS/UPS: " + df.format(getAverageFPS()) + "/"
                    + df.format(getAverageUPS()), 20, 25);

            dbg.setColor(Color.black);

            // Traverse our list of orbs "backwards."
            for (int i = planets.size()-1; i >= 0; i--)
                planets.get(i).draw(dbg);
        }
    }


    /**
     * This is the GameUpdate() method that should be overridden during inheritance
     * and customized for your game to handle frame updates. customizeGameUpdate
     * helps the celestial spheres move around.
     */
    @Override
    public void customizeGameUpdate() {
        // let the user know that we are in the customizeGameUpdate() method
        System.out.println("Update game state");
        
        // starting with the left-plus-one-most planet, traverse...
        for(int i = 1; i < planets.size(); i++) {
            for (Planet p1 : planets) {
                if (!planets.get(i).equals(p1)) {
                    // add the attractive force between our current traversing element with
                    // its neighbor "to-the-right"
                    force = force.plus(calcGravForce(planets.get(i), p1));
                }
            }
            
            // Calculate the new velocity for our Planet object at planets[i].
            calcAcceleration(planets.get(i));

            // reset the gravitational attraction all other "party-goers" are feeling
            // for the current planet.
            force = force.times(0);
             
        } // end outer for

        // run through all planets and move them (the Sun will not move as no acceleration
        // has been calculated for it)
        for (Planet p2 : planets)
            // Schooch each orb along a bit.
            p2.move();

    } // end method customizeGameUpdate


    /**
     * This method runs before the first "clink" of
     * the timing chain (e.g., the main game loop or
     * "move-update" cycle runner)
     */
    @Override
    protected void preGameLoop() {
    }


    /**
     * This method runs as soon as we are "within" the game loop.
     */
    @Override
    protected void insideGameLoop() {
    }


    /**
     * This method holds operations carried out at the <i>PlanetSim</i>'s end.
     * After play is complete, a message such as <i>"You've Won!"</i> might be
     * displayed at this point in time.
     */
    @Override
    protected void postGameLoop() {
    }
}
