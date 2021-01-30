# ToSuccess - Android app
A todo app for android. An android implementation of the [ToSuccessWepage](https://github.com/VegSja/tosuccessweb)

# About
## How it is built
### Design
The app is built using Android Studio and Java

### Communications with REST-API
To send GET/POST requests to the REST-API (see [The GitHub page of the backend](https://github.com/VegSja/ToSuccessBackend) for more information). To achieve this the app uses an instance of the class API_Connection which uses volleys and callbacks to send and recieve requests.

Authorization with the backend is completed wtih oAuth Google sign in and the backend uses a token/refresh token system to keep the user signed in. See [The GitHub page of the backend](https://github.com/VegSja/ToSuccessBackend) for more information.

# Goals

## Missing features
- Categorize activities
    - Create a system where the user can add different groups of activities
    - Let user choose a group for each activity in modal
    - Create page which displays these categories created by the user
- Get statitics
    - Get statitics from backend
    - Display statitics with different diagrams
- Improve UI
    - Animations
    - More work on colors and pictures

# Images
As this is a work in progress the only image availible per this date is of the main activity page. Displaying activities planned for the current date

[ActivityPage](/Pictures/mainpage.jpg)