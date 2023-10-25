package com.efesen.recyclerviewlikepinterest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.efesen.recyclerviewlikepinterest.adapter.ItemAdapter
import com.efesen.recyclerviewlikepinterest.databinding.ActivityMainBinding
import com.efesen.recyclerviewlikepinterest.model.ApiError
import com.efesen.recyclerviewlikepinterest.model.ApiResponse
import com.efesen.recyclerviewlikepinterest.model.ImageItem
import com.efesen.recyclerviewlikepinterest.service.PixabayApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val apiKey = "Your Api Key"
    private val baseUrl = "https://pixabay.com/api/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchImagesFromPixabay {
            val adapter = ItemAdapter(it, this)

            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS

            binding.recyclerview.layoutManager = layoutManager
            binding.recyclerview.adapter = adapter
        }

    }

    private fun fetchImagesFromPixabay(completionHandler: (list: MutableList<ImageItem>) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(PixabayApiService::class.java)

        val call = service.searchImages(apiKey, "landscape", 200, "all", true)

        val resultList = mutableListOf<ImageItem>()

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val images = apiResponse?.hits ?: emptyList()

                    // Add images from Pixabay to imageList
                    for (image in images) {
                        val imageItem = ImageItem(
                            id = image.id,
                            previewURL = image.previewURL,
                            webformatURL = image.webformatURL,
                            largeImageURL = image.largeImageURL
                        )
                        resultList.add(imageItem)
                        completionHandler.invoke(resultList)
                    }
                } else {
                    val errorBody = response.errorBody()
                    val error = Gson().fromJson(errorBody?.string(), ApiError::class.java)
                    val errorMessage = "Error Code: ${error.code}, Error Message: ${error.message}"
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    completionHandler.invoke(resultList)
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message.toString(), Toast.LENGTH_SHORT).show()
                completionHandler.invoke(resultList)
            }
        })
    }
}