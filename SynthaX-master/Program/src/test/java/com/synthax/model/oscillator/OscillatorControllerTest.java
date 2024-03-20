package com.synthax.model.oscillator;
import com.synthax.controller.OscillatorController;
import com.synthax.model.enums.CombineMode;
import com.synthax.model.enums.Waveforms;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Add;
import net.beadsproject.beads.ugens.Mult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Marcus Larsson
 * OscillatorControllerTest checks important functions and checks their result.
 * These tests are important for saving and loading presets.
 */
class OscillatorControllerTest {

    private OscillatorController controller;
    private OscillatorVoice mockVoice;
    private AudioContext mockContext;

    @BeforeEach
    void setUp() {
        mockContext = mock(AudioContext.class);
        AudioContext.setDefaultContext(mockContext);

        controller = new OscillatorController(1);

        mockVoice = mock(OscillatorVoice.class);

        controller.getVoices()[0] = mockVoice;

        when(mockVoice.getWavePlayerBuffer()).thenAnswer((Answer<Buffer>) invocation -> {
            return mock(Buffer.class);
        });
    }

    /**
     * @author Marcus Larsson
     * This method sets the detune value to a known rate (100.0f).
     * Then it returns the value to verify if the value is set correctly.
     */
    @Test
    void testSetDetuneCent() {
        float detuneCent = 100.0f;
        controller.setDetuneCent(detuneCent);
        verify(mockVoice, times(1)).updateDetuneValue(detuneCent, controller.getOctaveOperand());
    }

    /**
     * @author Marcus Larsson
     * This method sets the waveform value to a known enum.
     * Then it returns the value to verify if the value is set correctly.
     */
    @Test
    void testSetWaveform() {
        Waveforms testWaveform = Waveforms.SQUARE;
        controller.setWaveform(testWaveform);
        verify(mockVoice, times(1)).setWavePlayerBuffer(testWaveform.getBuffer());
    }
    /**
     * @author Marcus Larsson
     * This method sets the CombineMode value to a known enum.
     * Then it returns the value to verify if the value is set correctly.
     */
    @Test
    void testSetOutputTypeAdd() {
        CombineMode testCombineMode = CombineMode.ADD;
        controller.setOutputType(testCombineMode);
        assertTrue(controller.getOutput() instanceof Add);
    }

    /**
     * @author Marcus Larsson
     * This method sets the CombineMode value to a known enum.
     * Then it returns the value to verify if the value is set correctly.
     */
    @Test
    void testSetOutputTypeMult() {
        CombineMode testCombineMode = CombineMode.MULT;
        controller.setOutputType(testCombineMode);
        assertTrue(controller.getOutput() instanceof Mult);
    }


}
