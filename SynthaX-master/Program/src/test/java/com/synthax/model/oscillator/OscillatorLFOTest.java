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

    /**
     * @author Oliver Berggren
     * This is used to create an instance object OscillatorLFO to be able to do Unit tests
     * on that class and its functions.
     */
    @BeforeEach
    void setUp() {
        lfo = new OscillatorLFO();
    }

    /**
     * @author Oliver Berggren
     * This method tests the initial values or initilazed value of:
     * FrequencyModulation
     * PlayedFrequency
     * Depth
     * They are ran by assertAll which runs all the assert-tests.
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
     * This method sets the frequency value to a known frequency (440.0f).
     * Then it returns the value to verify if the value is set correctly.
     *
     * If test fail, the following will be printed:
     * "Played frequency should match what was set."
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
     * This method sets the rate value to a known rate (1.0f).
     * Then it returns the value to verify if the value is set correctly.
     *
     * If test fail, the following will be printed:
     * "The rate should match what was set."
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
     * This method sets the depth value to a known depth (0.5f).
     * Then the expected value is multiplied by 35 as done in the setDepth method.
     * Then it returns the value to verify if the value is set correctly
     *
     * If test fail, the following will be printed:
     * "Depth scaling should be correct."
     */
    @Test
    void setDepth() {
        // Set a known depth
        float testDepth = 0.5f; // Test depth
        lfo.setDepth(testDepth);
        assertEquals((testDepth * 35), lfo.getDepth(), "Depth scaling should be correct.");
    }
}