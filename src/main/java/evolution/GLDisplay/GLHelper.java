package evolution.GLDisplay;

import com.jogamp.opengl.GL2;

public class GLHelper {
    private static final int CIRCLE_VERTEX_COUNT = 36;

    public static void setColor(GL2 gl, double r, double g, double b, double a) {
        gl.glColor4d(r, g, b, a);
    }

    public static void setColor(GL2 gl, double r, double g, double b) {
        setColor(gl, r, g, b, 1.0);
    }

    public static void drawLine(GL2 gl, double x, double y, double w, double h) {
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + w, y + h);
        gl.glEnd();
    }

    public static void drawRect(GL2 gl, double x, double y, double w, double h, boolean fill) {
        if (fill) {
            gl.glBegin(GL2.GL_QUADS);
        } else {
            gl.glBegin(GL2.GL_LINE_LOOP);
        }
        gl.glVertex2d(x, y);
        gl.glVertex2d(x + w, y);
        gl.glVertex2d(x + w, y + h);
        gl.glVertex2d(x, y + h);
        gl.glEnd();
    }

    public static void drawCircle(GL2 gl, double x, double y, double r, boolean fill) {
        if (fill) {
            gl.glBegin(GL2.GL_TRIANGLE_FAN);
            gl.glVertex2d(x, y);
        } else {
            gl.glBegin(GL2.GL_LINE_LOOP);
        }
        for (int i = 0; i < CIRCLE_VERTEX_COUNT; i++) {
            gl.glVertex2d(x + r * Math.cos(2 * Math.PI / CIRCLE_VERTEX_COUNT * i),
                    y + r * Math.sin(2 * Math.PI / CIRCLE_VERTEX_COUNT * i));
        }
        gl.glEnd();
    }

    public static void drawString(GL2 gl, double x, double y, double size, String s, boolean scale) {

    }
}