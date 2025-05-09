![alt text](https://github.com/Singularity-Coder/FOLK-Database/blob/master/assets/logo192.png)
# FOLK-Database
This is a customer relationship management App.

## Screenshots
![alt text](https://github.com/Singularity-Coder/FOLK-Database/blob/master/assets/s1.jpg)
![alt text](https://github.com/Singularity-Coder/FOLK-Database/blob/master/assets/s2.jpg)
![alt text](https://github.com/Singularity-Coder/FOLK-Database/blob/master/assets/s3.jpg)
![alt text](https://github.com/Singularity-Coder/FOLK-Database/blob/master/assets/s4.jpg)
![alt text](https://github.com/Singularity-Coder/FOLK-Database/blob/master/assets/s5.jpg)

## Tech stack & Open-source libraries
- Minimum SDK level 21
- [Java](https://www.java.com/en/) based, [RxAndroid](https://github.com/ReactiveX/RxAndroid) + [LiveData](https://developer.android.com/topic/libraries/architecture/livedatahttps://developer.android.com/topic/libraries/architecture/livedata) for asynchronous.
- Jetpack
  - Lifecycle: Observe Android lifecycles and handle UI states upon the lifecycle changes.
  - ViewModel: Manages UI-related data holder and lifecycle aware. Allows data to survive configuration changes such as screen rotations.
  - DataBinding: Binds UI components in your layouts to data sources in your app using a declarative format rather than programmatically.
  - Room: Constructs Database by providing an abstraction layer over SQLite to allow fluent database access.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model)
  - Repository Pattern
- [Retrofit2 & OkHttp3](https://github.com/square/retrofit): Construct the REST APIs and paging network data.
- [gson](https://github.com/google/gson): A Java serialization/deserialization library to convert Java Objects into JSON and back.
- [Material-Components](https://github.com/material-components/material-components-android): Material design components for building ripple animation, and CardView.
- [Glide](https://github.com/bumptech/glide): Loading images from the network.
- [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics): Firebase Crashlytics is a lightweight, realtime crash reporter that helps you track, prioritize, and fix stability issues that erode your app quality.
- [Firebase Firestore](https://firebase.google.com/docs/firestore): Flexible, scalable NoSQL cloud database, built on Google Cloud infrastructure, to store and sync data for client- and server-side development.
- [Firebase Authentication](https://firebase.google.com/products/auth): An end-to-end identity solution, supporting email and password accounts, phone auth, and Google, Apple, X, Facebook, GitHub login, and more.

## Architecture
![alt text](https://github.com/Singularity-Coder/FOLK-Database/blob/master/assets/arch.png)

This App is based on the MVVM architecture and the Repository pattern, which follows the [Google's official architecture guidance](https://developer.android.com/topic/architecture).

The overall architecture of this App is composed of two layers; the UI layer and the data layer. Each layer has dedicated components and they have each different responsibilities.
