# SDSU Assignment Tracker

------
## Table of Content

- [Description](#description)
- [Usage](#usage)
- [Demo and Links](#Demo-and-Links)
- [Deployed Link](#Deployed-Links)
- [Author/GitHub Repository](#AuthorCollaborators-GitHub-Repository)
- [License](#license)

------
## Description:

The SDSU Assignment Tracker is a web application that allows users to track their scheduled work/assignments efficiently.
The Tracker assigns task based on priority allowing for certain work that is considered tedious or has a sooner deadlines.

------
## Usage:

To use this application, MySQL Workbench, MySQL version 8.0, Java 17, node.js, and Maven will need to be installed.
Once installed, you must first run the Database through the Workbench. This allows for the data to be presentable to the backend and frontend once they start.


This project was intended to be worked on **Intellij** as the IDE.

**To run the BackEnd:**
1) Ensure connection to the database is working and online.
2) Ensure the Run/Debug configuration for the BackendApplication, under the Environmental Variables has the right credentials for your MySQL. 
```aiignore
Ex: MYSQLUSERNAME=[YOUR_OWN_USERNAME];MYSQLPASSWORD=[YOUR_OWN_PASSWORD]
```

**To run the FrontEnd:**
1) Ensure both the database and BackEnd are both online and running
2) In the terminal (starting from the root directory) you can type:
```aiignore
cd frontend/
```
Then you must install all the dependencies the application uses by typing:
```aiignore
npm i
```
Finally, you can run the FrontEnd by typing:
```aiignore
npm start
```
------
## Demo and Links:
- Project is in the early stages of development, so no demo or links have been created.

------
## Deployed Links
- Project will potentially be deployed on Heroku/AWS

------
## Author/Collaborators GitHub Repository:

- [Charles Kim](https://github.com/kims1998)
- [Rachel Rogers](https://github.com/rrogers5143)
- [Perfect Phanitchaleun](https://github.com/SailmanSeeulater)
- [Nico Reyasbautista](https://github.com/nicoReyas)
- [Marie-Jhayne Ayroso](https://github.com/mayroso4517)
- [Estephanie Fernandez](https://github.com/efernandez1121)
-----
## License:
- A license hasn't been decided/chosen for this project.