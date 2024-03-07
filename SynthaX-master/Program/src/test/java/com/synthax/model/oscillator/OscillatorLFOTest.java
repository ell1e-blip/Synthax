package com.synthax.model.oscillator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Oliver Berggren
 * OscillatorLFOTest checks important functions and checks their result.
 * These tests are important for saving and loading presets.
 */
class OscillatorLFOTest {
    private OscillatorLFO lfo;
    /**
     * @author Oliver Berggren
     */
    @BeforeEach
    void setUp() {
        lfo = new OscillatorLFO();
    }

    /**
     * @author Oliver Berggren
     */
    @Test
    void testInitialConditions() {
        assertAll(
                () -> assertNotNull(lfo.getFrequencyModulation(), "Frequency modulation should be initialized"),
                () -> assertEquals(0f, lfo.getPlayedFrequency(), "Initially played frequency should be zero"),
                () -> assertEquals(0f, lfo.getDepth(), "Initially depth should be zero")
        );
    }
    /**
     * @author Oliver Berggren
     */
    @Test
    void setAndGetFrequencyModulation() {
        // Set a known frequency
        float testFrequency = 440.0f; // A4 note
        lfo.setPlayedFrequency(testFrequency);

        // Get the frequency and verify it is set correctly
        assertEquals(testFrequency, lfo.getPlayedFrequency(), "Played frequency should match what was set.");
    }

    /**
     * @author Oliver Berggren
     */
    @Test
    void setRate() {
        float testRate = 1.0f; // 1 Hz
        lfo.setRate(testRate);

        // Check if the LFO rate is set correctly
        assertEquals(lfo.convertRate(testRate), lfo.getRate(), "The rate should match what is being set");
    }

    /**
     * @author Oliver Berggren
     */
    @Test
    void setDepth() {
        // Set a known depth
        float testDepth = 0.5f; // Test depth
        lfo.setDepth(testDepth);
        assertEquals((testDepth * 35), lfo.getDepth(), "Depth scaling should be correct.");
    }
}