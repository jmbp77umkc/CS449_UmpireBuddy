package edu.umkc.jmbp77.umpirebuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected boolean dialogUp = false;
    protected int valueStrike = 0;
    protected int maxStrike = 3;
    protected int valueBall = 0;
    protected int maxBall = 4;
    protected int valueOut = 0;
    protected AlertDialog dialogOut;
    protected AlertDialog dialogWalk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        View v = findViewById(R.id.activity_main);

        dialogOut = ( new AlertDialog.Builder(v.getContext())
                .setTitle("Out!").setNeutralButton("OK",alertOnClickListener)
                .setCancelable(false) ).create();
        dialogWalk = ( new AlertDialog.Builder(v.getContext())
                .setTitle("Walk!").setNeutralButton("OK",alertOnClickListener)
                .setCancelable(false) ).create();

        SharedPreferences sp = getPreferences(0);
        valueStrike = sp.getInt("valueStrike",0);
        valueBall = sp.getInt("valueBall",0);
        valueOut = sp.getInt("valueOut",0);

        ( (TextView)findViewById(R.id.countStrike) ).setText(Integer.toString(valueStrike));
        ( (TextView)findViewById(R.id.countBall) ).setText(Integer.toString(valueBall));
        ( (TextView)findViewById(R.id.countOut) ).setText(Integer.toString(valueOut));

        ( (Button)findViewById(R.id.buttonStrike) ).setOnClickListener(viewOnClickListener);
        ( (Button)findViewById(R.id.buttonBall) ).setOnClickListener(viewOnClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                resetCount();
                valueOut = 0;
                ( (TextView)findViewById(R.id.countOut) ).setText("0");
                return true;
            case R.id.about:
                //show about screen
                Intent intent = new Intent(this,About.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        saveData();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }

    protected void saveData() {
        SharedPreferences sp = getPreferences(0);
        SharedPreferences.Editor e = sp.edit();
        e.putInt("valueStrike",valueStrike);
        e.putInt("valueBall",valueBall);
        e.putInt("valueOut",valueOut);
        e.commit();
    }

    protected void resetCount() {
        valueStrike = valueBall = 0;
        ( (TextView)findViewById(R.id.countStrike) ).setText("0");
        ( (TextView)findViewById(R.id.countBall) ).setText("0");
        dialogUp = false;
    }

    protected void incrementOut() {
        TextView countOut = (TextView) findViewById(R.id.countOut);
        countOut.setText(Integer.toString(++valueOut));
    }

    private View.OnClickListener viewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (!dialogUp) {
                switch (v.getId()) {
                    case R.id.buttonStrike:
                        //DO something
                        TextView countStrike = (TextView) findViewById(R.id.countStrike);
                        if (valueStrike < maxStrike)
                            countStrike.setText(Integer.toString(++valueStrike));
                        if (valueStrike == maxStrike) {
                            dialogUp = true;
                            dialogOut.show();
                        }
                        break;
                    case R.id.buttonBall:
                        //DO something
                        TextView countBall = (TextView) findViewById(R.id.countBall);
                        if (valueBall < maxBall) countBall.setText(Integer.toString(++valueBall));
                        if (valueBall == maxBall) {
                            dialogUp = true;
                            dialogWalk.show();
                        }
                        break;
                }
            }
        }
    };

    private AlertDialog.OnClickListener alertOnClickListener = new AlertDialog.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            resetCount();
            if (dialog == dialogOut) incrementOut();
            dialog.dismiss();
        }
    };
}
