package com.mobile.todo

import android.app.Application
import com.mobile.todo.data.AppContainer
import com.mobile.todo.data.AppDataContainer

class TodoApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}