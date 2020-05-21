/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.amazon.ionschema.internal

import com.amazon.ionschema.Import
import com.amazon.ionschema.Schema
import com.amazon.ionschema.Type

/**
 * Implementation of [Import] for all user-provided ISL.
 */
internal class ImportImpl(
        override val id: String,
        private val schema: Schema?,
        private val types: Map<String, Type> = emptyMap()
) : Import {

    override fun getType(name: String) = schema?.getType(name) ?: types[name]

    override fun getTypes() =
            ((schema?.getTypes()?.asSequence() ?: emptySequence())
                    + types.values.asSequence()).iterator()
}

