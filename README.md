# Get-My-Notes
An Android Application to Get Notes Related to your subjects.

## Features
### Has Two Diffrent Modes User and Admin
⭐ Admin
- **Subject Management**: The admin can create, edit, and delete subjects in the app.
- **PDF Upload**: The admin can upload PDFs to the app and assign them to specific subjects.
- **PDF Management**: The admin can edit and delete PDFs that have been uploaded to the app.
- **User Management**: The admin can view and manage user accounts in the app.
- **Analytics**: The admin can view app usage analytics, such as the number of downloads and user activity.

⭐ User
- **Subject Selection**: Users can view a list of subjects available in the app and select a subject to view its PDFs.
- **PDF Download**: Users can download PDFs from the app for offline reading.
- **PDF Viewing**: Users can read PDFs within the app with a built-in PDF viewer.
- **Search**: Users can search for PDFs within a subject using keywords.

## Requirements
- Android Studio version 4.0 or higher.
- An Android device running Android 5.0 (Lollipop) or higher, or an Android emulator.
- A Google Firebase account and a project created in the Firebase console.
- Google Services plugin version 4.3.5 or higher in your app-level build.gradle file.

  ```
  dependencies {
    implementation 'com.google.android.gms:play-services-auth:19.2.0'
  }
  ```
- Firebase Authentication dependency in your app-level build.gradle file.

  ```
  dependencies {
    implementation 'com.google.firebase:firebase-auth:21.0.1'
  }
  ```

- Firebase Realtime Database dependency in your app-level build.gradle file.
  
  ```
  dependencies {
    implementation 'com.google.firebase:firebase-database:19.6.0'
  }
  ```
  
- Glide dependency in your app-level build.gradle file. 

  ```
  dependencies {
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
  }
  ```
  
  ## Screen Shorts
  
  <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/Login%20Page.jpeg" width="250" title="Login Page"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/Sign_up_Page.jpeg" width="250" title="Sign Up Page"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/Forgot%20password%20page.jpeg" width="250" title="Forgot Password Page"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/Admin_dashboard.jpeg" width="250" title="Admin Dashboard"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/add%20new%20subject.jpeg" width="250" title="Add New Subject"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/Upload%20new%20pdf.jpeg" width="250" title="Add New Pdf"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/Admin%20operations.jpeg" width="250" title="Admin Operation on Pdf"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/Pdf%20List.jpeg" width="250" title="Pdf List"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/dashboar%20user.jpeg" width="250" title="Dashboard User"> <img src="https://github.com/SHabaj-dev/Get-My-Notes/blob/main/Screenshorts/user%20Profile.jpeg" width="250" title="User Profile">
  
  
