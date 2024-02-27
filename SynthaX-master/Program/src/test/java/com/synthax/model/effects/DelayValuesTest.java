package com.synthax.model.effects;

import com.synthax.MainApplication;
import com.synthax.controller.OscillatorManager;
import net.beadsproject.beads.ugens.Gain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/** @author Menel Abdennour
 * Investigating and testing the DelayValues class that are relevant in conjuction
 * with saving and loading the state of the application.
 * Boundary value analysis will also be tested.
 */
@ExtendWith(value = MockitoExtension.class)
class DelayValuesTest {
    SynthaxEQFilters filters;
    SynthaxReverb synthaxReverb;
    SynthaxLFO synthaxLFO;
    OscillatorManager oscillatorManager;
    MainApplication mainApplication;

    @BeforeEach
    void setUp() {
        mainApplication = new MainApplication();

        oscillatorManager = OscillatorManager.getInstance();
        Gain oscCombined = oscillatorManager.getFinalOutput();

        synthaxLFO = new SynthaxLFO();
        synthaxLFO.setInput(oscCombined);

        filters = new SynthaxEQFilters();
        filters.addInput(synthaxLFO.getOutput());

        synthaxReverb = new SynthaxReverb(filters.getOutput());
    }

    /**
     * --------- Methods belows are for testing Feedback using Boundary Value Analysis ---------
     */

    /**
     * To test one index below the max value of Feedback.
     */
    @Test
    void testDelayFeedbackBelowMax() {
        DelayValues delayValues = new DelayValues();
        delayValues.setFeedbackDuration(100.0f);
        assertEquals(100.0f, delayValues.getFeedbackDuration());
    }

    /**
     * To test with the max value of Feedback.
     */
    @Test
    void testDelayFeedbackAtMax() {
        DelayValues delayValues = new DelayValues();
        delayValues.setFeedbackDuration(100.0f);
        assertEquals(100.0f, delayValues.getFeedbackDuration());
    }

    /**
     * To test one index above the max value of Feedback.
     */
    @Test
    void testDelayFeedbackAboveMax() {
        DelayValues delayValues = new DelayValues();
        delayValues.setFeedbackDuration(100.0f);
        assertEquals(100.0f, delayValues.getFeedbackDuration());
    }

    /**
     * --------- Methods belows are for testing Time using Boundary Value Analysis ---------
     */

    /**
     * To test one index below the max value of Time.
     */
    @Test
    void testDelayTimeBelowMax() {
        DelayValues delayValues = new DelayValues();
        delayValues.setFeedbackDuration(100.0f);
        assertEquals(100.0f, delayValues.getFeedbackDuration());
    }

    /**
     * To test with the max value of Time.
     */
    @Test
    void testDelayTimeAtMax() {
        DelayValues delayValues = new DelayValues();
        delayValues.setFeedbackDuration(100.0f);
        assertEquals(100.0f, delayValues.getFeedbackDuration());
    }

    /**
     * To test one index above the max value of Time.
     */
    @Test
    void testDelayTimeAboveMax() {
        DelayValues delayValues = new DelayValues();
        delayValues.setFeedbackDuration(100.0f);
        assertEquals(100.0f, delayValues.getFeedbackDuration());
    }
    @Test
    void getOutput() {
    }

    @Test
    void setActive() {
    }

    @Test
    void setDelayTime() {
    }

    @Test
    void setDecay() {
    }

    @Test
    void setLevel() {
    }

    @Test
    void setFeedbackDuration() {
    }

    @Test
    void getEnvelope() {
    }

    @Test
    void getFeedbackDuration() {
    }

    @Test
    void getCachedDelayTime() {
    }
}