import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class DiscreteAuctionTest {
    private static final Random R = new Random();
    private static final DiscreteAuction DISCRETE_AUCTION = new DiscreteAuction();

    @Test
    public void noResult() throws Exception {
        AuctionResult actualResult = DISCRETE_AUCTION.run(Collections.emptyList());
        assertEquals(0, actualResult.getAmount());

        List<Bid> bids = Arrays.asList(
                new Bid(100, 1000, Direction.B),
                new Bid(150, 1010, Direction.S));
        actualResult = DISCRETE_AUCTION.run(bids);
        assertEquals(0, actualResult.getAmount());

        bids = Arrays.asList(
                new Bid(100, 1000, Direction.S),
                new Bid(150, 1010, Direction.S));
        actualResult = DISCRETE_AUCTION.run(bids);
        assertEquals(0, actualResult.getAmount());
    }

    @Test
    public void result() throws Exception {
        List<Bid> bids = Arrays.asList(
                new Bid(100, 1540, Direction.B),
                new Bid(100, 1530, Direction.B),
                new Bid(150, 1530, Direction.S));
        AuctionResult actualResult = DISCRETE_AUCTION.run(bids);

        assertEquals(new AuctionResult(150, 1530), actualResult);
    }

    @Test
    public void severalResult() throws Exception {
        List<Bid> bids = Arrays.asList(
                new Bid(1, 120, Direction.B),
                new Bid(1, 120, Direction.B),
                new Bid(2, 103, Direction.S));

        AuctionResult actualResult = DISCRETE_AUCTION.run(bids);

        assertEquals(new AuctionResult(2, 112), actualResult);
    }

    @Test(timeout = 1500)
    public void performance() {
        int bidsNumber = (int) 1e6;
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