# react-native-paymentengine-android-sdk
Attempts to wrap the paymentengine-android-sdk as a react native component that can be used (https://docs.paymentengine.io/android/home/)

copied from (https://github.com/davidfritch/react-native-android-toast) as a starting point

# Steps to actually integrate this into a getting started project and see it run

This was tested using the node v10.12.0

Note: I was not using `yarn` to help.  It seemed I ran into issues when yarn was installed.

Creating a React Native project following the instructions here:
https://facebook.github.io/react-native/docs/getting-started

Follow the steps under Building Projects with Native code but initialize the project with React Version 0.57.1 as 0.57.3 has some bugs in it at the time of this writing.

For example,

```
react-native init --version="react-native@0.57.1" DemoNativeAndroidModuleProject
```

But then, this answer https://stackoverflow.com/questions/52784633/i-have-some-error-when-add-button-to-my-react-native-app/52796919#52796919

Mentions that once you do that you need to add some missing dependencies.  Initially, I had just used the default which put me at react v0.53 and I needed to downgrade

```
npm add @babel/runtime
npm i -D schedule@0.4.0
npm i
```

NOTE: if you forget to run these, you will see errors in the Metro Bundler console logs saying it couldn't find a file, I believe it is under the `@babel` component and will recommend 4 different things to try.  But, none of those work and you just need to add the dependencies above.

First make sure you can run the default project following their steps without any issues.  Once you can see the project run on both an Android Emulator and an iOS Emulator proceed with the following steps.

NOTE: the first time trying to run it on ios, I believe I saw something like `:CFBundleIdentifer does not exist`, but just running it a second time it went away.  Also, don't get thrown off by warnings that you may see in the console after initially running and it can take almost a minute or so for everything to compile before it actually launches the emulator the first time and deploys the app to the emulated device.  Android seems to be much quicker initially.


After the project is initialized add this project by running at the root

```
npm install --save git+https://github.com/davidfritch/react-native-android-toast.git
```

Then see the "Installing it as a library in your main project" section in https://cmichel.io/how-to-create-react-native-android-library where he says which files need to be updated.

But here are the steps for convenience:

Launch Android Studio and import the project by selecting `[your project root]/android` directory.

Note: A gotcha here is that it will fail to find a file and in the project `build.gradle`, need to set `google()` first in both of the `repositories` or Gradle will fail.  See https://stackoverflow.com/questions/52944351/android-ci-build-could-not-find-aapt2-proto-jar

modify `settings.gradle` like so:

```
include ':react-native-android-paymentengine-android-sdk'
project(':react-native-android-paymentengine-android-sdk').projectDir = new File(settingsDir, '../node_modules/react-native-paymentengine-android-sdk/android')
```

Modify `/app/build.gradle` with the following:

```
dependencies {
    // ...
    implementation "com.facebook.react:react-native:0.57.1"  // From node_modules
    api project(':react-native-android-paymentengine-android-sdk')
}
```

Note: the site references `compile` but this has been updated to use `api` with the updated gradle build tools

Once you have done that, and resync the project, you should see both the main android project project and the `react-native-android-toast` project/component.

A Note on Android Studio and Gradle: When I initially just used the code as it was in the other Tutorial, the build tools and the target SDK in the main React Native project were not in sync with target SDK found in the react-native-android-toast `build.gradle` file.  So it is possible that when you run `react-native init ...` It may be targeting a more recent version of the SDK.  At this time, I am not sure the best way because then Android Studio was encouraging to update the `build.gradle` file as found under `node_modules` but if you ever update the package using `npm` that obviously can become a problem.  At this time, I am not sure the best way to deal with that.  Thus, this package is built to be compatible if you initialize a react-native at 0.57.1 and I am assuming that would target android sdk 27.

Then in `MainApplication.java` in the main project you can add the following:

```
import com.upstreamengineering.PaymentEngineAndroidSdkPackage;
```

```
@Override
    protected List<ReactPackage> getPackages() {
      return Arrays.asList(
          new MainReactPackage(),
          new PaymentEngineAndroidSdkPackage()
      );
    }
```

NOTE: if Android Studio is appearing to have compile issues, try refreshing/resyncing/running the gradle build.

All the steps above, if I understand it, basically registers this new ReactPackage which is a wrapper for the native Android Toast component to be available.  

The `getName()` in `Module.java` allows it to be referenced in `index.android.js` like this

```
'use strict'

import { NativeModules } from 'react-native'
// name as defined via ReactContextBaseJavaModule's getName
module.exports = NativeModules.ToastA
```

NOTE: there is an `index.ios.js` in this project so that when it runs iOS it at least loads this required file but obviously doesn't do anything.  But if this is not present, you will see an error in the Metro Bundler console logs saying it couldn't find an index.js file.

Another Note: When I had the getName() returning `"ToastAndroid"` I believe I was running into a conflict because there is already a module named `ToastAndroid` in one of the facebook .jar libraries, so it was renamed to `"ToastA"` just for this example.

Add a `ToastAndroidExample.js` file to your React Native project as follows:

```
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Button, Alert} from 'react-native';
import ToastA from 'react-native-android-toast'

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' + 'Cmd+D or shake for dev menu',
  android:
    'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

export default class ToastAndroidExample extends Component {
  _onPress = Platform.select({
      ios: () => {
          Alert.alert('Alert', 'You pressed me on ios!')
      },
      android: () => {
        ToastA.show('Android Toast Ran Again!', ToastA.LONG)
      }
  })

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Welcome to React Native!</Text>
        <Text style={styles.instructions}>To get started, edit App.js</Text>
        <Text style={styles.instructions}>{instructions}</Text>
        <Button onPress={this._onPress} title="Press me"></Button>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
```

And then update the `index.js` file to run that component

```
/** @format */

import {AppRegistry} from 'react-native';
import ToastAndroidExample from './ToastAndroidExample';
import {name as appName} from './app.json';

AppRegistry.registerComponent(appName, () => ToastAndroidExample);
```

Then from the root of the project finally run

```
react-native run-android
```
or
```
react-native run-ios
```

You should be able to see that when the platform is Android, it executes the native Android Module that was added, and when it is ios, it just executes an alert.
