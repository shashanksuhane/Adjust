package com.suhane.adjust;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.suhane.lib_core.utils.TimeUtils;
import com.suhane.lib_core.result.Result;

public class MainActivity extends AppCompatActivity {

    TextView logTextView;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = findViewById(R.id.scrollView);

        logTextView = findViewById(R.id.textview_log);

        Button sendButton = findViewById(R.id.button_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSendButtonClick();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                updateLogText("");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void handleSendButtonClick() {
        StringBuffer sb = new StringBuffer(logTextView.getText());
        long currentTimeInSec = System.currentTimeMillis() / 1000;

        Result result = AdjustApp.getLogAPI().send(currentTimeInSec);
        if (result.isSuccess()) {
            sb.append(TimeUtils.getSecondInAMinute(currentTimeInSec) + "\n");
        } else {
            sb.append(TimeUtils.getSecondInAMinute(currentTimeInSec) + " " + result.getError().getErrorMessage() +"\n");
        }
        updateLogText(sb.toString());
    }

    private void updateLogText(String text) {
        logTextView.setText(text);
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
