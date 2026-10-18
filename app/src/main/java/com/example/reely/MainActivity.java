package com.example.reely;


import android.content.Intent;
import android.os.Bundle;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Keep the screen content clear of the phone's system bars.
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, windowInsets) -> {
                    Insets systemBars = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return windowInsets;
                }
        );

        // Each button passes its category to the same method.
        findViewById(R.id.btnGym).setOnClickListener(
                view -> selectCategory("Gym")
        );

        findViewById(R.id.btnFood).setOnClickListener(
                view -> selectCategory("Food")
        );

        findViewById(R.id.btnGaming).setOnClickListener(
                view -> selectCategory("Gaming")
        );

        findViewById(R.id.btnStudy).setOnClickListener(
                view -> selectCategory("Study")
        );

        findViewById(R.id.btnGeneral).setOnClickListener(
                view -> selectCategory("General")
        );
    }

    private void selectCategory(String category) {
        Intent intent = new Intent(
                MainActivity.this,
                PlannerActivity.class
        );

        intent.putExtra("CATEGORY", category);
        startActivity(intent);
    }
}