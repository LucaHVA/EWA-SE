# Project Enterprise Web Application (EWA)
This project uses Vue.js 3 in the frontend and Spring boot in the backend.

## Description
Catan is a multiplayer game application where users can create and edit accounts, compete with friends, and track their game history. The game also features a leaderboard, friend requests, and the option to play against bots. Our project differentiates itself by providing a seamless integration of social features and robust account management.

## How to install and run the project

1. Clone this repository to your own device.
2. Open the project in IntelliJ.
3. Install the Maven dependencies.
4. Install dependencies by running `npm install` in the root folder of the project.

### For the frontend
1. `cd` into the `frontend` directory.
2. Run `npm install` in the `frontend` directory.
3. To serve your project run `npm run serve` in the `frontend` directory.

### For the backend
1. Navigate to the backend folders and open the folder BackendApplication.java in `src/main/java/org/example/backend`.
2. Run the application.

## Features
-	Account Management: Create and edit user accounts.
-	Multiplayer Gameplay: Play Catan with friends or against bots.
-	Leaderboard: Track your progress and compare with others.
-	Friend System: Send friend requests and manage your friend list.
-	Game History: View your past games and performance.

## Contributing
We make use of issue tracking via the issues board in this GitLab repository. Pick up an issue and start coding. Please update the issue you are working on frequently during your development, this is so other contributers can track your overall progress.

### Folder structure (frontend)

-   `src` Root folder of the frontend.
-   `src` Tests go in here.
-   `src/assets/css` The main.css file goes here.
-   `src/assets/images` Images go in here.
-   `src/components` Welcome, UnknownRoute and NavBar components go in here.
-   `src/components/pages` When you want to create a new page it goes in here.
-   `src/models` Models go in here.
-   `src/router` The router index.js file is in here. 
-   `src/services` Services go in here.

### Folder structure (backend)

- `src/main/java/org/example/backend`
- `src/main/java/org/example/backend/config` The corsConfig goes in here.
- `src/main/java/org/example/backend/controllers` Controllers go here.
- `src/main/java/org/example/backend/enums` Enums go in here.
- `src/main/java/org/example/backend/exceptions` Exceptions go in here.
- `src/main/java/org/example/backend/models` Models go here.
- `src/main/java/org/example/backend/repositories` Repositories go in here.
- `src/main/java/org/example/backend/security` Security related things go here.
- `src/main/java/org/example/backend/sockets` Websockets logic goes here.
- `src/test/java/org/example/backend` Tests go in here.

## Deployment
The project is deployed on Render.com using Docker. If you want to learn more about how the deployment works and how you can update it click [here](https://gitlab.fdmci.hva.nl/se-ewa/deployment-workshop/-/tree/master) go to the gitlab. You can access the live application via the following links:
- Frontend: https://front-end-catan.onrender.com
- Backend: https://back-end-catan.onrender.com

## Authors and acknowledgment
This project was developed by Sufyaan, Luca, Stephanie, Jesper, and Armando. Special thanks to everyone who contributed to the development, testing, and deployment of this project. Your hard work and dedication are greatly appreciated.
