# 🍔 FastFood Android App
A modern Android application for browsing meals, managing favorites, and placing orders.

## 📱 Overview
FastFood Android App is a mobile food ordering client built using Java + Android Studio, integrated with a Spring Boot backend.
Users can browse categories, view products, add to cart, mark favorites, and authenticate securely using JWT.

## 🔐 Authentication

- Phone number login
- JWT-based authentication
- Secure token storage

<img width="266" height="616" alt="image" src="https://github.com/user-attachments/assets/9c11c30a-333b-4a5f-a09c-4d99f299f937" />

## 🛒 Menu & Products

- Load categories from backend
- View product list (Lavash, Pizza, Burgers...)
- Glide image loading

<img width="266" height="616" alt="image" src="https://github.com/user-attachments/assets/3a59a235-4f35-481d-b73c-54e13b50627b" />

## ❤️ Favorites
- Add/remove product to favorites
- Favorite icon synced with backend
- Smooth UI updates

## 🧺 Cart
- Add to cart
- Remove product from cart
- Recalculate total price
- Save cart state

## 🛠 Tech Stack
- Frontend (Mobile)
- Java
- Android Studio
- Retrofit2 → REST API client
- Glide → Image loading
- RecyclerView, CardView, GridLayout
- Material Components

## Authentication
- JWT Token stored using SharedPreferences

## 🧪 How to Run

### Start backend API

1. Clone the backend repository

```bash
git clone https://github.com/asror812/fastfood-api-spring.git
cd fastfood-api-spring
```

2. Start required services (Redis + PostgreSQL)
```bash
docker compose up -d
```    

3. Check running containers:
```bash
docker ps
```    

4. Run the Spring Boot application
Using IDEA or

```bash
./gradlew bootRun
```    
The backend will start at: <http://localhost:8080>
   
### Run the Android Mobile App

1. Clone the Android application
```bash
git clone https://github.com/asror812/android-fastfood-app.git
cd android-fastfood-app
```

2. Open the project in Android Studio

File → Open → choose the project directory
Wait for Gradle sync
Make sure the emulator or device is ready

3. Install & Run the App
Click ▶ Run
Choose emulator or phone
