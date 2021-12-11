package com.as.Jetpakpakvf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.EventsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private boolean isMute;
    private static final String TAG = "JetPackPack";
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_UNUSED = 5001;

    private GoogleSignInClient googleSignInClient;
    private AchievementsClient achievementClient;
    private LeaderboardsClient leaderboardsClient;


    private GoogleSignInClient mGoogleSignInClient;
    private AchievementsClient mAchievementsClient;
    private LeaderboardsClient mLeaderboardsClient;
    private EventsClient mEventsClient;
    private PlayersClient mPlayersClient;

    private String mDisplayName = "";

    private final AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();

    private TextView mGreetingTextView;
    private String mGreeting = "Hello, anonymous user (not signed in)";
    private ImageView msignin, signout, mShowAchievementsButton, mShowLeaderboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });


         msignin = findViewById(R.id.signin);
        msignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInIntent();
            }
        });
         signout = findViewById(R.id.signOut);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

         mShowAchievementsButton = findViewById(R.id.achievementbutton);
         mShowLeaderboardButton = findViewById(R.id.leaderboard);
        mShowLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowLeaderboardsRequested();
            }
        });
        mShowAchievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowAchievementsRequested();
            }
        });




        TextView coinscore = findViewById(R.id.highScoreTxt);
        TextView highScoreTxt = findViewById(R.id.highScoreTxt2);

        final SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);//récupère les informations de gameview

        int highScoreTxto = prefs.getInt("highscore",0);////récupère la donnée Highscore
        highScoreTxt.setText("HighScore: " + highScoreTxto);
        updateLeaderboards(highScoreTxto);
        checkForAchievements(1,highScoreTxto);


        int coininput = prefs.getInt("Coins", 0);

        int coins =+ coininput;
        coinscore.setText("Coinscore: " + coins);

        isMute = prefs.getBoolean("isMute", false);

        final ImageView volumeCtrl = findViewById(R.id.volumeCtrl);


        if (isMute)
            volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
        else
            volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isMute = !isMute;
                if (isMute)
                    volumeCtrl.setImageResource(R.drawable.ic_volume_off_black_24dp);
                else
                    volumeCtrl.setImageResource(R.drawable.ic_volume_up_black_24dp);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();

            }
        });

        mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());


        mGreetingTextView = findViewById(R.id.text_greeting);
    }


    private boolean isSignedIn() {

        return GoogleSignIn.getLastSignedInAccount(this) != null;

    }

    private void signInSilently() { //connexion automatique
        Log.d(TAG, "signInSilently()");


        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInSilently(): success");
                            onConnected(task.getResult());
                        } else {
                            Log.d(TAG, "signInSilently(): failure", task.getException());
                            onDisconnected();
                        }
                    }
                });
    }

    private void startSignInIntent() {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();

        msignin.setVisibility(View.GONE);
        signout.setVisibility(View.VISIBLE);
        mShowLeaderboardButton.setVisibility(View.VISIBLE);
        mShowAchievementsButton.setVisibility(View.VISIBLE);

        Log.d(TAG, "onResume()");

        // Since the state of the signed in user can change when the activity is not active
        // it is recommended to try and sign in silently from when the app resumes.
        signInSilently();
    }

    private void signOut() {//deconnexion
        Log.d(TAG, "signOut()");

        msignin.setVisibility(View.VISIBLE);
        signout.setVisibility(View.GONE);
        mShowAchievementsButton.setVisibility(View.GONE);
        mShowLeaderboardButton.setVisibility(View.GONE);
        if (!isSignedIn()) {
            Log.w(TAG, "signOut() called, but was not signed in!");
            return;
        }

        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        Log.d(TAG, "signOut(): " + (successful ? "success" : "failed"));
                        setGreeting("Hello, guest156598");
                        onDisconnected();
                    }
                });
    }


    private void handleException(Exception e, String details) { // Exception handler
        int status = 0;

        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            status = apiException.getStatusCode();
        }

        String message = getString(R.string.status_exception_error, details, status, e);

        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }


    private void checkForAchievements(int requestedScore, int finalScore) { //vérification des réussites, si condition rempli, push sur google play services
        // Check if each condition is met; if so, unlock the corresponding
        // achievement.

        if (finalScore >= 10) {
            mOutbox.mPrimeAchievement = true;
        }

    }
/*
    private boolean isPrime(int n) {
        int i;
        if (n == 0 || n == 1) {
            return false;
        }
        for (i = 2; i <= n / 2; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
*/
    private void updateLeaderboards(int finalScore) {// update du leaderboard

            mOutbox.mEasyModeScore = finalScore;
    }

    private void achievementToast(String achievement) {
        // Only show toast if not signed in. If signed in, the standard Google Play
        // toasts will appear, so we don't need to show our own.
        if (!isSignedIn()) {
            Toast.makeText(this, getString(R.string.achievement) + ": " + achievement,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void pushAccomplishments() {//le push sur serveur...
        if (!isSignedIn()) {
            // can't push to the cloud, try again later
            return;
        }
        if (mOutbox.mPrimeAchievement) {
            mAchievementsClient.unlock(getString(R.string.achievement_i_am_flying));
            mOutbox.mPrimeAchievement = false;
        }

        if (mOutbox.mEasyModeScore >= 0) {
            mLeaderboardsClient.submitScore(getString(R.string.leaderboard_highscore),
                    mOutbox.mEasyModeScore);
            mOutbox.mEasyModeScore = -1;
        }
    }

    public PlayersClient getPlayersClient() {
        return mPlayersClient;
    }

    public String getDisplayName() {
        return mDisplayName;
    }


    public void onShowAchievementsRequested() {//catch réussites
        onResume();
        mAchievementsClient.getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {//affichage de l'activity réussite design :)
                        startActivityForResult(intent, RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handleException(e, getString(R.string.achievements_exception));
                    }
                });
    }

    public void onShowLeaderboardsRequested() {//catch leaderboard
        onResume();
        mLeaderboardsClient.getAllLeaderboardsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {//idem
                        startActivityForResult(intent, RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handleException(e, getString(R.string.leaderboards_exception));
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }

                onDisconnected();

                new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {//connexion
        Log.d(TAG, "onConnected(): connected to Google APIs");

        mAchievementsClient = Games.getAchievementsClient(this, googleSignInAccount);
        mLeaderboardsClient = Games.getLeaderboardsClient(this, googleSignInAccount);
        mEventsClient = Games.getEventsClient(this, googleSignInAccount);
        mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);

        // Set the greeting appropriately on main menu
        mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<Player>() {
                    @Override
                    public void onComplete(@NonNull Task<Player> task) {
                        String displayName;
                        if (task.isSuccessful()) {
                            displayName = task.getResult().getDisplayName();
                        } else {
                            Exception e = task.getException();
                            handleException(e, getString(R.string.players_exception));
                            displayName = "???";
                        }
                        mDisplayName = displayName;
                        setGreeting("Hello, " + displayName);
                    }
                });


        // if we have accomplishments to push, push them
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(this, getString(R.string.your_progress_will_be_uploaded),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void onDisconnected() {//deconnexion
        Log.d(TAG, "onDisconnected()");

        mAchievementsClient = null;
        mLeaderboardsClient = null;
        mPlayersClient = null;

    }
    public void setGreeting(String greeting) {
        mGreeting = greeting;
        updateUI();
    }


    private void updateUI() {
        mGreetingTextView.setText(mGreeting);
    }

    private class AccomplishmentsOutbox {//sert a push
        boolean mPrimeAchievement = false;
        int mBoredSteps = 0;
        int mEasyModeScore = -1;
        int mHardModeScore = -1;

        boolean isEmpty() {
            return !mPrimeAchievement && mBoredSteps == 0 && mEasyModeScore < 0 &&
                    mHardModeScore < 0;
        }

    }
    }

