package jackreuter.rockdrop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;
    TimerTask displayCurrentTime;
    TextView timerView;
    TextView averageHeightView;
    LinearLayout linearLayout;

    long startTime;
    double dropTime;
    double averageHeight;
    ArrayList<Double> distances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        distances = new ArrayList<Double>();
        linearLayout = (LinearLayout) findViewById(R.id.layout);
        timerView = (TextView) findViewById(R.id.timerView);
        averageHeightView = (TextView) findViewById(R.id.averageHeight);

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
                        dropTime = round((System.currentTimeMillis()-startTime)*.001,2);
                        timerView.setText(dropTime+"");
                    }
                });
            }
        };
        timer.schedule(displayCurrentTime, 10, 10);
    }

    //on stop click
    public void stopTimer(View view) {
        double distance = round(calculateDistance(dropTime),2);
        distances.add(distance);
        averageHeight = round(getAverageDistance(distances),2);

        //update timerView in UIthread -- don't fully understand threads but this works
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timerView.setText(dropTime+"");
                averageHeightView.setText("Est. height: "+averageHeight);
            }
        });

        TextView t = new TextView(this);
        t.setText(dropTime+"");
        linearLayout.addView(t);

        timer.cancel();
    }

    public static double calculateDistance(double time) {
        return 0.5*9.8*time*time;
    }

    public static double getAverageDistance(ArrayList<Double> ds) {
        double total = 0;
        double count = 0;
        for (double d:ds) {
            total += d;
            count += 1;
        }
        return total/count;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
