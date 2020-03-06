/*
 * MIT License
 *
 * Copyright (c) 2019 Mitty Robotics (Team 1351)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.amhsrobotics.pathgeneration.parametrics.splines;


import com.amhsrobotics.pathgeneration.parametrics.abstractions.Parametric;
import com.amhsrobotics.pathgeneration.positioning.library.Position;
import com.amhsrobotics.pathgeneration.positioning.library.Rotation;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.amhsrobotics.pathgeneration.positioning.library.TransformWithVelocity;
import com.badlogic.gdx.math.Vector2;

public class CubicHermiteSpline extends Parametric {
    private double x0, x1, y0, y1, vx0, vx1, vy0, vy1;

    /**
     * Constructs a {@link CubicHermiteSpline} given the start and end {@link Transform}s.
     *
     * @param startWaypoint the {@link Transform} to start the spline.
     * @param endWaypoint   the {@link Transform} to end the spline.
     */
    public CubicHermiteSpline(Transform startWaypoint, Transform endWaypoint) {
        initSpline(new TransformWithVelocity(startWaypoint, 0), new TransformWithVelocity(endWaypoint, 0));
    }

    /**
     * Constructs a {@link CubicHermiteSpline} given the start and end {@link Transform}s as well as the magnitude of
     * the start and end tangent vectors.
     *
     * @param startWaypoint the {@link Transform} to start the spline.
     * @param endWaypoint   the {@link Transform} to end the spline.
     * @param m1            the magnitude of the start tangent vector.
     * @param m2            the magnitude of the end tangent vector.
     */
    public CubicHermiteSpline(Transform startWaypoint, Transform endWaypoint, double m1, double m2) {
        initSpline(new TransformWithVelocity(startWaypoint, m1), new TransformWithVelocity(endWaypoint, m2));
    }

    /**
     * Constructs a {@link CubicHermiteSpline} given the start and end {@link TransformWithVelocity}s containing a
     * {@link Transform} and velocity vector magnitude
     *
     * @param startWaypoint the {@link TransformWithVelocity} to start the spline containing a {@link Transform} and
     *                      velocity vector magnitude.
     * @param endWaypoint   the {@link TransformWithVelocity} to end the spline containing a {@link Transform} and
     *                      velocity vector magnitude.
     */
    public CubicHermiteSpline(TransformWithVelocity startWaypoint, TransformWithVelocity endWaypoint) {
        initSpline(startWaypoint, endWaypoint);
    }

    private void initSpline(TransformWithVelocity startWaypoint, TransformWithVelocity endWaypoint) {
        x0 = startWaypoint.getPosition().getX();
        x1 = endWaypoint.getPosition().getX();
        y0 = startWaypoint.getPosition().getY();
        y1 = endWaypoint.getPosition().getY();

        //Get angles in radians
        double heading0 = Math.toRadians(startWaypoint.getRotation().getHeading());
        double heading1 = Math.toRadians(endWaypoint.getRotation().getHeading());

        double startMagnitude = startWaypoint.getVelocity();
        double endMagnitude = endWaypoint.getVelocity();

        double d = startWaypoint.getPosition().distance(endWaypoint.getPosition());

        if (startMagnitude == 0) {
            startMagnitude = d;
        }
        if (endMagnitude == 0) {
            endMagnitude = d;
        }

        //Create tangent vectors proportional to the distance between points
        vx0 = Math.cos(heading0) * startMagnitude;
        vy0 = Math.sin(heading0) * startMagnitude;
        vx1 = Math.cos(heading1) * endMagnitude;
        vy1 = Math.sin(heading1) * endMagnitude;
    }

    /**
     * Returns the {@link Position} along the {@link Parametric} at <code>t</code> where <code>0 <= t <= 1</code>.
     *
     * @param t the parameter
     * @return the {@link Position} at the parameter <code>t</code>.
     */
    @Override
    public Position getPosition(double t) {
        //Cubic hermite spline equations https://rose-hulman.edu/~finn/CCLI/Notes/day09.pdf#page=2
        double h0, h1, h2, h3;
        h0 = 1 - 3 * t * t + 2 * t * t * t;
        h1 = t - 2 * t * t + t * t * t;
        h2 = -t * t + t * t * t;
        h3 = 3 * t * t - 2 * t * t * t;

        return computeFromCoefficients(h0, h1, h2, h3);
    }

    /**
     * Returns the {@link Transform} along the {@link Parametric} at <code>t</code> where <code>0 <= t <= 1</code>.
     * <p>
     * The {@link Transform} contains the {@link Position} and {@link Rotation}, with the {@link Rotation} being the
     * tangent angle at the {@link Position}.
     *
     * @param t the parameter
     * @return the {@link Transform} at the parameter <code>t</code>.
     */
    @Override
    public Transform getTransform(double t) {
        Position position = getPosition(t);
        Position firstDerivative = getFirstDerivative(t);
        Rotation rotation = new Rotation(Math.toDegrees(Math.atan2(firstDerivative.getY(), firstDerivative.getX())));

        return new Transform(position, rotation);
    }

    /**
     * Returns the curvature at point <code>t</code> on the {@link Parametric}.
     *
     * @param t the parameter
     * @return the curvature at the parameter <code>t</code>.
     */
    @Override
    public double getCurvature(double t) {
        Position firstDerivative = getFirstDerivative(t);
        Position secondDerivative = getSecondDerivative(t);

        return (firstDerivative.getX() * secondDerivative.getY() - secondDerivative.getX() * firstDerivative.getY()) /
                Math.sqrt(Math.pow(firstDerivative.getX() * firstDerivative.getX() +
                        firstDerivative.getY() * firstDerivative.getY(), 3));
    }

    /**
     * Returns the first derivative of the {@link Parametric} in the form of a {@link Position} containing the x and
     * y value of the first derivative at the parameter <code>t</code>.
     *
     * @param t the parameter
     * @return the first derivative {@link Position} at the parameter <code>t</code>.
     */
    @Override
    public Position getFirstDerivative(double t) {
        //First derivative of cubic hermite spline functions
        double h0, h1, h2, h3;
        h0 = 6 * t * t - 6 * t;
        h1 = 3 * t * t - 4 * t + 1;
        h2 = 3 * t * t - 2 * t;
        h3 = -6 * t * t + 6 * t;

        return computeFromCoefficients(h0, h1, h2, h3);
    }

    /**
     * Returns the second derivative of the {@link Parametric} in the form of a {@link Position} containing the x and
     * y value of the second derivative at the parameter <code>t</code>.
     *
     * @param t the parameter
     * @return the second derivative {@link Position} at the parameter <code>t</code>.
     */
    @Override
    public Position getSecondDerivative(double t) {
        //Second derivative of cubic hermite spline functions
        double h0, h1, h2, h3;
        h0 = 12 * t - 6;
        h1 = 6 * t - 4;
        h2 = 6 * t - 2;
        h3 = 6 - 12 * t;

        return computeFromCoefficients(h0, h1, h2, h3);
    }

    /**
     * Computes the {@link Position} from the 4 base coefficients.
     *
     * @param h0 base coefficient 1
     * @param h1 base coefficient 2
     * @param h2 base coefficient 3
     * @param h3 base coefficient 4
     * @return the {@link Position} containing the x and y values computed from the coefficients.
     */
    private Position computeFromCoefficients(double h0, double h1, double h2, double h3) {
        double x = h0 * x0 + h1 * vx0 + h2 * vx1 + h3 * x1;
        double y = h0 * y0 + h1 * vy0 + h2 * vy1 + h3 * y1;

        return new Position(x, y);
    }

    public Vector2 getStartPosition() {
        return new Vector2((float) x0, (float) y0);
    }

    public void setStartPosition(float x0, float y0) {
        this.x0 = x0;
        this.y0 = y0;
    }

    public Vector2 getTargetPosition() {
        return new Vector2((float) x1, (float) y1);
    }

    public void setTargetPosition(float x1, float y1) {
        this.x1 = x1;
        this.y1 = y1;
    }

    public Vector2 getDer1Position() {
        return new Vector2((float) vx0, (float) vy0);
    }

    public void setDer1Position(float vx0, float vy0) {
        this.vx0 = vx0;
        this.vy0 = vy0;
    }

    public Vector2 getDer2Position() {
        return new Vector2((float) vx1, (float) vy1);
    }

    public void setDer2Position(float vx1, float vy1) {
        this.vx1 = vx1;
        this.vy1 = vy1;
    }

}
