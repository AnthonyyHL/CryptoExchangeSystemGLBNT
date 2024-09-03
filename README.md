# CryptoExchangeSystemGLBNT

## Summary

The Cryptocurrencies Exchange System is a console-based application designed to facilitate the trading of cryptocurrencies between users. The system models the core entities of a cryptocurrency exchange, including User, Crypto, Wallet, Exchange, Order, OrderBook, and Transaction.

## Getting Started

To run the application, simply execute the `main` method in the `CryptoExchangeApp` class. This will initialize the system and allow you to start interacting with the exchange.

### Prerequisites:
- JDK 17 or higher

### Pre-Registered Users:

- **User 1:**
    - **Email**: josehc@gmail.com
    - **Password**: 12345

- **User 2:**
    - **Email**: anthleon@gmail.com
    - **Password**: password1

You can use these accounts to log in and start placing orders on the exchange.

## Structure

- **User**: Represents the users of the system.
- **Crypto**: Represents the cryptocurrencies available for trading.
- **Wallet**: Manages the balances of different cryptocurrencies for each user.
- **Exchange**: The entity where users can buy cryptocurrencies directly from the system.
- **Order**: Represents buy/sell requests made by users. Orders act as tickets waiting for someone to fulfill the user's request.
- **OrderBook**: Keeps track of all active orders and matches buy and sell orders.
- **Transaction**: Records completed trades between users.

## Design Patterns

### Singleton:
The `Currency` class uses the Singleton pattern to ensure that each currency is instantiated only once throughout the system.

### Factory Method:
Used in conjunction with the Singleton pattern to create and manage instances of different currencies.

## Hexagonal Architecture

The application is structured using Hexagonal Architecture, which ensures a clean separation between the core business logic and user interface.

## UML Diagrams

UML diagrams for this project can be found in the `UMLDiagrams` folder.