package com.amhsrobotics.pathgeneration.parametrics.libraries;


import com.amhsrobotics.pathgeneration.parametrics.abstractions.Parametric;
import com.amhsrobotics.pathgeneration.positioning.library.Position;
import com.amhsrobotics.pathgeneration.positioning.library.Rotation;
import com.amhsrobotics.pathgeneration.positioning.library.Transform;
import com.amhsrobotics.pathgeneration.positioning.library.TransformWithParameter;

import java.util.ArrayList;

public class Path extends Parametric {
    private Transform[] waypoints;
    private Parametric[] parametrics;

    public Path(Parametric[] parametrics) {
        this.parametrics = parametrics;
        initWaypoints();
    }

    public Path(Parametric parametric) {
        this.parametrics = new Parametric[]{parametric};
        initWaypoints();
    }

    private void initWaypoints() {
        waypoints = new Transform[parametrics.length * 2];
        int j = 0;
        for (int i = 0; i < parametrics.length; i++) {
            waypoints[j] = parametrics[i].getTransform(0);
            waypoints[j + 1] = parametrics[i].getTransform(1);
            j += 2;
        }
    }

    /**
     * Returns the {@link Position} along the {@link Parametric} at <code>t</code> where <code>0 <= t <= 1</code>.
     *
     * @param t the parameter
     * @return the {@link Position} at the parameter <code>t</code>.
     */
    @Override
    public Position getPosition(double t) {
        //Convert t from 0 to 1 to 0 to waypoints.length-1 so that the t value represents all parametric equation
        //segments of the total path.
        t = t * parametrics.length;

        for (int i = 0; i < parametrics.length; i++) {
            //Find which parametric equation segment that t falls in
            if (t >= i && t <= i + 1) {
                //return the Transform from the segment that it falls in
                return parametrics[i].getPosition(t - i);
            }
        }
        return new Position();
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
        //Convert t from 0 to 1 to 0 to waypoints.length-1 so that the t value represents all parametric equation
        //segments of the total path.
        t = t * parametrics.length;

        for (int i = 0; i < parametrics.length; i++) {
            //Find which parametric equation segment that t falls in
            if (t >= i && t <= i + 1) {
                //return the Transform from the segment that it falls in
                return parametrics[i].getTransform(t - i);
            }
        }
        return new Transform();
    }

    /**
     * Returns the curvature at point <code>t</code> on the {@link Parametric}.
     *
     * @param t the parameter
     * @return the curvature at the parameter <code>t</code>.
     */
    @Override
    public double getCurvature(double t) {
        //Convert t from 0 to 1 to 0 to waypoints.length-1 so that the t value represents all parametric equation
        //segments of the total path.
        t = t * parametrics.length;

        for (int i = 0; i < parametrics.length; i++) {
            //Find which parametric equation segment that t falls in
            if (t >= i && t <= i + 1) {
                //return the Transform from the segment that it falls in
                return parametrics[i].getCurvature(t - i);
            }
        }

        return 1 / 2e16;
    }

    @Override
    public Position getFirstDerivative(double t) {
        //Convert t from 0 to 1 to 0 to waypoints.length-1 so that the t value represents all parametric equation
        //segments of the total path.
        t = t * parametrics.length;

        for (int i = 0; i < parametrics.length; i++) {
            //Find which parametric equation segment that t falls in
            if (t >= i && t <= i + 1) {
                //return the Transform from the segment that it falls in
                return parametrics[i].getFirstDerivative(t - i);
            }
        }

        return new Position();
    }

    @Override
    public Position getSecondDerivative(double t) {
        //Convert t from 0 to 1 to 0 to waypoints.length-1 so that the t value represents all parametric equation
        //segments of the total path.
        t = t * parametrics.length;

        for (int i = 0; i < parametrics.length; i++) {
            //Find which parametric equation segment that t falls in
            if (t >= i && t <= i + 1) {
                //return the Transform from the segment that it falls in
                return parametrics[i].getSecondDerivative(t - i);
            }
        }

        return new Position();
    }

    /**
     * Finds the closest {@link TransformWithParameter} to the <code>referencePosition</code>.
     * <p>
     * The {@link TransformWithParameter} contains the {@link Transform} of the point as well as the <code>t</code> value of it
     * along the {@link Parametric}.
     * <p>
     * This function defaults to a <code>searchIncrement</code> of 10 and a <code>searches</code> of 3.
     * <p>
     * If the point is outside the start and end of the {@link Path}, either the start or end {@link Transform} will be
     * picked.
     *
     * @param referencePosition the {@link Position} to find the closest {@link TransformWithParameter} to.
     * @return the closest {@link TransformWithParameter} to the <code>referencePosition</code>.
     */
    public TransformWithParameter getClosestTransform(Position referencePosition) {
        return getClosestTransform(referencePosition, 0, true, 10, 3);
    }

    /**
     * Finds the closest {@link TransformWithParameter} to the <code>referencePosition</code> given a
     * <code>distanceShift</code>.
     * <p>
     * The {@link TransformWithParameter} contains the {@link Transform} of the point as well as the <code>t</code> value of it
     * along the {@link Parametric}.
     * <p>
     * This function defaults to a <code>searchIncrement</code> of 10 and a <code>searches</code> of 3.
     * <p>
     * If the point is outside the start and end of the {@link Path}, either the start or end {@link Transform} will be
     * picked. If the distance shifted point is off the {@link Path}, it will be interpolated on a line extending either
     * the start or end {@link Transform} of the {@link Path}.
     * <p>
     * Since there is a <code>distanceShift</code> in this search, it first finds the actual closest point to the
     * <code>referencePosition</code> and then performs the distance shifted search using the actual closest point as a
     * guide of whether or not the point is in front or behind the <code>referencePosition</code>.
     *
     * @param referencePosition the {@link Position} to find the closest {@link TransformWithParameter} to.
     * @param distanceShift     the distance away from the <code>referencePosition</code> the closest point should be.
     * @return the closest {@link TransformWithParameter} to the <code>referencePosition</code>.
     */
    public TransformWithParameter getClosestTransform(Position referencePosition, double distanceShift) {
        return getClosestTransform(referencePosition, distanceShift, true, 10, 3);
    }

    /**
     * Finds the closest {@link TransformWithParameter} to the <code>referencePosition</code>.
     * <p>
     * The {@link TransformWithParameter} contains the {@link Transform} of the point as well as the <code>t</code> value of it
     * along the {@link Parametric}.
     * <p>
     * The closest point is found by sampling <code>searchIncrement</code> amount of points on the {@link Parametric}.
     * After the initial points are sampled, it finds the closest point and creates a smaller bound to search within. It
     * then repeats the same process within the smaller bound for a total of <code>searches</code> loops. Therefore, the
     * total resolution of the closest point is <code>searchIncrement</code> to the power of <code>searches</code>.
     * <p>
     * For example, if <code>searchIncrement</code> is 10 and <code>searches</code> is 3, it will get 10 points, create
     * a smaller boundary, get another 10 points within the boundary, create a smaller boundary again, and lastly find
     * the closest point within 10 points within the final smaller boundary. This would find a point within the accuracy
     * of 1000 points, although it only does 30 total samples.
     * <p>
     * If the point is outside the start and end of the {@link Path}, either the start or end {@link Transform} will be
     * picked.
     *
     * @param referencePosition the {@link Position} to find the closest {@link TransformWithParameter} to.
     * @param searchIncrement   the samples within each search.
     * @param searches          the amount of searches to perform to get the final closest value.
     * @return the closest {@link TransformWithParameter} to the <code>referencePosition</code>.
     */
    public TransformWithParameter getClosestTransform(Position referencePosition, double searchIncrement,
                                                      double searches) {
        return getClosestTransform(referencePosition, 0, true, searchIncrement, searches);
    }

    /**
     * Finds the closest {@link TransformWithParameter} to the <code>referencePosition</code> given a
     * <code>distanceShift</code>.
     * <p>
     * The {@link TransformWithParameter} contains the {@link Transform} of the point as well as the <code>t</code> value of it
     * along the {@link Parametric}.
     * <p>
     * The closest point is found by sampling <code>searchIncrement</code> amount of points on the {@link Parametric}.
     * After the initial points are sampled, it finds the closest point and creates a smaller bound to search within. It
     * then repeats the same process within the smaller bound for a total of <code>searches</code> loops. Therefore, the
     * total resolution of the closest point is <code>searchIncrement</code> to the power of <code>searches</code>.
     * <p>
     * For example, if <code>searchIncrement</code> is 10 and <code>searches</code> is 3, it will get 10 points, create
     * a smaller boundary, get another 10 points within the boundary, create a smaller boundary again, and lastly find
     * the closest point within 10 points within the final smaller boundary. This would find a point within the accuracy
     * of 1000 points, although it only does 30 total samples.
     * <p>
     * If the point is outside the start and end of the {@link Path}, either the start or end {@link Transform} will be
     * picked. If the distance shifted point is off the {@link Path}, it will be interpolated on a line extending either
     * the start or end {@link Transform} of the {@link Path}.
     * <p>
     * Since there is a <code>distanceShift</code> in this search, it first finds the actual closest point to the
     * <code>referencePosition</code> and then performs the distance shifted search using the actual closest point as a
     * guide of whether or not the point is in front or behind the <code>referencePosition</code>.
     *
     * @param referencePosition the {@link Position} to find the closest {@link TransformWithParameter} to.
     * @param distanceShift     the distance away from the <code>referencePosition</code> the closest point should be.
     * @param searchIncrement   the samples within each search.
     * @param searches          the amount of searches to perform to get the final closest value.
     * @return the closest {@link TransformWithParameter} to the <code>referencePosition</code>.
     */
    public TransformWithParameter getClosestTransform(Position referencePosition, double distanceShift,
                                                      double searchIncrement,
                                                      double searches) {
        return getClosestTransform(referencePosition, distanceShift, true, searchIncrement, searches);
    }

    /**
     * Finds the closest {@link TransformWithParameter} to the <code>referencePosition</code> given a
     * <code>distanceShift</code>.
     * <p>
     * The {@link TransformWithParameter} contains the {@link Transform} of the point as well as the <code>t</code> value of it
     * along the {@link Parametric}.
     * <p>
     * The closest point is found by sampling <code>searchIncrement</code> amount of points on the {@link Parametric}.
     * After the initial points are sampled, it finds the closest point and creates a smaller bound to search within. It
     * then repeats the same process within the smaller bound for a total of <code>searches</code> loops. Therefore, the
     * total resolution of the closest point is <code>searchIncrement</code> to the power of <code>searches</code>.
     * <p>
     * For example, if <code>searchIncrement</code> is 10 and <code>searches</code> is 3, it will get 10 points, create
     * a smaller boundary, get another 10 points within the boundary, create a smaller boundary again, and lastly find
     * the closest point within 10 points within the final smaller boundary. This would find a point within the accuracy
     * of 1000 points, although it only does 30 total samples.
     * <p>
     * If the point is outside the start and end of the {@link Path}, either the start or end {@link Transform} of the
     * {@link Path} will be picked. If the distance shifted point is off the {@link Path}, it will be interpolated on a
     * line extending either the start or end {@link Transform} of the {@link Path}.
     * <p>
     * Since there is a <code>distanceShift</code> in this search, it first finds the actual closest point to the
     * <code>referencePosition</code> and then performs the distance shifted search using the actual closest point as a
     * guide of whether or not the point is in front or behind the <code>referencePosition</code>. It will only pick
     * points in front of the actual closest point on the {@link Path} if <code>pointInFront</code> is
     * <code>false</code>, and only behind the actual closest point on the {@link Path} if <code>pointInFront</code> is
     * <code>true</code>.
     *
     * @param referencePosition the {@link Position} to find the closest {@link TransformWithParameter} to.
     * @param distanceShift     the distance away from the <code>referencePosition</code> the closest point should be.
     * @param pointInFront      whether to find the closest {@link Position} behind or in front of the
     *                          <code>referencePosition</code>.
     * @param searchIncrement   the samples within each search.
     * @param searches          the amount of searches to perform to get the final closest value.
     * @return the closest {@link TransformWithParameter} to the <code>referencePosition</code>.
     */
    public TransformWithParameter getClosestTransform(Position referencePosition, double distanceShift,
                                                      boolean pointInFront,
                                                      double searchIncrement, double searches) {

        double distanceToEndWaypoint = referencePosition.distance(getEndWaypoint().getPosition());
        if (distanceToEndWaypoint <= distanceShift) {
            double distanceOffset = distanceShift - distanceToEndWaypoint;
            Rotation rotation = getEndWaypoint().getRotation();
            Position position = getEndWaypoint().getPosition()
                    .add(new Position(rotation.cos() * distanceOffset, rotation.sin() * distanceOffset));
            return new TransformWithParameter(new Transform(position), 1);
        }

        double tFinal;
        if (distanceShift == 0) {
            tFinal = getClosestT(referencePosition, searchIncrement, searches);
        } else {
            tFinal = getClosestT(referencePosition, distanceShift, pointInFront, searchIncrement, searches);
        }

        Transform transform = getTransform(tFinal);
        return new TransformWithParameter(transform, tFinal);
    }

    /**
     * Finds the closest <code>t</code> value on the {@link Parametric} to the <code>referencePosition</code>.
     * <p>
     * The <code>t</code> value contains the position along the {@link Parametric} between 0 and 1.
     * <p>
     * The closest <code>t</code> value of the point is found by sampling <code>searchIncrement</code> amount of points
     * on the {@link Parametric}. After the initial points are sampled, it finds the closest point and creates a smaller
     * bound to search within. It then repeats the same process within the smaller bound for a total of
     * <code>searches</code> loops. Therefore, the total resolution of the closest point is
     * <code>searchIncrement</code> to the power of <code>searches</code>.
     * <p>
     * For example, if <code>searchIncrement</code> is 10 and <code>searches</code> is 3, it will get 10 points, create
     * a smaller boundary, get another 10 points within the boundary, create a smaller boundary again, and lastly find
     * the closest point within 10 points within the final smaller boundary. This would find a point within the accuracy
     * of 1000 points, although it only does 30 total samples.
     * <p>
     * If the point is outside the start and end of the {@link Path}, either 0 or 1 will be picked, representing the
     * first or last point on the {@link Parametric}.
     *
     * @param referencePosition the {@link Position} to find the closest <code>t</code> value to.
     * @param searchIncrement   the samples within each search.
     * @param searches          the amount of searches to perform to get the final closest value.
     * @return the closest <code>t</code> value on the {@link Parametric} that is closest to the
     * <code>referencePosition</code>.
     */
    public double getClosestT(Position referencePosition, double searchIncrement, double searches) {
        double tFinal = 0;
        double minSearchT = 0;
        double maxSearchT = 1;
        double finalMinSearchT = 0;
        double finalMaxSearchT = 1;
        double closestDistance = Double.POSITIVE_INFINITY;
        for (int j = 1; j < searches + 1; j++) {
            double currentIncrement = 1 / Math.pow(searchIncrement, j);
            for (double t = finalMinSearchT; t < finalMaxSearchT; t += currentIncrement) {
                Transform transform = getTransform(t);
                double distance = Math.abs(transform.getPosition().distance(referencePosition));
                if (distance < closestDistance) {
                    closestDistance = distance;
                    minSearchT = t - currentIncrement;
                    maxSearchT = t + currentIncrement;
                    tFinal = t;
                }
            }
            finalMinSearchT = minSearchT;
            finalMaxSearchT = maxSearchT;
        }

        return tFinal;
    }

    /**
     * Finds the closest <code>t</code> value on the {@link Parametric} to the <code>referencePosition</code> given a
     * <code>distanceShift</code>.
     * <p>
     * The {@link TransformWithParameter} contains the {@link Transform} of the point as well as the <code>t</code> value of it
     * along the {@link Parametric}.
     * <p>
     * The closest point is found by sampling <code>searchIncrement</code> amount of points on the {@link Parametric}.
     * After the initial points are sampled, it finds the closest point and creates a smaller bound to search within. It
     * then repeats the same process within the smaller bound for a total of <code>searches</code> loops. Therefore, the
     * total resolution of the closest point is <code>searchIncrement</code> to the power of <code>searches</code>.
     * <p>
     * For example, if <code>searchIncrement</code> is 10 and <code>searches</code> is 3, it will get 10 points, create
     * a smaller boundary, get another 10 points within the boundary, create a smaller boundary again, and lastly find
     * the closest point within 10 points within the final smaller boundary. This would find a point within the accuracy
     * of 1000 points, although it only does 30 total samples.
     * <p>
     * If the point is outside the start and end of the {@link Path}, either 0 or 1 will be picked, representing the
     * first or last point on the {@link Parametric}. If the distance shifted point is off the {@link Path}, it will
     * also either choose 0 or 1.
     * <p>
     * Since there is a <code>distanceShift</code> in this search, it first finds the actual closest point to the
     * <code>referencePosition</code> and then performs the distance shifted search using the actual closest point as a
     * guide of whether or not the point is in front or behind the <code>referencePosition</code>.
     *
     * @param referencePosition the {@link Position} to find the closest <code>t</code> value to.
     * @param distanceShift     the distance away from the <code>referencePosition</code> the closest point should be.
     * @param searchIncrement   the samples within each search.
     * @param searches          the amount of searches to perform to get the final closest value.
     * @return the closest <code>t</code> value on the {@link Parametric} that is closest to the
     * <code>referencePosition</code>.
     */
    public double getClosestT(Position referencePosition, double distanceShift, double searchIncrement,
                              double searches) {
        return getClosestT(referencePosition, distanceShift, searchIncrement, searches);
    }

    /**
     * Finds the closest <code>t</code> value on the {@link Parametric} to the <code>referencePosition</code> given a
     * <code>distanceShift</code>.
     * <p>
     * The {@link TransformWithParameter} contains the {@link Transform} of the point as well as the <code>t</code> value of it
     * along the {@link Parametric}.
     * <p>
     * The closest point is found by sampling <code>searchIncrement</code> amount of points on the {@link Parametric}.
     * After the initial points are sampled, it finds the closest point and creates a smaller bound to search within. It
     * then repeats the same process within the smaller bound for a total of <code>searches</code> loops. Therefore, the
     * total resolution of the closest point is <code>searchIncrement</code> to the power of <code>searches</code>.
     * <p>
     * For example, if <code>searchIncrement</code> is 10 and <code>searches</code> is 3, it will get 10 points, create
     * a smaller boundary, get another 10 points within the boundary, create a smaller boundary again, and lastly find
     * the closest point within 10 points within the final smaller boundary. This would find a point within the accuracy
     * of 1000 points, although it only does 30 total samples.
     * <p>
     * If the point is outside the start and end of the {@link Path}, either 0 or 1 will be picked, representing the
     * first or last point on the {@link Parametric}. If the distance shifted point is off the {@link Path}, it will
     * also either choose 0 or 1.
     * <p>
     * Since there is a <code>distanceShift</code> in this search, it first finds the actual closest point to the
     * <code>referencePosition</code> and then performs the distance shifted search using the actual closest point as a
     * guide of whether or not the point is in front or behind the <code>referencePosition</code>. It will only pick
     * points in front of the actual closest point on the {@link Path} if pointInFront is <code>false</code>, and only
     * behind the actual closest point on the {@link Path} if pointInFront is <code>true</code>.
     *
     * @param referencePosition the {@link Position} to find the closest <code>t</code> value to.
     * @param distanceShift     the distance away from the <code>referencePosition</code> the closest point should be.
     * @param pointInFront      whether to find the closest {@link Position} behind or in front of the
     *                          <code>referencePosition</code>.
     * @param searchIncrement   the samples within each search.
     * @param searches          the amount of searches to perform to get the final closest value.
     * @return the closest <code>t</code> value on the {@link Parametric} that is closest to the
     * <code>referencePosition</code>.
     */
    public double getClosestT(Position referencePosition, double distanceShift, boolean pointInFront,
                              double searchIncrement, double searches) {
        double actualClosestT = getClosestT(referencePosition, searchIncrement, searches);

        if (distanceShift == 0) {
            return actualClosestT;
        }

        double tFinal = 0;
        double closestDistance = Double.POSITIVE_INFINITY;
        double minSearchT = 0;
        double maxSearchT = 1;
        double finalMinSearchT = 0;
        double finalMaxSearchT = 1;
        for (int j = 1; j < searches + 1; j++) {
            double currentIncrement = 1 / Math.pow(searchIncrement, j);
            for (double t = finalMinSearchT; t < finalMaxSearchT; t += currentIncrement) {
                Transform transform = getTransform(t);
                double distance = Math.abs(transform.getPosition().distance(referencePosition) - distanceShift);
                if (distance < closestDistance &&
                        ((!pointInFront && actualClosestT >= t) || (pointInFront && actualClosestT <= t))) {
                    closestDistance = distance;
                    minSearchT = t - currentIncrement;
                    maxSearchT = t + currentIncrement;
                    tFinal = t;
                }
            }
            finalMinSearchT = minSearchT;
            finalMaxSearchT = maxSearchT;
        }

        return tFinal;
    }

    /**
     * Generates an adaptive {@link Path} that makes <code>newStartTransform</code> the starting {@link Transform} of
     * the {@link Path}.
     * <p>
     * It will adapt the waypoints of the {@link Path} to get from the <code>newStartTransform</code> to the current
     * {@link Path} with a distance of <code>adjustPathDistance</code> ahead of the <code>newStartTransform</code>.
     * Maintains all waypoints after the <code>newStartTransform</code>.
     * <p>
     * In other words, it will set the first waypoint of the new {@link Path} to <code>newStartTransform</code>. Then,
     * it will set the next waypoint of the new {@link Path} to a point on the original {@link Path} that is
     * <code>adjustedPathDistance</code> away from the <code>newStartTransform</code>. After those two waypoints, all
     * remaining waypoints of the path stay the same, maintaining the original structure of the path after the
     * <code>newStartTransform</code>.
     *
     * @param newStartTransform
     * @param adaptToStartHeading
     * @return
     */
    public Transform[] generateAdaptivePathWaypoints(
            Transform newStartTransform,
            boolean adaptToStartHeading) {
        TransformWithParameter onPathPoint = getClosestTransform(newStartTransform.getPosition(), 10, 3);
        Transform[] waypoints = getWaypoints();

        int startWaypointIndex = 0;
        double currentClosest = Double.POSITIVE_INFINITY;
        for (int i = 0; i < waypoints.length; i++) {
            double waypointT = getClosestT(waypoints[i].getPosition(), 10, 3);
            double distance = waypoints[i].getPosition().distance(onPathPoint.getPosition());
            if (distance < currentClosest && waypointT > onPathPoint.getParameter()) {
                currentClosest = distance;
                startWaypointIndex = i;
            }
        }

        ArrayList<Transform> adjustedPathWaypoints = new ArrayList<>();
        Transform nextWaypoint = waypoints[startWaypointIndex];

        if (adaptToStartHeading) {
            adjustedPathWaypoints.add(newStartTransform);
        } else {
            adjustedPathWaypoints
                    .add(new Transform(new Transform(newStartTransform.getPosition(), onPathPoint.getRotation())));
        }

        adjustedPathWaypoints.add(nextWaypoint);
        for (int i = startWaypointIndex; i < waypoints.length; i++) {
            adjustedPathWaypoints.add(waypoints[i]);
        }

        Transform[] adjustedPathWaypointsArray = new Transform[adjustedPathWaypoints.size()];
        for (int i = 0; i < adjustedPathWaypointsArray.length; i++) {
            adjustedPathWaypointsArray[i] = adjustedPathWaypoints.get(i);
        }

        return adjustedPathWaypointsArray;
    }

    /**
     * Returns an array of {@link Transform}s that make up the waypoints of this {@link Path} in reverse.
     * <p>
     * The first waypoint becomes the last waypoint, and all following waypoints are reversed in order from front to
     * back.
     *
     * @returnan array of {@link Transform}s that make up the waypoints of this {@link Path} in reverse.
     */
    public Transform[] getReversedWaypoints() {
        Transform[] newWaypoints = new Transform[waypoints.length];
        for (int i = 0; i < newWaypoints.length; i++) {
            newWaypoints[i] = waypoints[waypoints.length - 1 - i].rotateBy(new Rotation(180));
        }
        return newWaypoints;
    }

    /**
     * Returns the array of waypoint {@link Transform}s that make up the {@link Path}.
     *
     * @return the array of waypoint {@link Transform}s that make up the {@link Path}.
     */
    public Transform[] getWaypoints() {
        return waypoints;
    }

    /**
     * Returns the first waypoint {@link Transform} in the {@link Path}.
     *
     * @return the first waypoint {@link Transform} in the {@link Path}.
     */
    public Transform getStartWaypoint() {
        return waypoints[0];
    }

    /**
     * Returns the last waypoint {@link Transform} in the {@link Path}.
     *
     * @return the last waypoint {@link Transform} in the {@link Path}.
     */
    public Transform getEndWaypoint() {
        return waypoints[waypoints.length - 1];
    }

    /**
     * Returns the array of {@link Parametric}s that make up the different {@link Parametric} segments of the {@link
     * Path}.
     *
     * @return the array of {@link Parametric}s that make up the different {@link Parametric} segments of the {@link
     * Path}.
     */
    public Parametric[] getParametrics() {
        return parametrics;
    }

    /**
     * Sets the array of {@link Parametric}s that make up the different {@link Parametric} segments of the {@link
     * Path}.
     *
     * @param parametrics the new array of {@link Parametric}s that make up the different {@link Parametric} segments of
     *                    the {@link Path}.
     */
    public void setParametrics(Parametric[] parametrics) {
        this.parametrics = parametrics;
    }
}
