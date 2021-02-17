package com.amazon.ionschema.internal.constraint


import com.amazon.ion.IonFloat
import com.amazon.ion.IonSymbol
import com.amazon.ion.IonValue
import com.amazon.ionschema.InvalidSchemaException
import com.amazon.ionschema.Violation
import com.amazon.ionschema.Violations
import java.math.BigDecimal

/**
 * Implements the float_size constraint.
 */
internal class FloatSize(
        ion: IonValue
) : ConstraintBase(ion) {

    private val validValues = setOf("float16", "float32", "float64")
    private val maxFloat32Range: BigDecimal = BigDecimal.valueOf(Float.MAX_VALUE.toDouble())
    private val minPosValue: BigDecimal = BigDecimal.valueOf(Float.MIN_VALUE.toDouble())


    init {
        if (!(ion is IonSymbol
                        && !ion.isNullValue
                        && validValues.contains(ion.stringValue()))) {
            throw InvalidSchemaException("Invalid float_size constraint: $ion")
        }

        // float16 not implemented yet
        if (ion.stringValue() == "float16") throw InvalidSchemaException("\'float16\' is not currently supported")
    }


    override fun validate(value: IonValue, issues: Violations) {
        validateAs<IonFloat>(value, issues) {
            when (ion.toString()) {
                "float16" -> throw InvalidSchemaException("\'float16\' is not currently supported")
                "float32" -> if (!fitsFloat32(it)) {
                    issues.add(Violation(ion, "insufficient_float_size", "value does not fit float32 size"))
                }
                "float64" -> Unit
            }
        }
    }


    private fun fitsFloat32(value: IonFloat): Boolean {
        if (value.isNumericValue) {
            val absValue = value.bigDecimalValue().abs()
            if (absValue != BigDecimal.ZERO) {
                return (maxFloat32Range >= absValue)
                        && (minPosValue <= absValue)
            }
        }
        return true
    }
}