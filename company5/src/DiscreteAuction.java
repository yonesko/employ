import java.util.*;
import java.util.stream.LongStream;

public class DiscreteAuction {
    AuctionResult run(Collection<Bid> bids) {
        if (bids.size() == 0) return new AuctionResult(0, 0);

        Map<Long, Integer> possibleAmounts = new HashMap<>();

        LongStream.rangeClosed(bids.stream().mapToLong(Bid::getPrice).min().getAsLong(),
                bids.stream().mapToLong(Bid::getPrice).max().getAsLong())
                .forEach(price -> possibleAmounts.put(price, amount(bids, price)));

        Map.Entry<Long, Integer> maxAmountEntry = possibleAmounts.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue)).get();

        double price = possibleAmounts.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), maxAmountEntry.getValue()))
                .mapToLong(Map.Entry::getKey)
                .average()
                .getAsDouble();

        return new AuctionResult(maxAmountEntry.getValue(), (long) Math.ceil(price));
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
