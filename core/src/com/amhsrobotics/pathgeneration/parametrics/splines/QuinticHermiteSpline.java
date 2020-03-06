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
import com.amhsrobotics.pathgeneration.positioning.library.*;
import com.badlogic.gdx.math.Vector2;

/**
 * Quintic Hermite Spline class
 * <p>
 * Reference: https://rose-hulman.edu/~finn/CCLI/Notes/day09.pdf
 * <p>
 * Desmos graph of spline: https://www.desmos.com/calculator/g8sls8d7dm
 */
public class QuinticHermiteSpline extends Parametric {
    private double x0, x1, y0, y1, vx0, vx1, vy0, vy1, ax0, ax1, ay0, ay1;

    /**
     * Constructs a {@link QuinticHermiteSpline} given a start and end {@link Transform}.
     *
     * @param startWaypoint the starting {@link Transform} of the {@link QuinticHermiteSpline}.
     * @param endWaypoint   the ending {@link Transform} of the {@link QuinticHermiteSpline}.
     */
    public QuinticHermiteSpline(Transform startWaypoint, Transform endWaypoint) {
        initSpline(startWaypoint, endWaypoint);
    }

    /**
     * Constructs a {@link QuinticHermiteSpline} given a start and end {@link TransformWithCurvature}.
     *
     * @param startWaypoint the starting {@link TransformWithCurvature} of the {@link QuinticHermiteSpline}.
     * @param endWaypoint   the ending {@link TransformWithCurvature} of the {@link QuinticHermiteSpline}.
     */
    public QuinticHermiteSpline(TransformWithCurvature startWaypoint,
                                TransformWithCurvature endWaypoint) {
        initSpline(new TransformWithVelocityAndCurvature(startWaypoint, 0, startWaypoint.getCurvature()),
                new TransformWithVelocityAndCurvature(endWaypoint, 0, endWaypoint.getCurvature()));
    }

    /**
     * Constructs a {@link QuinticHermiteSpline} given a start and end {@link TransformWithVelocity}.
     *
     * @param startWaypoint the starting {@link TransformWithVelocity} of the {@link QuinticHermiteSpline}.
     * @param endWaypoint   the ending {@link TransformWithVelocity} of the {@link QuinticHermiteSpline}.
     */
    public QuinticHermiteSpline(TransformWithVelocity startWaypoint,
                                TransformWithVelocity endWaypoint) {
        initSpline(new TransformWithVelocityAndCurvature(startWaypoint, startWaypoint.getVelocity(), 0),
                new TransformWithVelocityAndCurvature(endWaypoint, endWaypoint.getVelocity(), 0));
    }

    /**
     * Constructs a {@link QuinticHermiteSpline} given a start and end {@link TransformWithVelocityAndCurvature}.
     *
     * @param startWaypoint the starting {@link TransformWithVelocityAndCurvature} of the {@link QuinticHermiteSpline}.
     * @param endWaypoint   the ending {@link TransformWithVelocityAndCurvature} of the {@link QuinticHermiteSpline}.
     */
    public QuinticHermiteSpline(TransformWithVelocityAndCurvature startWaypoint,
                                TransformWithVelocityAndCurvature endWaypoint) {
        initSpline(startWaypoint, endWaypoint);
    }

    /**
     * Constructs a {@link QuinticHermiteSpline} given a start and end {@link Transform} and the coordinates of the
     * two acceleration vectors.
     *
     * @param startWaypoint the starting {@link Transform} of the {@link QuinticHermiteSpline}.
     * @param endWaypoint   the ending {@link Transform} of the {@link QuinticHermiteSpline}.
     * @param ax0           the x value of the first acceleration vector
     * @param ay0           the y value of the first acceleration vector
     * @param ax1           the x value of the second acceleration vector
     * @param ay1           the y value of the second acceleration vector
     */
    public QuinticHermiteSpline(Transform startWaypoint, Transform endWaypoint, double ax0, double ay0, double ax1,
                                double ay1) {
        initSpline(startWaypoint, endWaypoint, ax0, ay0, ax1, ay1);
    }

    /**
     * Constructs a {@link QuinticHermiteSpline} given a start and end {@link Position} and the coordinates of the
     * two velocity and acceleration vectors.
     *
     * @param startPosition the starting {@link Position} of the {@link QuinticHermiteSpline}.
     * @param endPosition   the ending {@link Position} of the {@link QuinticHermiteSpline}.
     * @param vx0           the x value of the first velocity vector
     * @param vy0           the y value of the first velocity vector
     * @param vx1           the x value of the second velocity vector
     * @param vy1           the y value of the second velocity vector
     * @param ax0           the x value of the first acceleration vector
     * @param ay0           the y value of the first acceleration vector
     * @param ax1           the x value of the second acceleration vector
     * @param ay1           the y value of the second acceleration vector
     */
    public QuinticHermiteSpline(Position startPosition, Position endPosition, double vx0, double vy0,
                                double vx1, double vy1, double ax0,
                                double ay0, double ax1,
                                double ay1) {
        initSpline(startPosition, endPosition, vx0, vy0, vx1, vy1, ax0, ay0, ax1, ay1);
    }

    /**
     * Constructs a {@link QuinticHermiteSpline} given the coordinates of the two velocity and acceleration vectors
     * as well as the start and end point coordinates.
     *
     * @param x0  the x value of the first position
     * @param y0  the y value of the first position
     * @param x1  the x value of the second position
     * @param y1  the y value of the second position
     * @param vx0 the x value of the first velocity vector
     * @param vy0 the y value of the first velocity vector
     * @param vx1 the x value of the second velocity vector
     * @param vy1 the y value of the second velocity vector
     * @param ax0 the x value of the first acceleration vector
     * @param ay0 the y value of the first acceleration vector
     * @param ax1 the x value of the second acceleration vector
     * @param ay1 the y value of the second acceleration vector
     */
    public QuinticHermiteSpline(double x0, double y0, double x1, double y1, double vx0, double vy0,
                                double vx1, double vy1, double ax0,
                                double ay0, double ax1,
                                double ay1) {
        initSpline(x0, y0, x1, y1, vx0, vy0, vx1, vy1, ax0, ay0, ax1, ay1);
    }


    /**
     * Initializes the {@link QuinticHermiteSpline} given a start and end {@link Transform}.
     * <p>
     * The velocity vector coordinates of this {@link QuinticHermiteSpline} will be calculated from of the heading of
     * the {@link Transform} waypoints and the magnitude will be proportional to the distance between the waypoints.
     * The acceleration vector coordinates will therefore be zero.
     *
     * @param startWaypoint the starting {@link Transform} of the {@link QuinticHermiteSpline}.
     * @param endWaypoint   the ending {@link Transform} of the {@link QuinticHermiteSpline}.
     */
    private void initSpline(Transform startWaypoint, Transform endWaypoint) {
        double velocityScale = getDefaultVelocityMagnitude(startWaypoint.getPosition(), endWaypoint.getPosition());
        initSpline(startWaypoint.getPosition(), endWaypoint.getPosition(),
                Math.cos(startWaypoint.getRotation().getRadians()) * velocityScale,
                Math.sin(startWaypoint.getRotation().getRadians()) * velocityScale,
                Math.cos(endWaypoint.getRotation().getRadians()) * velocityScale,
                Math.sin(endWaypoint.getRotation().getRadians()) * velocityScale,
                0, 0, 0, 0);
    }

    /**
     * Initializes the {@link QuinticHermiteSpline} given a start and end {@link TransformWithVelocityAndCurvature}.
     * <p>
     * The velocity vector coordinates of this {@link QuinticHermiteSpline} will be calculated from of the heading of
     * the {@link Transform} waypoints and the magnitude will be the velocity value of each
     * {@link TransformWithVelocityAndCurvature}. The acceleration vector coordinates will also be calculated from the
     * heading of the {@link Transform} and the magnitude will be the curvature value of each
     * {@link TransformWithVelocityAndCurvature}.
     *
     * @param startWaypoint the starting {@link TransformWithVelocityAndCurvature} of the {@link QuinticHermiteSpline}.
     * @param endWaypoint   the ending {@link TransformWithVelocityAndCurvature} of the {@link QuinticHermiteSpline}.
     */
    private void initSpline(TransformWithVelocityAndCurvature startWaypoint,
                            TransformWithVelocityAndCurvature endWaypoint) {
        double d = startWaypoint.getPosition().distance(endWaypoint.getPosition());
        double velocityScale = getDefaultVelocityMagnitude(startWaypoint.getPosition(), endWaypoint.getPosition());
        double a0 = getAccelerationMagnitudeFromCurvature(startWaypoint.getCurvature(), d);
        double a1 = getAccelerationMagnitudeFromCurvature(endWaypoint.getCurvature(), d);
        double v0 = startWaypoint.getVelocity();
        double v1 = endWaypoint.getVelocity();
        if (v0 == 0) {
            v0 = velocityScale;
        }
        if (v1 == 0) {
            v1 = velocityScale;
        }
        initSpline(startWaypoint.getPosition(), endWaypoint.getPosition(),
                Math.cos(startWaypoint.getRotation().getRadians()) * v0,
                Math.sin(startWaypoint.getRotation().getRadians()) * v0,
                Math.cos(endWaypoint.getRotation().getRadians()) * v1,
                Math.sin(endWaypoint.getRotation().getRadians()) * v1,
                Math.sin(startWaypoint.getRotation().getRadians()) * a0,
                Math.cos(startWaypoint.getRotation().getRadians()) * a0,
                Math.sin(endWaypoint.getRotation().getRadians()) * a1,
                Math.cos(endWaypoint.getRotation().getRadians()) * a1);
    }

    /**
     * Initializes the {@link QuinticHermiteSpline} given a start and end {@link Transform} and the coordinates of the
     * two acceleration vectors.
     * <p>
     * The velocity vector coordinates of this {@link QuinticHermiteSpline} will be calculated from of the heading of
     * the {@link Transform} waypoints and the magnitude will be proportional to the distance between the waypoints.
     * The acceleration vector will be determined by the input acceleration vector coordinates.
     *
     * @param startWaypoint the starting {@link Transform} of the {@link QuinticHermiteSpline}.
     * @param endWaypoint   the ending {@link Transform} of the {@link QuinticHermiteSpline}.
     * @param ax0           the x value of the first acceleration vector
     * @param ay0           the y value of the first acceleration vector
     * @param ax1           the x value of the second acceleration vector
     * @param ay1           the y value of the second acceleration vector
     */
    private void initSpline(Transform startWaypoint, Transform endWaypoint, double ax0, double ay0, double ax1,
                            double ay1) {
        double velocityScale = getDefaultVelocityMagnitude(startWaypoint.getPosition(), endWaypoint.getPosition());
        initSpline(startWaypoint.getPosition(), endWaypoint.getPosition(),
                Math.cos(startWaypoint.getRotation().getRadians()) * velocityScale,
                Math.sin(startWaypoint.getRotation().getRadians()) * velocityScale,
                Math.cos(endWaypoint.getRotation().getRadians()) * velocityScale,
                Math.sin(endWaypoint.getRotation().getRadians()) * velocityScale,
                ax0, ay0, ax1, ay1);
    }

    /**
     * Initializes the {@link QuinticHermiteSpline} given a start and end {@link Position} and the coordinates of the
     * two velocity and acceleration vectors.
     * <p>
     * The velocity vector coordinates of this {@link QuinticHermiteSpline} will be determined by the input velocity
     * vector coordinates. The acceleration vector will be determined by the input acceleration vector coordinates.
     *
     * @param startPosition the starting {@link Position} of the {@link QuinticHermiteSpline}.
     * @param endPosition   the ending {@link Position} of the {@link QuinticHermiteSpline}.
     * @param vx0           the x value of the first velocity vector
     * @param vy0           the y value of the first velocity vector
     * @param vx1           the x value of the second velocity vector
     * @param vy1           the y value of the second velocity vector
     * @param ax0           the x value of the first acceleration vector
     * @param ay0           the y value of the first acceleration vector
     * @param ax1           the x value of the second acceleration vector
     * @param ay1           the y value of the second acceleration vector
     */
    private void initSpline(Position startPosition, Position endPosition, double vx0, double vy0,
                            double vx1, double vy1, double ax0, double ay0,
                            double ax1,
                            double ay1) {
        initSpline(startPosition.getX(), startPosition.getY(),
                endPosition.getX(), endPosition.getY(), vx0, vy0, vx1, vy1, ax0, ay0, ax1,
                ay1);
    }

    /**
     * Initializes the {@link QuinticHermiteSpline} given the coordinates of the two velocity and acceleration
     * vectors as well as the start and end point coordinates.
     * <p>
     * The velocity vector coordinates of this {@link QuinticHermiteSpline} will be determined by the input velocity
     * vector coordinates. The acceleration vector will be determined by the input acceleration vector coordinates.
     *
     * @param x0  the x value of the first position
     * @param y0  the y value of the first position
     * @param x1  the x value of the second position
     * @param y1  the y value of the second position
     * @param vx0 the x value of the first velocity vector
     * @param vy0 the y value of the first velocity vector
     * @param vx1 the x value of the second velocity vector
     * @param vy1 the y value of the second velocity vector
     * @param ax0 the x value of the first acceleration vector
     * @param ay0 the y value of the first acceleration vector
     * @param ax1 the x value of the second acceleration vector
     * @param ay1 the y value of the second acceleration vector
     */
    private void initSpline(double x0, double y0, double x1, double y1, double vx0, double vy0,
                            double vx1, double vy1, double ax0, double ay0,
                            double ax1,
                            double ay1) {
        //Init start and end points
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;

        //Init tangent vectors
        this.vx0 = vx0;
        this.vy0 = vy0;
        this.vx1 = vx1;
        this.vy1 = vy1;

        //Init acceleration vectors
        this.ax0 = ax0;
        this.ay0 = ay0;
        this.ax1 = ax1;
        this.ay1 = ay1;
    }

    /**
     * Returns the default velocity magnitude for any waypoint passed into the {@link QuinticHermiteSpline} without a
     * given velocity magnitude.
     *
     * @param p1 the first {@link Position} of the {@link QuinticHermiteSpline}
     * @param p2 the last {@link Position} of the {@link QuinticHermiteSpline}
     * @return the default velocity magnitude for any waypoint passed into the {@link QuinticHermiteSpline}
     */
    public double getDefaultVelocityMagnitude(Position p1, Position p2) {
        return p1.distance(p2);
    }

    /**
     * Returns the magnitude of the acceleration vector for a waypoint passed into the {@link QuinticHermiteSpline}
     * to achieve the <code>desiredCurvature</code> curvature value at that waypoint.
     *
     * @param desiredCurvature      the desired curvature of the waypoint
     * @param distanceBetweenPoints the distance between waypoints passed into the {@link QuinticHermiteSpline}
     * @return the magnitude of the acceleration vector for the waypoint to achieve the desired curvature value
     */
    public double getAccelerationMagnitudeFromCurvature(double desiredCurvature, double distanceBetweenPoints) {
        return desiredCurvature * (distanceBetweenPoints * distanceBetweenPoints);
    }

    /**
     * Returns the {@link Position} along the {@link Parametric} at <code>t</code> where <code>0 <= t <= 1</code>.
     *
     * @param t the parameter
     * @return the {@link Position} at the parameter <code>t</code>.
     */
    @Override
    public Position getPosition(double t) {
        //Quintic hermite spline equations https://rose-hulman.edu/~finn/CCLI/Notes/day09.pdf#page=4
        double h0 = -6 * t * t * t * t * t + 15 * t * t * t * t - 10 * t * t * t + 1;
        double h1 = -3 * t * t * t * t * t + 8 * t * t * t * t - 6 * t * t * t + t;
        double h2 = -(t * t * t * t * t) / 2 + (3 * t * t * t * t) / 2 - (3 * t * t * t) / 2 + (t * t) / 2;
        double h3 = (t * t * t * t * t) / 2 - t * t * t * t + (t * t * t) / 2;
        double h4 = -3 * t * t * t * t * t + 7 * t * t * t * t - 4 * t * t * t;
        double h5 = 6 * t * t * t * t * t - 15 * t * t * t * t + 10 * t * t * t;

        return computeFromCoefficients(h0, h1, h2, h3, h4, h5);
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
     * Returns the derivative of the curvature at point <code>t</code> on the {@link Parametric}.
     *
     * @param t the parameter
     * @return the derivative of the curvature at the parameter <code>t</code>.
     */
    public double getCurvatureDerivative(double t) {
        Position firstDerivative = getFirstDerivative(t);
        Position secondDerivative = getSecondDerivative(t);
        Position thirdDerivative = getThirdDerivative(t);

        double g = firstDerivative.getX();
        double j = secondDerivative.getY();
        double k = secondDerivative.getX();
        double l = firstDerivative.getY();
        double gd = secondDerivative.getX();
        double jd = thirdDerivative.getY();
        double kd = thirdDerivative.getX();
        double ld = secondDerivative.getY();

        double eqn1 = (j * gd + g * jd - l * kd - k * ld) / Math.sqrt(Math.pow(g * g + l * l, 3));
        double eqn2 = (3.0 * (g * g + l * l) * (g * g + l * l) * (2.0 * g * gd + 2.0 * l * ld) * (g * j - k * l)) /
                (2.0 * Math.sqrt(Math.pow(Math.pow(g * g + l * l, 3), 3)));

        return eqn1 - eqn2;
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
        //First derivative of quintic hermite spline functions
        double h0 = -30 * t * t * t * t + 60 * t * t * t - 30 * t * t;
        double h1 = -15 * t * t * t * t + 32 * t * t * t - 18 * t * t + 1;
        double h2 = -(5 * t * t * t * t) / 2 + 6 * t * t * t - (9 * t * t) / 2 + t;
        double h3 = (5 * t * t * t * t) / 2 - (4 * t * t * t) + (3 * t * t) / 2;
        double h4 = -15 * t * t * t * t + 28 * t * t * t - 12 * t * t;
        double h5 = 30 * t * t * t * t - 60 * t * t * t + 30 * t * t;

        return computeFromCoefficients(h0, h1, h2, h3, h4, h5);
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
        //Second derivative of quintic hermite spline functions
        double h0 = -120 * t * t * t + 180 * t * t - 60 * t;
        double h1 = -60 * t * t * t + 96 * t * t - 36 * t;
        double h2 = -10 * t * t * t + 18 * t * t - 9 * t + 1;
        double h3 = t * (10 * t * t - 12 * t + 3);
        double h4 = -60 * t * t * t + 84 * t * t - 24 * t;
        double h5 = 120 * t * t * t - 180 * t * t + 60 * t;

        return computeFromCoefficients(h0, h1, h2, h3, h4, h5);
    }

    /**
     * Returns the third derivative of the {@link Parametric} in the form of a {@link Position} containing the x and
     * y value of the third derivative at the parameter <code>t</code>.
     *
     * @param t the parameter
     * @return the third derivative {@link Position} at the parameter <code>t</code>.
     */
    public Position getThirdDerivative(double t) {
        //Third derivative of quintic hermite spline functions
        double h0 = -360 * t * t * t + 360 * t - 60;
        double h1 = -180 * t * t + 192 * t - 36;
        double h2 = -30 * t * t + 36 * t - 9;
        double h3 = 30 * t * t - 24 * t + 3;
        double h4 = -180 * t * t + 168 * t - 24;
        double h5 = 360 * t * t - 360 * t + 60;

        return computeFromCoefficients(h0, h1, h2, h3, h4, h5);
    }

    /**
     * Computes the {@link Position} from the 6 base coefficients.
     *
     * @param h0 base coefficient 1
     * @param h1 base coefficient 2
     * @param h2 base coefficient 3
     * @param h3 base coefficient 4
     * @param h4 base coefficient 5
     * @param h5 base coefficient 6
     * @return the {@link Position} containing the x and y values computed from the coefficients.
     */
    private Position computeFromCoefficients(double h0, double h1, double h2, double h3, double h4, double h5) {
        double x = h0 * x0 + h1 * vx0 + h2 * ax0 + h3 * ax1 + h4 * vx1 + h5 * x1;
        double y = h0 * y0 + h1 * vy0 + h2 * ay0 + h3 * ay1 + h4 * vy1 + h5 * y1;
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

    public Vector2 getA1Position() {
        return new Vector2((float) ax0, (float) ay0);
    }

    public void setA1Position(float ax0, float ay0) {
        this.ax0 = ax0;
        this.ay0 = ay0;
    }

    public Vector2 getA2Position() {
        return new Vector2((float) ax1, (float) ay1);
    }

    public void setA2Position(float ax1, float ay1) {
        this.ax1 = ax1;
        this.ay1 = ay1;
    }

    public Vector2 getV1Position() {
        return new Vector2((float) vx0, (float) vy0);
    }

    public void setV1Position(float vx0, float vy0) {
        this.vx0 = vx0;
        this.vy0 = vy0;
    }

    public Vector2 getV2Position() {
        return new Vector2((float) vx1, (float) vy1);
    }

    public void setV2Position(float vx1, float vy1) {
        this.vx1 = vx1;
        this.vy1 = vy1;
    }

}