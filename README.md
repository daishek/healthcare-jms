# Medical Alert System with ActiveMQ JMS

The project at hand is a comprehensive and modular implementation of Apache ActiveMQ and Java Message Service (JMS) for efficient message exchange and communication within a distributed system. This project has been designed to facilitate the exchange of messages and data between various components and applications in a way that is both scalable and maintainable.

> **Warning**
> Please note that this project is part of my university's etudes projects for the Internet of Things (IoT) module, which involves using Apache ActiveMQ with JMS.

## Download and run

1.  Download .zip or clone the project

        git clone https://github.com/daishek/JMSActiveMQ.git
2.  Download requirments
    - [activemq from apache](https://activemq.apache.org/)
    - [Log4j](https://logging.apache.org/log4j/2.x/download.html)
3.  Setup project

    - cretae a new folder **lib** in each project
    - You need to add the ActiveMQ JAR file (activemq-all-5.18.2.jar) located in the Activemq directory into the lib folder for each project.
    - cretae a new folder **USER** inside **lib** directory
    - You simply need to add the two JAR files: log4japi-2.20.0 and log4j-core-2.20.0. into **USER** directory

> **Important**
> Folder structure
>  - **project**
> 	 - **bin**
> 	 - **lib**
> 		- **USER**
> 			 - *log4j-api-2.20.0.jar*
> 			 - *log4j-core-2.20.0.jar*
> 		- *activemq-all-5.18.2.jar*
> 	 - **src**
> 		 - *App.java*

4.  Run ActiveMQ
    cd to activemq\bin and run `activemq start`
