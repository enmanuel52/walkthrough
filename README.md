# Walkthrough
## ⚠⚠ Not ready yet ⚠⚠

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
implementation("io.github.enmanuel52:walkthrough:1.1.1-alpha03")
```


3- You need a list of pages

```
val WALK_STEPS = arrayListOf(
    WalkStepRes(
        R.drawable.ic_explore,
        null,//R.string.explore,
        R.string.explore_description
    ),
    WalkStepRes(
        R.drawable.ic_booking,
        R.string.bookings,
        R.string.bookings_description
    ),
    WalkStepRes(
        imageResource = R.drawable.ic_wizard_payment,
        titleResource = R.string.pay,
        descriptionResource = R.string.pay_description
    ),
    WalkStepRes(
        R.drawable.ic_wizard_waiting_room,
        R.string.waiting_room,
        R.string.waiting_room_description
    ),
    WalkStepRes(
        R.drawable.ic_notifications,
        R.string.notifications,
        R.string.notifications_description
    ),
)

data class WalkStepRes(
    @DrawableRes val imageResource: Int,
    val titleResource: Int? = null,
    val descriptionResource: Int,
) {
    fun toModel(context: Context) = WalkStep(
        imageResource = imageResource,
        title = titleResource?.let { context.getString(it) },
        description = context.getString(descriptionResource),
    )
}
```

4- Then just add it, you have a callback when the walk is ended:

```
WalkThrough(
    steps = WALK_STEPS.map { it.toModel(context) },
    pagerState = pagerState,
    modifier = Modifier.padding(paddingValues),
    bottomButton = {
        Button(
            onClick = {
                scope.launch {
                    if (pagerState.canScrollForward) {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1,
                            animationSpec = tween(500)
                        )
    
                    } else {
                        snackBarHost.showSnackbar("The walk has ended")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(.7f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            AnimatedContent(
                targetState = pagerState.canScrollForward,
                label = "text button animation"
            ) { forward ->
                if (forward) {
                    Text(text = "Next")
                } else {
                    Text(text = "Get started")
                }
            }
        }
    },
    skipButton = {
        SkipButton {
            scope.launch {
                snackBarHost.showSnackbar("The walk has been skipped")
            }
        }
    },
    scrollStyle = WalkScrollStyle.Instagram
)
```

# Issue
```
java.lang.NoSuchMethodError: No static method HorizontalPager-xYaah8o(Landroidx/compose/foundation/pager/PagerState;Landroidx/compose/ui/Modifier;Landroidx/compose/foundation/layout/PaddingValues;Landroidx/compose/foundation/pager/PageSize;IFLandroidx/compose/ui/Alignment$Vertical;Landroidx/compose/foundation/gestures/snapping/SnapFlingBehavior;ZZLkotlin/jvm/functions/Function1;Landroidx/compose/ui/input/nestedscroll/NestedScrollConnection;Lkotlin/jvm/functions/Function4;Landroidx/compose/runtime/Composer;III)V in class Landroidx/compose/foundation/pager/PagerKt; or its super classes (declaration of 'androidx.compose.foundation.pager.PagerKt' appears in /data/app/~~UzU6Y7L0uRQhd6-HVwTvQA==/com.enmanuelbergling.pathpower-JN5gZNmvYBniDk6yzSjaeA==/base.apk)
                                                                                                    	at com.enmanuelbergling.walkthrough.ui.WalkThroughKt$WalkThrough$$inlined$ConstraintLayout$2.invoke(ConstraintLayout.kt:1534)
                                                                                                    	at com.enmanuelbergling.walkthrough.ui.WalkThroughKt$WalkThrough$$inlined$ConstraintLayout$2.invoke(ConstraintLayout.kt:89)

```
