package com.shumidub.todoapprealm.firebase;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shumidub.todoapprealm.R;

/**
 *  https://firebase.google.com/docs/auth/android/google-signin
 */

public class AuthenticationUtil {

    private static FirebaseAuth mAuth;

//    public static void buildGoogleSingInOpinions(){
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//    }





    public static void initAuth(){
        mAuth = FirebaseAuth.getInstance();
    }

    public static  FirebaseUser getCurrentUser(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        return currentUser;
    }






}
