# Boosting your Compose Apps

![Compose](https://github.com/enmanuel52/Compose-Boost/assets/102194318/18541363-507d-4ae5-97dc-cadfd7811413)

1- First than all add the dependency in your build.gradle.kts
```
implementation("io.github.enmanuel52:walkthrough:0.0.1-alpha")
```


2- You need a list of pages

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

3- Then just add it, you have a callback when the walk is ended:

```
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHost) }
    ) { paddingValues ->
        Box(Modifier.padding(paddingValues)) {
            WalkThrough(steps = WALK_STEPS, style = style) {
                scope.launch {
                    snackBarHost.showSnackbar(
                        message = "The walk has finished",
                        duration = SnackbarDuration.Indefinite,
                        withDismissAction = true
                    )
                }
            }
        }
    }
```

3- This is how it looks like with differents types of indicators



https://github.com/enmanuel52/Compose-Boost/assets/102194318/eacd1626-784f-4fb3-820f-da247cc516b0
