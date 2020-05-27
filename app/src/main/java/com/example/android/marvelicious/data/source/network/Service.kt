package com.example.android.marvelicious.data.source.network

import com.example.android.marvelicious.BuildConfig
import com.example.android.marvelicious.util.Crypto
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import timber.log.Timber


interface MarveliciousService {
    @GET("/v1/public/characters")
    fun getAllCharactersAsync(
        @Query("orderBy") orderBy: String = "name",
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): Deferred<CharacterDataWrapper>

    @GET("/v1/public/characters/{characterId}")
    fun getCharacter(
        @Path("characterId") characterId: Int
    ): Deferred<CharacterDataWrapper>

    companion object {
        private const val BASE_URL = "https://gateway.marvel.com/"

        fun create(): MarveliciousService = create(BASE_URL.toHttpUrlOrNull()!!)
        fun create(httpUrl: HttpUrl): MarveliciousService {

            val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Timber.tag("OkHttp").d(message)
                }
            })

            logging.level = HttpLoggingInterceptor.Level.BASIC

            val paramsInterceptor = object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val timeStamp = System.currentTimeMillis() / 1000
                    val publicKey = BuildConfig.MarvelPublicApiKey
                    val privateKey = BuildConfig.MarvelPrivateApiKey
                    val md5Digest = Crypto.md5(timeStamp.toString() + privateKey + publicKey)

                    val url = chain.request().url.newBuilder()
                        .addQueryParameter("apikey", publicKey)
                        .addQueryParameter("ts", timeStamp.toString())
                        .addQueryParameter("hash", md5Digest)
                        .build()
                    val request = chain.request().newBuilder()
                        // .addHeader("Authorization", "Bearer token")
                        .url(url)
                        .build()
                    return chain.proceed(request)
                }
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(paramsInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(MarveliciousService::class.java)
        }
    }
}