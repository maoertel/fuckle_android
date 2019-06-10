# FUCKLE – searx client for Android

## Introduction
This is a small app that works as a client for independent [searx](https://searx.me) instances. TL;DR -> searx is a kind of meta search engine.

Where does the name comes from? The analogy came up because this app allows you to [fuckle]() with the different search engines: e.g. you are using Google search without actually using it. ;-) I think you get the idea. In the end it still is a working title... who knows what it will be called in the future. And even more important: Who knows what happens to searx, as it is pretty unstable. But that is another story.

## What works so far?
1. You can search for terms and can open the results in the in-app webview
2. Technically there are search suggestion - works from app side but searx is currently not offering that feature. So it works but an empty array of search suggestions is returned. To bad... cool feature.
2. You can setup search settings like on which engines you want to search, in which categories, language, time range a.s.o.
3. Currently you can just select between a couple of preset searx instances

## What is in the pipeline?
1. Adding, editing and deleting own instances of searx, so you can have a client to your own "search engine"
2. The idea came up to spread all the searches in a round-robin style -> you have a kind of ring-buffer filled with instances and every search goes out to another instance. But... let's see what the future brings. ;-)
3. The WebView is pretty untouched yet, things like tabs, extended functions (sharing, search a.s.o.) come to my mind. But same here... everything needs time.

## What about the architecture?
The app is designed in strict layers without compromising the dependency rule and tries to follow a clean architecture approach as well as the SOLID principles.

##### Presentation Layer
- the presentation layer follows MVP design to allow a proper separation of the UI from the underlying business logic, which in turn can than be tested way more comfortable.

##### Domain Layer
- the domain layer houses the use cases and delivers the only interface for the presentation layer to deeper layers
- presenters have use cases where each case represents a certain kind of task (e.g. fetching or updating data)
- use cases communicate with (often different) repositories from the data layer and can combine data from different sources 
- if needed this layer has mappers that map different data to a suitable model for the presentation layer

##### Data Layer
- **Repositories**
    - the repositories deliver and persist data 
    - here goes the logic of the persisting strategy (for example only remote or database, only database, shared preferences) as well as fetching strategy (remote  or database first, database first, while waiting for remote or whatever...)
    - this layer is also responsible to deliver the demanded data in a suitable model for the upper layers
    - repositories hold their datasources: remote & local datasources (which both live in the same layer under the repositories)
- **Remote Data Sources**
    - remote data sources handle the network stuff -> in this case this is realized with retrofit
    - to do this it holds a service
    - it has it's own model to handle the mostly different data structure
    - there for it holds mappers that convert to a model that fits the needs of higher layers
- **Local Data Sources**
    - in analogy to the remote data source the local datasources deliver and persist data, just from local sources (mostly databases, concerning android there are shared preferences too)
    - for this it holds instances to DAOs a.s.o. (here it is mostly room based)
    - as the data structure for storing things into a database differs it has it's own model too
    - what again needs mappers to map to suitable models for higher layers
    - side note: with saying this, all the mapping starts to make sense, as remote data mapper and local data mapper map from different models to the same model that is used by the repository

## Technologies ...so far
- 100% [Kotlin](https://kotlinlang.org/), no Java code
- [Dagger](https://dagger.dev/android) for all the Dependency Injection
- [Reactive X](http://reactivex.io/) -> [RxJava](https://github.com/ReactiveX/RxJava) & [RxKotlin](https://github.com/ReactiveX/RxKotlin) for asynchronous programming 
- [Retrofit](https://square.github.io/retrofit/) for the network stuff
- [Room](https://developer.android.com/topic/libraries/architecture/room) for having a smooth database experience
- [Moshi](https://github.com/square/moshi) for all the Json de-/serializing
- JUnit & [Mockito](https://site.mockito.org/) with the great extension by [nhaarman](https://github.com/nhaarman/mockito-kotlin) which provides valuable helper functions


