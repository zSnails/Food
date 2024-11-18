package com.zsnails.food

import io.github.jan.supabase.auth.user.UserInfo

class State {
    lateinit var user: UserInfo

    companion object {
        private var instance: State? = null

        fun getInstance(): State {
            if (instance == null) {
                instance = State()
            }
            return instance!!
        }
    }
}