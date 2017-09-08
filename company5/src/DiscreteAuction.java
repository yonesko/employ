import java.util.*;

public class DiscreteAuction {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Collection<Bid> bids = new ArrayList<>();

        while (sc.hasNext()) {
            String d = sc.next();
            int amount = sc.nextInt();
            double price = sc.nextDouble();

            bids.add(new Bid(amount, (long) (price * 100), Direction.valueOf(d)));
        }

        AuctionResult auctionResult = new DiscreteAuction().run(bids);

        if (auctionResult.getAmount() == 0) System.out.println("0 n/a");
        else System.out.println(String.format("%d %.2f", auctionResult.getAmount(), auctionResult.getPrice() / 100.));
    }

    AuctionResult run(Collection<Bid> bids) {
        if (bids.size() == 0) return new AuctionResult(0, 0);

        Map<Long, Integer> possibleResults = new HashMap<>();

        bids.stream().mapToLong(Bid::getPrice).distinct()
                .forEach(price -> possibleResults.put(price, amount(bids, price)));

        Map.Entry<Long, Integer> maxAmountEntry = possibleResults.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue)).get();

        double price = possibleResults.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), maxAmountEntry.getValue()))
                .mapToLong(Map.Entry::getKey)
                .average()
                .getAsDouble();

        return new AuctionResult(maxAmountEntry.getValue(), (long) Math.ceil(price));
    }

    private int amount(Collection<Bid> bids, long price) {
        int buyAmount = 0, sellAmount = 0;

        for (Bid bid : bids) {
            if (bid.getDirection() == Direction.B && price <= bid.getPrice()) buyAmount += bid.getAmount();
            if (bid.getDirection() == Direction.S && price >= bid.getPrice()) sellAmount += bid.getAmount();
        }

        return Math.min(buyAmount, sellAmount);
    }
}
