package com.synthax.model.effects;

import com.synthax.MainApplication;
import com.synthax.controller.OscillatorManager;
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
     */
    @Test
    void testDelayFeedbackBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxDelay.setFeedbackDuration(newValue.floatValue());
        };

        float t1 = 2499.99F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2500) {
            t1 = 2500;
        } else if (t1 < 100) {
            t1 = 100;
        }

        assertEquals(2499.99F, synthaxDelay.getFeedbackDuration());
    }

    /**
     * To test one index at the middle value of Feedback.
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
     */
    @Test
    void TestDelayIsActive() {
        synthaxDelay.setActive();
        assertTrue(synthaxDelay.getDelayIsActive());
    }

    /**
     * To test if the delay is not active.
     */
    @Test
    void TestDelayIsActiveFalse() {
        assertFalse(synthaxDelay.getDelayIsActive());
    }

}