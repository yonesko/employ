public class Bid {
    private final int amount;
    private final long price;
    private final Direction direction;

    public Bid(int amount, long price, Direction direction) {
        this.amount = amount;
        this.price = price;
        this.direction = direction;
    }

    public int getAmount() {
        return amount;
    }

    public long getPrice() {
        return price;
    }

    public Direction getDirection() {
        return direction;
    }
}
