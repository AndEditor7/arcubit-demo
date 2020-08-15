package com.andedit.arcubit.util.math;

import com.andedit.arcubit.chunk.Chunk;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Plane.PlaneSide;

/** Fast frustum utilities. */
public class FrustUtil 
{
	public static boolean pointInFrustum (final Plane[] planes, Vector3 point) {
		for (int i = 2; i < planes.length; i++) {
			if (planes[i].testPoint(point) == PlaneSide.Back) return false;
		}
		return true;
	}
	
	/** the six clipping planes, near, far, left, right, top, bottom **/
	public static boolean frustBounds(final Plane[] planes, Chunk chunk) 
	{
		final float x = (chunk.x<<4)+8;
		final float y = (chunk.y<<4)+8;
		final float z = (chunk.z<<4)+8;
		for (Plane plane : planes) {
			if (testBounds(plane, x, y, z) != PlaneSide.Back) {
				continue;
			}
			return false;
		}
		
		return true;
	}

	private static PlaneSide testBounds(final Plane plane, final float x, final float y, final float z) {
		// Compute the projection interval radius of b onto L(t) = b.c + t * p.n
		final float radius = 8f * Math.abs(plane.normal.x) +
					   8f * Math.abs(plane.normal.y) +
					   8f * Math.abs(plane.normal.z);

		// Compute distance of box center from plane
		final float dist = plane.normal.dot(x, y, z) + plane.d;

		// Intersection occurs when plane distance falls within [-r,+r] interval
		if (dist > radius) {
			return PlaneSide.Front;
		} else if (dist < -radius) {
			return PlaneSide.Back;
		}
		
		return PlaneSide.OnPlane;
	}
}
