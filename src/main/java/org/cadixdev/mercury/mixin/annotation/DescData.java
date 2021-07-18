/*
 * This file is part of Mixin, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.cadixdev.mercury.mixin.annotation;

import static org.cadixdev.mercury.mixin.util.MixinConstants.DESC_CLASS;

import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;

import java.util.Objects;

/**
 * A container for data held in the {@code @Desc} annotation.
 *
 * @author Jamie Mansfield
 * @since 0.1.0
 */
public class DescData {

    public static DescData fetch(final ITypeBinding binding) {
        for (final IAnnotationBinding annotation : binding.getAnnotations()) {
            if (Objects.equals(DESC_CLASS, annotation.getAnnotationType().getBinaryName())) {
                return from(annotation);
            }
        }

        return null;
    }

    // @Desc(value = "a", ret = void.class, args = { String.class })
    public static DescData from(final IAnnotationBinding binding) {
        ITypeBinding ownerBinding = null;
        String name = null;
        ITypeBinding returnBinding = null;
        ITypeBinding[] argBindings = {};

        for (final IMemberValuePairBinding pair : binding.getDeclaredMemberValuePairs()) {
            if ("owner".equals(pair.getName())) {
                ownerBinding = (ITypeBinding) pair.getValue();
            }
            if ("value".equals(pair.getName())) {
                name = (String) pair.getValue();
            }
            if ("ret".equals(pair.getName())) {
                returnBinding = (ITypeBinding) pair.getValue();
            }
            if ("args".equals(pair.getName())) {
                final Object[] targetsTemp = (Object[]) pair.getValue();

                argBindings = new ITypeBinding[targetsTemp.length];
                for (int i = 0; i < targetsTemp.length; i++) {
                    argBindings[i] = (ITypeBinding) targetsTemp[i];
                }
            }
        }

        return new DescData(ownerBinding, name, returnBinding, argBindings);
    }

    private final ITypeBinding ownerBinding;
    private final String name;
    private final ITypeBinding returnBinding;
    private final ITypeBinding[] argBindings;

    public DescData(final ITypeBinding ownerBinding, final String name,
                    final ITypeBinding returnBinding, final ITypeBinding[] argBindings) {
        this.ownerBinding = ownerBinding;
        this.name = name;
        this.returnBinding = returnBinding;
        this.argBindings = argBindings;
    }

    public ITypeBinding getOwnerBinding() {
        return this.ownerBinding;
    }

    public String getName() {
        return this.name;
    }

    public ITypeBinding getReturnBinding() {
        return this.returnBinding;
    }

    public ITypeBinding[] getArgBindings() {
        return this.argBindings;
    }

}
