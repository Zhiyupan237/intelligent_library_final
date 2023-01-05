package com.example.intelligent_library.ui.returnbook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReturnbookViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ReturnbookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is returnbook fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}