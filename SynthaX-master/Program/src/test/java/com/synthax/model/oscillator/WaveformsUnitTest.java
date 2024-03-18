package com.synthax.model.oscillator;

import com.synthax.controller.OscillatorController;
import com.synthax.controller.VoiceController;
import com.synthax.model.enums.Waveforms;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit tests  for requirement [FK_A_OSC_002].
 * @author Edin Jahic
 */
public class WaveformsUnitTest {
    OscillatorController oscillatorController;
    OscillatorVoice oscillatorVoice;

    /**
     * Tests requirement [FK_S_OSC_002.1].
     *
     * [FK_S_OSC_002.1] Sine wave:
     * Oscillatorn ska kunna generera en sinusvåg.
     *
     */
    @Test
    public void testSine() {
        oscillatorController = new OscillatorController(VoiceController.VOICE_COUNT);
        oscillatorVoice = new OscillatorVoice(null, oscillatorController, 0);

        oscillatorVoice.setWavePlayerBuffer(Waveforms.SINE.getBuffer());
        assertEquals(Waveforms.SINE.getBuffer(), oscillatorVoice.getWavePlayerBuffer());

    }

    /**
     * Tests requirement [FK_S_OSC_002.2].
     *
     * [FK_S_OSC_002.2] Triangle wave:
     * Oscillatorn ska kunna generera en triangelvåg.
     *
     */
    @Test
    public void testTriangle() {
        oscillatorController = new OscillatorController(VoiceController.VOICE_COUNT);
        oscillatorVoice = new OscillatorVoice(null, oscillatorController, 0);

        oscillatorVoice.setWavePlayerBuffer(Waveforms.TRIANGLE.getBuffer());
        assertEquals(Waveforms.TRIANGLE.getBuffer(), oscillatorVoice.getWavePlayerBuffer());

    }

    /**
     * Tests requirement [FK_S_OSC_002.3].
     *
     * [FK_S_OSC_002.3] Sawtooth:
     * Oscillatorn ska kunna generera en sågtandsvåg.
     *
     */
    @Test
    public void testSaw() {
        oscillatorController = new OscillatorController(VoiceController.VOICE_COUNT);
        oscillatorVoice = new OscillatorVoice(null, oscillatorController, 0);

        oscillatorVoice.setWavePlayerBuffer(Waveforms.SAWTOOTH.getBuffer());
        assertEquals(Waveforms.SAWTOOTH.getBuffer(), oscillatorVoice.getWavePlayerBuffer());

    }

    /**
     * Tests requirement [FK_A_OSC_002.4].
     *
     * [FK_ S_OSC_002.4] Square wave:
     * Oscillatorn ska kunna generera en fyrkantsvåg.
     *
     */
    @Test
    public void testSquare() {
        oscillatorController = new OscillatorController(VoiceController.VOICE_COUNT);
        oscillatorVoice = new OscillatorVoice(null, oscillatorController, 0);

        oscillatorVoice.setWavePlayerBuffer(Waveforms.SQUARE.getBuffer());
        assertEquals(Waveforms.SQUARE.getBuffer(), oscillatorVoice.getWavePlayerBuffer());

    }
}
