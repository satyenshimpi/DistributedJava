# DistributedJava
Distribute Java class files to clients and execute even though clients don't have them already.

Server will distribute any new class files which might be generated on the fly to clients for execution. In general client program should have the class files present on class path before exceuting them. But in this case the class files will be dynamically added and executed.
