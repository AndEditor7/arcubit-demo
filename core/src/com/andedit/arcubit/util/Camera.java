package com.andedit.arcubit.util;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Quaternion;

/** A {@link PerspectiveCamera} with Pitch and yaw. */
public final class Camera extends PerspectiveCamera 
{
	private static final Quaternion quat = new Quaternion();
	
	/** A pitch of up and down. */
	public float pitch;
	/** A yaw of left and right. */
	public float yaw;
	
	public Camera () {
	}

	/** Constructs a new {@link PerspectiveCamera} with the given field of view and viewport size. The aspect ratio is derived from
	 * the viewport size.
	 * 
	 * @param fieldOfViewY the field of view of the height, in degrees, the field of view for the width will be calculated
	 *           according to the aspect ratio.
	 * @param viewportWidth the viewport width
	 * @param viewportHeight the viewport height */
	public Camera (float fieldOfViewY, float viewportWidth, float viewportHeight) {
		super(fieldOfViewY, viewportWidth, viewportHeight);
	}
	
	public void updateRotation() {
		//reset quaternion and then set its rotation.
		quat.setEulerAngles(yaw, pitch, 0f);
		
		//set camera angle back to zero and rotate it.
		direction.set(0f, 0f, 1f);
		up.set(0f, 1f, 0f);
		rotate(quat);
	}
}
