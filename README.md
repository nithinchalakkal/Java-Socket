# Java-Socket-To-Execute-Android-Commends-


# Use :
Following will the modified content of AndroidManifest.xml file. Here we have added <service.../> tag to include our service:

 <service android:name=".MyService" />
 
 
#  Following is the content of the modified main activity file src/com.example.My Application/MainActivity.java. This file can include each of the fundamental life cycle methods. We have added startService() and stopService() methods to start and stop the service.
 
 
 
 # Method to start the service
     startService(new Intent(getBaseContext(), MyService.class));


  #  Method to stop the service
      stopService(new Intent(getBaseContext(), MyService.class));
