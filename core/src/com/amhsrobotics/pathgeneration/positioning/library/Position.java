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

package com.amhsrobotics.pathgeneration.positioning.library;

/**
 * Represents a 2d <code>(x,y)</code> position on a standard cartesian coordinate plane.
 * <p>
 * Inspired by team 254's geometry system: https://github.com/Team254/FRC-2019-Public/blob/master/src/main/java/com/team254/lib/geometry/
 */
public class Position {
    private double x;
    private double y;

    public Position() {
        this(0, 0);
    }

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the distance from this {@link Position} to <code>other</code>.
     *
     * @param other the {@link Position} to find the distance to
     * @return the distance from this {@link Position} to <code>other</code>.
     */
    public double distance(Position other) {
        return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2));
    }

    /**
     * Converts this {@link Position} from inches to meters.
     *
     * @return this {@link Position} from inches to meters.
     */
    public Position inToM() {
        return multiply(Conversions.IN_TO_M);
    }

    /**
     * Converts this {@link Position} from meters to inches.
     *
     * @return this {@link Position} from meters to inches.
     */
    public Position mToIn() {
        return multiply(Conversions.M_TO_IN);
    }

    /**
     * Calculates the angle from this {@link Position} to other.
     *
     * @param other the {@link Position} to get the angle to
     * @return {@link Rotation} from this {@link Position} to <code>other</code> in degrees
     */
    public Rotation angleTo(Position other) {
        double x = getX() - other.getX();
        double y = getY() - other.getY();
        double angleToPoint = 180 + Math.toDegrees(Math.atan2(y, x));
        return new Rotation(angleToPoint);
    }

    /**
     * Rotates this {@link Position} by {@link Rotation} <code>rotation</code> around the origin (0,0) of a standard
     * cartesian coordinate plane.
     *
     * @param rotation the {@link Rotation} to rotate this {@link Position} by
     * @return a new {@link Position} containing the rotated coordinates
     */
    public Position rotateBy(Rotation rotation) {
        return new Position(x * rotation.cos() - y * rotation.sin(), x * rotation.sin() + y * rotation.cos());
    }

    /**
     * Adds <code>other</code> to this {@link Position}.
     *
     * @param other the other {@link Position} to add to this
     * @return a new {@link Position} with this and <code>other</code> added together
     */
    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    /**
     * Subtracts <code>other</code> from this {@link Position}.
     *
     * @param other the other {@link Position} to subtract this by
     * @return a new {@link Position} with this subtracted by <code>other</code>
     */
    public Position subtract(Position other) {
        return new Position(x - other.x, y - other.y);
    }

    /**
     * Multiplies the x and y values of this {@link Position} by the <code>scalar</code>.
     *
     * @param scalar The amount to multiply by
     * @return a new {@link Position} multiplied by <code>scalar</code>.
     */
    public Position multiply(double scalar) {
        return new Position(x * scalar, y * scalar);
    }

    /**
     * Divides the x and y values of this {@link Position} by the <code>scalar</code>.
     *
     * @param scalar The amount to divide by
     * @return a new {@link Position} divided by <code>scalar</code>.
     */
    public Position divide(double scalar) {
        return new Position(x / scalar, y / scalar);
    }

    /**
     * Returns the inverse of this {@link Position}
     *
     * @return a new {@link Position} with the opposite x and y of this {@link Position}
     */
    public Position inverse() {
        return new Position(-x, -y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("Position(%s, %s)", x, y);
    }
}
