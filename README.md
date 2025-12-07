🍔 FastFood Android App
A modern Android application for browsing meals, managing favorites, and placing orders.

📱 Overview
FastFood Android App is a mobile food ordering client built using Java + Android Studio, integrated with a Spring Boot backend.
Users can browse categories, view products, add to cart, mark favorites, and authenticate securely using JWT.

🚀 Features
🔐 Authentication

Phone number login
JWT-based authentication
Secure token storage

🛒 Menu & Products
Load categories from backend
View product list (Lavash, Pizza, Burgers...)
Glide image loading

❤️ Favorites
Add/remove product to favorites
Favorite icon synced with backend
Smooth UI updates

🧺 Cart
Add to cart
Remove product from cart
Recalculate total price
Save cart state

🛠 Tech Stack
Frontend (Mobile)
Java
Android Studio
Retrofit2 → REST API client
Glide → Image loading
RecyclerView, CardView, GridLayout
Material Components

Authentication
JWT Token stored using SharedPreferences

🧪 How to Run

1. Start backend API
    1)Clone the backend repository
    git clone https://github.com/asror812/fastfood-api-spring.git
    cd fastfood-api-spring
    2)Start required services (Redis + PostgreSQL)
    docker compose up -d
    
    Check running containers:
    docker ps
    
    Run the Spring Boot application
    Using IDEA or ./gradlew bootRun
    
    The backend will start at:
    http://localhost:8080
   
2. Run the Android Mobile App
    1)Clone the Android application
    git clone https://github.com/asror812/android-fastfood-app.git
    cd android-fastfood-app
   2)Open the project in Android Studio
    File → Open → choose the project directory
    Wait for Gradle sync
    Make sure the emulator or device is ready
  3)Install & Run the App
    Click ▶ Run
    Choose emulator or phone
