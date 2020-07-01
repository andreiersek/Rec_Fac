package com.ersek.opensesame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class DoorController extends AppCompatActivity {
    public static boolean OPEN_DOOR = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.door_controller);

        Button openDoorButton = findViewById(R.id.doorControllerButton);
        openDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new DoorControlTCP().execute(2).get();
                    new DoorControlTCP().execute(1).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new DoorControlTCP().execute(1).get();
                    if(OPEN_DOOR) {
                        TextView textView = findViewById(R.id.infoTextView);
                        textView.setText(R.string.open_info);
                    } else {
                        TextView textView = findViewById(R.id.infoTextView);
                        textView.setText(R.string.closed_info);
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            new DoorControlTCP().execute(2).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(OPEN_DOOR) {
            TextView textView = findViewById(R.id.infoTextView);
            textView.setText(R.string.open_info);
        } else {
            TextView textView = findViewById(R.id.infoTextView);
            textView.setText(R.string.closed_info);
        }

    }
}
