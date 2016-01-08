# READ-ME

## HOWTO RUN

### Bus Driver

It expects you to have 

* a Database server
* Configuration & log file
* Bluetooth

An executable jar has already been packed and is in /dist. I believe this is the one we are expected to run....

`java -jar jarfile system-config_path logfile_path`

#### Database

The program was originally coded expecting a MySQL database.

I (Cyril) am trying to make it work for SqLite because MySQL is too annoying (300MB + Bloatware for MySQL VS 2MB, single portable file for SQLite)

* Download and add the jbdc-sqlite library ot the build path
* In the META-INF/persistence.xml file, comment the SQL Lines and uncomment the SQLite lines

If you do this don't forget to repack and export a new jar file

#### Configuration and log file

You can use the ones in the /res folder. They must be supplied as arguments of the program. Config file first

#### Bluetooth

Alright, running bluetooth on my laptop/Windows 10. 

* Make sure you enable bluetooth on your device
* If you have some errors "Native library missing", download another version of the bluecove library .jar that contains the native libraries (same version, but the file you'll find on the internet is bigger ^^)