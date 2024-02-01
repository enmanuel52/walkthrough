# Walkthrough

![walkthrough](https://github.com/enmanuel52/walkthrough/assets/102194318/ff2b052d-c6b3-43a9-8665-7f32c296a5a7)

1- First that all add this in the settings.gradle.kts
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url = uri("https://jitpack.io") }
    }
}
```

2- Second add the dependency in your build.gradle.kts
```
implementation("io.github.enmanuel52:walkthrough:0.0.1")
```


3- You need a list of pages

```
val WALK_STEPS = arrayListOf(
    WalkStep(
        R.drawable.ic_explore,
        R.string.explore,
        R.string.explore_description
    ),
    WalkStep(
        R.drawable.ic_booking,
        R.string.bookings,
        R.string.bookings_description
    ),
    WalkStep(
        R.drawable.ic_wizard_payment,
        R.string.pay,
        R.string.pay_description
    ),
    WalkStep(
        R.drawable.ic_wizard_waiting_room,
        R.string.waiting_room,
        R.string.waiting_room_description
    ),
    WalkStep(
        R.drawable.ic_notifications,
        R.string.notifications,
        R.string.notifications_description
    ),
)
```

4- Then just add it, you have a callback when the walk is ended:

```
WalkThrough(
steps = WALK_STEPS,
modifier = Modifier.padding(paddingValues),
nextButtonText = { ended ->
    Text(
        text = if (ended) "Get started" else "Next",
        modifier = Modifier.padding(vertical = DimenTokens.Small)
    )
},
skipButton = {
    SkipButton {
        scope.launch {
            snackBarHost.showSnackbar("The walk has been skipped")
        }
    }
},
nextButtonVisible = true
) {
    scope.launch {
        snackBarHost.showSnackbar("The walk has ended")
    }
}
```

4- This is how it looks like with differents types of indicators, without the toggle button of course :)



https://github.com/enmanuel52/walkthrough/assets/102194318/ebf9572e-d062-4416-aadd-b67a0bd81534




