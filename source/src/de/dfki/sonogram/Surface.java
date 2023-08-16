/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Surface class Renders a single, interactively rotatable, traslatable, and zoomable
 * Sonogram-Surface object.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
package de.dfki.sonogram;

import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.vecmath.*;

public class Surface extends JFrame {
  static Sonogram reftomain;
  TransformGroup objTransform = new TransformGroup();
  SceneRotatorThread rotateThread;
  boolean isWv;

  // --------------------------------------------------------------------------------------------
  // cereate a simple scene and attach it to the virtual universe
  public Surface(Sonogram sono, boolean isWvArgument) {
    reftomain = sono;

    isWv = isWvArgument;
    System.out.println("--> Painting Surface");
    BranchGroup scene = createSceneGraph();
    if (scene == null) {
      dispose();
      return;
    }
    setSize(600, 600);
    setLocation(150, 150);
    if (isWv == true) setTitle("Wigner-Ville - Perspectogram");
    else setTitle("Perspectogram");
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));

    // get an Canvas3d
    GraphicsConfigTemplate3D gcTemplate = new GraphicsConfigTemplate3D();
    GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
    // there could be more than one screen
    GraphicsDevice screen = local.getDefaultScreenDevice();
    GraphicsConfiguration configuration = screen.getBestConfiguration(gcTemplate);
    // Get a Canvas3D and call its constructor with the configuration
    Canvas3D canvas3D = new Canvas3D(configuration);
    canvas3D.addMouseListener(
        new MouseInputAdapter() {
          public void mouseClicked(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseDragged(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseEntered(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseMoved(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mousePressed(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseReleased(MouseEvent e) {
            rotateThread.resetTime();
          }
        });
    canvas3D.addMouseMotionListener(
        new MouseMotionAdapter() {
          public void mouseDragged(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseMoved(MouseEvent e) {
            rotateThread.resetTime();
          }
        });
    canvas3D.addKeyListener(
        new KeyAdapter() {
          public void keyPressed(KeyEvent e) {
            rotateThread.resetTime();
          }

          public void keyReleased(KeyEvent e) {
            rotateThread.resetTime();
          }

          public void keyTyped(KeyEvent e) {
            rotateThread.resetTime();
          }
        });

    canvas3D.setDoubleBufferEnable(true);
    getContentPane().add(canvas3D);

    // SimpleUniverse is a Convenience Utility class
    SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
    canvas3D.getView().setSceneAntialiasingEnable(reftomain.iperantialias);
    canvas3D.getView().setDepthBufferFreezeTransparent(false);

    // objects in the scene can be viewed.
    simpleU.getViewingPlatform().setNominalViewingTransform();

    WindowListener wndCloser = new WindowAdapter() // Windowlistener
        {
          public void windowClosing(WindowEvent e) {
            dispose();
          }
        };
    addWindowListener(wndCloser);
    simpleU.addBranchGraph(scene);

    setVisible(true);

    if (sono.perkeysconfirm == false) {
      message m = new message();
      m.start();
    }
  }

  // ---------------------------------------------------------------------------------------------------------------

  class message extends Thread implements ActionListener {
    private JCheckBox askAgain;

    public void run() {
      askAgain =
          new JCheckBox("<html><i><font size = -2>Do not show this information message again");
      Object[] message = new Object[2];
      message[1] = askAgain;
      message[0] =
          "<html><font size = 4>You can use the mouse or the keyboard<br>"
              + "to move and scale the Perspectogram:<br><br><pre>"
              + "<u><font size = 5>Keyboard:</font></u><br>"
              + "<font size=4>Left/Right         </font> = <u>Rotate Horizontal</u><br>"
              + "<font size=4>Up/Down            </font> = <u>Rotate Vertical</u><br>"
              + "<font size=4>PageUp/PageDown    </font> = <u>Zoom</u><br><br>"
              + "<u><font size = 5>Mouse:</font></u><br>"
              + "<font size=4>Left Mouse Button  </font> = <u>Rotate</u><br>"
              + "<font size=4>Right Mouse Button </font> = <u>Translate</u><br>"
              + "<font size=4>Middle Mouse Button</font> = <u>Zoom</u> (if available)<br>";
      Object[] options = {"Close"};
      JOptionPane pane =
          new JOptionPane(
              message, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options);

      JDialog dialog =
          pane.createDialog(Surface.this, "Usage Information - Mouse and Keyboard Shortcuts");
      dialog.setModal(false);
      askAgain.addActionListener(this);
      dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
      if (askAgain.isSelected() == true) Surface.reftomain.perkeysconfirm = true;
    }
  }

  // ---------------------------------------------------------------------------------------------------------------

  public BranchGroup createSceneGraph() {
    // Create the root of the branch graph
    BranchGroup objRoot = new BranchGroup();
    objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

    objRoot.addChild(objTransform);

    // add the shapes to the transormation obejct
    if (reftomain.gad.cpercoord.isSelected() == true) {
      objTransform.addChild(new CoordSysLines(reftomain));
      objTransform.addChild(new CoordSysText(reftomain));
    }
    SurfacePane sp = new SurfacePane(reftomain, this, false, isWv);
    if (sp.getGeometry() == null) {
      return (null);
    }
    objTransform.addChild(sp);
    SurfacePane spFlat = new SurfacePane(reftomain, this, true, isWv);
    if (sp.getGeometry() == null) {
      return (null);
    }
    objTransform.addChild(spFlat);

    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

    // add the background universe texture
    if (reftomain.gad.cuniverse.isSelected() == true) {
      Background bg = new Background();
      bg.setApplicationBounds(bounds);
      Sphere sphereObj =
          new Sphere(
              3.0f,
              Sphere.GENERATE_NORMALS
                  | Sphere.GENERATE_NORMALS_INWARD
                  | Sphere.GENERATE_TEXTURE_COORDS,
              40);
      Appearance backApp = sphereObj.getAppearance();
      TextureLoader tex =
          new TextureLoader(Sonogram.class.getResource("bg.jpg"), new String("RGB"), this);
      if (tex != null) backApp.setTexture(tex.getTexture());
      objTransform.addChild(bg);
      objTransform.addChild(sphereObj);
    }

    // add the background
    Color3f bgColor = new Color3f(reftomain.gad.bgcol);
    Background bgNode = new Background(bgColor);
    bgNode.setApplicationBounds(bounds);
    objRoot.addChild(bgNode);

    // move the position
    Transform3D tr = new Transform3D();
    Transform3D tgr = new Transform3D();
    Vector3f vec = new Vector3f(0.0f, 0.0f, 0.5f);
    tr.setTranslation(vec);
    objTransform.getTransform(tgr);
    tgr.mul(tr);
    objTransform.setTransform(tgr);

    // add the mouse and keyboard interaction
    MyKeyNavigatorBehavior keyNavBeh = new MyKeyNavigatorBehavior(objTransform, 0.1f, 1.0d);
    keyNavBeh.setSchedulingBounds(new BoundingSphere());
    objRoot.addChild(keyNavBeh);
    MouseRotate myMouseRotate = new MouseRotate();
    myMouseRotate.setSchedulingBounds(new BoundingSphere());
    myMouseRotate.setTransformGroup(objTransform);
    objRoot.addChild(myMouseRotate);
    MouseTranslate myMouseTranslate = new MouseTranslate();
    myMouseTranslate.setTransformGroup(objTransform);
    myMouseTranslate.setSchedulingBounds(
        new BoundingSphere(new BoundingSphere(new Point3d(0, 0, 0), 400.0)));
    objRoot.addChild(myMouseTranslate);
    MouseZoom myMouseZoom = new MouseZoom();
    myMouseZoom.setTransformGroup(objTransform);
    myMouseZoom.setSchedulingBounds(
        new BoundingSphere(new BoundingSphere(new Point3d(0, 0, 0), 400.0)));
    objRoot.addChild(myMouseZoom);
    addMouseListener(
        new MouseInputAdapter() {
          public void mouseClicked(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseDragged(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseEntered(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseMoved(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mousePressed(MouseEvent e) {
            rotateThread.resetTime();
          }

          public void mouseReleased(MouseEvent e) {
            rotateThread.resetTime();
          }
        });

    // Let Java 3D perform optimizations on this scene graph.
    objRoot.compile();

    // create the screen saver thread
    rotateThread = new SceneRotatorThread();

    return objRoot;
  }
  // --------------------------------------------------------------------------------------------

  // the screen saver thread
  private class SceneRotatorThread extends Thread {
    private int time = 0;

    public SceneRotatorThread() {
      start();
    }

    public void resetTime() {
      time = 0;
    }

    public void run() {
      for (; ; ) {
        // sleep
        try {
          sleep(20);
        } catch (Exception e) {
        }
        // increase time counter
        time++;
        // rotate if some time nothing happend
        if (time > 500 && reftomain.gad.cscrennsaver.isSelected()) {
          Transform3D tr = new Transform3D();
          tr.rotZ(Math.toRadians(0.05));
          Transform3D tgr = new Transform3D();
          objTransform.getTransform(tgr);
          tgr.mul(tr);
          objTransform.setTransform(tgr);
        }
      }
    }
  }

  // -----------------------------------------------------------------------------------------------

  // my own implementation of the keyboard interaction
  private class MyKeyNavigatorBehavior extends Behavior {
    private Transform3D tr, tgr;
    private TransformGroup tg;
    private WakeupOnAWTEvent wup;
    private Vector3f vec;
    private float step;
    private double angle;

    public MyKeyNavigatorBehavior(TransformGroup targetTG, float moveStep, double rotStep) {
      super();
      this.tgr = new Transform3D();
      this.tg = targetTG;
      this.wup = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);
      this.step = moveStep;
      this.angle = rotStep;
    }

    public void initialize() {
      wakeupOn(wup);
      tg.getTransform(tgr);
    }

    public void processStimulus(java.util.Enumeration criteria) {
      KeyEvent event = (KeyEvent) (wup.getAWTEvent())[0];
      int keyCode = event.getKeyCode();

      if (keyCode == KeyEvent.VK_UP
          || keyCode == KeyEvent.VK_DOWN
          || keyCode == KeyEvent.VK_LEFT
          || keyCode == KeyEvent.VK_RIGHT
          || keyCode == KeyEvent.VK_PAGE_DOWN
          || keyCode == KeyEvent.VK_PAGE_UP) {
        switch (keyCode) {
          case KeyEvent.VK_UP:
            tr = new Transform3D();
            tr.rotX(-Math.toRadians(angle));
            tg.getTransform(tgr);
            tgr.mul(tr);
            tg.setTransform(tgr);
            wakeupOn(wup);
            return;
          case KeyEvent.VK_DOWN:
            tr = new Transform3D();
            tr.rotX(Math.toRadians(angle));
            tg.getTransform(tgr);
            tgr.mul(tr);
            tg.setTransform(tgr);
            wakeupOn(wup);
            return;
          case KeyEvent.VK_LEFT:
            tr = new Transform3D();
            tr.rotZ(-Math.toRadians(angle));
            tg.getTransform(tgr);
            tgr.mul(tr);
            tg.setTransform(tgr);
            wakeupOn(wup);
            return;
          case KeyEvent.VK_RIGHT:
            tr = new Transform3D();
            tr.rotZ(Math.toRadians(angle));
            tg.getTransform(tgr);
            tgr.mul(tr);
            tg.setTransform(tgr);
            wakeupOn(wup);
            return;
          case KeyEvent.VK_PAGE_UP:
            tr = new Transform3D();
            vec = new Vector3f(0.0f, 0.0f, step);
            tr.setTranslation(vec);
            tg.getTransform(tgr);
            tgr.mul(tr);
            tg.setTransform(tgr);
            wakeupOn(wup);
            return;
          case KeyEvent.VK_PAGE_DOWN:
            tr = new Transform3D();
            vec = new Vector3f(0.0f, 0.0f, -step);
            tr.setTranslation(vec);
            tg.getTransform(tgr);
            tgr.mul(tr);
            tg.setTransform(tgr);
            wakeupOn(wup);
            return;
        }
      } else {
        wakeupOn(wup);
      }
    } // end function processStimulus
  } // end inner class MyKeyNavigatorBehavior
} // end class Surface
