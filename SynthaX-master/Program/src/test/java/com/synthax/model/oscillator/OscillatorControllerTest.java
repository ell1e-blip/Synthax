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

    @Test
    void testSetDetuneCent() {
        float detuneCent = 100.0f;
        controller.setDetuneCent(detuneCent);
        verify(mockVoice, times(1)).updateDetuneValue(detuneCent, controller.getOctaveOperand());
    }
    @Test
    void testSetWaveform() {
        // Given
        Waveforms testWaveform = Waveforms.SQUARE; // Assume this is a valid enum value

        // When
        controller.setWaveform(testWaveform);

        // Then
        verify(mockVoice, times(1)).setWavePlayerBuffer(testWaveform.getBuffer());
    }

    @Test
    void testSetOutputTypeAdd() {
        // Given
        CombineMode testCombineMode = CombineMode.ADD; // Assume this is a valid enum value

        // When
        controller.setOutputType(testCombineMode);

        // Then
        assertTrue(controller.getOutput() instanceof Add);
    }

    @Test
    void testSetOutputTypeMult() {
        CombineMode testCombineMode = CombineMode.MULT;

        controller.setOutputType(testCombineMode);

        assertTrue(controller.getOutput() instanceof Mult);
    }




}
