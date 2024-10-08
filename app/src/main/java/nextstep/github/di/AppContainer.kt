package nextstep.github.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import nextstep.github.data.GithubRepository
import nextstep.github.data.GithubRepositoryImpl
import nextstep.github.data.remote.GithubRemoteDataSource
import nextstep.github.data.remote.GithubRemoteDataSourceImpl
import nextstep.github.data.remote.api.GithubService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class AppContainer {

    private val serialization = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient
                .Builder()
                .build()
        )
        .addConverterFactory(serialization.asConverterFactory(CONTENT_TYPE.toMediaType()))
        .build()

    private val githubService = retrofit.create(GithubService::class.java)

    private val githubRemoteDataSource: GithubRemoteDataSource = GithubRemoteDataSourceImpl(githubService)

    val githubRepository: GithubRepository = GithubRepositoryImpl(githubRemoteDataSource)

    companion object {
        private const val CONTENT_TYPE = "application/json"
        private const val BASE_URL = "https://api.github.com"
    }
}
