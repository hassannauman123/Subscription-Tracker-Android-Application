package com.track_it.Presentation;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.track_it.R;
import android.content.Intent;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    @Override // Make sure this function is overriding some default onCreate method
    protected void onCreate(Bundle savedInstanceState)  // when this crated
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Switch screen to display main page

        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        //Button sections:
        // Target subscription button, and switch to that intent when clicked
        Button button = (Button) findViewById(R.id.button_subscription);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddSubscriptionActivity.class);
                startActivity(intent);

            }
        });
    }

}

