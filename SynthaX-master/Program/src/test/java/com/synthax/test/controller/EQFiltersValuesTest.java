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
    SynthaxEQFilters synthaxEQFilters;

    @BeforeEach
    void setUp() {

        synthaxEQFilters = new SynthaxEQFilters();
    }

    @Test
    void testHPCutOffValueWhenActive() {
        ObservableValue<Number> HPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setHPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setHPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, HP_MIN_FREQ, HP_MAX_FREQ);
        rateListener.changed(HPCutOffValue, 0, cutoff);

        assertEquals(mapped, synthaxEQFilters.getHPCutOff0());
    }

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

    @Test
    void testHPCutOffIsSameHighPassFilters01() {
        ObservableValue<Number> HPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setHPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setHPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, HP_MIN_FREQ, HP_MAX_FREQ);
        rateListener.changed(HPCutOffValue, 0, cutoff);

        assertEquals(synthaxEQFilters.getHPCutOff0(), synthaxEQFilters.getHPCutOff1());

    }

    @Test
    void testHPCutOffIsSameHighPassFilters12() {
        ObservableValue<Number> HPCutOffValue = mock(ObservableValue.class);
        synthaxEQFilters.setHPActive();
        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxEQFilters.setHPCutoff(newValue.floatValue());
        };
        float cutoff = 1.0F;
        float mapped = HelperMath.map(cutoff, 0f, 1f, HP_MIN_FREQ, HP_MAX_FREQ);
        rateListener.changed(HPCutOffValue, 0, cutoff);

        assertEquals(synthaxEQFilters.getHPCutOff1(), synthaxEQFilters.getHPCutOff2());

    }

}
