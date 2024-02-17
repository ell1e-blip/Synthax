package com.synthax.test.controller;

import com.synthax.controller.OscillatorController;
import com.synthax.controller.SynthaxController;

import com.synthax.view.SynthaxView;
import com.synthax.view.controls.KnobBehavior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito.*;

@ExtendWith(value = MockitoExtension.class)
class OscillatorControllerTest {

    SynthaxController controller;

    SynthaxView synthaxView;

    @BeforeEach
    void setUp() {
        controller = mock(SynthaxController.class);
        synthaxView = mock(SynthaxView.class);
    }

    @Test
    void testDelay() {
        Slider knobLFORate = new Slider();
        KnobBehavior mockKnobLFORate = mock(KnobBehavior.class);

        knobLFORate.setOnMouseDragged(mockKnobLFORate);
        mockKnobLFORate.knobValueProperty().addListener((v, oldValue, newValue) -> {
            controller.setLFORate(newValue.floatValue());
        });

        ObservableValue<Number> rateValue = mock(ObservableValue.class);

        ChangeListener<Number> rateListener = (obs, oldValue, newValue) -> {
            controller.setLFORate(newValue.floatValue());
        };

        rateListener.changed(rateValue, 0, 1);

        assertEquals(1, controller());

    }
}