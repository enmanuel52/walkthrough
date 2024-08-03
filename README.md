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
implementation("io.github.enmanuel52:walkthrough:1.1.2")
```


3- You need a list of pages

```
val IMAGES = listOf(
    R.drawable.ic_explore,
    R.drawable.ic_booking,
    R.drawable.ic_wizard_payment,
    R.drawable.ic_wizard_waiting_room,
    R.drawable.ic_notifications,
)

private const val LOREM_IPSUM =
    "Lorem ipsum odor amet, consectetuer adipiscing elit. Scelerisque dis metus parturient viverra enim. Quisque nostra dui metus eget viverra posuere nulla quisque. Auctor senectus blandit eros facilisi parturient risus volutpat curabitur."

val WALK_STEPS = IMAGES.map {
    WalkStep(
        it, description = LOREM_IPSUM
    )
}
```

4- Then just add it, you have a callback when the walk is ended:

```
WalkThrough(
    steps = WALK_STEPS,
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
    scrollStyle = WalkScrollStyle.Instagram(boxAngle = 20f)
)
```

# Instagram Pager
```
InstagramPager(state = pagerState) {index, pageModifier->
    PageUi(index = index, modifier = pageModifier)
}
```


https://github.com/user-attachments/assets/76047091-54de-4bb7-8647-53815568bb43


## Louder effect
```
InstagramPager(state = pagerState, boxAngle = 70) {index, pageModifier->
    PageUi(index = index, modifier = pageModifier)
}
```

https://github.com/user-attachments/assets/760594b5-ab79-403e-acf8-3e5197b7a031


## Reverse
```
InstagramPager(state = pagerState, reverse = true) {index, pageModifier->
    PageUi(index = index, modifier = pageModifier)
}
```


https://github.com/user-attachments/assets/48ca4565-fb44-4729-a80d-345a6d610f28


## Shift indicator
```
ShiftIndicator(pagerState = pagerState, stepSize = 8.dp)
```

https://github.com/user-attachments/assets/817492d5-8106-42b3-8c42-4508742eba98


# Known Issue
_Using compose version 1.7.0 can led to this due compatibility issues_
```
java.lang.NoSuchMethodError: No static method HorizontalPager-xYaah8o(Landroidx/compose/foundation/pager/PagerState;Landroidx/compose/ui/Modifier;Landroidx/compose/foundation/layout/PaddingValues;Landroidx/compose/foundation/pager/PageSize;IFLandroidx/compose/ui/Alignment$Vertical;Landroidx/compose/foundation/gestures/snapping/SnapFlingBehavior;ZZLkotlin/jvm/functions/Function1;Landroidx/compose/ui/input/nestedscroll/NestedScrollConnection;Lkotlin/jvm/functions/Function4;Landroidx/compose/runtime/Composer;III)V in class Landroidx/compose/foundation/pager/PagerKt; or its super classes (declaration of 'androidx.compose.foundation.pager.PagerKt' appears in /data/app/~~UzU6Y7L0uRQhd6-HVwTvQA==/com.enmanuelbergling.pathpower-JN5gZNmvYBniDk6yzSjaeA==/base.apk)
                                                                                                    	at com.enmanuelbergling.walkthrough.ui.WalkThroughKt$WalkThrough$$inlined$ConstraintLayout$2.invoke(ConstraintLayout.kt:1534)
                                                                                                    	at com.enmanuelbergling.walkthrough.ui.WalkThroughKt$WalkThrough$$inlined$ConstraintLayout$2.invoke(ConstraintLayout.kt:89)

```
