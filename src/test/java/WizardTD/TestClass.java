package WizardTD;


import processing.core.PApplet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestClass {

    @Test
    public void appTest() {
        App app = new App();
        assertNotNull(app);
        assertEquals(App.CELLSIZE, 32);
        assertEquals(App.SIDEBAR, 120);
        assertEquals(App.TOPBAR, 40);
        assertEquals(App.BOARD_WIDTH, 20);
        
        assertEquals(App.WIDTH, App.CELLSIZE * App.BOARD_WIDTH + App.SIDEBAR);
        assertEquals(App.HEIGHT, App.BOARD_WIDTH * App.CELLSIZE + App.TOPBAR);

        assertEquals(App.FPS, 60);
    }
}
