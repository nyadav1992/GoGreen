package com.example.gogreen.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gogreen.api.ApiInterface
import com.example.gogreen.api.Response
import com.example.gogreen.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProfileRepo(private val apiInterface: ApiInterface) {
    private lateinit var job: Job
    private var profileLiveData = MutableLiveData<StationInfo>()

    val profileData: LiveData<StationInfo>
        get() {
            return profileLiveData
        }

    fun getProfileInfo(id: String): Job{
        try {
            job = MainScope().launch(Dispatchers.IO) {
                var response: StationInfo
                try {
                    response = apiInterface.getProfileInfo(id)
                    if (response.status){
                        profileLiveData.postValue(response)
                    } else {
                        profileLiveData.postValue(StationInfo(response.message, response.error, null, false, response.value, response.code, response.version))
                    }
                } catch (e: Exception){
                    profileLiveData.postValue(StationInfo("Something went wrong","", null, false))
                }
            }
            return job
        } catch (e: Exception){
            profileLiveData.postValue(StationInfo("Something went wrong","", null, false))
        }
        return job
    }

}