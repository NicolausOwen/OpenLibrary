package com.kelompok5.openlibrary.ui.setting;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingViewModel extends ViewModel {

    private final MutableLiveData<String> userName = new MutableLiveData<>("");
    private final MutableLiveData<String> userEmail = new MutableLiveData<>("");

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SettingViewModel() {
        loadUserData();
    }

    private void loadUserData() {
        if (auth.getCurrentUser() == null) return;

        String uid = auth.getCurrentUser().getUid();

        db.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        userName.setValue(doc.getString("name"));
                        userEmail.setValue(doc.getString("email"));
                    }
                });
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserEmail() {
        return userEmail;
    }
}
