import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionResult that = (AuctionResult) o;
        return amount == that.amount &&
                price == that.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, price);
    }

    @Override
    public String toString() {
        return "AuctionResult{" +
                "amount=" + amount +
                ", price=" + price +
                '}';
    }
}
