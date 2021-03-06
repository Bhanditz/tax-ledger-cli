/*
 * Copyright (c) 2018 Stanislaw stasbar Baranski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *          __             __
 *    _____/ /_____ ______/ /_  ____ ______
 *   / ___/ __/ __ `/ ___/ __ \/ __ `/ ___/
 *  (__  ) /_/ /_/ (__  ) /_/ / /_/ / /
 * /____/\__/\__,_/____/_.___/\__,_/_/
 *            taxledger@stasbar.com
 */

package com.stasbar.taxledger

import com.stasbar.taxledger.models.Credential
import org.junit.Test
import java.util.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class MainKtTest {

    @Test
    fun test_parseCredentials() {
        val args = ArrayDeque<String>()
        args.add("bb")
        args.add("fe0d3cd1-6cee-46f0-8c73-512304c40ff9")
        args.add("706c560d-bb31-42ea-8137-01232522d413")
        injectArgs(args)
        parseCredentials()

        val lines = PreferencesManager.load()
        assertTrue { lines.contains("bitbay") }
        assertTrue { lines.contains("fe0d3cd1-6cee-46f0-8c73-512304c40ff9") }
        assertTrue { lines.contains("706c560d-bb31-42ea-8137-01232522d413") }
    }


    @Test
    fun test_isExchangeName() {
        assertTrue { isExchangeName("bb") }
        assertTrue { isExchangeName("bitbay") }
        assertTrue { isExchangeName("BITBAY") }
        assertTrue { isExchangeName("abu") }
        assertTrue { isExchangeName("ABUCOINS") }
        assertTrue { isExchangeName("AbU") }
        assertFalse { isExchangeName("bbb") }
        assertFalse { isExchangeName("b") }
        assertFalse { isExchangeName("ab") }
    }

    @Test
    fun test_exchangeByName() {

        assertTrue { exchangeByName("bb") == BitBay::class }
        assertTrue { exchangeByName("bitbay") == BitBay::class }
        assertTrue { exchangeByName("BITBAY") == BitBay::class }
        assertTrue { exchangeByName("abu") == Abucoins::class }
        assertTrue { exchangeByName("ABUCOINS") == Abucoins::class }
        assertTrue { exchangeByName("AbU") == Abucoins::class }
    }

    @Test(expected = IllegalStateException::class)
    fun test_failAddCredentialTooShort() {
        val credential = Credential("publicKey", 32)
        credential.value = "123"
    }

    @Test(expected = IllegalStateException::class)
    fun test_failAddCredentialEmpty() {
        val credential = Credential("publicKey", 32)
        credential.value = ""
    }

    @Test(expected = IllegalStateException::class)
    fun test_failAddCredentialTooLong() {
        val credential = Credential("publicKey", 32)
        credential.value = "x".repeat(33)
    }

}