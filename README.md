# Marvelicious

🎓 App written and maintained to lean latest Android and Kotlin practices

🔨 Sample implementation for real-world Android use cases. 


## Marvelicious
is an android app built to learn and see with live example the latest tech provided by the community.

The app consumes the api `https://developer.marvel.com/` provided by the good people of Marvel.

🚗The application is still a WIP, and contributions, suggestions will be highly appreciated.


## 🔧 Project Setup
Android studio and gradle magic will do everything for you, so just fork, clone and let android studio do its job.
There is one small pre-req of using the app though.
Since the app uses the Marvel api, you have to get a couple of keys to be able to consume the API.
The direction on how to get the api can be found on the following [page](https://developer.marvel.com/documentation/authorization).

Since the API Keys should be confidential especially considering marvel has a
limited number a call that can be made to the api per day, it is **essential** that the keys _not_ be checked in.
Once you have your keys, you can put them in your local `gradle.properies` file.``
```
MARVEL_API_PUBLIC_KEY="xxxxxxxxxxxx"
MARVEL_API_PRIVATE_KEY="xxxxxxxxxxxx"
```
They are then overwritten by gradle and can be accessed in the code as 
```Kotlin
val publicKey = BuildConfig.MarvelPublicApiKey
val privateKey = BuildConfig.MarvelPrivateApiKey
```

🏃‍♂️🏃‍♀️For now thats pretty much it for you to be able to start running the app locally!


The app uses many new cool features from Android. I, _may be_ with the help of other contributors, try to document more about them in the **readme**
Soon, I will also write a blog on the complete journey of start to _finish_ (_hopefully one day_) 😅🙈 of the project.

For now this is the list of dependencies the app uses:
## 💳 Libraries used 
* Support Libraries
*  Constraint Layout
* Android Navigation
* Kotlin oroutines
* Retrofit
* Okhttp logger
* Moshi
* Joda time
* Architecture Components, ViewModel and LiveData
* Glide
* Room
* WorkManager
* Android Paging
* Junit
* Android X Junit Extension
* Espresso
* MultiDex

## 👷‍♀️ Contributing 👷‍♂️

I am learning Android/ Kotlin myself, therefore would love to have some feedback, advise and help.
If you wish to contribute, have an idea, think about some new use cases on how to use the awesome api provided from marvel,
or may be make the code better by following **SOLID** principles or some know other good desing patterns, feel free to 
just open an issue, create a pull request.
**This app is _still_ very much in its beginning phase, a lot of things are open to be added**

