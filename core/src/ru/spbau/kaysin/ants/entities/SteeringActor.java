package ru.spbau.kaysin.ants.entities;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import ru.spbau.kaysin.ants.utils.GameMathUtils;
import ru.spbau.kaysin.ants.utils.Scene2dLocation;


// TODO simplify this class. There are a lot of useless things
public abstract class SteeringActor extends Actor implements Steerable<Vector2> {

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());


    private Vector2 position;  // like scene2d centerX and centerY, but we need a vector to implement Steerable
    private Vector2 linearVelocity;
    private float angularVelocity;
    private float boundingRadius;
    private boolean tagged;

    private float speed = 120;

    private boolean independentFacing;

    private SteeringBehavior<Vector2> steeringBehavior;

    private Rectangle bounds;


    public SteeringActor (boolean independentFacing) {
        this.independentFacing = independentFacing;

        this.position = new Vector2();
        this.linearVelocity = new Vector2();
    }

    public void setBoundingRadius(float boundingRadius) {
        this.boundingRadius = boundingRadius;
    }

    @Override
    public Vector2 getPosition () {
        return position;
    }

    @Override
    public float getOrientation () {
        return getRotation() * MathUtils.degreesToRadians;
    }

    @Override
    public void setOrientation (float orientation) {
        setRotation(orientation * MathUtils.radiansToDegrees);
    }

    @Override
    public Vector2 getLinearVelocity () {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity () {
        return angularVelocity;
    }

    public void setAngularVelocity (float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    @Override
    public float getBoundingRadius () {
        return boundingRadius;
    }

    @Override
    public boolean isTagged () {
        return tagged;
    }

    @Override
    public void setTagged (boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Location<Vector2> newLocation () {
        return new Scene2dLocation();
    }

    @Override
    public float vectorToAngle (Vector2 vector) {
        return GameMathUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector (Vector2 outVector, float angle) {
        return GameMathUtils.angleToVector(outVector, angle);
    }

    @Override
    public float getMaxLinearSpeed () {
        return speed;
    }

    @Override
    public void setMaxLinearSpeed (float maxLinearSpeed) {
        this.speed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration () {
        return speed;
    }

    @Override
    public void setMaxLinearAcceleration (float maxLinearAcceleration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMaxAngularSpeed () {
        return speed;
    }

    @Override
    public void setMaxAngularSpeed (float maxAngularSpeed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMaxAngularAcceleration () {
        return 1;
    }

    @Override
    public void setMaxAngularAcceleration (float maxAngularAcceleration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getZeroLinearSpeedThreshold () {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold (float value) {
        throw new UnsupportedOperationException();
    }

    public SteeringBehavior<Vector2> getSteeringBehavior () {
        return steeringBehavior;
    }

    public void setSteeringBehavior (SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

    @Override
    public void act (float delta) {

        position.set(getX(Align.center), getY(Align.center));
        if (steeringBehavior != null) {

            // Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);

            // Apply steering acceleration
            applySteering(steeringOutput, delta);

            wrapAround(position, getParent().getWidth(), getParent().getHeight());
            setPosition(position.x, position.y, Align.center);
        } else {
            linearVelocity.set(0, 0);
            angularVelocity = 0;
        }
        super.act(delta);
    }

    // the display area is considered to wrap around from top to bottom
    // and from left to right
    protected static void wrapAround (Vector2 pos, float maxX, float maxY) {
        if (pos.x > maxX) pos.x = 0.0f;

        if (pos.x < 0) pos.x = maxX;

        if (pos.y < 0) pos.y = maxY;

        if (pos.y > maxY) pos.y = 0.0f;
    }

    private void applySteering (SteeringAcceleration<Vector2> steering, float time) {
        // Update position and linear velocity. Velocity is trimmed to maximum speed
        position.mulAdd(linearVelocity, time);
//        linearVelocity.mulAdd(steering.linear, time).limit(getMaxLinearSpeed());
        linearVelocity = steering.linear.limit(getMaxLinearSpeed());
        // Update orientation and angular velocity
        if (independentFacing) {
            setRotation(getRotation() + (angularVelocity * time) * MathUtils.radiansToDegrees);
            angularVelocity += steering.angular * time;
        } else {
            // If we haven't got any velocity, then we can do nothing.
            if (!linearVelocity.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linearVelocity);
                angularVelocity = (newOrientation - getRotation() * MathUtils.degreesToRadians) * time; // this is superfluous if independentFacing is always true
                setRotation(newOrientation * MathUtils.radiansToDegrees);
            }
        }
    }

}