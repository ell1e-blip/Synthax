package com.synthax.model.effects;

import com.synthax.MainApplication;
import com.synthax.controller.OscillatorManager;
import com.synthax.model.effects.SynthaxDelay;
import com.synthax.model.effects.SynthaxEQFilters;
import com.synthax.model.effects.SynthaxLFO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.beadsproject.beads.ugens.Gain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/** @author Menel Abdennour
 * Investigating and testing the DelayValues class that are relevant in conjuction
 * with saving and loading the state of the application.
 * Boundary value analysis will also be tested.
 */
@ExtendWith(value = MockitoExtension.class)
class DelayValuesTest {
    SynthaxEQFilters filters;
    SynthaxDelay synthaxDelay;
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

        synthaxDelay = new SynthaxDelay(filters.getOutput());
    }
    
    /*** --------- Methods belows are for testing Feedback using Boundary Value Analysis --------- */
    /**

    /**
     * To test one index above the max value of Feedback.
     * Same issue stated below. It's returning 100.0 when it's supposed to be 2500. Claiming that 100 is its max value which is a mismatch to
     * the values I retrieved when I ran SynthaX.
     */
    @Test
    void testDelayFeedbackAboveMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setFeedbackDuration(newValue.floatValue());
        };

        float t1 = 2500.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2500) {
            t1 = 2500;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(2500, synthaxDelay.getFeedbackDuration());
    }

    /**
     * To test with the max value of Feedback.
     * Returns 100.0 when it's supposed to return 2500. Seems to be that 100 is its max value.
     */
    @Test
    void testDelayFeedbackAtMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setFeedbackDuration(newValue.floatValue());
        };

        float t1 = 2500;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2500) {
            t1 = 2500;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(2500, synthaxDelay.getFeedbackDuration());
    }

    /**
     * To test one index below the max value of Feedback.
     * Something is not correct here. When I ran SynthaX, I had it to print out the value of the feedback duration and I used those values as
     * limits for the test. For some reason it's returning 100.0.
     */
    @Test
    void testDelayFeedbackBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setFeedbackDuration(newValue.floatValue());
        };

        float t1 = 2499.9F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2500) {
            t1 = 2500;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(2499.9F, synthaxDelay.getFeedbackDuration());
    }

    /**
     * To test one index at the middle value of Feedback.
     * Returns 100 when it's supposed to return 1250. Unsure why as it's a mismatch to the values I retrieved earlier when I ran SynthaX.
     */
    @Test
    void testDelayFeedbackMiddle() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setFeedbackDuration(newValue.floatValue());
        };

        float t1 = 1250F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2500) {
            t1 = 2500;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(1250F, synthaxDelay.getFeedbackDuration());
    }

    /**
     * To test one index above the min value of Feedback.
     * Here it is returning 100.0, which is not correct. I suspect that the feedbacks duration might be limited to 100 in fact. The only problem however
     * was that when I ran SynthaX to retrieve the boundary valeus, it explicitly showed to me that its limit was 2500 and 100, not that 100 was the max limit.
     */
    @Test
    void testDelayFeedbackAboveMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setFeedbackDuration(newValue.floatValue());
        };

        float t1 = 100.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2500) {
            t1 = 2500;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(100.01F, synthaxDelay.getFeedbackDuration());
    }

    /**
     * To test one index at the min value of Feedback.
     *  Test returned as OK.
     */
    @Test
    void testDelayFeedbackAtMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setFeedbackDuration(newValue.floatValue());
        };

        float t1 = 100F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2500) {
            t1 = 2500;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(100, synthaxDelay.getFeedbackDuration());
    }

    /**
     * To test one index below the min value of Feedback.
     *  Test returned as OK.
     */
    @Test
    void testDelayFeedbackBelowMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setFeedbackDuration(newValue.floatValue());
        };

        float t1 = 99.9F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2500) {
            t1 = 2500;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(100, synthaxDelay.getFeedbackDuration());
    }

    /**
     * --------- Methods belows are for testing Time using Boundary Value Analysis ---------
     */

    /**
     * To test one index above the max value of Time.
     * Returns 900190.0 when it's supposed to return 1000. This means that its max value is above or equal to 900190.0.
     * Seems to be a parsing error.
     */
    @Test
    void testDelayTimeAboveMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDelayTime(newValue.floatValue());
        };

        float t1 = 1000.1F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1000) {
            t1 = 1000;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(1000, synthaxDelay.getCachedDelayTime());
    }

    /**
     * To test one index at the max value of Time.
     * Returns 900100.0 when it's supposed to return 1000. This means that its max value is above or equal to 900100.0.
     */
    @Test
    void testDelayTimeAtMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDelayTime(newValue.floatValue());
        };

        float t1 = 1000;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1000) {
            t1 = 1000;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(1000, synthaxDelay.getCachedDelayTime());
    }


    /**
     * To test one index below the max value of Time.
     * Same issue here. Its returning 900010.0 when it's supposed to return 999.9. This means that its max value is above or equal to 900010.0.
     * I believe the issue may have to do with parsing.
     */
    @Test
    void testDelayTimeBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDelayTime(newValue.floatValue());
        };

        float t1 = 999.9F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1000) {
            t1 = 1000;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(999.9, synthaxDelay.getCachedDelayTime());
    }

    /**
     * To test one index at the middle value of Time.
     * Returns 450100.0 which is incorrect!! Why the numbers are so high I am unsure of.
     */
    @Test
    void testDelayTimeMiddle() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDelayTime(newValue.floatValue());
        };

        float t1 = 500F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1000) {
            t1 = 1000;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(500, synthaxDelay.getCachedDelayTime());
    }

    /**
     * To test one index above the min value of Time.
     * Here its showing 90109.0 which is incorrect, it's supposed to return 100.01. This means that its max value is above or equal to 90109.0.
     */
    @Test
    void testDelayTimeAboveMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDelayTime(newValue.floatValue());
        };

        float t1 = 100.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1000) {
            t1 = 1000;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(100.01F, synthaxDelay.getCachedDelayTime());
    }

    /**
     * To test one index at the min value of Time.
     * Returns 90100.0 which is incorrect.
     */
    @Test
    void testDelayTimeAtMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDelayTime(newValue.floatValue());
        };

        float t1 = 100F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1000) {
            t1 = 1000;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(100, synthaxDelay.getCachedDelayTime());
    }

    /**
     * To test one index below the min value of Time.
     * Here it's returning 90010.0 which is an anomaly. It's supposed to return 100. This means that its max value is above or equal to 90010.0.
     */
    @Test
    void testDelayTimeBelowMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDelayTime(newValue.floatValue());
        };

        float t1 = 99.9F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1000) {
            t1 = 1000;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(99.9, synthaxDelay.getCachedDelayTime());
    }

    /**
     * --------- Methods belows are for testing Decay using Boundary Value Analysis ---------
     */

    /**
     * To test one index above the max value of Decay.
     * Returns 1.01 which means that 1 is not it's limit. It's supposed to return 1 according to the values I retrieved when I ran SynthaX.
     */
    @Test
    void testDelayDecayAboveMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDecay(newValue.floatValue());
        };

        float t1 = 1.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(1, synthaxDelay.getCachedDecayValue());
    }

    /**
     * To test one index at the max value of Decay.
     * Returns 0.0 when it's supposed to return 1. Why it does that I am not sure.
     */
    @Test
    void testDelayDecayMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDecay(newValue.floatValue());
        };

        float t1 = 1;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(1, synthaxDelay.getCachedDelayTime());
    }

    /**
     * To test one index below the max value of Decay.
     *  Test returned as OK.
     */
    @Test
    void testDelayDecayBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDecay(newValue.floatValue());
        };

        float t1 = 0.99F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.99F, synthaxDelay.getCachedDecayValue());
    }

    /**
     * To test one index at the middle value of Decay.
     * Test returned as OK.
     */
    @Test
    void testDelayDecayMiddle() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDecay(newValue.floatValue());
        };

        float t1 = 0.5F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.5F, synthaxDelay.getCachedDecayValue());
    }

    /**
     * To test one index above the min value of Decay.
     *  Test returned as OK.
     */
    @Test
    void testDelayDecayAboveMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDecay(newValue.floatValue());
        };

        float t1 = 0.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.01F, synthaxDelay.getCachedDecayValue());
    }

    /**
     * To test one index at the min value of Decay.
     * Test returned as OK.
     */
    @Test
    void testDelayDecayAtMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDecay(newValue.floatValue());
        };

        float t1 = 0F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0, synthaxDelay.getCachedDecayValue());
    }

    /**
     * To test one index below the min value of Decay.
     * Returns -0.01 when it's supposed to return 0.0. Unsure why.
     */
    @Test
    void testDelayDecayBelowMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setDecay(newValue.floatValue());
        };

        float t1 = -0.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0, synthaxDelay.getCachedDecayValue());
    }

    /**
     * --------- Methods belows are for testing Level using Boundary Value Analysis ---------
     */

    /**
     * To test one index above the max value of Level.
     * Returns 1.01 which means that 1 is not it's limit. It's supposed to return 1 according to the values I retrieved when I ran SynthaX.
     */
    @Test
    void testDelayLevelAboveMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setLevel(newValue.floatValue());
        };

        float t1 = 1.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(1, synthaxDelay.getCachedLevelValue());
    }

    /**
     * To test one index at the max value of Level.
     *  Test returned as OK.
     */
    @Test
    void testDelayLevelMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setLevel(newValue.floatValue());
        };

        float t1 = 1;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(1, synthaxDelay.getCachedLevelValue());
    }

    /**
     * To test one index below the max value of Level.
     *  Test returned as OK.
     */
    @Test
    void testDelayLevelBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setLevel(newValue.floatValue());
        };

        float t1 = 0.99F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.99F, synthaxDelay.getCachedLevelValue());
    }

    /**
     * To test one index at the middle value of Level.
     *  Test returned as OK.
     */
    @Test
    void testDelayLevelMiddle() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setLevel(newValue.floatValue());
        };

        float t1 = 0.5F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.5F, synthaxDelay.getCachedLevelValue());
    }


    /**
     * To test one index above the min value of Level.
     *  Test returned as OK.
     */
    @Test
    void testDelayLevelAboveMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setLevel(newValue.floatValue());
        };

        float t1 = 0.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.01F, synthaxDelay.getCachedLevelValue());
    }


    /**
     * To test one index at the min value of Level.
     * Test returned as OK.
     */
    @Test
    void testDelayLevelAtMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setLevel(newValue.floatValue());
        };

        float t1 = 0F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0, synthaxDelay.getCachedLevelValue());
    }

    /**
     * To test one index below the min value of Level.
     * Returns -0.01 when it's supposed to return 0.0. Unsure why.
     */
    @Test
    void testDelayLevelBelowMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setLevel(newValue.floatValue());
        };

        float t1 = -0.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0, synthaxDelay.getCachedLevelValue());
    }

    /**
     * To test if the delay is active.
     * Test returned as OK.
     */
    @Test
    void TestDelayIsActive() {
        synthaxDelay.setActive();
        assertTrue(synthaxDelay.getDelayIsActive());
    }

    /**
     * To test if the delay is not active.
     *  Test returned as OK.
     */
    @Test
    void TestDelayIsActiveFalse() {
        assertFalse(synthaxDelay.getDelayIsActive());
    }

}