import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DiscreteAuctionTest {
    public static final DiscreteAuction DISCRETE_AUCTION = new DiscreteAuction();


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

}