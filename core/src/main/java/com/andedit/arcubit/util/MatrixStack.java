package com.andedit.arcubit.util;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MatrixStack {
	private final Array<Matrix4> mats = new Array<>();
	private final Vector3 vec = new Vector3();
	
	/** Multiplies a returned vector with a xyz input.
	* @return a vector instance from this MatixStack with matrix multiplication. */
	public Vector3 mul(float x, float y, float z) {
		return mulOut(vec.set(x, y, z));
	}
	
	/** Multiplies a returned vector with an input vector.
	 * @param out the input vector will be modified with matrix multiplication. 
	 * @return a vector out parameter with matrix multiplication. */
	public Vector3 mulOut(Vector3 out) {
		for (int i = mats.size-1; i >= 0; --i) {
			out.mul(mats.get(i));
		}
		return out;
	}
	
	/** Multiplies a returned vector with an input vector.
	 * @param in the input vector will be unmodified with matrix multiplication. 
	 * @return a vector instance from this MatixStack with matrix multiplication. */
	public Vector3 mulIn(Vector3 in) {
		return mulOut(vec.set(in));
	}

	public Matrix4 push() {
		var mat = new Matrix4();
		mats.add(mat);
		return mat;
	}
	
	public Matrix4 peek() {
		return mats.peek();
	}
	
	public void pop() {
		mats.pop();
	}
	
	public void clear() {
		mats.clear();
	}
}
