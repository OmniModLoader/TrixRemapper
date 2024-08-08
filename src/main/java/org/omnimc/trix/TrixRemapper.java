/*
 * MIT License
 *
 * Copyright (c) 2024 OmniMC
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

package org.omnimc.trix;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Remapper;
import org.omnimc.lumina.paser.ParsingContainer;

/**
 * {@code TrixRemapper} is a custom remapper that uses information from the {@linkplain ParsingContainer} to translate
 * class, method, and field names from obfuscated to readable forms.
 *
 * <p>This remapper helps convert internal names, method names, and field names into more understandable forms
 * based on the mappings provided by the {@linkplain ParsingContainer}. It's especially useful when working with
 * obfuscated code and you need to map it to more meaningful names.</p>
 *
 * @author <b><a href="https://github.com/CadenCCC">Caden</a></b>
 * @since 1.0.0
 */
public class TrixRemapper extends Remapper {

    private final ParsingContainer container;

    public TrixRemapper(ParsingContainer container) {
        this.container = container;
    }

    /**
     * <h6>Remaps the given class name to its readable form.
     *
     * @param internalName The internal name of the class.
     * @return The readable class name.
     */
    @Override
    public String map(String internalName) {
        return mapType(internalName);
    }

    /**
     * {@inheritDoc}
     *
     * @param internalName the internal name (or array type descriptor) of some (array) class (see
     *                     {@linkplain Type#getInternalName()}).
     * @return remapped internalName, or returns internalName if it can't find it.
     */
    @Override
    public String mapType(String internalName) {
        return container.getClassName(internalName);
    }

    /**
     * <h6>Remaps the given method name to its readable form.
     *
     * @param owner      The name of the class that owns the method.
     * @param name       The obfuscated name of the method.
     * @param descriptor The method descriptor.
     * @return The readable method name.
     */
    @Override
    public String mapMethodName(String owner, String name, String descriptor) {
        if (descriptor != null) {
            descriptor = mapMethodDesc(descriptor);
        }

        return container.getMethodName(owner, name, descriptor);
    }

    /**
     * <h6>Remaps the given field name to its readable form.
     *
     * @param owner      The name of the class that owns the field.
     * @param name       The obfuscated name of the field.
     * @param descriptor The field descriptor.
     * @return The readable field name.
     */
    @Override
    public String mapFieldName(String owner, String name, String descriptor) {
        return container.getFieldName(owner, name);
    }
}