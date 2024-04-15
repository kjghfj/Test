package com.test.unsplashdemoapp.ui.detail

import com.test.unsplashdemoapp.models.ImageRoot

sealed class DetailStates {
    class Loading(val isLoading: Boolean): DetailStates()
    class Success(val imageRoot: ImageRoot): DetailStates()
    class Error(val e: Throwable): DetailStates()
}