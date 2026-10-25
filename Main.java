
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

class AuctionItem {
    int id;
    String name;
    double basePrice;
    boolean transferred;

    AuctionItem(int id, String name, double basePrice) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.transferred = false;
    }
}

class Seller {
    int id;
    String name;

    Seller(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Bidder {
    int id;
    String name;

    Bidder(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

class Bid {
    int id;
    Auction auction;
    Bidder bidder;
    double amount;
    LocalDateTime time;

    Bid(int id, Auction auction, Bidder bidder, double amount) {
        this.id = id;
        this.auction = auction;
        this.bidder = bidder;
        this.amount = amount;
        this.time = LocalDateTime.now();
    }
}

class Winner {
    Bidder bidder;
    double finalPrice;
    boolean confirmed;

    Winner(Bidder bidder, double finalPrice) {
        this.bidder = bidder;
        this.finalPrice = finalPrice;
        this.confirmed = false;
    }
}

class Settlement {
    int id;
    Auction auction;
    Winner winner;
    boolean paid;
    boolean itemTransferred;

    Settlement(int id, Auction auction, Winner winner) {
        this.id = id;
        this.auction = auction;
        this.winner = winner;
        this.paid = false;
        this.itemTransferred = false;
    }

    void processPayment() {
        if (!winner.confirmed) {
            System.out.println("Winner confirmation is required.");
            return;
        }

        if (paid) {
            System.out.println("Payment already completed.");
            return;
        }

        paid = true;
        itemTransferred = true;
        auction.item.transferred = true;

        System.out.println("\n========== SETTLEMENT RECEIPT ==========");
        System.out.println("Receipt ID       : " + id);
        System.out.println("Auction ID       : " + auction.id);
        System.out.println("Item             : " + auction.item.name);
        System.out.println("Winner           : " + winner.bidder.name);
        System.out.printf("Final Price      : Rs. %.2f%n", winner.finalPrice);
        System.out.println("Payment Status   : PAID");
        System.out.println("Item Transfer    : COMPLETED");
        System.out.println("========================================");
    }
}

class Auction {
    int id;
    AuctionItem item;
    Seller seller;
    LocalDateTime startTime;
    LocalDateTime endTime;
    List<Bid> bids = new ArrayList<>();
    boolean closed;
    Winner winner;

    Auction(int id, AuctionItem item, Seller seller,
            LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.item = item;
        this.seller = seller;
        this.startTime = startTime;
        this.endTime = endTime;
        this.closed = false;
    }

    double getHighestBid() {
        double highest = item.basePrice;

        for (Bid bid : bids) {
            if (bid.amount > highest) {
                highest = bid.amount;
            }
        }

        return highest;
    }

    boolean placeBid(Bid bid) {
        LocalDateTime now = LocalDateTime.now();

        if (closed || now.isBefore(startTime) || !now.isBefore(endTime)) {
            System.out.println("Bidding is not allowed outside the auction window.");
            return false;
        }

        if (bid.bidder.id == -1) {
            return false;
        }

        if (bid.amount <= getHighestBid()) {
            System.out.printf("Bid must exceed Rs. %.2f%n", getHighestBid());
            return false;
        }

        bids.add(bid);

        System.out.println("Bid accepted successfully!");
        System.out.println("Bidder            : " + bid.bidder.name);
        System.out.printf("Current Highest   : Rs. %.2f%n", getHighestBid());

        return true;
    }

    void closeAuction() {
        if (closed) {
            System.out.println("Auction is already closed.");
            return;
        }

        if (LocalDateTime.now().isBefore(endTime)) {
            System.out.println("Auction cannot close before its end time.");
            return;
        }

        closed = true;

        System.out.println("\n========== AUCTION SUMMARY ==========");
        System.out.println("Auction ID : " + id);
        System.out.println("Item       : " + item.name);

        if (bids.isEmpty()) {
            System.out.println("No valid bids received.");
            System.out.println("Winner     : None");
        } else {
            Bid highest = bids.get(0);

            for (Bid bid : bids) {
                if (bid.amount > highest.amount) {
                    highest = bid;
                }
            }

            winner = new Winner(highest.bidder, highest.amount);

            System.out.println("Winner     : " + winner.bidder.name);
            System.out.printf("Final Price: Rs. %.2f%n", winner.finalPrice);
            winner.confirmed = true;
            System.out.println("Winner confirmed.");
        }

        System.out.println("Auction Status: CLOSED");
        System.out.println("=====================================");
    }
}

public class OnlineAuction {
    static Scanner sc = new Scanner(System.in);

    static List<Seller> sellers = new ArrayList<>();
    static List<AuctionItem> items = new ArrayList<>();
    static List<Bidder> bidders = new ArrayList<>();
    static List<Auction> auctions = new ArrayList<>();
    static List<Settlement> settlements = new ArrayList<>();

    static int sellerId = 1;
    static int itemId = 1;
    static int bidderId = 1;
    static int auctionId = 1;
    static int bidId = 1;
    static int settlementId = 1;

    static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    static int readInt(String message) {
        System.out.print(message);
        return Integer.parseInt(sc.nextLine().trim());
    }

    static double readDouble(String message) {
        System.out.print(message);
        return Double.parseDouble(sc.nextLine().trim());
    }

    static LocalDateTime readDate(String message) {
        System.out.print(message);
        return LocalDateTime.parse(sc.nextLine().trim(), formatter);
    }

    static Seller findSeller(int id) {
        for (Seller s : sellers)
            if (s.id == id) return s;
        return null;
    }

    static AuctionItem findItem(int id) {
        for (AuctionItem i : items)
            if (i.id == id) return i;
        return null;
    }

    static Bidder findBidder(int id) {
        for (Bidder b : bidders)
            if (b.id == id) return b;
        return null;
    }

    static Auction findAuction(int id) {
        for (Auction a : auctions)
            if (a.id == id) return a;
        return null;
    }

    static void addSeller() {
        System.out.print("Enter seller name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        sellers.add(new Seller(sellerId++, name));
        System.out.println("Seller registered successfully.");
    }

    static void addItem() {
        int id = readInt("Enter seller ID: ");
        Seller seller = findSeller(id);

        if (seller == null) {
            System.out.println("Seller not found.");
            return;
        }

        System.out.print("Enter item name: ");
        String name = sc.nextLine().trim();
        double price = readDouble("Enter base price: ");

        if (name.isEmpty() || price <= 0) {
            System.out.println("Enter a valid item name and positive price.");
            return;
        }

        AuctionItem item = new AuctionItem(itemId++, name, price);
        items.add(item);

        System.out.println("Item added successfully. Item ID: " + item.id);
        System.out.println("Seller: " + seller.name);
    }

    static void createAuction() {
        int id = readInt("Enter item ID: ");
        AuctionItem item = findItem(id);

        if (item == null || item.transferred) {
            System.out.println("Item not found or already transferred.");
            return;
        }

        for (Auction a : auctions) {
            if (a.item.id == item.id && !a.closed) {
                System.out.println("This item already has an active auction.");
                return;
            }
        }

        System.out.print("Enter seller ID: ");
        int sid = Integer.parseInt(sc.nextLine().trim());
        Seller seller = findSeller(sid);

        if (seller == null) {
            System.out.println("Seller not found.");
            return;
        }

        LocalDateTime start = readDate("Start time (yyyy-MM-dd HH:mm): ");
        LocalDateTime end = readDate("End time (yyyy-MM-dd HH:mm): ");

        if (!end.isAfter(start)) {
            System.out.println("End time must be after start time.");
            return;
        }

        Auction auction = new Auction(auctionId++, item, seller, start, end);
        auctions.add(auction);

        System.out.println("Auction created successfully. Auction ID: " + auction.id);
    }

    static void registerBidder() {
        System.out.print("Enter bidder name: ");
        String name = sc.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        bidders.add(new Bidder(bidderId++, name));
        System.out.println("Bidder registered successfully.");
    }

    static void placeBid() {
        int aid = readInt("Enter auction ID: ");
        Auction auction = findAuction(aid);

        if (auction == null) {
            System.out.println("Auction not found.");
            return;
        }

        int bid = readInt("Enter bidder ID: ");
        Bidder bidder = findBidder(bid);

        if (bidder == null) {
            System.out.println("Bidder not found.");
            return;
        }

        double amount = readDouble("Enter bid amount: ");

        if (amount <= 0) {
            System.out.println("Bid must be positive.");
            return;
        }

        Bid newBid = new Bid(bidId, auction, bidder, amount);

        if (auction.placeBid(newBid)) {
            bidId++;
        }
    }

    static void closeAuction() {
        int id = readInt("Enter auction ID: ");
        Auction auction = findAuction(id);

        if (auction == null) {
            System.out.println("Auction not found.");
            return;
        }

        auction.closeAuction();
    }

    static void settlePayment() {
        int id = readInt("Enter auction ID: ");
        Auction auction = findAuction(id);

        if (auction == null) {
            System.out.println("Auction not found.");
            return;
        }

        if (!auction.closed) {
            System.out.println("Close the auction before settlement.");
            return;
        }

        if (auction.winner == null) {
            System.out.println("No winner. Settlement is not possible.");
            return;
        }

        Settlement existing = null;

        for (Settlement s : settlements) {
            if (s.auction.id == auction.id) {
                existing = s;
                break;
            }
        }

        if (existing == null) {
            existing = new Settlement(settlementId++, auction, auction.winner);
            settlements.add(existing);
        }

        existing.processPayment();
    }

    static void displayAuctions() {
        if (auctions.isEmpty()) {
            System.out.println("No auctions available.");
            return;
        }

        for (Auction a : auctions) {
            System.out.println("\nAuction ID : " + a.id);
            System.out.println("Item       : " + a.item.name);
            System.out.println("Seller     : " + a.seller.name);
            System.out.printf("Base Price : Rs. %.2f%n", a.item.basePrice);
            System.out.printf("Highest Bid: Rs. %.2f%n", a.getHighestBid());
            System.out.println("Start      : " + a.startTime.format(formatter));
            System.out.println("End        : " + a.endTime.format(formatter));
            System.out.println("Status     : " + (a.closed ? "CLOSED" : "OPEN"));

            if (a.winner != null) {
                System.out.println("Winner     : " + a.winner.bidder.name);
            }

            System.out.println("Valid bids : " + a.bids.size());
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n===== ONLINE AUCTION SYSTEM =====");
            System.out.println("1. Add Seller");
            System.out.println("2. Add Item");
            System.out.println("3. Create Auction");
            System.out.println("4. Register Bidder");
            System.out.println("5. Place Bid");
            System.out.println("6. Close Auction");
            System.out.println("7. Settle Payment");
            System.out.println("8. Display Auctions");
            System.out.println("9. Exit");

            try {
                int choice = readInt("Enter choice: ");

                switch (choice) {
                    case 1: addSeller(); break;
                    case 2: addItem(); break;
                    case 3: createAuction(); break;
                    case 4: registerBidder(); break;
                    case 5: placeBid(); break;
                    case 6: closeAuction(); break;
                    case 7: settlePayment(); break;
                    case 8: displayAuctions(); break;
                    case 9:
                        System.out.println("Thank you for using Online Auction System.");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please enter a valid value.");
            } catch (DateTimeException e) {
                System.out.println("Invalid date/time. Use yyyy-MM-dd HH:mm.");
            }
        }
    }
}
