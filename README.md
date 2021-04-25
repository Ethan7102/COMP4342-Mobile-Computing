# COMP4342-Mobile-Computing

Installation Guide
This part illustrates how to install the mobile shopping application on your computer.

First, you need to go to the URL and download Android Studio. The recommended version is 4.1.2.
https://developer.android.com/studio/archive

![Alt text](https://github.com/Ethan7102/COMP4342-Mobile-Computing/blob/main/Images/p1.png)

Second, you need to download XAMPP to connect to the MariaDB database. The recommended version is 8.0.2.
https://www.apachefriends.org/download.html
 

In the directory of XAMPP, go to your htdocs folder and put all the PHP format documents into the folder.

![Alt text](https://github.com/Ethan7102/COMP4342-Mobile-Computing/blob/main/Images/p2.png)


run the XAMPP and start the first two modules (Apache & MySQL).

![Alt text](https://github.com/Ethan7102/COMP4342-Mobile-Computing/blob/main/Images/p3.png)

Open your browser and go to http://localhost/phpmyadmin/index.php

![Alt text](https://github.com/Ethan7102/COMP4342-Mobile-Computing/blob/main/Images/p6.png)

 
Click the import button at the top.

![Alt text](https://github.com/Ethan7102/COMP4342-Mobile-Computing/blob/main/Images/p7.png)

Select the MSDB.sql in MSDB folder and click the execute button at the bottom right corner. 
 

Then, you will see a comp4342_msdb database.

![Alt text](https://github.com/Ethan7102/COMP4342-Mobile-Computing/blob/main/Images/p8.png)



Run the Android Studio and click the Open an Existing Project. Then, select the MobileShopping application and click “OK” to open it.

![Alt text](https://github.com/Ethan7102/COMP4342-Mobile-Computing/blob/main/Images/p4.png)
 
 
Open the APIUrl.java in Android Studio, you need to add your IP address in it.
the input must be “String url ="http://XXXXXXXXXX"; ”, which XXX is your IP address.

![Alt text](https://github.com/Ethan7102/COMP4342-Mobile-Computing/blob/main/Images/p5.png)


Then, you can run our mobile shopping application.
