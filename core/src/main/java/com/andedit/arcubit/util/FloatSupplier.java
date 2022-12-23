package com.andedit.arcubit.util;

/**
 * Represents a supplier of {@code float}-valued results.
 */
@FunctionalInterface
public interface FloatSupplier {

    /**
     * Gets a result.
     * @return a result
     */
    float getAsFloat();
}
