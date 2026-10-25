# Online Auction & Bidding System

**Developer:** R.Harish  
**Language:** Java  
**Application Type:** Console-based application

## Overview

The Online Auction & Bidding System is a Java console application that manages sellers, auction items, bidders, auctions, bids, winners, and payment settlements. It demonstrates object-oriented programming concepts and enforces basic auction rules.

## Features

- Add and manage sellers
- Add auction items with a base price
- Create auctions with start and end times
- Register bidders
- Place bids during the auction window
- Reject bids that do not exceed the current highest bid
- Close auctions and identify the highest bidder
- Generate a settlement receipt
- Display auction details and status

## Required Classes

- `AuctionItem` — stores item details, base price, and transfer status.
- `Seller` — stores seller information.
- `Bidder` — stores bidder information.
- `Auction` — manages the auction window, bids, highest bid, and winner.
- `Bid` — links a bid to its auction and bidder.
- `Winner` — stores winner details and final price.
- `Settlement` — records payment and item-transfer status.
- `OnlineAuction` — contains the main menu and application workflow.

## Requirements

- Java Development Kit (JDK) 8 or later
- Terminal or command prompt

## How to Run

1. Save the source code in a file named `OnlineAuction.java`.
2. Open a terminal in the directory containing the file.
3. Compile the program:

   ```bash
   javac OnlineAuction.java
   ```

4. Run the application:

   ```bash
   java OnlineAuction
   ```

## Menu Options

1. Add Seller
2. Add Item
3. Create Auction
4. Register Bidder
5. Place Bid
6. Close Auction
7. Settle Payment
8. Display Auctions
9. Exit

## Basic Usage

1. Add a seller.
2. Add an item using the seller ID.
3. Create an auction for the item and provide its start and end times.
4. Register one or more bidders.
5. Place bids while the auction is active. Each bid must be greater than the current highest bid, including the item's base price.
6. Close the auction after its end time.
7. Settle payment for the confirmed winner.
8. Display auctions to review their details and status.

Use the date/time format:

```text
yyyy-MM-dd HH:mm
```

Example:

```text
2026-10-25 20:30
```

Choose an auction start time that has already passed and an end time in the future when testing bid placement. To test closing, wait until the end time has passed.

## Business Rules

- Bids are accepted only during the auction's configured time window.
- Every new bid must exceed the current highest bid.
- The highest valid bid determines the winner when the auction closes.
- Settlement is available only after the auction closes and a winner exists.
- The bid object stores direct references to its auction and bidder.
- The item is marked as transferred after settlement.

## Important Notes

- This is an educational console simulation; it does not process real payments.
- The current implementation automatically confirms the winner when the auction closes.
- Data is stored in memory and is lost when the program exits.
- The current source code uses a manually triggered auction close; it does not close auctions automatically at the end time.
- The seller is selected when creating an auction; ensure the seller corresponds to the item owner.

## Example Output

```text
===== ONLINE AUCTION SYSTEM =====
1. Add Seller
2. Add Item
3. Create Auction
4. Register Bidder
5. Place Bid
6. Close Auction
7. Settle Payment
8. Display Auctions
9. Exit

Enter choice: 1
Enter seller name: R.Harish
Seller registered successfully.
```

## Future Improvements

- Store records in a database
- Add input validation for all fields
- Add seller-to-item ownership validation
- Add explicit winner confirmation before settlement
- Integrate a real payment gateway
- Automatically close auctions when the end time is reached
- Add authentication and authorization

---
**Project:** Online Auction & Bidding System  
**Author:** R.Harish
