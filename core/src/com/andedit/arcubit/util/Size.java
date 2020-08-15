package com.andedit.arcubit.util;

/** Simple dimension width and height integer components. */
public final class Size
{
	/** Width dimension. */
	public int w;
	/** Height dimension. */
	public int h;
	
	public Size() {
	}
	
	public Size(int width, int height) {
		w = width;
		h = height;
	}
	
	public void set (int width, int height) {
		w = width;
		h = height;
	}
	
	public void set (Size size) {
		w = size.w;
		h = size.h;
	}
	
	@Override
	public int hashCode() {
		return w * 31 + h;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null) return false;
        if (obj.getClass() == Size.class) {
        	Size size = (Size)obj;
        	return size.w == w && size.h == h;
        }
		return false;
    }
	
	@Override
	public String toString () {
		return "Width: " + w + ", Height: " + h;
	}

	@Override
	public Size clone() {
		return new Size(w, h);
	}
}
