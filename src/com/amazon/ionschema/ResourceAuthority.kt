package com.amazon.ionschema

import com.amazon.ion.IonValue
import com.amazon.ionschema.internal.IonSchemaSystemImpl
import com.amazon.ionschema.util.CloseableIterator
import java.io.Closeable
import java.io.InputStream

/**
 * An [Authority] implementation that attempts to resolve schema ids to resources
 * in a [ClassLoader]'s classpath.
 *
 * @property[rootPackage] The base path within the [ClassLoader]'s classpath in which
 *     to resolve schema identifiers.
 * @property[classLoader] The [ClassLoader] to use to find the schema resources.
 */
class ResourceAuthority(
    rootPackage: String,
    private val classLoader: ClassLoader
) : Authority {
    private val rootPackage = if (rootPackage.endsWith('/')) rootPackage else "$rootPackage/"

    override fun iteratorFor(iss: IonSchemaSystem, id: String): CloseableIterator<IonValue> {
        val resourceName = "$rootPackage/$id"
        val stream: InputStream = classLoader.getResourceAsStream(resourceName) ?: return EMPTY_ITERATOR

        val ion = (iss as IonSchemaSystemImpl).getIonSystem()
        val reader = ion.newReader(stream)

        return object : CloseableIterator<IonValue>, Iterator<IonValue> by ion.iterate(reader), Closeable by reader {
            // Intentionally empty body because this object has all its methods implemented by delegation.
        }
    }

    companion object {
        /**
         * Factory method for constructing a [ResourceAuthority] that can access the schemas provided by
         * [`ion-schema-schemas`](https://github.com/amzn/ion-schema-schemas/).
         */
        @JvmStatic
        fun forIonSchemaSchemas(): ResourceAuthority = ResourceAuthority("ion-schema-schemas", ResourceAuthority::class.java.classLoader)
    }
}
