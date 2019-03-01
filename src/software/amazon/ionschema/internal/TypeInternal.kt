package software.amazon.ionschema.internal

import software.amazon.ion.IonValue
import software.amazon.ionschema.Type

internal interface TypeInternal : Type {
    fun getBaseType(): TypeBuiltin

    fun isValidForBaseType(value: IonValue): Boolean
}

