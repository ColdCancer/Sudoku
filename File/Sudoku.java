import java.util.ArrayList;
import java.util.Collections;

// Sudoku game class
class Sudoku {
    private int[][] number = new int[9][9];
    private int[] count = new int[9];

    Sudoku() {
        if(!randomlyGenerated()) {
            System.out.println("Data generation failed!");
        } else {
            System.out.println("------------------");
            for(int i = 0; i < 81; i++) {
                int x = i / 9, y = i % 9;
                System.out.print(number[x][y] + (y == 8 ? "\n" : " "));
            }
            System.out.println("------------------");
        }
    }

    void setNumber(int x, int y, int key) {
        number[x][y] = key;
    }
    int getNumber(int x, int y) {
        return number[x][y];
    }

    // Check filling: whether it is legal to fill key in Palace (x, y)
    boolean check(int x, int y, int key) {
        for(int i = 0; i < 9; i++) {
            if(i != y && number[x][i] == key) return false;
        }
        for(int i = 0; i < 9; i++) {
            if(i != x && number[i][y] == key) return false;
        }
        int blockX = x / 3 * 3, blockY = y / 3 * 3;
        for(int i = 0; i < 9; i++) {
            int dx = blockX + i / 3, dy = blockY + i % 3;
            if(x == dx && y == dy) continue;
            if(number[dx][dy] == key) return false;
        }
        return true;
    }

    // Data generation by backtracking: generate random legal data through deep search and backtracking
    private boolean search(int position) {
        if(position == 81) return true;
        int x = position / 9, y = position % 9;
        // Generate 1 - 9 random permutations
        ArrayList<Integer> randNumber = new ArrayList<>();
        for(int i = 0; i < 9; i++) randNumber.add(i + 1);
        Collections.shuffle(randNumber);
        // state transition
        for(int i = 0; i < 9; i++) {
            int key = randNumber.get(i);
            if(count[key - 1] != 0 && check(x, y, key)) {
                count[key - 1]--;
                number[x][y] = key;
                if(search(position + 1)) return true;
                // Backtracking process
                count[key - 1]++;
                number[x][y] = -1;
            }
        }
        return false;
    }

    // Random generation: generate legal data after initializing data
    private boolean randomlyGenerated() {
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                number[i][j] = -1;
            }
            count[i] = 9;
        }
        return search(0);
    }

}
