package com.example.intelligent_library.ui.borrow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BorrowViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BorrowViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is borrow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}