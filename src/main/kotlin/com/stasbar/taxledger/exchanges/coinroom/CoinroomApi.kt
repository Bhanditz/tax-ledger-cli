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

package com.stasbar.taxledger.exchanges.coinroom

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



import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stasbar.taxledger.Constants.DATE_FORMAT
import com.stasbar.taxledger.DEBUG
import com.stasbar.taxledger.ExchangeApi
import com.stasbar.taxledger.exchanges.coinroom.models.CoinroomOrdersResponse
import com.stasbar.taxledger.exchanges.coinroom.requests.CoinroomOrdersRequest
import com.stasbar.taxledger.models.Credential
import com.stasbar.taxledger.models.Transactionable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface PrivateService {

    /**
     * history of all operations on user account
     *
     * @param imit int (formData)
     * @param page int (formData)
     * @param realCurrency string (formData)
     * @param cryptoCurrency string (formData)
     * @param status string (formData)
     * @param type string (formData)
     * @param sign * string(formData)
     */
    @FormUrlEncoded
    @POST("orders")
    fun orders(@FieldMap(encoded = true) fields: Map<String, String>): Call<CoinroomOrdersResponse>

}

class CoinroomApi(credentials: HashSet<Credential>, private val gson: Gson)
    : ExchangeApi<Transactionable, Transactionable> {
    private val publicKey: String = credentials.first { it.name == "publicKey" }.value
    private val privateKey: String = credentials.first { it.name == "privateKey" }.value

    override val URL = "https://coinroom.com/api/"

    override val service: Lazy<PrivateService> = lazy {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = if (DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val httpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(CoinroomHeaderInterceptor(publicKey))
                .addNetworkInterceptor(logInterceptor)
                .build()

        val gson = GsonBuilder().setDateFormat(DATE_FORMAT).create()
        val retrofit = Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(URL)
                .build()
        retrofit.create(PrivateService::class.java)
    }

    override fun transactions(): List<Transactionable> {
        val request = CoinroomOrdersRequest()
        val response = service.value.orders(request.toMap(privateKey)).execute()
        return if (response.isSuccessful && response.body() != null && response.body()!!.result)
            response.body()?.data ?: ArrayList()
        else {
            println("Unsuccessfully fetched transactions error code: ${response.code()} body: ${response.errorBody()} ")
            emptyList()
        }


    }


}