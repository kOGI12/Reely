package com.example.reely;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    private String fullPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.resultRoot),
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

        // Read the information sent by the Planner screen.
        String category = readExtra("CATEGORY", "General");
        String topic = readExtra("TOPIC", "My next video");
        String style = readExtra("STYLE", "Informative");

        String hook = createHook(topic, style);
        String shots = createShotList(category, topic);
        String ending = createEnding(category);
        String caption = createCaption(topic, style, ending);
        String hashtags = createHashtags(category);

        String guide =
                "Suggested length: about 20–30 seconds\n\n"
                        + "OPENING — 0–3 seconds\n"
                        + "Say or display:\n\"" + hook + "\"\n\n"
                        + "MAIN CLIPS — 3–23 seconds\n"
                        + shots + "\n\n"
                        + "ENDING — 23–30 seconds\n"
                        + ending + "\n\n"
                        + "STYLE TIP\n"
                        + createStyleTip(style);

        TextView tvSummary = findViewById(R.id.tvSummary);
        TextView tvGuide = findViewById(R.id.tvGuide);
        TextView tvCaption = findViewById(R.id.tvCaption);
        TextView tvHashtags = findViewById(R.id.tvHashtags);

        tvSummary.setText(category + " • " + style + "\n" + topic);
        tvGuide.setText(guide);
        tvCaption.setText(caption);
        tvHashtags.setText(hashtags);

        fullPlan =
                "REELY — REEL PLAN\n\n"
                        + "Category: " + category + "\n"
                        + "Topic: " + topic + "\n"
                        + "Style: " + style + "\n\n"
                        + "FILMING GUIDE\n" + guide + "\n\n"
                        + "CAPTION\n" + caption + "\n\n"
                        + "SUGGESTED HASHTAGS\n" + hashtags;

        findViewById(R.id.btnShare).setOnClickListener(
                view -> sharePlan()
        );

        // Close this screen and return to the existing Planner.
        findViewById(R.id.btnEdit).setOnClickListener(
                view -> finish()
        );
        findViewById(R.id.btnOpenFilmingAssistant).setOnClickListener(
                view -> {
                    Intent intent = new Intent(
                            ResultActivity.this,
                            FilmingAssistantActivity.class
                    );

                    intent.putExtra(
                            "TOPIC",
                            getIntent().getStringExtra("TOPIC")
                    );

                    startActivity(intent);
                }
        );
    }

    private String readExtra(String key, String fallback) {
        String value = getIntent().getStringExtra(key);

        if (value == null || value.trim().isEmpty()) {
            return fallback;
        }

        return value.trim();
    }

    private String createHook(String topic, String style) {
        switch (style) {
            case "Motivational":
                return "Making progress with " + topic
                        + " — one step at a time.";

            case "Funny":
                return "POV: you thought " + topic
                        + " would be easy.";

            case "Aesthetic":
                return "A closer look at " + topic + ".";

            case "Storytime":
                return "Here is what happened when I tried "
                        + topic + ".";

            default:
                return "Here are three things to know about "
                        + topic + ".";
        }
    }

    private String createShotList(String category, String topic) {
        switch (category) {
            case "Gym":
                return "1. Introduce \"" + topic
                        + "\" with a setup or equipment shot.\n"
                        + "2. Show a short demonstration relevant "
                        + "to your topic.\n"
                        + "3. Add a second angle and one useful "
                        + "tip as text.\n"
                        + "4. Finish with your result, takeaway "
                        + "or progress.";

            case "Food":
                return "1. Show the food or ingredients for \""
                        + topic + "\".\n"
                        + "2. Film the key preparation steps.\n"
                        + "3. Add quantities or helpful details "
                        + "as text.\n"
                        + "4. Show the finished dish and your "
                        + "reaction.";

            case "Gaming":
                return "1. Introduce \"" + topic
                        + "\" with a relevant gameplay clip.\n"
                        + "2. Show a highlight or demonstrate "
                        + "your main point.\n"
                        + "3. Add commentary or a short reaction.\n"
                        + "4. End with the result or your verdict.";

            case "Study":
                return "1. Introduce \"" + topic
                        + "\" with a desk or notes shot.\n"
                        + "2. Demonstrate your method or routine.\n"
                        + "3. Add one practical tip as text.\n"
                        + "4. Show the outcome or summarise "
                        + "what you learned.";

            default:
                return "1. Show a clip that introduces \""
                        + topic + "\".\n"
                        + "2. Demonstrate your first main point.\n"
                        + "3. Add another example or useful detail.\n"
                        + "4. Finish with a clear takeaway.";
        }
    }

    private String createEnding(String category) {
        switch (category) {
            case "Gym":
                return "Save this for your next gym session.";

            case "Food":
                return "Save this idea for your next meal.";

            case "Gaming":
                return "What is your take? Leave a comment.";

            case "Study":
                return "Save this for your next study session.";

            default:
                return "Save this idea and share your thoughts.";
        }
    }

    private String createCaption(
            String topic,
            String style,
            String ending
    ) {
        switch (style) {
            case "Motivational":
                return topic
                        + " — small steps, steady progress.\n"
                        + ending;

            case "Funny":
                return topic
                        + " — expectations versus reality.\n"
                        + "Please tell me I am not the only one.\n"
                        + ending;

            case "Aesthetic":
                return "A few favourite moments from "
                        + topic + ".\n" + ending;

            case "Storytime":
                return "The story behind " + topic
                        + " — here is what I learned.\n"
                        + ending;

            default:
                return "A quick guide to " + topic
                        + " with a few useful takeaways.\n"
                        + ending;
        }
    }

    private String createHashtags(String category) {
        switch (category) {
            case "Gym":
                return "#Gym #Fitness #Training "
                        + "#Workout #GymRoutine";

            case "Food":
                return "#Food #Cooking #Recipes "
                        + "#MealIdeas #FoodCreator";

            case "Gaming":
                return "#Gaming #Gameplay #Gamer "
                        + "#GamingClips #GameHighlights";

            case "Study":
                return "#Study #College #StudyTips "
                        + "#StudentLife #Productivity";

            default:
                return "#ContentCreator #Reels "
                        + "#ShortVideo #CreativeIdeas";
        }
    }

    private String createStyleTip(String style) {
        switch (style) {
            case "Motivational":
                return "Show a challenge, the effort involved, "
                        + "and a realistic step forward.";

            case "Funny":
                return "Set up an expectation, then reveal "
                        + "the unexpected result.";

            case "Aesthetic":
                return "Use consistent lighting, close-ups "
                        + "and a simple visual theme.";

            case "Storytime":
                return "Explain the situation, what happened, "
                        + "and what you learned.";

            default:
                return "Make three clear points and use "
                        + "short on-screen labels.";
        }
    }

    private void sharePlan() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, fullPlan);

        try {
            startActivity(
                    Intent.createChooser(
                            shareIntent,
                            "Share your Reely plan"
                    )
            );
        } catch (ActivityNotFoundException exception) {
            Toast.makeText(
                    this,
                    "No compatible sharing app is available",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}