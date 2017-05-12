package androidsupersquad.rocketfrenzy.MiniGame.AccGame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidsupersquad.rocketfrenzy.Data.RocketData;
import androidsupersquad.rocketfrenzy.DataBase.RocketContentProvider;
import androidsupersquad.rocketfrenzy.DataBase.RocketDB;
import androidsupersquad.rocketfrenzy.R;

/**
 * Jimmy Chao
 * 012677182
 */
public class AccGame extends AppCompatActivity  {
    private static final String TAG="androidsupersquad.rocketfrenzy.MiniGame.AccGame.AccGame";
    private PowerManager.WakeLock mWakeLock;
    private SimulationView simulationView;
    CountDownTimer timers;
    private TextView counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accgame);
        //set wake lock
        PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        //register/unregister sensor listener
        simulationView=(SimulationView) findViewById(R.id.view);
        simulationView.freeze();
        counter = (TextView) findViewById(R.id.AsteroidCounter);
        ImageButton close = (ImageButton) findViewById(R.id.AccGameCloseButton);
        final TextView gameOver = (TextView) findViewById(R.id.accgameover);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gameOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameOver.setVisibility(View.INVISIBLE);
                simulationView.unFreeze();
                timers = new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        Long timeleft = (millisUntilFinished / 1000);
                        counter.setText(timeleft + "");
                    }

                    public void onFinish() {
                        counter.setText(0 + "");
                        gameOver.setText("GAME OVER");
                        gameOver.setClickable(false);
                        simulationView.freeze();
                        gameOver.setVisibility(View.VISIBLE);
                        int score = simulationView.getScore();
                        updatePlayerCoinAmount(getPlayerName(), score * 100, false);
                    }
                }.start();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //get wakelock
        mWakeLock.acquire();
        //set listener
        simulationView.startSimulation();

    }
    @Override
    protected void onPause()
    {
        super.onPause();
        //release wakelock
        mWakeLock.release();
        //unregister listener
        simulationView.stopSimulation();
    }

    private String getPlayerName()
    {
        Cursor cursor = getContentResolver().query(RocketContentProvider.CONTENT_URI, null, null, null, null);
        int username = cursor.getColumnIndex(RocketDB.USER_NAME_COLUMN);
        cursor.moveToFirst();
        //still kind of testing//
        String name = cursor.getString(username);

        Log.d("PLAYER_NAME_INFO", "Username: " + name);
        return name;
    }

    private int updatePlayerCoinAmount(String playerName, int coinAmount, boolean set)
    {
        String whereClause = RocketDB.USER_NAME_COLUMN + "= ?";
        String[] whereArgs = {playerName};
        int newCoinAmount = 0;
        ContentValues newValues = new ContentValues();
        if(set) {
            newCoinAmount = coinAmount;
        } else {
            int currentCoins = getPlayerCoinAmount(playerName);
            newCoinAmount = currentCoins + coinAmount;
        }
        newValues.put(RocketDB.COIN_AMOUNT_COLUMN, newCoinAmount);
        return getContentResolver().update(RocketContentProvider.CONTENT_URI, newValues, whereClause, whereArgs);
    }

    private int getPlayerCoinAmount(String playerName)
    {
        String where = RocketDB.USER_NAME_COLUMN + "= ?";
        String whereArgs[] = {playerName};
        String[] resultColumns = {RocketDB.COIN_AMOUNT_COLUMN};
        Cursor cursor = getContentResolver().query(RocketContentProvider.CONTENT_URI, resultColumns, where, whereArgs, null);
        int coin = cursor.getColumnIndex(RocketDB.COIN_AMOUNT_COLUMN);
        cursor.moveToFirst();
        int coinAmount = cursor.getInt(coin);
        Log.d("COIN_INFO", "Username: " + playerName + "\nCoin amount: " + coinAmount);
        return coinAmount;
    }
}