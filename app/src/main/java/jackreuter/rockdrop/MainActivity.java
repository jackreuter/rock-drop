package jackreuter.rockdrop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;
    TimerTask displayCurrentTime;
    TextView timerView;

    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerView = (TextView) findViewById(R.id.timerView);

        final Button startStop = (Button) findViewById(R.id.button);
        startStop.setTag(1);
        startStop.setText("start");
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                final int status =(Integer) v.getTag();
                if(status == 1) {
                    startTimer(v);
                    startStop.setText("stop");
                    v.setTag(0);
                } else {
                    stopTimer(v);
                    startStop.setText("start");
                    v.setTag(1);
                }
            }
        });

    }

    //on start click
    public void startTimer(View view) {
        startTime = System.currentTimeMillis();
        timer = new Timer();
        displayCurrentTime = new TimerTask() {
            @Override
            public void run() {
                //update timerView in UIthread -- don't fully understand threads but this works
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerView.setText((System.currentTimeMillis()-startTime)*.001+"");
                    }
                });
            }
        };
        timer.schedule(displayCurrentTime, 10, 10);
    }

    //on stop click
    public void stopTimer(View view) {
        timer.cancel();
    }
}
