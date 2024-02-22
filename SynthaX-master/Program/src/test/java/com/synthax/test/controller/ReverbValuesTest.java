package com.synthax.test.controller;
import com.synthax.MainApplication;
import com.synthax.controller.OscillatorManager;
import com.synthax.controller.SynthaxController;
import com.synthax.model.effects.SynthaxEQFilters;
import com.synthax.model.effects.SynthaxLFO;
import com.synthax.model.effects.SynthaxReverb;
import com.synthax.model.enums.Waveforms;
import com.synthax.util.HelperMath;
import net.beadsproject.beads.ugens.Gain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(value = MockitoExtension.class)
public class ReverbValuesTest {

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

    @Test
    void testLFORateFrequencyMiddle()  {

        ObservableValue<Number> rateValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 0.5F;
        sizeListener.changed(rateValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(0.5, synthaxReverb.getReverbSize());
    }

    @Test
    void testLFORateFrequencyMin()  {

        ObservableValue<Number> rateValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 0.0F;
        sizeListener.changed(rateValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(0.01F, synthaxReverb.getReverbSize());
    }
}
