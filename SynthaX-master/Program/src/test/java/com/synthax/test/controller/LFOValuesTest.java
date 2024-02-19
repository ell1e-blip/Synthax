package com.synthax.test.controller;

import com.synthax.model.effects.SynthaxLFO;
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
class LFOValuesTest {



    SynthaxLFO synthaxLFO;

    private static final float MIN_RATE = 0.1f;
    private static final float MAX_RATE = 20f;


    @BeforeEach
    void setUp() {

        synthaxLFO = new SynthaxLFO();
    }

    @Test
    void testLFORateFrequency() {

        ObservableValue<Number> rateValue = mock(ObservableValue.class);

        ChangeListener<Number> rateListener = (v, oldValue, newValue) -> {
            synthaxLFO.setRate(newValue.floatValue());
        };
        float t1 = 1.0F;
        rateListener.changed(rateValue, 0, t1);

        float mapped = HelperMath.map(t1, 0f, 1f, MIN_RATE, MAX_RATE);
        assertEquals(mapped, synthaxLFO.getRateFrequency());

    }

    @Test
    void testLFODepth() {
        ObservableValue<Number> depthValue = mock(ObservableValue.class);

        ChangeListener<Number> depthListener = (v, oldValue, newValue) -> {
            synthaxLFO.setDepth(newValue.floatValue());
        };

        float t1 = 1.0F;
        depthListener.changed(depthValue, 0, t1);

        assertEquals(1.0, synthaxLFO.getDepthValue());
    }
}