package gleb;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import static gleb.Main.NUM;
import static gleb.Util.indToPos;
import static gleb.Util.posToInd;
import static java.lang.Math.floor;
import static java.lang.Math.min;

public class Main {
    static int NUM = 6;

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();

        Board board = new Board();
        board.print();

        for (int i : board.getChessmen()) threads.add(new Thread(new Chessman(board, i)));

        for (Thread thread : threads) thread.start();

        for (Thread thread : threads) thread.join();

        System.out.println(T.n);
    }
}

class Chessman implements Runnable {
    private int cur;
    private final Board board;

    public Chessman(Board board, int cur) {
        this.board = board;
        this.cur = cur;
    }

    private int chooseTarget() {
        ArrayList<Integer> possible = new ArrayList<>();
        Pos pos = indToPos(cur);

        for (int i = 0; i < Board.COLS; i++) {
            int ind = posToInd(pos.r, i);
            if (ind != cur)
                possible.add(ind);
        }

        for (int i = 0; i < Board.ROWS; i++) {
            int ind = posToInd(i, pos.c);
            if (ind != cur)
                possible.add(ind);
        }


        possible.remove(Integer.valueOf(cur));

        return possible.get(Util.randInt(possible.size()));
    }

    private void makeStep() throws InterruptedException {
        while (true) {
            int to = chooseTarget();
            synchronized (board) {
                try {
                    if (board.tryMakeStep(cur, to)) {
                        cur = to;
                        return;
                    } else {
                        board.wait(5000);
                        if (board.tryMakeStep(cur, to)) {
                            cur = to;
                            return;
                        }
                    }
                } finally {
                    board.notify();
                }
            }
        }
    }

//    private void makeStep() throws InterruptedException {
//        while (true) {
//            long start = T.currentTimeMillis();
//            int to = chooseTarget();
//            while (true) {
//                if (T.currentTimeMillis() - start >= 5000) break;
//                synchronized (board) {
//                    while (!board.free) board.wait();
//                    board.free = false;
//                    try {
//                        if (board.tryMakeStep(cur, to)) {
//                            cur = to;
//                            return;
//                        }
//                    } finally {
//                        board.free = true;
//                        board.notify();
//                    }
//                }
//            }
//        }
//    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                if (i % 2 == 0) Thread.sleep(250);
                makeStep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class T {
    static volatile int n = 0;

    static long currentTimeMillis() {
        n++;
        return System.currentTimeMillis();
    }

}

class Pos {
    final int r, c;

    Pos(int r, int c) {
        this.r = r;
        this.c = c;
    }
}

class Util {
    private static Random random = new Random();

    static int randInt(int n) {
        return random.nextInt(n);
    }

    static Pos indToPos(int ind) {
        return new Pos((int) floor(ind / Board.COLS), ind % Board.COLS);
    }

    static int posToInd(int r, int c) {
        return Board.COLS * r + c;
    }

}

class Board {
    final static int ROWS = 8;
    final static int COLS = 8;
    volatile boolean free = true;

    private BitSet chessmen = new BitSet();

    Board() {
        ArrayList<Integer> possible = new ArrayList<>(ROWS * COLS);
        for (int i = 0; i < ROWS * COLS; i++) {
            possible.add(i);
        }

        for (int i = 0; i < NUM; i++) {
            int ind = Util.randInt(possible.size());
            chessmen.set(possible.get(ind));
            possible.remove(ind);
        }
    }

    boolean tryMakeStep(int from, int to) {
        Pos fromPos = indToPos(from), toPos = indToPos(to);

        if (fromPos.r != toPos.r && fromPos.c != toPos.c) return false;
        if (from == to) return false;

        if (fromPos.r == toPos.r)
            for (int c = min(fromPos.c, toPos.c); c <= Math.max(fromPos.c, toPos.c); c++)
                if (chessmen.get(posToInd(fromPos.r, c)) && c != fromPos.c)
                    return false;

        if (fromPos.c == toPos.c)
            for (int r = min(fromPos.r, toPos.r); r <= Math.max(fromPos.r, toPos.r); r++)
                if (chessmen.get(posToInd(r, fromPos.c)) && r != fromPos.r)
                    return false;

        chessmen.set(from, false);
        chessmen.set(to);
        assert to != from;
        assert chessmen.cardinality() == NUM;
        print(from, to);
        return true;
    }

    List<Integer> getChessmen() {
        ArrayList<Integer> ret = new ArrayList<>();
        for (int i = chessmen.nextSetBit(0); i >= 0; i = chessmen.nextSetBit(i + 1)) {
            ret.add(i);
        }
        return ret;
    }

    void print() {
        print(-1, -1);
    }

    void print(int from, int to) {
        StringBuilder sb = new StringBuilder();
        char rook = '♜';

        for (int r = ROWS - 1; r >= 0; r--) {
            sb.append(r);
            for (int ind, c = 0; c < COLS; c++) {
                ind = posToInd(r, c);
                sb.append('|');
                if (ind == to) sb.append("\u001B[34m").append(to < 10 ? " " + to : to).append("\u001B[0m");
                else if (chessmen.get(ind)) sb.append(ind < 10 ? " " + ind : ind);
                else if (ind == from) sb.append("..");
                else sb.append("  ");
            }
            sb.append('|').append(r).append('\n');

        }
        sb.append("  ");
        for (int i = 0; i < COLS; i++) sb.append(i).append("  ");

        System.out.println(sb.toString());
    }

}