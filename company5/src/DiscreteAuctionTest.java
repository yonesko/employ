import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DiscreteAuctionTest {
    private static final Random R = new Random();
    private static final DiscreteAuction DISCRETE_AUCTION = new DiscreteAuction();

    @Test
    public void noResult() throws Exception {
        AuctionResult auctionResult = DISCRETE_AUCTION.run(Collections.emptyList());
        assertEquals(0, auctionResult.getAmount());

        List<Bid> bids = Arrays.asList(
                new Bid(100, 1000, Direction.BUY),
                new Bid(150, 1010, Direction.SELL));
        auctionResult = DISCRETE_AUCTION.run(bids);
        assertEquals(0, auctionResult.getAmount());
    }

    @Test
    public void result() throws Exception {
        List<Bid> bids = Arrays.asList(
                new Bid(100, 1540, Direction.BUY),
                new Bid(100, 1530, Direction.BUY),
                new Bid(150, 1530, Direction.SELL));
        AuctionResult auctionResult = DISCRETE_AUCTION.run(bids);
        assertEquals(150, auctionResult.getAmount());
        assertEquals(1530, auctionResult.getPrice());
    }

    @Test
    public void performance() {
        int bidsNumber = (int) 1e5;
        List<Bid> bids = new ArrayList<>(bidsNumber);

        for (int i = 0; i < bidsNumber; i++)
            bids.add(new Bid(randAmount(), randPrice(), randDirection()));

        long start = System.nanoTime();
        DISCRETE_AUCTION.run(bids);
        System.out.println(String.format("ms. per bid:%f", (System.nanoTime() - start) / bidsNumber / 1e6));
    }

    private int randAmount() {
        return 1 + R.nextInt(1000);
    }

    private long randPrice() {
        return (1 + R.nextInt(100)) * 100;
    }

    private Direction randDirection() {
        return Direction.values()[R.nextInt(Direction.values().length)];
    }
}