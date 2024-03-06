package com.synthax.model.effects;

import com.synthax.MainApplication;
import com.synthax.controller.OscillatorManager;
import com.synthax.model.effects.SynthaxADSR;
import com.synthax.model.effects.SynthaxEQFilters;
import com.synthax.model.effects.SynthaxLFO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import net.beadsproject.beads.ugens.Gain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ADSRValuesTest {
    SynthaxEQFilters filters;
    SynthaxADSR synthaxADSR;
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

        synthaxADSR = new SynthaxADSR();
    }

    /*** --------- Methods belows are for testing Attack using Boundary Value Analysis --------- */
    /**

     /**
     * To test one index above the max value of Attack.
     * Returns 3000.01F although I've explicitly stated that if its above 3000, return 3000. Must be the decimal.
     */
    @Test
    void testADSRAttackAboveMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setAttackValue(newValue.floatValue());
        };

        float t1 = 3000.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 3000) {
            t1 = 3000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(3000, synthaxADSR.getAttackValue());
    }

    /**
     * To test with the max value of Attack.
     * Test case OK.
     */
    @Test
    void testADSRAttackAtMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setAttackValue(newValue.floatValue());
        };

        float t1 = 3000;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 3000) {
            t1 = 3000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(3000, synthaxADSR.getAttackValue());
    }

    /**
     * To test one index below the max value of Attack.
     *  Test case OK.
     */
    @Test
    void testADSRAttackBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setAttackValue(newValue.floatValue());
        };

        float t1 = 2999.99F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 3000) {
            t1 = 3000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(2999.99F, synthaxADSR.getAttackValue());
    }

    /**
     * To test one index at the middle value of Attack.
     * Test case OK.
     */
    @Test
    void testADSRAttackMiddle() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setAttackValue(newValue.floatValue());
        };

        float t1 = 1500F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 3000) {
            t1 = 3000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(1500F, synthaxADSR.getAttackValue());
    }

    /**
     * To test one index above the min value of Attack.
     *  Test case OK.
     */
    @Test
    void testADSRAttackAboveMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setAttackValue(newValue.floatValue());
        };

        float t1 = 10.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 3000) {
            t1 = 3000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(10.01F, synthaxADSR.getAttackValue());
    }

    /**
     * To test one index at the min value of Attack.
     * Test case OK.
     */
    @Test
    void testADSRAttackAtMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setAttackValue(newValue.floatValue());
        };

        float t1 = 10F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 3000) {
            t1 = 3000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(10, synthaxADSR.getAttackValue());
    }

    /**
     * To test one index below the min value of Attack.
     * Returns 9.99F although I've explicitly stated that if its below 10, return 10. Must be a decimal.
     */
    @Test
    void testADSRAttackBelowMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setAttackValue(newValue.floatValue());
        };

        float t1 = 9.99F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 3000) {
            t1 = 3000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(10, synthaxADSR.getAttackValue());
    }

    /**
     * --------- Methods belows are for testing Decay using Boundary Value Analysis ---------
     */

    /**
     * To test one index above the max value of Decay.
     * Same issue as above, it returns values above the max value as OK due to the decimal. This time it returned 1500.01F when it was supposed to return 1500.
     */
    @Test
    void testADSRDecayAboveMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setDecayValue(newValue.floatValue());
        };

        float t1 = 1500.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1500) {
            t1 = 1500;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(1500, synthaxADSR.getDecayValue());
    }

    /**
     * To test one index at the max value of Decay.
     * Test case OK.
     */
    @Test
    void testADSRDecayAtMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setDecayValue(newValue.floatValue());
        };

        float t1 = 1500;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1500) {
            t1 = 1500;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(1500, synthaxADSR.getDecayValue());
    }


    /**
     * To test one index below the max value of Decay.
     * Test case OK.
     */
    @Test
    void testADSRDecayBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setDecayValue(newValue.floatValue());
        };

        float t1 = 1499.99F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1500) {
            t1 = 1500;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(1499.99F, synthaxADSR.getDecayValue());
    }

    /**
     * To test one index at the middle value of Decay.
     * Test case OK.
     */
    @Test
    void testADSRDecayMiddle() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setDecayValue(newValue.floatValue());
        };

        float t1 = 750F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1500) {
            t1 = 1500;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(750, synthaxADSR.getDecayValue());
    }

    /**
     * To test one index above the min value of Decay.
     * Test case OK.
     */
    @Test
    void testADSRDecayAboveMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setDecayValue(newValue.floatValue());
        };

        float t1 = 10.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1500) {
            t1 = 1500;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(10.01F, synthaxADSR.getDecayValue());
    }

    /**
     * To test one index at the min value of Decay.
     * Test case OK.
     */
    @Test
    void testADSRDecayAtMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setDecayValue(newValue.floatValue());
        };

        float t1 = 10F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1500) {
            t1 = 1500;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(10, synthaxADSR.getDecayValue());
    }

    /**
     * To test one index below the min value of Decay.
     * Returned the value 9.989999771118164F when it was supposed to return 10. Why it returned random decimals is odd.
     */
    @Test
    void testADSRDecayBelowMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setDecayValue(newValue.floatValue());
        };

        float t1 = 9.99F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1500) {
            t1 = 1500;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(9.99, synthaxADSR.getDecayValue());
    }

    /**
     * --------- Methods belows are for testing Sustain using Boundary Value Analysis ---------
     */

    /**
     * To test one index above the max value of Sustain.
     * Same issue as above, it returns values above the max value as OK due to the decimal. This time it returned 1.01F when it was supposed to return 1.
     */
    @Test
    void testADSRSustainAboveMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setSustainValue(newValue.floatValue());
        };

        float t1 = 1.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(1, synthaxADSR.getSustainValue());
    }

    /**
     * To test one index at the max value of Sustain.
     * Returned 750 for some reason?? It's max value is 1 so where it got 750 I am very unsure. Can be interesting to note.
     */
    @Test
    void testADSRSustainMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setSustainValue(newValue.floatValue());
        };

        float t1 = 1;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(1, synthaxADSR.getDecayValue());
    }

    /**
     * To test one index below the max value of Sustain.
     * Test case OK.
     */
    @Test
    void testADSRSustainBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setSustainValue(newValue.floatValue());
        };

        float t1 = 0.99F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.99F, synthaxADSR.getSustainValue());
    }

    /**
     * To test one index at the middle value of Sustain.
     * Test case OK.
     */
    @Test
    void testADSRSustainMiddle() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setSustainValue(newValue.floatValue());
        };

        float t1 = 0.5F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.5F, synthaxADSR.getSustainValue());
    }

    /**
     * To test one index above the min value of Sustain.
     * Test case OK.
     */
    @Test
    void testADSRSustainAboveMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setSustainValue(newValue.floatValue());
        };

        float t1 = 0.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0.01F, synthaxADSR.getSustainValue());
    }

    /**
     * To test one index at the min value of Sustain.
     * Test case OK.
     */
    @Test
    void testADSRSustainAtMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setSustainValue(newValue.floatValue());
        };

        float t1 = 0F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0, synthaxADSR.getSustainValue());
    }

    /**
     * To test one index below the min value of Sustain.
     * Returned the value -0.01F when it was supposed to return 0.
     */
    @Test
    void testADSRSustainBelowMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setSustainValue(newValue.floatValue());
        };

        float t1 = -0.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }

        assertEquals(0, synthaxADSR.getSustainValue());
    }

    /**
     * --------- Methods belows are for testing Release using Boundary Value Analysis ---------
     */

    /**
     * To test one index above the max value of Release.
     * Same issue as above, it returns values above the max value as OK due to the decimal. This time it returned 2000.01F when it was supposed to return 2000.
     */
    @Test
    void testADSRReleaseAboveMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setReleaseValue(newValue.floatValue());
        };

        float t1 = 2000.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2000) {
            t1 = 2000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(2000, synthaxADSR.getReleaseValue());
    }

    /**
     * To test one index at the max value of Release.
     * Test case OK.
     */
    @Test
    void testADSRReleaseMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setReleaseValue(newValue.floatValue());
        };

        float t1 = 2000;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2000) {
            t1 = 2000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(2000, synthaxADSR.getReleaseValue());
    }

    /**
     * To test one index below the max value of Release.
     * Test case OK.
     */
    @Test
    void testADSRReleaseBelowMax() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setReleaseValue(newValue.floatValue());
        };

        float t1 = 1999.9F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2000) {
            t1 = 2000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(1999.9F, synthaxADSR.getReleaseValue());
    }

    /**
     * To test one index at the middle value of Release.
     * Test case OK.
     */
    @Test
    void testADSRReleaseMiddle() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setReleaseValue(newValue.floatValue());
        };

        float t1 = 1000F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2000) {
            t1 = 2000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(1000F, synthaxADSR.getReleaseValue());
    }


    /**
     * To test one index above the min value of Release.
     * Test case OK.
     */
    @Test
    void testADSRReleaseAboveMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setReleaseValue(newValue.floatValue());
        };

        float t1 = 10.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2000) {
            t1 = 2000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(10.01F, synthaxADSR.getReleaseValue());
    }


    /**
     * To test one index at the min value of Release.
     * Test case OK.
     */
    @Test
    void testADSRReleaseAtMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setReleaseValue(newValue.floatValue());
        };

        float t1 = 10F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 2000) {
            t1 = 2000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(10, synthaxADSR.getReleaseValue());
    }

    /**
     * To test one index below the min value of Release.
     * Returned the value 9.99F when it was supposed to return 10.
     */
    @Test
    void testADSRReleaseBelowMin() {
        ObservableValue<Number> sizeValue = mock(ObservableValue.class);
        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxADSR.setReleaseValue(newValue.floatValue());
        };

        float t1 = 9.99F;
        sizeListener.changed(sizeValue, 10, t1);

        if (t1 > 2000) {
            t1 = 2000;
        } else if (t1 < 10) {
            t1 = 10;
        }

        assertEquals(10, synthaxADSR.getReleaseValue());
    }
}