# AndroidIntentApp

# Documentation

**`app/src/main/AndroidManifest.xml`**: the manifest file describes essential information about the app. It includes the permissions the app needs and the name of the other apps it will interact with. In the following code block you can see that this app needs to interact with the app `"com.example.alizarol_android_app"`.

```
<queries>
    <!-- Specific apps you interact with, eg: -->
    <package android:name="com.example.alizarol_android_app" />
</queries>
```

**`app/src/main/java/com/example/otherintentapp/MainActivity.kt`**: this file contains the code for the application. It has a button to pick an image from the gallery. When an image is chosen, the `sendDataToOtherApp` function is called. This function receives the name of the other app (`"com.example.alizarol_android_app"` in this case) and a key, value pair with the data. To send the image path to the other app, the key needs to be "image" and the value needs to be the image path (`"/storage/emulated/0/Download/image.jpg"` for example). Then, the function uses an intent to start the other app and send the data.`

```
val otherAppIntent = packageManager.getLaunchIntentForPackage(otherAppPackageName)
if (otherAppIntent != null) {
            // Add additional data to intent
            otherAppIntent.putExtra(name, value)
            // Start other app
            startActivity(otherAppIntent)
        }
```


The other app then classifies the image and sends the result back. This result is received by an intent that uses the key "data". 

```
val dataVal: String? = intent.getStringExtra("data")
```