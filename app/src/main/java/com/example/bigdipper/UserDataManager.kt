package com.example.bigdipper

class UserDataManager private constructor() {
    private var userData: UserData? = null

    fun setUserData(data: UserData) {
        userData = data
    }

    fun getUserData(): UserData? {
        return userData
    }

    companion object {
        @Volatile
        private var instance: UserDataManager? = null

        fun getInstance(): UserDataManager {
            return instance ?: synchronized(this) {
                instance ?: UserDataManager().also { instance = it }
            }
        }
    }
}