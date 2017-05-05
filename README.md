# FlowR [![](https://jitpack.io/v/Fueled/flowr.svg)](https://jitpack.io/#Fueled/flowr)

FlowR is a wrapper class around the Fragment Manager. It's mainly used to navigate between different fragments easily while providing a wide range of functionality. The following are the functionalities provided by the Flowr:

* Easily navigate between different fragments.
* Ability to open fragments for result.
* Keeping the state of the activity correctly synced depending on the custom values specified by the fragment currently visible such as the screen orientation and navigation bar color.

##Install
Add it in your root build.gradle at the end of repositories:
```groovy
	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
```
Then add the dependency to the application module:
```groovy
	dependencies {
		compile 'com.github.fueled:flowr:1.1.1'
	}
```

## Getting Started

Each new activity will have its own Flowr instance, the Flowr lifecycle should be bound to the activity, meaning it should be destroyed and created with the activity. For the activity to work with the Flowr we need to make sure that it implements the interface `FlowrScreen`.

While creating a new Flowr instance these are the required parameters:

* **`containerId`**: this is the id for the layout that the Flowr will use to display the fragments inside.
* **`FlowrScreen`**: in most cases this will be your activity with the interface `FlowrScreen` implemented, this will provide the Flowr with access to the activity `FragmentManager` and the ability to set values such as the screen orientation and navigation bar color depending on the fragment currently being displayed.
* **`FragmentsResultPublisher`**: this is used to publish results from fragments that where opened for results.

And some of the optional values are:

* **`ToolbarHandler`**: this is only needed if a single `Toolbar` is going to be used for every fragment, in most cases this will be your activity with the interface `ToolbarHandler` implemented. The `ToolbarHandler` provide extra functionality to the fragments displayed to define the toolbar navigation icon, and to toggle the toolbar visibility.
* **`tagPrefix`**: a custom prefix for the tags to be used for fragments that will be added to the backstack, the default tag used is `#id-`.
* **`DrawerHandler`**: this is only needed if the activity contains a side drawer, again in most cases this will be your activity with the `DrawerHandler` interface implemented. The `DrawerHandler` provides the ability to enable/disable the drawer and open/close the drawer.


Creating a new Flowr instance for an activity that does not have a toolbar and a drawer:
```java
Flowr Flowr = new Flowr(R.id.main_container, flowrScreen, resultPublisher);
```

Creating a new Flowr instance for an activity that does have a toolbar and a drawer:
```java
Flowr Flowr = new Flowr(R.id.main_container, flowrScreen, toolbarHandler, drawerHandler, resultPublisher);
```


## Displaying a Fragment

When displaying a new fragment there are multiple parameters that can be specified. Displaying a Fragment is done through a builder pattern which allows us to easily only specify the parameters that we require. these parameters are listed and explained below:

* **`fragmentClass`**: The class of the fragment to be displayed, this is the only parameter that would always be required when displaying a new fragment.
* **`data`**: a `Bundle` containing the arguments the fragment might need. The default value for this is null.
* **`skipBackStack`**: specify whether this fragment will be added to the `FragmentManager` back stack or not. The default value for this is `false`.
* **`clearBackStack`**: specify whether the `FragmentManager` backstack should be cleared before displaying this fragment. The default value used for this is `false`.
* **`replaceCurrentFragment`**: specify whether this fragment should replace the fragment currently displayed inside the container or just be added over it. The default value used for this is `false`.
* **`enterAnim`** and **`exitAnim`**: animation resource ID for the enter and exit fragment animation. The default values used here are `R.anim.fragment_enter_anim` and `R.anim.fragment_exit_anim`.

finally after we have specified all the parameters we need we can simply call `displayFragment()` to display the fragment.

```java
Flowr.open(DemoFragment.class)
      .setData(data)
      .skipBackStack(false)
      .clearBackStack(true)
      .replaceCurrentFragment(true)
      .setCustomTransactionAnimation(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
      .displayFragment();
```

## Displaying a Fragment for Results

When displaying a fragment for results, we will have access to all the parameters described in the previous section with the only difference being that `displayFragmentForResults` should be called rather than `displayFragment` at the end with the following parameters:

* **`fragmentId`**: a unique ID that each new instance of `AbstractFragment` is assigned at the start and it can simply be accessed by calling `getFragmentId()` from inside your fragment. This ID is used to deliver the results to the correct fragment instance at the end.
* **`requestCode`**: this code will be returned in `onFragmentResults()` when the fragment is closed, and it can be used to identify the request from which the results were returned.

```java
Flowr.open(RequestFragment.class)
      .displayFragmentForResults(getFragmentId(), REQUEST_CODE);
```

To handle the results all you need to do is simply just override the `onFragmentResults()` method in your fragment:

```java
@Override
protected void onFragmentResults(int requestCode, int resultCode, Bundle data) {
    super.onFragmentResults(requestCode, resultCode, data);

    if (requestCode == REQUEST_CODE) {
        if (resultCode == Activity.RESULT_OK) {
            demoTextView.setText("Result OK");
        } else {
            demoTextView.setText("Result CANCELED");
        }
    }
}
```

Finally when closing a fragment that was opened for results you will need to call the Flowr method `closeWithResults()`:

```java
Flowr.closeWithResults(getResultsResponse(resultCode, resultData));
```

## State Sync and Fragment Dependent Customization

The Flowr provides the ability to set custom attributes for values such as the screen orientation and navigation bar color for each fragment. These values are kept in sync automatically with the parent activity when navigating between different fragments through the Flowr `syncScreenState()` method. The `syncScreenState()` method is only required to be invoked manually when a change to one of these values has been made after the fragment has been already displayed.

The following custom values are what is currently supported by the Flowr:

### Screen Orientation

Each fragment can specify its own preferred screen orientation by simply overriding the method `getScreenOrientation()` from `AbstractFragment` and returning one of the following supported values listed [here](https://developer.android.com/reference/android/content/pm/ActivityInfo.html#screenOrientation).

```java
public class DemoFragment extends AbstractFragment<DemoPresenter> implements DemoScene {

    ....

    @Override
    public int getScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }
}
```

### Navigation Bar Color

Fragments can also specify a preferred color to be used for the navigation bar, this can be done by simply overriding `getNavigationBarColor()` and returning the integer value for the color to be used.

```java
public class DemoFragment extends AbstractFragment<DemoPresenter> implements DemoScene {

    ....

    @Override
    public int getNavigationBarColor() {
        return navigationBarColor;
    }
}
```

### Toolbar Options

These values are only used if the parent activity has a toolbar and the Flowr was provided a `ToolbarHandler` instance.

1. **Toolbar Visibility:** The boolean value specified here by overriding the `isToolbarVisible()` method in your fragment, will be used to determine if the fragment should be visible or not. By default the toolbar is visible.
```java
public class DemoFragment extends AbstractFragment<DemoPresenter> implements DemoScene {

    ....

    @Override
    public boolean isToolbarVisible() {
        return false;
    }
}
```

2. **Navigation Icon Type:** This value is used to determine the type of the navigation icon to be used by the toolbar. This is done simply by overriding the `getNavigationIconType()` method in your fragment, and returning one of the following values `HIDDEN`, `HAMBURGER`, `BACK`, or `CUSTOM`.
```java
public class DemoFragment extends AbstractFragment<DemoPresenter> implements DemoScene {

    ....

    @Override
    public NavigationIconType getNavigationIconType() {
        return NavigationIconType.HAMBURGER;
    }
}
```

3. **Navigation Icon Drawable:** This value is only used if the navigation icon type returned previously was `CUSTOM`. The drawable returned here will be used as the toolbar navigation icon, which can be simply done by just overriding the `getNavigationIcon()` in you fragment.
```java
public class DemoFragment extends AbstractFragment<DemoPresenter> implements DemoScene {

    ....

    @Override
    public Drawable getNavigationIcon() {
        return navigationIcon;
    }
}
```

### Drawer Options

The Flowr also provides the ability to specify whether the drawer should be enabled or not for the current fragment, this option is also only available if the fragment has a drawer and the Flowr was provided with a `DrawerHandler` instance. To specify this you will have to override the `isDrawerEnabled()` method in your fragment and return either `true` or `false`.

```java
public class DemoFragment extends AbstractFragment<DemoPresenter> implements DemoScene {

    ....

    @Override
    public boolean isDrawerEnabled() {
        return super.isDrawerEnabled();
    }
}
```

#License

    Copyright 2016 Fueled

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
