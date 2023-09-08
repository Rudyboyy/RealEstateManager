# RealEstateManager
## Table of Contents
1. [General Info](#general-info)
2. [Demo](#demo)
3. [Utilized Skills](#utilized-skills)
4. [Installation](#installation)
5. [Configuration](#configuration)
6. [Run and compile](#run-and-compile)

### General Info
***
RealEstate Manager is a comprehensive real estate application designed for real estate agents. 
It enables agents to efficiently store and manage properties for sale, create new listings, view them on a map, and even includes tools for calculating mortgage loans.
#### Logo
<img src="https://github.com/Rudyboyy/RealEstateManager/blob/main/app/src/main/res/drawable/real_estate.png" alt="Logo" width="300" height="300">

### Demo
***

https://github.com/Rudyboyy/RealEstateManager/assets/96139750/a42a336f-1487-4173-ab3e-7a20e51f75e7

#### Landscape

https://github.com/Rudyboyy/RealEstateManager/assets/96139750/90f282b1-f03d-4010-a20f-a7aec1c3cdf3


### Utilized Skills
***
- **Programming Language:** Kotlin
- **Android Architecture:** MVVM (Model-View-ViewModel)
- **User Interface:** XML for creating the user interface
- **Integration of Google Maps API**
- **Database:** SQLite
- **Android Notifications**
- **Camera Usage for Image Capture**
- **Importing Images from the Gallery**
- **Adaptive Design for Android Devices (Tablets and Phones)**
- **Support for Dark and Light Modes (Light/Dark mode)**
- **Use of Coroutines for Asynchronous Management**
- **Utilization of a ContentProvider for Data Management**
- **Testing:** Unit tests with JUnit, integration tests with Espresso and Mockito
- **Version Control:** Use of Git for version control
- **Compatibility with Android Versions:** Compatible with Android versions 21 (minSdkVersion) to 33 (compileSdkVersion)
- **Third-party Libraries:** Use of Glide, Room, Gson, etc.
- **Dependency Management:** Utilization of Kotlin Gradle DSL and configuration of Kotlin Kapt.

### Installation
***
A little intro about the installation. 
#### Option 1
Use the terminal of Android Studio, copy and execute the command line :
```
$ git clone https://github.com/Rudyboyy/RealEstateManager.git
```
#### Option 2
* Download the ZIP folder -> https://github.com/Rudyboyy/RealEstateManager/archive/refs/heads/main.zip
* Open it in Android Studio

### Configuration
***
To make this application work correctly, you need to add your own Google Maps API key. Follow the steps below to set up the API key:
1. Go to [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project or select an existing one.
3. In the project dashboard, navigate to the "APIs & Services" section and click on "Library" to search for and enable the "Maps SDK for Android" API.
4. Once the API is enabled, click on "Credentials" in the "APIs & Services" section to create an API key. Select "Create credentials" > "API key."
5. Follow the instructions to generate the Android API key. When prompted, add the application information such as the package name. You can find the package name in your `build.gradle` file (usually under `defaultConfig -> applicationId`).
6. Once the key is generated, copy it.
7. In your Android project, open the `local.properties` file and add a line with your API key like this (replace `YOUR_API_KEY` with the key you copied):

   ```properties
   MAPS_API_KEY=YOUR_API_KEY

### Run and compile
***
* Choose a device (virtual or physical)
* Click on Run or use <kbd>Shift</kbd>+<kbd>F10</kbd>
