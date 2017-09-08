public class AuctionResult {
    private final int amount;
    private final long price;

    public AuctionResult(int amount, long price) {
        this.amount = amount;
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public long getPrice() {
        return price;
    }
}
