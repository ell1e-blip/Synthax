package com.synthax.controller;

import com.synthax.model.oscillator.NoiseVoice;
import com.synthax.model.oscillator.Voice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Tests the NoiseController's ability to manage noise voices, including creation, activation,
 * deactivation, and gain control. These tests ensure the NoiseController's functionality
 * is reliable.
 * @author Oliver Berggren
 */
class NoiseControllerTest {
    private NoiseController noiseController;
    /**
     * Initializes a NoiseController with a set number of voices before each test to ensure
     * a consistent testing environment.
     * @author Oliver Berggren
     */
    @BeforeEach
    void setUp() {
        noiseController = new NoiseController(4);
    }

    /**
     * Validates that the NoiseController can successfully create a NoiseVoice and that the
     * created voice has the correct type and is not null, ensuring dynamic voice management.
     * @author Oliver Berggren
     */
    @Test
    void createVoice() {
        Voice voice = noiseController.createVoice(0);
        assertAll(
                () -> assertNotNull(voice, "The created voice should not be null."),
                () -> assertInstanceOf(NoiseVoice.class, voice, "The created voice should be an instance of NoiseVoice.")
        );
    }

    /**
     * Tests setting the gain within expected parameters and verifies the gain is accurately
     * applied, critical for controlling the amplitude of noise voices in synthesis processes.
     * Activation is required to ensure the gain is applied immediately, reflecting real usage scenarios.
     * @author Oliver Berggren
     */
    @Test
    void setGainNormal() {
        // Test normal behavior
        noiseController.setActive(true); // Ensure the controller is active to apply gain immediately
        float testGain = 0.5f;
        noiseController.setGain(testGain);
        assertEquals(testGain, noiseController.getGain(), "The gain should match the set value.");
    }

    /**
     * Ensures that setting the gain below the minimum expected value (0) is handled appropriately,
     * to prevent unexpected behavior when it's used.
     * @author Oliver Berggren
     */
    @Test
    void setGainBelow() {
        // Test below boundary condition
        noiseController.setGain(-1f); // Test with a gain lower than the expected range
        assertTrue(noiseController.getGain() >= 0, "The gain should not be less than 0.");

    }

    /**
     * Verifies that setting the gain above the maximum expected value is handled properly,
     * preventing distortion or other issues by not exceeding the maximum gain limit.
     * @author Oliver Berggren
     */
    @Test
    void setGainAbove() {
        // Test above boundary condition
        noiseController.setGain(2f); // Test with a gain higher than the expected range
        assertTrue(noiseController.getGain() <= NoiseController.MAX_GAIN, "The gain should not exceed MAX_GAIN.");
    }

    /**
     * Tests the NoiseController's ability to activate noise voices, ensuring they can be
     * correctly engaged for sound generation when it's used.
     * @author Oliver Berggren
     */
    @Test
    void setActiveActive() {
        // Test activating
        noiseController.setActive(true);
        assertTrue(noiseController.isActive(), "The controller should be active.");
    }

    /**
     * Examines the ability of the NoiseController to toggle between active and inactive states,
     * reflecting the controller's capacity to dynamically manage voice activation in response to user input or programmatic conditions.
     * @author Oliver Berggren
     */
    @Test
    void setActiveDeactive() {
        assertAll(
                () -> {
                    noiseController.setActive(true);
                    assertTrue(noiseController.isActive(), "The controller should be active.");
                },
                () -> {
                    // Test deactivating
                    noiseController.setActive(false);
                    assertFalse(noiseController.isActive(), "The controller should be inactive.");
                }
                );
    }

    /**
     * Confirms the initial gain setting of the NoiseController upon setup, ensuring a predictable
     * starting point for further gain adjustments when it's used.
     * @author Oliver Berggren
     */
    @Test
    void getGain() {
        // Assuming default gain is set in the setup, 0.5 for example
        assertEquals(0.5, noiseController.getGain(), "The initial gain should be 0.5.");
    }

    /**
     * Verifies the return of the output UGen from the NoiseController.
     * @author Oliver Berggren
     */
    @Test
    void getOutput() {
        assertNotNull(noiseController.getOutput(), "The output UGen should not be null.");
    }
}