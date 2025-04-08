import java.util.*;

// Lớp giải quyết Sudoku
public class SudokuSolver {
    private static final int SIZE = 9; // Kích thước của bảng Sudoku (9x9)
    private int[][] grid; // Lưới Sudoku
    private Map<String, Set<Integer>> domains; // Miền giá trị khả dĩ cho từng ô

    // Hàm khởi tạo, nhận vào lưới Sudoku
    public SudokuSolver(int[][] grid) {
        this.grid = grid; // Gán lưới Sudoku
        this.domains = new HashMap<>(); // Khởi tạo miền giá trị
        initializeDomains(); // Khởi tạo miền giá trị cho các ô trống
    }

    // Hàm khởi tạo miền giá trị cho các ô trống
    private void initializeDomains() {
        for (int row = 0; row < SIZE; row++) { // Duyệt qua từng hàng
            for (int col = 0; col < SIZE; col++) { // Duyệt qua từng cột
                if (grid[row][col] == 0) { // Nếu ô trống (giá trị là 0)
                    Set<Integer> possible = getValidValues(row, col); // Lấy các giá trị hợp lệ
                    domains.put(row + "," + col, possible); // Lưu miền giá trị vào map
                }
            }
        }
    }

    // Hàm lấy các giá trị hợp lệ cho một ô
    private Set<Integer> getValidValues(int row, int col) {
        Set<Integer> values = new HashSet<>(); // Tập hợp các giá trị từ 1 đến 9
        for (int i = 1; i <= 9; i++)
            values.add(i);

        // Loại bỏ các giá trị đã xuất hiện trong hàng và cột
        for (int i = 0; i < SIZE; i++) {
            values.remove(grid[row][i]); // Loại bỏ giá trị trong hàng
            values.remove(grid[i][col]); // Loại bỏ giá trị trong cột
        }

        // Loại bỏ các giá trị đã xuất hiện trong ô 3x3
        int startRow = row - row % 3; // Xác định hàng bắt đầu của ô 3x3
        int startCol = col - col % 3; // Xác định cột bắt đầu của ô 3x3
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                values.remove(grid[r][c]); // Loại bỏ giá trị trong ô 3x3
            }
        }

        return values; // Trả về tập hợp các giá trị hợp lệ
    }

    // Hàm chọn ô có miền giá trị nhỏ nhất (MRV - Minimum Remaining Values)
    private int[] selectCellWithMRV() {
        int minSize = Integer.MAX_VALUE; // Kích thước miền giá trị nhỏ nhất
        int[] selected = null; // Ô được chọn

        for (String key : domains.keySet()) { // Duyệt qua các ô trong miền giá trị
            int size = domains.get(key).size(); // Lấy kích thước miền giá trị của ô
            if (size < minSize) { // Nếu kích thước nhỏ hơn kích thước hiện tại
                minSize = size; // Cập nhật kích thước nhỏ nhất
                String[] parts = key.split(","); // Tách hàng và cột từ key
                selected = new int[] { Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) }; // Lưu ô được chọn
            }
        }

        return selected; // Trả về ô có miền giá trị nhỏ nhất
    }

    // Hàm giải Sudoku bằng backtracking kết hợp MRV và forward checking
    public boolean solve() {
        if (domains.isEmpty()) // Nếu không còn ô trống
            return true; // Sudoku đã được giải

        int[] cell = selectCellWithMRV(); // Chọn ô có miền giá trị nhỏ nhất
        if (cell == null) // Nếu không có ô nào được chọn
            return true; // Sudoku đã được giải

        int row = cell[0], col = cell[1]; // Lấy hàng và cột của ô được chọn
        String key = row + "," + col; // Tạo key cho ô
        Set<Integer> candidates = new HashSet<>(domains.get(key)); // Lấy các giá trị khả dĩ của ô

        for (int value : candidates) { // Duyệt qua từng giá trị khả dĩ
            if (isValid(row, col, value)) { // Kiểm tra giá trị có hợp lệ không
                grid[row][col] = value; // Gán giá trị vào ô

                Map<String, Set<Integer>> removed = new HashMap<>(); // Lưu các giá trị bị loại bỏ
                domains.remove(key); // Loại bỏ ô khỏi miền giá trị

                boolean forwardOk = forwardCheck(row, col, value, removed); // Kiểm tra forward checking

                if (forwardOk && solve()) // Nếu forward checking thành công và giải được Sudoku
                    return true; // Trả về thành công

                grid[row][col] = 0; // Quay lui, đặt lại giá trị ô
                restoreDomains(removed); // Khôi phục miền giá trị
                domains.put(key, candidates); // Đưa ô trở lại miền giá trị
            }
        }

        return false; // Trả về thất bại nếu không giải được
    }

    // Hàm kiểm tra giá trị có hợp lệ không
    private boolean isValid(int row, int col, int value) {
        for (int i = 0; i < SIZE; i++) { // Kiểm tra hàng và cột
            if (grid[row][i] == value || grid[i][col] == value)
                return false; // Giá trị không hợp lệ
        }

        int startRow = row - row % 3; // Xác định hàng bắt đầu của ô 3x3
        int startCol = col - col % 3; // Xác định cột bắt đầu của ô 3x3
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                if (grid[r][c] == value)
                    return false; // Giá trị không hợp lệ
            }
        }

        return true; // Giá trị hợp lệ
    }

    // Hàm kiểm tra forward checking
    private boolean forwardCheck(int row, int col, int value, Map<String, Set<Integer>> removed) {
        for (int i = 0; i < SIZE; i++) { // Duyệt qua hàng và cột
            updateDomain(row, i, value, removed); // Cập nhật miền giá trị trong hàng
            updateDomain(i, col, value, removed); // Cập nhật miền giá trị trong cột
        }

        int startRow = row - row % 3; // Xác định hàng bắt đầu của ô 3x3
        int startCol = col - col % 3; // Xác định cột bắt đầu của ô 3x3
        for (int r = startRow; r < startRow + 3; r++) {
            for (int c = startCol; c < startCol + 3; c++) {
                updateDomain(r, c, value, removed); // Cập nhật miền giá trị trong ô 3x3
            }
        }

        for (String key : domains.keySet()) { // Kiểm tra nếu có miền giá trị rỗng
            if (domains.get(key).isEmpty())
                return false; // Forward checking thất bại
        }

        return true; // Forward checking thành công
    }

    // Hàm cập nhật miền giá trị của một ô
    private void updateDomain(int row, int col, int value, Map<String, Set<Integer>> removed) {
        String key = row + "," + col; // Tạo key cho ô
        if (!domains.containsKey(key)) // Nếu ô không có trong miền giá trị
            return;

        Set<Integer> domain = domains.get(key); // Lấy miền giá trị của ô
        if (domain.contains(value)) { // Nếu miền giá trị chứa giá trị cần loại bỏ
            domain.remove(value); // Loại bỏ giá trị

            if (!removed.containsKey(key)) { // Nếu ô chưa có trong danh sách bị loại bỏ
                removed.put(key, new HashSet<>()); // Thêm ô vào danh sách
            }

            removed.get(key).add(value); // Thêm giá trị bị loại bỏ vào danh sách
        }
    }

    // Hàm khôi phục miền giá trị
    private void restoreDomains(Map<String, Set<Integer>> removed) {
        for (String key : removed.keySet()) { // Duyệt qua các ô trong danh sách bị loại bỏ
            if (!domains.containsKey(key)) { // Nếu ô không có trong miền giá trị
                domains.put(key, new HashSet<>()); // Thêm ô vào miền giá trị
            }
            domains.get(key).addAll(removed.get(key)); // Khôi phục các giá trị bị loại bỏ
        }
    }

    // Hàm in lưới Sudoku
    public void printGrid() {
        for (int r = 0; r < SIZE; r++) { // Duyệt qua từng hàng
            for (int c = 0; c < SIZE; c++) { // Duyệt qua từng cột
                System.out.print(grid[r][c] + " "); // In giá trị của ô
            }
            System.out.println(); // Xuống dòng sau mỗi hàng
        }
    }

    public static void main(String[] args) {
        int[][] puzzle = { // Lưới Sudoku cần giải
                { 5, 3, 0, 0, 7, 0, 0, 0, 0 },
                { 6, 0, 0, 1, 9, 5, 0, 0, 0 },
                { 0, 9, 8, 0, 0, 0, 0, 6, 0 },
                { 8, 0, 0, 0, 6, 0, 0, 0, 3 },
                { 4, 0, 0, 8, 0, 3, 0, 0, 1 },
                { 7, 0, 0, 0, 2, 0, 0, 0, 6 },
                { 0, 6, 0, 0, 0, 0, 2, 8, 0 },
                { 0, 0, 0, 4, 1, 9, 0, 0, 5 },
                { 0, 0, 0, 0, 8, 0, 0, 7, 9 }
        };

        SudokuSolver solver = new SudokuSolver(puzzle); // Tạo đối tượng giải Sudoku

        if (solver.solve()) { // Nếu giải được Sudoku
            System.out.println("Sudoku Solved:"); // In thông báo
            solver.printGrid(); // In lưới Sudoku đã giải
        } else {
            System.out.println("No solution found."); // In thông báo nếu không giải được
        }
    }
}
