# Reely

Reely is an Android application that helps users plan short-form
video content, such as Instagram Reels, TikTok videos, and
YouTube Shorts.

Users choose a category, enter a topic, and select a style.
Reely then creates a filming guide, caption, and suggested
hashtags using predefined Java templates.

## Features

- Five categories: Gym, Food, Gaming, Study, and General
- Topic input with validation
- Five styles: Motivational, Funny, Informative, Aesthetic,
  and Storytime
- Suggested filming guide with an opening, main clips,
  ending, and presentation tip
- Template-based captions
- Category-based hashtag suggestions
- Return to the Planner to edit the topic or style
- Share the full plan through Android's sharing interface

## Screens

1. MainActivity: choose a content category.
2. PlannerActivity: enter a topic and select a style.
3. ResultActivity: view and share the generated plan.

## Technologies

- Java for application logic
- XML for screen layouts
- Gradle with Kotlin DSL for build configuration
- Android Studio

## Android Concepts Demonstrated

- Explicit intents for navigation between Activities
- Intent extras to pass category, topic, and style
- An implicit ACTION_SEND intent for sharing text
- Button click listeners
- A Spinner for selecting a style
- Empty-topic validation

## Running the Project

1. Open the project root folder in Android Studio.
2. Allow Gradle to sync.
3. Install any required SDK components if prompted.
4. Select an emulator or connected Android device.
5. Run the app module.

## Current Limitations

- Content generation uses local templates, not AI.
- Hashtags are predefined suggestions, not live trends.
- The app does not record, edit, or upload videos.
- Generated plans are not saved as permanent history.

## Milestone 1

This prototype demonstrates three functional layouts,
user interaction, explicit intents, and an implicit intent.~~~~