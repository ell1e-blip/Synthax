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
    void testReverbSizeMiddle()  {

        ObservableValue<Number> sizeValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 0.5F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(0.5, synthaxReverb.getReverbSize());
    }

    @Test
    void testReverbSizeMin()  {

        ObservableValue<Number> sizeValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 0.01F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(0.01F, synthaxReverb.getReverbSize());
    }

    @Test
    void testReverbSizeBelowMin()  {

        ObservableValue<Number> sizeValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 0.0F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(0.01F, synthaxReverb.getReverbSize());
    }

    @Test
    void testReverbSizeAboveMin()  {

        ObservableValue<Number> sizeValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 0.02F;
        sizeListener.changed(sizeValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(0.02F, synthaxReverb.getReverbSize());
    }

    @Test
    void testReverbSizeMax()  {

        ObservableValue<Number> rateValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 1;
        sizeListener.changed(rateValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(1, synthaxReverb.getReverbSize());
    }

    @Test
    void testReverbSizeBelowMax()  {

        ObservableValue<Number> rateValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 0.99F;
        sizeListener.changed(rateValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(0.99F, synthaxReverb.getReverbSize());
    }

    @Test
    void testReverbSizeABoveMax()  {

        ObservableValue<Number> rateValue = mock(ObservableValue.class);

        ChangeListener<Number> sizeListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbSize(newValue.floatValue());
        };
        float t1 = 1.01F;
        sizeListener.changed(rateValue, 0, t1);

        if (t1 > 1) {
            t1 = 1;
        } else if (t1 < 0.01) {
            t1 = .01f;
        }


        assertEquals(1, synthaxReverb.getReverbSize());
    }

    // reverb tone tests below
    @Test
    void TestReverbToneMiddle() {
        ObservableValue<Number> toneValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbTone(newValue.floatValue());
        };
        float t1 = 0.5F;
        toneListener.changed(toneValue, 0, t1);

        if (t1 < 0)
            t1 = 0;
        else if (t1 > 1)
            t1 = 1;
        t1 = t1;
        assertEquals(0.5F, synthaxReverb.getReverbTone());
    }

    @Test
    void TestReverbToneMin() {
        ObservableValue<Number> toneValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbTone(newValue.floatValue());
        };
        float t1 = 0;
        toneListener.changed(toneValue, 0, t1);

        if (t1 < 0)
            t1 = 0;
        else if (t1 > 1)
            t1 = 1;
        t1 = t1;
        assertEquals(0F, synthaxReverb.getReverbTone());
    }

    @Test
    void TestReverbToneBelowMin() {
        ObservableValue<Number> toneValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbTone(newValue.floatValue());
        };
        float t1 = -0.01F;
        toneListener.changed(toneValue, 0, t1);

        if (t1 < 0)
            t1 = 0;
        else if (t1 > 1)
            t1 = 1;
        t1 = t1;
        assertEquals(0F, synthaxReverb.getReverbTone());
    }

    @Test
    void TestReverbToneAboveMin() {
        ObservableValue<Number> toneValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbTone(newValue.floatValue());
        };
        float t1 = 0.01F;
        toneListener.changed(toneValue, 0, t1);

        if (t1 < 0)
            t1 = 0;
        else if (t1 > 1)
            t1 = 1;
        t1 = t1;
        assertEquals(0.01F, synthaxReverb.getReverbTone());
    }

    @Test
    void TestReverbToneMax() {
        ObservableValue<Number> toneValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbTone(newValue.floatValue());
        };
        float t1 = 1F;
        toneListener.changed(toneValue, 0, t1);

        if (t1 < 0)
            t1 = 0;
        else if (t1 > 1)
            t1 = 1;
        t1 = t1;
        assertEquals(1F, synthaxReverb.getReverbTone());
    }

    @Test
    void TestReverbToneBelowMax() {
        ObservableValue<Number> toneValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbTone(newValue.floatValue());
        };
        float t1 = 0.99F;
        toneListener.changed(toneValue, 0, t1);

        if (t1 < 0)
            t1 = 0;
        else if (t1 > 1)
            t1 = 1;
        t1 = t1;
        assertEquals(0.99F, synthaxReverb.getReverbTone());
    }

    @Test
    void TestReverbToneAboveMax() {
        ObservableValue<Number> toneValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbTone(newValue.floatValue());
        };
        float t1 = 1.01F;
        toneListener.changed(toneValue, 0, t1);

        if (t1 < 0)
            t1 = 0;
        else if (t1 > 1)
            t1 = 1;
        t1 = t1;
        assertEquals(1F, synthaxReverb.getReverbTone());
    }
    //test reverb amount below
    @Test
    void TestReverbAmountMiddle() {
        ObservableValue<Number> amountValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbAmount(newValue.floatValue());
        };
        float t1 = 0.5F;
        toneListener.changed(amountValue, 0, t1);

        assertEquals(0.5F, synthaxReverb.getReverbAmount());
    }

    /**
     * @Author Ellie Rosander
     * TestReverbAmountBelowMin has an intresting result,
     * as oppossed to the tone and size, there is no algorithm
     * in SynthaxReverb to check that the float parameter is
     * within certain values.
     * This results in that a float input of -0.01F will return
     * 0.01F from getReverbAmount().
     * However, in practice the knob that the listener changing this
     * value is tied to should only be able to have values between 0-1F.
     * Were the knob to somehow have a value outside 0-1F this could cause
     * some unexpected behaviour and so this should be considered going forward.
     *
     */
    @Test
    void TestReverbAmountBelowMin() {
        ObservableValue<Number> amountValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbAmount(newValue.floatValue());
        };
        float t1 = -0.01F;
        toneListener.changed(amountValue, 0, t1);

        assertEquals(-0.01F, synthaxReverb.getReverbAmount());
    }

    @Test
    void TestReverbAmountAboveMax() {
        ObservableValue<Number> amountValue = mock(ObservableValue.class);

        ChangeListener<Number> toneListener = (v, oldValue, newValue) -> {
            synthaxReverb.setReverbAmount(newValue.floatValue());
        };
        float t1 = 2.0F;
        toneListener.changed(amountValue, 0, t1);

        assertEquals(2.0F, synthaxReverb.getReverbAmount());
    }

    //tests for boolean isActive below

    @Test
    void TestReverbIsActive() {
        synthaxReverb.setActive();

        assertTrue(synthaxReverb.getReverbIsActive());
    }

    @Test
    void TestReverbIsActiveFalse() {
        assertFalse(synthaxReverb.getReverbIsActive());
    }

}
