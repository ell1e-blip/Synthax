package com.synthax.test.controller;

import com.synthax.controller.OscillatorController;
import com.synthax.controller.VoiceController;
import com.synthax.model.oscillator.OscillatorVoice;
import com.synthax.model.oscillator.Voice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OscillatorControllerTest {

    OscillatorController controller;

    @BeforeEach
    void setUp() {
        controller = new OscillatorController(VoiceController.VOICE_COUNT);
    }

    @Test
    void testDelay() {

        controller.setDelayTime(1.0f);
        for (Voice voice : controller.getVoices()) {
            assertEquals(1000,((OscillatorVoice)voice).getDelay().getCachedDelayTime());
        }

        controller.setDelayTime(0.0f);
        for (Voice voice : controller.getVoices()) {
            assertEquals(100,((OscillatorVoice)voice).getDelay().getCachedDelayTime());
        }
    }
}