package com.example.reely;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PlannerActivity extends AppCompatActivity {

    private EditText etTopic;
    private Spinner spinnerStyle;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_planner);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.plannerRoot),
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

        TextView tvCategory = findViewById(R.id.tvCategory);
        etTopic = findViewById(R.id.etTopic);
        spinnerStyle = findViewById(R.id.spinnerStyle);

        // Read the category sent by the Home screen.
        category = getIntent().getStringExtra("CATEGORY");

        if (category == null || category.trim().isEmpty()) {
            category = "General";
        }

        tvCategory.setText("Category: " + category);

        String[] styles = {
                "Motivational",
                "Funny",
                "Informative",
                "Aesthetic",
                "Storytime"
        };

        // Provide the dropdown options and readable colours.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                styles
        ) {
            @Override
            public View getView(
                    int position,
                    View convertView,
                    ViewGroup parent
            ) {
                View view = super.getView(position, convertView, parent);

                TextView text = (TextView) view;
                text.setTextColor(Color.WHITE);
                text.setTextSize(18);

                return view;
            }

            @Override
            public View getDropDownView(
                    int position,
                    View convertView,
                    ViewGroup parent
            ) {
                View view = super.getDropDownView(
                        position,
                        convertView,
                        parent
                );

                TextView text = (TextView) view;
                text.setTextColor(Color.WHITE);
                text.setBackgroundColor(Color.rgb(32, 35, 44));
                text.setPadding(24, 24, 24, 24);

                return view;
            }
        };

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerStyle.setAdapter(adapter);

        findViewById(R.id.btnGenerate).setOnClickListener(
                view -> generatePlan()
        );

        findViewById(R.id.btnBack).setOnClickListener(
                view -> finish()
        );
    }

    private void generatePlan() {
        String topic = etTopic.getText().toString().trim();

        if (topic.isEmpty()) {
            etTopic.setError("Please enter a reel topic");
            etTopic.requestFocus();
            return;
        }

        String style = spinnerStyle.getSelectedItem().toString();

        Intent intent = new Intent(
                PlannerActivity.this,
                ResultActivity.class
        );

        intent.putExtra("CATEGORY", category);
        intent.putExtra("TOPIC", topic);
        intent.putExtra("STYLE", style);

        startActivity(intent);
    }
}