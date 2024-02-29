package com.synthax.test.controller;

import com.synthax.model.effects.SynthaxEQFilters;
import com.synthax.model.effects.SynthaxLFO;
import com.synthax.model.enums.Waveforms;
import com.synthax.util.HelperMath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value = MockitoExtension.class)
public class EQFiltersValuesTest {

    private static final float HP_MIN_FREQ = 400f;
    private static final float HP_MAX_FREQ = 2000f;

    private static final float LP_MIN_FREQ = 100f;
    private static final float LP_MAX_FREQ = 1500f;
    SynthaxEQFilters synthaxEQFilters;

    @BeforeEach
    void setUp() {

        synthaxEQFilters = new SynthaxEQFilters();
    }

    /**
     * Section Tests for HPCutOff values
     * These values are related to the cutoff frequency of the values
     * in the 3 highpass-filters.
     * See private final BiquadFilter[] highPassFilters in SynthaxEQFilters
     * for more information about the highpass-filters.
     */

    /**
     * HPCutOff has a button to set it active or inactive.
     * This test tests setting the value with the maximum input value (1F).
     * The setter for HPCutoff has internal logic to map the input float
     * to a float frequency between 400f- 2000f.
     * results: float 2000.0 as expected.
     */

    @Test
    void testHPCutOffValueWhenActiveMax() {
        ObservableValue<Number> HPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setHPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setHPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, HP_MIN_FREQ, HP_MAX_FREQ);
        rateListener.changed(HPCutOffValue, 0, cutoff);

        assertEquals(mapped, synthaxEQFilters.getHPCutOff0());
        assertEquals(2000, synthaxEQFilters.getHPCutOff0());
    }
    /**
     * HPCutOff has a button to set it active or inactive.
     * This test tests setting the value with the minimum input value (0F).
     * The setter for HPCutoff has internal logic to map the input float
     * to a float frequency between 400f- 2000f.
     * results: float 400.0 as expected.
     */
    @Test
    void testHPCutOffValueWhenActiveMin() {
        ObservableValue<Number> HPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setHPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setHPCutoff(newValue.floatValue());
        };
        float cutoff = 0.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, HP_MIN_FREQ, HP_MAX_FREQ);
        rateListener.changed(HPCutOffValue, 0, cutoff);

        assertEquals(mapped, synthaxEQFilters.getHPCutOff0());
        assertEquals(400, synthaxEQFilters.getHPCutOff0());
    }

    /**
     * This test tests setting a value to HPCutoff when it is inactive.
     * The setter for HPCutoff has internal logic to set the value to float 50F
     * when HPCutoff is set to inactive.
     * However for the sake of saving/loading presets we always return
     * the enabled value, so the disabled value which would be 50f is never returned,
     * due to this logic:
     * if(hpActive) {
     *             return highPassFilters[0].getFrequency();
     *         } else {
     *             return savedHPCutoff;
     *         }
     * So the results of the tests were not as expected, but it makes sense.
     */
    @Test
    void testHPCutOffValueWhenNotActive() {
        ObservableValue<Number> HPCutOffValue = mock(ObservableValue.class);
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setHPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;

        rateListener.changed(HPCutOffValue, 0, cutoff);

        assertEquals(50f, synthaxEQFilters.getHPCutOff0());
    }

    /**
     * The test testHPCutOffIsSameHighPassFilters012() tests that the
     * cutoff frequency set by setHPCutoff is the same for
     * all three highpass-filters.
     * Results show that the cut-off frequency is the same for all
     * three filters.
     */


    @Test
    void testHPCutOffIsSameHighPassFilters012() {
        ObservableValue<Number> HPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setHPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setHPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, HP_MIN_FREQ, HP_MAX_FREQ);
        rateListener.changed(HPCutOffValue, 0, cutoff);

        assertEquals(synthaxEQFilters.getHPCutOff1(), synthaxEQFilters.getHPCutOff2());
        assertEquals(synthaxEQFilters.getHPCutOff0(), synthaxEQFilters.getHPCutOff2());

    }

    //section LPCutoff

    /**
     * LPCutOff has a button to set it active or inactive.
     * This test tests setting the value with the maximum input value (1F).
     * The setter for LPCutoff has internal logic to map the input float
     * to a float frequency between 100f- 1500f.
     * results: float 1500.0 as expected.
     */
    @Test
    void testLPCutOffValueWhenActiveMax() {
        ObservableValue<Number> LPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setLPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setLPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, LP_MIN_FREQ, LP_MAX_FREQ);
        rateListener.changed(LPCutOffValue, 0, cutoff);

        assertEquals(mapped, synthaxEQFilters.getLPCutOff0());
        assertEquals(1500, synthaxEQFilters.getLPCutOff0());
    }

    /**
     * LPCutOff has a button to set it active or inactive.
     * This test tests setting the value with the minimum input value (0F).
     * The setter for LPCutoff has internal logic to map the input float
     * to a float frequency between 100f- 1500f.
     * results: float 100f as expected.
     */
    @Test
    void testLPCutOffValueWhenActiveMin() {
        ObservableValue<Number> LPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setLPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setLPCutoff(newValue.floatValue());
        };
        float cutoff = 0.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, LP_MIN_FREQ, LP_MAX_FREQ);
        rateListener.changed(LPCutOffValue, 0, cutoff);

        assertEquals(mapped, synthaxEQFilters.getLPCutOff0());
        assertEquals(100, synthaxEQFilters.getLPCutOff0());

    }
    /**
     * This test tests setting a value to LPCutoff when it is inactive.
     * The setter for LPCutoff has internal logic to set the value to float 22000F
     * when LPCutoff is set to inactive.
     * However, for the sake of saving/loading presets we always return
     * the enabled value, so the disabled value which would be 22000f is never returned,
     * due to this logic:
     * if(lpActive) {
     *             return lowPassFilters[0].getFrequency();
     *         } else {
     *             return savedLPCutoff;
     *         }
     * So the results of the tests were not as expected, but it makes sense.
     */
    @Test
    void testLPCutOffValueWhenNotActive() {
        ObservableValue<Number> LPCutOffValue = mock(ObservableValue.class);
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setLPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;

        rateListener.changed(LPCutOffValue, 0, cutoff);

        assertEquals(22000f, synthaxEQFilters.getLPCutOff0());
    }

    /**
     * The test testLPCutOffIsSameLowPassFilters012() tests that the
     * cutoff frequency set by setLPCutoff is the same for
     * all three highpass-filters.
     * Results show that the cut-off frequency is the same for all
     * three filters.
     */


    @Test
    void testLPCutOffIsSameLowPassFilters012() {
        ObservableValue<Number> LPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setHPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setLPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, HP_MIN_FREQ, HP_MAX_FREQ);
        rateListener.changed(LPCutOffValue, 0, cutoff);

        assertEquals(synthaxEQFilters.getLPCutOff1(), synthaxEQFilters.getLPCutOff2());
        assertEquals(synthaxEQFilters.getLPCutOff0(), synthaxEQFilters.getLPCutOff2());

    }

}
