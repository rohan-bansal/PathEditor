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

package com.amhsrobotics.pathgeneration.parametrics.abstractions;


import com.amhsrobotics.pathgeneration.positioning.library.Position;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.amhsrobotics.pathgeneration.positioning.library.Rotation;

public abstract class Parametric {
    /**
     * Returns the {@link Position} along the {@link Parametric} at <code>t</code> where <code>0 <= t <= 1</code>.
     *
     * @param t the parameter
     * @return the {@link Position} at the parameter <code>t</code>.
     */
    public abstract Position getPosition(double t);

    /**
     * Returns the {@link Transform} along the {@link Parametric} at <code>t</code> where <code>0 <= t <= 1</code>.
     * <p>
     * The {@link Transform} contains the {@link Position} and {@link Rotation}, with the {@link Rotation} being the
     * tangent angle at the {@link Position}.
     *
     * @param t the parameter
     * @return the {@link Transform} at the parameter <code>t</code>.
     */
    public abstract Transform getTransform(double t);

    /**
     * Returns the curvature at point <code>t</code> on the {@link Parametric}.
     *
     * @param t the parameter
     * @return the curvature at the parameter <code>t</code>.
     */
    public abstract double getCurvature(double t);

    /**
     * Returns the first derivative of the {@link Parametric} in the form of a {@link Position} containing the x and
     * y value of the first derivative at the parameter <code>t</code>.
     *
     * @param t the parameter
     * @return the first derivative {@link Position} at the parameter <code>t</code>.
     */
    public abstract Position getFirstDerivative(double t);

    /**
     * Returns the second derivative of the {@link Parametric} in the form of a {@link Position} containing the x and
     * y value of the second derivative at the parameter <code>t</code>.
     *
     * @param t the parameter
     * @return the second derivative {@link Position} at the parameter <code>t</code>.
     */
    public abstract Position getSecondDerivative(double t);
}
