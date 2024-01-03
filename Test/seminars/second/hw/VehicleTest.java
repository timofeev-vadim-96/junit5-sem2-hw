package seminars.second.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehicleTest {

    private Car car;
    private Motorcycle motorcycle;

    @BeforeEach
    void init(){
        car = new Car("Mazda", "CX-5", 2017);
        motorcycle = new Motorcycle("Ducati", "Panigale", 2020);
    }

    /**
     * Тест: экземпляр объекта Car также является экземпляром транспортного средства
     */
    @Test
    void testCarInheritance() {
        assertTrue(car instanceof Vehicle);
    }

    /**
     * Тест: объект Car создается с 4-мя колесами.
     */
    @Test
    void checkCarWheelsQuantity() {
        assertEquals(4, car.getNumWheels());
    }

    /**
     * Тест: объект Motorcycle создается с 2-мя колесами.
     */
    @Test
    void checkMotorcycleWheelsQuantity() {
        assertEquals(2, motorcycle.getNumWheels());
    }

    /**
     * Тест: объект Car развивает скорость 60 в режиме тестового вождения
     */
    @Test
    void checkCarSpeedDuringTestDrive(){
        car.testDrive();

        assertEquals(60, car.getSpeed());
    }

    /**
     * Тест: объект Motorcycle развивает скорость 75 в режиме тестового вождения
     */
    @Test
    void checkMotorcycleSpeedDuringTestDrive(){
        motorcycle.testDrive();

        assertEquals(75, motorcycle.getSpeed());
    }

    /**
     * Тест: в режиме парковки после тест-драйва машина останавливается
     */
    @Test
    void checkCarSpeedAfterParking(){
        car.testDrive();
        car.park();

        assertEquals(0, car.getSpeed());
    }

    /**
     * Тест: в режиме парковки после тест-драйва машина останавливается
     */
    @Test
    void checkMotorcycleSpeedAfterParking(){
        motorcycle.testDrive();
        motorcycle.park();

        assertEquals(0, motorcycle.getSpeed());
    }
}
