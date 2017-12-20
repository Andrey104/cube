package evolution.GLDisplay;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class GLDisplay {

    private JFrame frame;
    private GLCanvas canvas;
    private JPanel side;
    private JTextArea bottom;

    public GLDisplay(String title, int width, int height, CoordSystem coordSystem, Draw draw) {
        this.frame = new JFrame(title);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas = new GLCanvas();
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.addGLEventListener(new GLEL(width, height, draw, coordSystem.value));
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        final Animator animator = new Animator(canvas);
        animator.setRunAsFastAsPossible(true);
        animator.start();
    }

    public JPanel enableSide(int width, int height) {
        side = new JPanel();
        side.setPreferredSize(new Dimension(width, height));
        frame.add(side, BorderLayout.EAST);
        frame.pack();
        return side;
    }

    public JTextArea enableBottom(int width, int height) {
        bottom = new JTextArea();
        bottom.setPreferredSize(new Dimension(width, height));
        frame.add(bottom, BorderLayout.SOUTH);
        frame.pack();
        return bottom;
    }

    public void addKeyListener(KeyListener listener) {
        canvas.addKeyListener(listener);
    }

    public interface Draw {
        void init(GL2 gl);

        void display(GL2 gl);
    }

    public enum CoordSystem {
        CENTER(true), CORNER(false);

        public final boolean value;

        CoordSystem(boolean value) {
            this.value = value;
        }
    }

    private static final class GLEL implements GLEventListener {
        private int width;
        private int height;
        private Draw draw;

        private boolean coordSystem;

        private GLEL(int width, int height, Draw draw, boolean coordSystem) {
            this.width = width;
            this.height = height;
            this.draw = draw;
            this.coordSystem = coordSystem;
        }

        @Override
        public void init(GLAutoDrawable glAutoDrawable) {
            final GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glClearColor(1, 1, 1, 1);
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_NICEST);
            gl.glHint(GL2.GL_POLYGON_SMOOTH_HINT, GL2.GL_NICEST);
            gl.glEnable(GL2.GL_LINE_SMOOTH);
            gl.glEnable(GL2.GL_POLYGON_SMOOTH);
            gl.glEnable(GL2.GL_MULTISAMPLE);
/*            gl.glMatrixMode(GL2.GL_TEXTURE);
            gl.glScaled(1.0 / 16.0, 1.0 / 16.0, 1.0);
            gl.glMatrixMode(GL2.GL_MODELVIEW);*/
            draw.init(gl);
        }

        @Override
        public void dispose(GLAutoDrawable glAutoDrawable) {
        }

        @Override
        public void display(GLAutoDrawable glAutoDrawable) {
            final GL2 gl = glAutoDrawable.getGL().getGL2();
            draw.display(gl);
        }

        @Override
        public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int w, int h) {
            final GL2 gl = glAutoDrawable.getGL().getGL2();
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();
            if (coordSystem) {
                gl.glOrtho(-w / 2, w / 2, -h / 2, h / 2, -1, 1);
            } else {
                gl.glOrtho(0.0D, w, 0.0D, h, -1.0D, 1.0D);
            }
            gl.glMatrixMode(GL2.GL_MODELVIEW);
        }
    }
}
