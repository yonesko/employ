import java.util.*;
import java.util.stream.LongStream;

public class DiscreteAuction {
    AuctionResult run(Collection<Bid> bids) {
        Map<Long, Integer> amounts = new HashMap<>(10_000 - 100 + 1);

        LongStream.rangeClosed(100, 10_000).forEach(price -> amounts.put(price, amount(bids, price)));

        Optional<Map.Entry<Long, Integer>> max = amounts.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue));

        return max
                .filter(entry -> entry.getValue() > 0)
                .map(entry -> new AuctionResult(entry.getValue(), entry.getKey()))
                .orElseGet(() -> new AuctionResult(0, 0));
    }

    private int amount(Collection<Bid> bids, long price) {
        int buyAmount = bids.stream()
                .filter(bid -> bid.getDirection() == Direction.BUY)
                .filter(bid -> price <= bid.getPrice())
                .mapToInt(Bid::getAmount).sum();

        int sellAmount = bids.stream()
                .filter(bid -> bid.getDirection() == Direction.SELL)
                .filter(bid -> price >= bid.getPrice())
                .mapToInt(Bid::getAmount).sum();

        return Math.min(buyAmount, sellAmount);
    }
}
