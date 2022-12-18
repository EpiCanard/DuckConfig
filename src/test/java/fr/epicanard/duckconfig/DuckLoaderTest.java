package fr.epicanard.duckconfig;

import fr.epicanard.duckconfig.annotations.ResourceLocation;
import fr.epicanard.duckconfig.annotations.ResourceWrapper;
import fr.epicanard.duckconfig.models.Obj;
import fr.epicanard.duckconfig.models.Plop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DuckLoaderTest {
    @Test
    @DisplayName("Test decoding of a full object")
    void loadTOObject() {
        Obj result = DuckLoader.load(Obj.class, new ResourceWrapper("yaml", "object.yml", ResourceLocation.CLASS_PATH));

        assertNotNull(result);
        assertEquals(result.Plop.A, "AA");
        assertEquals(result.Plop.B, "BB");
    }

    @Test
    @DisplayName("Test decoding of a map of object")
    void loadTOMap() {
        Map<String, Plop> result = DuckLoader.loadMap(Plop.class, new ResourceWrapper("yaml", "map.yml", ResourceLocation.CLASS_PATH));

        assertNotNull(result);
        assertEquals(result.size(), 4);
        assertEquals(result.get("cop").A, "AA");
        assertEquals(result.get("cop").B, "BB");
    }
}
