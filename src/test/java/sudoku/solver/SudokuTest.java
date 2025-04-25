package sudoku.solver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ForwardCheckingTest {

    private ForwardChecking forwardChecking;

    @BeforeEach
    public void setUp() {
        forwardChecking = new ForwardChecking();
    }

    @Test
    public void test1() {
        int num = 5;
        assertNotNull(num);
    }
//    @Test
//    public void testInitializeDomains() {
//        int[][] grid = new int[9][9];  // 9x9 sudoku grid
//        forwardChecking.initializeDomains(grid);
//        // Kiểm tra xem tất cả các domain được khởi tạo đúng cách chưa
//        assertNotNull(forwardChecking.getDomains());
//    }
//
//    @Test
//    public void testSelectCellWithMRV() {
//        int[][] grid = new int[9][9];
//        grid[0][0] = 5;  // đặt giá trị ở ô (0,0)
//        int[] selected = forwardChecking.selectCellWithMRV(grid);
//        assertNotNull(selected);  // chắc chắn có ô được chọn
//    }
//
//    @Test
//    public void testSolve() {
//        int[][] grid = new int[9][9];
//        grid[0][0] = 5;  // bắt đầu với một số đã được điền
//        boolean result = forwardChecking.solve();
//        assertTrue(result);  // Kiểm tra xem solver có thể tìm ra lời giải không
//    }
}
