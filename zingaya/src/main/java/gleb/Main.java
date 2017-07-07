package gleb;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

import static gleb.Util.indToPos;
import static gleb.Util.posToInd;
import static java.lang.Math.floor;
import static java.lang.Math.min;

public class Main {
    static int NUM = 6;

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();

        Board board = new Board();
        board.print(-1);

        for (int i : board.getChessmen()) threads.add(new Thread(new Chessman(board, i)));

        long start = System.nanoTime();

        for (Thread thread : threads) thread.start();

        for (Thread thread : threads) thread.join();

        System.out.println("time: " + (System.nanoTime() - start) / 1e9 + " sec");
    }
}

class Chessman implements Runnable {
    private int cur;
    private Board board;

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
            long start = System.currentTimeMillis();
            int to = chooseTarget();
            while (true) {
                if (System.currentTimeMillis() - start >= 5000) break;
                if (board.tryMakeStep(cur, to)) {
                    cur = to;
                    return;
                }
            }
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 50; i++) {
            try {
                if (i % 2 == 0) Thread.sleep(250);
                board.wait();
                makeStep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    private BitSet chessmen = new BitSet();

    Board() {
        ArrayList<Integer> possible = new ArrayList<>(ROWS * COLS);
        for (int i = 0; i < ROWS * COLS; i++) {
            possible.add(i);
        }

        for (int i = 0; i < Main.NUM; i++) {
            int ind = Util.randInt(possible.size());
            chessmen.set(possible.get(ind));
            possible.remove(ind);
        }
    }

    synchronized boolean tryMakeStep(int from, int to) {
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
        print(from);
        return true;
    }

    List<Integer> getChessmen() {
        ArrayList<Integer> ret = new ArrayList<>();
        for (int i = chessmen.nextSetBit(0); i >= 0; i = chessmen.nextSetBit(i + 1)) {
            ret.add(i);
        }
        return ret;
    }

    public void print(int mark) {
        StringBuilder sb = new StringBuilder();
        char rook = '♜';

        for (int r = ROWS - 1; r >= 0; r--) {
            sb.append(r);
            for (int c = 0; c < COLS; c++) {
                sb.append('|');
                if (chessmen.get(posToInd(r, c)))
                    sb.append(posToInd(r, c) < 10 ? " " + posToInd(r, c) : posToInd(r, c));
                else if (mark == posToInd(r, c)) sb.append("fr");
                else sb.append("  ");
            }
            sb.append('|').append(r).append('\n');

        }
        sb.append("  ");
        for (int i = 0; i < COLS; i++) {
            sb.append(i).append("  ");
        }

        System.out.println(sb.toString());
    }

}