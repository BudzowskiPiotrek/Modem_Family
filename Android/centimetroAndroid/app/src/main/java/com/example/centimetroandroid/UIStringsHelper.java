package com.example.centimetroandroid;

import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UIStringsHelper {

    public interface StringsLoadedListener {
        void onStringsLoaded();

        void onError(Exception e);
    }

    public static void loadCommonStrings(FirebaseFirestore db, final TextView tvTitle, final Button btnBack,
            final StringsLoadedListener listener, LanguageManager languageManager) {
        String collection = languageManager.getUIStringsCollection();

        db.collection(collection).document("common")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                String btnBackText = document.getString("btn_back");

                                if (btnBack != null && btnBackText != null) {
                                    btnBack.setText(btnBackText);
                                }

                                if (listener != null) {
                                    listener.onStringsLoaded();
                                }
                            }
                        } else if (listener != null) {
                            listener.onError(task.getException());
                        }
                    }
                });
    }
}
