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

package com.stasbar.taxledger.exchanges.bitfinex.models

import com.stasbar.taxledger.OperationType
import com.stasbar.taxledger.models.Transaction
import com.stasbar.taxledger.models.Transactionable

class BitfinexHistory(
        val ID: Int, //BitmarketTransaction database id
        val PAIR: String, //Pair (BTCUSD, …)
        val MTS_CREATE: Int, //Execution timestamp
        val ORDER_ID: Int, //Order id
        val EXEC_AMOUNT: Float, //Positive means buy, negative means sell
        val EXEC_PRICE: Float,    //Execution price
        val ORDER_TYPE: String,    //Order type
        val ORDER_PRICE: Float,    //Order price
        val MAKER: Int,    //1 if true, 0 if false
        val FEE: Float,    //Fee
        val FEE_CURRENCY: String    //Fee currency
) : Transactionable {
    override fun toTransaction(): Transaction {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun operationType(): OperationType {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}