BudgetTracker - Secure Expense Tracking App


Project Overview

BudgetTracker is a smart and secure Android mobile application designed to help users manage their personal finances with confidence and convenience. The app allows users to securely track expenses, access essential financial data, and view live currency exchange rates while ensuring strong protection of sensitive information.
The application supports both online and offline usage and is developed using Kotlin, Jetpack Compose, MVVM architecture, Firebase Authentication, and Room Database.

Key Features

Secure user authentication using Firebase Email and Password and Google Sign-In  
Controlled app entry validation for enhanced security  
Offline expense data storage using Room Database  
Live currency exchange rate integration  
Dark mode and light mode support  
About and version information screens  
Clean MVVM-based architecture  
Smooth splash to dashboard navigation flow

Technologies Used

Language: Kotlin  
User Interface: Jetpack Compose  
Architecture: MVVM  
Authentication: Firebase Authentication  
Local Storage: Room Database  
API: ExchangeRate API  
IDE: Android Studio  
Version Control: Git and GitHub  
Project Management: Trello

External API Used

ExchangeRate API  
https://www.exchangerate-api.com/

Application Flow
Splash Screen → Secure Access Validation → Login or Register → Dashboard → Expense Management, Currency Rates, Settings

Security Implementation

Firebase Authentication is used for secure login  
Secure app entry validation is enforced before accessing financial data  
All communication uses HTTPS encryption  
Sensitive credentials are not stored locally  
Only authenticated users can access protected screens

Installation and Setup

Clone the repository  
Open the project in Android Studio  
Connect Firebase and enable Email and Password authentication  
Add your ExchangeRate API key  
Sync Gradle and run the app on an emulator or physical device

Agile Development

The project followed a sprint-based Agile methodology across five sprints. Development covered authentication setup, API integration, offline support, personalization features, and secure app entry validation.

Future Enhancements

Expense analytics and reports  
Multi-currency budgeting  
Encrypted local database  
Notification-based spending alerts

Useful Links

GitHub: https://github.com/Ravi-67/MAD
Trello: https://trello.com/b/yNZ3RXQS/mad
ExchangeRate API: https://www.exchangerate-api.com/