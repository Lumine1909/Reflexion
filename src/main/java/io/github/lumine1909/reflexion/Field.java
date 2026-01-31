package io.github.lumine1909.reflexion;

import io.github.lumine1909.reflexion.exception.OperationException;
import io.github.lumine1909.reflexion.internal.UnsafeUtil;
import io.github.lumine1909.reflexion.internal.VarHolder;

import java.lang.Class;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import static io.github.lumine1909.reflexion.internal.UnsafeUtil.UNSAFE;

/**
 * A high-performance wrapper around {@link java.lang.reflect.Field} that
 * provides unsafe and varhandle-based access.
 *
 * <p>This abstraction supports:
 * <ul>
 *   <li>Bypassing Java access checks</li>
 *   <li>Fast field access via inlined {@link VarHandle}</li>
 *   <li>Fallback to {@code Unsafe}-based access</li>
 * </ul>
 *
 * <p>Static fields may be accessed with {@code instance == null}.</p>
 *
 * @param <T> the field type
 */
@SuppressWarnings("unchecked")
public record Field<T>(java.lang.reflect.Field javaField, UnsafeFieldHolder holder, boolean isStatic,
                       Supplier<VarHandle> supplier) {

    /**
     * Creates a {@link Field} wrapper from a reflective {@link java.lang.reflect.Field}
     *
     * @param javaField the reflective field
     */
    public Field(java.lang.reflect.Field javaField) {
        this(javaField, new UnsafeFieldHolder(javaField), Modifier.isStatic(javaField.getModifiers()), VarHolder.createSupplier(javaField));
    }

    /**
     * Looks up a field by name and type from the given class.
     *
     * @param clazz the declaring class
     * @param name  the field name
     * @param type  the expected field type
     * @param <T>   field type
     * @return a {@link Field} wrapper
     */
    public static <T> Field<T> of(Class<?> clazz, String name, Class<T> type) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getField(name, type);
    }

    /**
     * Looks up a field by name and type from the given class name.
     *
     * @param className fully-qualified class name
     * @param name      field name
     * @param type      expected field type
     * @param <T>       field type
     * @return a {@link Field} wrapper
     */
    public static <T> Field<T> of(String className, String name, Class<T> type) {
        return io.github.lumine1909.reflexion.Class.forName(className).getField(name, type);
    }

    /**
     * Looks up a field by name from the given class.
     *
     * @param clazz the declaring class
     * @param name  the field name
     * @param <T>   inferred field type
     * @return a {@link Field} wrapper
     */
    public static <T> Field<T> of(Class<?> clazz, String name) {
        return io.github.lumine1909.reflexion.Class.of(clazz).getField(name);
    }

    /**
     * Looks up a field by name from the given class name.
     *
     * @param className fully-qualified class name
     * @param name      the field name
     * @param <T>       inferred field type
     * @return a {@link Field} wrapper
     */
    public static <T> Field<T> of(String className, String name) {
        return io.github.lumine1909.reflexion.Class.forName(className).getField(name);
    }

    /**
     * Returns whether this field is static.
     *
     * @return {@code true} if the field is static
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Reads the field value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field value
     */
    public T get(Object instance) {
        return (T) holder.get(instance);
    }

    /**
     * Reads the field value using an inlined {@link VarHandle} when available.
     *
     * <p>This method is typically faster than {@link #get(Object)}.</p>
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field value
     * @throws OperationException if access fails
     */
    public T getFast(Object instance) {
        try {
            if (supplier != null) {
                return instance == null ? (T) supplier.get().get() : (T) supplier.get().get(instance);
            } else {
                return get(instance);
            }
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    /**
     * Reads the field value without static typing.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param <S>      expected return type
     * @return the field value
     */
    public <S> S getUntyped(Object instance) {
        return (S) holder.get(instance);
    }

    /**
     * Reads the field value without static typing using an inlined {@link VarHandle}.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param <S>      expected return type
     * @return the field value
     * @throws OperationException if access fails
     */
    public <S> S getUntypedFast(Object instance) {
        try {
            if (supplier != null) {
                return instance == null ? (S) supplier.get().get() : (S) supplier.get().get(instance);
            } else {
                return getUntyped(instance);
            }
        } catch (Throwable t) {
            throw new OperationException(t);
        }
    }

    /**
     * Writes a value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field value
     */
    public void set(Object instance, T value) {
        holder.set(instance, value);
    }

    /**
     * THIS IS NOT ACTUALLY FAST, FORCE TO USE #SET
     * Writes a value to the field using an inlined {@link VarHandle} when available.
     *
     * <p>This method is typically faster than {@link #set(Object, Object)}.</p>
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field value
     * @throws OperationException if access fails
     */
    public void setFast(Object instance, T value) {
        set(instance, value);
//        try {
//            if (supplier != null) {
//                if (instance == null) {
//                    supplier.get().set(value);
//                } else {
//                    supplier.get().set(instance, value);
//                }
//            } else {
//                set(instance, value);
//            }
//        } catch (Throwable t) {
//            throw new OperationException(t);
//        }
    }

    /**
     * Reads the field object value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field object value
     */
    public Object getObject(Object instance) {
        return holder.getObject(instance);
    }

    /**
     * Writes an object value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field object value
     */
    public void setObject(Object instance, Object value) {
        holder.setObject(instance, value);
    }

    /**
     * Reads the field byte value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field byte value
     */
    public byte getByte(Object instance) {
        return holder.getByte(instance);
    }

    /**
     * Writes a byte value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field byte value
     */
    public void setByte(Object instance, byte value) {
        holder.setByte(instance, value);
    }

    /**
     * Reads the field short value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field short value
     */
    public short getShort(Object instance) {
        return holder.getShort(instance);
    }

    /**
     * Writes a short value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field short value
     */
    public void setShort(Object instance, short value) {
        holder.setShort(instance, value);
    }

    /**
     * Reads the field int value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field int value
     */
    public int getInt(Object instance) {
        return holder.getInt(instance);
    }

    /**
     * Writes a int value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field int value
     */
    public void setInt(Object instance, int value) {
        holder.setInt(instance, value);
    }

    /**
     * Reads the field long value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field long value
     */
    public long getLong(Object instance) {
        return holder.getLong(instance);
    }

    /**
     * Writes a long value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field long value
     */
    public void setLong(Object instance, long value) {
        holder.setLong(instance, value);
    }

    /**
     * Reads the field float value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field float value
     */
    public float getFloat(Object instance) {
        return holder.getFloat(instance);
    }

    /**
     * Writes a float value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field float value
     */
    public void setFloat(Object instance, float value) {
        holder.setFloat(instance, value);
    }

    /**
     * Reads the field double value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field double value
     */
    public double getDouble(Object instance) {
        return holder.getDouble(instance);
    }

    /**
     * Writes a double value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field double value
     */
    public void setDouble(Object instance, double value) {
        holder.setDouble(instance, value);
    }

    /**
     * Reads the field boolean value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field boolean value
     */
    public boolean getBoolean(Object instance) {
        return holder.getBoolean(instance);
    }

    /**
     * Reads the field char value.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @return the field char value
     */
    public char getChar(Object instance) {
        return holder.getChar(instance);
    }

    /**
     * Writes a char value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field char value
     */
    public void setChar(Object instance, char value) {
        holder.setChar(instance, value);
    }


    /**
     * Writes a boolean value to the field.
     *
     * @param instance the target instance, or {@code null} for static fields
     * @param value    the new field boolean value
     */
    public void setBoolean(Object instance, boolean value) {
        holder.setBoolean(instance, value);
    }

    @SuppressWarnings({"DataFlowIssue"})
    private record UnsafeFieldHolder(long objectOffset, Object staticBase, long staticOffset, Type type) {

        private UnsafeFieldHolder(java.lang.reflect.Field javaField) {
            this(
                Modifier.isStatic(javaField.getModifiers()) ? -1 : UnsafeUtil.objectFieldOffset(javaField),
                Modifier.isStatic(javaField.getModifiers()) ? UnsafeUtil.staticFieldBase(javaField) : null,
                Modifier.isStatic(javaField.getModifiers()) ? UnsafeUtil.staticFieldOffset(javaField) : -1,
                getType(javaField)
            );
        }

        private static Type getType(java.lang.reflect.Field javaField) {
            Class<?> type = javaField.getType();
            if (type == byte.class) return Type.BYTE;
            if (type == int.class) return Type.INT;
            if (type == short.class) return Type.SHORT;
            if (type == long.class) return Type.LONG;
            if (type == float.class) return Type.FLOAT;
            if (type == double.class) return Type.DOUBLE;
            if (type == char.class) return Type.CHAR;
            if (type == boolean.class) return Type.BOOLEAN;
            return Type.OBJECT;
        }

        public Object get(Object obj) {
            if (objectOffset == -1) {
                return get0(staticBase, staticOffset);
            } else {
                return get0(obj, objectOffset);
            }
        }

        public void set(Object obj, Object value) {
            if (objectOffset == -1) {
                set0(staticBase, staticOffset, value);
            } else {
                set0(obj, objectOffset, value);
            }
        }

        private Object get0(Object base, long offset) {
            return switch (type) {
                case OBJECT -> UNSAFE.getObject(base, offset);
                case BYTE -> UNSAFE.getByte(base, offset);
                case SHORT -> UNSAFE.getShort(base, offset);
                case INT -> UNSAFE.getInt(base, offset);
                case LONG -> UNSAFE.getLong(base, offset);
                case FLOAT -> UNSAFE.getFloat(base, offset);
                case DOUBLE -> UNSAFE.getDouble(base, offset);
                case CHAR -> UNSAFE.getChar(base, offset);
                case BOOLEAN -> UNSAFE.getBoolean(base, offset);
            };
        }

        private void set0(Object base, long offset, Object value) {
            switch (type) {
                case OBJECT -> UNSAFE.putObject(base, offset, value);
                case BYTE -> UNSAFE.putByte(base, offset, (byte) value);
                case SHORT -> UNSAFE.putShort(base, offset, (short) value);
                case INT -> UNSAFE.putInt(base, offset, (int) value);
                case LONG -> UNSAFE.putLong(base, offset, (long) value);
                case FLOAT -> UNSAFE.putFloat(base, offset, (float) value);
                case DOUBLE -> UNSAFE.putDouble(base, offset, (double) value);
                case CHAR -> UNSAFE.putChar(base, offset, (char) value);
                case BOOLEAN -> UNSAFE.putBoolean(base, offset, (boolean) value);
            }
        }

        public Object getObject(Object object) {
            if (type != Type.OBJECT)
                throw new UnsupportedOperationException("Can not read object from non-object field");
            if (objectOffset == -1) {
                return UNSAFE.getObject(staticBase, staticOffset);
            } else {
                return UNSAFE.getObject(object, objectOffset);
            }
        }

        public void setObject(Object object, Object value) {
            if (type != Type.OBJECT)
                throw new UnsupportedOperationException("Can not write object to non-object field");
            if (objectOffset == -1) {
                UNSAFE.putObject(staticBase, staticOffset, value);
            } else {
                UNSAFE.putObject(object, objectOffset, value);
            }
        }

        public byte getByte(Object object) {
            if (type != Type.BYTE) throw new UnsupportedOperationException("Can not read byte from non-byte field");
            if (objectOffset == -1) {
                return UNSAFE.getByte(staticBase, staticOffset);
            } else {
                return UNSAFE.getByte(object, objectOffset);
            }
        }

        public void setByte(Object object, byte value) {
            if (type != Type.BYTE) throw new UnsupportedOperationException("Can not write byte to non-byte field");
            if (objectOffset == -1) {
                UNSAFE.putByte(staticBase, staticOffset, value);
            } else {
                UNSAFE.putByte(object, objectOffset, value);
            }
        }

        public short getShort(Object object) {
            if (type != Type.SHORT) throw new UnsupportedOperationException("Can not read short from non-short field");
            if (objectOffset == -1) {
                return UNSAFE.getShort(staticBase, staticOffset);
            } else {
                return UNSAFE.getShort(object, objectOffset);
            }
        }

        public void setShort(Object object, short value) {
            if (type != Type.SHORT) throw new UnsupportedOperationException("Can not write short to non-short field");
            if (objectOffset == -1) {
                UNSAFE.putShort(staticBase, staticOffset, value);
            } else {
                UNSAFE.putShort(object, objectOffset, value);
            }
        }

        public int getInt(Object object) {
            if (type != Type.INT) throw new UnsupportedOperationException("Can not read int from non-int field");
            if (objectOffset == -1) {
                return UNSAFE.getInt(staticBase, staticOffset);
            } else {
                return UNSAFE.getInt(object, objectOffset);
            }
        }

        public void setInt(Object object, int value) {
            if (type != Type.INT) throw new UnsupportedOperationException("Can not write int to non-int field");
            if (objectOffset == -1) {
                UNSAFE.putInt(staticBase, staticOffset, value);
            } else {
                UNSAFE.putInt(object, objectOffset, value);
            }
        }

        public long getLong(Object object) {
            if (type != Type.LONG) throw new UnsupportedOperationException("Can not read long from non-long field");
            if (objectOffset == -1) {
                return UNSAFE.getLong(staticBase, staticOffset);
            } else {
                return UNSAFE.getLong(object, objectOffset);
            }
        }

        public void setLong(Object object, long value) {
            if (type != Type.LONG) throw new UnsupportedOperationException("Can not write long to non-long field");
            if (objectOffset == -1) {
                UNSAFE.putLong(staticBase, staticOffset, value);
            } else {
                UNSAFE.putLong(object, objectOffset, value);
            }
        }

        public float getFloat(Object object) {
            if (type != Type.FLOAT) throw new UnsupportedOperationException("Can not read float from non-float field");
            if (objectOffset == -1) {
                return UNSAFE.getFloat(staticBase, staticOffset);
            } else {
                return UNSAFE.getFloat(object, objectOffset);
            }
        }

        public void setFloat(Object object, float value) {
            if (type != Type.FLOAT) throw new UnsupportedOperationException("Can not write float to non-float field");
            if (objectOffset == -1) {
                UNSAFE.putFloat(staticBase, staticOffset, value);
            } else {
                UNSAFE.putFloat(object, objectOffset, value);
            }
        }

        public double getDouble(Object object) {
            if (type != Type.DOUBLE)
                throw new UnsupportedOperationException("Can not read double from non-double field");
            if (objectOffset == -1) {
                return UNSAFE.getDouble(staticBase, staticOffset);
            } else {
                return UNSAFE.getDouble(object, objectOffset);
            }
        }

        public void setDouble(Object object, double value) {
            if (type != Type.DOUBLE)
                throw new UnsupportedOperationException("Can not write double to non-double field");
            if (objectOffset == -1) {
                UNSAFE.putDouble(staticBase, staticOffset, value);
            } else {
                UNSAFE.putDouble(object, objectOffset, value);
            }
        }

        public char getChar(Object object) {
            if (type != Type.CHAR) throw new UnsupportedOperationException("Can not read char from non-char field");
            if (objectOffset == -1) {
                return UNSAFE.getChar(staticBase, staticOffset);
            } else {
                return UNSAFE.getChar(object, objectOffset);
            }
        }

        public void setChar(Object object, char value) {
            if (type != Type.CHAR) throw new UnsupportedOperationException("Can not write char to non-char field");
            if (objectOffset == -1) {
                UNSAFE.putChar(staticBase, staticOffset, value);
            } else {
                UNSAFE.putChar(object, objectOffset, value);
            }
        }

        public boolean getBoolean(Object object) {
            if (type != Type.BOOLEAN)
                throw new UnsupportedOperationException("Can not read boolean from non-boolean field");
            if (objectOffset == -1) {
                return UNSAFE.getBoolean(staticBase, staticOffset);
            } else {
                return UNSAFE.getBoolean(object, objectOffset);
            }
        }

        public void setBoolean(Object object, boolean value) {
            if (type != Type.BOOLEAN)
                throw new UnsupportedOperationException("Can not write boolean to non-boolean field");
            if (objectOffset == -1) {
                UNSAFE.putBoolean(staticBase, staticOffset, value);
            } else {
                UNSAFE.putBoolean(object, objectOffset, value);
            }
        }

        private enum Type {
            OBJECT, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, CHAR, BOOLEAN
        }
    }
}
