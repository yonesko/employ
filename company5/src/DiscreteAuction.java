import java.util.*;

public class DiscreteAuction {
    AuctionResult run(Collection<Bid> bids) {
        Map<Long, Integer> amounts = new HashMap<>();

        bids.stream().mapToLong(Bid::getPrice).distinct().forEach(price -> amounts.put(price, amount(bids, price)));

        Optional<Map.Entry<Long, Integer>> max = amounts.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));

        return max
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> new AuctionResult(entry.getValue(), entry.getKey()))
                .orElseGet(() -> new AuctionResult(0, 0));
    }

    private int amount(Collection<Bid> bids, long price) {
        int buyAmount = 0, sellAmount = 0;

        for (Bid bid : bids) {
            if (bid.getDirection() == Direction.BUY && price <= bid.getPrice()) buyAmount += bid.getAmount();
            if (bid.getDirection() == Direction.SELL && price >= bid.getPrice()) sellAmount += bid.getAmount();
        }

        return Math.min(buyAmount, sellAmount);
    }
}
