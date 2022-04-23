# Android Blog Application

This is a native android application built with jetpack compose (Modern Android UI library)
The app is available for download [here](https://mystory-f1ec9.web.app/) for now but I will upload
it to the play store soon
The app there might be an old version

## Technologies Used
1. Jetpack Compose
2. Dagger Hilt for Dependency Injection
3. Retrofit to make Api Calls
4. Some of the google accompanist libraries for extra features
5. Room Library for offline saved posts
6. Push notifications for likes and comments (Firebase messaging)
7. Cloudinary for storing images and retrieving them via url
8. 


## Backend Application
The backend application is built on node.js with the express.js framework to store and retrieve 
information of the application.The database used is Mongo DB

The backend is deployed on heroku and the code is on a private github repository
Ps (You will find the deployed API endpoint in the source code so the code should run smoothly)

## Features
1. Ability to create posts 
2. Abiity to like a post
3. Ability to comment on posts
4. Follower and Following system
5. ABility to view viewer counts on posts
6. CRUD operations on your posts

## Features to come
1. Comment mentions
2. Ability to upload profile photos
3. Make the UI more appealing
4. Chat Feature possibly

## Known bugs (Working on them in future commits)

1. Big images sometimes will not show in the add post screen when selected
2. Crashes on some parts of the application when there is no internet
